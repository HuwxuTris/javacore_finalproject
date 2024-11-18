package Midterm.entities;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Course {
    private String courseId;
    private String courseName;
    private Map<DayOfWeek, List<TimeSlot>> studySchedule;
    private String teacherName;
    private Map<Student, StudentRecord> studentRecords;

    public static class  StudentRecord{
        private float midtermScore;
        private float finalScore;
        private float overallScore;
        private int attendance;

        public StudentRecord(float midtermScore, float finalScore, float overallScore, int attendance) {
            this.midtermScore = midtermScore;
            this.finalScore = finalScore;
            this.overallScore = overallScore;
            this.attendance = attendance;
        }

        public float getOverallScore() {
            return overallScore;
        }

        public void setOverallScore(float overallScore) {
            this.overallScore = overallScore;
        }

        public float getMidtermScore() {
            return midtermScore;
        }

        public void setMidtermScore(float midtermScore) {
            this.midtermScore = midtermScore;
        }

        public float getFinalScore() {
            return finalScore;
        }

        public void setFinalScore(float finalScore) {
            this.finalScore = finalScore;
        }

        public int getAttendance() {
            return attendance;
        }

        public void setAttendance(int attendance) {
            this.attendance = attendance;
        }
    }

    public Map<DayOfWeek, List<TimeSlot>> getStudySchedule() {
        return studySchedule;
    }

    public void setStudySchedule(Map<DayOfWeek, List<TimeSlot>> studySchedule) {
        this.studySchedule = studySchedule;
    }

    public Course(String courseId, String courseName, Map<DayOfWeek, List<TimeSlot>> studySchedule, String teacherName) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.studySchedule = studySchedule; // Now accepts the schedule
        this.teacherName = teacherName;
        this.studentRecords = new HashMap<>();
    }


    public static class TimeSlot {
        private DayOfWeek day;
        private LocalTime startTime;
        private LocalTime endTime;

        public TimeSlot(DayOfWeek day, LocalTime startTime, LocalTime endTime) {
            this.day = day;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public DayOfWeek getDay() {
            return day;
        }

        public void setDay(DayOfWeek day) {
            this.day = day;
        }

        // Getters and Setters
        public LocalTime getStartTime() {
            return startTime;
        }

        public void setStartTime(LocalTime startTime) {
            this.startTime = startTime;
        }

        public LocalTime getEndTime() {
            return endTime;
        }

        public void setEndTime(LocalTime endTime) {
            this.endTime = endTime;
        }

        @Override
        public String toString() {
            return startTime + " to " + endTime;
        }
    }
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }


    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Map<Student, StudentRecord> getStudentRecords() {
        return studentRecords;
    }

    public void setStudentRecords(Map<Student, StudentRecord> studentRecords) {
        this.studentRecords = studentRecords;
    }

    public void addStudent(Student student) {
        studentRecords.put(student, new StudentRecord(0, 0,0,0)); // Initialize with default score and attendance
    }
    public void updateMidtermScore(Student student, float midtermScore){
        if (studentRecords.containsKey(student)){
            studentRecords.get(student).setMidtermScore(midtermScore);
        }
    }
    public void updateAttendanceByOne(Student student){
        if (studentRecords.containsKey(student)){
            int updatedAttendance = studentRecords.get(student).getAttendance() + 1;
            studentRecords.get(student).setAttendance(updatedAttendance);
        }
    }
    public void updateFinalScore(Student student, float finalScore){
        if (studentRecords.containsKey(student)){
            studentRecords.get(student).setFinalScore(finalScore);
        }
    }
    public void updateOverallScore(Student student){
        if (studentRecords.containsKey(student)){
            StudentRecord a = studentRecords.get(student);
            float overallScore = (float) (a.getAttendance() * 10 /100) + (a.getMidtermScore()*30)/100 +  a.getFinalScore()*60/100 ;
            a.setOverallScore(overallScore);
        }
    }
}
