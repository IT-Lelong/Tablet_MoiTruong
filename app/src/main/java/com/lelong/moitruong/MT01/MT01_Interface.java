package com.lelong.moitruong.MT01;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface MT01_Interface {
    //Gửi dữ liệu kim tra đến Oracle MT01
    @POST("uploadDatatToOracle.php") // Thay thế bằng đường dẫn API của bạn
    Call<ResponseBody> sendDataToServer(@Body JsonObject jsonObject);

    @Multipart
    @POST("uploadPhoto.php")
    Call<ResponseBody> uploadImage(
            @Part MultipartBody.Part image,  // Một phần của dữ liệu là tệp ảnh
            @Part("description") RequestBody description  // Phần khác của dữ liệu (nếu có)
    );

}
