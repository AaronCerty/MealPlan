package com.example.pickupmeal.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickupmeal.R;
import com.example.pickupmeal.constants.AppDataJ;
import com.example.pickupmeal.model.CommentJ;
import com.example.pickupmeal.viewholder.CommentViewHolderJ;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentMenuJActivity extends AppCompatActivity {

    @BindView(R.id.rcv_comment)
    RecyclerView rcvComment;
    @BindView(R.id.edt_comment)
    EditText edtComment;
    @BindView(R.id.iv_send)
    ImageView ivSend;

    public FirebaseDatabase database;
    public DatabaseReference comment;

    FirebaseRecyclerAdapter<CommentJ, CommentViewHolderJ> adapter;

    private String menuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_menu);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Bình luận");

        database = FirebaseDatabase.getInstance();
        comment = database.getReference("Comment");

        menuId = getIntent().getStringExtra("menuId");

        adapter = new FirebaseRecyclerAdapter<CommentJ, CommentViewHolderJ>(
                CommentJ.class,
                R.layout.item_comment,
                CommentViewHolderJ.class,
                comment.child(menuId).orderByKey()
        ) {
            @Override
            protected void populateViewHolder(CommentViewHolderJ viewHolder, CommentJ comment, int i) {
                viewHolder.name.setText(comment.getName());
                viewHolder.comment.setText(comment.getContent());
            }
        };
        adapter.notifyDataSetChanged();
        rcvComment.setAdapter(adapter);


    }

    public boolean onSupportNavigateUp() {
        this.finish();
        return true;
    }


    @OnClick(R.id.iv_send)
    public void onViewClicked() {
        CommentJ comment = new CommentJ(
                AppDataJ.g().currentUser.getName(),
                edtComment.getText().toString()
        );
        this.comment.child(menuId).push().setValue(comment);
        edtComment.setText("");
    }
}