package com.example.mapleore.Buyers;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapleore.Prevalent.Prevalent;
import com.example.mapleore.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView ProfileImageView;
    private EditText SettingPhoneNumber , SettingName ,  SettingAddress;
    private TextView SettingClose , SettingUpdate , SettingChangeProfile;
    private StorageTask uploadtask;
    private Uri imageUri;
    private String myUrl = "";
    private StorageReference StorageProfilePictureref;
    private String Checker = "";
    private Button security;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ProfileImageView = (CircleImageView)findViewById(R.id.Setting_profile_Image);
        SettingPhoneNumber = (EditText) findViewById(R.id.Settings_phone_number);
        SettingName = (EditText)findViewById(R.id.Settings_name);
        SettingAddress = (EditText)findViewById(R.id.Settings_address);
        SettingClose = (TextView) findViewById(R.id.close_settings);
        SettingUpdate = (TextView) findViewById(R.id.update_settings);
        SettingChangeProfile = (TextView) findViewById(R.id.profile_Change_settings);
security = (Button) findViewById(R.id.Sequrity_questions_buttn);

        StorageProfilePictureref = FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        UserInfoDisplay(ProfileImageView , SettingName , SettingName , SettingAddress);

    SettingClose.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    });

SettingUpdate.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(Checker.equals("clicked"))
        {
               UserInfoSaved();
        }
        else
        {
                UpdateOnlyuserinfo();
        }
    }
});

SettingChangeProfile.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Checker = "clicked";

        CropImage.activity(imageUri).setAspectRatio(1,1)
                .start(SettingsActivity.this);
    }
});

security.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SettingsActivity.this , ResetPasswordActivity.class);
        intent.putExtra("setting" , "check");
        startActivity(intent);
    }
});

    }

    private void UpdateOnlyuserinfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String,Object> userMap = new HashMap<String,Object>();
        userMap.put("Name",SettingName.getText().toString());
        userMap.put("Address",SettingAddress.getText().toString());
        userMap.put("PhoneOrder",SettingPhoneNumber.getText().toString());

        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);
        startActivity(new Intent(SettingsActivity.this , UserProductHomeActivity.class));
        Toast.makeText(SettingsActivity.this , "Profile Info Updated Successfully" ,Toast.LENGTH_SHORT).show();
        finish();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
   if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data!=null){
       CropImage.ActivityResult result = CropImage.getActivityResult(data);
       imageUri =result.getUri();

       ProfileImageView.setImageURI(imageUri);
   }
   else{
       Toast.makeText(SettingsActivity.this , "Error: Try Again" , Toast.LENGTH_SHORT ).show();
startActivity(new Intent(SettingsActivity.this , SettingsActivity.class));
finish();
   }

    }



    private void UserInfoSaved() {
if(TextUtils.isEmpty(SettingName.getText().toString())){
    Toast.makeText(SettingsActivity.this , "Name is Mendatory" , Toast.LENGTH_SHORT ).show();
}
else if(TextUtils.isEmpty(SettingPhoneNumber.getText().toString())){
    Toast.makeText(SettingsActivity.this , "Phonenumber is Mendatory" , Toast.LENGTH_SHORT ).show();
}
        else if(TextUtils.isEmpty(SettingAddress.getText().toString())){
            Toast.makeText(SettingsActivity.this , "Address is Mendatory" , Toast.LENGTH_SHORT ).show();
        }
else if(Checker.equals("clicked")){

    uploadImage();
}


    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, While we are Updating your account Info");
progressDialog.setCanceledOnTouchOutside(false);
progressDialog.show();

if(imageUri != null){
    final StorageReference fileref = StorageProfilePictureref.child(Prevalent.currentOnlineUser.getPhone() + ".jpg");
uploadtask = fileref.putFile(imageUri);

uploadtask.continueWithTask(new Continuation() {
    @Override
    public Object then(@NonNull Task task) throws Exception {
   if(!task.isSuccessful()){
       throw  task.getException();
   }
        return fileref.getDownloadUrl();
    }
}).addOnCompleteListener(new OnCompleteListener<Uri>() {
    @Override
    public void onComplete(@NonNull Task<Uri> task) {
if(task.isSuccessful()){
    Uri downloadUrl = task.getResult();
    myUrl = downloadUrl.toString();

DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
    HashMap<String,Object> userMap = new HashMap<String,Object>();
    userMap.put("Name",SettingName.getText().toString());
    userMap.put("Address",SettingAddress.getText().toString());
    userMap.put("PhoneOrder",SettingPhoneNumber.getText().toString());
    userMap.put("Image",myUrl);
    ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

    progressDialog.dismiss();

    startActivity(new Intent(SettingsActivity.this , UserProductHomeActivity.class));
    Toast.makeText(SettingsActivity.this , "Profile Info Updated Successfully" ,Toast.LENGTH_SHORT).show();
    finish();

}
else{
    progressDialog.dismiss();
    Toast.makeText(SettingsActivity.this , "Error" ,Toast.LENGTH_SHORT).show();

}


    }
});
}
else{
    Toast.makeText(SettingsActivity.this , "Image not Selected" ,Toast.LENGTH_SHORT).show();

}

    }

    private void UserInfoDisplay(final CircleImageView profileImageView, EditText settingName, EditText settingName1, EditText settingAddress) {
        DatabaseReference Usersref = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
Usersref.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if(dataSnapshot.child("Image").exists()){
            String image = dataSnapshot.child("Image").getValue().toString();
            String name = dataSnapshot.child("Name").getValue().toString();
            String phone = dataSnapshot.child("Phone").getValue().toString();
            String address = dataSnapshot.child("Address").getValue().toString();

            Picasso.get().load(image).into(profileImageView);
            SettingName.setText(name);
            SettingPhoneNumber.setText(phone);
            SettingAddress.setText(address);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
    }
}
