package com.lelong.moitruong.MT01;


import static android.content.Intent.getIntent;
import static android.content.Intent.getIntentOld;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.lelong.moitruong.Constant_Class;
import com.lelong.moitruong.Create_Table;
import com.lelong.moitruong.MainActivity;
import com.lelong.moitruong.R;

import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Search_Dialog extends DialogFragment {
    private static final int REQUEST_IMAGE_CAPTURE = 99;
    private Create_Table Cre_db = null;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Spinner hm_kiemtra, hm_con,sp_bophan,sp_tuan;
    Button btnOk, btnCancel;
    Cursor cur_getbophan;
    private String myVariable;
    String result;
    DatePickerDialog datePickerDialog;
    // Constructor không đối số bắt buộc
    //public Search_Dialog() {
    //}
    // Phương thức để thiết lập giá trị biến
    //public void setMyVariable(String variable) {
    //  myVariable = variable;
    //}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mt01_search_dialog, container, false);

        // Áp dụng animation vào dialog
        Animation scaleAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up);
        view.startAnimation(scaleAnimation);

        Cre_db = new Create_Table(getContext());
        Cre_db.open();
        addControl(view);

        return view;
    }
    // Định nghĩa một Interface callback
    //public interface OnDialogResultListener {
    //void onDialogResult(String result);
    //}

    //OnDialogResultListener callback;

    private void addControl(View view) {
        sp_tuan = view.findViewById(R.id.sp_tuan);
        hm_kiemtra = view.findViewById(R.id.hm_kiemtra);
        hm_con= view.findViewById(R.id.hm_con);
        sp_bophan = view.findViewById(R.id.sp_bophan);
        btnOk = view.findViewById(R.id.btnOk);
        btnCancel = view.findViewById(R.id.btnCancel);

        List<String> kiemtra_List = new ArrayList<>();
        List<String> con_List = new ArrayList<>();
        List<String> bophan_List = new ArrayList<>();
        List<String> tuan_List = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM/yyyy", Locale.getDefault());

        for (int i = 0; i >= -10; i--) { // Hiển thị 8 tuần gần nhất trở về trước
            String monthYear = monthFormat.format(calendar.getTime());
            int weekInMonth = calendar.get(Calendar.WEEK_OF_MONTH);
            //weeks.add(new WeekData(weekInMonth, monthYear));
            String tuan = monthYear + " - " + String.valueOf(weekInMonth);
            tuan_List.add(tuan);
            calendar.add(Calendar.DAY_OF_MONTH, -7); // Chuyển sang tuần trước đó
        }
        Cursor curs_hangmuclon = Cre_db.getHangMucLon();
        curs_hangmuclon.moveToFirst();
        for (int i = 0; i < curs_hangmuclon.getCount(); i++) {
            String data = curs_hangmuclon.getString(curs_hangmuclon.getColumnIndexOrThrow("tc_fcb005"));
            kiemtra_List.add(data);
            curs_hangmuclon.moveToNext();
        }
        kiemtra_List.add("");
        //itemList.add("");
        //factory_List.add("Đức Hòa");
        //factory_List.add("Bến Lức");

        ArrayAdapter<String> tuan_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, tuan_List);
        ArrayAdapter<String> kiemtra_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, kiemtra_List);
        ArrayAdapter<String> bophan_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, bophan_List);
        hm_kiemtra.setAdapter(kiemtra_adapter);
        hm_kiemtra.setSelection(kiemtra_List.size() - 1);
        sp_bophan.setAdapter(bophan_adapter);
        sp_tuan.setAdapter(tuan_adapter);
        String xuong= null;
        cur_getbophan = null;
        cur_getbophan = Cre_db.getdata_tc_fcd(Constant_Class.UserFactory);
        if (cur_getbophan.getCount() > 0) {
            cur_getbophan.moveToFirst();
            for (int i = 0; i < cur_getbophan.getCount(); i++) {
                String g_ten = cur_getbophan.getString(cur_getbophan.getColumnIndexOrThrow("tc_fcd004"));
                String g_xuong = cur_getbophan.getString(cur_getbophan.getColumnIndexOrThrow("tc_fcd005"));
                String materialInfo = g_ten + " - " + g_xuong;
                bophan_List.add(materialInfo);
                cur_getbophan.moveToNext();
            }
            bophan_List.add("");
            bophan_adapter.notifyDataSetChanged();
            sp_bophan.setSelection(bophan_List.size() - 1);
        }
        /*edt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy ngày, tháng và năm hiện tại
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Tạo DatePickerDialog
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Xử lý ngày đã chọn
                                //String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, monthOfYear);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                String formattedDate = sdf.format(calendar.getTime());
                                edt_date.setText(formattedDate);
                            }
                        }, year, month, day);

                // Hiển thị DatePickerDialog
                datePickerDialog.show();
            }
        });*/

        hm_kiemtra.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String g_index="";
                String name_hm_kiemtra = kiemtra_List.get(position);
                int index = kiemtra_List.indexOf(kiemtra_List.get(position));
                g_index = String.valueOf(index);
                Cursor curs_hangmuccon = Cre_db.get_hangmucchitiet(g_index,"");
                curs_hangmuccon.moveToFirst();
                con_List.clear();
                for (int i = 0; i < curs_hangmuccon.getCount(); i++) {
                    //con_List.remove(0);
                    String data = curs_hangmuccon.getString(curs_hangmuccon.getColumnIndexOrThrow("tc_fcc007"));
                    con_List.add(data);
                    curs_hangmuccon.moveToNext();
                }
                con_List.add("");
                ArrayAdapter<String> con_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, con_List);
                hm_con.setAdapter(con_adapter);
                hm_con.setSelection(con_List.size() - 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date=null;
                String g_tc_fcc05=null;
                String g_tc_fcd006=null;
                /*boolean isChecked = checkbox.isChecked();
                if (isChecked) {
                    // Checkbox đã được kiểm tra, thực hiện hành động tương ứng
                    date = "";
                    g_tc_fcd006="";
                    g_tc_fcc05="";
                } else {*/
                    // Checkbox chưa được kiểm tra, thực hiện hành động khác (nếu cần)
                    //date = edt_date.getText().toString();
                    String tuan_thang = sp_tuan.getSelectedItem().toString();
                    int g_bpPosition = sp_bophan.getSelectedItemPosition();
                    int g_hmkiemtraPosition = hm_kiemtra.getSelectedItemPosition();
                    int g_conPosition = hm_con.getSelectedItemPosition();
                    String g_position="";
                    String g_hmconPosition="";
                    if (g_hmkiemtraPosition == 8){
                        g_position="";
                        g_hmconPosition= "";
                        g_tc_fcc05 ="";
                    }else{
                        g_position = String.valueOf(g_hmkiemtraPosition);
                        if(hm_con.getSelectedItem().toString().equals("")){
                            g_hmconPosition="";
                            g_tc_fcc05 ="";
                        }
                        else{
                            g_hmconPosition = String.valueOf(hm_con.getSelectedItemPosition());
                            Cursor curs_data = Cre_db.get_hangmucchitiet(g_position,g_hmconPosition);
                            curs_data.moveToFirst();
                            for (int i = 0; i < curs_data.getCount(); i++) {
                                g_tc_fcc05 = curs_data.getString(curs_data.getColumnIndexOrThrow("tc_fcc005"));
                                curs_data.moveToNext();
                            }
                        }
                    }
                    /*if(g_hmconPosition.equals(""))
                    {
                        g_tc_fcc05 ="";
                    }
                    else{
                        Cursor curs_data = Cre_db.get_hangmucchitiet(g_position,g_hmconPosition);
                        curs_data.moveToFirst();
                        for (int i = 0; i < curs_data.getCount(); i++) {
                            g_tc_fcc05 = curs_data.getString(curs_data.getColumnIndexOrThrow("tc_fcc005"));
                            curs_data.moveToNext();
                        }
                    }*/
                    if(sp_bophan.getSelectedItem().toString().equals(""))
                    {
                        g_tc_fcd006 ="";
                    }
                    else{
                        cur_getbophan.moveToPosition(g_bpPosition);
                        g_tc_fcd006 = String.valueOf(cur_getbophan.getString(cur_getbophan.getColumnIndexOrThrow("tc_fcd006")));
                    }

                //}
                dismiss();
                Intent intent = new Intent(getContext(), ThuVien_Anh.class);
                intent.putExtra("ngay", "");
                intent.putExtra("tuan", tuan_thang);
                intent.putExtra("bophan", g_tc_fcd006);
                intent.putExtra("hangmuclon", g_position);
                intent.putExtra("hangmuc", g_tc_fcc05);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Thêm cờ vào Intent
                startActivity(intent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}
