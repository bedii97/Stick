package com.example.stick.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.stick.Adapter.NoteAdapter;
import com.example.stick.DB.DatabaseHelper;
import com.example.stick.Dialog.CreateNoteDialog;
import com.example.stick.Model.NoteModel;
import com.example.stick.R;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

public class MainActivity extends AppCompatActivity{

    public static final String TAG = "MainActivity";

    private ExtendedFloatingActionButton fab;
    private RecyclerView noteRV;
    private NoteAdapter adapter;
    private List<NoteModel> mNoteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_logo_24dp);
        initViews();
        getDataFromDB();
        setNotes();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.groundColorDark));
        }

        fab = findViewById(R.id.activity_main_extended_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNoteDialog();
            }
        });

    }

    private void initViews() {
        noteRV = findViewById(R.id.activity_main_recycler_view);
    }

    private void getDataFromDB() {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        mNoteList = db.getAllNotes();
    }

    private void setNotes() {
        adapter = new NoteAdapter(this, mNoteList);
        adapter.setOnNoteClickListener(new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(long id) {
                openNoteDetailActivity(id);
            }
        });
        noteRV.setHasFixedSize(true);
        noteRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        noteRV.setAdapter(adapter);
    }

    private void createNoteDialog() {
        CreateNoteDialog dialog = new CreateNoteDialog();
        dialog.show(getSupportFragmentManager(), "createDialog");
    }

    private void openNoteDetailActivity(long id) {
        Intent intent = new Intent(this, NoteDetailActivity.class);
        intent.putExtra(NoteDetailActivity.NOTEID, id);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_theme) {
            return true;
        } else if (id == R.id.action_libraries) {
            startActivity(new Intent(this, OssLicensesMenuActivity.class));
            OssLicensesMenuActivity.setActivityTitle(getString(R.string.action_libraries));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        doInBackGround();
    }

    private void doInBackGround() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getDataFromDB();
                setNotes();
            }
        }, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noteRV = null;
        adapter = null;
    }
}
