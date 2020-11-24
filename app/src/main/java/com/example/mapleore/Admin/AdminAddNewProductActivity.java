package com.example.mapleore.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mapleore.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {
    private String CatagoryName;
    private ImageView ProductImage;
    private Button AddProduct;
    private EditText ProductName , ProductDescription , ProductPrice;
    private Uri imageUri;
    private static final int GalleryPick = 1;
    private String Pname , PDescription ,PPrice;
    private String SaveCurrentDate , SaveCurrentTime;
    private String ProductRandomKey;
    private StorageReference ProductImageRef;
    private String DownloadImageURL;
    private DatabaseReference productRef;
    private ProgressDialog LoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Product Image");
productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        CatagoryName = getIntent().getExtras().get("catagory").toString();

        AddProduct = (Button)findViewById(R.id.Add_new_Product);
        ProductName = (EditText)findViewById(R.id.product_name);
        ProductDescription = (EditText)findViewById(R.id.product_description);
        ProductPrice = (EditText)findViewById(R.id.product_price);
        ProductImage = (ImageView)findViewById(R.id.select_product_image);
        LoadingBar = new ProgressDialog(this);


ProductImage.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        OpenGallery();
    }
});

AddProduct.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        ValidateProductData();
    }
});
    }

    private void ValidateProductData() {
PDescription = ProductDescription.getText().toString();
        PPrice = ProductPrice.getText().toString();
        Pname = ProductName.getText().toString();

    if(imageUri == null){
        Toast.makeText(AdminAddNewProductActivity.this , "Product Image Is mandatory" ,Toast.LENGTH_SHORT).show();
    }

    else if(TextUtils.isEmpty(Pname)){
        Toast.makeText(AdminAddNewProductActivity.this , "Please, Write Product Name" ,Toast.LENGTH_SHORT).show();
    }
else if(TextUtils.isEmpty(PDescription)){
        Toast.makeText(AdminAddNewProductActivity.this , "Please, Write Product Description" ,Toast.LENGTH_SHORT).show();
    }

    else if(TextUtils.isEmpty(PPrice)){
        Toast.makeText(AdminAddNewProductActivity.this , "Please, Write Product Price" ,Toast.LENGTH_SHORT).show();
    }
else{
    StoreProductInformation();
    }

    }

    private void StoreProductInformation() {

        LoadingBar.setTitle("Adding new Product");
        LoadingBar.setMessage("Please wait, While we are Adding new Product...");
        LoadingBar.setCanceledOnTouchOutside(false);
        LoadingBar.show();

        Calendar calender = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd,yyyy");

        SaveCurrentDate = currentdate.format(calender.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");

        SaveCurrentTime = currentTime.format(calender.getTime());
        ProductRandomKey = SaveCurrentDate + SaveCurrentTime ;

final StorageReference filepath = ProductImageRef.child(imageUri.getLastPathSegment() + ProductRandomKey);
    final UploadTask uploadTask = filepath.putFile(imageUri);


    uploadTask.addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
       String message = e.toString() ;
       Toast.makeText(AdminAddNewProductActivity.this, "Error: " +message , Toast.LENGTH_SHORT).show();
        LoadingBar.dismiss();
        }
    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
       Toast.makeText(AdminAddNewProductActivity.this , "Image Uploaded SuccessFully...", Toast.LENGTH_SHORT).show();

            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
               if(!task.isSuccessful()){
                   throw task.getException();

               }
                DownloadImageURL = filepath.getDownloadUrl().toString();
               return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                 DownloadImageURL = task.getResult().toString();
                        Toast.makeText(AdminAddNewProductActivity.this , "Product Image URL Saved Successfully...",Toast.LENGTH_SHORT).show();
                     SaveProductInfoTODatabase();
                    }
                }
            });

        }
    });

    }

    private void SaveProductInfoTODatabase() {
        HashMap<String,Object> productMap = new HashMap<>();
        productMap.put("pid" , ProductRandomKey);
        productMap.put("date" ,SaveCurrentDate);
        productMap.put("time" , SaveCurrentTime);
        productMap.put("description" , PDescription);
        productMap.put("image" ,DownloadImageURL);
        productMap.put("catagory" , CatagoryName);
        productMap.put("price" , PPrice);
        productMap.put("name" , Pname);

        productRef.child(ProductRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                   if(task.isSuccessful()){
                       Intent intent = new Intent(AdminAddNewProductActivity.this, AdminCatagoryActivity.class);
                       startActivity(intent);

                       LoadingBar.dismiss();
                       Toast.makeText(AdminAddNewProductActivity.this , "Product Added Successfully." , Toast.LENGTH_SHORT).show();


                   }
                   else{
                       LoadingBar.dismiss();
                       String message = task.getException().toString();
                       Toast.makeText(AdminAddNewProductActivity.this , "Error: "+ message , Toast.LENGTH_SHORT).show();

                   }
                    }
                });

    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent , GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GalleryPick && resultCode == RESULT_OK && data!= null){
        imageUri = data.getData();
        ProductImage.setImageURI(imageUri);
        }
    }
}
