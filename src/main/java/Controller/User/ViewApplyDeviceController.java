package Controller.User;

import Dao.DeviceApplyDao;
import Dao.UserDao;
import Entity.DeviceApply;
import Entity.User;
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

@WebServlet("/view-borrowed-devices")
public class ViewApplyDeviceController extends HttpServlet {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("application/json;charset=utf-8");
            request.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();

            String username = request.getParameter("username"); // 获取用户名参数
            System.out.println(username);
            // 创建一个列表来存储用户的已借用设备
            List<DeviceApply> appliesByUsername = null;

            DeviceApplyDao deviceApplyDao = new DeviceApplyDao();

            try {
                // 从数据库检索该用户的借用设备
                appliesByUsername = deviceApplyDao.findAppliesByUsername(username);//找到该用户所有领用记录

            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 设置为 500 错误
                return;
            }

            // 将设备列表转换为 JSON 数组
            Gson gson = new Gson();
            JsonArray borrowedDevices = gson.toJsonTree(appliesByUsername).getAsJsonArray();

            JsonObject jsonResponse = new JsonObject();
            jsonResponse.add("borrowedDevices", borrowedDevices);

            out.write(jsonResponse.toString());
        }

}
