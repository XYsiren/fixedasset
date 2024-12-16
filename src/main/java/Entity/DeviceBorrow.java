package Entity;

public class DeviceBorrow {
    private int borrowingID;
    private int deviceID;
    private String devicename;
    private int userID;
    private String username;
    private String returnStatus;
    private String borrowPeriod;

    public DeviceBorrow(){
    }



    public DeviceBorrow(int borrowingID, int deviceID, String devicename, int userID, String username, String returnStatus, String borrowPeriod) {
        this.borrowingID = borrowingID;
        this.deviceID = deviceID;
        this.devicename = devicename;
        this.userID = userID;
        this.username = username;
        this.returnStatus = returnStatus;
        this.borrowPeriod = borrowPeriod;
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
}
