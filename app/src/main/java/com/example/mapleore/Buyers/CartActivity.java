package com.example.mapleore.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapleore.Model.Cart;
import com.example.mapleore.Prevalent.Prevalent;
import com.example.mapleore.R;
import com.example.mapleore.ViewHolder.CartViewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {
private RecyclerView recyclerView;
private RecyclerView.LayoutManager layoutManager;
private Button Nextbuttn;
private TextView TotalAmount , TxtMsg1;


private  int OverallTotalPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

recyclerView = (RecyclerView)findViewById(R.id.Cart_list);
recyclerView.setHasFixedSize(true);
layoutManager = new LinearLayoutManager(this);
recyclerView.setLayoutManager(layoutManager);

Nextbuttn = (Button)findViewById(R.id.Cart_next_process_button);
TotalAmount = (TextView)findViewById(R.id.Cart_total_price);
        TxtMsg1 = (TextView)findViewById(R.id.msg1);
Nextbuttn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Intent intent = new Intent(CartActivity.this , ConfirmFonalOrderActivity.class);
        intent.putExtra("Total Price" , String.valueOf(OverallTotalPrice));
        startActivity(intent);
        finish();
    }
});
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOrderStatus();
        final DatabaseReference cartref  = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
.setQuery(cartref.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Products"),Cart.class)
                .build();


        FirebaseRecyclerAdapter<Cart , CartViewholder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewholder holder, int position, @NonNull final Cart model) {
                int oneTypeTotalPrice = (Integer.valueOf(model.getPrice())) * (Integer.valueOf(model.getQuantity())) ;

                OverallTotalPrice = OverallTotalPrice + oneTypeTotalPrice;

                TotalAmount.setText("Total Price: " + String.valueOf(OverallTotalPrice) + "/-");
                holder.txttotPrice.setText("Total Price: " + String.valueOf(oneTypeTotalPrice));
                holder.txtproductquantity.setText("Quantity: " + model.getQuantity());
                holder.txtproductprice.setText("Price: " + model.getPrice());
                holder.txtproductname.setText("Name: " +model.getPname());

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CharSequence options[] = new CharSequence[]{
                               "Edit",
                                    "Remove"

                            };
                            AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i)
                            {
                            if(i == 0){
                               Intent intent = new Intent(CartActivity.this , ProductDetailsActivity.class);
                            intent.putExtra("pid",model.getPid());
                            startActivity(intent);
                            }
                            if(i == 1){
                                cartref.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                        .child("Products").child(model.getPid()).removeValue();

                                cartref.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                                        .child("Products").child(model.getPid()).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                           if(task.isSuccessful()){
                                               Toast.makeText(CartActivity.this , "Item removed" , Toast.LENGTH_SHORT).show();
                                               Intent intent = new Intent(CartActivity.this , UserProductHomeActivity.class);
                                               startActivity(intent);
                                           }
                                            }
                                        });
                            }

                            }

                        });
builder.show();

                        }
                    });
            }

            @NonNull
            @Override
            public CartViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);
                  CartViewholder holder = new CartViewholder(view);
                  return holder;

            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

private void checkOrderStatus(){
        DatabaseReference orderref = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());
 orderref.addValueEventListener(new ValueEventListener() {
     @Override
     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
         if(dataSnapshot.exists()){
             String shippingstate = dataSnapshot.child("state").getValue().toString();
             String Username = dataSnapshot.child("name").getValue().toString();

             if(shippingstate.equals("shipped")){
                TotalAmount.setText("Dear " + Username  + "\n Order is shipped Successfully!");
                recyclerView.setVisibility(View.GONE);
                TxtMsg1.setVisibility(View.VISIBLE);
                Nextbuttn.setVisibility(View.GONE);

                Toast.makeText(CartActivity.this , "Please wait! ",Toast.LENGTH_SHORT).show();
             }
             else if(shippingstate.equals("not shipped")){
                 TotalAmount.setText("Shipping State:     Not Shipped");
                 recyclerView.setVisibility(View.GONE);
                 TxtMsg1.setVisibility(View.VISIBLE);
                 Nextbuttn.setVisibility(View.GONE);

                 Toast.makeText(CartActivity.this , "Please wait! while Order is been prepared",Toast.LENGTH_LONG).show();

             }
         }
     }

     @Override
     public void onCancelled(@NonNull DatabaseError databaseError) {

     }
 });

}


}
