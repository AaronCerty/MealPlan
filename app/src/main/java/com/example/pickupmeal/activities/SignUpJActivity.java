package com.example.pickupmeal.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pickupmeal.R;
import com.example.pickupmeal.model.UserJ;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpJActivity extends AppCompatActivity {

    private Button btnSignUp;
    private TextView btnSignIn;
    private EditText edtName, edtEmail, edtPassword;

    private FirebaseDatabase database;
    private DatabaseReference tableUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnSignUp = findViewById(R.id.sign_up);
        btnSignIn = findViewById(R.id.sign_in);

        edtName = findViewById(R.id.name);
        edtEmail = findViewById(R.id.email);
        edtPassword = findViewById(R.id.password);

        database = FirebaseDatabase.getInstance();
        tableUser = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().replace(".", "1");
                tableUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(email).exists()) {
                            Toast.makeText(SignUpJActivity.this, "Email đã được đăng ký trước!", Toast.LENGTH_SHORT).show();
                        } else {
                            UserJ user = new UserJ(
                                    edtName.getText().toString(),
                                    edtEmail.getText().toString(),
                                    edtPassword.getText().toString(),
                                    false
                            );
                            tableUser.child(email).setValue(user);
                            Toast.makeText(SignUpJActivity.this, "Đăng ký Email thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}