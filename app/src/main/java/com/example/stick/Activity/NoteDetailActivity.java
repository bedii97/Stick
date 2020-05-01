package com.example.stick.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.stick.Adapter.TaskAdapter;
import com.example.stick.DB.DatabaseHelper;
import com.example.stick.Dialog.BottomDialog;
import com.example.stick.Model.NoteModel;
import com.example.stick.Model.TaskModel;
import com.example.stick.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.List;

public class NoteDetailActivity extends AppCompatActivity {

    private static final String TAG = "NoteDetailActivity";
    public static final String NOTEID = "noteid";
    private long mNoteID;
    private NoteModel mNote;

    //Views
    private EditText titleET;
    private RecyclerView taskRV;
    private TaskAdapter mAdapter;

    private List<TaskModel> mTaskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        //Init Views
        initViews();
        //GetNoteID
        Intent intent = getIntent();
        mNoteID = intent.getLongExtra(NOTEID, -1);
        //GetNote
        getNote();
        //GetTasks
        getTasksFromDB();
        //SetTasks
        setTaskData();
        //SetUp Toolbar
        Toolbar toolbar = findViewById(R.id.activity_note_details_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_icon_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FloatingActionButton fab = findViewById(R.id.activity_note_details_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet(-1);
            }
        });
        //Get Note From Database
    }

    private void initViews() {
        titleET = findViewById(R.id.activity_note_details_note_title_edit_text);
        taskRV = findViewById(R.id.activity_note_details_recycler_view);
        taskRV.setNestedScrollingEnabled(false);
    }

    private void getNote() {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        if (mNoteID != -1) {
            mNote = db.getNote(mNoteID);
            setNoteData();//Setting note datas to views
        } else {
            Toast.makeText(this, "Error Occured", Toast.LENGTH_SHORT).show();
        }
    }

    private void setNoteData() {
        String title = mNote.getTitle();
        titleET.setText(title);
    }

    private void getTasksFromDB(){
        //GetTasks
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        mTaskList = db.getTasks(mNoteID);
    }

    private void setTaskData(){
        mAdapter = new TaskAdapter(this, mTaskList);
        mAdapter.setOnTaskClickListener(new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onTaskClick(int position) {
                //Recyclerview
                taskRV.scrollToPosition(position);
                //Open bottom dialog
                showBottomSheet(mTaskList.get(position).getId());
            }

            @Override
            public void onCheckClick(int position) {
                DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                long id = mTaskList.get(position).getId();
                int status = mTaskList.get(position).getStatus();
                db.updateTaskStatus(id, status);
                refreshTasks();
            }
        });
        taskRV.setHasFixedSize(true);
        taskRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        taskRV.setAdapter(mAdapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mAdapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(taskRV);

    }

    public void showBottomSheet(long id) {
        BottomDialog bottomDialog = BottomDialog.newInstance();
        bottomDialog.setOnTaskAddListener(new BottomDialog.BottomDialogListener() {
            @Override
            public void onAddItemClick(TaskModel task) {
                Log.d(TAG, "Listener İçinde");
                addTask(task);
            }

            @Override
            public void onDialogClose() {
                Log.d(TAG, "Dialog Kapandı");
                refreshTasks();
            }
        });
        Bundle bundle = new Bundle();
        bundle.putLong(BottomDialog.NOTE_ID, mNoteID);
        bundle.putLong(BottomDialog.TASK_ID, id);
        bottomDialog.setArguments(bundle);
        bottomDialog.show(getSupportFragmentManager(), BottomDialog.TAG);
    }

    private void addTask(TaskModel task){
        String content = task.getContent();
        int status = task.getStatus();
        long parentID = task.getParentID();
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        long taskID = db.insertTask(content, status, parentID);
        //refreshTasks();
        //Insert Animation
        TaskModel newTask = db.getTask(taskID);
        mTaskList.add(0, newTask);
        mAdapter.notifyItemInserted(0);
        taskRV.scrollToPosition(0);
    }

    private void refreshTasks(){
        getTasksFromDB();
        setTaskData();
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
        String title = mNote.getTitle().trim();
        String inputTitle = titleET.getText().toString().trim();
        if (!inputTitle.equals(title)) {
            //Update DB Title
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            db.updateTitle(mNoteID, inputTitle);
            Log.d(TAG, "onDestroy: Updated");
        }
    }
}
