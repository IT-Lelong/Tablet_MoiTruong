package com.lelong.moitruong;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiInterface {
    @GET
    Call<List<JsonObject>> getData(@Url String url);

    @GET
    Call<JsonArray> getDataTable(@Url String url);

}


