package com.lelong.moitruong.MT01;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lelong.moitruong.Constant_Class;
import com.lelong.moitruong.Create_Table;
import com.lelong.moitruong.R;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.recyclerview.animators.ScaleInLeftAnimator;

//public class HangMucKiemTra extends AppCompatActivity implements HangMucKiemTra_Adapter.OnCaptureImageClickListener {
public class HangMucKiemTra extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 99;
    private Create_Table Cre_db = null;
    DecimalFormat decimalFormat;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String g_factory, g_maBP, g_tenBP, g_ngay,g_user;
    RecyclerView rcv_hangmuc;
    ListView lv_hangmuclon;
    List<String> hangMucLon_list;
    List hangmucChiTiet_list;
    HangMucKiemTra_Adapter hangMucKiemTra_adapter;
    TextView tv_ngay, tv_bophan;
    Integer g_hangMucPosition = 0;
    private String capturedImageDept;
    private String capturedImageName;


    @Override
    protected void onResume() {
        super.onResume();
        Call_updateData(g_hangMucPosition, g_ngay, g_maBP, "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mt01_hang_muc_main_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Bundle getbundle = getIntent().getExtras();
        g_factory = getbundle.getString("FACTORY");
        g_maBP = getbundle.getString("DEPNO");
        g_tenBP = getbundle.getString("DEPNAME");
        g_ngay = getbundle.getString("DATE");
        //g_user = getbundle.getString("USER");
        addcontrols();
        addEvent();
    }

    private void addEvent() {
        tv_ngay.setText(dateFormat.format(new Date()));
        tv_bophan.setText(g_tenBP);

        //Lấy dữ liệu hạng mục lớn vào listview (S)
        hangMucLon_list = new ArrayList<>();
        Cursor curs_hangmuclon = Cre_db.getHangMucLon();
        curs_hangmuclon.moveToFirst();
        for (int i = 0; i < curs_hangmuclon.getCount(); i++) {
            String data = curs_hangmuclon.getString(curs_hangmuclon.getColumnIndexOrThrow("tc_fcb005"));
            hangMucLon_list.add(data);

            curs_hangmuclon.moveToNext();
        }
        curs_hangmuclon.close();
        ArrayAdapter<String> hangMucLon_adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, hangMucLon_list
        );
        lv_hangmuclon.setAdapter(hangMucLon_adapter);
        //Lấy dữ liệu hạng mục lớn vào listview (E)

        //Chọn hạng mục lớn và truy xuất dữ liệu hạng mục kiểm tra (S)
        lv_hangmuclon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                g_hangMucPosition = position;
                Call_updateData(position, g_ngay, g_maBP, "");
            }
        });
        //Chọn hạng mục lớn và truy xuất dữ liệu hạng mục kiểm tra (E)
    }

    private void addcontrols() {
        Cre_db = new Create_Table(getApplicationContext());
        Cre_db.open();

        rcv_hangmuc = findViewById(R.id.rcv_hangmuc);
        lv_hangmuclon = findViewById(R.id.lv_hangmuclon);
        tv_ngay = findViewById(R.id.tv_ngay);
        tv_bophan = findViewById(R.id.tv_bophan);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HangMucKiemTra.this);
        rcv_hangmuc.setLayoutManager(linearLayoutManager);
        // Áp dụng hiệu ứng xuất hiện từ trái sang phải
        ScaleInLeftAnimator animator = new ScaleInLeftAnimator();
        animator.setAddDuration(500); // Thiết lập thời gian hiệu ứng khi thêm item (ms)
        animator.setRemoveDuration(500); // Thiết lập thời gian hiệu ứng khi xóa item (ms)
        rcv_hangmuc.setItemAnimator(animator);

        hangmucChiTiet_list = new ArrayList<HangMucKiemTra_Model>();
        /*hangMucKiemTra_adapter = new HangMucKiemTra_Adapter(getApplicationContext(),
                R.layout.mt01_hang_muc_main_item,
                hangmucChiTiet_list,
                g_ngay,
                g_maBP,
                this::onCaptureImageClick);*/
        hangMucKiemTra_adapter = new HangMucKiemTra_Adapter(getApplicationContext(),
                R.layout.mt01_hang_muc_main_item,g_hangMucPosition,
                hangmucChiTiet_list,
                g_ngay,
                g_maBP);
        rcv_hangmuc.setAdapter(hangMucKiemTra_adapter);
    }

    private void Call_updateData(Integer g_hangMucPosition, String g_ngay, String g_maBP, String userID) {
        hangmucChiTiet_list.clear();
        Cursor curs_hmChiTiet = Cre_db.getHangMucChiTiet(g_hangMucPosition, g_ngay, g_maBP, "");
        curs_hmChiTiet.moveToFirst();
        for (int i = 0; i < curs_hmChiTiet.getCount(); i++) {
            String g_tc_fcc004 = curs_hmChiTiet.getString(curs_hmChiTiet.getColumnIndexOrThrow("tc_fcc004"));
            String g_tc_fcc005 = curs_hmChiTiet.getString(curs_hmChiTiet.getColumnIndexOrThrow("tc_fcc005"));
            String g_tc_fcc006 = curs_hmChiTiet.getString(curs_hmChiTiet.getColumnIndexOrThrow("tc_fcc006"));
            String g_tc_fcc007 = curs_hmChiTiet.getString(curs_hmChiTiet.getColumnIndexOrThrow("tc_fcc007"));
            String g_tc_fcc008 = curs_hmChiTiet.getString(curs_hmChiTiet.getColumnIndexOrThrow("tc_fcc008"));

            String g_tc_fce006 = curs_hmChiTiet.getString(curs_hmChiTiet.getColumnIndexOrThrow("tc_fce006"));
            String g_tc_fce007 = curs_hmChiTiet.getString(curs_hmChiTiet.getColumnIndexOrThrow("tc_fce007"));
            String g_tc_fce008 = curs_hmChiTiet.getString(curs_hmChiTiet.getColumnIndexOrThrow("tc_fce008"));

            hangmucChiTiet_list.add(new HangMucKiemTra_Model(g_tc_fcc004, g_tc_fcc005, g_tc_fcc006, g_tc_fcc007, g_tc_fcc008, g_tc_fce006, g_tc_fce007, g_tc_fce008));
            curs_hmChiTiet.moveToNext();
        }

        hangMucKiemTra_adapter.notifyDataSetChanged();
    }

    //Code chụp hình (S)
    /*@Override
    public void onCaptureImageClick(String g_tc_fcc005) {
        captureImage(g_tc_fcc005);
    }

    private void captureImage(String g_tc_fcc005) {
        // Tạo tệp ảnh tạm thời với tên mới
        String timestamp = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = g_maBP + "_" + g_tc_fcc005 + "_" + Constant_Class.UserID + "_" + g_ngay + "_" + timestamp + "";
        capturedImageName = imageFileName;
        capturedImageDept = g_tc_fcc005;

        // Lấy thư mục lưu trữ ảnh (trong ví dụ này, lấy thư mục ứng dụng)
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = null;
        File[] mediaDirs = getExternalMediaDirs();
        if (mediaDirs.length > 0) {
            storageDir = mediaDirs[0];
        }

        // Tạo tệp ảnh mới
        File imageFile = new File(storageDir, imageFileName + ".png");

        // Lưu đường dẫn của tệp ảnh mới
        //String imagePath = imageFile.getAbsolutePath();

        // Tạo Intent để mở ứng dụng máy ảnh và lưu ảnh tại đường dẫn mới
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", imageFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);


        // Kiểm tra xem thiết bị có ứng dụng máy ảnh nào để xử lý yêu cầu này không
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                // Mã hạng mục : capturedImageDept
                // Tên của ảnh : capturedImageName
                Cre_db.call_insertPhotoData(capturedImageDept, g_ngay, g_maBP, Constant_Class.UserID, capturedImageName);
                Toast.makeText(this, "Save: " + capturedImageName, Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Thực hiện các xử lý cần thiết khi việc chụp ảnh bị hủy
                capturedImageDept = ""; // Đường dẫn tới ảnh sau khi chụp
                capturedImageName = ""; // Tên mới của ảnh
            }
        }
    }*/
    //Code chụp hình (E)

}