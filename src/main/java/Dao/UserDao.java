package Dao;

import Entity.User;
import Utils.DruidDateUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    public static User findByID(Integer userID) throws SQLException {
        User user = null;
        Connection cn = DruidDateUtils.getConnection();
        String sql = "SELECT * FROM user WHERE userID = ?;";
        PreparedStatement pstate = cn.prepareStatement(sql);
        pstate.setInt(1, userID);
        ResultSet rs = pstate.executeQuery();
        if (rs.next()) {
            String username = rs.getString("username");
            String password = rs.getString("password");
            String email = rs.getString("email");
            String phone = rs.getString("phone");
            user = new User(userID,username, password, email, phone);
        }
        rs.close();
        pstate.close();
        cn.close();
        return user;
    }

    public static User findByName(String name) {
        User user = null;
        String sql = "SELECT * FROM user WHERE username = ?;";

        // 使用 try-with-resources 确保资源会被自动关闭
        try (Connection cn = DruidDateUtils.getConnection();
             PreparedStatement pstate = cn.prepareStatement(sql)) {

            pstate.setString(1, name);  // 设置查询参数

            try (ResultSet rs = pstate.executeQuery()) {
                if (rs.next()) {
                    int userID = rs.getInt("userID");
                    String password = rs.getString("password");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");

                    // 使用查询结果创建 User 对象
                    user = new User(userID, name, password, email, phone);
                }
            }
        } catch (SQLException e) {
            // 记录异常信息
            System.err.println("数据库查询失败：" + e.getMessage());
            e.printStackTrace();
        }
        return user;
    }



}
