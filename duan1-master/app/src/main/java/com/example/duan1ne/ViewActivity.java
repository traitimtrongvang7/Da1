package com.example.duan1ne;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1ne.Adapter.ProductAdapterAdmin;
import com.example.duan1ne.Model.Product;
import com.example.duan1ne.dao.ProductDao;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ViewActivity extends AppCompatActivity implements ProductAdapterAdmin.OnProductClickListener {

    private RecyclerView recyclerView;
    private ProductAdapterAdmin productAdapterAdmin;
    private ProductDao productDao;
    TextView tv_back;
    private Bitmap productImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        productDao = new ProductDao(this);

        recyclerView = findViewById(R.id.recyclerView);
        tv_back = findViewById(R.id.tv_back);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Product> productList = ProductDao.getDsProduct();
        if (productList.isEmpty()) {
            Toast.makeText(this, "No products available", Toast.LENGTH_SHORT).show();
        } else {
            productAdapterAdmin = new ProductAdapterAdmin(this, productList, this);
            recyclerView.setAdapter(productAdapterAdmin);
        }

        tv_back.setOnClickListener(v -> {
            Intent intent = new Intent(ViewActivity.this, AdminActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onUpdateClick(Product product) {
        showUpdateDialog(product);
    }

    @Override
    public void onDeleteClick(int productId) {
        showDeleteDialog(productId);
    }

    private void showUpdateDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_update_product, null);
        builder.setView(dialogView);

        EditText etProductName = dialogView.findViewById(R.id.etProductName);
        EditText etProductPrice = dialogView.findViewById(R.id.etProductPrice);

        // Điền thông tin sản phẩm hiện tại vào dialog
        etProductName.setText(product.getName());
        etProductPrice.setText(String.valueOf(product.getPrice()));

        builder.setTitle("Update Product")
                .setPositiveButton("Update", (dialog, id) -> {
                    String name = etProductName.getText().toString();
                    int price = Integer.parseInt(etProductPrice.getText().toString());
                    byte[] image = getImageBytes(productImage);

                    // Cập nhật sản phẩm trong cơ sở dữ liệu
                    productDao.updateProduct(product.getId(), name, image, price);
                    Toast.makeText(this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();

                    // Refresh RecyclerView
                    refreshProductList();
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDeleteDialog(int productId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Product")
                .setMessage("Are you sure you want to delete this product?")
                .setPositiveButton("Delete", (dialog, id) -> {
                    // Xóa sản phẩm trong cơ sở dữ liệu
                    productDao.deleteProduct(productId);
                    Toast.makeText(this, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();

                    // Refresh RecyclerView
                    refreshProductList();
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private byte[] getImageBytes(Bitmap bitmap) {
        if (bitmap == null) return new byte[0];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void refreshProductList() {
        List<Product> productList = ProductDao.getDsProduct();
        productAdapterAdmin = new ProductAdapterAdmin(this, productList, this);
        recyclerView.setAdapter(productAdapterAdmin);
    }
}