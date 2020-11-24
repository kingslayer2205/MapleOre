package com.example.mapleore.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapleore.Admin.AdminCatagoryActivity;
import com.example.mapleore.Model.Users;
import com.example.mapleore.Prevalent.Prevalent;
import com.example.mapleore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
private EditText InputNumber ,InputPassword;
private Button LoginButton;
private ProgressDialog LoadingBar;
private String ParentDbName = "Users";
private CheckBox rememberme;
private TextView AdminLink , NotAdminLink , forgetpassword;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton = (Button)findViewById(R.id.login_button);
        InputNumber = (EditText)findViewById(R.id.login_phonenumber_input);
        InputPassword = (EditText) findViewById(R.id.login_password_input);
        AdminLink = (TextView) findViewById(R.id.admin_login_link);
        NotAdminLink = (TextView) findViewById(R.id.not_admin_login_link);
        rememberme = (CheckBox)findViewById(R.id.login_checkbox);
        Paper.init(this);

        LoadingBar = new ProgressDialog(this);
forgetpassword = (TextView)findViewById(R.id.forget_password_link);

        AdminLink.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         LoginButton.setText("Login Admin");
         AdminLink.setVisibility(View.INVISIBLE);
         NotAdminLink.setVisibility(View.VISIBLE);
         ParentDbName = "admin";
     }
 });

 NotAdminLink.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         LoginButton.setText("Login");
         AdminLink.setVisibility(View.VISIBLE);
         NotAdminLink.setVisibility(View.INVISIBLE);
         ParentDbName = "Users";
     }
 });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });


forgetpassword.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(LoginActivity.this , ResetPasswordActivity.class);
       // intent.putExtra("login" , "check");
        startActivity(intent);
    }
});


                }


    private void LoginUser() {

        String phone   = InputNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if(TextUtils.isEmpty(phone))
        {

            Toast.makeText(this,"Please, Write your Number..",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {

            Toast.makeText(this,"Please, Enter your Password..",Toast.LENGTH_SHORT).show();
        }
        else{
            LoadingBar.setTitle("Login Account");
            LoadingBar.setMessage("Please wait. While we are checking the Credentials...");
            LoadingBar.setCanceledOnTouchOutside(false);
            LoadingBar.show();

            AllowAccessToAccount(phone , password);
        }







    }

    private void AllowAccessToAccount(final String phone, final String password) {
        if(rememberme.isChecked() && ParentDbName.equals("Users")){
            Paper.book().write(Prevalent.UserPhoneKey , phone);
            Paper.book().write(Prevalent.UserPasswordKey , password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                    if(dataSnapshot.child(ParentDbName).child(phone).exists())
                {
                    Users userData = dataSnapshot.child(ParentDbName).child(phone).getValue(Users.class);

                    assert userData != null;
                    if(userData.getPhone().equals(phone)){
                        if(userData.getPassword().equals(password)){

                            if(ParentDbName.equals("admin")){
                                Toast.makeText(LoginActivity.this,"Welcome Admin, You Are Logged In Successfully...",Toast.LENGTH_SHORT).show();
                                LoadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, AdminCatagoryActivity.class);
                                startActivity(intent);

                            }
                            else if(ParentDbName.equals("Users")){
                                Toast.makeText(LoginActivity.this,"Logged In Successfully...",Toast.LENGTH_SHORT).show();
                                LoadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, UserProductHomeActivity.class);
                                Prevalent.currentOnlineUser = userData;
                                startActivity(intent);

                            }

                        }
                        else{
                            LoadingBar.dismiss();
                            Toast.makeText(LoginActivity.this,"Incorrect Password",Toast.LENGTH_SHORT).show();

                        }
                    }

                }
                else
                {
                Toast.makeText(LoginActivity.this,"Account with this "+ phone + " do not Exist." , Toast.LENGTH_SHORT).show();
                LoadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
