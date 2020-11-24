package com.example.mapleore.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mapleore.Prevalent.Prevalent;
import com.example.mapleore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFonalOrderActivity extends AppCompatActivity {
private EditText name , phonenumber , address , city;
private Button confirm;
private String totalamount = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_fonal_order);
    name = (EditText)findViewById(R.id.shippment_name);
        phonenumber = (EditText)findViewById(R.id.shippment_phone_number);
        address = (EditText)findViewById(R.id.shippment_address);
        city = (EditText)findViewById(R.id.shippment_city);
        confirm = (Button) findViewById(R.id.shippment_Confirm_buttn);

totalamount = getIntent().getStringExtra("Total Price");

confirm.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
            check();
    }
});
    }

    private void check() {
    if(TextUtils.isEmpty(name.getText().toString())){
        Toast.makeText(ConfirmFonalOrderActivity.this , "Please Enter the name" , Toast.LENGTH_SHORT).show();
    }
else if(TextUtils.isEmpty(phonenumber.getText().toString())){
        Toast.makeText(ConfirmFonalOrderActivity.this , "Please Enter the PhoneNumber" , Toast.LENGTH_SHORT).show();
    }
    else if(TextUtils.isEmpty(address.getText().toString())){
        Toast.makeText(ConfirmFonalOrderActivity.this , "Please Enter the address" , Toast.LENGTH_SHORT).show();
    }
    else if(TextUtils.isEmpty(city.getText().toString())){
        Toast.makeText(ConfirmFonalOrderActivity.this , "Please Enter the city" , Toast.LENGTH_SHORT).show();
    }
else{
    ConfirmOrder();
    }


    }

    private void ConfirmOrder()
    {

       final String savecurrenttime , savecurrentdate;

        Calendar CalforDate = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd,yyyy");
        savecurrentdate = currentdate.format(CalforDate.getTime());
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime = currenttime.format(CalforDate.getTime());

final DatabaseReference orderref = FirebaseDatabase.getInstance().getReference().child("Orders")
        .child(Prevalent.currentOnlineUser.getPhone());

        HashMap<String ,Object> orderMap = new HashMap<>();

        orderMap.put("totalamount",totalamount);
        orderMap.put("name",name.getText().toString());
        orderMap.put("phone",phonenumber.getText().toString());
        orderMap.put("address",address.getText().toString());
        orderMap.put("city",city.getText().toString());
        orderMap.put("date",savecurrentdate);
        orderMap.put("time",savecurrenttime);
        orderMap.put("state","not shipped");

        orderref.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View")
                            .child(Prevalent.currentOnlineUser.getPhone()).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {


                               if(task.isSuccessful()){
                                   Toast.makeText(ConfirmFonalOrderActivity.this , "Final Order Placed" , Toast.LENGTH_SHORT).show();
                                   Intent intent = new Intent(ConfirmFonalOrderActivity.this , UserProductHomeActivity.class);
                                   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // so that user cant come back again
                                   startActivity(intent);
finish();
                               }
                                }
                            });
                }
            }
        });
    }
}
