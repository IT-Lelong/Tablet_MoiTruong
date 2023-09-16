package com.lelong.moitruong.MT01;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.lelong.moitruong.Create_Table;
import com.lelong.moitruong.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageDetailActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ImagePagerAdapter pagerAdapter;
    private ThuVien_Anh_Adapter thuVienAnhAdapter;
    private ArrayList<File> imageFiles; // Danh sách File của các tệp hình ảnh
    ArrayList<String> imagePathList;
    private int currentPosition;
    ActionBar actionBar;
    private Create_Table Cre_db = null;
    String selectedDate,selectedDepartment,selectedDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();
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
    //Khởi tạo menu trên thanh tiêu đề (S)
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.image_context_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                // Xử lý sự kiện xóa ảnh tại vị trí hiện tại
                deleteImageAtPosition(currentPosition);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    public boolean deleteImageByPath(String imagePath) {
        File imageFile = new File(imagePath);

        if (imageFile.exists()) {
            boolean deleted = imageFile.delete();
            if (deleted) {
                // Xóa thành công
                return true;
            } else {
                // Xóa không thành công
                return false;
            }
        } else {
            // Tệp ảnh không tồn tại
            return false;
        }
    }
    // Phương thức xóa ảnh tại vị trí hiện tại
    private void deleteImageAtPosition(int position) {
        // Xóa ảnh tại vị trí position từ danh sách imageUrls
        if (position >= 0 && position < imageFiles.size()) {
            Cre_db.delete_Image(imageFiles.get(position).getName());
            deleteImageByPath(imageFiles.get(position).getName());

            imageFiles.remove(position);
            Intent intent = getIntent();
            selectedDate = intent.getStringExtra("ngay");
            selectedDepartment = intent.getStringExtra("bophan");
            selectedDetail = intent.getStringExtra("hangmuc");

            // Cập nhật ViewPager sau khi xóa ảnh
            //pagerAdapter.notifyDataSetChanged();
            // Kiểm tra xem còn ảnh nào trong danh sách không
            if (imageFiles.isEmpty()) {
                // Nếu không còn ảnh, kết thúc Activity hoặc thực hiện hành động tùy ý
                finish(); // Kết thúc Activity
            } else {
                // Nếu còn ảnh, hiển thị ảnh tiếp theo hoặc ảnh trước đó
                int newPosition = position;
                newPosition = currentPosition + 1;
                if (newPosition < imageFiles.size()) {
                    // Nếu vị trí mới nằm trong phạm vi danh sách ảnh, thì hiển thị ảnh tiếp theo
                    viewPager.setCurrentItem(newPosition);
                    currentPosition = newPosition;
                }else{
                    newPosition = currentPosition - 1;
                    if (newPosition >= 0) {
                        // Nếu vị trí mới nằm trong phạm vi danh sách ảnh, thì hiển thị ảnh trước đó
                        viewPager.setCurrentItem(newPosition);
                        currentPosition = newPosition;
                    }
                }
                //if (newPosition >= imageFiles.size()) {
                //    newPosition = imageFiles.size() - 1;
                //    viewPager.setCurrentItem(position);
                //}
            }
        }
    }

}
