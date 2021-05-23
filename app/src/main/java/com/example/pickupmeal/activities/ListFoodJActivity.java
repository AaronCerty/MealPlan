package com.example.pickupmeal.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.pickupmeal.R;
import com.example.pickupmeal.constants.AppDataJ;
import com.example.pickupmeal.interfaces.OnClickViewJListener;
import com.example.pickupmeal.model.FoodJ;
import com.example.pickupmeal.model.MealJ;
import com.example.pickupmeal.sheet.AddFoodBottomSheet;
import com.example.pickupmeal.viewholder.FoodViewHolderJ;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListFoodJActivity extends AppCompatActivity {

    @BindView(R.id.rcv_food)
    SwipeableRecyclerView rcvFood;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.btn_save)
    AppCompatButton btnSave;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    FirebaseDatabase database;
    DatabaseReference food;
    DatabaseReference list;
    DatabaseReference menu;

    FirebaseRecyclerAdapter<FoodJ, FoodViewHolderJ> adapter;

    List<FoodJ> foods = new ArrayList();

    Boolean isAddFood = false;

    String menuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Danh sách món ăn");

        database = FirebaseDatabase.getInstance();
        food = database.getReference("Food");
        list = database.getReference("List");
        menu = database.getReference("Menu");

        if (getIntent().hasExtra("menuId")) {
            menuId = getIntent().getStringExtra("menuId");
        }

        if (getIntent().hasExtra("isAddFood")) {
            isAddFood = true;
            fab.setVisibility(View.VISIBLE);
        } else {
            btnSave.setVisibility(View.VISIBLE);
        }

        loadFood();

    }

    private void loadFood() {
        if (AppDataJ.g().isHost()) {
            adapter = new FirebaseRecyclerAdapter<FoodJ, FoodViewHolderJ>(
                    FoodJ.class,
                    R.layout.item_food,
                    FoodViewHolderJ.class,
                    food.orderByKey()
            ) {
                @Override
                protected void populateViewHolder(FoodViewHolderJ viewHolder, FoodJ food, int i) {
                    viewHolder.name.setText(food.getName());
                    viewHolder.calo.setText("Số Calo: " + food.getCalo());
                    if (isAddFood) {
                        viewHolder.cbTick.setVisibility(View.GONE);
                    }
                    viewHolder.cbTick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                foods.add(food);
                            } else {
                                foods.remove(food);
                            }
                        }
                    });
                }
            };

            adapter.notifyDataSetChanged();
            rcvFood.setAdapter(adapter);
        } else {
            rcvFood.setVisibility(View.GONE);

            menu.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(AppDataJ.g().adminEmail.replace(".", "1"))
                    .child(getIntent().getStringExtra("time"))
                    .child(getIntent().getStringExtra("type"))
                    .exists()) {
                        MealJ meal = snapshot.child(AppDataJ.g().adminEmail.replace(".", "1"))
                                .child(getIntent().getStringExtra("time"))
                                .child(getIntent().getStringExtra("type")).getValue(MealJ.class);
                        
                        adapter = new FirebaseRecyclerAdapter<FoodJ, FoodViewHolderJ>(
                                FoodJ.class,
                                R.layout.item_food,
                                FoodViewHolderJ.class,
                                list.child(meal.getId()).orderByKey()
                        ) {
                            @Override
                            protected void populateViewHolder(FoodViewHolderJ viewHolder, FoodJ food, int i) {
                                viewHolder.name.setText(food.getName());
                                viewHolder.calo.setText("Số Calo: " + food.getCalo());
                                if (isAddFood) {
                                    viewHolder.cbTick.setVisibility(View.GONE);
                                }
                                viewHolder.cbTick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked) {
                                            foods.add(food);
                                        } else {
                                            foods.remove(food);
                                        }
                                    }
                                });
                            }
                        };

                        adapter.notifyDataSetChanged();
                        rcvFood.setAdapter(adapter);
                        rcvFood.setVisibility(View.VISIBLE);
                    } else {
                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        
        if (isAddFood) {
            rcvFood.setRightBg(R.color.red);
            rcvFood.setRightImage(R.drawable.ic_baseline_delete_24);
            
            rcvFood.setListener(new SwipeLeftRightCallback.Listener() {
                @Override
                public void onSwipedLeft(int position) {
                    food.child(adapter.getRef(position).getKey()).removeValue();
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

    @OnClick({R.id.btn_save, R.id.fab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                if (foods.isEmpty()) {
                    Toast.makeText(this, "Danh sách món ăn trống!", Toast.LENGTH_SHORT).show();
                } else {
                    if (menuId != null) {
                        for (FoodJ food1 : foods) {
                            list.child(menuId).push().setValue(food1);
                        }
                        Toast.makeText(this, "Bổ sung Khẩu phần ăn thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        String timeInterval = String.valueOf((System.currentTimeMillis() / 1000));
                        for (FoodJ food1 : foods) {
                            list.child(timeInterval).push().setValue(food1);
                        }
                        MealJ meal = new MealJ(
                                timeInterval,
                                "Đầy đủ"
                        );
                        menu.child(AppDataJ.g().currentUser.getEmail().replace(".", "1"))
                                .child(getIntent().getStringExtra("time"))
                                .child(getIntent().getStringExtra("type"))
                                .setValue(meal);
                        Toast.makeText(this, "Đăng ký Khẩu phần ăn thành công!", Toast.LENGTH_SHORT).show();
                    }

                    finish();
                }
                break;
            case R.id.fab:
                showAddFoodDialog();
                break;
        }
    }

    private void showAddFoodDialog() {
        AddFoodBottomSheet addFoodBottomSheet = new AddFoodBottomSheet();
        addFoodBottomSheet.setListener(new OnClickViewJListener() {
            @Override
            public void onClick(@NotNull String name, int calo) {
                FoodJ newFood = new FoodJ(
                        name, calo
                );
                food.push().setValue(newFood);
                addFoodBottomSheet.dismiss();
            }
        });
        addFoodBottomSheet.show(getSupportFragmentManager(), "tag");
    }
}