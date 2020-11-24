package com.example.mapleore.Buyers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapleore.Admin.AdminMaintainProductsActivity;
import com.example.mapleore.Model.Products;
import com.example.mapleore.Prevalent.Prevalent;
import com.example.mapleore.R;
import com.example.mapleore.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class UserProductHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference ProductRef;

private String type = "";
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_product_home);


        Intent intent =getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null){
            type = getIntent().getExtras().get("Admin").toString();
        }

        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");
        Paper.init(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("HOME");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();

                if(!type.equals("Admin")){
                    Intent intent =new Intent(UserProductHomeActivity.this , CartActivity.class);
                    startActivity(intent);
                 }


            }
        });

         drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerview = navigationView.getHeaderView(0);
        TextView usernametextview = headerview.findViewById(R.id.User_profile_name);
        CircleImageView profileimageview = headerview.findViewById(R.id.user_profile_image);

        if(!type.equals("Admin")){

            usernametextview.setText("Hey! " + Prevalent.currentOnlineUser.getName());
            Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileimageview);

        }
        if(type.equals("Admin")){

            usernametextview.setText("Welcome! Admin");

        }

        recyclerView = findViewById(R.id.Recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_product_home, menu);
        return true;
    }



    @Override
  protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions
                        .Builder<Products>().setQuery(ProductRef,Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter
                = new FirebaseRecyclerAdapter<Products,ProductViewHolder>(options) {
            @Override

            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int i, @NonNull final Products products) {

                holder.txtProductanme.setText(products.getName());
                holder.txtProductDescription.setText(products.getDescription());
                holder.txtProductPrice.setText("Price =" + products.getPrice() + "/-");
                Picasso.get().load(products.getImage()).into(holder.imageView);


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {

                        if(type.equals("Admin")){


                            Intent intent = new Intent(UserProductHomeActivity.this, AdminMaintainProductsActivity.class);
                            intent.putExtra("pid", products.getPid());
                            startActivity(intent);
                        }
                        else {

                            Intent intent = new Intent(UserProductHomeActivity.this, ProductDetailsActivity.class);
                            intent.putExtra("pid", products.getPid());
                            startActivity(intent);
                        }
                    }
                });

            }
            @NonNull

            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_item_layout,parent,false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cart) {

            if(!type.equals("Admin")){
                Intent intent =new Intent(UserProductHomeActivity.this , CartActivity.class);
                startActivity(intent);
            }
        }
        else if (id == R.id.nav_search) {
            if(!type.equals("Admin")){
                startActivity(new Intent(UserProductHomeActivity.this , SearchProductsActivity.class));
            }


        }
        else if (id == R.id.nav_catagory) {

        }
        else if (id == R.id.nav_setting) {
            if(!type.equals("Admin")){
                Intent intent = new Intent(UserProductHomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }

        }
        else if (id == R.id.nav_logout) {
            if(!type.equals("Admin")){
                Paper.book().destroy();

                Intent intent = new Intent(UserProductHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity(intent);
            }


        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {


    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    if(drawer.isDrawerOpen(GravityCompat.START)){
    drawer.closeDrawer(GravityCompat.START);
    }
    else{
    if(!type.equals("Admin")){Toast.makeText(UserProductHomeActivity.this , "Logout First" , Toast.LENGTH_SHORT).show();
    }
    else {
   super.onBackPressed();
    }
    }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
