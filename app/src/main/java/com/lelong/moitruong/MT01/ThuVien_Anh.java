package com.lelong.moitruong.MT01;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
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

        addControls();
        addEvents();
    }

    private void addEvents() {
        List<File> imageFiles = getImageFiles(); // Lấy danh sách tập tin ảnh từ thư mục bộ nhớ
        ThuVien_Anh_Adapter adapter = new ThuVien_Anh_Adapter(imageFiles);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(dotsLayout, viewPager,
                (tab, position) -> {}).attach();
    }

    private void addControls() {
        Intent intent = getIntent();
        selectedDate = intent.getStringExtra("ngay");
        selectedDepartment = intent.getStringExtra("bophan");
        selectedDetail = intent.getStringExtra("hangmuc");

        viewPager = findViewById(R.id.viewPager);
        dotsLayout = findViewById(R.id.dotsLayout);
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