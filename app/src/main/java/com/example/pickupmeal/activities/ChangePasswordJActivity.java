package com.example.pickupmeal.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pickupmeal.R;
import com.example.pickupmeal.constants.AppDataJ;
import com.example.pickupmeal.model.UserJ;
import com.example.pickupmeal.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePasswordJActivity extends AppCompatActivity {

    public FirebaseDatabase database;
    public DatabaseReference table_user;
    @BindView(R.id.edt_old_pw)
    EditText edtOldPw;
    @BindView(R.id.edt_new_pw)
    EditText edtNewPw;
    @BindView(R.id.edt_re_new_pw)
    EditText edtReNewPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Đổi mật khẩu");

        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User");
    }

    public boolean onSupportNavigateUp() {
        this.finish();
        return true;
    }

    @OnClick(R.id.change_pw)
    public void onViewClicked() {
        if (Utils.isEmpty(edtOldPw)) {
            Toast.makeText(this, "Mật khẩu cũ không được bỏ trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Utils.isEmpty(edtNewPw)) {
            Toast.makeText(this, "Mật khẩu cũ không được bỏ trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Utils.isEmpty(edtReNewPw)) {
            Toast.makeText(this, "Mật khẩu cũ không được bỏ trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!edtNewPw.getText().toString().equals(edtReNewPw.getText().toString())) {
            Toast.makeText(this, "Mật khẩu mới không trùng nhau!", Toast.LENGTH_SHORT).show();
            return;
        }

        table_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String email = AppDataJ.g().currentUser.getEmail().replace(".", "1");
                if (snapshot.child(email).exists()) {
                    UserJ user = snapshot.child(email).getValue(UserJ.class);

                    if (user.getPassword().equals(edtOldPw.getText().toString())) {
                        table_user.child(email).child("password").setValue(edtNewPw.getText().toString());
                        Toast.makeText(ChangePasswordJActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ChangePasswordJActivity.this, "Mật khẩu cũ không chính xác!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}