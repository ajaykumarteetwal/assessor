package assign.craysoft.com.assignindia.bean;

import org.json.JSONObject;

public class AssessmentAttempted extends Parent<AssessmentAttempted> {

    private int sno;
    private String studentId;
    private String studentName;
    private String tp;
    private String enrolmentNumber;
    private String fatherHusband;
    private String vivaStatus;
    private String demoStatus;
    private long vivaDuration;
    private long demoDuration;

    public AssessmentAttempted(JSONObject jsonObject) {
        super(jsonObject);
    }

    public int getSno() {
        return sno;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getTp() {
        return tp;
    }

    public String getEnrolmentNumber() {
        return enrolmentNumber;
    }

    public String getFatherHusband() {
        return fatherHusband;
    }

    public String getVivaStatus() {
        return vivaStatus;
    }

    public String getDemoStatus() {
        return demoStatus;
    }

    public long getVivaDuration() {
        return vivaDuration;
    }

    public long getDemoDuration() {
        return demoDuration;
    }
}
