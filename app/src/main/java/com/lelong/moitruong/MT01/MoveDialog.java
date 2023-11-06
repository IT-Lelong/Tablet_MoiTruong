package com.lelong.moitruong.MT01;


import static android.content.Intent.getIntent;
import static android.content.Intent.getIntentOld;

import android.app.Activity;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MoveDialog extends DialogFragment {
    private static final int REQUEST_IMAGE_CAPTURE = 99;
    private Create_Table Cre_db = null;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Spinner hm_kiemtra, hm_con,sp_bophan;
    Button btnOk, btnCancel;
    Cursor cur_getbophan;
    Cursor g_dataimage;
    private String myVariable;
    private String myfactory;
    String result;
    String g_hmlon,g_hmct,g_bp;
    // Constructor không đối số bắt buộc
    public MoveDialog() {
    }
    // Phương thức để thiết lập giá trị biến
    public void setMyVariable(String variable,String g_factory) {
        myVariable = variable;
        myfactory = g_factory;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mt01_move_dialog, container, false);

        // Áp dụng animation vào dialog
        Animation scaleAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up);
        view.startAnimation(scaleAnimation);

        Cre_db = new Create_Table(getContext());
        Cre_db.open();
        addControl(view);

        return view;
    }
    // Định nghĩa một Interface callback
    public interface OnDialogResultListener {
        void onDialogResult(String result);
    }

    OnDialogResultListener callback;

    private void addControl(View view) {
        hm_kiemtra = view.findViewById(R.id.hm_kiemtra);
        hm_con= view.findViewById(R.id.hm_con);
        sp_bophan = view.findViewById(R.id.sp_bophan);
        btnOk = view.findViewById(R.id.btnOk);
        btnCancel = view.findViewById(R.id.btnCancel);
        g_dataimage = Cre_db.getDataImage(myVariable);
        if (g_dataimage.getCount() > 0) {
            g_dataimage.moveToFirst();
            for (int i = 0; i < g_dataimage.getCount(); i++) {
                g_hmlon = g_dataimage.getString(g_dataimage.getColumnIndexOrThrow("tc_fcb005"));
                g_hmct = g_dataimage.getString(g_dataimage.getColumnIndexOrThrow("tc_fcc007"));
                String g_bp1 = g_dataimage.getString(g_dataimage.getColumnIndexOrThrow("tc_fcd004"));
                String g_bp2 = g_dataimage.getString(g_dataimage.getColumnIndexOrThrow("tc_fcd005"));
                g_bp = g_bp1 + " - " + g_bp2;
                g_dataimage.moveToNext();
            }
        }
        List<String> kiemtra_List = new ArrayList<>();
        List<String> con_List = new ArrayList<>();
        List<String> bophan_List = new ArrayList<>();
        Cursor curs_hangmuclon = Cre_db.getHangMucLon();
        curs_hangmuclon.moveToFirst();
        for (int i = 0; i < curs_hangmuclon.getCount(); i++) {
            String data = curs_hangmuclon.getString(curs_hangmuclon.getColumnIndexOrThrow("tc_fcb005"));
            kiemtra_List.add(data);
            curs_hangmuclon.moveToNext();
        }
        //itemList.add("");
        //factory_List.add("Đức Hòa");
        //factory_List.add("Bến Lức");


        ArrayAdapter<String> kiemtra_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, kiemtra_List);
        ArrayAdapter<String> bophan_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, bophan_List);
        hm_kiemtra.setAdapter(kiemtra_adapter);
        for (int i = 0; i < kiemtra_List.size(); i++) {
            String item = (String) kiemtra_List.get(i);
            if (item.equals(g_hmlon)) {
                hm_kiemtra.setSelection(i);
                break;
            }
        }
        sp_bophan.setAdapter(bophan_adapter);
        String xuong= null;
        /*String g_user = Constant_Class.UserID;
        Cursor getXuong = Cre_db.getUserData(g_user);
        getXuong.moveToFirst();
        for (int i = 0; i < getXuong.getCount(); i++) {
            xuong = getXuong.getString(getXuong.getColumnIndexOrThrow("cpf281"));
            getXuong.moveToNext();
        }*/
        //cur_getdata = Cre_db.getdata_tc_fcd("DH");
        cur_getbophan = null;
        String g_x = null;
        if (myfactory.equals("Đức Hòa"))
        {
            g_x = "DH";
        }
        else{
            g_x = "BL";
        }
        cur_getbophan = Cre_db.getdata_tc_fcd(g_x);
        if (cur_getbophan.getCount() > 0) {
            cur_getbophan.moveToFirst();
            for (int i = 0; i < cur_getbophan.getCount(); i++) {
                String g_ten = cur_getbophan.getString(cur_getbophan.getColumnIndexOrThrow("tc_fcd004"));
                String g_xuong = cur_getbophan.getString(cur_getbophan.getColumnIndexOrThrow("tc_fcd005"));
                String materialInfo;
                if (g_xuong.equals("null"))
                {
                    materialInfo = g_ten ;
                }
                else{
                    materialInfo = g_ten + " - " + g_xuong;
                }
                bophan_List.add(materialInfo);
                cur_getbophan.moveToNext();
            }
            bophan_adapter.notifyDataSetChanged();
            for (int i = 0; i < bophan_List.size(); i++) {
                String item = (String) bophan_List.get(i);
                if (item.equals(g_bp)) {
                    sp_bophan.setSelection(i);
                    break;
                }
            }
        }
        hm_kiemtra.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                con_List.clear();
                String name_hm_kiemtra = kiemtra_List.get(position);
                int index = kiemtra_List.indexOf(kiemtra_List.get(position));
                Cursor curs_hangmuccon = Cre_db.get_hangmucchitiet(String.valueOf(index),"");
                curs_hangmuccon.moveToFirst();
                for (int i = 0; i < curs_hangmuccon.getCount(); i++) {
                    String data = curs_hangmuccon.getString(curs_hangmuccon.getColumnIndexOrThrow("tc_fcc007"));
                    con_List.add(data);
                    curs_hangmuccon.moveToNext();
                }
                ArrayAdapter<String> con_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, con_List);
                hm_con.setAdapter(con_adapter);
                for (int i = 0; i < con_List.size(); i++) {
                    String item = (String) con_List.get(i);
                    if (item.equals(g_hmct)) {
                        hm_con.setSelection(i);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String g_tc_fcc05=null;
                int g_bpPosition = sp_bophan.getSelectedItemPosition();
                int g_hmkiemtraPosition = hm_kiemtra.getSelectedItemPosition();
                String g_hmconPosition = String.valueOf(hm_con.getSelectedItemPosition());
                Cursor curs_data = Cre_db.get_hangmucchitiet(String.valueOf(g_hmkiemtraPosition),g_hmconPosition);
                curs_data.moveToFirst();
                for (int i = 0; i < curs_data.getCount(); i++) {
                    g_tc_fcc05 = curs_data.getString(curs_data.getColumnIndexOrThrow("tc_fcc005"));
                    curs_data.moveToNext();
                }
                cur_getbophan.moveToPosition(g_bpPosition);
                String g_tc_fcd006 = String.valueOf(cur_getbophan.getString(cur_getbophan.getColumnIndexOrThrow("tc_fcd006")));
                Cursor get_Thongtin_Anh =Cre_db.get_Thongtin_Anh(myVariable);
                get_Thongtin_Anh.moveToFirst();
                String g_tc_fcf001= null,g_tc_fcf002=null,g_tc_fcf003=null,g_tc_fcf004=null;
                for (int i = 0; i < get_Thongtin_Anh.getCount(); i++) {
                    g_tc_fcf001 = get_Thongtin_Anh.getString(get_Thongtin_Anh.getColumnIndexOrThrow("tc_fcf001"));
                    g_tc_fcf002 = get_Thongtin_Anh.getString(get_Thongtin_Anh.getColumnIndexOrThrow("tc_fcf002"));
                    g_tc_fcf003 = get_Thongtin_Anh.getString(get_Thongtin_Anh.getColumnIndexOrThrow("tc_fcf003"));
                    g_tc_fcf004 = get_Thongtin_Anh.getString(get_Thongtin_Anh.getColumnIndexOrThrow("tc_fcf004"));
                }
                //Cre_db.update_move(g_tc_fcc05, g_tc_fcf002, g_tc_fcd006, g_user);
                if (g_tc_fcf001 == g_tc_fcc05 && g_tc_fcf003 == g_tc_fcd006 )
                {
                    //return;
                }
                else{
                    String timestamp = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(new Date());
                    String fileName = g_tc_fcd006 + "_" + g_tc_fcc05 + "_" + g_tc_fcf004 + "_" + g_tc_fcf002 + "_" + timestamp + ".png";
                    Cre_db.update_MoveImage(myVariable, fileName, g_tc_fcc05, g_tc_fcd006);
                    Cre_db.update_movenewImage(g_tc_fcc05, g_tc_fcf002, g_tc_fcd006, g_tc_fcf004);
                    Cre_db.update_move(g_tc_fcf001, g_tc_fcf002, g_tc_fcf003, g_tc_fcf004);
                    String old_image_path = "/storage/emulated/0/Android/media/com.lelong.moitruong/" + g_tc_fcf002.replace("-", "") + "/" + myVariable;
                    boolean success = Rename_Image(old_image_path, fileName);
                    if (success) {
                        // Đổi tên thành công
                        result = "Y";
                        Toast.makeText(getContext(), "Chuyển đổi thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Đổi tên thất bại (có thể do không tìm thấy tệp hiện tại hoặc lỗi khác)
                        result = "N";
                        Toast.makeText(getContext(), "Chuyển đổi không thành công!", Toast.LENGTH_SHORT).show();
                    }
                }
                //Intent intent = new Intent();
                //intent.putExtra("result_key", result);
                //getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                if (callback != null) {
                    callback.onDialogResult(result);
                }
                dismiss();
                //Intent ImageDetailIntent = new Intent();
                //ImageDetailIntent.setClass(getContext(), ImageDetailActivity.class);
                //Bundle bundle = new Bundle();
                //bundle.putString("RESULT", result);
                //bundle.putString("DEPNO", g_maBP);
                //bundle.putString("DEPNAME", g_tenBP);
                //bundle.putString("DATE", g_ngay);
                //ImageDetailIntent.putExtras(bundle);
                //startActivity(ImageDetailIntent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public boolean Rename_Image(String old_imagepath,String new_image){
        File currentFile = new File(old_imagepath);
        // Kiểm tra xem tệp hiện tại có tồn tại không
        if (!currentFile.exists()) {
            return false; // Tệp không tồn tại, không thể đổi tên
        }

        // Tạo một đối tượng File cho tên mới và đường dẫn mới
        File newFile = new File(currentFile.getParent(), new_image);

        // Thực hiện đổi tên
        if (currentFile.renameTo(newFile)) {
            return true; // Đổi tên thành công
        } else {
            return false; // Đổi tên thất bại
        }
    }
}
