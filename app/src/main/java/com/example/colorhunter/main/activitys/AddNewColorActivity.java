package com.example.colorhunter.main.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.colorhunter.R;
import com.example.colorhunter.main.adapters.DBAdapter;

public class AddNewColorActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView saveBtn, hexTextView, rgbTextView;
    private EditText enterName, enterDescription;
    private View colorView;

    private String hex;
    private int rC, gC, bC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_color);

        getExtras();

        findAllViewById();

        setToolbar();

        setData();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                Intent intent = new Intent(AddNewColorActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveData() {
        if (!enterName.getText().toString().equals("")) {
            DBAdapter myAdapter = new DBAdapter(this);

            String rgbString = "R: " + rC + "    " + "G: " + gC + "    " + "B: " + bC;

            if (enterDescription.getText().toString().equals("")){
                myAdapter.putData(enterName.getText().toString(), "0", hex, rgbString);
            }else {
                myAdapter.putData(enterName.getText().toString(), enterDescription.getText().toString(), hex, rgbString);
            }
        }
    }

    private void setData() {
        hexTextView.setText("Hex: " + hex);
        rgbTextView.setText("R: " + rC + "    " + "G: " + gC + "    " + "B: " + bC);
        colorView.setBackgroundColor(Color.parseColor(hex));
    }

    private void getExtras() {
        if (getIntent() != null){
            Bundle extras = getIntent().getExtras();
            hex = extras.getString("hex");
            rC = extras.getInt("r");
            gC = extras.getInt("g");
            bC = extras.getInt("b");
        }
    }

    private void findAllViewById(){
        saveBtn = findViewById(R.id.save_button);
        enterDescription = findViewById(R.id.enter_description);
        enterName = findViewById(R.id.enter_name);
        hexTextView = findViewById(R.id.textView_hex);
        rgbTextView = findViewById(R.id.textView_RGB);
        colorView = findViewById(R.id.color);
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.add_new_activity_toolBar);
        toolbar.setTitle(R.string.add_new_color_toolbar_title);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setTitleTextColor(getResources().getColor(R.color.text_color_1));
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddNewColorActivity.this, CameraActivity.class);
                startActivity(i);
            }
        });

    }

}