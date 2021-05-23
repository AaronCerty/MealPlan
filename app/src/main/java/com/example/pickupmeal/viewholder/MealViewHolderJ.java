package com.example.pickupmeal.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickupmeal.R;
import com.example.pickupmeal.interfaces.OnClickJListener;

import org.jetbrains.annotations.NotNull;

public class MealViewHolderJ extends RecyclerView.ViewHolder {

    @NotNull
    public TextView name;
    @NotNull
    public TextView type;
    @NotNull
    public ImageView comment;

    private OnClickJListener itemClickListener;
    private OnClickJListener itemCommentListener;

    public void setItemClick(OnClickJListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setItemCommentClick(OnClickJListener itemCommentListener) {
        this.itemCommentListener = itemCommentListener;
    }

    public MealViewHolderJ(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.tv_name);
        type = itemView.findViewById(R.id.tv_type);
        comment = itemView.findViewById(R.id.iv_comment);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onClick(v, getAdapterPosition(), false);
            }
        });

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCommentListener.onClick(v, getAdapterPosition(), false);
            }
        });
    }


}
