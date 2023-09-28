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
    String TABLE_NAME_TC_FCE = "tc_fce_file"; //TB Dữ liệu đã kiểm tra
    String TABLE_NAME_TC_FCQ = "tc_fcq_file"; //TB Dữ liệu nhân viên kiểm 5S
    String TABLE_NAME_TC_FCF = "tc_fcf_file"; //TB Dữ liệu thông tin ảnh lỗi

    String CREATE_TABLE_TC_FCA = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TC_FCA + " (tc_fca001 TEXT, tc_fca002 TEXT, tc_fca003 TEXT, tc_fca004 TEXT )";
    String CREATE_TABLE_TC_FCB = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TC_FCB + " (tc_fcb001 TEXT, tc_fcb002 TEXT, tc_fcb003 TEXT, tc_fcb004 TEXT, tc_fcb005 TEXT )";
    String CREATE_TABLE_TC_FCC = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TC_FCC + " (tc_fcc001 TEXT, tc_fcc002 TEXT, tc_fcc003 TEXT, tc_fcc004 TEXT, tc_fcc005 TEXT, tc_fcc006 TEXT, tc_fcc007 TEXT, tc_fcc008 TEXT )";
    String CREATE_TABLE_TC_FCD = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TC_FCD + " (tc_fcd001 TEXT, tc_fcd002 TEXT, tc_fcd003 TEXT, tc_fcd004 TEXT, tc_fcd005 TEXT, tc_fcd006 TEXT )";
    String CREATE_TABLE_TC_FCE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TC_FCE + " (tc_fce001 TEXT, tc_fce002 TEXT, tc_fce003 TEXT, tc_fce004 TEXT, tc_fce005 TEXT, tc_fce006 TEXT, tc_fce007 TEXT, tc_fce008 TEXT , tc_fcepost TEXT )";
    String CREATE_TABLE_TC_FCQ = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TC_FCQ + " (tc_fcq001 TEXT, cpf02 TEXT, ta_cpf001 TEXT, cpf29 TEXT, gem02 TEXT, cpf281 TEXT)";
    String CREATE_TABLE_TC_FCF = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TC_FCF + " (tc_fcf001 TEXT, tc_fcf002 TEXT, tc_fcf003 TEXT, tc_fcf004 TEXT, tc_fcf005 TEXT, tc_fcfpost TEXT)";

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
            db.execSQL(CREATE_TABLE_TC_FCE);
            db.execSQL(CREATE_TABLE_TC_FCQ);
            db.execSQL(CREATE_TABLE_TC_FCF);
        } catch (Exception e) {

        }
    }

    public void close() {
        try {
            String DROP_TABLE_NAME_TC_FCA = "DROP TABLE IF EXISTS " + TABLE_NAME_TC_FCA;
            String DROP_TABLE_NAME_TC_FCB = "DROP TABLE IF EXISTS " + TABLE_NAME_TC_FCB;
            String DROP_TABLE_NAME_TC_FCC = "DROP TABLE IF EXISTS " + TABLE_NAME_TC_FCC;
            String DROP_TABLE_NAME_TC_FCD = "DROP TABLE IF EXISTS " + TABLE_NAME_TC_FCD;
            String DROP_TABLE_NAME_TC_FCE = "DROP TABLE IF EXISTS " + TABLE_NAME_TC_FCE;
            String DROP_TABLE_NAME_TC_FCQ = "DROP TABLE IF EXISTS " + TABLE_NAME_TC_FCQ;
            String DROP_TABLE_NAME_TC_FCF = "DROP TABLE IF EXISTS " + TABLE_NAME_TC_FCF;
            db.execSQL(DROP_TABLE_NAME_TC_FCA);
            db.execSQL(DROP_TABLE_NAME_TC_FCB);
            db.execSQL(DROP_TABLE_NAME_TC_FCC);
            db.execSQL(DROP_TABLE_NAME_TC_FCD);
            db.execSQL(DROP_TABLE_NAME_TC_FCE);
            db.execSQL(DROP_TABLE_NAME_TC_FCQ);
            db.execSQL(DROP_TABLE_NAME_TC_FCF);
            db.close();
        } catch (Exception e) {
        }
    }

    public void delete_table() {
        db.delete(TABLE_NAME_TC_FCA, null, null);
        db.delete(TABLE_NAME_TC_FCB, null, null);
        db.delete(TABLE_NAME_TC_FCC, null, null);
        db.delete(TABLE_NAME_TC_FCD, null, null);
        db.delete(TABLE_NAME_TC_FCQ, null, null);
    }

    public void delete_DataTable() {
        db.delete(TABLE_NAME_TC_FCE, null, null);
        db.delete(TABLE_NAME_TC_FCF, null, null);
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

                        case "tc_fcq": {
                            try {
                                jsonObject = jsonArray.getJSONObject(i);
                                String g_tc_fcq001 = jsonObject.getString("TC_FCQ001");
                                String g_cpf02 = jsonObject.getString("CPF02");
                                String g_ta_cpf001 = jsonObject.getString("TA_CPF001");
                                String g_cpf29 = jsonObject.getString("CPF29");
                                String g_gem02 = jsonObject.getString("GEM02");
                                String g_cpf281 = jsonObject.getString("CPF281");

                                insert_fcq(g_tc_fcq001, g_cpf02, g_ta_cpf001, g_cpf29, g_gem02, g_cpf281);

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

    public void call_insertPhotoData(String selectedDetail, String selectedDate, String selectedDepartment, String g_user, String fileName) {

        try {
            ContentValues args = new ContentValues();
            args.put("tc_fcf001", selectedDetail);
            args.put("tc_fcf002", selectedDate);
            args.put("tc_fcf003", selectedDepartment);
            args.put("tc_fcf004", g_user);
            args.put("tc_fcf005", fileName);
            args.put("tc_fcfpost", "N");
            db.insert(TABLE_NAME_TC_FCF, null, args);

            //Cập nhật table chứ dữ liệu đã kiểm tra
            call_update_tc_fce(selectedDetail, selectedDate, selectedDepartment, g_user);
        } catch (Exception e) {
        }
    }

    public void update_tc_fcfpost(String image_no, String image_date, String image_dept, String image_employ, String image_name) {
        try {
            db.execSQL(" UPDATE tc_fcf_file SET tc_fcfpost = 'Y' " +
                    " WHERE tc_fcf001 ='" + image_no + "' " +
                    " AND tc_fcf002 ='" + image_date + "'" +
                    " AND tc_fcf003 = '" + image_dept + "'" +
                    " AND tc_fcf004 = '" + image_employ + "' " +
                    " AND tc_fcf005 = '" + image_name + "' ");
        } catch (Exception e) {
        }
    }

    private void call_update_tc_fce(String selectedDetail, String selectedDate, String selectedDepartment, String g_user) {
        String g_tc_fce008;
        Cursor c = db.rawQuery(" SELECT tc_fce008 FROM tc_fce_file " +
                " WHERE tc_fce001 ='" + selectedDetail + "' " +
                " AND tc_fce002 ='" + selectedDate + "' " +
                " AND tc_fce003 ='" + g_user + "' " +
                " AND tc_fce004 = '" + selectedDepartment + "' ", null);
        ;
        if (c.moveToFirst()) {
            g_tc_fce008 = String.valueOf(Integer.parseInt(c.getString(0)) + 1);

            db.execSQL(" UPDATE tc_fce_file SET tc_fce008 = '" + g_tc_fce008 + "' , tc_fce006 = 'true' " +
                    " WHERE tc_fce001 ='" + selectedDetail + "' " +
                    " AND tc_fce002 ='" + selectedDate + "'" +
                    " AND tc_fce003 = '" + g_user + "'" +
                    " AND tc_fce004 = '" + selectedDepartment + "' ");
        } else {
            //'1' : Số lượng ảnh lỗi đầu tiên
            //'N' : Trạng thái chưa chuyển đến server
            db.execSQL(" INSERT INTO tc_fce_file VALUES('" + selectedDetail + "', '" + selectedDate + "', '" + g_user + "','" + selectedDepartment + "','false','true','','1','N')");
        }
        c.close();
    }

    private void insert_fcq(String g_tc_fcq001, String g_cpf02, String g_ta_cpf001, String g_cpf29, String g_gem02, String g_cpf281) {
        try {
            ContentValues args = new ContentValues();
            args.put("tc_fcq001", g_tc_fcq001);
            args.put("cpf02", g_cpf02);
            args.put("ta_cpf001", g_ta_cpf001);
            args.put("cpf29", g_cpf29);
            args.put("gem02", g_gem02);
            args.put("cpf281", g_cpf281);
            db.insert(TABLE_NAME_TC_FCQ, null, args);
        } catch (Exception e) {
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

    public Cursor getUserData(String g_UserID) {
        String selectQuery = "SELECT * FROM tc_fcq_file WHERE tc_fcq001 = '" + g_UserID + "' ";
        return db.rawQuery(selectQuery, null);
    }

    public int checkUserData(String g_UserID) {
        Cursor c = db.rawQuery("SELECT count(*) FROM tc_fcq_file WHERE tc_fcq001 = '" + g_UserID + "'", null);
        c.moveToFirst();
        Integer tcount = c.getInt(0);
        c.close();
        return tcount;
    }

    public Cursor getdata_tc_fcd(String g_factory) {
        String g_dk;
        if (g_factory.equals("DH")) {
            g_dk = "'01','02','03'";
        } else {
            g_dk = "'04'";
        }

        String selectQuery = "SELECT * FROM tc_fcd_file " +
                " WHERE tc_fcd001 in (" + g_dk + ") " +
                " ORDER BY tc_fcd001,tc_fcd002 ";

        return db.rawQuery(selectQuery, null);
    }

    public Cursor departmentCheckedData() {
        String selectQuery = " SELECT 0,tc_fcd004||' '||tc_fcd005 AS donvi ,tc_fcd003, SUM((CASE WHEN tc_fce006 = 'true' THEN 1 ELSE 0 end)) slerr  ,tc_fce002,tc_fce004 " +
                " FROM tc_fce_file,tc_fcd_file " +
                " WHERE tc_fcd006=tc_fce004 " +
                " GROUP BY  tc_fcd004||' '||tc_fcd005,tc_fcd003,tc_fce002,tc_fce004 " +
                " ORDER BY tc_fce002 DESC,tc_fce004,tc_fce003 ";

        return db.rawQuery(selectQuery, null);
    }

    public Cursor getHangMucLon() {
        String selectQuery = " SELECT tc_fcb003,tc_fcb004,tc_fcb005  FROM tc_fcb_file WHERE tc_fcb003 BETWEEN '01' AND '08' ORDER BY tc_fcb003 ";
        return db.rawQuery(selectQuery, null);
    }

    public Cursor getHangMucChiTiet(int g_position, String g_ngay, String g_maBP, String userID) {
        String g_hangmuc = String.format("%02d", g_position + 1);

        String selectQuery = " SELECT tc_fcc004, tc_fcc005, tc_fcc006, tc_fcc007, tc_fcc008, tc_fce007,  COALESCE(SUM(tc_fce008), 0 )  AS tc_fce008, " +
                "    CASE WHEN tc_fce006 = 'true' THEN 'false' ELSE 'true' END AS tc_fce006 " +
                " FROM    tc_fcc_file" +
                " LEFT JOIN    tc_fce_file ON tc_fcc005 = tc_fce001 AND tc_fce004 = '" + g_maBP + "' AND tc_fce002 = '" + g_ngay + "'  " +
                " WHERE    tc_fcc003 = '" + g_hangmuc + "'" +
                " GROUP BY tc_fcc004, tc_fcc005, tc_fcc006, tc_fcc007, tc_fcc008, tc_fce007 " +
                " ORDER BY  tc_fcc005";
        return db.rawQuery(selectQuery, null);
    }

    public void upd_GhiChu(String g_ngay, String g_maBP, String g_tc_fcc005, String g_User, String inputData) {
        Cursor c = db.rawQuery("SELECT count(*) FROM tc_fce_file " +
                " WHERE tc_fce001 = '" + g_tc_fcc005 + "' " +
                " AND tc_fce002 = '" + g_ngay + "' " +
                " AND tc_fce003 = '" + g_User + "' " +
                " AND tc_fce004 = '" + g_maBP + "' ", null);
        c.moveToFirst();
        Integer tcount = c.getInt(0);
        c.close();

        if (tcount == 0) {
            db.execSQL(" INSERT INTO tc_fce_file VALUES('" + g_tc_fcc005 + "', '" + g_ngay + "', '" + g_User + "','" + g_maBP + "','false','false','" + inputData + "','0')");
        } else {
            db.execSQL(" UPDATE tc_fce_file SET tc_fce007 = '" + inputData + "' " +
                    " WHERE tc_fce001 = '" + g_tc_fcc005 + "' " +
                    " AND tc_fce002 = '" + g_ngay + "' " +
                    " AND tc_fce003 = '" + g_User + "' " +
                    " AND tc_fce004 = '" + g_maBP + "' ");
        }
    }

    public Cursor getTc_fce_Upload(String input_bdate, String input_edate, String input_department) {
        //Lấy dữ liệu tc_fce_file update tới máy chủ Oracle
        String selectQuery = " SELECT * FROM tc_fce_file WHERE 1=1 ";
        if (input_bdate.isEmpty() && input_edate.isEmpty() && input_department.isEmpty()) {
            selectQuery += " AND tc_fcepost = 'N' ";
        }
        if (!input_department.isEmpty()) {
            selectQuery += " AND tc_fce004 = '" + input_department + "' ";
        }
        if (!input_bdate.isEmpty() && !input_edate.isEmpty()) {
            selectQuery += " AND tc_fce002 BETWEEN '" + input_bdate + "' AND '" + input_edate + "'";
        }
        selectQuery += " ORDER BY tc_fce002,tc_fce004,tc_fce001 ";

        return db.rawQuery(selectQuery, null);
    }

    public Cursor getTc_fcf_Upload(String input_bdate, String input_edate, String input_department) {
        //Lấy dữ liệu tc_fcf_file update tới máy chủ Oracle
        String selectQuery = " SELECT * FROM tc_fcf_file WHERE 1=1 ";
        if (input_bdate.isEmpty() && input_edate.isEmpty() && input_department.isEmpty()) {
            selectQuery += " AND tc_fcfpost = 'N' ";
        }
        if (!input_department.isEmpty()) {
            selectQuery += " AND tc_fcf003 = '" + input_department + "' ";
        }
        if (!input_bdate.isEmpty() && !input_edate.isEmpty()) {
            selectQuery += " AND tc_fcf002 BETWEEN '" + input_bdate + "' AND '" + input_edate + "'";
        }
        selectQuery += " ORDER BY tc_fcf002,tc_fcf003,tc_fcf001 ";

        return db.rawQuery(selectQuery, null);
    }

    public void delete_Image(String name) {
        try {
            db.execSQL("DELETE FROM tc_fcf_file WHERE tc_fcf005 = '" + name + "' ");
        } catch (Exception e) {
            String ex = e.getMessage().toString();
        }
    }

    public Cursor getGroup(String position,String date, String bophan, String hangmuc) {
        String selectQuery = " SELECT DISTINCT tc_fcf001,tc_fcf002,tc_fcf003,tc_fcd004,tc_fcd005,tc_fcc006,tc_fcc007 FROM tc_fcf_file,tc_fcd_file,tc_fcc_file WHERE tc_fcc005 = tc_fcf001 AND tc_fcd006 = tc_fcf003 ";
        if (date.isEmpty() && bophan.isEmpty() && hangmuc.isEmpty()) {
            //selectQuery += " AND tc_fcfpost = 'N' ";
        }
        if (!position.isEmpty()) {
            String g_position = String.format("%02d", Integer.parseInt(position) + 1);
            selectQuery += " AND tc_fcc003 = '" + g_position + "' ";
        }
        if (!bophan.isEmpty()) {
            selectQuery += " AND tc_fcf001 = '" + bophan + "' ";
        }
        if (!date.isEmpty()) {
            selectQuery += " AND tc_fcf002 = '" + date + "'";
        }
        if (!hangmuc.isEmpty()) {
            selectQuery += " AND tc_fcf003 = '" + hangmuc + "'";
        }
        selectQuery += " ORDER BY tc_fcf002,tc_fcf003,tc_fcf001 ";
        return db.rawQuery(selectQuery, null);
    }

    public Cursor getImage(String date, String bophan, String hangmuc) {
        String selectQuery = " SELECT tc_fcf005 FROM tc_fcf_file WHERE 1=1 ";
        if (date.isEmpty() && bophan.isEmpty() && hangmuc.isEmpty()) {
            //selectQuery += " AND tc_fcfpost = 'N' ";
        }
        if (!bophan.isEmpty()) {
            selectQuery += " AND tc_fcf001 = '" + bophan + "' ";
        }
        if (!date.isEmpty()) {
            selectQuery += " AND tc_fcf002 = '" + date + "'";
        }
        if (!hangmuc.isEmpty()) {
            selectQuery += " AND tc_fcf003 = '" + hangmuc + "'";
        }
        selectQuery += " ORDER BY tc_fcf005 ";
        return db.rawQuery(selectQuery, null);
    }

    public void update_imagecount(String selectedDetail, String selectedDate, String selectedDepartment, String g_user) {
        String g_tc_fce008;
        Cursor c = db.rawQuery(" SELECT tc_fce008 FROM tc_fce_file " +
                " WHERE tc_fce001 ='" + selectedDetail + "' " +
                " AND tc_fce002 ='" + selectedDate + "' " +
                " AND tc_fce003 ='" + g_user + "' " +
                " AND tc_fce004 = '" + selectedDepartment + "' ", null);
        c.moveToFirst();
        g_tc_fce008 = String.valueOf(Integer.parseInt(c.getString(0)) - 1);

        db.execSQL(" UPDATE tc_fce_file SET tc_fce008 = '" + g_tc_fce008 + "' " +
                " WHERE tc_fce001 ='" + selectedDetail + "' " +
                " AND tc_fce002 ='" + selectedDate + "'" +
                " AND tc_fce003 = '" + g_user + "'" +
                " AND tc_fce004 = '" + selectedDepartment + "' ");
        c.close();
    }

    public Cursor get_Thongtin_Anh(String name) {
        String selectQuery = " SELECT tc_fcf001,tc_fcf002,tc_fcf003,tc_fcf004 FROM tc_fcf_file WHERE 1=1 ";
        if (!name.isEmpty()) {
            selectQuery += " AND tc_fcf005 = '" + name + "' ";
        }
        selectQuery += " ORDER BY tc_fcf002,tc_fcf003,tc_fcf001 ";
        return db.rawQuery(selectQuery, null);
    }

    public Cursor get_hangmucchitiet(String g_positionlon, String g_positioncon) {
        String selectQuery = " SELECT tc_fcc005,tc_fcc006,tc_fcc007 FROM tc_fcc_file WHERE 1=1  ";
        if (!g_positionlon.isEmpty()) {
            String g_hangmuc = String.format("%02d", Integer.parseInt(g_positionlon) + 1);
            selectQuery += " AND tc_fcc003 = '" + g_hangmuc + "' ";
        }
        if (!g_positioncon.isEmpty()) {
            String g_hangmuccon = String.format("%02d", Integer.parseInt(g_positioncon) + 1);
            selectQuery += " AND tc_fcc004 = '" + g_hangmuccon + "' ";
        }
        selectQuery += " ORDER BY  tc_fcc005 ";
        return db.rawQuery(selectQuery, null);
    }

    public void update_MoveImage(String nameold, String namenew, String chitiet, String bophan) {
        db.execSQL("UPDATE tc_fcf_file SET tc_fcf001 = '" + chitiet + "' , tc_fcf003 = '" + bophan + "', " +
                " tc_fcf005 = '" + namenew + "' WHERE tc_fcf005='" + nameold + "' ");
    }

    public void update_move(String selectedDetail, String selectedDate, String selectedDepartment, String g_user) {
        String g_tc_fce008;
        Cursor c = db.rawQuery(" SELECT tc_fce008 FROM tc_fce_file " +
                " WHERE tc_fce001 ='" + selectedDetail + "' " +
                " AND tc_fce002 ='" + selectedDate + "' " +
                " AND tc_fce003 ='" + g_user + "' " +
                " AND tc_fce004 = '" + selectedDepartment + "' ", null);
        ;
        if (c.moveToFirst()) {
            g_tc_fce008 = String.valueOf(Integer.parseInt(c.getString(0)) - 1);
            if (Integer.parseInt(g_tc_fce008) == 0) {
                db.execSQL("DELETE FROM tc_fce_file " +
                        " WHERE tc_fce001 ='" + selectedDetail + "' " +
                        " AND tc_fce002 ='" + selectedDate + "'" +
                        " AND tc_fce003 = '" + g_user + "'" +
                        " AND tc_fce004 = '" + selectedDepartment + "' ");
            } else {
                db.execSQL(" UPDATE tc_fce_file SET tc_fce008 = '" + g_tc_fce008 + "' " +
                        " WHERE tc_fce001 ='" + selectedDetail + "' " +
                        " AND tc_fce002 ='" + selectedDate + "'" +
                        " AND tc_fce003 = '" + g_user + "'" +
                        " AND tc_fce004 = '" + selectedDepartment + "' ");
            }
        }
        c.close();
    }

    public void update_movenewImage(String selectedDetail, String selectedDate, String selectedDepartment, String g_user) {
        String g_tc_fce008;
        Cursor c = db.rawQuery(" SELECT tc_fce008 FROM tc_fce_file " +
                " WHERE tc_fce001 ='" + selectedDetail + "' " +
                " AND tc_fce002 ='" + selectedDate + "' " +
                " AND tc_fce003 ='" + g_user + "' " +
                " AND tc_fce004 = '" + selectedDepartment + "' ", null);
        ;
        if (c.moveToFirst()) {
            g_tc_fce008 = String.valueOf(Integer.parseInt(c.getString(0)) + 1);

            db.execSQL(" UPDATE tc_fce_file SET tc_fce008 = '" + g_tc_fce008 + "' , tc_fce006 = 'true' " +
                    " WHERE tc_fce001 ='" + selectedDetail + "' " +
                    " AND tc_fce002 ='" + selectedDate + "'" +
                    " AND tc_fce003 = '" + g_user + "'" +
                    " AND tc_fce004 = '" + selectedDepartment + "' ");
        } else {
            //'1' : Số lượng ảnh lỗi đầu tiên
            //'N' : Trạng thái chưa chuyển đến server
            db.execSQL(" INSERT INTO tc_fce_file VALUES('" + selectedDetail + "', '" + selectedDate + "', '" + g_user + "','" + selectedDepartment + "','false','true','','1','N')");
        }
        c.close();
    }

    public Cursor get_ImageInfo(String name) {
        String selectQuery = " SELECT tc_fcb004,tc_fcb005,tc_fcc005,tc_fcc006,tc_fcc007,tc_fcd003," +
                " tc_fcd004,tc_fcd005,tc_fcf002,tc_fcf005,tc_fcq001,cpf02,ta_cpf001  FROM tc_fcc_file,tc_fcf_file," +
                " tc_fcb_file,tc_fcd_file,tc_fcq_file  WHERE tc_fcc005=tc_fcf001 AND tc_fcb001=tc_fcc001" +
                " AND tc_fcd006 = tc_fcf003 AND tc_fcb002=tc_fcc002 AND tc_fcb003= tc_fcc003 AND tc_fcq001 = tc_fcf004 ";
        if (!name.isEmpty()) {
            selectQuery += " AND tc_fcf005='" + name + "' ";
        }
        selectQuery += " ORDER BY  tc_fcc005 ";
        return db.rawQuery(selectQuery, null);
    }

    public Cursor getChartCompleteData() {
        //select max(tc_fcd002) max_tc_fcd002  from tc_fcd_filemColumns = {String[1]@33400} ["g_count"]
        String selectQuery = " select tc_fce004, tc_fcd004,tc_fcd005, round((CAST(sum(tc_fce008) as REAL) / (select sum(tc_fce008) total from tc_fce_file where tc_fce002 = (select max(tc_fce002) from tc_fce_file)) ) * 100   ,2)  g_count " +
                " from tc_fce_file ,tc_fcd_file " +
                " where tc_fcd006= tc_fce004 and tc_fce002 = (select max(tc_fce002) from tc_fce_file)  group by tc_fce004  ";

        return db.rawQuery(selectQuery, null);
    }

    public void call_upd_tc_fcepost(Cursor c_getTc_fce) {
        if (c_getTc_fce.getCount() > 0) {
            c_getTc_fce.moveToFirst();
            for (int i = 0; i < c_getTc_fce.getCount(); i++) {
                String g_tc_fce001 = c_getTc_fce.getString(c_getTc_fce.getColumnIndexOrThrow("tc_fce001"));
                String g_tc_fce002 = c_getTc_fce.getString(c_getTc_fce.getColumnIndexOrThrow("tc_fce002"));
                String g_tc_fce003 = c_getTc_fce.getString(c_getTc_fce.getColumnIndexOrThrow("tc_fce003"));
                String g_tc_fce004 = c_getTc_fce.getString(c_getTc_fce.getColumnIndexOrThrow("tc_fce004"));

                db.execSQL(" UPDATE tc_fce_file SET tc_fcepost = 'Y' " +
                        " WHERE tc_fce001 ='" + g_tc_fce001 + "' " +
                        " AND tc_fce002 ='" + g_tc_fce002 + "'" +
                        " AND tc_fce003 = '" + g_tc_fce003 + "'" +
                        " AND tc_fce004 = '" + g_tc_fce004 + "' ");
                c_getTc_fce.moveToNext();
            }
        }
    }
    public Cursor getDepartment_today(String date) {
        String selectQuery = " select distinct tc_fce004,tc_fcd003,tc_fcd004,tc_fcd005 from tc_fce_file,tc_fcd_file  where tc_fcd006 = tc_fce004 ";
        if (!date.isEmpty()) {
            selectQuery += " and  tc_fce002= '" + date + "'";
        }
        selectQuery += " ORDER BY tc_fce004 ";
        return db.rawQuery(selectQuery, null);
    }
}