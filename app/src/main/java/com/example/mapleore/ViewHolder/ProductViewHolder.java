package com.example.mapleore.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapleore.R;
import com.example.mapleore.interface_folder.ItemClickListner;

import java.security.PublicKey;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

public TextView txtProductanme , txtProductDescription , txtProductPrice;
public ImageView imageView;
public  ItemClickListner listner;

    public ProductViewHolder(@NonNull View itemView)
    {
        super(itemView);

        imageView =(ImageView)itemView.findViewById(R.id.Product_layout_image);
        txtProductanme =(TextView) itemView.findViewById(R.id.Product_layout_name);
        txtProductDescription =(TextView) itemView.findViewById(R.id.Product_layout_Description);
        txtProductPrice = (TextView) itemView .findViewById(R.id.Product_layout_price);

    }

    public void SetItemClickListner(ItemClickListner listner){
        this.listner = listner;
    }

    @Override
    public void onClick(View v) {
listner.onClick(v, getAdapterPosition() , false);
    }


}
