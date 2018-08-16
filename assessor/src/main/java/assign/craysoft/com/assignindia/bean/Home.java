package assign.craysoft.com.assignindia.bean;

import org.json.JSONObject;

public class Home extends Parent<Home> {

    private String welcomeMessage;
    private int todayTaskCount;
    private String remark;
    private String subject;
    private String teacherName;
    private String teacherEmailId;

    public Home(JSONObject jsonObject) {
        super(jsonObject);
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public int getTodayTaskCount() {
        return todayTaskCount;
    }

    public String getRemark() {
        return remark;
    }

    public String getSubject() {
        return subject;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getTeacherEmailId() {
        return teacherEmailId;
    }
}
