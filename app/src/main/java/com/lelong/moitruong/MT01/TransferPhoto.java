package com.lelong.moitruong.MT01;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.lelong.moitruong.Constant_Class;
import com.lelong.moitruong.Create_Table;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
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
    Create_Table Cre_db;
    private final Context context;
    private final Cursor c_getTc_fcf;
    private TransferDialog transferDialog;

    public TransferPhoto(Context context, Cursor c_getTc_fcf, TransferDialog transferDialog) {
        this.context = context;
        this.c_getTc_fcf = c_getTc_fcf;
        this.transferDialog = transferDialog;

        Cre_db = new Create_Table(context);
        Cre_db.open();

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
        transferDialog.setProgressBar(c_getTc_fcf.getCount());
        // Sử dụng một danh sách các tệp tin cần tải lên
        List<FileInfo> filesToUpload = new ArrayList<>();
        for (int i = 0; i < c_getTc_fcf.getCount(); i++) {
            String image_no = c_getTc_fcf.getString(c_getTc_fcf.getColumnIndexOrThrow("tc_fcf001"));
            String image_date = c_getTc_fcf.getString(c_getTc_fcf.getColumnIndexOrThrow("tc_fcf002"));
            String image_dept = c_getTc_fcf.getString(c_getTc_fcf.getColumnIndexOrThrow("tc_fcf003"));
            String image_employ = c_getTc_fcf.getString(c_getTc_fcf.getColumnIndexOrThrow("tc_fcf004"));
            String image_name = c_getTc_fcf.getString(c_getTc_fcf.getColumnIndexOrThrow("tc_fcf005"));

            String image_path = "/storage/emulated/0/Android/media/com.lelong.moitruong/" + image_date.replace("-", "") + "/" + image_name;
            File file = new File(image_path);
            //filesToUpload.add(file);

            // Tạo một đối tượng FileInfo từ thông tin tên tệp và ngày
            FileInfo fileInfo = new FileInfo(image_no,image_date,image_dept,image_employ,image_name,file);

            // Thêm FileInfo vào danh sách
            filesToUpload.add(fileInfo);
            c_getTc_fcf.moveToNext();
        }

        // Bắt đầu quá trình tải lên bằng cách gọi hàm đệ quy
        uploadFileRecursive(filesToUpload, 0);

        /*
        //Open FOR
        for (int i = 0; i < c_getTc_fcf.getCount(); i++) {
            String image_name = c_getTc_fcf.getString(c_getTc_fcf.getColumnIndexOrThrow("tc_fcf005"));
            String image_date = c_getTc_fcf.getString(c_getTc_fcf.getColumnIndexOrThrow("tc_fcf002"));
            String image_path = "/storage/emulated/0/Android/media/com.lelong.moitruong/" + image_date.replace("-", "") + "/" + image_name;
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
        */
    }

    public class FileInfo {

        private final String image_no;
        private final String image_date;
        private final String image_dept;
        private final String image_employ;
        private final String image_name;
        private final File file;

        public String getImage_no() {
            return image_no;
        }

        public String getImage_date() {
            return image_date;
        }

        public String getImage_dept() {
            return image_dept;
        }

        public String getImage_employ() {
            return image_employ;
        }

        public String getImage_name() {
            return image_name;
        }

        public File getFilePath() {
            return file;
        }

        public FileInfo(String image_no, String image_date, String image_dept, String image_employ, String image_name, File file) {
            this.image_no = image_no;
            this.image_date = image_date;
            this.image_dept = image_dept;
            this.image_employ = image_employ;
            this.image_name = image_name;
            this.file = file;
        }

    }

    // Hàm đệ quy để tải lên từng tệp tin một
    void uploadFileRecursive(final List<FileInfo> files, final int currentIndex) {
        if (currentIndex >= files.size()) {
            transferDialog.setStatus("2");
            transferDialog.setEnableBtn(false,true);
            // Tất cả tệp tin đã được tải lên
            return;
        }

        FileInfo fileToUpload = files.get(currentIndex);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileToUpload.getFilePath());
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", fileToUpload.getImage_name(), requestFile);

        Call<ResponseBody> callImage = apiService.uploadImage(imagePart, null);
        callImage.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Xử lý kết quả ở đây
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
                        Cre_db.update_tc_fcfpost(fileToUpload.image_no,fileToUpload.image_date,fileToUpload.image_dept,fileToUpload.image_employ,fileToUpload.image_name);
                        transferDialog.updateProgressBar(currentIndex + 1);
                        // Gọi đệ quy để tải lên tệp tin tiếp theo
                        uploadFileRecursive(files, currentIndex + 1);
                    } else {
                        Toast.makeText(context, "Lỗi : " + message, Toast.LENGTH_LONG).show();
                    }


                } else {
                    // Xử lý lỗi ở đây
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Xử lý khi có lỗi xảy ra trong quá trình gửi dữ liệu

                // Gọi đệ quy để tải lên tệp tin tiếp theo
                uploadFileRecursive(files, currentIndex + 1);
            }
        });
    }
}
