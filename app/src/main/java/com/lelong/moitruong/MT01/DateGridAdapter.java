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
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DateGridAdapter extends RecyclerView.Adapter<DateGridAdapter.ViewHolder> {

    private List<File> imageFiles;
    private Context context;
    private String g_factory;
    private List<ImageGroup> imageGroups;
    public DateGridAdapter(Context context,List<File> imageFiles,String g_factory) {
        this.imageFiles = imageFiles;
        this.context = context;
        this.g_factory = g_factory;
    }
    //public ThuVien_Anh_Adapter(Context context, List<File> imageFiles) {
    //    this.context = context;
    //    this.imageFiles = imageFiles;
    //}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mt01_thu_vien_anh_item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int getAdapterPosition) {

        //File imageFile = imageFiles.get(position);
        // Sử dụng Glide để tải và hiển thị hình ảnh
        int adapterPosition = holder.getAdapterPosition(); // Lấy vị trí của mục

        if (adapterPosition != RecyclerView.NO_POSITION) {
            File imageFile = imageFiles.get(adapterPosition);
            Glide.with(context).load(imageFile).into(holder.imageView);
            String a = imageFiles.get(adapterPosition).getName();
        }
        //Glide.with(context).load(imageFile).into(holder.imageView);
        // Xử lý sự kiện khi người dùng click vào ảnh
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickedPosition = holder.getAdapterPosition(); // Lấy lại vị trí của mục khi được click

                if (clickedPosition != RecyclerView.NO_POSITION) {
                    // Thực hiện hành động mở ảnh lớn ở đây
                    // Sử dụng clickedPosition để xác định mục đã click
                    ArrayList<Uri> imageUris = new ArrayList<>();
                    //for (File imageFile : imageFiles) {
                    //    Uri imageUri = Uri.fromFile(imageFile);
                    //    imageUris.add(imageUri);
                    //}
                    ArrayList<String> imagePathList = new ArrayList<>();
                    for (File imageFile : imageFiles) {
                        imagePathList.add(imageFile.getAbsolutePath());
                    }
                    Intent intent = new Intent(context, ImageDetailActivity.class);
                    //intent.putParcelableArrayListExtra("imageUris", imageUris);
                    intent.putStringArrayListExtra("imagePaths", imagePathList);
                    intent.putExtra("position", clickedPosition);
                    intent.putExtra("xuong", g_factory);
                    context.startActivity(intent);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageFiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}