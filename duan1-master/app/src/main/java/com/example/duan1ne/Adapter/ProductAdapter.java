package com.example.duan1ne.Adapter;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1ne.Data.Database;
import com.example.duan1ne.MainActivity;
import com.example.duan1ne.Model.Cart;
import com.example.duan1ne.Model.Product;
import com.example.duan1ne.R;
import com.example.duan1ne.dao.ProductDao;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> listproduct;
    private List<Cart> listCart;
    private final Context context;
    private final List<Product> originalList;
    private final List<Product> filteredList;
    private final Database dbHelper;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public ProductAdapter(Context context, List<Product> list) {
        this.context = context;
        this.originalList = list;
        this.filteredList = new ArrayList<>(list);
        this.listproduct = new ArrayList<>(list);
        this.dbHelper = new Database(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_product1, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = filteredList.get(position);
        ProductDao productDao = new ProductDao(context);
        boolean isInCart = productDao.isProductInCart(product.getId(), Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        holder.name.setText(product.getName());
        holder.price.setText(String.valueOf(product.getPrice()) + "VNĐ");

        if (isInCart) {
            holder.add.setVisibility(View.GONE);
        } else {
            holder.add.setVisibility(View.VISIBLE);
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = holder.getAdapterPosition();
                    if (clickedPosition != RecyclerView.NO_POSITION) {
                        addToCart(filteredList.get(clickedPosition));
                        product.setInCart(true); // Đánh dấu sản phẩm đã được thêm vào giỏ hàng
                        notifyItemChanged(clickedPosition); // Cập nhật trạng thái sản phẩm trong RecyclerView
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }
    public void updateProductList(ArrayList<Product> listproduct) {
        this.listproduct = listproduct;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filter(String keyword) {
        filteredList.clear();

        if (keyword.isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            keyword = keyword.toLowerCase().trim();
            for (Product product : originalList) {
                if (product.getName().toLowerCase().contains(keyword)) {
                    filteredList.add(product);
                }
            }
        }

        notifyDataSetChanged(); // Cập nhật RecyclerView khi thay đổi dữ liệu
    }

    private void addToCart(Product product) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("product_id", product.getId());
        values.put("name", product.getName());
        values.put("price", product.getPrice());
        values.put("quantity", 1);
        values.put("user_id", Objects.requireNonNull(mAuth.getCurrentUser()).getUid());

        long result = db.insert("CART", null, values);
        db.close();

        if (result != -1) {
            Toast.makeText(context, "Added " + product.getName() + " to cart", Toast.LENGTH_SHORT).show();
            // Cập nhật danh sách sản phẩm trong CartFragment
            updateCartFragment();
        } else {
            Toast.makeText(context, "Error. Cannot add product to cart", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCartFragment() {
        // Gọi phương thức updateProductList trong CartFragment
        if (context instanceof MainActivity) {
            ArrayList<Product> cartProducts = getCartProductsFromDatabase(); // Lấy danh sách sản phẩm từ bảng CART
            ((MainActivity) context).updateCart(cartProducts);
        }
    }

    private ArrayList<Product> getCartProductsFromDatabase() {
        ArrayList<Product> cartProducts = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("CART", null, null, null, null, null, null);
        try {
            while (cursor.moveToNext()) {
                int productIdIndex = cursor.getColumnIndex("product_id");
                int nameIndex = cursor.getColumnIndex("name");
                int priceIndex = cursor.getColumnIndex("price");
                if (productIdIndex != -1 && nameIndex != -1 && priceIndex != -1) {
                    int productId = cursor.getInt(productIdIndex);
                    String name = cursor.getString(nameIndex);
                    double price = cursor.getDouble(priceIndex);
                    Product product = new Product(productId, name, (int) price, false);
                    cartProducts.add(product);
                }
            }
        } finally {
            cursor.close();
            db.close();
        }
        return cartProducts;
    }





    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, price, addtocart;
        ImageView add;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            add = itemView.findViewById(R.id.buttonAddToCart);
        }
    }
}