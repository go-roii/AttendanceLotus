package attendanceLotus.model.teacher;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class Student {

    private int ssu_ID, studID;
    private String fName, mName, lName;
    private int present, absent, late, total;
    private boolean GenerateButtons;
    private RadioButton radioButton_present, radioButton_absent, radioButton_late;
    private ToggleGroup group = new ToggleGroup();

    public Student(int studID, String fName, String mName, String lName) {
        this.studID = studID;
        this.fName = fName;
        this.mName = mName;
        this.lName = lName;
    }

    public Student(int ssu_ID, int studID, String fName, String mName, String lName, boolean GenerateButtons) {
        this.ssu_ID = ssu_ID;
        this.studID = studID;
        this.fName = fName;
        this.mName = mName;
        this.lName = lName;
        this.GenerateButtons = GenerateButtons;

        if(GenerateButtons) {
            this.radioButton_present = new RadioButton();
            this.radioButton_present.setUserData(1);
            this.radioButton_absent = new RadioButton();
            this.radioButton_absent.setUserData(2);
            this.radioButton_late = new RadioButton();
            this.radioButton_late.setUserData(3);

            this.radioButton_present.getStyleClass().add("radio-button-present");
            this.radioButton_absent.getStyleClass().add("radio-button-absent");
            this.radioButton_late.getStyleClass().add("radio-button-late");
            this.radioButton_present.setToggleGroup(group);
            this.radioButton_absent.setToggleGroup(group);
            this.radioButton_late.setToggleGroup(group);
        }
    }

    public int getSsu_ID() {
        return ssu_ID;
    }

    public int getStudID() {
        return studID;
    }

    public String getFName() {
        return fName;
    }

    public String getMName() {
        return mName;
    }

    public String getLName() {
        return lName;
    }

    public int getPresent() {
        return present;
    }

    public int getAbsent() {
        return absent;
    }

    public int getLate() {
        return late;
    }

    public int getTotal() {
        return total;
    }

    public void setPresent(int present) {
        this.present = present;
    }

    public void setAbsent(int absent) {
        this.absent = absent;
    }

    public void setLate(int late) {
        this.late = late;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public RadioButton getRadioButton_present() {
        return radioButton_present;
    }

    public RadioButton getRadioButton_absent() {
        return radioButton_absent;
    }

    public RadioButton getRadioButton_late() {
        return radioButton_late;
    }

    public ToggleGroup getGroup() {
        return group;
    }

}
