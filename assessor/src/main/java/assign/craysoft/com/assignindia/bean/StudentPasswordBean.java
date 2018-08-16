package assign.craysoft.com.assignindia.bean;

import org.json.JSONObject;

public class StudentPasswordBean extends Parent<StudentPasswordBean> {

    private int sno;
    private String studentName;
    private int studentId;
    private String studentPassword;
    private boolean studentLoginFirstTime;
    private boolean assessorAttempted;


    public StudentPasswordBean(JSONObject jsonObject) {
        super(jsonObject);
    }

    public int getSno() {
        return sno;
    }

    public String getStudentName() {
        return studentName;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStudentPassword() {
        return studentPassword;
    }

    public boolean isStudentLoginFirstTime() {
        return studentLoginFirstTime;
    }

    public boolean isAssessorAttempted() {
        return assessorAttempted;
    }
}
