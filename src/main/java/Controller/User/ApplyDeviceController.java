package Controller.User;

import Dao.AdminDao;
import Dao.DeviceApplyDao;
import Dao.DeviceBorrowDao;
import Dao.DeviceDao;
import Dao.UserDao;
import Entity.Admin;
import Entity.Device;
import Entity.DeviceApply;
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
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet("/apply-device")
public class ApplyDeviceController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        BufferedReader reader = request.getReader();
        StringBuilder json = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            json.append(line);
        }

        if (json.length() == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Request body is empty");
            return;
        }

        String username = null;
        String deviceId = null;
        String deviceName = null;
        String applyPeriod = null;
        String returnDueDate = null;
        String quantity = null;

        try {
            JsonObject jsonObject = JsonParser.parseString(json.toString()).getAsJsonObject();
            if (jsonObject.has("deviceId") && !jsonObject.get("deviceId").isJsonNull()) {
                deviceId = jsonObject.get("deviceId").getAsString();
            }
            if (jsonObject.has("username") && !jsonObject.get("username").isJsonNull()) {
                username = jsonObject.get("username").getAsString();
            }
            if (jsonObject.has("deviceName") && !jsonObject.get("deviceName").isJsonNull()) {
                deviceName = jsonObject.get("deviceName").getAsString();
            }
            if (jsonObject.has("applyPeriod") && !jsonObject.get("applyPeriod").isJsonNull()) {
                applyPeriod = jsonObject.get("applyPeriod").getAsString();
            }
            if (jsonObject.has("returnDueDate") && !jsonObject.get("returnDueDate").isJsonNull()) {
                returnDueDate = jsonObject.get("returnDueDate").getAsString();
            }
            if (jsonObject.has("quantity") && !jsonObject.get("quantity").isJsonNull()) {
                quantity = jsonObject.get("quantity").getAsString();
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid JSON format: " + e.getMessage());
            return;
        }

        JsonObject jsonResponse = new JsonObject();

        UserDao userDao = new UserDao();
        User user = userDao.findByName(username);

        DeviceDao deviceDao = new DeviceDao();
        Device device = null;
        boolean ok = false;

        DeviceApplyDao deviceApplyDao = new DeviceApplyDao();
        int deviceid = 0;
        int applyNumber = 0;

        //转换应归还日期格式
        // 定义日期格式，这里的分隔符使用的是"/"
        Date sqlDate = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        try {
            // 将字符串解析为 java.util.Date
            java.util.Date utilDate = formatter.parse(returnDueDate);
            // 转换为 java.sql.Date
            sqlDate = new Date(utilDate.getTime());
            // 打印结果
            System.out.println("Converted java.sql.Date: " + sqlDate);
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("日期格式不正确: " + returnDueDate);
        }

        List<DeviceApply> appliesByUsername = null;
        if (deviceId != null) {
            deviceid = Integer.parseInt(deviceId);
            applyNumber = Integer.parseInt(quantity);
            try {
                device = deviceDao.findDeviceByID(deviceid);
            } catch (SQLException e) {throw new RuntimeException(e);}

            if (device != null) {
                try {
                    deviceApplyDao.addApply(deviceid, device.getDevicename(), user.getUserID(), username, applyNumber, "待审核", "", "", applyPeriod, sqlDate);
                    jsonResponse.addProperty("success", true);
                    jsonResponse.addProperty("message", "Application submitted for approval");
                } catch (SQLException e) {throw new RuntimeException(e);}
            } else {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Apply failed. Device is not available");
            }

        } else if (deviceName != null) {
            try {
                device = deviceDao.findDeviceByName(deviceName);
            } catch (SQLException e) {throw new RuntimeException(e);}

            if (device != null) {
                try {
                    appliesByUsername = deviceApplyDao.findAppliesByUsername(username);//找到该用户所有领用记录
                    // 将设备列表转换为 JSON 数组
                    Gson gson = new Gson();
                    JsonArray borrowedDevices = gson.toJsonTree(appliesByUsername).getAsJsonArray();

                    deviceApplyDao.addApply(deviceid, device.getDevicename(), user.getUserID(), username,applyNumber, "待审核", "", "", applyPeriod, sqlDate);
                    jsonResponse.addProperty("success", true);
                    jsonResponse.addProperty("message", "Application submitted for approval");
                    jsonResponse.add("borrowedDevices", borrowedDevices);

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Device not exists or not available");
            }
        }

        out.write(jsonResponse.toString());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

