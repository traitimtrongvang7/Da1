// BillFragment.java
package com.example.yourapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class BillFragment extends Fragment {

    private MaterialButton buttonCheckout;
    private String totalAmount = "$6.99";  // Đây là số tiền bạn lấy từ hóa đơn
    private String productName = "Product Name";  // Đây là tên sản phẩm từ hóa đơn

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_bill, container, false);

        // Tìm nút Track Order
        buttonCheckout = view.findViewById(R.id.buttonCheckout);

        // Đặt sự kiện click cho nút Track Order
        buttonCheckout.setOnClickListener(v -> {
            // Tạo Intent chuyển sang OrderDetailActivity
            Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
            // Truyền số tiền và tên sản phẩm
            intent.putExtra("TOTAL_AMOUNT", totalAmount);
            intent.putExtra("PRODUCT_NAME", productName);

            // Chuyển màn hình
            startActivity(intent);
        });

        return view;
    }
}
