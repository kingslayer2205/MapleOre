package com.example.mapleore.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mapleore.Buyers.MainActivity;
import com.example.mapleore.R;
import com.example.mapleore.Buyers.UserProductHomeActivity;

public class AdminCatagoryActivity extends AppCompatActivity {
private ImageView tshirt , sportsTshirt , femaledress;
private Button checkorderbuttn , logoutbuttn , maintainOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_catagory);

        checkorderbuttn = (Button)findViewById(R.id.check_orders_bttn);
        logoutbuttn = (Button)findViewById(R.id.admin_logout_button);
        maintainOrder = (Button)findViewById(R.id.check_maintain_order);

        tshirt = (ImageView)findViewById(R.id.tshirt);
        sportsTshirt = (ImageView)findViewById(R.id.Sports_tshirt);
        femaledress = (ImageView)findViewById(R.id.Female_dressess11);

        tshirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCatagoryActivity.this , AdminAddNewProductActivity.class);
                intent.putExtra("catagory","tShirts");
                startActivity(intent);
            }

        });
    sportsTshirt.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(AdminCatagoryActivity.this , AdminAddNewProductActivity.class);
            intent.putExtra("catagory","SportsTShirts");
            startActivity(intent);
        }
    });
        femaledress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCatagoryActivity.this , AdminAddNewProductActivity.class);
                intent.putExtra("catagory","Female Dresses");
                startActivity(intent);
            }
        });

   logoutbuttn.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           Intent intent = new Intent(AdminCatagoryActivity.this , MainActivity.class);
           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
           startActivity(intent);
           finish();
       }
   });

checkorderbuttn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(AdminCatagoryActivity.this , AdminOrdersActivity.class);
        startActivity(intent);



    }
});

maintainOrder.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(AdminCatagoryActivity.this , UserProductHomeActivity.class);
        intent.putExtra("Admin", "Admin");
        startActivity(intent);

    }
});

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(AdminCatagoryActivity.this , "Can't Go Back, Logout!" , Toast.LENGTH_SHORT).show();
    }
}
