package com.example.duan1ne.Fagment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.duan1ne.Adapter.CartAdapter;
import com.example.duan1ne.Model.Cart;
import com.example.duan1ne.R;
import com.example.duan1ne.dao.CartDao;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class CartFragment extends Fragment {
    RecyclerView recyclerCart;
    LinearLayout ln1,ln2;
    TextView total;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart1, container, false);
        recyclerCart = view.findViewById(R.id.recyclerViewCart);
        total = view.findViewById(R.id.txtTotalPrice);
        Button btnCheckout = view.findViewById(R.id.buttonCheckout);
        ln1 = view.findViewById(R.id.linearLayout);
        ln2 = view.findViewById(R.id.linearLayout1);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String totalAmount = total.getText().toString();
                BillFragment billFragment = BillFragment.newInstance(totalAmount,null);

                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.container, billFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        loadData();
        return view;
    }

    private void loadData(){
        CartDao cartDao = new CartDao(getContext());
        ArrayList<Cart> listCart = cartDao.getDsCart(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        if (!listCart.isEmpty()) {
            ln1.setVisibility(View.GONE);
            ln2.setVisibility(View.VISIBLE);
        }else {
            ln1.setVisibility(View.VISIBLE);
            ln2.setVisibility(View.GONE);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerCart.setLayoutManager(linearLayoutManager);
        CartAdapter adapter = new CartAdapter(getContext(), listCart, total,ln1,ln2);
        recyclerCart.setAdapter(adapter);
    }

}