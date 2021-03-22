package attendanceLotus.model.teacher;

public class Curriculum {

    private int courseID, cclsu_ID;
    private String courseName, yearBlock, subject;

    public Curriculum(int courseID, int cclsu_ID, String courseName, String yearBlock, String subject) {
        this.courseID = courseID;
        this.cclsu_ID = cclsu_ID;
        this.courseName = courseName;
        this.yearBlock = yearBlock;
        this.subject = subject;
    }

    public Curriculum(int cclsu_ID, String courseName, String yearBlock, String subject) {
        this.cclsu_ID = cclsu_ID;
        this.courseName = courseName;
        this.yearBlock = yearBlock;
        this.subject = subject;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public int getCclsu_ID() {
        return cclsu_ID;
    }

    public void setCclsu_ID(int cclsu_ID) {
        this.cclsu_ID = cclsu_ID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getYearBlock() {
        return yearBlock;
    }

    public void setYearBlock(String yearBlock) {
        this.yearBlock = yearBlock;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return this.getCourseName() + " " + getYearBlock() + " " + getSubject();
    }
}
