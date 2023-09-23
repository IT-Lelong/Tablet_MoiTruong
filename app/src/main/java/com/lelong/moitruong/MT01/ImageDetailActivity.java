package com.lelong.moitruong.MT01;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.lelong.moitruong.Constant_Class;
import com.lelong.moitruong.Create_Table;
import com.lelong.moitruong.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageDetailActivity extends AppCompatActivity implements MoveDialog.OnDialogResultListener {
    private ViewPager viewPager;
    private ImagePagerAdapter pagerAdapter;
    private ThuVien_Anh_Adapter thuVienAnhAdapter;
    private ArrayList<File> imageFiles;
    ArrayList<String> imagePathList;
    private int currentPosition;
    ActionBar actionBar;
    private Create_Table Cre_db = null;
    String selectedDate,selectedDepartment,selectedDetail,g_result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Cre_db = new Create_Table(this);
        Cre_db.open();
        actionBar = getSupportActionBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mt01_thu_vien_anh_fragment_image_detail);

        viewPager = findViewById(R.id.viewPager);
        currentPosition = getIntent().getIntExtra("position", 0);

        // Nhận danh sách đường dẫn của các tệp File
        imagePathList = getIntent().getStringArrayListExtra("imagePaths");

        // Tạo lại danh sách File từ đường dẫn
        imageFiles = new ArrayList<>();
        for (String imagePath : imagePathList) {
            imageFiles.add(new File(imagePath));
        }

        pagerAdapter = new ImagePagerAdapter(this, imageFiles);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(currentPosition);

    }
    //Khởi tạo menu trên thanh tiêu đề
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.image_context_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int position;
        switch (item.getItemId()) {
            case R.id.action_delete:
                // Xử lý sự kiện xóa ảnh tại vị trí hiện tại
                int currentPosition = viewPager.getCurrentItem();
                deleteImageAtPosition(currentPosition);
                break;
            case R.id.action_move:
                position = viewPager.getCurrentItem();
                String myVariable = imageFiles.get(position).getName();
                MoveDialog moveDialog = new MoveDialog();
                moveDialog.setMyVariable(myVariable);
                moveDialog.callback = this;
                moveDialog.show(getSupportFragmentManager(), "MoveDialog");
                break;
            case R.id.action_info:
                position = viewPager.getCurrentItem();
                String value = imageFiles.get(position).getName();
                Thongtin_Dialog thongtin_dialog = new Thongtin_Dialog();
                thongtin_dialog.setMyVariable(value);
                thongtin_dialog.show(getSupportFragmentManager(), "Thongtin_Dialog");
                break;
        }
        return super.onContextItemSelected(item);
    }
    @Override
    public void onDialogResult(String result) {
        g_result = result;
        int position = viewPager.getCurrentItem();
        if (g_result == "Y"){
            imageFiles.remove(position);
            pagerAdapter = new ImagePagerAdapter(this, imageFiles);
            viewPager.setAdapter(pagerAdapter);
            viewPager.setCurrentItem(position);
            pagerAdapter.notifyDataSetChanged();
            if (imageFiles.isEmpty()) {
                // Nếu không còn ảnh, kết thúc Activity hoặc thực hiện hành động tùy ý
                finish(); // Kết thúc Activity
            } else {
                viewPager.setCurrentItem(position);
            }
        }
    }

    public boolean deleteImageByPath(String imagePath) {
        File imageFile = new File(imagePath);

        if (imageFile.exists()) {
            boolean deleted = imageFile.delete();
            if (deleted) {
                Toast.makeText(this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(this, "Xóa không thành công!", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this, "Tệp ảnh không tồn tại!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // Phương thức xóa ảnh tại vị trí hiện tại
    private void deleteImageAtPosition(int position) {
        // Xóa ảnh tại vị trí position từ danh sách imageUrls
        if (position >= 0 && position < imageFiles.size()) {
            Cursor get_Thongtin_Anh =Cre_db.get_Thongtin_Anh(imageFiles.get(position).getName());
            get_Thongtin_Anh.moveToFirst();
            String tc_fcf001= null,tc_fcf002=null,tc_fcf003=null,tc_fcf004=null;
            for (int i = 0; i < get_Thongtin_Anh.getCount(); i++) {
                tc_fcf001 = get_Thongtin_Anh.getString(get_Thongtin_Anh.getColumnIndexOrThrow("tc_fcf001"));
                tc_fcf002 = get_Thongtin_Anh.getString(get_Thongtin_Anh.getColumnIndexOrThrow("tc_fcf002"));
                tc_fcf003 = get_Thongtin_Anh.getString(get_Thongtin_Anh.getColumnIndexOrThrow("tc_fcf003"));
                tc_fcf004 = get_Thongtin_Anh.getString(get_Thongtin_Anh.getColumnIndexOrThrow("tc_fcf004"));
            }
            Cre_db.delete_Image(imageFiles.get(position).getName());
            String datePath = getDate(imageFiles.get(position).getAbsolutePath());
            String image_path = "/storage/emulated/0/Android/media/com.lelong.moitruong/" + datePath.replace("-", "") + "/" + imageFiles.get(position).getName();
            deleteImageByPath(image_path);
            imageFiles.remove(position);
            pagerAdapter = new ImagePagerAdapter(this, imageFiles);
            viewPager.setAdapter(pagerAdapter);
            viewPager.setCurrentItem(currentPosition);
            Intent intent = getIntent();
            selectedDate = intent.getStringExtra("ngay");
            selectedDepartment = intent.getStringExtra("bophan");
            selectedDetail = intent.getStringExtra("hangmuc");
            Cre_db.update_imagecount(tc_fcf001, tc_fcf002, tc_fcf003, tc_fcf004);
            pagerAdapter.notifyDataSetChanged();
            if (imageFiles.isEmpty()) {
                finish(); // Kết thúc Activity
            } else {
                viewPager.setCurrentItem(currentPosition);
            }
        }
    }
    public String getDate(String name) {
        String date = null;
        String regex = "\\d{4}-\\d{2}-\\d{2}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        while (matcher.find()) {
            date = matcher.group();
        }
        return date;
    }

}
