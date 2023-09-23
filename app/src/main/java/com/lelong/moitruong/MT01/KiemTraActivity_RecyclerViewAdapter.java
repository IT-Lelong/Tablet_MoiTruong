package com.lelong.moitruong.MT01;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lelong.moitruong.R;

public class KiemTraActivity_RecyclerViewAdapter extends RecyclerView.Adapter<KiemTraActivity_RecyclerViewAdapter.ViewHolder> {
    private Cursor cursor;
    private Context context;

    public KiemTraActivity_RecyclerViewAdapter(Context context,Cursor cursor) {
        this.cursor = cursor;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mt01_kiem_tra_activity_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        cursor.moveToPosition(position);

        String g_stt = String.valueOf(position + 1);
        String g_donvi = cursor.getString(cursor.getColumnIndexOrThrow("donvi"));
        String g_error = cursor.getString(cursor.getColumnIndexOrThrow("slerr"));
        String g_ngay = cursor.getString(cursor.getColumnIndexOrThrow("tc_fce002"));
        //String g_ = cursor.getString(cursor.getColumnIndexOrThrow("name"));

        holder.tv_stt.setText(g_stt);
        holder.tv_donvi.setText(g_donvi);
        holder.tv_ngay.setText(g_ngay);
        holder.tv_loi.setText(g_error);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickedPosition = holder.getAdapterPosition();
                cursor.moveToPosition(clickedPosition);
                String g_donvi = cursor.getString(cursor.getColumnIndexOrThrow("donvi"));
                String g_mabp = cursor.getString(cursor.getColumnIndexOrThrow("tc_fce004"));
                String g_ngay = cursor.getString(cursor.getColumnIndexOrThrow("tc_fce002"));
                //String g_user = cursor.getString(cursor.getColumnIndexOrThrow("tc_fce003"));
                String g_tc_fcd003 = cursor.getString(cursor.getColumnIndexOrThrow("tc_fcd003"));
                String g_tenBP = g_tc_fcd003 + " " + g_donvi;

                Intent hangmucIntent = new Intent(context, HangMucKiemTra.class);
                Bundle bundle = new Bundle();
                bundle.putString("FACTORY", "");
                bundle.putString("DEPNO", g_mabp);
                bundle.putString("DEPNAME", g_tenBP);
                bundle.putString("DATE", g_ngay);
                //bundle.putString("USER", g_user);
                hangmucIntent.putExtras(bundle);
                context.startActivity(hangmucIntent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_stt,tv_donvi,tv_ngay,tv_loi;
        public ImageView img_galery;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_stt = itemView.findViewById(R.id.tv_stt);
            tv_donvi = itemView.findViewById(R.id.tv_donvi);
            tv_ngay = itemView.findViewById(R.id.tv_ngay);
            tv_loi = itemView.findViewById(R.id.tv_loi);

        }
    }
}
