package attendanceLotus.model.admin;

public class CurriculumClass {
    //Main Columns - Laman ng Table
    private int classID;
    private int year;
    private char block;
    private String yearBlock;

    //For Linking - Siningit lang
    private int ccl_ID;

    public CurriculumClass(int classID, int year, char block, String yearBlock) {
        this.classID = classID;
        this.year = year;
        this.block = block;
        this.yearBlock = yearBlock;
    }

    public CurriculumClass(int ccl_ID, int classID, String yearBlock) {
        this.ccl_ID = ccl_ID;
        this.classID = classID;
        this.yearBlock = yearBlock;
    }

    public CurriculumClass(int classID, String yearBlock) {
        this.classID = classID;
        this.yearBlock = yearBlock;
    }

    public int getCcl_ID() {
        return ccl_ID;
    }

    public void setCcl_ID(int ccl_ID) {
        this.ccl_ID = ccl_ID;
    }

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public char getBlock() {
        return block;
    }

    public void setBlock(char block) {
        this.block = block;
    }

    public String getYearBlock() {
        return yearBlock;
    }

    public void setYearBlock(String yearBlock) {
        this.yearBlock = yearBlock;
    }

    @Override
    public String toString() {
        return this.getYearBlock();
    }
}
