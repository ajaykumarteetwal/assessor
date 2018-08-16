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
import assign.craysoft.com.assignindia.adapter.StudentPasswordAdapter;
import assign.craysoft.com.assignindia.bean.StudentPasswordBean;
import assign.craysoft.com.assignindia.network.NetworkUtil;
import assign.craysoft.com.assignindia.util.Constant;

public class StudentPasswordFragment extends Fragment {
    private static final String ASSESSMENT_TYPE = "ASSESSMENT_TYPE";
    private int type;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private StudentPasswordAdapter assessmentAdapter;

    public StudentPasswordFragment() {
    }

    public static StudentPasswordFragment newInstance(int type) {
        StudentPasswordFragment fragment = new StudentPasswordFragment();
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
        assessmentAdapter = new StudentPasswordAdapter(new ArrayList<StudentPasswordBean>(), type);
        recyclerView.setAdapter(assessmentAdapter);
        loadStudentPassword(type);
        return view;
    }


    private void loadStudentPassword(final int type) {
        JSONObject params = new JSONObject();
        try {
            params.put("type", type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url;
        switch (type) {
            case 0: {
                url = Constant.URL_STUDENT_PASSWORD;
                break;
            }
            default: {
                url = Constant.URL_STUDENT_PASSWORD;
                break;
            }
        }
        NetworkUtil.getInstance().connectPostRequest(getActivity(), url, params.toString(), new NetworkUtil.CallBack() {
            @Override
            public void done(JSONObject jsonObject) {
                StudentPasswordBean instruction = new StudentPasswordBean(jsonObject);
                for (StudentPasswordBean inst : instruction.getRows())
                    assessmentAdapter.add(inst);
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
