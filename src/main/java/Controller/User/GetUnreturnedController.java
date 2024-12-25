package Controller.User;

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
import java.util.List;

@WebServlet("/get-unreturned-devices")
public class GetUnreturnedController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // Retrieve username from the URL parameter
        String username = request.getParameter("username");

        // Check if username is provided
        if (username == null || username.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("Username parameter is missing or empty");
            return;
        }

        // Create a JSON response object
        JsonObject jsonResponse = new JsonObject();
        DeviceApplyDao deviceApplyDao = new DeviceApplyDao();
        DeviceBorrowDao deviceBorrowDao = new DeviceBorrowDao();
        List<DeviceApply> appliesForUserUnreturned = null;
        List<DeviceBorrow> borrowingsForUserUnreturned = null;

        try {
            // Get device lists
            appliesForUserUnreturned = deviceApplyDao.findAppliesForUserUnreturned(username);
            borrowingsForUserUnreturned = deviceBorrowDao.findBorrowingsForUserUnreturned(username);

            // Convert device lists to JSON arrays
            Gson gson = new Gson();
            JsonArray deviceApplyArray = gson.toJsonTree(appliesForUserUnreturned).getAsJsonArray();
            JsonArray deviceBorrowArray = gson.toJsonTree(borrowingsForUserUnreturned).getAsJsonArray();

            // Construct JSON response
            jsonResponse.addProperty("success", true);
            jsonResponse.addProperty("message", "Loading devices list successful");
            jsonResponse.add("deviceApplyArray", deviceApplyArray);
            jsonResponse.add("deviceBorrowArray", deviceBorrowArray);
        } catch (SQLException e) {
            // In case of SQLException, capture exception and return fail response
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Loading devices list failed: " + e.getMessage());
        }

        // Return JSON response
        out.write(jsonResponse.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}