package com.lelong.moitruong.MT01;

import android.content.Context;
import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lelong.moitruong.Constant_Class;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransferPhoto {
    MT01_Interface apiService;
    private final Context context;
    private final Cursor c_getTc_fcf;

    public TransferPhoto(Context context, Cursor c_getTc_fcf) {
        this.context = context;
        this.c_getTc_fcf = c_getTc_fcf;

        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.16.40.20/" + Constant_Class.server + "/HSEAPP/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = retrofit.create(MT01_Interface.class);

        if (c_getTc_fcf.getCount() > 0) {
            Call_transPhoto();
        }
    }

    private void Call_transPhoto() {
        c_getTc_fcf.moveToFirst();
        //Open FOR
        for (int i = 0; i < c_getTc_fcf.getCount(); i++) {
            String image_name = c_getTc_fcf.getString(c_getTc_fcf.getColumnIndexOrThrow("tc_fcf005"));
            String image_date = c_getTc_fcf.getString(c_getTc_fcf.getColumnIndexOrThrow("tc_fcf002"));
            String image_path = "/storage/emulated/0/Android/media/com.lelong.moitruong/" + image_date.replace("-","") + "/" + image_name;
            File file = new File(image_path);  // Thay thế bằng đường dẫn thực tế của tệp ảnh
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);


            Call<ResponseBody> callImage = apiService.uploadImage(imagePart, null); // Thay thế descriptionRequestBody nếu bạn có dữ liệu khác
            callImage.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        // Xử lý phản hồi từ máy chủ

                        c_getTc_fcf.moveToNext();
                    } else {
                        // Xử lý lỗi


                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Xử lý khi có lỗi xảy ra trong quá trình gửi dữ liệu
                }
            });
        }
        // Close FOR
    }
}
