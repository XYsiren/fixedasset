package Dao;

import Entity.Admin;
import Utils.DruidDateUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDao {
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
}
