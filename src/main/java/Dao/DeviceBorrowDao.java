package Dao;

import Entity.DeviceBorrow;
import Utils.DruidDateUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeviceBorrowDao {
    // 查找借用记录 by borrowingID
    public DeviceBorrow findBorrowingByID(int borrowingID) throws SQLException {
        String query = "SELECT * FROM device_borrowing WHERE borrowingID = ?";
        try (Connection connection = DruidDateUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, borrowingID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    DeviceBorrow borrowing = new DeviceBorrow();
                    borrowing.setBorrowingID(resultSet.getInt("borrowingID"));
                    borrowing.setDeviceID(resultSet.getInt("deviceID"));
                    borrowing.setDevicename(resultSet.getString("devicename"));
                    borrowing.setUserID(resultSet.getInt("userID"));
                    borrowing.setUsername(resultSet.getString("username"));
                    borrowing.setReturnStatus(resultSet.getString("returnStatus"));
                    borrowing.setReturnStatus(resultSet.getString("borrowPeriod"));
                    return borrowing;
                }
            }
        }
        return null;  // 如果没有找到借用记录，返回null
    }

    // 查找借用记录 by username
    public List<DeviceBorrow> findBorrowingsByUsername(String username) throws SQLException {
        List<DeviceBorrow> borrowings = new ArrayList<>();
        String query = "SELECT * FROM deviceborrowing WHERE username LIKE ?";
        try (Connection connection = DruidDateUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + username + "%");  // 使用通配符进行模糊查询
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    DeviceBorrow borrowing = new DeviceBorrow();
                    borrowing.setBorrowingID(resultSet.getInt("borrowingID"));
                    borrowing.setDeviceID(resultSet.getInt("deviceID"));
                    borrowing.setDevicename(resultSet.getString("devicename"));
                    borrowing.setUserID(resultSet.getInt("userID"));
                    borrowing.setUsername(resultSet.getString("username"));
                    borrowing.setReturnStatus(resultSet.getString("returnStatus"));
                    borrowing.setReturnStatus(resultSet.getString("borrowPeriod"));
                    borrowings.add(borrowing);
                }
            }
        }
        return borrowings;  // 如果没有匹配借用记录，返回空列表
    }

    public List<DeviceBorrow> getAllBorrowings() throws SQLException {
        List<DeviceBorrow> borrowings = new ArrayList<>();
        String query = "SELECT * FROM deviceborrowing";
        try (Connection connection = DruidDateUtils.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                DeviceBorrow borrowing = new DeviceBorrow();
                borrowing.setBorrowingID(resultSet.getInt("borrowingID"));
                borrowing.setDeviceID(resultSet.getInt("deviceID"));
                borrowing.setDevicename(resultSet.getString("devicename"));
                borrowing.setUserID(resultSet.getInt("userID"));
                borrowing.setUsername(resultSet.getString("username"));
                borrowing.setReturnStatus(resultSet.getString("returnStatus"));
                borrowing.setReturnStatus(resultSet.getString("borrowPeriod"));
                borrowings.add(borrowing);
            }
        }
        return borrowings;
    }

    public void deleteBorrowing(int borrowingID) throws SQLException {
        String query = "DELETE FROM deviceborrowing WHERE borrowingID = ?";
        try (Connection connection = DruidDateUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, borrowingID);
            statement.executeUpdate();
        }
    }

    public void addBorrowing(int deviceID, String devicename,int userID,String username,String returnStatus,String borrowPeriod) throws SQLException {
        String query = "INSERT INTO deviceborrowing (deviceID, devicename, userID, username, returnStatus, borrowPeriod) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DruidDateUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, deviceID);
            statement.setString(2, devicename);
            statement.setInt(3, userID);
            statement.setString(4, username);
            statement.setString(5, returnStatus);
            statement.setString(6,borrowPeriod);
            statement.executeUpdate();
        }
    }

    // 更新借用状态
    public boolean updateBorrowingStatus(int deviceID,String username, String returnStatus) {
        String query = "UPDATE deviceborrowing SET returnStatus = ? WHERE deviceID = ? AND username = ?";

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
}
