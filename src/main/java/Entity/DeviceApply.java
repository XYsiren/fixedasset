package Entity;

import Dao.DeviceApplyDao;

import java.util.Date;

public class DeviceApply {
    private int applyID;
    private int deviceID;
    private String devicename;
    private int userID;
    private String username;
    private int applyNumber;
    private String applyStatus;
    private String approvedby;
    private String returnStatus;
    private String applyPeriod;
    private Date returnDueDate;

    public DeviceApply(){
    }

    public DeviceApply(int applyID, int deviceID, String devicename, int userID, String username,int applyNumber, String applyStatus, String approvedby, String returnStatus, String applyPeriod, Date returnDueDate) {
        this.applyID = applyID;
        this.deviceID = deviceID;
        this.devicename = devicename;
        this.userID = userID;
        this.username = username;
        this.applyNumber = applyNumber;
        this.applyStatus = applyStatus;
        this.approvedby = approvedby;
        this.returnStatus = returnStatus;
        this.applyPeriod = applyPeriod;
        this.returnDueDate = returnDueDate;
    }

    public int getApplyID() {
        return applyID;
    }

    public void setApplyID(int applyID) {
        this.applyID = applyID;
    }

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

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getApplyNumber() {
        return applyNumber;
    }

    public void setApplyNumber(int applyNumber) {
        this.applyNumber = applyNumber;
    }

    public String getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(String returnStatus) {
        this.returnStatus = returnStatus;
    }
    public String getApplyPeriod() {
        return applyPeriod;
    }

    public void setApplyPeriod(String applyPeriod) {
        this.applyPeriod = applyPeriod;
    }

    public String getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getApprovedby() {
        return approvedby;
    }

    public void setApprovedby(String approvedby) {
        this.approvedby = approvedby;
    }

    public Date getReturnDueDate() {
        return returnDueDate;
    }

    public void setReturnDueDate(Date returnDueDate) {
        this.returnDueDate = returnDueDate;
    }
}

