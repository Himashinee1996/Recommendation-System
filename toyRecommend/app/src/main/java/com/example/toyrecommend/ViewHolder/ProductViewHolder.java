package com.example.toyrecommend.ViewHolder;

import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toyrecommend.Interface.ItemClickListner;
import com.example.toyrecommend.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtProductName, txtProductDescription, txtProductPrice, txtProductRatings;
    public ImageView imageView;
    public ItemClickListner listner;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        txtProductName = (TextView) itemView.findViewById(R.id.product_name);
        txtProductDescription = (TextView) itemView.findViewById(R.id.product_description);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);
        txtProductRatings = (TextView) itemView.findViewById(R.id.product_ratings);

        txtProductName.setPaintFlags(txtProductName.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
        txtProductPrice.setPaintFlags(txtProductPrice.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
        txtProductDescription.setPaintFlags(txtProductDescription.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View v) {
        listner.onClick(v, getAdapterPosition(), false);
    }
}
