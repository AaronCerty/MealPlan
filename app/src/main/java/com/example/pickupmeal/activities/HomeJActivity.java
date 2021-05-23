package com.example.pickupmeal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
import com.google.android.material.navigation.NavigationView;
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

public class HomeJActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.calendarView)
    CalendarView calendarView;
    @BindView(R.id.rcv_meal)
    RecyclerView rcvMeal;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.fab)
    FloatingActionMenu fab;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    TextView profilename;
    TextView profilemail;

    FirebaseDatabase database;
    DatabaseReference menu;

    String time;

    FirebaseRecyclerAdapter<MealJ, MealViewHolderJ> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        toolbar.setTitle("Trang chủ");
        setSupportActionBar(toolbar);

        initTime();

        initCalendar();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
        View header = navView.getHeaderView(0);

        profilename = header.findViewById(R.id.profilename);
        profilemail = header.findViewById(R.id.profilemail);

        profilename.setText(AppDataJ.g().currentUser.getName());
        profilemail.setText(AppDataJ.g().currentUser.getEmail());

        if (!AppDataJ.g().isHost()) {
            fab.setVisibility(View.GONE);
        }

        database = FirebaseDatabase.getInstance();
        menu = database.getReference("Menu");
        getMenu();
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
                if (snapshot.child(AppDataJ.g().adminEmail.replace(".", "1")).child(time).exists()) {
                    adapter = new FirebaseRecyclerAdapter<MealJ, MealViewHolderJ>(
                            MealJ.class,
                            R.layout.item_meal,
                            MealViewHolderJ.class,
                            menu.child(AppDataJ.g().adminEmail.replace(".", "1")).child(time).orderByKey()
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
                            viewHolder.setItemClick(new OnClickJListener() {
                                @Override
                                public void onClick(@NotNull View view, int position, Boolean isLongClick) {
                                    Intent intent = new Intent(HomeJActivity.this, DetailMenuJActivity.class);
                                    intent.putExtra("menuId", meal.getId());
                                    intent.putExtra("isAddFood", true);
                                    if (!AppDataJ.g().isHost()) {
                                        intent.putExtra("isFromEmployee", true);
                                    }
                                    startActivity(intent);
                                }
                            });
                            viewHolder.setItemCommentClick(new OnClickJListener() {
                                @Override
                                public void onClick(@NotNull View view, int position, Boolean isLongClick) {
                                    startActivity(
                                            new Intent(
                                                    HomeJActivity.this, CommentMenuJActivity.class
                                            ).putExtra("menuId", meal.getId())
                                    );
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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_change_pw:
                startActivity(
                        new Intent(
                                this,
                                ChangePasswordJActivity.class
                        )
                );
                break;
            case R.id.nav_foods:
                if (AppDataJ.g().isHost()) {
                    startActivity(
                            new Intent(
                                    this,
                                    ListFoodJActivity.class
                            ).putExtra("isAddFood", true)
                    );
                } else {
                    Toast.makeText(this, "Tính năng này chỉ dành cho chủ Canteen!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_meals:
                startActivity(
                        new Intent(
                                this,
                                HomeJActivity.class
                        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                );
                finish();
                break;
            case R.id.nav_my_meals:
                if (AppDataJ.g().isHost()) {
                    Toast.makeText(this, "Tính năng này chỉ dành cho Học viên!", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(
                            new Intent(
                                    this,
                                    MyMealJActivity.class
                            )
                    );
                }
                break;
            case R.id.nav_log_out:
                startActivity(
                        new Intent(
                                this,
                                LoginJActivity.class
                        )
                );
                finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}