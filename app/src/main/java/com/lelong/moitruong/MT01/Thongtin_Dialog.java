package com.lelong.moitruong.MT01;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.lelong.moitruong.Create_Table;
import com.lelong.moitruong.R;
import java.text.SimpleDateFormat;

public class Thongtin_Dialog extends DialogFragment {
    private static final int REQUEST_IMAGE_CAPTURE = 99;
    private Create_Table Cre_db = null;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    TextView tv_ngay,tv_bophan,tv_maH,tv_hmlon,tv_hmchitiet1,tv_hmchitiet2,tv_hmchitiet3,tv_tenhinh;
    //Button btnOk;
    Cursor cur_getdata;
    private String myVariable;
    String result;
    // Constructor không đối số bắt buộc
    public Thongtin_Dialog() {
    }
    // Phương thức để thiết lập giá trị biến
    public void setMyVariable(String variable) {
        myVariable = variable;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mt01_thu_vien_anh_info_dialog, container, false);

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
    //    void onDialogResult(String result);
    //}

    //OnDialogResultListener callback;

    private void addControl(View view) {
        tv_ngay = view.findViewById(R.id.tv_ngay);
        tv_bophan= view.findViewById(R.id.tv_bophan);
        tv_maH = view.findViewById(R.id.tv_maH);
        tv_hmlon = view.findViewById(R.id.tv_hmlon);
        tv_hmchitiet1 = view.findViewById(R.id.tv_hmchitiet1);
        tv_hmchitiet2 = view.findViewById(R.id.tv_hmchitiet2);
        tv_hmchitiet3 = view.findViewById(R.id.tv_hmchitiet3);
        tv_tenhinh = view.findViewById(R.id.tv_tenhinh);
        //btnOk = view.findViewById(R.id.btnOk);

        cur_getdata =Cre_db.get_ImageInfo(myVariable);
        cur_getdata.moveToFirst();
        String g_tc_fcb004= null,g_tc_fcb005=null,g_tc_fcc005=null,g_tc_fcc006=null,g_tc_fcc007=null,g_tc_fcf002=null,
                g_tc_fcd003=null,g_tc_fcd004=null,g_tc_fcd005=null,g_tc_fcf005=null,g_tc_fcq001=null,g_cpf02=null,g_ta_cpf001=null;
        for (int i = 0; i < cur_getdata.getCount(); i++) {
            g_tc_fcb004 = cur_getdata.getString(cur_getdata.getColumnIndexOrThrow("tc_fcb004"));
            g_tc_fcb005 = cur_getdata.getString(cur_getdata.getColumnIndexOrThrow("tc_fcb005"));
            g_tc_fcc005 = cur_getdata.getString(cur_getdata.getColumnIndexOrThrow("tc_fcc005"));
            g_tc_fcc006 = cur_getdata.getString(cur_getdata.getColumnIndexOrThrow("tc_fcc006"));
            g_tc_fcc007 = cur_getdata.getString(cur_getdata.getColumnIndexOrThrow("tc_fcc007"));
            g_tc_fcd003 = cur_getdata.getString(cur_getdata.getColumnIndexOrThrow("tc_fcd003"));
            g_tc_fcd004 = cur_getdata.getString(cur_getdata.getColumnIndexOrThrow("tc_fcd004"));
            g_tc_fcd005 = cur_getdata.getString(cur_getdata.getColumnIndexOrThrow("tc_fcd005"));
            g_tc_fcf002 = cur_getdata.getString(cur_getdata.getColumnIndexOrThrow("tc_fcf002"));
            g_tc_fcf005 = cur_getdata.getString(cur_getdata.getColumnIndexOrThrow("tc_fcf005"));
            g_tc_fcq001 = cur_getdata.getString(cur_getdata.getColumnIndexOrThrow("tc_fcq001"));
            g_cpf02 = cur_getdata.getString(cur_getdata.getColumnIndexOrThrow("cpf02"));
            g_ta_cpf001 = cur_getdata.getString(cur_getdata.getColumnIndexOrThrow("ta_cpf001"));
            cur_getdata.moveToNext();
        }
        if(g_tc_fcd005.equals("null"))
        {
            g_tc_fcd005 = "";
        }
        String g_bophan = g_tc_fcd003 + " " + g_tc_fcd004 + " " + g_tc_fcd005;
        String g_hmlon = g_tc_fcb004 + " " + g_tc_fcb005;
        String g_maH = g_tc_fcq001 + " " + g_cpf02 + " " + g_ta_cpf001;
        tv_ngay.setText(g_tc_fcf002);
        tv_bophan.setText(g_bophan);
        tv_hmlon.setText(g_hmlon);
        tv_hmchitiet1.setText(g_tc_fcc005);
        tv_hmchitiet2.setText(g_tc_fcc006);
        tv_hmchitiet3.setText(g_tc_fcc007);
        tv_tenhinh.setText(g_tc_fcf005);
        tv_maH.setText(g_maH);


    }
}

