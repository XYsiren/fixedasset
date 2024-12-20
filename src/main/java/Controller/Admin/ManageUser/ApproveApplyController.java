package Controller.Admin.ManageUser;

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

@WebServlet("/approve-apply")
public class ApproveApplyController extends HttpServlet {
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

        String userID = null;
        String deviceID = null;
        String username = null;
        String applyPeriod = null;

        // 解析 JSON 数据
        try {
            JsonObject jsonObject = JsonParser.parseString(json.toString()).getAsJsonObject();

            // 获取字段
            if (jsonObject.has("userID") && !jsonObject.get("userID").isJsonNull()) {
                userID = jsonObject.get("userID").getAsString();
            }
            if (jsonObject.has("deviceID") && !jsonObject.get("deviceID").isJsonNull()) {
                deviceID = jsonObject.get("deviceID").getAsString();
            }
            if (jsonObject.has("username") && !jsonObject.get("username").isJsonNull()) {
                username = jsonObject.get("username").getAsString();
            }
            if (jsonObject.has("applyPeriod") && !jsonObject.get("applyPeriod").isJsonNull()) {
                applyPeriod = jsonObject.get("applyPeriod").getAsString();
            }
            //验证
            System.out.println("userID: " + userID);
            System.out.println("username: " + username);
            System.out.println("deviceID: " + deviceID);
            System.out.println("applyPeriod: " + applyPeriod);

        } catch (Exception e) {
            // 捕获 JSON 解析错误
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 错误
            response.getWriter().write("Invalid JSON format: " + e.getMessage());
            return;
        }

        UserDao userDao = new UserDao();
        //把String类型的id转成int
        int userid = Integer.parseInt(userID);
        int deviceid = Integer.parseInt(deviceID);
        //User user = userDao.findByIdAndName(userid,username);

        // 创建返回的 JSON 对象
        JsonObject jsonResponse = new JsonObject();
        DeviceApplyDao deviceApplyDao = new DeviceApplyDao();
        DeviceDao deviceDao = new DeviceDao();
        boolean ok = false;

        if (deviceID != null && userID != null) {
           ok = deviceApplyDao.updateApplyStatusForApprove(deviceid,userid,username,applyPeriod);
           boolean ok2 = deviceDao.updateDeviceStatus(deviceid,userid,"离库");
            if (ok && ok2) {
                // 领用成功
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "approve apply successful");
            } else {
                // 失败
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "approve apply failed");
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
