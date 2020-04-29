package com.example.stick.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.stick.DB.DatabaseHelper;
import com.example.stick.Activity.NoteDetailActivity;
import com.example.stick.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class CreateNoteDialog extends DialogFragment {

    private static final String TAG = "CreateNoteDialog";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new MaterialAlertDialogBuilder(getActivity())
                .setView(R.layout.dialog_create_note)
                .setTitle("Sample")
                .setPositiveButton(R.string.createNoteDialogPositiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText titleET = getDialog().findViewById(R.id.create_note_dialog_title_et);
                        String title = titleET.getText().toString().trim();
                        if(inputCheck(title)){ //input check
                            Log.d(TAG, "onClick: Girdi");
                            DatabaseHelper db = new DatabaseHelper(getContext());
                            long noteID = db.insertNote(title, "red");
                            openNoteDetailActivity(noteID);
                        }
                    }
                })
                .setNegativeButton(R.string.createNoteDialogNegativeButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }

    private void openNoteDetailActivity(long id) {
        Intent intent = new Intent(getActivity(), NoteDetailActivity.class);
        intent.putExtra(NoteDetailActivity.NOTEID, id);
        startActivity(intent);
    }

    /**
     * Return true if everything ok
     * @param input
     * @return
     */
    private boolean inputCheck(String input){
        return input.length() > 0 ? true : false;
    }
}
