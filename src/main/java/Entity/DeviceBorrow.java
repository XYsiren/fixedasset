package Entity;

import java.util.Date;

public class DeviceBorrow {
    private int borrowingID;
    private int deviceID;
    private String devicename;
    private int userID;
    private String username;
    private int borrowNumber;
    private String borrowStatus;
    private String approvedby;
    private String returnStatus;
    private String borrowPeriod;
    private Date returnDueDate;

    public DeviceBorrow(){
    }

    public DeviceBorrow(int borrowingID, int deviceID, String devicename, int userID, String username, int borrowNumber, String borrowStatus, String approvedby, String returnStatus, String borrowPeriod, Date returnDueDate) {
        this.borrowingID = borrowingID;
        this.deviceID = deviceID;
        this.devicename = devicename;
        this.userID = userID;
        this.username = username;
        this.borrowNumber = borrowNumber;
        this.borrowStatus = borrowStatus;
        this.approvedby = approvedby;
        this.returnStatus = returnStatus;
        this.borrowPeriod = borrowPeriod;
        this.returnDueDate = returnDueDate;
    }

    public int getBorrowingID() {
        return borrowingID;
    }

    public void setBorrowingID(int borrowingID) {
        this.borrowingID = borrowingID;
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

    public int getBorrowNumber() {
        return borrowNumber;
    }

    public void setBorrowNumber(int borrowNumber) {
        this.borrowNumber = borrowNumber;
    }

    public String getBorrowStatus() {
        return borrowStatus;
    }

    public void setBorrowStatus(String borrowStatus) {
        this.borrowStatus = borrowStatus;
    }

    public String getApprovedby() {
        return approvedby;
    }

    public void setApprovedby(String approvedby) {
        this.approvedby = approvedby;
    }

    public String getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(String returnStatus) {
        this.returnStatus = returnStatus;
    }
    public String getBorrowPeriod() {
        return borrowPeriod;
    }

    public void setBorrowPeriod(String borrowPeriod) {
        this.borrowPeriod = borrowPeriod;
    }

    public Date getReturnDueDate() {
        return returnDueDate;
    }

    public void setReturnDueDate(Date returnDueDate) {
        this.returnDueDate = returnDueDate;
    }
}
