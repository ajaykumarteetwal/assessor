package assign.craysoft.com.assignindia.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import assign.craysoft.com.assignindia.R;
import assign.craysoft.com.assignindia.bean.QuestionBean;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    private final ArrayList<QuestionBean> list;
    private final QuestionChangeListener listener;

    public QuestionAdapter(ArrayList<QuestionBean> list, QuestionChangeListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public QuestionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final QuestionBean bean = list.get(position);
        int bgColor = Color.parseColor("#000000");
        int textColor = Color.parseColor("#ffffff");
        holder.questionNumberButton.setText(String.valueOf(bean.getSno()));
        if (bean.isDisplayed()) {
            if (bean.getMark() == 0) {
                bgColor = Color.parseColor("#FFE6EE9C");
                textColor = Color.parseColor("#000000");
            } else if (bean.getMark() != 0) {
                bgColor = Color.parseColor("#FF00C853");
                textColor = Color.parseColor("#000000");
            }
        }
        holder.questionNumberButton.setBackgroundColor(bgColor);
        holder.questionNumberButton.setTextColor(textColor);

        holder.questionNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onClick(bean);
            }
        });
    }

    public void add(QuestionBean t) {
        list.add(t);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface QuestionChangeListener {
        void onClick(QuestionBean questionBean);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Button questionNumberButton;

        public ViewHolder(View v) {
            super(v);
            questionNumberButton = itemView.findViewById(R.id.questionNumberButton);
        }
    }
}

