package com.lelong.moitruong.MT01;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.lelong.moitruong.ApiInterface;
import com.lelong.moitruong.Constant_Class;
import com.lelong.moitruong.Create_Table;
import com.lelong.moitruong.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class KiemTraActivity extends AppCompatActivity implements OnSpinnerItemSelectedListener{
    private Create_Table Cre_db = null;
    RecyclerView rcv_kiemtra;
    Button btn_kiemtra, btn_KetChuyen, btn_TraCuu;
    private KiemTraActivity_RecyclerViewAdapter kiemTraActivity_recyclerViewAdapter;
    private PieChart pieChart;
    int memo_xuong, memo_bophan;

    @Override
    protected void onRestart() {
        super.onRestart();
        call_reloaFormData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mt01_kiem_tra_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Cre_db = new Create_Table(this);
        Cre_db.open();

        addControls();
        addEvents();
    }

    private void addEvents() {
        btn_KetChuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call_showTranferDialog();
            }
        });

        btn_kiemtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginDialogFragment dialogFragment = new LoginDialogFragment(memo_xuong,memo_bophan);
                dialogFragment.setSpinnerItemSelectedListener(KiemTraActivity.this);
                dialogFragment.show(getSupportFragmentManager(), "LoginDialogFragment");
            }
        });
        btn_TraCuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Search_Dialog search_dialog = new Search_Dialog();
                search_dialog.show(getSupportFragmentManager(), "Search_Dialog");
            }
        });

    }


    private void addControls() {
        rcv_kiemtra = findViewById(R.id.rcv_kiemtra);
        btn_kiemtra = findViewById(R.id.btn_kiemtra);
        btn_KetChuyen = findViewById(R.id.btn_KetChuyen);
        btn_TraCuu = findViewById(R.id.btn_TraCuu);
        pieChart = findViewById(R.id.pieChart);
        rcv_kiemtra.setLayoutManager(new LinearLayoutManager(this));
        call_reloaFormData();
    }

    private void call_reloaFormData() {
        call_completeChart();
        Cursor cursor = Cre_db.departmentCheckedData();
        kiemTraActivity_recyclerViewAdapter = new KiemTraActivity_RecyclerViewAdapter(this,cursor);
        rcv_kiemtra.setAdapter(kiemTraActivity_recyclerViewAdapter);
    }

    private void call_completeChart() {
        Cursor g_completed = Cre_db.getChartCompleteData();
        // Tạo danh sách dữ liệu cho biểu đồ
        ArrayList<PieEntry> entries = new ArrayList<>();
        // Lấy dữ liệu từ cơ sở dữ liệu và thêm vào danh sách entrie
        //float completedPercentage = g_completed; // Tỷ lệ hoàn thành (%)
        //float remainingPercentage = 100.00f - completedPercentage; // Tỷ lệ chưa hoàn thành
        g_completed.moveToFirst();
        String bophan,g_tc_fcd004,g_tc_fcd005;
        float value;
        for (int i = 0; i < g_completed.getCount(); i++) {
            g_tc_fcd004 = g_completed.getString(g_completed.getColumnIndexOrThrow("tc_fcd004"));
            g_tc_fcd005 = g_completed.getString(g_completed.getColumnIndexOrThrow("tc_fcd005"));
            value = Float.valueOf(g_completed.getString(g_completed.getColumnIndexOrThrow("g_count")));
            bophan = g_tc_fcd004 + " " + g_tc_fcd005;
            entries.add(new PieEntry(value, bophan));
            g_completed.moveToNext();
        }
        // Tạo PieDataSet từ danh sách dữ liệu
        ArrayList<Integer> uniqueBrightContrastColors = new ArrayList<>();
        Random random = new Random();
        Set<Integer> colorSet = new HashSet<>(); // Dùng để kiểm tra tính duy nhất

        int numberOfColors = 16; // Số lượng màu bạn muốn sử dụng
        while (uniqueBrightContrastColors.size() < numberOfColors) {
            // Tạo màu sắc (hue) ngẫu nhiên
            float hue = random.nextFloat() * 360; // 0-360 độ
            // Sử dụng màu sắc (hue) ngẫu nhiên và gia tăng độ tương phản và độ tươi sáng
            float saturation = 0.7f + random.nextFloat() * 0.3f; // Độ bão hòa (0.7-1.0)
            float brightness = 0.7f + random.nextFloat() * 0.3f; // Độ tương phản (0.7-1.0)
            int color = Color.HSVToColor(new float[]{hue, saturation, brightness});
            // Kiểm tra xem màu đã tồn tại trong danh sách chưa
            if (colorSet.add(color)) {
                uniqueBrightContrastColors.add(color);
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(uniqueBrightContrastColors);
        dataSet.setValueTextSize(20f); // Đặt cỡ chữ thành 20

        // Tạo PieData từ PieDataSet
        PieData data = new PieData(dataSet);
        // Đặt chú thích màu bên ngoài biểu đồ
        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);
        legend.setWordWrapEnabled(true);

        data.setValueFormatter(new PercentFormatter(pieChart));

        // Thiết lập dữ liệu cho biểu đồ và cấu hình biểu đồ
        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);

        pieChart.setUsePercentValues(true);
        pieChart.invalidate(); // Cập nhật biểu đồ
    }

    private void Call_showTranferDialog() {
        TransferDialog TransferDialog = new TransferDialog(this);
        TransferDialog.setEnableBtn(true,true);
        TransferDialog.setOkButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_bdate = TransferDialog.getSelected_bDate();
                String input_edate = TransferDialog.getSelected_eDate();
                String input_factory = TransferDialog.getSelected_sp_factory();
                String input_department = TransferDialog.getSelected_sp_department();

                new KiemTraActivity_Transfer(getApplicationContext(),TransferDialog,input_bdate,input_edate,input_factory,input_department);
            }
        });
        TransferDialog.setCancelButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransferDialog.dismiss();
            }
        });
        TransferDialog.show();
    }


    @Override
    public void onItemSelected(int memo_xuong,int memo_bophan) {
        this.memo_xuong = memo_xuong;
        this.memo_bophan= memo_bophan;
    }
}