package com.lelong.moitruong.MT01;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lelong.moitruong.Create_Table;
import com.lelong.moitruong.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransferDialog {
    private Create_Table Cre_db = null;
    private Context context;
    private Dialog dialog;
    private TextView tv_bdate, tv_edate, tv_processing;
    private Spinner sp_factory, sp_department;
    private ProgressBar progBar_transfer;
    private Button btnOk, btnCancel;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    Cursor cur_getdata;
    List<String> factory_List, department_List;
    ArrayAdapter<String> factory_adapter, department_adapter;
    String g_today;

    public TransferDialog(Context context) {
        this.context = context;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.mt01_data_transfer_dialog);
        dialog.setCancelable(false);

        Cre_db = new Create_Table(context);
        Cre_db.open();

        addControls();
        addEvents();
    }

    private void addControls() {
        tv_bdate = dialog.findViewById(R.id.tv_bdate);
        tv_edate = dialog.findViewById(R.id.tv_edate);
        tv_processing = dialog.findViewById(R.id.tv_processing);
        progBar_transfer = dialog.findViewById(R.id.progBar_transfer);
        sp_factory = dialog.findViewById(R.id.sp_factory);
        sp_department = dialog.findViewById(R.id.sp_department);
        btnOk = dialog.findViewById(R.id.btnOk);
        btnCancel = dialog.findViewById(R.id.btnCancel);

        g_today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        tv_bdate.setHint(g_today);
        tv_edate.setHint(g_today);

        factory_List = new ArrayList<>();
        department_List = new ArrayList<>();
        factory_List.add("");
        factory_List.add("Đức Hòa");
        factory_List.add("Bến Lức");
        factory_adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, factory_List);
        department_adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, department_List);
        sp_factory.setAdapter(factory_adapter);
        sp_department.setAdapter(department_adapter);
    }

    private void addEvents() {
        tv_bdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerTransferDialog(tv_bdate);
            }
        });

        tv_edate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerTransferDialog(tv_edate);
            }
        });

        sp_factory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cur_getdata = null;
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        cur_getdata = Cre_db.getdata_tc_fcd("DH");
                        break;
                    case 2:
                        cur_getdata = Cre_db.getdata_tc_fcd("BL");
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + position);
                }

                if (cur_getdata != null && cur_getdata.moveToFirst()) {
                    cur_getdata.moveToFirst();
                    department_List.add("");
                    for (int i = 0; i < cur_getdata.getCount(); i++) {
                        String g_ten = cur_getdata.getString(cur_getdata.getColumnIndexOrThrow("tc_fcd004"));
                        String g_xuong = cur_getdata.getString(cur_getdata.getColumnIndexOrThrow("tc_fcd005"));
                        String materialInfo = g_ten + " - " + g_xuong;
                        department_List.add(materialInfo);
                        cur_getdata.moveToNext();
                    }
                    department_adapter.notifyDataSetChanged();
                    cur_getdata.close();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showDatePickerTransferDialog(TextView tv_var) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(dialog.getContext());
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Xử lý khi người dùng chọn ngày
                String selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                if (tv_var == tv_bdate) {
                    if (tv_edate.getText().toString() == "") {
                        tv_var.setText(selectedDate);
                        tv_edate.setText(selectedDate);
                    } else {
                        try {
                            if (dateFormat.parse(selectedDate).after(dateFormat.parse(tv_edate.getText().toString()))) {
                                tv_var.setText(tv_edate.getText().toString());
                            }else{
                                tv_var.setText(selectedDate);
                            }
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                if (tv_var == tv_edate) {
                    if (tv_bdate.getText().toString() == "") {
                        tv_var.setText(selectedDate);
                        tv_bdate.setText(selectedDate);
                    } else {
                        try {
                            if (dateFormat.parse(selectedDate).before(dateFormat.parse(tv_bdate.getText().toString()))) {
                                tv_var.setText(tv_bdate.getText().toString());
                            }else{
                                tv_var.setText(selectedDate);
                            }
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
        datePickerDialog.show();
    }

    public void setProgressBar(int g_value) {
        progBar_transfer.setMax(g_value);
    }

    public void updateProgressBar(int progress) {
        progBar_transfer.setProgress(progress);
    }

    public String getSelected_bDate() {
        return tv_bdate.getText().toString().trim();
    }

    public String getSelected_eDate() {
        return tv_edate.getText().toString().trim();
    }

    public String getSelected_sp_factory() {
        return sp_factory.getSelectedItem().toString();
    }

    public String getSelected_sp_department() {
        String selectedDepartment = (sp_department.getSelectedItem() != null) ? sp_department.getSelectedItem().toString() : "";
        return selectedDepartment;
    }

    public void setOkButtonClickListener(View.OnClickListener listener) {
        btnOk.setOnClickListener(listener);
    }

    public void setCancelButtonClickListener(View.OnClickListener listener) {
        btnCancel.setOnClickListener(listener);
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
