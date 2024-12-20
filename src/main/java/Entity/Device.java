package Entity;

import java.util.Date;

public class Device {
    private int deviceID;
    private String devicename;
    private String type;
    private String status;
    private Date purchase_date;
    private String warranty_period;//保修年限
    private int user_id;
    private String location;
    private Date created_at;
    private Date updated_at;
    private String putinstorageby;
    private String takeoutstorageby;

    public Device(){}

    public Device(int deviceID, String devicename, String type, String status, Date purchase_date, String warranty_period, int user_id, String location, Date created_at, Date updated_at, String putinstorageby, String takeoutstorageby) {
        this.deviceID = deviceID;
        this.devicename = devicename;
        this.type = type;
        this.status = status;
        this.purchase_date = purchase_date;
        this.warranty_period = warranty_period;
        this.user_id = user_id;
        this.location = location;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.putinstorageby = putinstorageby;
        this.takeoutstorageby = takeoutstorageby;
    }

    // Getters and setters
    public int getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getPurchase_date() {
        return purchase_date;
    }

    public void setPurchase_date(Date purchase_date) {
        this.purchase_date = purchase_date;
    }

    public String getWarranty_period() {
        return warranty_period;
    }

    public void setWarranty_period(String warranty_period) {
        this.warranty_period = warranty_period;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getPutinstorageby() {
        return putinstorageby;
    }

    public void setPutinstorageby(String putinstorageby) {
        this.putinstorageby = putinstorageby;
    }

    public String getTakeoutstorageby() {
        return takeoutstorageby;
    }

    public void setTakeoutstorageby(String takeoutstorageby) {
        this.takeoutstorageby = takeoutstorageby;
    }
}
