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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.lelong.moitruong.Create_Table;
import com.lelong.moitruong.R;

import java.util.ArrayList;
import java.util.List;

public class LoginDialogFragment extends DialogFragment {
    private Create_Table Cre_db = null;
    Spinner sp_factory,sp_department;
    Button btnOk,btnCancel;
    Cursor cur_getdata;

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

        List<String> factory_List = new ArrayList<>();
        List<String> department_List = new ArrayList<>();
        //itemList.add("");
        factory_List.add("Đức Hòa");
        factory_List.add("Bến Lức");

        ArrayAdapter<String> factory_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, factory_List);
        ArrayAdapter<String> department_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, department_List);
        sp_factory.setAdapter(factory_adapter);
        sp_department.setAdapter( department_adapter);

        sp_factory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cur_getdata = null;
                    switch (position){
                        case 0:
                            cur_getdata = Cre_db.getdata_tc_fcd("DH");
                            break;
                        case 1:
                            cur_getdata = Cre_db.getdata_tc_fcd("BL");
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + position);
                    }

                if (cur_getdata.getCount() > 0 ) {
                    cur_getdata.moveToFirst();
                    for(int i = 0 ; i < cur_getdata.getCount();i++){
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
                String g_maBP = String.valueOf(cur_getdata.getColumnIndexOrThrow("tc_fcd006"));
                String g_spFactory = String.valueOf(sp_factory.getSelectedItem());

                Intent hangmucIntent = new Intent();
                hangmucIntent.setClass(getContext(), HangMucKiemTra.class);
                Bundle bundle = new Bundle();
                bundle.putString("FACTORY", g_spFactory);
                bundle.putString("DEPNO", g_maBP);
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
