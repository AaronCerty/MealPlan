package com.example.pickupmeal.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickupmeal.R;

import org.jetbrains.annotations.NotNull;

public class CommentViewHolderJ extends RecyclerView.ViewHolder {

    @NotNull
    public TextView name;
    @NotNull
    public TextView comment;

    public CommentViewHolderJ(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.tv_name);
        comment = itemView.findViewById(R.id.tv_comment);
    }
}
