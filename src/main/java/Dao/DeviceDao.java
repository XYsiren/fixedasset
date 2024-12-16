package Dao;

import Entity.Device;
import Utils.DruidDateUtils;

import java.sql.*;
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

    // 更新设备状态
    public boolean updateDeviceStatus(int id, String status) {
        String query = "UPDATE device SET status = ? WHERE deviceID = ?";

        try (Connection connection = DruidDateUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, id);
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
