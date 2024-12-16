package Controller.User;

import Dao.DeviceBorrowDao;
import Dao.DeviceDao;
import Dao.UserDao;
import Entity.Device;
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


@WebServlet("/borrow-device")
public class BorrowDeviceController extends HttpServlet {
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
        String deviceId = null;
        String borrowPeriod = null;

        // 解析 JSON 数据
        try {
            JsonObject jsonObject = JsonParser.parseString(json.toString()).getAsJsonObject();

            // 获取字段
            if (jsonObject.has("deviceId") && !jsonObject.get("deviceId").isJsonNull()) {
                deviceId = jsonObject.get("deviceId").getAsString();
            }
            if (jsonObject.has("username") && !jsonObject.get("username").isJsonNull()) {
                username = jsonObject.get("username").getAsString();
            }
            if (jsonObject.has("borrowPeriod") && !jsonObject.get("borrowPeriod").isJsonNull()) {
                borrowPeriod = jsonObject.get("borrowPeriod").getAsString();
            }
            //验证
            System.out.println("Username: " + username);
            System.out.println("deviceId: " + deviceId);
            System.out.println("borrowPeriod: " + borrowPeriod +"天");

        } catch (Exception e) {
            // 捕获 JSON 解析错误
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 错误
            response.getWriter().write("Invalid JSON format: " + e.getMessage());
            return;
        }

        UserDao userDao = new UserDao();
        User user = userDao.findByName(username);

        // 创建返回的 JSON 对象
        JsonObject jsonResponse = new JsonObject();
        DeviceDao deviceDao = new DeviceDao();
        Device device = null;
        boolean ok = false;

        DeviceBorrowDao deviceBorrowingDao = new DeviceBorrowDao();
        int deviceid = 0;

        if (deviceId != null && "在库".equals(device.getStatus())) {
            //把String类型的id转成int
            deviceid = Integer.parseInt(deviceId);
            try {
                device = deviceDao.findDeviceByID(deviceid);
                ok = deviceDao.updateDeviceStatus(deviceid, "离库");

                deviceBorrowingDao.addBorrowing(deviceid,device.getDevicename(),user.getUserID(),username,"未归还",borrowPeriod);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (device != null && ok) {
                // 借用成功
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "borrow successful");
            } else {
                // 借用失败
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "borrow failed");
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
