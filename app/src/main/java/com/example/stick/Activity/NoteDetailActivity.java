package com.example.stick.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.stick.DB.DatabaseHelper;
import com.example.stick.Dialog.BottomDialog;
import com.example.stick.Model.NoteModel;
import com.example.stick.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NoteDetailActivity extends AppCompatActivity {

    public static final String NOTEID = "noteid";
    private long mNoteID;
    private NoteModel mNote;

    //Views
    private EditText titleET;
    private RecyclerView taskRV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        //Init Views
        initViews();
        //GetNoteID
        Intent intent = getIntent();
        mNoteID = intent.getLongExtra(NOTEID, -1);
        //SetUp Toolbar
        Toolbar toolbar = findViewById(R.id.activity_note_details_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_icon_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Back Button Clicked", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fab = findViewById(R.id.activity_note_details_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //Get Note From Database
        getNote();
    }

    private void initViews() {
        titleET = findViewById(R.id.activity_note_details_note_title_edit_text);
        taskRV = findViewById(R.id.activity_note_details_recycler_view);
    }

    private void getNote(){
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        if(mNoteID != -1){
            mNote = db.getNote(mNoteID);
            setNoteData();//Setting note datas to views
        }else{
            Toast.makeText(this, "Error Occured", Toast.LENGTH_SHORT).show();
        }
    }

    private void setNoteData() {
        String title = mNote.getTitle();
        titleET.setText(title);
    }

    public void showBottomSheet() {
        BottomDialog bottomDialog = BottomDialog.newInstance();
        bottomDialog.show(getSupportFragmentManager(), BottomDialog.TAG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_note_details_menu, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
