package com.lelong.moitruong.MT01;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lelong.moitruong.Constant_Class;
import com.lelong.moitruong.Create_Table;
import com.lelong.moitruong.CursorToJsonConverter;

import org.json.JSONArray;
import org.json.JSONException;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KiemTraActivity_Transfer {
    private Create_Table Cre_db = null;
    private TransferPhoto transferPhoto = null;
    MT01_Interface apiService;
    private Context context;
    private TransferDialog transferDialog;
    private final String input_bdate;
    private final String input_edate;
    private final String input_factory;
    private final String input_department;

    public KiemTraActivity_Transfer(Context context, TransferDialog transferDialog, String input_bdate, String input_edate, String input_factory, String input_department) {
        this.context = context;
        this.transferDialog = transferDialog;
        this.input_bdate = input_bdate;
        this.input_edate = input_edate;
        this.input_factory = input_factory;
        this.input_department = input_department;

        Cre_db = new Create_Table(context);
        Cre_db.open();

        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.16.40.20/" + Constant_Class.server + "/HSEAPP/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = retrofit.create(MT01_Interface.class);

        Call_transfer();
    }

    private void Call_transfer() {
        //Khi sử dụng Retrofit cần sử dụng thư viện Json của Google , không nên dùng thư viện Json của Java
        Cursor c_getTc_fce = Cre_db.getTc_fce_Upload(input_bdate, input_edate, input_department); //Hạng mục vi phạm
        Cursor c_getTc_fcf = Cre_db.getTc_fcf_Upload(input_bdate, input_edate, input_department); //Ảnh hạng mục vi phạm

        JsonArray jarray_tc_fce = CursorToJsonConverter.cursorToJson(c_getTc_fce);
        JsonArray jarray_tc_fcf = CursorToJsonConverter.cursorToJson(c_getTc_fcf);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("jarr_tc_fce", jarray_tc_fce);
        jsonObject.add("jarr_tc_fcf", jarray_tc_fcf);

        Call<ResponseBody> call = apiService.sendDataToServer(jsonObject);

        call.enqueue(new Callback<ResponseBody>() {
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

                    if (status.equals("success")) {
                        //Hàm lấy ảnh và gửi ảnh
                        transferPhoto = new TransferPhoto(context,c_getTc_fcf);

                    } else {
                        Toast.makeText(context, "Lỗi : " + message, Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Xử lý lỗi
                    String s = String.valueOf(response.body());
                    Toast.makeText(context, "Lỗi : " + s, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Xử lý khi có lỗi xảy ra trong quá trình gửi dữ liệu
                String s = String.valueOf(t.toString());
                Toast.makeText(context, "Lỗi : " + s, Toast.LENGTH_SHORT).show();
            }
        });

    }

}
