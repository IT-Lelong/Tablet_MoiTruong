package com.lelong.moitruong.MT01;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.lelong.moitruong.Create_Table;
import com.lelong.moitruong.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ThuVien_Anh extends AppCompatActivity {
    private Create_Table Cre_db = null;
    private RecyclerView recyclerView;
    private ThuVien_Anh_Adapter adapter;
    private ViewPager2 viewPager;
    private TabLayout dotsLayout;

    String selectedDate,selectedDepartment,selectedDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mt01_thu_vien_anh_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Cre_db = new Create_Table(this);
        Cre_db.open();
        Intent intent = getIntent();
        selectedDate = intent.getStringExtra("ngay");
        selectedDepartment = intent.getStringExtra("bophan");
        selectedDetail = intent.getStringExtra("hangmuc");
        recyclerView = findViewById(R.id.recyclerView);
        // Tạo danh sách các ảnh
        List<File> imageFiles = getImageFiles();
        // Thêm thêm ảnh vào danh sách theo nhu cầu

        // Thiết lập Adapter và LayoutManager cho RecyclerView
        adapter = new ThuVien_Anh_Adapter(this,imageFiles);
        recyclerView.setAdapter(adapter);

        // Cài đặt GridLayoutManager với 3 cột
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        List<File> imageFiles = getImageFiles();
        adapter = new ThuVien_Anh_Adapter(this,imageFiles);
        recyclerView.setAdapter(adapter);
    }

    private List<File> getImageFiles() {
        String[] targetKeywords = {selectedDate, selectedDepartment, selectedDetail}; // Các từ khóa mà bạn quan tâm
        List<File> imageFiles = new ArrayList<>();
        File mediaDir = getExternalMediaDirs()[0];
        File imageDirectory = new File(String.valueOf(mediaDir),selectedDate.replace("-","")); // Thay đổi đường dẫn đến thư mục ảnh
        File[] files = imageDirectory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (isImageFile(file)) {
                    boolean allKeywordsMatch = true; // Mặc định tất cả từ khóa khớp
                    for (String keyword : targetKeywords) {
                        if (!file.getName().contains(keyword)) { // Nếu tên tập tin không khớp với từ khóa
                            allKeywordsMatch = false;
                            break; // Thoát vòng lặp ngay lập tức
                        }
                    }
                    if (allKeywordsMatch) {
                        imageFiles.add(file);
                    }
                }
            }
        }

        return imageFiles;
    }

    private boolean isImageFile(File file) {
        String name = file.getName();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png");
        // Thêm các định dạng ảnh khác tương ứng
    }
}