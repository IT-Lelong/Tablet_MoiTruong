package com.lelong.moitruong;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Create_Table {
    private Call_interface callInterface;
    private Context mCtx = null;
    String DATABASE_NAME = "HSEDB.db";
    public SQLiteDatabase db = null;

    String TABLE_NAME_TC_FCA = "tc_fca_file"; //TB Mã biểu kiểm tra
    String TABLE_NAME_TC_FCB = "tc_fcb_file"; //TB Mã hạng mục kiểm tra (tên Tab)
    String TABLE_NAME_TC_FCC = "tc_fcc_file"; //TB Mã hạng mục chi tiết (hạng mục con)
    String TABLE_NAME_TC_FCD = "tc_fcd_file"; //TB Mã bộ phận

    String CREATE_TABLE_TC_FCA = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TC_FCA + " (tc_fca001 TEXT, tc_fca002 TEXT, tc_fca003 TEXT, tc_fca004 TEXT )";
    String CREATE_TABLE_TC_FCB = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TC_FCB + " (tc_fcb001 TEXT, tc_fcb002 TEXT, tc_fcb003 TEXT, tc_fcb004 TEXT, tc_fcb005 TEXT )";
    String CREATE_TABLE_TC_FCC = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TC_FCC + " (tc_fcc001 TEXT, tc_fcc002 TEXT, tc_fcc003 TEXT, tc_fcc004 TEXT, tc_fcc005 TEXT, tc_fcc006 TEXT, tc_fcc007 TEXT, tc_fcc008 TEXT )";
    String CREATE_TABLE_TC_FCD = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TC_FCD + " (tc_fcd001 TEXT, tc_fcd002 TEXT, tc_fcd003 TEXT, tc_fcd004 TEXT, tc_fcd005 TEXT, tc_fcd006 TEXT )";

    public void setInsertCallback(Call_interface callback) {
        this.callInterface = callback;
    }

    public Create_Table(Context ctx) {
        this.mCtx = ctx;
    }

    public void open() throws SQLException {
        db = mCtx.openOrCreateDatabase(DATABASE_NAME, 0, null);
    }

    public void openTable() {
        try {
            db.execSQL(CREATE_TABLE_TC_FCA);
            db.execSQL(CREATE_TABLE_TC_FCB);
            db.execSQL(CREATE_TABLE_TC_FCC);
            db.execSQL(CREATE_TABLE_TC_FCD);
        } catch (Exception e) {

        }
    }

    public void close() {
        try {
            String DROP_TABLE_NAME_TC_FCA = "DROP TABLE IF EXISTS " + TABLE_NAME_TC_FCA;
            String DROP_TABLE_NAME_TC_FCB = "DROP TABLE IF EXISTS " + TABLE_NAME_TC_FCB;
            String DROP_TABLE_NAME_TC_FCC = "DROP TABLE IF EXISTS " + TABLE_NAME_TC_FCC;
            String DROP_TABLE_NAME_TC_FCD = "DROP TABLE IF EXISTS " + TABLE_NAME_TC_FCD;
            db.execSQL(DROP_TABLE_NAME_TC_FCA);
            db.execSQL(DROP_TABLE_NAME_TC_FCB);
            db.execSQL(DROP_TABLE_NAME_TC_FCC);
            db.execSQL(DROP_TABLE_NAME_TC_FCD);
            db.close();
        } catch (Exception e) {
        }
    }

    public void delete_table() {
        db.delete(TABLE_NAME_TC_FCA, null, null);
        db.delete(TABLE_NAME_TC_FCB, null, null);
        db.delete(TABLE_NAME_TC_FCC, null, null);
        db.delete(TABLE_NAME_TC_FCD, null, null);
    }

    public Cursor getdata_tc_fcd(String g_factory) {
        String g_dk;
        if (g_factory == "DH") {
            g_dk = "1','2','3";
        }else {
            g_dk = "4";
        }

        String selectQuery = "SELECT * FROM tc_fcd_file " +
                " WHERE tc_fcd001 in ('" + g_dk + "') " +
                " ORDER BY tc_fcd001,tc_fcd002 ";

        return db.rawQuery(selectQuery, null);
    }

    public class InsertDataTask extends AsyncTask<String, Integer, Integer> {
        int progress;
        private ProgressBar progressBar;

        public InsertDataTask(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        protected Integer doInBackground(String... params) {
            String g_table = params[0];
            String jsonData = params[1];
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                int totalItems = jsonArray.length();
                for (int i = 0; i < totalItems; i++) {
                    JSONObject jsonObject;
                    // Thực hiện insert dữ liệu từ jsonObject
                    switch (g_table) {
                        case "tc_fca": {
                            try {
                                jsonObject = jsonArray.getJSONObject(i);
                                String g_tc_fca001 = jsonObject.getString("TC_FCA001");
                                String g_tc_fca002 = jsonObject.getString("TC_FCA002");
                                String g_tc_fca003 = jsonObject.getString("TC_FCA003");
                                String g_tc_fca004 = jsonObject.getString("TC_FCA004");

                                insert_fca(g_tc_fca001, g_tc_fca002, g_tc_fca003, g_tc_fca004);

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        }

                        case "tc_fcb": {
                            try {
                                jsonObject = jsonArray.getJSONObject(i);
                                String g_tc_fcb001 = jsonObject.getString("TC_FCB001");
                                String g_tc_fcb002 = jsonObject.getString("TC_FCB002");
                                String g_tc_fcb003 = jsonObject.getString("TC_FCB003");
                                String g_tc_fcb004 = jsonObject.getString("TC_FCB004");
                                String g_tc_fcb005 = jsonObject.getString("TC_FCB005");

                                insert_fcb(g_tc_fcb001, g_tc_fcb002, g_tc_fcb003, g_tc_fcb004, g_tc_fcb005);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        }

                        case "tc_fcc": {
                            try {
                                jsonObject = jsonArray.getJSONObject(i);
                                String g_tc_fcc001 = jsonObject.getString("TC_FCC001");
                                String g_tc_fcc002 = jsonObject.getString("TC_FCC002");
                                String g_tc_fcc003 = jsonObject.getString("TC_FCC003");
                                String g_tc_fcc004 = jsonObject.getString("TC_FCC004");
                                String g_tc_fcc005 = jsonObject.getString("TC_FCC005");
                                String g_tc_fcc006 = jsonObject.getString("TC_FCC006");
                                String g_tc_fcc007 = jsonObject.getString("TC_FCC007");
                                String g_tc_fcc008 = jsonObject.getString("TC_FCC008");

                                insert_fcc(g_tc_fcc001, g_tc_fcc002, g_tc_fcc003, g_tc_fcc004, g_tc_fcc005, g_tc_fcc006, g_tc_fcc007, g_tc_fcc008);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        }

                        case "tc_fcd": {
                            try {
                                jsonObject = jsonArray.getJSONObject(i);
                                String g_tc_fcd001 = jsonObject.getString("TC_FCD001");
                                String g_tc_fcd002 = jsonObject.getString("TC_FCD002");
                                String g_tc_fcd003 = jsonObject.getString("TC_FCD003");
                                String g_tc_fcd004 = jsonObject.getString("TC_FCD004");
                                String g_tc_fcd005 = jsonObject.getString("TC_FCD005");
                                String g_tc_fcd006 = jsonObject.getString("TC_FCD006");

                                insert_fcd(g_tc_fcd001, g_tc_fcd002, g_tc_fcd003, g_tc_fcd004, g_tc_fcd005, g_tc_fcd006);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        }
                    }
                    // Cập nhật tiến độ
                    progress = (int) (((i + 1) / (float) totalItems) * 100);
                    publishProgress(progress);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return progress;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];
            // Cập nhật tiến độ insert dữ liệu trên giao diện
            progressBar.setProgress(progress); // Cập nhật tiến trình trên ProgressBar
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Hoàn thành quá trình insert
            if (result == 100) {
                callInterface.ImportData_onInsertComplete("OK");
            }
        }


    }

    private void insert_fcd(String g_tc_fcd001, String g_tc_fcd002, String g_tc_fcd003,
                            String g_tc_fcd004, String g_tc_fcd005, String g_tc_fcd006) {
        try {
            ContentValues args = new ContentValues();
            args.put("tc_fcd001", g_tc_fcd001);
            args.put("tc_fcd002", g_tc_fcd002);
            args.put("tc_fcd003", g_tc_fcd003);
            args.put("tc_fcd004", g_tc_fcd004);
            args.put("tc_fcd005", g_tc_fcd005);
            args.put("tc_fcd006", g_tc_fcd006);
            db.insert(TABLE_NAME_TC_FCD, null, args);
        } catch (Exception e) {
        }
    }

    private void insert_fcc(String g_tc_fcc001, String g_tc_fcc002, String g_tc_fcc003, String g_tc_fcc004,
                            String g_tc_fcc005, String g_tc_fcc006, String g_tc_fcc007, String g_tc_fcc008) {
        try {
            ContentValues args = new ContentValues();
            args.put("tc_fcc001", g_tc_fcc001);
            args.put("tc_fcc002", g_tc_fcc002);
            args.put("tc_fcc003", g_tc_fcc003);
            args.put("tc_fcc004", g_tc_fcc004);
            args.put("tc_fcc005", g_tc_fcc005);
            args.put("tc_fcc006", g_tc_fcc006);
            args.put("tc_fcc007", g_tc_fcc007);
            args.put("tc_fcc008", g_tc_fcc008);
            db.insert(TABLE_NAME_TC_FCC, null, args);
        } catch (Exception e) {
        }
    }

    private void insert_fcb(String g_tc_fcb001, String g_tc_fcb002, String g_tc_fcb003, String g_tc_fcb004, String g_tc_fcb005) {
        try {
            ContentValues args = new ContentValues();
            args.put("tc_fcb001", g_tc_fcb001);
            args.put("tc_fcb002", g_tc_fcb002);
            args.put("tc_fcb003", g_tc_fcb003);
            args.put("tc_fcb004", g_tc_fcb004);
            args.put("tc_fcb005", g_tc_fcb005);
            db.insert(TABLE_NAME_TC_FCB, null, args);
        } catch (Exception e) {
        }
    }

    private void insert_fca(String g_tc_fca001, String g_tc_fca002, String g_tc_fca003, String g_tc_fca004) {
        try {
            ContentValues args = new ContentValues();
            args.put("tc_fca001", g_tc_fca001);
            args.put("tc_fca002", g_tc_fca002);
            args.put("tc_fca003", g_tc_fca003);
            args.put("tc_fca004", g_tc_fca004);
            db.insert(TABLE_NAME_TC_FCA, null, args);
        } catch (Exception e) {
        }
    }

    /*KT01*/
    /*public Cursor getAll_tc_fab() {
        Cursor a;
        try {
           //a  = db.query(TABLE_NAME_TC_FAB, new String[]{"rowid _id", tc_fab001,tc_fab002,tc_fab003,tc_fab004}
           //        , null, null, null, null, tc_fab002 + " DESC", null) ;
            // a = db.rawQuery("SELECT * FROM " + TABLE_NAME_TC_FAB + " WHERE tc_fab001='KT01'", null);

            //SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_NAME_TC_FAB + " WHERE tc_fab001='KT01'";
            return db.rawQuery(selectQuery, null);

        } catch (Exception e) {
            return null;
        }
    }*/
}
