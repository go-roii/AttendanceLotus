package attendanceLotus.model.admin;

public class Student {

    private int studID, userID;
    private String fName, mName, lName, studentID;
    private int courseID;

    public Student(int studID, int userID, String fName, String mName, String lName, int courseID, String studentID) {
        this.studID = studID;
        this.userID = userID;
        this.fName = fName;
        this.mName = mName;
        this.lName = lName;
        this.courseID = courseID;
        this.studentID = studentID;
    }

    public int getStudID() {
        return studID;
    }

    public void setStudID(int studID) {
        this.studID = studID;
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

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    //Admin Students View
    private String courseName;

    public Student(int studID, int userID, String fName, String mName, String lName, int courseID, String studentID,
                   String courseName) {
        this.studID = studID;
        this.userID = userID;
        this.fName = fName;
        this.mName = mName;
        this.lName = lName;
        this.courseID = courseID;
        this.studentID = studentID;

        this.courseName = courseName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
