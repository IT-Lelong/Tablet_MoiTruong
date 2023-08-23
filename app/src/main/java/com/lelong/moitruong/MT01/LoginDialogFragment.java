package com.lelong.moitruong.MT01;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
        Spinner sp_factory = view.findViewById(R.id.sp_factory);
        Spinner sp_department = view.findViewById(R.id.sp_department);

        List<String> itemList = new ArrayList<>();
        //itemList.add("");
        itemList.add("Đức Hòa");
        itemList.add("Bến Lức");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, itemList);
        sp_factory.setAdapter(adapter);
        sp_factory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cursor cur_getdata;
                    switch (position){
                        case 0:
                            cur_getdata = Cre_db.getdata_tc_fcd("DH");
                            break;
                        case  1:
                            cur_getdata = Cre_db.getdata_tc_fcd("BL");
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + position);
                    }

                if (cur_getdata.getCount() > 0 ) {
                    cur_getdata.moveToFirst();
                    for(int i = 0 ; i < cur_getdata.getCount();i++){

                        cur_getdata.moveToNext();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }
}
