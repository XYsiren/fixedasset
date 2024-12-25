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
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;


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
        String returnDueDate = null;
        String borrowQuantity = null;

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
            if (jsonObject.has("returnDueDate") && !jsonObject.get("returnDueDate").isJsonNull()) {
                returnDueDate = jsonObject.get("returnDueDate").getAsString();
            }
            if (jsonObject.has("borrowQuantity") && !jsonObject.get("borrowQuantity").isJsonNull()) {
                borrowQuantity = jsonObject.get("borrowQuantity").getAsString();
            }
            //验证
            System.out.println("Username: " + username);
            System.out.println("deviceId: " + deviceId);
            System.out.println("borrowQuantity: " + borrowQuantity);
            System.out.println("borrowPeriod: " + borrowPeriod +"天");
            System.out.println("returnDueDate: " + returnDueDate);

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
        //把String类型的id转成int
        int deviceid = Integer.parseInt(deviceId);
        int number = Integer.parseInt(borrowQuantity);

        DeviceDao deviceDao = new DeviceDao();
        Device device = null;
        try {
            device = deviceDao.findDeviceByID(deviceid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //boolean ok = false;
        DeviceBorrowDao deviceBorrowingDao = new DeviceBorrowDao();

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

        if (deviceId != null && "在库".equals(device.getStatus())) {
            // 创建申请记录，状态为“待审核”
            try {
                boolean ok = deviceDao.updateReturnStatus(deviceid,"待审核");
                //在这里还不能调用decreaseDeviceStock方法减少库存
                //boolean ok2 = deviceDao.decreaseDeviceStock(deviceid, number);
                if(ok) {
                    deviceBorrowingDao.addBorrowing(deviceid, device.getDevicename(), user.getUserID(), username, number, "待审核", "", "", borrowPeriod, sqlDate);
                    jsonResponse.addProperty("success", true);
                    jsonResponse.addProperty("message", "Application submitted for approval");
                }else {
                    deviceDao.updateReturnStatus(deviceid,"在库");
                    jsonResponse.addProperty("success", false);
                    jsonResponse.addProperty("message", "deviceBorrowing addBorrowing failed.");
                }
            } catch (SQLException e) {throw new RuntimeException(e);}
        }
        // 返回 JSON 响应
        out.write(jsonResponse.toString());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
