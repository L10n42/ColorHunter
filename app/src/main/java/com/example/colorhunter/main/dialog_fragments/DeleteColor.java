package com.example.colorhunter.main.dialog_fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.colorhunter.R;
import com.example.colorhunter.main.activitys.ListActivity;
import com.example.colorhunter.main.adapters.DBAdapter;

import java.util.Objects;

public class DeleteColor extends DialogFragment {

    private TextView btnCancel, btnDelete, colorName;
    private View color;

    private Context context;
    private int id;
    private String name, hex;

    public DeleteColor(Context c, int id, String name, String hex) {
        this.context = c;
        this.id = id;
        this.name = name;
        this.hex = hex;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_delete_color, container, false);

        color = view.findViewById(R.id.delete_fragment_color);
        btnCancel = view.findViewById(R.id.delete_fragment_negative_button);
        btnDelete = view.findViewById(R.id.delete_fragment_positive_button);
        colorName = view.findViewById(R.id.delete_fragment_name);

        color.setBackgroundColor(Color.parseColor(hex));
        colorName.setText(getResources().getString(R.string.name_text) + " " + name);

        setListeners();

        return view;
    }

    private void deleteColorFun() {
        DBAdapter myAdapter = new DBAdapter(context);
        SQLiteDatabase database = myAdapter.getWritableDatabase();

        try {
            database.delete(DBAdapter.DATABASE_TABLE_COLORS, DBAdapter.KEY_ID + "=" + id, null);
            Toast.makeText(context, getResources().getString(R.string.mes_deleted), Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(context, getResources().getString(R.string.mes_delete_failed), Toast.LENGTH_SHORT).show();
        }
        database.delete(DBAdapter.DATABASE_TABLE_COLORS, DBAdapter.KEY_ID + "=" + id, null);
        database.close();
    }

    private void setListeners() {
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteColorFun();
                dismiss();
                ((ListActivity) getActivity()).callShowList();
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
