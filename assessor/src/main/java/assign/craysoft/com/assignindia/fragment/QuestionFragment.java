package assign.craysoft.com.assignindia.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import assign.craysoft.com.assignindia.R;
import assign.craysoft.com.assignindia.adapter.QuestionAdapter;
import assign.craysoft.com.assignindia.bean.QuestionBean;
import assign.craysoft.com.assignindia.network.NetworkUtil;
import assign.craysoft.com.assignindia.util.Constant;

public class QuestionFragment extends Fragment {
    private static final String EXAM_TYPE = "EXAM_TYPE";
    private static final String STUDENT_ID = "STUDENT_ID";
    private String examType;
    private String studentId;
    private QuestionInteractionListener listener;
    private ArrayList<QuestionBean> questionBeanArrayList = new ArrayList<QuestionBean>();
    private QuestionBean questionBean;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private QuestionAdapter assessmentAdapter;

    public QuestionFragment() {
    }

    public static QuestionFragment newInstance(String examType, String studentId) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putString(EXAM_TYPE, examType);
        args.putString(STUDENT_ID, studentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            examType = getArguments().getString(EXAM_TYPE);
            studentId = getArguments().getString(STUDENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity(), 4);
        recyclerView.setLayoutManager(layoutManager);
        assessmentAdapter = new QuestionAdapter(questionBeanArrayList, new QuestionAdapter.QuestionChangeListener() {
            @Override
            public void onClick(QuestionBean questionBean) {
                if (listener != null)
                    listener.onQuestionClick(questionBean);
            }
        });
        recyclerView.setAdapter(assessmentAdapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onSaveInstanceState( Bundle outState) {
        super.onSaveInstanceState(outState);
        if (questionBean != null) {
            outState.putSerializable(QuestionBean.class.getName(), questionBean);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            if (questionBean == null) {
                questionBean = (QuestionBean) savedInstanceState.getSerializable(QuestionBean.class.getName());
                if (questionBean != null && listener != null) {
                    listener.questionReceived(questionBean, true);

                }
            }
        }
        if (questionBean == null)
            loadQuestionSet();
    }

    private void loadQuestionSet() {
        JSONObject params = new JSONObject();
        try {
            params.put("examType", examType);
            params.put("studentId", studentId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = Constant.URL_QUESTION_SET;
        switch (examType) {
            case "Viva":
                url = new Random().nextInt() % 2 == 0 ? "questionSet.json" : "questionSet2.json";
                break;
            case "Demo":
                url = new Random().nextInt() % 2 == 0 ? "questionSet1.json" : "questionSet3.json";
                break;
        }

        NetworkUtil.getInstance().connectPostRequest(getActivity(), url, params.toString(), new NetworkUtil.CallBack() {
            @Override
            public void done(JSONObject jsonObject) {
                QuestionBean questionBean = new QuestionBean(jsonObject);
                if (listener != null)
                    listener.questionReceived(questionBean, false);
            }

            @Override
            public void error(JSONObject jsonObject) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        }, true);
    }

    private void saveState(QuestionBean questionBean, ArrayList<QuestionBean> arrayList) {
        if (arrayList != null || questionBean != null) {
            if (questionBean != null) {
                this.questionBean = questionBean;
            }
        } else {
            this.questionBean = null;
        }
    }

    public void setQuestionInteractionListener(QuestionInteractionListener questionInteractionListener) {
        this.listener = questionInteractionListener;
    }

    public abstract class QuestionInteractionListener {

        private QuestionBean questionBean;
        private boolean isSubmit;
        private Uri idImageUri;
        private Uri studentImageUri;

        public QuestionInteractionListener() {
            listener = this;
        }

        public Uri getIdImageUri() {
            return idImageUri;
        }

        public void setIdImageUri(Uri idImageUri) {
            this.idImageUri = idImageUri;
        }

        public Uri getStudentImageUri() {
            return studentImageUri;
        }

        public void setStudentImageUri(Uri studentImageUri) {
            this.studentImageUri = studentImageUri;
        }

        private void questionReceived(QuestionBean questionBean, boolean isRestored) {
            if (questionBean != null) {
                this.questionBean = questionBean;
                if (!isRestored)
                    saveState(questionBean, null);
                QuestionBean[] questionBeans = questionBean.getRows();
                if (questionBeans != null) {
                    questionBeanArrayList.clear();
                    for (QuestionBean bean : questionBeans) {
                        if (bean != null) {
                            questionBeanArrayList.add(bean);
                        }
                    }
                }
                if (!isRestored)
                    saveState(null, questionBeanArrayList);
                if (assessmentAdapter != null)
                    assessmentAdapter.notifyDataSetChanged();
                onQuestionsReceived(questionBeanArrayList.size());
            }
        }

        public abstract void onQuestionsReceived(int questionCount);

        public abstract boolean onQuestionsChangeRequest(QuestionBean question);

        public final int getQuestionCount() {
            listener = this;
            return questionBeanArrayList.size();
        }

        public final int getAttemptedCount() {
            listener = this;
            int count = 0;
            for (QuestionBean bean : questionBeanArrayList) {
                if (bean.getMark() > 0) {
                    count++;
                }
            }
            return count;
        }

        private void questionUpdate(QuestionBean questionBean) {
            if (assessmentAdapter != null)
                for (int index = 0; index < questionBeanArrayList.size(); index++) {
                    QuestionBean bean = questionBeanArrayList.get(index);
                    if (bean.getQuestionId() == questionBean.getQuestionId()) {
                        assessmentAdapter.notifyItemChanged(index);
                        break;
                    }
                }
        }

        private void onQuestionClick(QuestionBean questionBean) {
            boolean status = onQuestionsChangeRequest(questionBean.clone());
            if (status && !questionBean.isDisplayed()) {
                questionBean.setDisplayed(true);
                questionUpdate(questionBean);
            }
        }

        public final int getNonAttemptedCount() {
            listener = this;
            return getQuestionCount() - getAttemptedCount();
        }

        public int getMaxMark() {
            if (questionBean != null)
                return questionBean.getMaxMark();
            return 0;
        }


        public String getType() {
            return questionBean.getType();
        }

        public long getDuration() {
            if (questionBean != null)
                return questionBean.getDuration();
            return 0;
        }

        public final QuestionBean getFirstNonAttemptedQuestion() {
            listener = this;
            for (QuestionBean bean : questionBeanArrayList) {
                if (bean.getMark() == 0) {
                    if (!bean.isDisplayed()) {
                        bean.setDisplayed(true);
                        questionUpdate(bean);
                    }
                    return bean.clone();
                }
            }
            return null;
        }

        public final QuestionBean getNextQuestion() {
            listener = this;
            for (QuestionBean bean : questionBeanArrayList) {
                if (bean.getMark() == 0 && !bean.isDisplayed()) {
                    bean.setDisplayed(true);
                    questionUpdate(bean);
                    return bean.clone();
                }
            }
            return null;
        }

        public final boolean onQuestionAnswered(QuestionBean question) {
            listener = this;
            if (question != null) {
                for (QuestionBean bean : questionBeanArrayList) {
                    if (bean.getQuestionId() == question.getQuestionId()) {
                        if (bean.isDisplayed()) {
                            bean.setRemark(question.getRemark());
                            bean.setDuration(question.getDuration());
                            bean.setMark(question.getMark());
                            bean.setVideoUri(question.getVideoUri());
                            questionUpdate(bean);
                            return true;
                        }
                        break;
                    }
                }
            }
            return false;
        }

        public boolean isSubmit() {
            return isSubmit;
        }

        public final void submitAllAnswers(Context context, double duration) {
            listener = this;
            isSubmit = true;
            if (getNonAttemptedCount() > 0)
                Toast.makeText(context, "Attempted " + getAttemptedCount() + " out of " + getQuestionCount(), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, "All attempted" + getQuestionCount(), Toast.LENGTH_SHORT).show();
        }
    }
}