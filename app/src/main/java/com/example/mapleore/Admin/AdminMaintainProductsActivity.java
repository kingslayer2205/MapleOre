package com.example.mapleore.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mapleore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {
private Button applychanges , deletebttn;
private EditText name ,price ,description ;
private ImageView imageView;
private String productId = "";

private DatabaseReference productsref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        productId = getIntent().getStringExtra("pid");

        deletebttn =(Button)findViewById(R.id.Maintain_delete_bttn);

applychanges =(Button)findViewById(R.id.Maintain_apply_changes);
name = (EditText)findViewById(R.id.Maintain_Product_layout_name);
        price = (EditText)findViewById(R.id.Maintain_Product_layout_price);
        description = (EditText)findViewById(R.id.Maintain_Product_layout_Description);
        imageView = (ImageView) findViewById(R.id.Maintain_Product_layout_image);

        productsref = FirebaseDatabase.getInstance().getReference().child("Products").child(productId);

        displayspecificproductsinfo();

    applychanges.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            applynewchanges();
        }
    });

    deletebttn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            deletethisproduct();
        }
    });

    }

    private void deletethisproduct() {

productsref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
    @Override
    public void onComplete(@NonNull Task<Void> task) {
   if (task.isSuccessful()){

  Intent intent = new Intent(AdminMaintainProductsActivity.this , AdminCatagoryActivity.class);
  startActivity(intent);
  finish();
       Toast.makeText(AdminMaintainProductsActivity.this ,"Product Deleted Successfully" ,Toast.LENGTH_SHORT).show();

   }
    }
});
    }

    private void applynewchanges() {
String Nname = name.getText().toString();
        String Nprice = price.getText().toString();
        String Ndescription = description.getText().toString();
if(Nname.equals("")){
    Toast.makeText(AdminMaintainProductsActivity.this , "Please Enter Name" , Toast.LENGTH_SHORT).show();
}
else if(Nprice.equals("")){
    Toast.makeText(AdminMaintainProductsActivity.this , "Please Enter Price" , Toast.LENGTH_SHORT).show();
}
else if(Ndescription.equals("")){
    Toast.makeText(AdminMaintainProductsActivity.this , "Please Enter Description" , Toast.LENGTH_SHORT).show();
}
else {
    HashMap<String,Object> productMap = new HashMap<>();
    productMap.put("pid" , productId);
    productMap.put("description" , Ndescription);
    productMap.put("price" , Nprice);
    productMap.put("name" , Nname);

    productsref.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
       if(task.isSuccessful()){
           Toast.makeText(AdminMaintainProductsActivity.this ,"Changes Applied Successfully",Toast.LENGTH_SHORT).show();

           Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminCatagoryActivity.class);
           startActivity(intent);
           finish();

       }

        }
    });

}

    }

    private void displayspecificproductsinfo() {
productsref.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
if(dataSnapshot.exists()){
    String pname = dataSnapshot.child("name").getValue().toString();
    String pprice = dataSnapshot.child("price").getValue().toString();
    String pdescription = dataSnapshot.child("description").getValue().toString();
    String pimage = dataSnapshot.child("image").getValue().toString();

name.setText(pname);
price.setText(pprice);
description.setText(pdescription);
    Picasso.get().load(pimage).into(imageView);
}
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});

    }

}
