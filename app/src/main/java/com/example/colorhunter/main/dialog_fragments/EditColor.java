package com.example.colorhunter.main.dialog_fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.camera2.internal.StreamUseCaseUtil;
import androidx.fragment.app.DialogFragment;

import com.example.colorhunter.R;
import com.example.colorhunter.main.activitys.ListActivity;
import com.example.colorhunter.main.adapters.DBAdapter;
import com.example.colorhunter.main.custom_view.CustomColorData;

import java.util.Objects;

public class EditColor extends DialogFragment {

    private EditText newName, newDescription;
    private TextView btnCancel, btnFinish;

    private String oldName, oldDescription, newNameText, newDescriptionText;

    private Context context;

    private DBAdapter myAdapter;

    public EditColor(Context c, String oldName, String oldDescription){
        this.context = c;
        this.oldDescription = oldDescription;
        this.oldName = oldName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_edit_color, container, false);
        myAdapter = new DBAdapter(context);

        newName = view.findViewById(R.id.enter_new_name);
        newDescription = view.findViewById(R.id.enter_new_description);
        btnCancel = view.findViewById(R.id.edit_fragment_negative_button);
        btnFinish = view.findViewById(R.id.edit_fragment_positive_button);

        setOldData();
        setListeners();

        return view;
    }

    private void setOldData() {
        newName.setText(oldName);
        newName.setSelection(newName.getText().length());

        if(!oldDescription.equals("0"))
            newDescription.setText(oldDescription);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }

    private boolean getNewData() {
        newNameText = newName.getText().toString().trim();
        newDescriptionText = newDescription.getText().toString().trim();

        if(newNameText.equals("")){
            Toast toast1 = Toast.makeText(context, getResources().getString(R.string.error_enter_name), Toast.LENGTH_SHORT);
            toast1.setGravity(Gravity.TOP, Resources.getSystem().getDisplayMetrics().widthPixels / 2, 40);
            toast1.show();
            return false;
        }else if(!newNameText.equals("")) {
            return true;
        }else
            return false;
    }

    private boolean checkingForExistence(){
        SQLiteDatabase database = myAdapter.getWritableDatabase();
        Cursor cursor = database.query(DBAdapter.DATABASE_TABLE_COLORS, null, null, null, null, null, null);
        if (!newNameText.equals(oldName)){
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_COLOR_NAME));
                    if (newNameText.equals(name)){
                        Toast toast2 = Toast.makeText(context, getResources().getString(R.string.error_already_exists), Toast.LENGTH_SHORT);
                        toast2.setGravity(Gravity.TOP, Resources.getSystem().getDisplayMetrics().widthPixels / 2, 40);
                        toast2.show();
                        return false;
                        break;
                    }
                } while (cursor.moveToNext());
            }else
                return true;
            cursor.close();
            database.close();
        }else
            return true;

    }

    private void setListeners() {
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getNewData()){

                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Objects.requireNonNull(getDialog()).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ((ListActivity) getActivity()).callShowList();
                ((ListActivity) getActivity()).dismissActiveFragment();
            }
        });

    }
}
