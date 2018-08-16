package assign.craysoft.com.assignindia.bean;

import android.net.Uri;

import org.json.JSONObject;

public class QuestionBean extends Parent<QuestionBean> {
    private int sno;
    private long duration;
    private int maxMark;
    private String question;
    private boolean isDisplayed;
    private long startTime;
    private int mark;
    private String remark;
    private String questionSetId;
    private int questionId;
    private Uri videoUri;
    private String type;

    public QuestionBean(JSONObject jsonObject) {
        super(jsonObject);
    }

    public int getSno() {
        return sno;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getMaxMark() {
        return maxMark;
    }

    public String getQuestion() {
        return question;
    }

    public boolean isDisplayed() {
        return isDisplayed;
    }

    public void setDisplayed(boolean displayed) {
        isDisplayed = displayed;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public String getQuestionSetId() {
        return questionSetId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Uri getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(Uri videoUri) {
        this.videoUri = videoUri;
    }

    public String getType() {
        return type;
    }

    @Override
    public QuestionBean clone() {
        try {
            return (QuestionBean) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return this;
    }
}