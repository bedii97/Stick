package com.example.stick.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stick.DB.DatabaseHelper;
import com.example.stick.Helper.DateTimeHelper;
import com.example.stick.Model.TaskModel;
import com.example.stick.R;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context mContext;
    private List<TaskModel> mTaskList;
    private OnTaskClickListener mListener;
    //Removed
    //private List<TaskModel> mRemovedTasks = new ArrayList<>();
    private HashMap<Integer, TaskModel> mRemovedTasks = new HashMap<Integer, TaskModel>();
    private static final String TAG = "TaskAdapter";

    public interface OnTaskClickListener {
        void onTaskClick(int position);

        void onCheckClick(int position);
    }

    public void setOnTaskClickListener(OnTaskClickListener listener) {
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
        long timeInMillis = currentTask.getDate();
        String date = new DateTimeHelper().getDateTime(timeInMillis);
        boolean status = currentTask.getStatus() != 0;
        holder.contentTV.setText(content);
        holder.dateTV.setText(date);
        holder.statusCB.setChecked(status);

        //Change Checked Item Text Color
        TypedValue typedValue = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.thirdTextColor, typedValue, true);
        @ColorInt int checkedColor = typedValue.data;

        if (status) {
            holder.contentTV.setPaintFlags(holder.contentTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.contentTV.setTextColor(checkedColor);
            holder.dateTV.setTextColor(checkedColor);
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

    public void hideItem(final int position, RecyclerView.ViewHolder holder) {
        //Position sürekli 0. index olarak geldiği için, swipe tan sonra silinmesi gereken item var mı kontrol ediyorum.
        if (mRemovedTasks.containsKey(position)) {
            deleteSingleItem(position); //Varsa itemi sql den siliyorum
            mRemovedTasks.remove(position); //Silinmesi gerekenler listesindende siliyorum
        }
        mRemovedTasks.put(position, mTaskList.get(position));
        mTaskList.remove(position);
        notifyItemRemoved(position);

        //SnackBar Colors
        TypedValue typedValue = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.colorSurface, typedValue, true);
        @ColorInt int backgroundColor = typedValue.data;

        TypedValue typedValue1 = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.colorSecondary, typedValue1, true);
        @ColorInt int actionTextColor = typedValue1.data;

        TypedValue typedValue2 = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.primaryTextColor, typedValue2, true);
        @ColorInt int textColor = typedValue2.data;

        Snackbar.make(holder.itemView, R.string.activity_note_details_snack_bar_deleted, Snackbar.LENGTH_LONG)
                .setBackgroundTint(backgroundColor)
                .setAction(R.string.activity_note_details_snack_bar_undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        undoDelete(position);
                    }
                })
                .setActionTextColor(actionTextColor)
                .setTextColor(textColor)
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                    }
                }).show();
        Log.d(TAG, "hideItem: Task Hided");

    }

    //Kaydırma işleminde silinmesi gereken item varsa bu metodla siliniyor
    private void deleteSingleItem(int position) {
        TaskModel myTask = mRemovedTasks.get(position);
        long taskID = myTask.getId();
        new DatabaseHelper(mContext).deleteTask(taskID);
        Log.d(TAG, "deleteSingleItem: Task Deleted!");
    }

    //Sadece bir item silmişse deleteSingleItem metodu çalışmayacağı için ve son itemide silmeyeceği için from exitta bu metod çalışıyor ve siliyor
    public void deleteItem() {
        for (Map.Entry<Integer, TaskModel> task : mRemovedTasks.entrySet()) {
            TaskModel myTask = task.getValue();
            long taskID = myTask.getId();
            new DatabaseHelper(mContext).deleteTask(taskID);
            Log.d(TAG, "deleteItem: Task Deleted!");
        }
    }

    private void undoDelete(int position) {
        mTaskList.add(position, mRemovedTasks.get(position));
        mRemovedTasks.remove(position);
        notifyItemInserted(position);
        Log.d(TAG, "undoDelete: Undo Task");
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
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
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onTaskClick(position);
                        }
                    }
                }
            });
        }
    }
}
