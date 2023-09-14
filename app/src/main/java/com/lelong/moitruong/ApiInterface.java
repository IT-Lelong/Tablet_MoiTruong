package com.lelong.moitruong;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiInterface {
    //Lấy thông tin nhân viên
    @GET
    Call<List<JsonObject>> getData(@Url String url);

    //Lấy dữ liệu cơ bản của các table
    @GET
    Call<JsonArray> getDataTable(@Url String url);
}


