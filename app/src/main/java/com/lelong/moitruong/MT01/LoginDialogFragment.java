package com.lelong.moitruong.MT01;

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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

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
        ArrayAdapter<String> department_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, department_List);
        sp_factory.setAdapter(factory_adapter);
        sp_department.setAdapter(department_adapter);

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
                    cur_getdata.moveToFirst();
                    for (int i = 0; i < cur_getdata.getCount(); i++) {
                        String g_ten = cur_getdata.getString(cur_getdata.getColumnIndexOrThrow("tc_fcd004"));
                        String g_xuong = cur_getdata.getString(cur_getdata.getColumnIndexOrThrow("tc_fcd005"));
                        String materialInfo = g_ten + " - " + g_xuong;
                        department_List.add(materialInfo);
                        cur_getdata.moveToNext();
                    }
                    department_adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int g_spDepPosition = sp_department.getSelectedItemPosition();
                cur_getdata.moveToPosition(g_spDepPosition);
                String g_tc_fcd003 = String.valueOf(cur_getdata.getString(cur_getdata.getColumnIndexOrThrow("tc_fcd003")));
                String g_tc_fcd004 = String.valueOf(cur_getdata.getString(cur_getdata.getColumnIndexOrThrow("tc_fcd004")));
                String g_tc_fcd005 = String.valueOf(cur_getdata.getString(cur_getdata.getColumnIndexOrThrow("tc_fcd005")));
                String g_maBP = String.valueOf(cur_getdata.getString(cur_getdata.getColumnIndexOrThrow("tc_fcd006")));
                String g_spFactory = String.valueOf(sp_factory.getSelectedItem());
                String g_tenBP = g_tc_fcd003 + " " + g_tc_fcd004 + " " + g_tc_fcd005;
                String g_ngay = tv_ngaykiemtra.getText().toString();
                dismiss();

                Intent hangmucIntent = new Intent();
                hangmucIntent.setClass(getContext(), HangMucKiemTra.class);
                Bundle bundle = new Bundle();
                bundle.putString("FACTORY", g_spFactory);
                bundle.putString("DEPNO", g_maBP);
                bundle.putString("DEPNAME", g_tenBP);
                bundle.putString("DATE", g_ngay);
                hangmucIntent.putExtras(bundle);
                startActivity(hangmucIntent);
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
