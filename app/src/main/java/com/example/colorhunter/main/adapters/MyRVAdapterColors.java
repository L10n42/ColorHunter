package com.example.colorhunter.main.adapters;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colorhunter.R;
import com.example.colorhunter.main.custom_view.CustomColorData;

import java.util.ArrayList;

public class MyRVAdapterColors extends RecyclerView.Adapter<MyRVAdapterColors.MyViewHolder> {

    private Context context, wrapper;
    private ClipboardManager clipboardManager;
    private ArrayList<CustomColorData> colorData;

    public MyRVAdapterColors(Context context1, ArrayList<CustomColorData> data){
        this.context = context1;
        this.colorData = data;
    }

    @NonNull
    @Override
    public MyRVAdapterColors.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_item_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRVAdapterColors.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.color.setBackgroundColor(Color.parseColor(colorData.get(position).getHex()));
        holder.rgb.setText(colorData.get(position).getRgb());
        holder.hex.setText("HEX: " + colorData.get(position).getHex());
        holder.name.setText("Name: " + colorData.get(position).getName());

        if (colorData.get(position).getDes().equals("0")){
            holder.description.setVisibility(View.INVISIBLE);
            holder.description.setMaxHeight(1);
            holder.description.setMinHeight(1);
        }else{
            holder.description.setText("Description: " + colorData.get(position).getDes());
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setMaxHeight(100000);
            holder.description.setMinHeight(20);
        }

        holder.rgb.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copyText(colorData.get(position).getRgb());
                return true;
            }
        });

        holder.hex.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copyText(colorData.get(position).getHex());
                return true;
            }
        });

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return colorData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView menu;
        public TextView name, description, hex, rgb;
        public View color;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            menu = itemView.findViewById(R.id.menu_card);
            name = itemView.findViewById(R.id.name_card);
            description = itemView.findViewById(R.id.description_card);
            hex = itemView.findViewById(R.id.hex_card);
            rgb = itemView.findViewById(R.id.rgb_card);
            color = itemView.findViewById(R.id.color_card);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {}
    }

    private void showPopupMenu(MyRVAdapterColors.MyViewHolder viewHolder){
        wrapper = new ContextThemeWrapper(context, R.style.MyPopupStyle);

        PopupMenu popupMenu = new PopupMenu(wrapper, viewHolder.menu);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu_btn_delete){

                }else if (id == R.id.menu_btn_edit){

                }

                return true;
            }
        });

        popupMenu.setForceShowIcon(true);
        popupMenu.show();
    }

    private void copyText(String text) {
        if (!text.isEmpty()) {
            clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("key", text);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
        }
    }
}
