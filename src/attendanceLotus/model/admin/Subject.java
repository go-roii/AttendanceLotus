package attendanceLotus.model.admin;

public class Subject {

    private int subjectID;
    private String subjectName;

    public Subject(int subjectID, String subjectName) {
        this.subjectID = subjectID;
        this.subjectName = subjectName;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String name) {
        this.subjectName = subjectName;
    }

    @Override
    public String toString() {
        return this.getSubjectName();
    }

}
