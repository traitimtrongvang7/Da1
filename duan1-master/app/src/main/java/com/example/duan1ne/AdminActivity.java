// AdminActivity.java
package com.example.duan1ne;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.duan1ne.Data.Database;

import java.io.ByteArrayOutputStream;

public class AdminActivity extends Activity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private EditText etProductName, etProductPrice, etCategoryId;
    private ImageView ivProductImage;
    private Database db;
    private Bitmap productImage;

    @SuppressLint({"MissingInflatedId", "QueryPermissionsNeeded"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        db = new Database(this);

        etProductName = findViewById(R.id.etProductName);
        etProductPrice = findViewById(R.id.etProductPrice);
        etCategoryId = findViewById(R.id.etCategoryId);
        ivProductImage = findViewById(R.id.ivProductImage);
        Button btnSelectImage = findViewById(R.id.btnSelectImage);
        Button btnAddProduct = findViewById(R.id.btnAddProduct);
        Button btnViewProduct = findViewById(R.id.btnViewProduct);

        Intent intent = getIntent();
        int productId = intent.getIntExtra("PRODUCT_ID", -1);

        btnSelectImage.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

        btnViewProduct.setOnClickListener(v -> {
            Intent i = new Intent(AdminActivity.this, ViewActivity.class);
            startActivity(i);
            finish();
        });

        btnAddProduct.setOnClickListener(v -> addProduct());
    }

    private void addProduct() {
        String name = etProductName.getText().toString();
        int price = Integer.parseInt(etProductPrice.getText().toString());
        int categoryId = Integer.parseInt(etCategoryId.getText().toString());
        byte[] image = getImageBytes(productImage);

        long result = db.addProduct(name, image, price, categoryId);
        if (result != -1) {
            Toast.makeText(this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
            resetFields();
        } else {
            Toast.makeText(this, "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] getImageBytes(Bitmap bitmap) {
        if (bitmap == null) return new byte[0];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            assert extras != null;
            productImage = (Bitmap) extras.get("data");
            ivProductImage.setImageBitmap(productImage);
        }
    }

    private void resetFields() {
        etProductName.setText("");
        etProductPrice.setText("");
        etCategoryId.setText("");
        ivProductImage.setImageBitmap(null);
        productImage = null;
    }
}