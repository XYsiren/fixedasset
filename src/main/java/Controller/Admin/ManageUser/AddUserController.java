package Controller.Admin.ManageUser;

import Dao.UserDao;
import Entity.User;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/add-user")
public class AddUserController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置请求和响应的字符编码为UTF-8
        response.setContentType("application/json;charset=utf-8");
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // 读取请求体
        BufferedReader reader = request.getReader();
        StringBuilder json = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            json.append(line);
        }
        // 确保请求体不为空
        if (json.length() == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 错误
            response.getWriter().write("Request body is empty");
            return;
        }

        String username = null;
        String password = null;
        String email = null;
        String phone = null;

        // 解析 JSON 数据
        try {
            JsonObject jsonObject = JsonParser.parseString(json.toString()).getAsJsonObject();

            // 获取字段
            if (jsonObject.has("username") && !jsonObject.get("username").isJsonNull()) {
                username = jsonObject.get("username").getAsString();
            }
            if (jsonObject.has("password") && !jsonObject.get("password").isJsonNull()) {
                password = jsonObject.get("password").getAsString();
            }
            if (jsonObject.has("email") && !jsonObject.get("email").isJsonNull()) {
                email = jsonObject.get("email").getAsString();
            }
            if (jsonObject.has("phone") && !jsonObject.get("phone").isJsonNull()) {
                phone = jsonObject.get("phone").getAsString();
            }
            //验证
            System.out.println("username: " + username);
            System.out.println("email: " + email);
            System.out.println("phone: " + phone );
            System.out.println("password: " + password );
        } catch (Exception e) {
            // 捕获 JSON 解析错误
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 错误
            response.getWriter().write("Invalid JSON format: " + e.getMessage());
            return;
        }

        // 创建返回的 JSON 对象
        JsonObject jsonResponse = new JsonObject();
        UserDao userDao = new UserDao();
        User newuser = null;

        if (username != null && password != null) {
            newuser = userDao.addUser(username, password, email, phone, "", "");
            if (newuser != null) {
                // 转换为 JSON 数组
                Gson gson = new Gson();
                JsonObject user = gson.toJsonTree(newuser).getAsJsonObject();
                // 添加成功
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "add user successful");
                jsonResponse.add("user", user);
            } else {
                // 删除失败
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "add user failed");
            }
        }
        // 返回 JSON 响应
        out.write(jsonResponse.toString());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
