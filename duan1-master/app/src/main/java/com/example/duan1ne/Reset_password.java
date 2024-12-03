package com.example.duan1ne;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Reset_password extends AppCompatActivity {
    private EditText gmailreset;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resetpass);
        TextView txtBack = findViewById(R.id.txtBack);
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Reset_password.this , Activity_login.class));
            }
        });
        gmailreset = findViewById(R.id.gmailreset);
        Button btnresetpassword = findViewById(R.id.btnreset);
        mAuth=FirebaseAuth.getInstance();
        btnresetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = gmailreset.getText().toString().trim();
                if (email.isEmpty()){
                    gmailreset.setError("Email is required");
                    gmailreset.requestFocus();
                    return;
                }
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Reset_password.this, "Password reset email sent. Check your email inbox.", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Reset_password.this , Activity_login.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(Reset_password.this, "Failed to send reset email. Please check your email address.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}