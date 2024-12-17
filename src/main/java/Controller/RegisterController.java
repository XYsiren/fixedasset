package Controller;

import Dao.AdminDao;
import Dao.UserDao;
import Entity.Admin;
import Entity.User;
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

@WebServlet(name = "RegisterController", value = "/register")
public class RegisterController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
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

        String role = null;
        String username = null;
        String password = null;
        String email = null;
        String phone = null;

        // 解析 JSON 数据
        try {
            JsonObject jsonObject = JsonParser.parseString(json.toString()).getAsJsonObject();

            // 获取字段
            role = jsonObject.get("role").getAsString();
            username = jsonObject.get("username").getAsString();
            password = jsonObject.get("password").getAsString();
            email = jsonObject.get("email").getAsString();
            phone = jsonObject.get("phone").getAsString();

            // 进一步处理（例如验证登录等）
            System.out.println("Role: " + role);
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
            System.out.println("email: " + email);
            System.out.println("phone: " + phone);

        } catch (Exception e) {
            // 捕获 JSON 解析错误
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 错误
            response.getWriter().write("Invalid JSON format: " + e.getMessage());
        }

        // 创建返回的 JSON 对象
        JsonObject jsonResponse = new JsonObject();

        //根据不同角色查找不同表进行查询
        if ("user".equals(role)) {
            // 用户注册处理
            UserDao userDao = new UserDao();
            // 检查用户名是否已存在
            if (userDao.exists(username)) {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Username already exists");
            } else {
                // 调用 addUser 注册用户
                User newUser = userDao.addUser(username, password, email, phone,"","");

                if (newUser != null) {
                    jsonResponse.addProperty("success", true);
                    jsonResponse.addProperty("message", "User registered successfully");
                } else {
                    jsonResponse.addProperty("success", false);
                    jsonResponse.addProperty("message", "Failed to register user");
                }
            }
        }else if ("admin".equals(role)) {
            // 管理员注册处理
            AdminDao adminDao = new AdminDao();
            // 检查管理员是否已存在
            if (adminDao.exists(username)) {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Username already exists");
            } else {
                Admin newUser = adminDao.addAdmin(username, password, email, phone);

                if (newUser != null) {
                    jsonResponse.addProperty("success", true);
                    jsonResponse.addProperty("message", "User registered successfully");
                } else {
                    jsonResponse.addProperty("success", false);
                    jsonResponse.addProperty("message", "Failed to register user");
                }
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
