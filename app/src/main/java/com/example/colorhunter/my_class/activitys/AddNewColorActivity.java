package com.example.colorhunter.my_class.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.colorhunter.R;

public class AddNewColorActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_color);

        saveBtn = findViewById(R.id.save_button);
        toolbar = findViewById(R.id.add_new_activity_toolBar);
        toolbar.setTitle(R.string.add_new_color_toolbar_title);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setTitleTextColor(getResources().getColor(R.color.text_color_1));
        setSupportActionBar(toolbar);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNewColorActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddNewColorActivity.this, CameraActivity.class);
                startActivity(i);
            }
        });
    }

}