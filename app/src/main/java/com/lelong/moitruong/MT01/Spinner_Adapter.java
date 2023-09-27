package com.lelong.moitruong.MT01;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lelong.moitruong.R;

import java.util.List;

public class Spinner_Adapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> data;
    private Cursor cursor;
    private int selectedItemPosition = -1; // Vị trí mục được chọn

    public Spinner_Adapter(Context context, Cursor cursor, List<String> data) {
        super(context, 0, data);
        this.cursor = cursor;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.mt01_spinner, parent, false);

        }

        TextView textView = convertView.findViewById(R.id.text);
        ImageView checkImageView = convertView.findViewById(R.id.img_check);

        String item = data.get(position);
        textView.setText(item);

        boolean itemInCursor = isItemInCursor(item, cursor);

        if (itemInCursor) {
            checkImageView.setVisibility(View.VISIBLE); // Hiển thị biểu tượng tick
        } else {
            checkImageView.setVisibility(View.GONE); // Ẩn biểu tượng tick
        }
        return convertView;
    }
    private boolean isItemInCursor(String item, Cursor cursor) {
        if (cursor == null || !cursor.moveToFirst()) {
            return false;
        }
        for (int i = 0; i < cursor.getCount(); i++) {
            String g_ten = cursor.getString(cursor.getColumnIndexOrThrow("tc_fcd004"));
            String g_xuong = cursor.getString(cursor.getColumnIndexOrThrow("tc_fcd005"));
            String materialInfo = g_ten + " - " + g_xuong;
            if (item.equals(materialInfo)) {
                return true;
            }
            cursor.moveToNext();
        }
        return false;
    }
}
