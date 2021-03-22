package attendanceLotus.model.admin;

public class Course {

    private int courseID;
    private String courseName;
    private int isHidden;

    public Course(int courseID, String courseName, int isHidden) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.isHidden = isHidden;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(int isHidden) {
        this.isHidden = isHidden;
    }

    @Override
    public String toString() {
        return this.getCourseName();
    }
}
