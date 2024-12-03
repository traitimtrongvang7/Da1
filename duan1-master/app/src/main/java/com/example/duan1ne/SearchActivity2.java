package com.example.duan1ne;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1ne.Adapter.ProductAdapter;
import com.example.duan1ne.Model.Product;
import com.example.duan1ne.dao.ProductDao;

import java.util.ArrayList;

public class SearchActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search2);
        EditText edtsearch = findViewById(R.id.edtsearch1);
        RecyclerView recyclerView = findViewById(R.id.list1);
        ProductDao productDao = new ProductDao(this); // Sử dụng 'this' trong Activity để lấy Context.
        ArrayList<Product> list = ProductDao.getDsProduct(); // Sử dụng productDao được khởi tạo ở trên thay vì gọi trực tiếp ProductDao.
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2); // Sử dụng 'this' trong Activity để lấy Context.
        recyclerView.setLayoutManager(gridLayoutManager); // Sử dụng thực thể của RecyclerView để gọi setLayoutManager().
        ProductAdapter adapter = new ProductAdapter(this, list); // Sử dụng 'this' trong Activity để lấy Context.
        recyclerView.setAdapter(adapter);
        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                String keyword = s.toString();
                adapter.filter(keyword);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });









    }}