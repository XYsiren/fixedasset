package Controller.Admin.ManageDevice;

import Dao.DeviceDao;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@WebServlet("/putin-storage")
public class PutInStorageController extends HttpServlet {
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

        String deviceName = null;
        String deviceType = null;
        String purchaseDate = null;
        String warrantyYears = null;
        String location = null;
        String createdAt = null;
        String updatedAt = null;
        String adminname = null;
        String deviceQuantity = null;

        // 解析 JSON 数据
        try {
            JsonObject jsonObject = JsonParser.parseString(json.toString()).getAsJsonObject();

            // 获取字段
            if (jsonObject.has("deviceName") && !jsonObject.get("deviceName").isJsonNull()) {
                deviceName = jsonObject.get("deviceName").getAsString();
            }
            if (jsonObject.has("deviceType") && !jsonObject.get("deviceType").isJsonNull()) {
                deviceType = jsonObject.get("deviceType").getAsString();
            }
            if (jsonObject.has("purchaseDate") && !jsonObject.get("purchaseDate").isJsonNull()) {
                purchaseDate = jsonObject.get("purchaseDate").getAsString();
            }
            if (jsonObject.has("warrantyYears") && !jsonObject.get("warrantyYears").isJsonNull()) {
                warrantyYears = jsonObject.get("warrantyYears").getAsString();
            }
            if (jsonObject.has("location") && !jsonObject.get("location").isJsonNull()) {
                location = jsonObject.get("location").getAsString();
            }
            if (jsonObject.has("createdAt") && !jsonObject.get("createdAt").isJsonNull()) {
                createdAt = jsonObject.get("createdAt").getAsString();
            }
            if (jsonObject.has("updatedAt") && !jsonObject.get("updatedAt").isJsonNull()) {
                updatedAt = jsonObject.get("updatedAt").getAsString();
            }
            if (jsonObject.has("adminname") && !jsonObject.get("adminname").isJsonNull()) {
                adminname = jsonObject.get("adminname").getAsString();
            }
            if (jsonObject.has("deviceQuantity") && !jsonObject.get("deviceQuantity").isJsonNull()) {
                deviceQuantity = jsonObject.get("deviceQuantity").getAsString();
            }

            //验证
            System.out.println("deviceName: " + deviceName);
            System.out.println("deviceType: " + deviceType);
            System.out.println("purchaseDate: " + purchaseDate );
            System.out.println("deviceQuantity: " + deviceQuantity );
            System.out.println("warrantyYears: " + warrantyYears + "年" );
            System.out.println("location: " + location );
            System.out.println("createdAt: " + createdAt );
            System.out.println("updatedAt: " + updatedAt );
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
        boolean ok = false;

        int number = Integer.parseInt(deviceQuantity);

        LocalDate purchase_date = null;
        LocalDate create_at = null;
        LocalDate update_at = null;

        // 如果 action 是 delete，就执行删除操作
        if (deviceName != null && deviceType != null && adminname != null) {
            //把String类型的id转成int
            if(purchaseDate != null) {
                purchase_date = convertStringToDate(purchaseDate);
            }
            if(createdAt != null) {
                create_at = convertStringToDate(createdAt);
            }
            if(updatedAt != null) {
                update_at = convertStringToDate(updatedAt);
            }
            try {
                ok = deviceDao.addDeviceAll(deviceName, deviceType, number, "在库", purchase_date, warrantyYears, location, create_at, update_at, adminname);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (ok) {
                // 入库成功
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "put in storage successful");
            } else {
                //入库失败
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "put in storage failed");
            }
        }
        // 返回 JSON 响应
        out.write(jsonResponse.toString());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public static LocalDate convertStringToDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 日期格式
        try {
            return LocalDate.parse(dateString, formatter); // 返回转换后的 LocalDate
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null; // 如果转换失败，返回 null
        }
    }
}

