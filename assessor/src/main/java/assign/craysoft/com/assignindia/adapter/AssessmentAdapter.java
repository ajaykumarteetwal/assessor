package assign.craysoft.com.assignindia.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import assign.craysoft.com.assignindia.R;
import assign.craysoft.com.assignindia.activity.VivaActivity;
import assign.craysoft.com.assignindia.bean.AssessmentAttempted;
import assign.craysoft.com.assignindia.bean.AssessmentInstruction;
import assign.craysoft.com.assignindia.bean.Parent;
import assign.craysoft.com.assignindia.util.Util;

public class AssessmentAdapter<T extends Parent> extends RecyclerView.Adapter<AssessmentAdapter.ViewHolder> {
    private final ArrayList<T> list;
    private final int type;

    public AssessmentAdapter(ArrayList<T> list, int type) {
        this.list = list;
        this.type = type;
    }

    @Override
    public AssessmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutID;
        switch (type) {
            case 0:
                layoutID = R.layout.item_assessment_instruction;
                break;
            case 1:
            case 2:
            default:
                layoutID = R.layout.item_assessment_attempt;
                break;
        }
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutID, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        T t = list.get(position);
        switch (type) {
            case 0:
                if (t instanceof AssessmentInstruction) {
                    final AssessmentInstruction instruction = (AssessmentInstruction) t;
                    holder.assessmentName.setText(instruction.getAssessmentName());
                    holder.description.setText(instruction.getDescription());
                    holder.duration.setText(instruction.getDuration());
                    holder.startTime.setText(instruction.getStartTime());
                    holder.endTime.setText(instruction.getEndTime());
                    holder.passingPercentage.setText(instruction.getPassingPercentage());
                    holder.ssc.setText(instruction.getSsc());
                    holder.tp.setText(instruction.getTp());
                    holder.batchId.setText(instruction.getBatchId());
                    holder.checkBoxText.setVisibility(instruction.getActionType() == 0 ? View.VISIBLE : View.GONE);
                    holder.checkBoxText.setText(instruction.getCheckBoxText());
                    holder.startButton.setText(instruction.getButtonText());
                    holder.checkBoxText.setEnabled(instruction.getActionType() == 0);
                    holder.startButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!holder.checkBoxText.isChecked()) {
                                Toast.makeText(holder.checkBoxText.getContext(), "Read Instructions First", Toast.LENGTH_SHORT).show();
                                return;
                            }
//                            instruction.getAssessmentName()
                            // Call Question Activity here
                        }
                    });
                    break;
                }
            case 1:
            case 2:
            default:
                if (t instanceof AssessmentAttempted) {
                    final AssessmentAttempted attempted = (AssessmentAttempted) list.get(position);
                    holder.sno.setText(attempted.getSno() + "");
                    holder.studentId.setText(attempted.getStudentId());
                    holder.studentName.setText(attempted.getStudentName());
                    holder.enrolmentNumber.setText(attempted.getEnrolmentNumber());
                    holder.fatherHusbandCount.setText(attempted.getFatherHusband());
                    holder.tp.setText(attempted.getTp());
                    if (attempted.getVivaDuration() > 0) {
                        holder.vivaDuration.setText(Util.checkDigit(attempted.getVivaDuration()));
                        holder.vivaRow.setVisibility(View.VISIBLE);
                    } else {
                        holder.vivaRow.setVisibility(View.GONE);
                    }
                    if (attempted.getDemoDuration() > 0) {
                        holder.demoDuration.setText(Util.checkDigit(attempted.getDemoDuration()));
                        holder.demoRow.setVisibility(View.VISIBLE);
                    } else {
                        holder.demoRow.setVisibility(View.GONE);
                    }
                    holder.vivaStatus.setText(attempted.getVivaStatus());
                    holder.demoStatus.setText(attempted.getDemoStatus());
                    View.OnClickListener listener = new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (view.getId() == R.id.demoStatus && attempted.getDemoDuration() == 0 ||
                                    view.getId() == R.id.vivaStatus && attempted.getVivaDuration() == 0) {
                                Intent intent = new Intent(holder.demoStatus.getContext(), VivaActivity.class);
                                if (view.getId() == R.id.demoStatus)
                                    intent.putExtra(VivaActivity.EXAM_TYPE, VivaActivity.ExamType.EXAM_TYPE_DEMO.ordinal());
                                else if (view.getId() == R.id.vivaStatus)
                                    intent.putExtra(VivaActivity.EXAM_TYPE, VivaActivity.ExamType.EXAM_TYPE_VIVA.ordinal());
                                intent.putExtra(VivaActivity.STUDENT_ID, attempted.getStudentId());
                                holder.demoStatus.getContext().startActivity(intent);
                            } else
                                Toast.makeText(holder.demoStatus.getContext(), "Attempted", Toast.LENGTH_SHORT).show();
                        }
                    };
                    holder.vivaStatus.setOnClickListener(listener);
                    holder.demoStatus.setOnClickListener(listener);
                    break;
                }
        }
    }

    public void add(T t) {
        list.add(t);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView assessmentName;
        private TextView description;
        private TextView duration;
        private TextView startTime;
        private TextView endTime;
        private TextView passingPercentage;
        private TextView ssc;
        private TextView tp;
        private TextView batchId;
        private CheckBox checkBoxText;
        private Button startButton;


        private TextView sno;
        private TextView studentId;
        private TextView studentName;
        private TextView enrolmentNumber;
        private TextView fatherHusbandCount;
        private TextView vivaDuration;
        private TextView demoDuration;
        private Button vivaStatus;
        private Button demoStatus;
        private View vivaRow;
        private View demoRow;

        public ViewHolder(View v) {
            super(v);
            assessmentName = itemView.findViewById(R.id.assessmentName);
            description = itemView.findViewById(R.id.descInstruction);
            duration = itemView.findViewById(R.id.duration);
            startTime = itemView.findViewById(R.id.startTime);
            endTime = itemView.findViewById(R.id.endTime);
            passingPercentage = itemView.findViewById(R.id.passingPercentage);
            ssc = itemView.findViewById(R.id.ssc);
            tp = itemView.findViewById(R.id.tp);
            batchId = itemView.findViewById(R.id.batchId);
            checkBoxText = itemView.findViewById(R.id.checkBoxText);
            startButton = itemView.findViewById(R.id.startButton);

            sno = itemView.findViewById(R.id.sno);
            studentId = itemView.findViewById(R.id.studentId);
            studentName = itemView.findViewById(R.id.studentName);
            enrolmentNumber = itemView.findViewById(R.id.enrolmentNumber);
            fatherHusbandCount = itemView.findViewById(R.id.fatherHusbandCount);
            vivaDuration = itemView.findViewById(R.id.vivaDuration);
            demoDuration = itemView.findViewById(R.id.demoDuration);
            vivaStatus = itemView.findViewById(R.id.vivaStatus);
            demoStatus = itemView.findViewById(R.id.demoStatus);
            vivaRow = itemView.findViewById(R.id.vivaRow);
            demoRow = itemView.findViewById(R.id.demoRow);

        }
    }
}

