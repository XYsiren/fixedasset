package Dao;

import Entity.DeviceApply;
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
                    borrowing.setBorrowPeriod(resultSet.getString("borrowPeriod"));
                    borrowings.add(borrowing);
                }
            }
        }
        return borrowings;  // 如果没有匹配借用记录，返回空列表
    }

    public List<DeviceBorrow> findBorrowingsForUnreturned() throws SQLException {
        List<DeviceBorrow> borrowings = new ArrayList<>();
        String query = "SELECT * FROM deviceborrowing WHERE returnStatus = '未归还'";
        try (Connection connection = DruidDateUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    DeviceBorrow borrowing = new DeviceBorrow();
                    borrowing.setDeviceID(resultSet.getInt("deviceID"));
                    borrowing.setDevicename(resultSet.getString("devicename"));
                    borrowing.setUserID(resultSet.getInt("userID"));
                    borrowing.setUsername(resultSet.getString("username"));
                    borrowing.setBorrowPeriod(resultSet.getString("borrowPeriod"));
                    borrowing.setReturnDueDate(resultSet.getDate("returnDueDate"));
                    borrowings.add(borrowing);
                }
            }
        }
        return borrowings;  // 如果没有匹配借用记录，返回空列表
    }

    public List<DeviceBorrow> findBorrowingsForUserUnreturned(String username) throws SQLException {
        List<DeviceBorrow> borrowings = new ArrayList<>();
        String query = "SELECT * FROM deviceborrowing WHERE returnStatus = '未归还' AND username = ?";
        try (Connection connection = DruidDateUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    DeviceBorrow borrowing = new DeviceBorrow();
                    borrowing.setDeviceID(resultSet.getInt("deviceID"));
                    borrowing.setDevicename(resultSet.getString("devicename"));
                    borrowing.setUserID(resultSet.getInt("userID"));
                    borrowing.setBorrowNumber(resultSet.getInt("borrowNumber"));
                    borrowing.setBorrowPeriod(resultSet.getString("borrowPeriod"));
                    borrowing.setReturnDueDate(resultSet.getDate("returnDueDate"));
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

    public List<DeviceBorrow> getAllBorrowsForExamine() throws SQLException {
        List<DeviceBorrow> borrows = new ArrayList<>();
        String query = "SELECT * FROM deviceborrowing where borrowStatus = '待审核'";
        try (Connection connection = DruidDateUtils.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                DeviceBorrow deviceBorrow = new DeviceBorrow();
                deviceBorrow.setBorrowingID(resultSet.getInt("borrowingID"));
                deviceBorrow.setDeviceID(resultSet.getInt("deviceID"));
                deviceBorrow.setDevicename(resultSet.getString("devicename"));
                deviceBorrow.setUserID(resultSet.getInt("userID"));
                deviceBorrow.setUsername(resultSet.getString("username"));
                deviceBorrow.setBorrowNumber(resultSet.getInt("borrowNumber"));
                deviceBorrow.setBorrowPeriod(resultSet.getString("borrowPeriod"));
                borrows.add(deviceBorrow);
            }
        }
        return borrows;
    }

    public void deleteBorrowing(int borrowingID) throws SQLException {
        String query = "DELETE FROM deviceborrowing WHERE borrowingID = ?";
        try (Connection connection = DruidDateUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, borrowingID);
            statement.executeUpdate();
        }
    }

    public void addBorrowing(int deviceID, String devicename,int userID,String username, int borrowNumber, String borrowStatus,String approvedby, String returnStatus,String borrowPeriod, Date returnDueDate) throws SQLException {
        String query = "INSERT INTO deviceborrowing (deviceID, devicename, userID, username, borrowNumber, borrowStatus, approvedby, returnStatus, borrowPeriod, returnDueDate) VALUES (?,?,?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DruidDateUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, deviceID);
            statement.setString(2, devicename);
            statement.setInt(3, userID);
            statement.setString(4, username);
            statement.setInt(5, borrowNumber);
            statement.setString(6, borrowStatus);
            statement.setString(7, approvedby);
            statement.setString(8, returnStatus);
            statement.setString(9,borrowPeriod);
            statement.setDate(10,returnDueDate);
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


    public boolean updateBorrowStatusForApprove(int deviceID, int userID, String username, String adminname) {
        String query = "UPDATE deviceborrowing SET returnStatus = '未归还', borrowStatus = '审核通过', approvedby = ? WHERE deviceID = ? AND userID = ? AND username = ?";

        try (Connection connection = DruidDateUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, adminname);
            preparedStatement.setInt(2, deviceID);
            preparedStatement.setInt(3, userID);
            preparedStatement.setString(4, username);
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
