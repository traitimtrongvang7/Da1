package com.example.duan1ne.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1ne.Model.Cart;
import com.example.duan1ne.R;
import com.example.duan1ne.dao.CartDao;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{

    private final Context context;
    private ArrayList<Cart> list;
    private CartDao cartDao;
    TextView total;
    LinearLayout ln1,ln2;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public CartAdapter(Context context, ArrayList<Cart> list, TextView total, LinearLayout ln1, LinearLayout ln2) {
        this.context = context;
        this.list = list;
        this.total = total;
        this.ln1 = ln1;
        this.ln2 = ln2;
        cartDao = new CartDao(context); // Khởi tạo CartDao
        updatePrice(); // Cập nhật tổng giá trị khi khởi tạo adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadData() {
        list.clear();
        list = cartDao.getDsCart(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        notifyDataSetChanged();
        checkAmount();
        updatePrice();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart1, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = list.get(position);
        cartDao = new CartDao(context);
        holder.name.setText(cart.getName());
        holder.price.setText(String.valueOf(cart.getPrice()*cart.getQuantity()));
        holder.quantity.setText(String.valueOf(cart.getQuantity()));
        holder.imgRm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rsl = cartDao.removeItemCart(cart.getId());
                if (rsl > 0){
                    Toast.makeText(context, "Xóa khỏi giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                    loadData();
                }
                else {
                    Toast.makeText(context, "Xóa khỏi giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.imgIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantityOld = cart.getQuantity();
                int quantityNew = quantityOld + 1;
                cartDao.updateQuantity(cart.getId(), quantityNew);
                loadData();
            }
        });
        holder.imgDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantityOld = cart.getQuantity();
                int quantityNew = quantityOld - 1;
                if (quantityOld <= 1){
                    Toast.makeText(context, "Số lượng không thể nhỏ hơn 1", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    cartDao.updateQuantity(cart.getId(), quantityNew);
                    loadData();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder{
        TextView name, quantity,price,total;
        ImageView imgIn, imgDe, imgRm;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);
            imgIn = itemView.findViewById(R.id.imgIncrease);
            imgDe = itemView.findViewById(R.id.imgDecrease);
            imgRm = itemView.findViewById(R.id.imgRemove);
            total = itemView.findViewById(R.id.txtTotalPrice);
        }
    }

    @SuppressLint("SetTextI18n")
    private void updatePrice() {
        int sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum += (int) (list.get(i).getPrice() * list.get(i).getQuantity());
        }
        total.setText("" + sum);
    }

    private void checkAmount(){
        if (!list.isEmpty()) {
            ln1.setVisibility(View.GONE);
            ln2.setVisibility(View.VISIBLE);
        }else {
            ln1.setVisibility(View.VISIBLE);
            ln2.setVisibility(View.GONE);
        }
    }

}