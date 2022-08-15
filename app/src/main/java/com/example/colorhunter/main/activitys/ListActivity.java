package com.example.colorhunter.main.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

    private ArrayList<CustomColorData> colorData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

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

    public void showList(Context context, RecyclerView recyclerView){
        fillColorData();

        MyRVAdapterColors myRVAdapterColors = new MyRVAdapterColors(context, colorData);

        recyclerView.setAdapter(myRVAdapterColors);
    }

    private void findAllViewById() {
        openCamera = findViewById(R.id.open_camera);
        myRecyclerView = findViewById(R.id.recyclerView);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });
    }

}