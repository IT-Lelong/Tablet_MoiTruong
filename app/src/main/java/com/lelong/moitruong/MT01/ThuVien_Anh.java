package com.lelong.moitruong.MT01;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.lelong.moitruong.Create_Table;
import com.lelong.moitruong.R;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThuVien_Anh extends AppCompatActivity {
    private Create_Table Cre_db = null;
    private RecyclerView recyclerView;
    private ThuVien_Anh_Adapter adapter;
    private DateGridAdapter dateAdapter;
    private ViewPager2 viewPager;
    private TabLayout dotsLayout;

    String selectedDate,selectedDepartment,selectedDetail,selectedKiemtra,selectedWeek,nam,thang,tuan;

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
        selectedKiemtra = intent.getStringExtra("hangmuclon");
        selectedDetail = intent.getStringExtra("hangmuc");
        selectedWeek = intent.getStringExtra("tuan");
        if(!selectedWeek.equals(""))
        {
            thang = selectedWeek.substring(0, 2);
            nam = selectedWeek.substring(3,7);
            tuan = selectedWeek.substring(10,11);
        }
        recyclerView = findViewById(R.id.recyclerView);
        //recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Tạo danh sách các ảnh
        //List<File> imageFiles = getImageFiles();
        // Thêm thêm ảnh vào danh sách theo nhu cầu
        List<ImageGroup> imageGroup = generateSampleData();
        // Thiết lập Adapter và LayoutManager cho RecyclerView
        adapter = new ThuVien_Anh_Adapter(this,imageGroup);
        recyclerView.setAdapter(adapter);

        // Cài đặt GridLayoutManager với 3 cột
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setLayoutManager(gridLayoutManager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //List<File> imageFiles = getImageFiles();
        //adapter = new ThuVien_Anh_Adapter(this,imageFiles);
        //recyclerView.setAdapter(adapter);
        List<ImageGroup> imageGroup = generateSampleData();
        adapter = new ThuVien_Anh_Adapter(this,imageGroup);
        recyclerView.setAdapter(adapter);
    }
    public static List<String> getWeekDaysForMonth(int year, int month, int weekInMonth) {
        List<String> weekDays = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.WEEK_OF_MONTH, weekInMonth);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); // Đặt ngày bắt đầu là Chủ Nhật
        int daysInMonthCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        for (int i = 0; i < 7; i++) { // Lặp qua 7 ngày của tuần
            Date date = calendar.getTime();

            // Kiểm tra xem ngày đó có trong tháng không
            if (calendar.get(Calendar.MONTH) == month - 1) {
                weekDays.add(String.valueOf(dateFormat.format(date)));
            }
            calendar.add(Calendar.DAY_OF_WEEK, -1); // Chuyển sang ngày tiếp theo
        }

        return weekDays;
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
    private List<File> getImageFilesAll() {
        List<File> imageFiles = null;
        List<String> weekdateList = getWeekDaysForMonth(Integer.parseInt(nam), Integer.parseInt(thang), Integer.parseInt(tuan));
        for (String date : weekdateList) {
            String[] targetKeywords = {date, selectedDepartment, selectedDetail}; // Các từ khóa mà bạn quan tâm
            imageFiles = new ArrayList<>();
            File mediaDir = getExternalMediaDirs()[0];
            File imageDirectory = new File(String.valueOf(mediaDir), date.replace("-", "")); // Thay đổi đường dẫn đến thư mục ảnh
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
        }
        return imageFiles;
    }

    private boolean isImageFile(File file) {
        String name = file.getName();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png");
        // Thêm các định dạng ảnh khác tương ứng
    }
    private List<ImageGroup> getDataGroup(String date){
        List<ImageGroup> imageGroups = new ArrayList<>();
        Cursor getGroup = Cre_db.getGroup(selectedKiemtra,date, selectedDetail,selectedDepartment);
        getGroup.moveToFirst();
        //Open FOR
        int a = getGroup.getCount();
        for (int i = 0; i < getGroup.getCount(); i++) {
            String tc_fcf001 = getGroup.getString(getGroup.getColumnIndexOrThrow("tc_fcf001"));
            String tc_fcf002 = getGroup.getString(getGroup.getColumnIndexOrThrow("tc_fcf002"));
            String tc_fcf003 = getGroup.getString(getGroup.getColumnIndexOrThrow("tc_fcf003"));
            String tc_fcd004 = getGroup.getString(getGroup.getColumnIndexOrThrow("tc_fcd004"));
            String tc_fcd005 = getGroup.getString(getGroup.getColumnIndexOrThrow("tc_fcd005"));
            String tc_fcc006 = getGroup.getString(getGroup.getColumnIndexOrThrow("tc_fcc006"));
            String tc_fcc007 = getGroup.getString(getGroup.getColumnIndexOrThrow("tc_fcc007"));
            String bophan = tc_fcf003 + "- " + tc_fcd004 + " " + tc_fcd005;
            String hangmuc = tc_fcf001 + " - " + tc_fcc007;
            List <File> searchImage= new ArrayList<>();
            Cursor getImage = Cre_db.getImage(tc_fcf002, tc_fcf001, tc_fcf003);
            getImage.moveToFirst();
            for (int x = 0; x < getImage.getCount(); x++) {
                String tc_fcf005 = getImage.getString(getImage.getColumnIndexOrThrow("tc_fcf005"));
                String image_path = "/storage/emulated/0/Android/media/com.lelong.moitruong/" + tc_fcf002.replace("-", "") + "/" + tc_fcf005;
                File file = new File(image_path);  // Thay thế bằng đường dẫn thực tế của tệp ảnh
                searchImage.add(file);
                getImage.moveToNext();
            }
            ImageGroup imageGroup = new ImageGroup(tc_fcf002, bophan,hangmuc, searchImage);
            imageGroups.add(imageGroup);
            getGroup.moveToNext();
        }
        return imageGroups;
    }
    private List<ImageGroup> generateSampleData() {
        List<ImageGroup> imageGroups = new ArrayList<>();
        if(!selectedWeek.equals(""))
        {
            List<String> weekdateList = getWeekDaysForMonth(Integer.parseInt(nam), Integer.parseInt(thang), Integer.parseInt(tuan));
            for (String date : weekdateList) {
                Cursor getGroup = Cre_db.getGroup(selectedKiemtra,date, selectedDetail,selectedDepartment);
                getGroup.moveToFirst();
                //Open FOR
                int a = getGroup.getCount();
                for (int i = 0; i < getGroup.getCount(); i++) {
                    String tc_fcf001 = getGroup.getString(getGroup.getColumnIndexOrThrow("tc_fcf001"));
                    String tc_fcf002 = getGroup.getString(getGroup.getColumnIndexOrThrow("tc_fcf002"));
                    String tc_fcf003 = getGroup.getString(getGroup.getColumnIndexOrThrow("tc_fcf003"));
                    String tc_fcd004 = getGroup.getString(getGroup.getColumnIndexOrThrow("tc_fcd004"));
                    String tc_fcd005 = getGroup.getString(getGroup.getColumnIndexOrThrow("tc_fcd005"));
                    String tc_fcc006 = getGroup.getString(getGroup.getColumnIndexOrThrow("tc_fcc006"));
                    String tc_fcc007 = getGroup.getString(getGroup.getColumnIndexOrThrow("tc_fcc007"));
                    String bophan = tc_fcf003 + "- " + tc_fcd004 + " " + tc_fcd005;
                    String hangmuc = tc_fcf001 + " - " + tc_fcc007;
                    List <File> searchImage= new ArrayList<>();
                    Cursor getImage = Cre_db.getImage(tc_fcf002, tc_fcf001, tc_fcf003);
                    getImage.moveToFirst();
                    for (int x = 0; x < getImage.getCount(); x++) {
                        String tc_fcf005 = getImage.getString(getImage.getColumnIndexOrThrow("tc_fcf005"));
                        String image_path = "/storage/emulated/0/Android/media/com.lelong.moitruong/" + tc_fcf002.replace("-", "") + "/" + tc_fcf005;
                        File file = new File(image_path);  // Thay thế bằng đường dẫn thực tế của tệp ảnh
                        searchImage.add(file);
                        getImage.moveToNext();
                    }
                    ImageGroup imageGroup = new ImageGroup(tc_fcf002, bophan,hangmuc, searchImage);
                    imageGroups.add(imageGroup);
                    getGroup.moveToNext();
                }
            }
        }
        else{
            Cursor getGroup = Cre_db.getGroup(selectedKiemtra,selectedDate, selectedDetail,selectedDepartment);
            getGroup.moveToFirst();
            //Open FOR
            int a = getGroup.getCount();
            for (int i = 0; i < getGroup.getCount(); i++) {
                String tc_fcf001 = getGroup.getString(getGroup.getColumnIndexOrThrow("tc_fcf001"));
                String tc_fcf002 = getGroup.getString(getGroup.getColumnIndexOrThrow("tc_fcf002"));
                String tc_fcf003 = getGroup.getString(getGroup.getColumnIndexOrThrow("tc_fcf003"));
                String tc_fcd004 = getGroup.getString(getGroup.getColumnIndexOrThrow("tc_fcd004"));
                String tc_fcd005 = getGroup.getString(getGroup.getColumnIndexOrThrow("tc_fcd005"));
                String tc_fcc006 = getGroup.getString(getGroup.getColumnIndexOrThrow("tc_fcc006"));
                String tc_fcc007 = getGroup.getString(getGroup.getColumnIndexOrThrow("tc_fcc007"));
                String bophan = tc_fcf003 + "- " + tc_fcd004 + " " + tc_fcd005;
                String hangmuc = tc_fcf001 + " - " + tc_fcc007;
                List <File> searchImage= new ArrayList<>();
                Cursor getImage = Cre_db.getImage(tc_fcf002, tc_fcf001, tc_fcf003);
                getImage.moveToFirst();
                for (int x = 0; x < getImage.getCount(); x++) {
                    String tc_fcf005 = getImage.getString(getImage.getColumnIndexOrThrow("tc_fcf005"));
                    String image_path = "/storage/emulated/0/Android/media/com.lelong.moitruong/" + tc_fcf002.replace("-", "") + "/" + tc_fcf005;
                    File file = new File(image_path);  // Thay thế bằng đường dẫn thực tế của tệp ảnh
                    searchImage.add(file);
                    getImage.moveToNext();
                }
                ImageGroup imageGroup = new ImageGroup(tc_fcf002, bophan,hangmuc, searchImage);
                imageGroups.add(imageGroup);
                getGroup.moveToNext();
            }
        }
        return imageGroups;
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