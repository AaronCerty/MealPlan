package com.example.pickupmeal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pickupmeal.R;
import com.example.pickupmeal.model.FoodJ;
import com.example.pickupmeal.viewholder.FoodViewHolderJ;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailMenuJActivity extends AppCompatActivity {

    @BindView(R.id.rcv_food)
    SwipeableRecyclerView rcvFood;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    
    FirebaseDatabase database;
    DatabaseReference list;
    
    FirebaseRecyclerAdapter<FoodJ, FoodViewHolderJ> adapter;
    
    String menuId;
    
    Boolean isAddFood = false;
    Boolean isFromEmployee = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_menu);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chi tiết Khẩu phần ăn");

        database = FirebaseDatabase.getInstance();
        list = database.getReference("List");
        
        if (getIntent().hasExtra("isAddFood")) {
            isAddFood = true;
        }
        
        if (getIntent().hasExtra("isFromEmployee")) {
            isFromEmployee = true;
            fab.setVisibility(View.GONE);
        }
        
        menuId = getIntent().getStringExtra("menuId");
        
        adapter = new FirebaseRecyclerAdapter<FoodJ, FoodViewHolderJ>(
                FoodJ.class,
                R.layout.item_food,
                FoodViewHolderJ.class,
                list.child(menuId).orderByKey()
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolderJ viewHolder, FoodJ food, int i) {
                viewHolder.name.setText(food.getName());
                viewHolder.calo.setText("Số Calo: " + food.getCalo());
                if (isAddFood) {
                    viewHolder.cbTick.setVisibility(View.GONE);
                }
            }
        };
        
        adapter.notifyDataSetChanged();
        rcvFood.setAdapter(adapter);

        if (isAddFood && !isFromEmployee) {
            rcvFood.setRightBg(R.color.red);
            rcvFood.setRightImage(R.drawable.ic_baseline_delete_24);

            rcvFood.setLeftBg(R.color.blue);
            rcvFood.setLeftImage(R.drawable.ic_baseline_toc_24);

            rcvFood.setListener(new SwipeLeftRightCallback.Listener() {
                @Override
                public void onSwipedLeft(int position) {
                    list.child(menuId).child(adapter.getRef(position).getKey()).removeValue();
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onSwipedRight(int position) {

                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @OnClick(R.id.fab)
    public void onViewClicked() {
        Intent intent = new Intent(DetailMenuJActivity.this, ListFoodJActivity.class);
        intent.putExtra("menuId", menuId);
        if (getIntent().hasExtra("time")) {
            intent.putExtra("time", getIntent().getStringExtra("time"));
        }
        if (getIntent().hasExtra("type")) {
            intent.putExtra("type", getIntent().getStringExtra("type"));
        }
        startActivity(intent);
    }
}