package com.example.duan1ne.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.duan1ne.Data.Database;
import com.example.duan1ne.Model.Cart;

import java.util.ArrayList;

public class CartDao {
    @SuppressLint("StaticFieldLeak")
    private static Database database;
    public CartDao(Context context){
        database = new Database(context);
    }



    public ArrayList<Cart> getDsCart(String id){
        ArrayList<Cart> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery("SELECT id, product_id, name, price, quantity  FROM CART WHERE user_id = ?",  new String[]{String.valueOf(id)});
        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            do {
                list.add(new Cart(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4)));
            }while (cursor.moveToNext());
        }
        return list;
    }

    public void updateQuantity(int id, int newQuantity) {
        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("quantity", newQuantity);
        int rowsAffected = sqLiteDatabase.update("CART", contentValues, "id = ?", new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
    }


    public int removeItemCart(int id) {
        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
        int rowsAffected = sqLiteDatabase.delete("CART", "id = ?", new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
        return rowsAffected;
    }

}