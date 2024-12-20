package Dao;

import Entity.Device;
import Utils.DruidDateUtils;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DeviceDao {
    // 查找设备 by deviceID
    public Device findDeviceByID(int deviceID) throws SQLException {
        String query = "SELECT * FROM device WHERE deviceID = ?";
        try (Connection connection = DruidDateUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, deviceID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Device device = new Device();
                    device.setDeviceID(resultSet.getInt("deviceID"));
                    device.setDevicename(resultSet.getString("devicename"));
                    device.setType(resultSet.getString("type"));
                    device.setStatus(resultSet.getString("status"));
                    return device;
                }
            }
        }
        return null;  // 如果没有找到设备，返回null
    }

    // 查找设备 by devicename
    public Device findDeviceByName(String devicename) throws SQLException {
        Device device = null;
        String query = "SELECT * FROM device WHERE devicename LIKE ?";
        try (Connection connection = DruidDateUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + devicename + "%");  // 使用通配符进行模糊查询
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {  // 使用 if 而不是 while，因为我们只需要找到一个设备
                    device = new Device();  // 初始化 device 对象
                    device.setDeviceID(resultSet.getInt("deviceID"));
                    device.setDevicename(resultSet.getString("devicename"));
                    device.setType(resultSet.getString("type"));
                    device.setStatus(resultSet.getString("status"));
                }
            }
        }
        return device;  // 如果没有找到设备，返回null
    }

    public List<Device> getAllDevices() throws SQLException {
        List<Device> devices = new ArrayList<>();
        String query = "SELECT * FROM device";
        try (Connection connection = DruidDateUtils.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Device device = new Device();
                device.setDeviceID(resultSet.getInt("deviceID"));
                device.setDevicename(resultSet.getString("devicename"));
                device.setType(resultSet.getString("type"));
                device.setStatus(resultSet.getString("status"));
                devices.add(device);
            }
        }
        return devices;
    }

    public List<Device> getAllDevicesByAdmin() throws SQLException {
        List<Device> devices = new ArrayList<>();
        String query = "SELECT * FROM device";
        try (Connection connection = DruidDateUtils.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Device device = new Device();
                device.setDeviceID(resultSet.getInt("deviceID"));
                device.setDevicename(resultSet.getString("devicename"));
                device.setType(resultSet.getString("type"));
                device.setStatus(resultSet.getString("status"));
                device.setUser_id(resultSet.getInt("user_id"));
                device.setPurchase_date(resultSet.getDate("purchase_date"));
                device.setWarranty_period(resultSet.getString("warranty_period"));
                device.setLocation(resultSet.getString("location"));
                device.setCreated_at(resultSet.getDate("created_at"));
                device.setUpdated_at(resultSet.getDate("updated_at"));
                device.setPutinstorageby(resultSet.getString("putinstorageby"));
                device.setTakeoutstorageby(resultSet.getString("takeoutstorageby"));
                devices.add(device);
            }
        }
        return devices;
    }

    public boolean deleteDevice(int deviceId) throws SQLException {
        String query = "DELETE FROM device WHERE deviceID = ?";
        try (Connection connection = DruidDateUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, deviceId);
            int i = statement.executeUpdate();
            if(i>0){
                return true;
            }
        }
        return false;
    }

    public void addDevice(Device device) throws SQLException {
        String query = "INSERT INTO device (devicename, type, status) VALUES (?, ?, ?)";
        try (Connection connection = DruidDateUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, device.getDevicename());
            statement.setString(2, device.getType());
            statement.setString(3, device.getStatus());
            statement.executeUpdate();
        }
    }

    public boolean addDeviceAll(String devicename, String type, String status, LocalDate purchaseDate, String warrantyPeriod, String location, LocalDate created_at,LocalDate updated_at, String putInStorageBy) throws SQLException {
        String query = "INSERT INTO device (devicename, type, status, purchase_date, warranty_period, location, created_at, updated_at, putinstorageby) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DruidDateUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, devicename); // 设备名称
            statement.setString(2, type); // 设备类型
            statement.setString(3, status); // 设备状态
            statement.setDate(4, java.sql.Date.valueOf(purchaseDate)); // 购买日期，转换LocalDate为java.sql.Date
            statement.setString(5, warrantyPeriod); // 保修期限
            statement.setString(6, location); // 所在位置
            statement.setDate(7, java.sql.Date.valueOf(created_at));
            statement.setDate(8, java.sql.Date.valueOf(updated_at));
            statement.setString(9, putInStorageBy); // 入库人

            int ok = statement.executeUpdate();// 执行插入
            if (ok>0){
                return true;
            }
        }
        return false;
    }

    // 更新设备状态
    public boolean updateDeviceStatus(int id, int userID,String status) {
        String query = "UPDATE device SET status = ?, user_id = ? WHERE deviceID = ?";

        try (Connection connection = DruidDateUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, userID);
            preparedStatement.setInt(3, id);
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //设备报废后更新设备状态
    public boolean updateDeviceScrapStatus(int id, String status,String location) {
        String query = "UPDATE device SET status = ?,location = ? WHERE deviceID = ?";

        try (Connection connection = DruidDateUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, status);
            preparedStatement.setString(2, location);
            preparedStatement.setInt(3, id);
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateDeviceTakeoutStatus(int deviceID, String status, int userId, String takeoutStorageBy) {
        String query = "UPDATE device SET status = ?, user_id = ?, takeoutstorageby = ? WHERE deviceID = ?";

        try (Connection connection = DruidDateUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, status); // 更新状态
            preparedStatement.setInt(2, userId); // 更新用户ID
            preparedStatement.setString(3, takeoutStorageBy); // 更新取出人
            preparedStatement.setInt(4, deviceID); // 设备ID

            int rowsUpdated = preparedStatement.executeUpdate(); // 执行更新
            return rowsUpdated > 0; // 如果更新成功，返回true

        } catch (SQLException e) {
            e.printStackTrace(); // 处理异常
        }
        return false; // 如果更新失败，返回false
    }

    public boolean updateReturnStatus(int deviceID, String status) {
        String query = "UPDATE device SET status = ?, user_id = NULL,takeoutstorageby = '' WHERE deviceID = ?";

        try (Connection connection = DruidDateUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, status); // 更新状态
            preparedStatement.setInt(2, deviceID); // 设备ID

            int rowsUpdated = preparedStatement.executeUpdate(); // 执行更新
            return rowsUpdated > 0; // 如果更新成功，返回true

        } catch (SQLException e) {
            e.printStackTrace(); // 处理异常
        }
        return false; // 如果更新失败，返回false
    }
}
