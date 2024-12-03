package com.example.duan1ne.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.duan1ne.Data.Database;
import com.example.duan1ne.Model.User;

public class UserDao {
    @SuppressLint("StaticFieldLeak")
    private static Database database;
    public UserDao(Context context){
        database = new Database(context);
    }

    public User getUserById(String id) {
        SQLiteDatabase db = database.getReadableDatabase();
        User user = null;

        // Tạo một đối tượng Cursor để lưu trữ kết quả truy vấn
        Cursor cursor = null;
        try {
            cursor = db.query("USER", // Tên bảng
                    new String[]{"id", "name", "role", "email", "phone", "address"}, // Các cột cần lấy
                    "id = ?", // Điều kiện WHERE
                    new String[]{id}, // Giá trị của tham số
                    null, null, null);

            if (cursor.moveToFirst()) {
                // Khởi tạo đối tượng User với thông tin từ Cursor
                user = new User(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return user;
    }

    public void updateUserProfile(String id, String name, String phone, String address) {
        SQLiteDatabase db = database.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phone", phone);
        values.put("address", address);

        // Điều kiện WHERE để xác định bản ghi nào cần cập nhật
        String whereClause = "id = ?";
        String[] whereArgs = new String[]{id};

        // Thực hiện cập nhật
        int rowsAffected = db.update("USER", values, whereClause, whereArgs);
        db.close();

        // Trả về true nếu ít nhất một hàng bị ảnh hưởng
    }

}