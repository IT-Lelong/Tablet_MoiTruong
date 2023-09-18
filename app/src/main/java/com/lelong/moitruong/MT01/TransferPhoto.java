package com.lelong.moitruong.MT01;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.lelong.moitruong.Constant_Class;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
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

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS) // Ví dụ: timeout sau 60 giây
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.16.40.20/" + Constant_Class.server + "/HSEAPP/")
                .client(client)
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
            c_getTc_fcf.moveToNext();
            callImage.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        InputStream inputStream = response.body().byteStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder sb = new StringBuilder();
                        String line;

                        while (true) {
                            try {
                                if (!((line = reader.readLine()) != null)) break;
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            sb.append(line);
                        }

                        String responseData = sb.toString(); // Dữ liệu JSON
                        // Sử dụng Gson để phân tích dữ liệu JSON thành đối tượng
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(responseData, JsonObject.class);

                        // Trích xuất các trường từ JSON
                        String status = jsonObject.get("status").getAsString();
                        String message = jsonObject.get("message").getAsString();

                        if (status.equals("OK")) {
                            Toast.makeText(context, "Sucess : " + message, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Lỗi : " + message, Toast.LENGTH_LONG).show();
                        }
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
