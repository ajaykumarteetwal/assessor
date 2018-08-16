package assign.craysoft.com.assignindia.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import assign.craysoft.com.assignindia.R;
import assign.craysoft.com.assignindia.adapter.AssessmentAdapter;
import assign.craysoft.com.assignindia.bean.AssessmentAttempted;
import assign.craysoft.com.assignindia.bean.AssessmentInstruction;
import assign.craysoft.com.assignindia.network.NetworkUtil;
import assign.craysoft.com.assignindia.util.Constant;

public class AssessmentFragment extends Fragment {
    private static final String ASSESSMENT_TYPE = "ASSESSMENT_TYPE";
    private int type;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private AssessmentAdapter assessmentAdapter;

    public AssessmentFragment() {
    }

    public static AssessmentFragment newInstance(int type) {
        AssessmentFragment fragment = new AssessmentFragment();
        Bundle args = new Bundle();
        args.putInt(ASSESSMENT_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(ASSESSMENT_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assessment, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        assessmentAdapter = new AssessmentAdapter(new ArrayList(), type);
        recyclerView.setAdapter(assessmentAdapter);
        loadAssessment(type);
        return view;
    }

    private void loadAssessment(final int type) {
        JSONObject params = new JSONObject();
        try {
            params.put("type", type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url;
        switch (type) {
            case 0: {
                url = Constant.URL_ASSESSMENT_INST;
            }
            break;
            case 1:
                url = Constant.URL_ASSESSMENT_PENDING;
                break;
            case 2:
                url = Constant.URL_ASSESSMENT_ATTEMPTED;
                break;
            default: {
                url = Constant.URL_ASSESSMENT_COMPLETE;
                break;
            }
        }
        NetworkUtil.getInstance().connectPostRequest(getActivity(), url, params.toString(), new NetworkUtil.CallBack() {
            @Override
            public void done(JSONObject jsonObject) {
                switch (type) {
                    case 0: {
                        AssessmentInstruction instruction = new AssessmentInstruction(jsonObject);
                        for (AssessmentInstruction inst : instruction.getRows())
                            if (inst != null)
                                if (inst.getActionType() == type)
                                    assessmentAdapter.add(inst);
                    }
                    break;
                    case 1:
                    case 2:
                    default: {
                        AssessmentAttempted instruction = new AssessmentAttempted(jsonObject);
                        for (AssessmentAttempted inst : instruction.getRows())
                            if (inst != null)
                                assessmentAdapter.add(inst);
                        break;
                    }
                }
                assessmentAdapter.notifyDataSetChanged();
            }

            @Override
            public void error(JSONObject jsonObject) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        }, true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
