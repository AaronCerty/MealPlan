package com.example.pickupmeal.viewholder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickupmeal.R;

import org.jetbrains.annotations.NotNull;

public class FoodViewHolderJ extends RecyclerView.ViewHolder {

    @NotNull
    public TextView name;
    @NotNull
    public TextView calo;
    @NotNull
    public CheckBox cbTick;

    public FoodViewHolderJ(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.tv_name);
        calo = itemView.findViewById(R.id.tv_calo);
        cbTick = itemView.findViewById(R.id.cb_tick);
    }
}
