// OrderDetailActivity.java
package com.example.yourapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OrderDetailActivity extends AppCompatActivity {

    // Khai báo các TextView để hiển thị thông tin
    private TextView txtTotalAmount, txtProductName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // Lấy thông tin từ Intent
        String totalAmount = getIntent().getStringExtra("TOTAL_AMOUNT");
        String productName = getIntent().getStringExtra("PRODUCT_NAME");

        // Ánh xạ các TextView
        txtTotalAmount = findViewById(R.id.txtTotalAmount);
        txtProductName = findViewById(R.id.txtProductName);

        // Hiển thị dữ liệu lên giao diện
        txtTotalAmount.setText("Total: " + totalAmount);
        txtProductName.setText("Product: " + productName);
    }
}
