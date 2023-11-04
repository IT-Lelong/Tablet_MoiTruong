package com.lelong.moitruong.MT01;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lelong.moitruong.MainActivity;
import com.lelong.moitruong.Menu;
import com.lelong.moitruong.R;
import java.util.List;

public class ThuVien_Anh_Adapter extends RecyclerView.Adapter<ThuVien_Anh_Adapter.ImageGroupViewHolder> {
    private List<ImageGroup> imageGroups;
    private Context context;
    private String g_factory;

    public ThuVien_Anh_Adapter(Context context, List<ImageGroup> imageGroups,String g_factory) {
        this.context = context;
        this.imageGroups = imageGroups;
        this.g_factory= g_factory;
    }

    @NonNull
    @Override
    public ImageGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mt1_thu_vien_anh_group, parent, false);
        return new ImageGroupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageGroupViewHolder holder, int position) {
        ImageGroup imageGroup = imageGroups.get(position);
        holder.tv_date.setText(imageGroup.getDate());
        holder.tv_department.setText(imageGroup.getDepartment());
        holder.tv_hangmuc.setText(imageGroup.getHangmuc());

        // Tạo Adapter con cho RecyclerView trong nhóm ảnh
        DateGridAdapter imageAdapter = new DateGridAdapter(context,imageGroup.getImage(),g_factory);
        holder.rv_image.setAdapter(imageAdapter);
    }

    @Override
    public int getItemCount() {
        return imageGroups.size();
    }

    public static class ImageGroupViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date;
        TextView tv_department;
        TextView tv_hangmuc;
        RecyclerView rv_image;

        public ImageGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_department = itemView.findViewById(R.id.tv_department);
            tv_hangmuc = itemView.findViewById(R.id.tv_hangmuc);
            rv_image = itemView.findViewById(R.id.rv_image);
        }
    }
}
