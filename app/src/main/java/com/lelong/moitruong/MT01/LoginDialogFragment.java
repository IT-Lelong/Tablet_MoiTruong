package com.lelong.moitruong.MT01;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.lelong.moitruong.Constant_Class;
import com.lelong.moitruong.Create_Table;
import com.lelong.moitruong.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoginDialogFragment extends DialogFragment {
    private Create_Table Cre_db = null;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Spinner sp_factory, sp_department;
    Button btnOk, btnCancel;
    Cursor cur_getdata;
    TextView tv_ngaykiemtra;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mt01_login_layout, container, false);

        // Áp dụng animation vào dialog
        Animation scaleAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up);
        view.startAnimation(scaleAnimation);

        Cre_db = new Create_Table(getContext());
        Cre_db.open();
        addControl(view);

        return view;
    }

    private void addControl(View view) {
        sp_factory = view.findViewById(R.id.sp_factory);
        sp_department = view.findViewById(R.id.sp_department);
        btnOk = view.findViewById(R.id.btnOk);
        btnCancel = view.findViewById(R.id.btnCancel);
        tv_ngaykiemtra = view.findViewById(R.id.tv_ngaykiemtra);
        tv_ngaykiemtra.setText(String.valueOf(dateFormat.format(new Date())));

        List<String> factory_List = new ArrayList<>();
        List<String> department_List = new ArrayList<>();
        //itemList.add("");
        factory_List.add("Đức Hòa");
        factory_List.add("Bến Lức");
        ArrayAdapter<String> factory_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, factory_List);
        //ArrayAdapter<String> department_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, department_List);
        sp_factory.setAdapter(factory_adapter);


        sp_factory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cur_getdata = null;
                switch (position) {
                    case 0:
                        cur_getdata = Cre_db.getdata_tc_fcd("DH");
                        break;
                    case 1:
                        cur_getdata = Cre_db.getdata_tc_fcd("BL");
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + position);
                }

                if (cur_getdata.getCount() > 0) {
                    department_List.clear();
                    cur_getdata.moveToFirst();
                    String materialInfo = null;
                    for (int i = 0; i < cur_getdata.getCount(); i++) {
                        String g_ten = cur_getdata.getString(cur_getdata.getColumnIndexOrThrow("tc_fcd004"));
                        String g_xuong = cur_getdata.getString(cur_getdata.getColumnIndexOrThrow("tc_fcd005"));
                        if (g_xuong.equals("null")) {
                            materialInfo = g_ten;
                        } else {
                            materialInfo = g_ten + " - " + g_xuong;
                        }
                        department_List.add(materialInfo);
                        cur_getdata.moveToNext();
                    }
                } else {
                    department_List.clear();
                }

                Cursor getDepartment_today = Cre_db.getDepartment_today(String.valueOf(dateFormat.format(new Date())));
                final Spinner_Adapter department_adapter = new Spinner_Adapter(getContext(), getDepartment_today, department_List);
                sp_department.setAdapter(department_adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int g_spDepPosition = sp_department.getSelectedItemPosition();
                String g_maBP = null, g_spFactory = null, g_tenBP = null, g_ngay = null;
                if (g_spDepPosition >= 0) {
                    cur_getdata.moveToPosition(g_spDepPosition);
                    String g_tc_fcd003 = String.valueOf(cur_getdata.getString(cur_getdata.getColumnIndexOrThrow("tc_fcd003")));
                    String g_tc_fcd004 = String.valueOf(cur_getdata.getString(cur_getdata.getColumnIndexOrThrow("tc_fcd004")));
                    String g_tc_fcd005 = String.valueOf(cur_getdata.getString(cur_getdata.getColumnIndexOrThrow("tc_fcd005")));
                    g_maBP = String.valueOf(cur_getdata.getString(cur_getdata.getColumnIndexOrThrow("tc_fcd006")));
                    g_spFactory = String.valueOf(sp_factory.getSelectedItem());
                    if (g_tc_fcd005.equals("null")) {
                        g_tc_fcd005 = "";
                    }
                    g_tenBP = g_tc_fcd003 + " " + g_tc_fcd004 + " " + g_tc_fcd005;
                    g_ngay = tv_ngaykiemtra.getText().toString();


                    Intent hangmucIntent = new Intent();
                    hangmucIntent.setClass(getContext(), HangMucKiemTra.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("FACTORY", g_spFactory);
                    bundle.putString("DEPNO", g_maBP);
                    bundle.putString("DEPNAME", g_tenBP);
                    bundle.putString("DATE", g_ngay);
                    bundle.putString("USER", Constant_Class.UserID);
                    hangmucIntent.putExtras(bundle);
                    startActivity(hangmucIntent);
                } else {
                    Toast.makeText(getContext(), "Bộ phận rỗng!", Toast.LENGTH_SHORT).show();
                }
                dismiss();
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
