package Entity;

public class DeviceApply {
    private int applyID;
    private int deviceID;
    private String devicename;
    private int userID;
    private String username;
    private String applyStatus;
    private String approvedby;
    private String returnStatus;
    private String applyPeriod;

    public DeviceApply(){
    }

    public DeviceApply(int applyID, int deviceID, String devicename, int userID, String username, String applyStatus, String approvedby, String returnStatus, String applyPeriod) {
        this.applyID = applyID;
        this.deviceID = deviceID;
        this.devicename = devicename;
        this.userID = userID;
        this.username = username;
        this.applyStatus = applyStatus;
        this.approvedby = approvedby;
        this.returnStatus = returnStatus;
        this.applyPeriod = applyPeriod;
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
}

