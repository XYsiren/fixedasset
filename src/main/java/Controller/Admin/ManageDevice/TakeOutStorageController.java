package Controller.Admin.ManageDevice;

import Dao.DeviceDao;
import Dao.UserDao;
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
import java.sql.SQLException;

@WebServlet("/takeout-storage")
public class TakeOutStorageController extends HttpServlet {
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

        String deviceID = null;
        String userID = null;
        String username = null;
        String adminname = null;

        // 解析 JSON 数据
        try {
            JsonObject jsonObject = JsonParser.parseString(json.toString()).getAsJsonObject();

            // 获取字段
            if (jsonObject.has("deviceID") && !jsonObject.get("deviceID").isJsonNull()) {
                deviceID = jsonObject.get("deviceID").getAsString();
            }
            if (jsonObject.has("userID") && !jsonObject.get("userID").isJsonNull()) {
                userID = jsonObject.get("userID").getAsString();
            }
            if (jsonObject.has("username") && !jsonObject.get("username").isJsonNull()) {
                username = jsonObject.get("username").getAsString();
            }
            if (jsonObject.has("adminname") && !jsonObject.get("adminname").isJsonNull()) {
                adminname = jsonObject.get("adminname").getAsString();
            }
            //验证
            System.out.println("selectedDeviceID: " + deviceID);
            System.out.println("userID: " + userID);
            System.out.println("username: " + username );
            System.out.println("adminname: " + adminname );

        } catch (Exception e) {
            // 捕获 JSON 解析错误
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 错误
            response.getWriter().write("Invalid JSON format: " + e.getMessage());
            return;
        }

        // 创建返回的 JSON 对象
        JsonObject jsonResponse = new JsonObject();
        DeviceDao deviceDao = new DeviceDao();
        UserDao userDao = new UserDao();
        boolean ok = false;
        //把String类型的id转成int
        int userid = Integer.parseInt(userID);
        User user = userDao.findByIdAndName(userid, username);

        // 如果 action 是 delete，就执行删除操作
        if (deviceID != null && user != null && adminname != null) {
            int deviceid = Integer.parseInt(deviceID);
            ok = deviceDao.updateDeviceTakeoutStatus(deviceid,"离库",userid,adminname);
            if (ok) {
                // 离库成功
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "takeout storage successful");
            } else {
                // 失败
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "takeout storage failed");
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
