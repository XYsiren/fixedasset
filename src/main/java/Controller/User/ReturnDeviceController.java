package Controller.User;

import Dao.DeviceApplyDao;
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

@WebServlet("/return-device")
public class ReturnDeviceController extends HttpServlet {
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
        String devicename = null;
        String returnType = null;
        String number = null;

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
            if (jsonObject.has("devicename") && !jsonObject.get("devicename").isJsonNull()) {
                devicename = jsonObject.get("devicename").getAsString();
            }
            if (jsonObject.has("returnType") && !jsonObject.get("returnType").isJsonNull()) {
                returnType = jsonObject.get("returnType").getAsString();
            }
            if (jsonObject.has("number") && !jsonObject.get("number").isJsonNull()) {
                number = jsonObject.get("number").getAsString();
            }
            //验证
            System.out.println("Username: " + username);
            System.out.println("deviceId: " + deviceId);
            System.out.println("devicename: " + devicename);
            System.out.println("returnType: " + returnType);
            System.out.println("number: " + number);
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
        DeviceBorrowDao deviceBorrowDao = new DeviceBorrowDao();
        int deviceid = 0;

        if (deviceId != null) {
            //把String类型的id转成int
            deviceid = Integer.parseInt(deviceId);
            int borrowNumber = Integer.parseInt(number);
            try {
                device = deviceDao.findDeviceByID(deviceid);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (device != null) {
               // ok = deviceDao.updateReturnStatus(deviceid, "在库");
                if(returnType.equals("apply")) {
                    deviceApplyDao.updateApplyStatus(device.getDeviceID(), username, "已归还");
                }else{
                    ok = deviceDao.increaseDeviceStock(deviceid, borrowNumber);
                    deviceBorrowDao.updateBorrowingStatus(device.getDeviceID(),username,"已归还");
                }
                if(ok) {
                    // 更新状态成功
                    jsonResponse.addProperty("success", true);
                    jsonResponse.addProperty("message", "Return successful");
                }
            } else {
                // 归还失败
                deviceDao.decreaseDeviceStock(deviceid,borrowNumber);
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Return failed. No this device");
            }

        } else if (devicename != null && "离库".equals(device.getStatus())) {
            try {
                device = deviceDao.findDeviceByName(devicename);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (device != null) {
                ok = deviceDao.updateReturnStatus(device.getDeviceID(), "在库");
                if(returnType.equals("apply")) {
                    deviceApplyDao.updateApplyStatus(device.getDeviceID(), username, "已归还");
                }else{
                    deviceBorrowDao.updateBorrowingStatus(device.getDeviceID(),username,"已归还");
                }
                if(ok){
                    jsonResponse.addProperty("success", true);
                    jsonResponse.addProperty("message", "Return successful");
                }else{
                    jsonResponse.addProperty("success", false);
                    jsonResponse.addProperty("message", "Update status failed");
                }
            } else {
                // 登录失败
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Return failed");
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