package com.example.mapleore.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapleore.R;
import com.example.mapleore.interface_folder.ItemClickListner;

public class CartViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtproductname , txtproductprice , txtproductquantity , txttotPrice;
    private ItemClickListner itemClickListner;

    public CartViewholder(@NonNull View itemView) {
        super(itemView);

        txtproductname = itemView.findViewById(R.id.Cart_item_Product_name);
        txtproductprice = itemView.findViewById(R.id.Cart_item_Product_Price);
        txtproductquantity = itemView.findViewById(R.id.Cart_item_Product_Quantity);
        txttotPrice = itemView.findViewById(R.id.Cart_item_TotalProduct_Price);

    }


    @Override
    public void onClick(View v) {
itemClickListner.onClick(v, getAdapterPosition() , false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
