package attendanceLotus.model.teacher;

public class Class {

    private int cclsu_ID, teacherID;
    private String courseName, yearBlock, subjectName;

    public Class(int cclsu_ID, int teacherID, String courseName, String yearBlock, String subjectName) {
        this.cclsu_ID = cclsu_ID;
        this.teacherID = teacherID;
        this.courseName = courseName;
        this.yearBlock = yearBlock;
        this.subjectName = subjectName;
    }

    public int getCclsu_ID() {
        return cclsu_ID;
    }

    public void setCclsu_ID(int cclsu_ID) {
        this.cclsu_ID = cclsu_ID;
    }

    public int getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
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

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
