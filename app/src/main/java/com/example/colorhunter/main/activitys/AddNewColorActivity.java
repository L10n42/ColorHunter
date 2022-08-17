package com.example.colorhunter.main.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.colorhunter.R;
import com.example.colorhunter.main.adapters.DBAdapter;

public class AddNewColorActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView saveBtn, hexTextView, rgbTextView;
    private EditText enterName, enterDescription;
    private View colorView;

    private ClipboardManager clipboardManager;

    private String hex, nameText, desText;
    private boolean check;
    private int rC, gC, bC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_color);

        getExtras();

        findAllViewById();

        setToolbar();

        setData();

        setListeners();
    }

    private void setListeners() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getData() && checkingForExistence()) {
                    saveData();

                    Intent intent = new Intent(AddNewColorActivity.this, ListActivity.class);
                    startActivity(intent);
                }
            }
        });

        hexTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copyText(hexTextView.getText().toString().trim());
                return true;
            }
        });

        rgbTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copyText(rgbTextView.getText().toString().trim());

                return true;
            }
        });
    }

    private boolean getData() {
        nameText = enterName.getText().toString().trim();
        desText = enterDescription.getText().toString().trim();

        if(nameText.equals("")){
            Toast.makeText(AddNewColorActivity.this, getResources().getString(R.string.error_enter_name), Toast.LENGTH_SHORT).show();
            return false;
        }else if(!nameText.equals("")) {
            return true;
        }else
            return false;
    }

    private boolean checkingForExistence(){
        DBAdapter dbAdapter = new DBAdapter(this);
        SQLiteDatabase database = dbAdapter.getWritableDatabase();
        Cursor cursor = database.query(DBAdapter.DATABASE_TABLE_COLORS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_COLOR_NAME));
                if (nameText.equals(name)){
                    Toast.makeText(AddNewColorActivity.this, getResources().getString(R.string.error_already_exists), Toast.LENGTH_SHORT).show();
                    check = false;
                    break;
                }else
                    check = true;
            } while (cursor.moveToNext());
            cursor.close();

            if (check) {
                return true;
            } else {
                return false;
            }
        }else
            return true;

    }

    private void copyText(String text) {
        if (!text.isEmpty()) {
            clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("key", text);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(AddNewColorActivity.this, "Copied", Toast.LENGTH_SHORT).show();
        }
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