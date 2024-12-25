package Dao;

import Entity.DeviceApply;
import Utils.DruidDateUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeviceApplyDao {
    // 查找申请记录 by applyID
    public DeviceApply findApplyByID(int applyID) throws SQLException {
        String query = "SELECT * FROM deviceapply WHERE applyID = ?";
        try (Connection connection = DruidDateUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, applyID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    DeviceApply apply = new DeviceApply();
                    apply.setApplyID(resultSet.getInt("applyID"));
                    apply.setDeviceID(resultSet.getInt("deviceID"));
                    apply.setDevicename(resultSet.getString("devicename"));
                    apply.setUserID(resultSet.getInt("userID"));
                    apply.setUsername(resultSet.getString("username"));
                    apply.setReturnStatus(resultSet.getString("returnStatus"));
                    apply.setApplyPeriod(resultSet.getString("applyPeriod"));
                    return apply;
                }
            }
        }
        return null;  // 如果没有找到申请记录，返回null
    }

    // 查找申请记录 by username
    public List<DeviceApply> findAppliesByUsername(String username) throws SQLException {
        List<DeviceApply> applies = new ArrayList<>();
        String query = "SELECT * FROM deviceapply WHERE username = ?";
        try (Connection connection = DruidDateUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    DeviceApply apply = new DeviceApply();
                    apply.setApplyID(resultSet.getInt("applyID"));
                    apply.setDeviceID(resultSet.getInt("deviceID"));
                    apply.setDevicename(resultSet.getString("devicename"));
                    apply.setUserID(resultSet.getInt("userID"));
                    apply.setUsername(resultSet.getString("username"));
                    apply.setApplyNumber(resultSet.getInt("applyNumber"));
                    apply.setReturnStatus(resultSet.getString("returnStatus"));
                    apply.setApplyPeriod(resultSet.getString("applyPeriod"));
                    apply.setReturnDueDate(resultSet.getDate("returnDueDate"));
                    applies.add(apply);
                }
            }
        }
        return applies;  // 如果没有匹配申请记录，返回空列表
    }

    public List<DeviceApply> findAppliesForUnreturned() throws SQLException {
        List<DeviceApply> applies = new ArrayList<>();
        String query = "SELECT * FROM deviceapply WHERE returnStatus = '未归还'";
        try (Connection connection = DruidDateUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    DeviceApply apply = new DeviceApply();
                    apply.setDeviceID(resultSet.getInt("deviceID"));
                    apply.setDevicename(resultSet.getString("devicename"));
                    apply.setUserID(resultSet.getInt("userID"));
                    apply.setUsername(resultSet.getString("username"));
                    apply.setApplyPeriod(resultSet.getString("applyPeriod"));
                    apply.setReturnDueDate(resultSet.getDate("returnDueDate"));
                    applies.add(apply);
                }
            }
        }
        return applies;  // 如果没有匹配申请记录，返回空列表
    }

    public List<DeviceApply> findAppliesForUserUnreturned(String username) throws SQLException {
        List<DeviceApply> applies = new ArrayList<>();
        String query = "SELECT * FROM deviceapply WHERE returnStatus = '未归还' AND username = ?";
        try (Connection connection = DruidDateUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
             statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    DeviceApply apply = new DeviceApply();
                    apply.setDeviceID(resultSet.getInt("deviceID"));
                    apply.setDevicename(resultSet.getString("devicename"));
                    apply.setUserID(resultSet.getInt("userID"));
                    apply.setApplyPeriod(resultSet.getString("applyPeriod"));
                    apply.setReturnDueDate(resultSet.getDate("returnDueDate"));
                    applies.add(apply);
                }
            }
        }
        return applies;  // 如果没有匹配申请记录，返回空列表
    }

    public List<DeviceApply> getAllApplies() throws SQLException {
        List<DeviceApply> applies = new ArrayList<>();
        String query = "SELECT * FROM deviceapply";
        try (Connection connection = DruidDateUtils.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                DeviceApply apply = new DeviceApply();
                apply.setApplyID(resultSet.getInt("applyID"));
                apply.setDeviceID(resultSet.getInt("deviceID"));
                apply.setDevicename(resultSet.getString("devicename"));
                apply.setUserID(resultSet.getInt("userID"));
                apply.setUsername(resultSet.getString("username"));
                apply.setReturnStatus(resultSet.getString("returnStatus"));
                apply.setApplyPeriod(resultSet.getString("applyPeriod"));
                applies.add(apply);
            }
        }
        return applies;
    }

    public List<DeviceApply> getAllAppliesForExamine() throws SQLException {
        List<DeviceApply> applies = new ArrayList<>();
        String query = "SELECT * FROM deviceapply where applyStatus = '待审核'";
        try (Connection connection = DruidDateUtils.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                DeviceApply apply = new DeviceApply();
                apply.setApplyID(resultSet.getInt("applyID"));
                apply.setDeviceID(resultSet.getInt("deviceID"));
                apply.setDevicename(resultSet.getString("devicename"));
                apply.setUserID(resultSet.getInt("userID"));
                apply.setUsername(resultSet.getString("username"));
                apply.setApplyPeriod(resultSet.getString("applyPeriod"));
                applies.add(apply);
            }
        }
        return applies;
    }

    public void deleteApply(int applyID) throws SQLException {
        String query = "DELETE FROM deviceapply WHERE applyID = ?";
        try (Connection connection = DruidDateUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, applyID);
            statement.executeUpdate();
        }
    }

    public void addApply(int deviceID, String devicename, int userID, String username,int applyNumber,String applyStatus,String approvedby, String returnStatus, String applyPeriod, Date returnDueDate) throws SQLException {
        String query = "INSERT INTO deviceapply (deviceID, devicename, userID, username,applyNumber, applyStatus, approvedby, returnStatus, applyPeriod, returnDueDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DruidDateUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, deviceID);
            statement.setString(2, devicename);
            statement.setInt(3, userID);
            statement.setString(4, username);
            statement.setInt(5, applyNumber);
            statement.setString(6, applyStatus);
            statement.setString(7, approvedby);
            statement.setString(8, returnStatus);
            statement.setString(9, applyPeriod);
            statement.setDate(10,returnDueDate);
            statement.executeUpdate();
        }
    }

    // 更新申请状态
    public boolean updateApplyStatus(int deviceID, String username, String returnStatus) {
        String query = "UPDATE deviceapply SET returnStatus = ? WHERE deviceID = ? AND username = ?";

        try (Connection connection = DruidDateUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, returnStatus);
            preparedStatement.setInt(2, deviceID);
            preparedStatement.setString(3, username);
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateApplyStatusForApprove(int deviceID,int userID, String username, String adminname, String applyPeriod) {
        String query = "UPDATE deviceapply SET returnStatus = '未归还', applyStatus = '审核通过',applyPeriod = ?, approvedby = ? WHERE deviceID = ? AND userID = ? AND username = ?";

        try (Connection connection = DruidDateUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, applyPeriod);
            preparedStatement.setString(2, adminname);
            preparedStatement.setInt(3, deviceID);
            preparedStatement.setInt(4, userID);
            preparedStatement.setString(5, username);
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
