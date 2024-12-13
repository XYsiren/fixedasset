package Dao;

import Entity.Admin;
import Utils.DruidDateUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDao {
    /**
     * 检查管理员是否已存在
     * @param adminName 管理员用户名
     * @return 如果管理员存在，返回true；否则返回false
     */
    public static boolean exists(String adminName) {
        String sql = "SELECT COUNT(*) FROM admin WHERE adminName = ?;";
        try (Connection cn = DruidDateUtils.getConnection();
             PreparedStatement pstate = cn.prepareStatement(sql)) {
            pstate.setString(1, adminName);
            ResultSet rs = pstate.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;  // 如果查询结果大于0，则说明已存在
            }
        } catch (SQLException e) {
            System.err.println("检查管理员是否存在失败：" + e.getMessage());
            e.printStackTrace();
        }
        return false;  // 如果发生异常或查询结果为0，说明不存在
    }

    public static Admin findByID(Integer adminID) throws SQLException {
        Admin admin = null;
        Connection cn = DruidDateUtils.getConnection();
        String sql = "SELECT * FROM admin WHERE adminID = ?;";
        PreparedStatement pstate = cn.prepareStatement(sql);
        pstate.setInt(1, adminID);
        ResultSet rs = pstate.executeQuery();
        if (rs.next()) {
            String adminName = rs.getString("adminName");
            String password = rs.getString("password");
            String email = rs.getString("email");
            String phone = rs.getString("phone");
            admin = new Admin(adminID, adminName, password, email, phone);
        }
        rs.close();
        pstate.close();
        cn.close();
        return admin;
    }

    public static Admin findByName(String name) throws SQLException {
        Admin admin = null;
        Connection cn = DruidDateUtils.getConnection();
        String sql = "SELECT * FROM admin WHERE adminName = ?;";
        PreparedStatement pstate = cn.prepareStatement(sql);
        pstate.setString(1, name);
        ResultSet rs = pstate.executeQuery();
        if (rs.next()) {
            int adminID = rs.getInt("adminID");
            String password = rs.getString("password");
            String email = rs.getString("email");
            String phone = rs.getString("phone");
            admin = new Admin(adminID, name, password, email, phone);
        }
        rs.close();
        pstate.close();
        cn.close();
        return admin;
    }

    /**
     * 增加新管理员
     * @param adminName 管理员用户名
     * @param password 密码
     * @param email 管理员邮箱
     * @param phone 管理员电话
     * @return 如果添加成功，返回新创建的Admin对象，失败返回null
     */
    public static Admin addAdmin(String adminName, String password, String email, String phone) {
        // 首先检查管理员是否已经存在
        if (exists(adminName)) {
            System.err.println("管理员已存在！");
            return null;  // 如果管理员已存在，则返回null
        }

        // 如果管理员不存在，继续执行添加操作
        String sql = "INSERT INTO admin (adminName, password, email, phone) VALUES (?, ?, ?, ?);";
        try (Connection cn = DruidDateUtils.getConnection();
             PreparedStatement pstate = cn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // 设置插入的字段值
            pstate.setString(1, adminName);
            pstate.setString(2, password);
            pstate.setString(3, email);
            pstate.setString(4, phone);

            // 执行插入操作
            int rowsAffected = pstate.executeUpdate();
            if (rowsAffected > 0) {
                // 获取自动生成的主键（adminID）
                try (ResultSet generatedKeys = pstate.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int adminID = generatedKeys.getInt(1);
                        // 返回新创建的Admin对象
                        return new Admin(adminID, adminName, password, email, phone);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("添加管理员失败：" + e.getMessage());
            e.printStackTrace();
        }
        return null;  // 如果插入失败，返回null
    }
}
