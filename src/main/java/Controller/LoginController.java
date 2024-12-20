package Controller;

import Dao.AdminDao;
import Dao.UserDao;
import Entity.Admin;
import Entity.User;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet(name = "LoginController", value = "/login")
public class LoginController extends HttpServlet {

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

        // 解析 JSON 数据
        try {
            JsonObject jsonObject = JsonParser.parseString(json.toString()).getAsJsonObject();

            // 获取字段
            role = jsonObject.get("role").getAsString();
            username = jsonObject.get("username").getAsString();
            password = jsonObject.get("password").getAsString();

            // 进一步处理（例如验证登录等）
            System.out.println("Role: " + role);
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);

        } catch (Exception e) {
            // 捕获 JSON 解析错误
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 错误
            response.getWriter().write("Invalid JSON format: " + e.getMessage());
        }

        // 创建返回的 JSON 对象
        JsonObject jsonResponse = new JsonObject();

        //根据不同角色查找不同表进行查询
        if ("user".equals(role)) {
            // 查询数据库中的用户
            UserDao userDao = new UserDao();
            User user = userDao.findByName(username);

            // 验证用户名和密码 并确保用户未被删除
            if (user != null && user.getPassword().equals(password) && user.getIsdeleted().isEmpty()) {
                // 登录成功
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "Login successful");
                jsonResponse.addProperty("username", user.getUsername()); // Include username in the response
            } else {
                // 登录失败
                jsonResponse.addProperty("success", false);
                if (user == null) {
                    jsonResponse.addProperty("message", "Username not found");
                } else {
                    jsonResponse.addProperty("message", "Incorrect password or this user is deleted");
                }
            }

        } else if ("admin".equals(role)) {
            AdminDao adminDao = new AdminDao();
            Admin admin = null;
            try {
                admin = adminDao.findByName(username);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (admin != null && admin.getPassword().equals(password)) {
                // 登录成功
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "Admin Login successful");
                jsonResponse.addProperty("username", admin.getAdminName()); // Include username in the response
            } else {
                // 登录失败
                jsonResponse.addProperty("success", false);
                if (admin == null) {
                    jsonResponse.addProperty("message", "Username not found");
                } else {
                    jsonResponse.addProperty("message", "Incorrect password");
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
