package com.lelong.moitruong.MT01;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lelong.moitruong.Create_Table;
import com.lelong.moitruong.R;

public class KiemTraActivity extends AppCompatActivity {
    private Create_Table Cre_db = null;
    RecyclerView rcv_kiemtra;
    Button btn_kiemtra, btn_KetChuyen, btn_TraCuu;
    private KiemTraActivity_RecyclerViewAdapter kiemTraActivity_recyclerViewAdapter;

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
    }

    private void addControls() {
        rcv_kiemtra = findViewById(R.id.rcv_kiemtra);
        btn_kiemtra = findViewById(R.id.btn_kiemtra);
        btn_KetChuyen = findViewById(R.id.btn_KetChuyen);
        btn_TraCuu = findViewById(R.id.btn_TraCuu);

        rcv_kiemtra.setLayoutManager(new LinearLayoutManager(this));
        Cursor cursor = Cre_db.departmentCheckedData();
        kiemTraActivity_recyclerViewAdapter = new KiemTraActivity_RecyclerViewAdapter(cursor);
        rcv_kiemtra.setAdapter(kiemTraActivity_recyclerViewAdapter);
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