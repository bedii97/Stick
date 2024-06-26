package com.example.stick.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.stick.Adapter.NoteAdapter;
import com.example.stick.DB.DatabaseHelper;
import com.example.stick.Dialog.CreateNoteDialog;
import com.example.stick.Model.NoteModel;
import com.example.stick.R;
import com.example.stick.Storage.SortingPreference;
import com.example.stick.Storage.ThemePreference;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private ExtendedFloatingActionButton fab;
    private RecyclerView noteRV;
    private NoteAdapter adapter;
    private List<NoteModel> mNoteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAppTheme();
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

    private void setAppTheme() {
        ThemePreference preference = ThemePreference.getInstance(this);
        if (preference.isDarkTheme()) setTheme(R.style.AppThemeDark);
        else setTheme(R.style.AppTheme);
    }

    private void initViews() {
        noteRV = findViewById(R.id.activity_main_recycler_view);
    }

    private void getDataFromDB() {
        SortingPreference preference = SortingPreference.getInstance(this);
        int sortPreference = preference.getNotePreference();
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        mNoteList = db.getAllNotes(sortPreference);

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
        ThemePreference preference = ThemePreference.getInstance(this);
        boolean isDarkTheme = preference.isDarkTheme();
        menu.findItem(R.id.action_theme).setChecked(isDarkTheme);
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
            setThemePreference();
        } else if (id == R.id.action_libraries) {
            startActivity(new Intent(this, OssLicensesMenuActivity.class));
            OssLicensesMenuActivity.setActivityTitle(getString(R.string.action_libraries));
        } else if (id == R.id.action_sorting) {
            setSortingMenu();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setThemePreference() {
        ThemePreference preference = ThemePreference.getInstance(this);
        preference.quickSaveThemePreference();
        //item.setChecked(!item.isChecked());
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_zoom_enter, R.anim.anim_zoom_exit);
    }

    private void setSortingMenu() {
        //initialize preference
        final SortingPreference preference = SortingPreference.getInstance(this);
        //Prepare Menu Items
        CharSequence[] options = new CharSequence[3];
        options[0] = getString(R.string.sorting_dialog_option_by_alphabetic);
        options[1] = getString(R.string.sorting_dialog_option_by_date_latest);
        options[2] = getString(R.string.sorting_dialog_option_by_date_oldest);
        //Get sorting preference for selecting dialog item
        int chosen = preference.getNotePreference();
        new MaterialAlertDialogBuilder(this, R.style.createDialogTheme)
                .setTitle(R.string.action_sorting)
                .setSingleChoiceItems(options, chosen, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        preference.saveNotePreference(which);
                        doInBackGround();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        doInBackGround();
    }

    private void doInBackGround() {
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getDataFromDB();
                    setNotes();
                }
            }, 500);
        } catch (NullPointerException e) {
            Log.d(TAG, "doInBackGround: " + e.getLocalizedMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noteRV = null;
        adapter = null;
    }
}
