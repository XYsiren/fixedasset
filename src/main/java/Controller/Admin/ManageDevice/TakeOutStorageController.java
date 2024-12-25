package Controller.Admin.ManageDevice;

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
        String quantity = null;
        String borrowDays = null;
        String returnDate = null;

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
            if (jsonObject.has("quantity") && !jsonObject.get("quantity").isJsonNull()) {
                quantity = jsonObject.get("quantity").getAsString();
            }
            if (jsonObject.has("borrowDays") && !jsonObject.get("borrowDays").isJsonNull()) {
                borrowDays = jsonObject.get("borrowDays").getAsString();
            }
            if (jsonObject.has("returnDate") && !jsonObject.get("returnDate").isJsonNull()) {
                returnDate = jsonObject.get("returnDate").getAsString();
            }

            //验证
            System.out.println("selectedDeviceID: " + deviceID);
            System.out.println("userID: " + userID);
            System.out.println("username: " + username );
            System.out.println("adminname: " + adminname );
            System.out.println("quantity: " + quantity );
            System.out.println("borrowDays: " + borrowDays );
            System.out.println("returnDate: " + returnDate );

        } catch (Exception e) {
            // 捕获 JSON 解析错误
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 错误
            response.getWriter().write("Invalid JSON format: " + e.getMessage());
            return;
        }

        //转换应归还日期格式
        // 定义日期格式，这里的分隔符使用的是"/"
        Date sqlDate = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // 将字符串解析为 java.util.Date
            java.util.Date utilDate = formatter.parse(returnDate);
            // 转换为 java.sql.Date
            sqlDate = new Date(utilDate.getTime());
            // 打印结果
            System.out.println("Converted java.sql.Date: " + sqlDate);
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("日期格式不正确: " + returnDate);
        }

        // 创建返回的 JSON 对象
        JsonObject jsonResponse = new JsonObject();
        DeviceDao deviceDao = new DeviceDao();
        DeviceBorrowDao deviceBorrowDao = new DeviceBorrowDao();
        UserDao userDao = new UserDao();
        boolean ok = false;
        //把String类型的id转成int
        int userid = Integer.parseInt(userID);
        User user = userDao.findByIdAndName(userid, username);
        int deviceid = Integer.parseInt(deviceID);
        Device device = null;
        try {
            device = deviceDao.findDeviceByID(deviceid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int number = Integer.parseInt(quantity);

        if (device != null && user != null && adminname != null) {
            System.out.println(device.getNumber());
//            if(device.getNumber()-number == 0) {
//                ok = deviceDao.updateDeviceTakeoutStatus(deviceid, "暂无库存", userid, adminname);
//            }else{
//                ok = deviceDao.updateDeviceTakeoutStatus(deviceid, "在库", userid, adminname);
//            }
            try {
                deviceBorrowDao.addBorrowing(deviceid,device.getDevicename(),userid,username,number,"审核通过",adminname,"未归还",borrowDays,sqlDate);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            boolean ok1 = deviceDao.decreaseDeviceStock(deviceid, number);
            if (ok1) {
                // 出库成功
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "takeout storage successful");
            } else {
                deviceDao.updateReturnStatus(deviceid,"在库");
                deviceDao.increaseDeviceStock(deviceid,number);
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
