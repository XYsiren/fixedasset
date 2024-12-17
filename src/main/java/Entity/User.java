package Entity;

public class User {
    private int userID;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String isdeleted;
    private String deletedby;
    public User(){
    }

    public User(int userID, String username, String password, String email, String phone, String isdeleted, String deletedby) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.isdeleted = isdeleted;
        this.deletedby = deletedby;
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
    public void setUsername (String username) {
        this.username = username;
    }
    public String getPassword () {
        return password;
    }
    public void setPassword (String password){
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail (String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone (String phone) {
        this.phone = phone;
    }

    public String getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(String isdeleted) {
        this.isdeleted = isdeleted;
    }

    public String getDeletedby() {
        return deletedby;
    }

    public void setDeletedby(String deletedby) {
        this.deletedby = deletedby;
    }
}