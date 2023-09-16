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

public class ThuVien_Anh_Adapter extends RecyclerView.Adapter<ThuVien_Anh_Adapter.ViewHolder> {

    private List<File> imageFiles;
    private Context context;

    public ThuVien_Anh_Adapter(Context context, List<File> imageFiles) {
        this.context = context;
        this.imageFiles = imageFiles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mt01_thu_vien_anh_item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File imageFile = imageFiles.get(position);
        // Sử dụng Glide để tải và hiển thị hình ảnh
        Glide.with(context).load(imageFile).into(holder.imageView);
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

            // Xử lý sự kiện khi người dùng nhấp vào một ảnh
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Thực hiện hành động hiển thị ảnh lớn và cho phép kéo qua ảnh tiếp theo và ảnh trước đó
                }
            });
        }
    }
}