package assign.craysoft.com.assignindia.bean;

import org.json.JSONObject;

public class AssessmentInstruction extends Parent<AssessmentInstruction> {

    private String assessmentName;
    private String description;
    private String instructions;
    private String duration;
    private String startTime;
    private String endTime;
    private String passingPercentage;
    private String ssc;
    private String tp;
    private String batchId;
    private String checkBoxText;
    private String buttonText;
    private int actionType;

    public AssessmentInstruction(JSONObject jsonObject) {
        super(jsonObject);
    }

    public String getAssessmentName() {
        return assessmentName;
    }

    public String getDescription() {
        return description;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getDuration() {
        return duration;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getPassingPercentage() {
        return passingPercentage;
    }

    public String getSsc() {
        return ssc;
    }

    public String getTp() {
        return tp;
    }

    public String getBatchId() {
        return batchId;
    }

    public String getCheckBoxText() {
        return checkBoxText;
    }

    public String getButtonText() {
        return buttonText;
    }

    public int getActionType() {
        return actionType;
    }
}
