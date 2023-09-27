package com.lelong.moitruong.MT01;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lelong.moitruong.Constant_Class;
import com.lelong.moitruong.Create_Table;
import com.lelong.moitruong.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HangMucKiemTra_Adapter extends RecyclerView.Adapter<HangMucKiemTra_Adapter.DataViewHolder> {
    private Create_Table Cre_db = null;
    private final Context context;
    private final int layout_resource;
    private final List<HangMucKiemTra_Model> hangmucChiTiet_list;
    private String g_ngay;
    private String g_maBP;
    DecimalFormat decimalFormat;
    //private OnCaptureImageClickListener captureImageClickListener;

    //public HangMucKiemTra_Adapter(Context context, int mt01_hang_muc_main_item, List<HangMucKiemTra_Model> hangmucChiTiet_list, String g_ngay, String g_maBP, OnCaptureImageClickListener listener) {
    public HangMucKiemTra_Adapter(Context context, int mt01_hang_muc_main_item, List<HangMucKiemTra_Model> hangmucChiTiet_list, String g_ngay, String g_maBP) {
        this.context = context;
        this.layout_resource = mt01_hang_muc_main_item;
        this.hangmucChiTiet_list = hangmucChiTiet_list;
        this.g_ngay = g_ngay;
        this.g_maBP = g_maBP;
        //this.captureImageClickListener = listener;

        String pattern = "#,###.##";
        decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
        decimalFormat.applyPattern(pattern);
        Cre_db = new Create_Table(context);
        Cre_db.open();
    }

    @NonNull
    @Override
    public HangMucKiemTra_Adapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layout_resource, parent, false);
        return new HangMucKiemTra_Adapter.DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HangMucKiemTra_Adapter.DataViewHolder holder, int position) {
        int adapterPosition = holder.getAdapterPosition();
        holder.tv_stt.setText(String.valueOf(adapterPosition + 1));
        holder.tv_thuyetminh.setText(hangmucChiTiet_list.get(adapterPosition).getG_tc_fcc007()); //Tên hạng mục chi tiết( tiếng việt)
        holder.tv_slAnhLoi.setText(hangmucChiTiet_list.get(adapterPosition).getG_tc_fce008()); //Số lượng hình chụp

        String g_tc_fce006 = hangmucChiTiet_list.get(adapterPosition).getG_tc_fce006(); //CheckBox2_Không Đạt
        if (g_tc_fce006.equals("false")) {
            holder.tv_trangthai.setText("NG");
            holder.tv_trangthai.setTextColor(Color.RED);
        } else {
            holder.tv_trangthai.setText("OK");
            holder.tv_trangthai.setTextColor(Color.BLACK);
        }
        String g_tc_fce007 = hangmucChiTiet_list.get(adapterPosition).getG_tc_fce007(); //Ghi chú
        holder.edt_ghichu.setText(Objects.requireNonNullElse(g_tc_fce007, ""));

        holder.edt_ghichu.addTextChangedListener(new TextWatcher() {
            private String originalText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Lưu trữ nội dung gốc của EditText trước khi thay đổi
                originalText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Được gọi khi có sự thay đổi trong EditText
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Được gọi sau khi người dùng đã nhập xong
                int adapterPosition = holder.getAdapterPosition();
                String inputData = s.toString();

                if (!inputData.equals(originalText)) {
                    // Được gọi sau khi người dùng đã nhập xong
                    String g_tc_fcc005 = hangmucChiTiet_list.get(adapterPosition).getG_tc_fcc005();
                    Cre_db.upd_GhiChu(g_ngay, g_maBP, g_tc_fcc005, Constant_Class.UserID, inputData);
                }
            }
        });

        holder.img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "PHOTO " + adapterPosition, Toast.LENGTH_SHORT).show();
                String g_tc_fcc005 = hangmucChiTiet_list.get(adapterPosition).getG_tc_fcc005();

                /*if (captureImageClickListener != null) {
                    captureImageClickListener.onCaptureImageClick(g_tc_fcc005);
                }*/

                Intent intent = new Intent(context, OpenCamera.class);
                intent.putExtra("ngay", g_ngay);
                intent.putExtra("bophan", g_maBP);
                intent.putExtra("hangmuc", g_tc_fcc005);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Thêm cờ vào Intent
                context.startActivity(intent);

                // Tạo tệp ảnh tạm thời với tên mới
                /*String timestamp = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(new Date());
                String fileName = g_tc_fcc005 + "_" + g_maBP + "_" + Constant_Class.UserID + "_" + g_ngay + "_" + timestamp + "";
                */

            }
        });

        holder.img_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "GALLERY " + adapterPosition, Toast.LENGTH_SHORT).show();
                String g_tc_fcc005 = hangmucChiTiet_list.get(adapterPosition).getG_tc_fcc005();

                Intent intent = new Intent(context, ThuVien_Anh.class);
                intent.putExtra("ngay", g_ngay);
                intent.putExtra("bophan", g_maBP);
                intent.putExtra("hangmuc", g_tc_fcc005);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Thêm cờ vào Intent
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hangmucChiTiet_list.size();
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        TextView tv_stt, tv_thuyetminh, tv_trangthai, tv_slAnhLoi;
        EditText edt_ghichu;
        ImageView img_camera, img_gallery;

        public DataViewHolder(View itemView) {
            super(itemView);

            tv_stt = itemView.findViewById(R.id.tv_stt);
            tv_thuyetminh = itemView.findViewById(R.id.tv_thuyetminh);
            tv_trangthai = itemView.findViewById(R.id.tv_trangthai);
            tv_slAnhLoi = itemView.findViewById(R.id.tv_slAnhLoi);
            edt_ghichu = itemView.findViewById(R.id.edt_ghichu);
            img_camera = itemView.findViewById(R.id.img_camera);
            img_gallery = itemView.findViewById(R.id.img_gallery);

            // set sự kiện onClick cho item
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.show();
                }
            });*/
        }
    }

    /*public interface OnCaptureImageClickListener {
        void onCaptureImageClick(String g_tc_fcc005);
    }*/

}
