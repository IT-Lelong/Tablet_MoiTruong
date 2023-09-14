package com.lelong.moitruong.MT01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lelong.moitruong.R;

import java.io.File;
import java.util.List;

public class ThuVien_Anh_Adapter extends RecyclerView.Adapter<ThuVien_Anh_Adapter.ImageViewHolder> {

    private List<File> imageFiles;
    private Context context;

    public ThuVien_Anh_Adapter(List<File> imageFiles) {
        this.imageFiles = imageFiles;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.mt01_thu_vien_anh_item_image, parent, false);
        return new ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        File imageFile = imageFiles.get(position);
        Glide.with(context).load(imageFile).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageFiles.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}