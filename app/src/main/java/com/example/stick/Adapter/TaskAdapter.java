package com.example.stick.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stick.Model.TaskModel;
import com.example.stick.R;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{

    private Context mContext;
    private List<TaskModel> mTaskList;
    private OnTaskClickListener mListener;
    private static final String TAG = "TaskAdapter";

    public interface OnTaskClickListener{
        void onTaskClick(long id);
    }

    public void setOnTaskClickListener(OnTaskClickListener listener){
        mListener = listener;
    }

    public TaskAdapter(Context mContext, List<TaskModel> mTaskList) {
        this.mContext = mContext;
        this.mTaskList = mTaskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        //binding
        TaskModel currentTask = mTaskList.get(position);
        holder.tv1.setText(Float.toString(currentTask.getId()));
        holder.tv2.setText(currentTask.getContent());
    }

    @Override
    public int getItemCount() {
        return mTaskList.size();
    }



    class TaskViewHolder extends RecyclerView.ViewHolder{
        //Tanımla
        TextView tv1, tv2;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            //init
            tv1 = itemView.findViewById(R.id.task_item_task_name_text_view);
            tv2 = itemView.findViewById(R.id.tast_item_date_text_view);
        }
    }
}
