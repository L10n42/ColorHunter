package com.example.colorhunter.main.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.colorhunter.R;
import com.example.colorhunter.main.adapters.DBAdapter;
import com.example.colorhunter.main.adapters.MyRVAdapterColors;
import com.example.colorhunter.main.custom_view.CustomColorData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private FloatingActionButton openCamera;
    private Toolbar toolbar;
    private RecyclerView myRecyclerView;

    private MyRVAdapterColors rvAdapterColors;

    private ArrayList<CustomColorData> colorData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        rvAdapterColors = new MyRVAdapterColors(this, colorData, getSupportFragmentManager());

        findAllViewById();

        setToolbar();

        showList(this, myRecyclerView);

    }

    private void setToolbar() {
        toolbar = findViewById(R.id.list_activity_toolBar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.text_color_1));
        setSupportActionBar(toolbar);
    }

    private void fillColorData() {
        DBAdapter myAdapter = new DBAdapter(ListActivity.this);
        SQLiteDatabase database = myAdapter.getWritableDatabase();

        Cursor cursor = database.query(DBAdapter.DATABASE_TABLE_COLORS, null, null, null, null, null, null);

        colorData.clear();

        if(cursor.moveToLast()){
            do{
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_COLOR_NAME));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_COLOR_DESCRIPTION));
                @SuppressLint("Range") String rgb = cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_COLOR_RGB));
                @SuppressLint("Range") String hex = cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_COLOR_HEX));

                CustomColorData cd = new CustomColorData(name, description, hex, rgb);
                colorData.add(cd);

            }while (cursor.moveToPrevious());
        }

        cursor.close();
        database.close();
    }

    public void callShowList() {
        showList(ListActivity.this, myRecyclerView);
    }

    public void showList(Context context, RecyclerView recyclerView){
        fillColorData();

        MyRVAdapterColors myRVAdapterColors = new MyRVAdapterColors(context, colorData, getSupportFragmentManager());

        recyclerView.setAdapter(myRVAdapterColors);
    }

    public void dismissActiveFragment() {
        Fragment frag = getSupportFragmentManager().findFragmentByTag("delete color dialog");
        Fragment frag_1 = getSupportFragmentManager().findFragmentByTag("edit color dialog");

        if (frag != null)
            dismissDialogFragment(frag);

        if (frag_1 != null)
            dismissDialogFragment(frag_1);
    }

    private void dismissDialogFragment(Fragment frag){
        DialogFragment df = (DialogFragment) frag;
        df.dismiss();
    }


    private void findAllViewById() {
        openCamera = findViewById(R.id.open_camera);
        myRecyclerView = findViewById(R.id.recyclerView);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        myRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0){
                    openCamera.setVisibility(View.INVISIBLE);
                }else{
                    openCamera.setVisibility(View.VISIBLE);
                }
            }
        });

        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_activity_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onQueryTextChange(String newText) {
                fillColorData();
                myRecyclerView.setAdapter(rvAdapterColors);
                rvAdapterColors.getFilter().filter(newText);
                rvAdapterColors.notifyDataSetChanged();
                return true;
            }
        });
        return true;
    }
}