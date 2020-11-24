package com.example.mapleore.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.mapleore.Model.Products;
import com.example.mapleore.Prevalent.Prevalent;
import com.example.mapleore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {
private FloatingActionButton addtocartfloatingbuttn;
private ImageView ProductImage;
private ElegantNumberButton numberButton;
private TextView Productname , ProductPrice , ProductDescription;
private String ProductId = "" , state = "normal";
private Button AddtocartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        ProductId = getIntent().getStringExtra("pid");
        addtocartfloatingbuttn = (FloatingActionButton)findViewById(R.id.Add_product_to_cart_Floating_buttn);
        numberButton = (ElegantNumberButton)findViewById(R.id.number_button_details);
        ProductImage = (ImageView)findViewById(R.id.Product_image_details);
        Productname = (TextView) findViewById(R.id.Product_name_details);
        ProductDescription = (TextView) findViewById(R.id.Product_Description_details);
        ProductPrice = (TextView) findViewById(R.id.Product_Price_details);
        AddtocartButton = (Button)findViewById(R.id.Add_product_to_cart_buttn);

        getProductDetails(ProductId);

   AddtocartButton.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           if(state.equals("Order Placed")){
               Toast.makeText(ProductDetailsActivity.this , "Waiting for Products to get Shipped/Confirmed",Toast.LENGTH_LONG).show();
           }
else{
               addingToCartList();

           }
       }
   });

   addtocartfloatingbuttn.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           Intent intent =new Intent(ProductDetailsActivity.this , CartActivity.class);
           startActivity(intent);
       }
   });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOrderStatus();
    }

    private void addingToCartList() {

        String savecurrenttime , savecurrentdate;

        Calendar CalforDate = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd,yyyy");
        savecurrentdate = currentdate.format(CalforDate.getTime());
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime = currenttime.format(CalforDate.getTime());

final DatabaseReference cartlistRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
final HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("pid",ProductId);
        cartMap.put("pname",Productname.getText().toString());
        cartMap.put("price",ProductPrice.getText().toString());
        cartMap.put("date",savecurrentdate);
        cartMap.put("time",savecurrenttime);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount","");

        cartlistRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone()).child("Products").child(ProductId)
                .updateChildren(cartMap);




        cartlistRef.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Products").child(ProductId)
.updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
    @Override
    public void onComplete(@NonNull Task<Void> task) {
      if(task.isSuccessful()){

          cartlistRef.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Products").child(ProductId)
                  .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
             if(task.isSuccessful()){
                 Toast.makeText(ProductDetailsActivity.this ,"Added To the Cart List" , Toast.LENGTH_SHORT).show();

                 Intent intent = new Intent(ProductDetailsActivity.this, UserProductHomeActivity.class);
             startActivity(intent);
             }
              }
          });


          }
    }
});

    }

    private void getProductDetails(String productId) {
        DatabaseReference Productref = FirebaseDatabase.getInstance().getReference().child("Products");
        Productref.child(ProductId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    Products products = dataSnapshot.getValue(Products.class);
                    Productname.setText(products.getName());
                    ProductDescription.setText(products.getDescription());
                    ProductPrice.setText(products.getPrice());
                    Picasso.get().load(products.getImage()).into(ProductImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
                        state = "Order Shipped";

                    }
                    else if(shippingstate.equals("not shipped")){
                        state = "Order Placed";


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}
