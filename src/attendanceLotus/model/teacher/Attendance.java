package attendanceLotus.model.teacher;

import java.time.LocalDate;

public class Attendance {

    private int attendanceID, ssuID;
    private LocalDate attDate;
    private int status;

    public Attendance(int attendanceID, int ssuID, LocalDate attDate, int status) {
        this.attendanceID = attendanceID;
        this.ssuID = ssuID;
        this.attDate = attDate;
        this.status = status;
    }

    public int getAttendanceID() {
        return attendanceID;
    }

    public void setAttendanceID(int attendanceID) {
        this.attendanceID = attendanceID;
    }

    public int getSsuID() {
        return ssuID;
    }

    public void setSsuID(int ssuID) {
        this.ssuID = ssuID;
    }

    public LocalDate getAttDate() {
        return attDate;
    }

    public void setAttDate(LocalDate attDate) {
        this.attDate = attDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
