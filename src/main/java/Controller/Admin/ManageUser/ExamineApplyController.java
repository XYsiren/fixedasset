package Controller.Admin.ManageUser;

import Dao.DeviceApplyDao;
import Dao.DeviceBorrowDao;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/examine-apply")
public class ExamineApplyController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();

        // 创建返回的 JSON 对象
        JsonObject jsonResponse = new JsonObject();
        DeviceApplyDao deviceApplyDao = new DeviceApplyDao();
        DeviceBorrowDao deviceBorrowDao = new DeviceBorrowDao();
        List<DeviceApply> deviceapplyList1 = new ArrayList<>();
        List<DeviceBorrow> allBorrowsForExamine = new ArrayList<>();
        try {
            // 获取设备列表
            deviceapplyList1 = deviceApplyDao.getAllAppliesForExamine();
            allBorrowsForExamine = deviceBorrowDao.getAllBorrowsForExamine();

            // 将设备列表转换为 JSON 数组
            Gson gson = new Gson();
            JsonArray deviceArray1 = gson.toJsonTree(deviceapplyList1).getAsJsonArray();
            JsonArray deviceArray2 = gson.toJsonTree(allBorrowsForExamine).getAsJsonArray();

            // 构造 JSON 响应
            jsonResponse.addProperty("success", true);
            jsonResponse.addProperty("message", "Loading devices list successful");
            jsonResponse.add("deviceApplyList", deviceArray1);
            jsonResponse.add("deviceBorrowList", deviceArray2);
        } catch (SQLException e) {
            // 如果发生 SQLException，捕获异常并返回失败响应
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Loading devices list failed: " + e.getMessage());
        }

        // 返回 JSON 响应
        out.write(jsonResponse.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
