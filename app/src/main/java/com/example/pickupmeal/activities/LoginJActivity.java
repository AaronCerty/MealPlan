package com.example.pickupmeal.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pickupmeal.R;
import com.example.pickupmeal.constants.AppDataJ;
import com.example.pickupmeal.model.UserJ;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginJActivity extends AppCompatActivity {

    private Button btnSignIn;
    private TextView btnSignUp;
    private EditText edtEmail, edtPassword;

    private FirebaseDatabase database;
    private DatabaseReference tableUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnSignIn = findViewById(R.id.sign_in);
        btnSignUp = findViewById(R.id.sign_up);

        edtEmail = findViewById(R.id.email);
        edtPassword = findViewById(R.id.password);

        database = FirebaseDatabase.getInstance();
        tableUser = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String email = edtEmail.getText().toString().replace(".", "1");
                        if (snapshot.child(email).exists()) {
                            UserJ user = snapshot.child(email).getValue(UserJ.class);
                            if (user.getPassword().equals(edtPassword.getText().toString())) {
                                AppDataJ.g().currentUser = user;
                                AppDataJ.g().setHost(user.getHost());

                                Toast.makeText(LoginJActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                startActivity(
                                        new Intent(
                                                LoginJActivity.this,
                                                HomeJActivity.class
                                        )
                                );
                                finish();
                            } else {
                                Toast.makeText(LoginJActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginJActivity.this, "Tài khoản không hợp lệ!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(
                        new Intent(
                                LoginJActivity.this,
                                SignUpJActivity.class
                        )
                );
            }
        });
    }
}