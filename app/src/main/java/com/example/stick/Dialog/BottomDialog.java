package com.example.stick.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

import com.example.stick.Activity.NoteDetailActivity;
import com.example.stick.Adapter.TaskAdapter;
import com.example.stick.DB.DatabaseHelper;
import com.example.stick.Model.TaskModel;
import com.example.stick.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.List;

public class BottomDialog extends BottomSheetDialogFragment{
    public static final String TAG = "BottomDialog";
    private BottomDialogListener mListener;

    private long mNoteID;

    //Views
    private EditText contentET;
    private AppCompatImageButton addTaskBTN;

    public interface BottomDialogListener{
        void onAddItemClick(TaskModel task);
        void onDialogClose();
    }

    public void setOnTaskAddListener(BottomDialogListener listener){
        mListener = listener;
    }

    public static BottomDialog newInstance() {
        return new BottomDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNoteID = getArguments().getLong(NoteDetailActivity.NOTEID, -1);
        initViews(view);
        getTasksFromDB();
    }

    private void getTasksFromDB() {
    }

    private void initViews(View v) {
        contentET = v.findViewById(R.id.bottom_dialog_et);
        addTaskBTN = v.findViewById(R.id.bottom_dialog_btn);
        addTaskBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Tıkladı");
                String content = contentET.getText().toString().trim();
                if(!content.isEmpty()){ //Checkbox kontrolüde yap
                    long dateMilis = Calendar.getInstance().getTimeInMillis();
                    TaskModel task = new TaskModel(0, content, "true", dateMilis, mNoteID);
                    mListener.onAddItemClick(task);
                }
            }
        });
    }



    @Override
    public void onStart() {
        super.onStart();
        //Disable Dialog Dimming
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0f; //Dimming Rate 0.90f
        windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(windowParams);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof BottomDialogListener) {
            mListener = (BottomDialogListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ItemClickListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.onDialogClose();
        mListener = null;
    }
}
