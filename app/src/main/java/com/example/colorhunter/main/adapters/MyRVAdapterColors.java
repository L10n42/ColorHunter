package com.example.colorhunter.main.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colorhunter.R;
import com.example.colorhunter.main.activitys.ListActivity;
import com.example.colorhunter.main.custom_view.CustomColorData;
import com.example.colorhunter.main.dialog_fragments.DeleteColor;
import com.example.colorhunter.main.dialog_fragments.EditColor;

import java.util.ArrayList;
import java.util.List;

public class MyRVAdapterColors extends RecyclerView.Adapter<MyRVAdapterColors.MyViewHolder> implements Filterable {

    private Context context, wrapper;
    private ClipboardManager clipboardManager;
    private ArrayList<CustomColorData> colorData;
    private ArrayList<CustomColorData> fullData;
    private FragmentManager fragmentManager;

    private DBAdapter myAdapter;

    public MyRVAdapterColors(Context context1, ArrayList<CustomColorData> data, FragmentManager fragmentManager){
        this.context = context1;
        this.colorData = data;
        this.fullData = data;
        this.fragmentManager = fragmentManager;
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
            holder.description.setText("");
            holder.description.setVisibility(View.INVISIBLE);
            holder.description.setMaxHeight(2);
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
                showPopupMenu(holder, findIdItem(colorData.get(position).getName()), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return colorData.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                if (constraint == null | constraint.length() == 0){
                    filterResults.count = fullData.size();
                    filterResults.values = fullData;
                }else{
                    String pattern = constraint.toString().toLowerCase();

                    List<CustomColorData> dataResult = new ArrayList<>();

                    for (CustomColorData customData : fullData){
                        if (customData.getName().toLowerCase().contains(pattern)){
                            dataResult.add(customData);
                        }
                    }
                    filterResults.count = dataResult.size();
                    filterResults.values = dataResult;
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                colorData = (ArrayList<CustomColorData>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
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

    private int findIdItem(String name) {
        myAdapter = new DBAdapter(context);
        SQLiteDatabase database = myAdapter.getWritableDatabase();

        Cursor cursor = database.query(DBAdapter.DATABASE_TABLE_COLORS, null, "name = ?", new String[]{name}, null, null, null);
        cursor.moveToFirst();

        @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(DBAdapter.KEY_ID));

        cursor.close();
        database.close();

        return id;
    }

    private void showPopupMenu(MyRVAdapterColors.MyViewHolder viewHolder, int itemId, int pos){
        wrapper = new ContextThemeWrapper(context, R.style.MyPopupStyle);

        PopupMenu popupMenu = new PopupMenu(wrapper, viewHolder.menu);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu_btn_delete){
                    DeleteColor deleteColor = new DeleteColor(context, itemId, colorData.get(pos).getName(), colorData.get(pos).getHex());
                    deleteColor.show(fragmentManager, "delete color dialog");

                }else if (id == R.id.menu_btn_edit){
                    EditColor editColor = new EditColor(context, colorData.get(pos).getName(),colorData.get(pos).getDes(), itemId);
                    editColor.show(fragmentManager, "edit color dialog");

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
