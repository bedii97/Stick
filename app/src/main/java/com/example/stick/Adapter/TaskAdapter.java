package com.example.stick.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stick.DB.DatabaseHelper;
import com.example.stick.Model.TaskModel;
import com.example.stick.R;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.Calendar;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{

    private Context mContext;
    private List<TaskModel> mTaskList;
    private OnTaskClickListener mListener;
    private static final String TAG = "TaskAdapter";

    public interface OnTaskClickListener{
        void onTaskClick(int position);
        void onCheckClick(int position);
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
    public void onBindViewHolder(@NonNull TaskViewHolder holder, final int position) {
        //binding
        TaskModel currentTask = mTaskList.get(position);
        String content = currentTask.getContent();
        long dateInMilis = currentTask.getDate();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(dateInMilis);
        String date = c.getTime().toString();
        boolean status = currentTask.getStatus() != 0;
        holder.contentTV.setText(content);
        holder.dateTV.setText(date);
        holder.statusCB.setChecked(status);
        if (status) {
            holder.contentTV.setPaintFlags(holder.contentTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }/*else{
            holder.contentTV.setPaintFlags(holder.contentTV.getPaintFlags() & ~ Paint.STRIKE_THRU_TEXT_FLAG);
        }*/
        holder.statusCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mListener.onCheckClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTaskList.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder{
        //Tanımla
        TextView contentTV, dateTV;
        MaterialCheckBox statusCB;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            //init
            contentTV = itemView.findViewById(R.id.task_item_task_name_text_view);
            dateTV = itemView.findViewById(R.id.task_item_date_text_view);
            statusCB = itemView.findViewById(R.id.task_item_task_status_check_box);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onTaskClick(position);
                        }
                    }
                }
            });
        }
    }
}
