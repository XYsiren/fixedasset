package Controller.Admin.ManageDevice;

import Dao.DeviceApplyDao;
import Dao.DeviceBorrowDao;
import Dao.DeviceDao;
import Entity.Device;
import Entity.DeviceApply;
import Entity.DeviceBorrow;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/unreturned-device")
public class UnreturnedDeviceController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();

        // 创建返回的 JSON 对象
        JsonObject jsonResponse = new JsonObject();
        DeviceBorrowDao deviceBorrowDao = new DeviceBorrowDao();
        DeviceApplyDao deviceApplyDao = new DeviceApplyDao();
        List<DeviceApply> applydevices = null;
        List<DeviceBorrow> borrowdevices = null;

        try {
            // 获取"未归还"设备列表
            applydevices  = deviceApplyDao.findAppliesForUnreturned();
            borrowdevices = deviceBorrowDao.findBorrowingsForUnreturned();
            // 将设备列表转换为 JSON 数组
            Gson gson = new Gson();
            JsonArray applyDevices = gson.toJsonTree(applydevices).getAsJsonArray();
            JsonArray borrowDevices = gson.toJsonTree(borrowdevices).getAsJsonArray();

            // 构造 JSON 响应
            jsonResponse.addProperty("success", true);
            jsonResponse.addProperty("message", "Loading apply-devices && borrow-devices list successful");
            jsonResponse.add("applyDevices", applyDevices);
            jsonResponse.add("borrowDevices", borrowDevices);
        } catch (SQLException e) {
            // 如果发生 SQLException，捕获异常并返回失败响应
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Loading apply-devices && borrow-devices list failed: " + e.getMessage());
        }

        // 返回 JSON 响应
        out.write(jsonResponse.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
