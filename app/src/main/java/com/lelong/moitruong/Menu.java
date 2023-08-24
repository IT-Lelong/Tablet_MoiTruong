package com.lelong.moitruong;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lelong.moitruong.MT01.KiemTraActivity;
import com.lelong.moitruong.MT01.LoginDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Menu extends AppCompatActivity implements Call_interface {
    private CheckAppUpdate checkAppUpdate = null;
    private Create_Table Cre_db = null;

    Button btn_MT01, btn_MT02, btn_MT03, btn_MT04;
    TextView menuID;
    String ID;
    Locale locale;
    ActionBar actionBar;
    SimpleDateFormat dateFormat;

    Dialog dialog;
    TextView tv_syncName;
    ProgressBar progressBar;
    private String[] tasks = {"tc_fca", "tc_fcb", "tc_fcc", "tc_fcd"};
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setLanguage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        actionBar = getSupportActionBar();
        //actionBar.hide();

        Bundle getbundle = getIntent().getExtras();
        ID = getbundle.getString("ID");
        menuID = (TextView) findViewById(R.id.menuID);
        new IDname().execute("http://172.16.40.20/" + Constant_Class.server + "/");
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        Cre_db = new Create_Table(this);
        Cre_db.open();
        Cre_db.openTable();

        btn_MT01 = findViewById(R.id.btn_MT01);
        btn_MT01.setOnClickListener(btnlistener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAppUpdate = new CheckAppUpdate(this);
        checkAppUpdate.checkVersion();
    }

    //取得登入者姓名
    private class IDname extends AsyncTask<String, Integer, String> {
        String result = "";

        @Override
        protected String doInBackground(String... params) {
            try {
                String baseUrl = params[0];
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                Call<List<JsonObject>> call = apiInterface.getData(baseUrl + "getidJson.php?ID=" + ID);
                Response<List<JsonObject>> response = call.execute();

                if (response.isSuccessful()) {
                    List<JsonObject> jsonObjects = response.body();
                    if (jsonObjects != null && jsonObjects.size() > 0) {
                        JsonObject jsonObject = jsonObjects.get(0);
                        result = jsonObject.toString(); // Convert JsonObject to a string
                    } else {
                        result = "FALSE";
                    }
                } else {
                    result = "FALSE";
                }
            } catch (Exception e) {
                result = "FALSE";
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        protected void onPostExecute(String result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!result.equals("FALSE")) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            menuID.setText(ID + " " + jsonObject.getString("TA_CPF001") + "\n" + jsonObject.getString("GEM02"));
                            Constant_Class.UserID = ID;
                            Constant_Class.UserName_zh = jsonObject.getString("CPF02");
                            Constant_Class.UserName_vn = jsonObject.getString("TA_CPF001");
                            Constant_Class.UserDepID = jsonObject.getString("CPF29");
                            Constant_Class.UserDepName = jsonObject.getString("GEM02");
                            Constant_Class.UserFactory = jsonObject.getString("CPF281");
                        } catch (JSONException e) {
                            Toast alert = Toast.makeText(Menu.this, e.toString(), Toast.LENGTH_LONG);
                            alert.show();
                        }
                    }
                }
            });
        }
    }

    //Khởi tạo menu trên thanh tiêu đề (S)
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_opt, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String g_status;
        switch (item.getItemId()) {
            case R.id.refresh_datatable:
                Cre_db.delete_table();
                dialog = new Dialog(this);
                dialog.setContentView(R.layout.data_sync_layout);
                tv_syncName = dialog.findViewById(R.id.tv_syncName);
                progressBar = dialog.findViewById(R.id.impotDataProgressBar);

                tv_syncName.setText("");
                currentIndex = 0;
                runNextTask();
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void runNextTask() {
        if (currentIndex < tasks.length) {
            tv_syncName.setText(tasks[currentIndex]);
            Import_Data(tasks[currentIndex]);
        } else {
            Toast.makeText(this, "Cập nhật hoàn tất", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }


    private void Import_Data(String g_table) {
        String baseUrl = "http://172.16.40.20/" + Constant_Class.server + "/HSEAPP/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<JsonArray> call = apiInterface.getDataTable(baseUrl + "getData.php?item=" + g_table);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    JsonArray jsonArray = response.body();
                    //Cre_db.insertData(g_table,jsonArray);
                    Cre_db.setInsertCallback(Menu.this);
                    Create_Table.InsertDataTask insertDataTask = Cre_db.new InsertDataTask(progressBar);
                    insertDataTask.execute(g_table, String.valueOf(jsonArray));
                } else {
                    // Xử lý trường hợp response không thành công
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                // Xử lý trường hợp lỗi
            }
        });
    }

    @Override
    public void ImportData_onInsertComplete(String status) {
        if (status.equals("OK")) {
            currentIndex++;
            runNextTask();
        }
    }
    //Khởi tạo menu trên thanh tiêu đề (E)

    private void setLanguage() {
        SharedPreferences preferences = getSharedPreferences("Language", Context.MODE_PRIVATE);
        int language = preferences.getInt("Language", 0);
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        switch (language) {
            case 0:
                configuration.setLocale(Locale.TRADITIONAL_CHINESE);
                break;
            case 1:
                locale = new Locale("vi");
                Locale.setDefault(locale);
                configuration.setLocale(locale);
                break;
        }
        resources.updateConfiguration(configuration, displayMetrics);

    }

    private Button.OnClickListener btnlistener = new Button.OnClickListener() {
        public void onClick(View v) {
            //利用switch case方法，之後新增按鈕只需新增case即可
            switch (v.getId()) {

                case R.id.btn_MT01: {
                    //LoginDialogFragment dialogFragment = new LoginDialogFragment();
                    //dialogFragment.show(getSupportFragmentManager(), "MyDialogFragment");
                    Intent mt01_intent = new Intent();
                    mt01_intent.setClass(Menu.this, KiemTraActivity.class);
                    startActivity(mt01_intent);
                    break;
                }

                /*case R.id.btn_MT02: {
                    Intent QR010 = new Intent();
                    QR010.setClass(Menu.this, MT02_activity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("ID", ID);
                    bundle.putString("SERVER", g_server);
                    QR010.putExtras(bundle);
                    startActivity(QR010);
                    break;
                }*/

            }
        }
    };
}