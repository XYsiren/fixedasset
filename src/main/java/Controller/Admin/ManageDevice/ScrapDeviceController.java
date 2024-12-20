package Controller.Admin.ManageDevice;

import Dao.DeviceDao;
import Dao.UserDao;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/scrap-device")
public class ScrapDeviceController extends HttpServlet {
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

        // 解析 JSON 数据
        try {
            JsonObject jsonObject = JsonParser.parseString(json.toString()).getAsJsonObject();
            // 获取字段
            if (jsonObject.has("deviceID") && !jsonObject.get("deviceID").isJsonNull()) {
                deviceID = jsonObject.get("deviceID").getAsString();
            }
            //验证
            System.out.println("deviceID: " + deviceID);
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

        // 如果 action 是 delete，就执行删除操作
        if (deviceID !=null) {
            //把String类型的id转成int
            int deviceid = Integer.parseInt(deviceID);
            ok = deviceDao.updateDeviceScrapStatus(deviceid,"已报废","仓库");

            if (ok) {
                // 成功
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "scrap device successful");
            } else {
                // 失败
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "scrap device failed");
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
