package com.example.duan1ne;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.duan1ne.Fagment.CartFragment;
import com.example.duan1ne.Fagment.Homefragment;
import com.example.duan1ne.Fagment.RewardsFragment;
import com.example.duan1ne.Fagment.UserFragment;


import com.example.duan1ne.Model.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Homefragment homeFragment = new Homefragment();
    CartFragment cartFragment = new CartFragment();
    UserFragment userFragment = new UserFragment();
    RewardsFragment rewardsFragment = new RewardsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_natigation);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, homeFragment).commit();

        ImageView img_search = findViewById(R.id.img_search);
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SearchActivity2.class);
                startActivity(i);
            }
        });

        // Đặt màu nền của thanh trạng thái thành màu trắng
        getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.white));

        // Đặt màu biểu tượng của thanh trạng thái thành màu tối
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if (item.getItemId()== R.id.menu_home) {

                    getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                    return true;
                }
                else if (item.getItemId()== R.id.menu_oder){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,cartFragment).commit();
                    return true;
                }
                else if  (item.getItemId()== R.id.menu_account){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,userFragment).commit();
                    return true;
                }
                else if  (item.getItemId()== R.id.menu_rewards){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,rewardsFragment).commit();
                    return true;
                }
                return false;
            }
        });

    }
    public void updateCart(ArrayList<Product> productList) {

    }
}