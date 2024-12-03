package com.example.duan1ne.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.duan1ne.Model.Product;
import com.example.duan1ne.Model.User;
import com.example.duan1ne.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1; // Define database version
    private final Context context;
    private static final String TAG = "Database";

    public Database(Context context) {
        super(context, "magiccoffee", null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String category = "CREATE TABLE CATEGORY(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);";
        db.execSQL(category);

        // Create CART table
        String cartTable = "CREATE TABLE CART(id INTEGER PRIMARY KEY AUTOINCREMENT, product_id INTEGER REFERENCES PRODUCT(id) , name text, price INTEGER, quantity INTEGER, user_id TEXT REFERENCES USER(id))";
        db.execSQL(cartTable);

        // Create PRODUCT table
        String product = "CREATE TABLE PRODUCT(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, image BLOB, price INTEGER, inCart INTEGER DEFAULT 0, category_id INTEGER REFERENCES CATEGORY(id))";
        db.execSQL(product);

        // Create USER table
        String users = "CREATE TABLE USER(id TEXT PRIMARY KEY, name TEXT, role INTEGER, email TEXT, phone TEXT, address TEXT)";
        db.execSQL(users);

        // Create ORDERS table
        String order = "CREATE TABLE ORDERS(id INTEGER PRIMARY KEY AUTOINCREMENT, user_id TEXT REFERENCES USER(id), product_id INTEGER REFERENCES PRODUCT(id), price INTEGER, date TEXT, time TEXT, state TEXT)";
        db.execSQL(order);

        // Create ORDERDETAIL table
        String orderdetail = "CREATE TABLE ORDERDETAIL(id INTEGER PRIMARY KEY AUTOINCREMENT, product_id INTEGER REFERENCES PRODUCT(id) , order_id INTEGER REFERENCES ORDERS(id), name text, price INTEGER, quantity INTEGER, user_id TEXT REFERENCES USER(id))";
        db.execSQL(orderdetail);

        // Add sample products
        addSampleProducts(db);
        addSampleCategories(db);
    }

    private void addSampleProducts(SQLiteDatabase db) {
        int[] categories = {1, 2, 3, 4, 5, 6};
        for (int categoryId : categories) {
            ContentValues values = new ContentValues();
            values.put("name", "Caffe Mocha");
            values.put("image", getBytesFromImage(R.drawable.anh1));
            values.put("price", 450000);
            values.put("category_id", categoryId);
            db.insert("PRODUCT", null, values);
        }

        db.execSQL("INSERT INTO USER VALUES ('0rOgurz8E1TmxVpeAqtKeBs9l7k2', 'Test', 1 ,'test123@gmail.com','09622421','TP. HCM')");
    }

    private void addSampleCategories(SQLiteDatabase db) {
        String[] categoryNames = {"Cà phê", "Trà sữa", "Trà", "Bánh ngọt", "Đồ ăn vặt", "Khác"};
        for (String categoryName : categoryNames) {
            ContentValues values = new ContentValues();
            values.put("name", categoryName);
            db.insert("CATEGORY", null, values);
        }
    }


    private byte[] getBytesFromImage(int resourceId) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            // Handle upgrades based on oldVersion and newVersion
            if (oldVersion < 2) {
                // Example: Add a new column
                db.execSQL("ALTER TABLE CART ADD COLUMN new_column TEXT");
            }
            // ... other upgrade logic ...
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle downgrades (drop and recreate tables or implement data migration)
        db.execSQL("DROP TABLE IF EXISTS CATEGORY");
        db.execSQL("DROP TABLE IF EXISTS PRODUCT");
        db.execSQL("DROP TABLE IF EXISTS CART");
        db.execSQL("DROP TABLE IF EXISTS USER");
        db.execSQL("DROP TABLE IF EXISTS ORDERS");
        db.execSQL("DROP TABLE IF EXISTS ORDERDETAIL");
        onCreate(db);
    }

    // Add product
    public long addProduct(String name, byte[] image, int price, int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("image", image);
        values.put("price", price);
        values.put("category_id", categoryId);
        return db.insert("PRODUCT", null, values);
    }

    // ... other database methods ...

    public ArrayList<Object> getAllCategories() {
        ArrayList<Object> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CATEGORY", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                org.tensorflow.lite.support.label.Category category = new org.tensorflow.lite.support.label.Category(name, id);
                categories.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return categories;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM PRODUCT", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                byte[] image = cursor.getBlob(2);
                int price = cursor.getInt(3);
                int inCart = cursor.getInt(4);
                int categoryId = cursor.getInt(5);
                Product product = new Product(id, name, image, price, inCart, categoryId);
                products.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return products;
    }

    public List<Product> getProductsByCategory(int categoryId) {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM PRODUCT WHERE category_id = ?", new String[]{String.valueOf(categoryId)});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                byte[] image = cursor.getBlob(2);
                int price = cursor.getInt(3);
                int inCart = cursor.getInt(4);
                Product product = new Product(id, name, image, price, inCart, categoryId);
                products.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return products;
    }

    public User getUserById(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM USER WHERE id = ?", new String[]{userId});
        if (cursor.moveToFirst()) {
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            int role = cursor.getInt(2);
            String email = cursor.getString(3);
            String phone = cursor.getString(4);
            String address = cursor.getString(5);
            cursor.close();
            db.close();}
        return null;
    }}