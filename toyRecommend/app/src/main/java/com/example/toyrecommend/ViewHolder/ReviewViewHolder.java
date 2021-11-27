package com.example.toyrecommend.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toyrecommend.R;

public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtuser,txtRate, txtReview;

    public ReviewViewHolder(@NonNull View itemView) {
        super(itemView);

        txtuser = (TextView) itemView.findViewById(R.id.review_user_name);
        txtRate = (TextView) itemView.findViewById(R.id.user_rate);
        txtReview = (TextView) itemView.findViewById(R.id.user_review);
    }

    @Override
    public void onClick(View v) {

    }
}
