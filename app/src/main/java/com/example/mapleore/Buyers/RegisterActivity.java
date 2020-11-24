package com.example.mapleore.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mapleore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button CreateAccountButton;
    private EditText InputName , InputPhoneNumber , InputPassword;
    private ProgressDialog LoadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccountButton = (Button) findViewById(R.id.Create_Account_button);
        InputName = (EditText) findViewById(R.id.register_username_input);
        InputPhoneNumber = (EditText) findViewById(R.id.register_phonenumber_input);
        InputPassword = (EditText) findViewById(R.id.register_password_input);
        LoadingBar = new ProgressDialog(this);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });




}
    private void CreateAccount() {
        String name = InputName.getText().toString();
        String number = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();
        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"Please, Write your Name..",Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(number))
        {

            Toast.makeText(this,"Please, Write your Number..",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {

            Toast.makeText(this,"Please, Enter your Password..",Toast.LENGTH_SHORT).show();
        }
else{
       LoadingBar.setTitle("Create Account");
       LoadingBar.setMessage("Please wait. While we are checking the Credentials...");
       LoadingBar.setCanceledOnTouchOutside(false);
       LoadingBar.show();

       ValidatePhoneNumebr(name , number , password);
        }

    }

    private void ValidatePhoneNumebr(final String name, final String phone, final String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("Users").child(phone).exists()){

                    HashMap<String,Object> userdataMap = new HashMap<>();
                    userdataMap.put("Phone" , phone);
                    userdataMap.put("Name" , name);
                    userdataMap.put("Password" , password);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                               if(task.isSuccessful()){
                                   Toast.makeText(RegisterActivity.this,"Congratulation! Your Account has been Created.",Toast.LENGTH_SHORT).show();
                                   LoadingBar.dismiss();
                                   Intent intent = new Intent(RegisterActivity.this , LoginActivity.class);
                                   startActivity(intent);
                               }
                               else{
                                   LoadingBar.dismiss();
                                   Toast.makeText(RegisterActivity.this,"Network Error: Please try again!",Toast.LENGTH_SHORT).show();

                               }

                                }
                            });
                }
                else{

                    Toast.makeText(RegisterActivity.this,"This " + phone + " Already Exist!" , Toast.LENGTH_SHORT).show();
                      LoadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this,"Please Try using Differnt PhoneNumber" ,Toast.LENGTH_SHORT ).show();

                    Intent intent = new Intent(RegisterActivity.this , MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    };




}




