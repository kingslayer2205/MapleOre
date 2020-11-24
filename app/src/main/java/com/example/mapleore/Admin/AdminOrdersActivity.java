package com.example.mapleore.Admin;

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

import com.example.mapleore.Model.AdminOrders;
import com.example.mapleore.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminOrdersActivity extends AppCompatActivity {
private RecyclerView ordersList;
private DatabaseReference ordersref;
    private DatabaseReference Adminref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_orders);

        Adminref = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View");
        ordersref = FirebaseDatabase.getInstance().getReference().child("Orders");
        ordersList = (RecyclerView)findViewById(R.id.Orders_list);

ordersList.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersref,AdminOrders.class).build();

        FirebaseRecyclerAdapter<AdminOrders,AdminOrderViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, AdminOrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrderViewHolder holder, final int position, @NonNull final AdminOrders model) {
                holder.Username.setText("Name: "+ model.getName());
                holder.Userphone.setText("Phone: "+ model.getPhone());
                holder.UsertotalPrice.setText("Total Amount: "+ model.getTotalamount());
                holder.UserDatetime.setText("Ordered At: "+ model.getDate() + " " + model.getTime());
                holder.UserShippingAddress.setText("Address: "+ model.getAddress() + ", "+model.getCity());
                holder.ShowOrder_bttn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String UID = getRef(position).getKey();

        Intent intent = new Intent(AdminOrdersActivity.this , AdminUserProductsActivity.class);
      intent.putExtra("uid" , UID);
        startActivity(intent);
    }
});
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]{
                                "Yes",
                                "No"
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminOrdersActivity.this);
                        builder.setTitle("Have you Shipped this Order");

builder.setItems(options, new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int i) {
   if(i==0){
       String Uid = getRef(position).getKey();
       RemoveOrder(Uid);
   }
   else{
       finish();
   }
    }
});

builder.show();                    }


                });

            }

            @NonNull
            @Override
            public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout , parent ,false);
               return new AdminOrderViewHolder(view);
            }
        };

    ordersList.setAdapter(adapter);
    adapter.startListening();
    }

    private void RemoveOrder(String uid) {
Adminref.child(uid).removeValue();
    ordersref.child(uid).removeValue();
    }

    public static class AdminOrderViewHolder extends RecyclerView.ViewHolder{

public TextView Username , Userphone , UsertotalPrice , UserDatetime , UserShippingAddress;
public Button ShowOrder_bttn;
    public AdminOrderViewHolder(@NonNull View itemView) {

        super(itemView);

        Username = (TextView)itemView.findViewById(R.id.Order_User_name);
        Userphone = (TextView)itemView.findViewById(R.id.Order_phoneNumber);
        UsertotalPrice = (TextView)itemView.findViewById(R.id.Order_product_price);
        UserDatetime = (TextView)itemView.findViewById(R.id.Order_time);
        UserShippingAddress = (TextView)itemView.findViewById(R.id.Order_address);
        ShowOrder_bttn = (Button) itemView.findViewById(R.id.Order_show_order);

    }
}


}
