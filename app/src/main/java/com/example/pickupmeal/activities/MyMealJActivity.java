package com.example.pickupmeal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.pickupmeal.R;
import com.example.pickupmeal.constants.AppDataJ;
import com.example.pickupmeal.interfaces.OnClickJListener;
import com.example.pickupmeal.model.MealJ;
import com.example.pickupmeal.utils.Utils;
import com.example.pickupmeal.viewholder.MealViewHolderJ;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyMealJActivity extends AppCompatActivity {

    @BindView(R.id.calendarView)
    CalendarView calendarView;
    @BindView(R.id.rcv_meal)
    RecyclerView rcvMeal;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.fab)
    FloatingActionMenu fab;

    FirebaseDatabase database;
    DatabaseReference menu;

    String time;

    FirebaseRecyclerAdapter<MealJ, MealViewHolderJ> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meal);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Khẩu phần ăn của tôi");

        initTime();

        initCalendar();

        database = FirebaseDatabase.getInstance();
        menu = database.getReference("Menu");
        getMenu();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void initCalendar() {
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar calendar = eventDay.getCalendar();
                time = Utils.fmNormalDay(calendar.getTimeInMillis());
                getMenu();
            }
        });
    }

    private void getMenu() {
        rcvMeal.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.GONE);

        menu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(AppDataJ.g().currentUser.getEmail().replace(".", "1")).child(time).exists()) {
                    adapter = new FirebaseRecyclerAdapter<MealJ, MealViewHolderJ>(
                            MealJ.class,
                            R.layout.item_meal,
                            MealViewHolderJ.class,
                            menu.child(AppDataJ.g().currentUser.getEmail().replace(".", "1")).child(time).orderByKey()
                    ) {
                        @Override
                        protected void populateViewHolder(MealViewHolderJ viewHolder, MealJ meal, int position) {
                            switch (adapter.getRef(position).getKey()) {
                                case "1breakfast":
                                    viewHolder.name.setText("Bữa sáng");
                                    break;
                                case "2lunch":
                                    viewHolder.name.setText("Bữa trưa");
                                    break;
                                default:
                                    viewHolder.name.setText("Bữa tối");
                                    break;
                            }

                            viewHolder.type.setText("Trạng thái: " + meal.getStatus());
                            viewHolder.comment.setVisibility(View.GONE);
                            viewHolder.setItemClick(new OnClickJListener() {
                                @Override
                                public void onClick(@NotNull View view, int position, Boolean isLongClick) {
                                    Intent intent = new Intent(MyMealJActivity.this, DetailMenuJActivity.class);
                                    intent.putExtra("menuId", meal.getId());
                                    intent.putExtra("isAddFood", true);
                                    intent.putExtra("type", adapter.getRef(position).getKey());
                                    intent.putExtra("time", time);
                                    startActivity(intent);
                                }
                            });
                        }
                    };

                    adapter.notifyDataSetChanged();
                    rcvMeal.setAdapter(adapter);
                    rcvMeal.setVisibility(View.VISIBLE);
                    tvEmpty.setVisibility(View.GONE);
                } else {
                    rcvMeal.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        time = Utils.fmNormalDay(calendar.getTimeInMillis());
    }

    @OnClick({R.id.fab_breakfast, R.id.fab_lunch, R.id.fab_dinner})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab_breakfast:
                initMeal("1breakfast");
                break;
            case R.id.fab_lunch:
                initMeal("2lunch");
                break;
            case R.id.fab_dinner:
                initMeal("3dinner");
                break;
        }
    }

    private void initMeal(String value) {
        fab.close(false);
        startActivity(
                new Intent(
                        this,
                        ListFoodJActivity.class
                ).putExtra("time", time)
                        .putExtra("type", value)
        );
    }
    
}