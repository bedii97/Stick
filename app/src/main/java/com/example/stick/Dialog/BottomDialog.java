package com.example.stick.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.stick.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomDialog extends BottomSheetDialogFragment{
    public static final String TAG = "BottomDialog";
    private BottomDialogListener mListener;

    public interface BottomDialogListener{
        void onAddItemClick(String text);
        void onDialogClose();
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
        final EditText editText = view.findViewById(R.id.bottom_dialog_et);
        ImageButton button = view.findViewById(R.id.bottom_dialog_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddItemClick(editText.getText().toString());
                dismiss();
            }
        });
        //view.findViewById(R.id.bottom_dialog_et).setOnClickListener(this);
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
        if (context instanceof BottomDialogListener) {
            mListener = (BottomDialogListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.onDialogClose();
        mListener = null;
    }
}
