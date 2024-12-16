package Controller.User;

import Dao.AdminDao;
import Dao.DeviceApplyDao;
import Dao.DeviceBorrowDao;
import Dao.DeviceDao;
import Dao.UserDao;
import Entity.Admin;
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

@WebServlet("/apply-device")
public class ApplyDeviceController extends HttpServlet {
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
        String deviceName = null;

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
            if (jsonObject.has("deviceName") && !jsonObject.get("deviceName").isJsonNull()) {
                deviceName = jsonObject.get("deviceName").getAsString();
            }
            //验证
            System.out.println("Username: " + username);
            System.out.println("deviceId: " + deviceId);
            System.out.println("deviceName: " + deviceName);

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

        DeviceApplyDao deviceApplyDao = new DeviceApplyDao();
        int deviceid = 0;

        if (deviceId != null) {
            //把String类型的id转成int
            deviceid = Integer.parseInt(deviceId);
            try {
                device = deviceDao.findDeviceByID(deviceid);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (device != null && "在库".equals(device.getStatus())) {
                ok = deviceDao.updateDeviceStatus(deviceid, "离库");
                try {
                    deviceApplyDao.addApply(deviceid,device.getDevicename(),user.getUserID(),username,"未归还","");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                if(ok) {
                    // 领用成功
                    jsonResponse.addProperty("success", true);
                    jsonResponse.addProperty("message", "Apply successful");
                }
            } else {
                // 领用失败
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Apply failed. Device is not enough");
            }

        } else if (deviceName != null && "在库".equals(device.getStatus())) {

            try {
                device = deviceDao.findDeviceByName(deviceName);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (device != null) {
                // 登录成功
                ok = deviceDao.updateDeviceStatus(device.getDeviceID(), "离库");
                try {
                    deviceApplyDao.addApply(device.getDeviceID(),device.getDevicename(),user.getUserID(),username,"未归还","");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                if(ok){
                    jsonResponse.addProperty("success", true);
                    jsonResponse.addProperty("message", "Apply successful");
                }else{
                    jsonResponse.addProperty("success", false);
                    jsonResponse.addProperty("message", "update status failed");
                }
            } else {
                // 登录失败
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "device not exists");
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