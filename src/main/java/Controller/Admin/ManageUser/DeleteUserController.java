package Controller.Admin.ManageUser;

import Dao.DeviceBorrowDao;
import Dao.DeviceDao;
import Dao.UserDao;
import Entity.Device;
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
import java.util.List;

@WebServlet("/delete-user")
public class DeleteUserController extends HttpServlet {
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

        String action = null;
        String userToDelete = null;
        String adminname = null;

        // 解析 JSON 数据
        try {
            JsonObject jsonObject = JsonParser.parseString(json.toString()).getAsJsonObject();

            // 获取字段
            if (jsonObject.has("action") && !jsonObject.get("action").isJsonNull()) {
                action = jsonObject.get("action").getAsString();
            }
            if (jsonObject.has("userToDelete") && !jsonObject.get("userToDelete").isJsonNull()) {
                userToDelete = jsonObject.get("userToDelete").getAsString();
            }
            if (jsonObject.has("username") && !jsonObject.get("username").isJsonNull()) {
                adminname = jsonObject.get("username").getAsString();
            }
            //验证
            System.out.println("action: " + action);
            System.out.println("userToDeleted: " + userToDelete);
            System.out.println("adminname: " + adminname );

        } catch (Exception e) {
            // 捕获 JSON 解析错误
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 错误
            response.getWriter().write("Invalid JSON format: " + e.getMessage());
            return;
        }

        // 创建返回的 JSON 对象
        JsonObject jsonResponse = new JsonObject();
        UserDao userDao = new UserDao();
        boolean ok = false;

        // 如果 action 是 delete，就执行删除操作
        if ("delete".equals(action) && userToDelete != null && adminname != null) {
            //把String类型的id转成int
            int userID = Integer.parseInt(userToDelete);
            try {
                ok = userDao.deleteUser(userID,"1",adminname);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (ok) {
                // 删除成功
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "delete user successful");
            } else {
                // 删除失败
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "delete user failed");
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
