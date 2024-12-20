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
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid JSON format: " + e.getMessage());
            return;
        }

        UserDao userDao = new UserDao();
        User user = userDao.findByName(username);
        JsonObject jsonResponse = new JsonObject();
        DeviceDao deviceDao = new DeviceDao();
        Device device = null;
        boolean ok = false;

        DeviceApplyDao deviceApplyDao = new DeviceApplyDao();
        int deviceid = 0;

        if (deviceId != null) {
            deviceid = Integer.parseInt(deviceId);
            try {
                device = deviceDao.findDeviceByID(deviceid);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (device != null && "在库".equals(device.getStatus())) {
                // 创建申请记录，状态为“待审核”
                try {
                    ok = deviceDao.updateReturnStatus(deviceid,"待审核");
                    if(ok) {
                        deviceApplyDao.addApply(deviceid, device.getDevicename(), user.getUserID(), username, "待审核", "", "", applyPeriod);
                        jsonResponse.addProperty("success", true);
                        jsonResponse.addProperty("message", "Application submitted for approval");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Apply failed. Device is not available");
            }

        } else if (deviceName != null) {
            try {
                device = deviceDao.findDeviceByName(deviceName);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (device != null && "在库".equals(device.getStatus())) {
                // 创建申请记录，状态为“待审核”
                try {
                    deviceApplyDao.addApply(deviceid, device.getDevicename(), user.getUserID(), username, "待审核","","", "");
                    jsonResponse.addProperty("success", true);
                    jsonResponse.addProperty("message", "Application submitted for approval");
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

