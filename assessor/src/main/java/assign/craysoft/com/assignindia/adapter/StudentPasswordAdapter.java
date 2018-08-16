package assign.craysoft.com.assignindia.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import assign.craysoft.com.assignindia.R;
import assign.craysoft.com.assignindia.bean.StudentPasswordBean;

public class StudentPasswordAdapter extends RecyclerView.Adapter<StudentPasswordAdapter.ViewHolder> {
    private final ArrayList<StudentPasswordBean> list;
    private final int type;

    public StudentPasswordAdapter(ArrayList<StudentPasswordBean> list, int type) {
        this.list = list;
        this.type = type;
    }

    @Override
    public StudentPasswordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutID = R.layout.item_student_password;
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutID, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final StudentPasswordBean bean = list.get(position);
        holder.sno.setText(String.valueOf(bean.getSno()));
        holder.studentName.setText(bean.getStudentName());
        holder.studentId.setText(String.valueOf(bean.getStudentId()));
        holder.assessorAttempted.setText(bean.isAssessorAttempted() ? "Yes" : "No");
        holder.viewPassword.setText("View");
        holder.viewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.viewPassword.setText(bean.getStudentPassword());
            }
        });
    }

    public void add(StudentPasswordBean t) {
        list.add(t);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView sno;
        private TextView studentName;
        private TextView studentId;
        private TextView assessorAttempted;
        private Button viewPassword;

        public ViewHolder(View v) {
            super(v);
            sno = itemView.findViewById(R.id.sno);
            studentName = itemView.findViewById(R.id.studentName);
            studentId = itemView.findViewById(R.id.studentId);
            viewPassword = itemView.findViewById(R.id.viewPasswordButton);
            assessorAttempted = itemView.findViewById(R.id.examAttempted);
        }
    }
}