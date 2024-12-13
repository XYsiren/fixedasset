package Dao;

import Entity.User;
import Utils.DruidDateUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return 如果用户名存在，返回 true；否则返回 false
     */
    public static boolean exists(String username) {
        String sql = "SELECT COUNT(*) FROM user WHERE username = ?;";
        try (Connection cn = DruidDateUtils.getConnection();
             PreparedStatement pstate = cn.prepareStatement(sql)) {

            pstate.setString(1, username);  // 设置查询参数

            try (ResultSet rs = pstate.executeQuery()) {
                if (rs.next()) {
                    // 如果返回的计数大于0，表示用户名已存在
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("数据库查询失败：" + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

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

    /**
     * 增加新用户
     * @param username 用户名
     * @param password 密码
     * @param email 用户邮箱
     * @param phone 用户电话
     * @return 如果添加成功，返回新创建的User对象，失败返回null
     */
    public static User addUser(String username, String password, String email, String phone) {
        // 首先检查用户是否已经存在
        if (exists(username)) {
            System.err.println("c此用户已存在！");
            return null;  // 如果用户已存在，则返回null
        }

        // 如果用户不存在，继续执行添加操作
        String sql = "INSERT INTO user (username, password, email, phone) VALUES (?, ?, ?, ?);";
        try (Connection cn = DruidDateUtils.getConnection();
             PreparedStatement pstate = cn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // 设置插入的字段值
            pstate.setString(1, username);
            pstate.setString(2, password);
            pstate.setString(3, email);
            pstate.setString(4, phone);

            // 执行插入操作
            int rowsAffected = pstate.executeUpdate();
            if (rowsAffected > 0) {
                // 获取自动生成的主键（userID）
                try (ResultSet generatedKeys = pstate.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int userID = generatedKeys.getInt(1);
                        // 返回新创建的User对象
                        return new User(userID, username, password, email, phone);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("添加用户失败：" + e.getMessage());
            e.printStackTrace();
        }
        return null;  // 如果插入失败，返回null
    }

}
