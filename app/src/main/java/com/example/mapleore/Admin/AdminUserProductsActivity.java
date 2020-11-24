package com.example.mapleore.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mapleore.Model.Cart;
import com.example.mapleore.R;
import com.example.mapleore.ViewHolder.CartViewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executors;

public class AdminUserProductsActivity extends AppCompatActivity {
private RecyclerView productslist;
RecyclerView.LayoutManager layoutManager;
private DatabaseReference cartListref;
private String UserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

productslist = (RecyclerView)findViewById(R.id.Products_list);
productslist.setHasFixedSize(true);
layoutManager = new LinearLayoutManager(this);
productslist.setLayoutManager(layoutManager);

UserId = getIntent().getStringExtra("uid");
       cartListref = FirebaseDatabase.getInstance().getReference().child("Cart List")
               .child("Admin View").child(UserId).child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListref , Cart.class).build();

        FirebaseRecyclerAdapter<Cart , CartViewholder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewholder holder, int position, @NonNull Cart model) {
                holder.txtproductquantity.setText("Quantity: " + model.getQuantity());
                holder.txtproductprice.setText("Price: " + model.getPrice());
                holder.txtproductname.setText("Name: " +model.getPname());

                int oneTypeTotalPrice = (Integer.valueOf(model.getPrice())) * (Integer.valueOf(model.getQuantity())) ;

                holder.txttotPrice.setText("Total Price: " + oneTypeTotalPrice);
            }

            @NonNull
            @Override
            public CartViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);
                CartViewholder holder = new CartViewholder(view);
                return holder;
            }
        };
productslist.setAdapter(adapter);
adapter.startListening();
    }
}
