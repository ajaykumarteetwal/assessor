package assign.craysoft.com.assignindia.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import assign.craysoft.com.assignindia.R;
import assign.craysoft.com.assignindia.bean.Home;

public class DashboardFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private Home home;

    public DashboardFragment() {
    }

    public static DashboardFragment newInstance(Home home) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, home);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            home = (Home) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deshboard, container, false);
        TextView textView = view.findViewById(R.id.homeTextView);
        textView.setText(home.getTeacherName());
        return view;
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
