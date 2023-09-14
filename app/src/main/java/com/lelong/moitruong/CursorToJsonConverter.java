package com.lelong.moitruong;

import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CursorToJsonConverter {
    public static JsonArray cursorToJson(Cursor cursor) {
        JsonArray jsonArray = new JsonArray();

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int totalColumn = cursor.getColumnCount();
            JsonObject rowObject = new JsonObject();

            for (int i = 0; i < totalColumn; i++) {
                String columnName = cursor.getColumnName(i);
                String columnValue = cursor.getString(i);

                // Thêm cặp khóa-giá trị vào rowObject
                rowObject.add(columnName, new JsonPrimitive(columnValue));
            }

            jsonArray.add(rowObject);
            cursor.moveToNext();
        }

        //cursor.close();

        return jsonArray;
    }
}
