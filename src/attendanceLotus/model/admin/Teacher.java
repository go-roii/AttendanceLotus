package attendanceLotus.model.admin;

public class Teacher {

    private int teacherID, userID;
    private String fName, mName, lName, username;

    public Teacher(int teacherID, int userID, String fName, String mName, String lName) {
        this.teacherID = teacherID;
        this.userID = userID;
        this.fName = fName;
        this.mName = mName;
        this.lName = lName;
    }

    public int getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getMName() {
        return mName;
    }

    public void setMName(String mName) {
        this.mName = mName;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Teacher(int teacherID, int userID, String fName, String mName, String lName, String username) {
        this.teacherID = teacherID;
        this.userID = userID;
        this.username = username;
        this.fName = fName;
        this.mName = mName;
        this.lName = lName;
    }

}
