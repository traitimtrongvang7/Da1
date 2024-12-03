package com.example.duan1ne;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.duan1ne.Data.Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Activity_signin extends AppCompatActivity {
    private EditText edtemail, edtpassword, edtpassword1, edtName, edtPhone, edtAddress;
    private FirebaseAuth mAuth;
    @SuppressLint("StaticFieldLeak")
    private static Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Khởi tạo Database
        database = new Database(this);

        // Gán các EditText và Button
        edtemail = findViewById(R.id.edtgmail);
        edtpassword = findViewById(R.id.edtpassword);
        edtpassword1 = findViewById(R.id.edtCfpassword);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAdd);
        Button btnregister = findViewById(R.id.btnregister);
        Button btnlogin = findViewById(R.id.btnlogin);

        // Xử lý sự kiện nhấn nút đăng nhập
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Activity_signin.this, Activity_login.class);
                startActivity(i);
            }
        });

        // Xử lý sự kiện nhấn nút đăng ký
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }

            private void register() {
                String email = edtemail.getText().toString();
                String password = edtpassword.getText().toString();
                String password1 = edtpassword1.getText().toString();
                String name = edtName.getText().toString();
                String phone = edtPhone.getText().toString();
                String address = edtAddress.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password1)) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập xác nhận mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(password1)) {
                    Toast.makeText(getApplicationContext(), "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tạo tài khoản Firebase
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = user.getUid();
                            ContentValues values = new ContentValues();
                            values.put("id", uid);
                            values.put("name", name);
                            values.put("role", 1);
                            values.put("email", email);
                            values.put("phone", phone);
                            values.put("address", address);

                            // Chèn dữ liệu vào cơ sở dữ liệu SQLite
                            SQLiteDatabase db = database.getWritableDatabase();
                            db.insert("USER", null, values);
                            db.close();

                            Toast.makeText(getApplicationContext(), "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Activity_signin.this, Activity_login.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(getApplicationContext(), "Tạo tài khoản không thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}