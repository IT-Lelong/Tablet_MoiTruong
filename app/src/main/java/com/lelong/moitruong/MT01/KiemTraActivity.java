package com.lelong.moitruong.MT01;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.lelong.moitruong.Create_Table;
import com.lelong.moitruong.R;

import java.util.ArrayList;

public class KiemTraActivity extends AppCompatActivity {
    private Create_Table Cre_db = null;
    RecyclerView rcv_kiemtra;
    Button btn_kiemtra, btn_KetChuyen, btn_TraCuu;
    private KiemTraActivity_RecyclerViewAdapter kiemTraActivity_recyclerViewAdapter;
    private PieChart pieChart;

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
                LoginDialogFragment dialogFragment = new LoginDialogFragment();
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

        call_completeChart();

        rcv_kiemtra.setLayoutManager(new LinearLayoutManager(this));
        Cursor cursor = Cre_db.departmentCheckedData();
        kiemTraActivity_recyclerViewAdapter = new KiemTraActivity_RecyclerViewAdapter(cursor);
        rcv_kiemtra.setAdapter(kiemTraActivity_recyclerViewAdapter);
    }

    private void call_completeChart() {
        float g_completed = Cre_db.getChartCompleteData();
        // Tạo danh sách dữ liệu cho biểu đồ
        ArrayList<PieEntry> entries = new ArrayList<>();
        // Lấy dữ liệu từ cơ sở dữ liệu và thêm vào danh sách entries
        float completedPercentage = g_completed; // Tỷ lệ hoàn thành (%)
        float remainingPercentage = 100.00f - completedPercentage; // Tỷ lệ chưa hoàn thành


        entries.add(new PieEntry(completedPercentage, "OK"));
        entries.add(new PieEntry(remainingPercentage, "NG"));

        // Tạo PieDataSet từ danh sách dữ liệu
        PieDataSet dataSet = new PieDataSet(entries, "Tiến độ Kiểm tra 5S");
        dataSet.setColors(ColorTemplate.PASTEL_COLORS);
        dataSet.setValueTextSize(20f); // Đặt cỡ chữ thành 16

        // Tạo PieData từ PieDataSet
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));

        // Thiết lập dữ liệu cho biểu đồ và cấu hình biểu đồ
        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.invalidate(); // Cập nhật biểu đồ
    }

    private void Call_showTranferDialog() {
        TransferDialog TransferDialog = new TransferDialog(this);
        TransferDialog.setOkButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_bdate = TransferDialog.getSelected_bDate();
                String input_edate = TransferDialog.getSelected_eDate();
                String input_factory = TransferDialog.getSelected_sp_factory();
                String input_department = TransferDialog.getSelected_sp_department();

                new KiemTraActivity_Transfer(getApplicationContext(),TransferDialog,input_bdate,input_edate,input_factory,input_department);
                //TransferDialog.dismiss();
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


}