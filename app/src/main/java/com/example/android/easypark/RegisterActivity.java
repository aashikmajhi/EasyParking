package com.example.android.easypark;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button registerButton;
    private EditText email;
    private EditText name;
    private EditText password;
    private EditText mobileNumber;
    private EditText vehicleNumber;

    private UserInformation userInformation;
    private ProgressDialog progressDialog;

    private FirebaseUser mFirebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Firebase.setAndroidContext(this);

        databaseRef = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        registerButton = findViewById(R.id.email_sign_in_button);
        email =  findViewById(R.id.id_r_email);
        password = findViewById(R.id.id_r_password);
        name = findViewById(R.id.id_r_name);
        mobileNumber =  findViewById(R.id.id_r_mnumber);
        vehicleNumber = findViewById(R.id.id_r_vnumber);
        progressDialog = new ProgressDialog(this);

        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        registerUser();
    }

    private void registerUser() {
        final String emailId = email.getText().toString().trim();
        final String userPassword = password.getText().toString().trim();
        final String mobileNum = mobileNumber.getText().toString().trim();
        final String vehicleNum = vehicleNumber.getText().toString().trim();
        final String userName = name.getText().toString().trim();

        Log.i("---------EMAIL----- :-",emailId);
        Log.i("-----Password---- :",userPassword);

        if(TextUtils.isEmpty(emailId)){
            Toast.makeText(this,"Please Enter your EMAIL ADDRESS !",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(userPassword)){
            Toast.makeText(this,"Please Enter your PASSWORD !",Toast.LENGTH_SHORT).show();
            return;
        }


        if(TextUtils.isEmpty(mobileNum)){
            Toast.makeText(this,"Please Enter your MOBILE NUMBER !",Toast.LENGTH_SHORT).show();
            return;
        }


        if(TextUtils.isEmpty(vehicleNum)){
            Toast.makeText(this,"Please Enter your VEHICLE'S NUMBER !",Toast.LENGTH_SHORT).show();
            return;
        }


        if(TextUtils.isEmpty(userName)){
            Toast.makeText(this,"Please Enter your NAME !",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(emailId,userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this,"Registred Successfully !!",Toast.LENGTH_SHORT).show();

                            userInformation = new UserInformation(emailId, userPassword,mobileNum,vehicleNum, userName);
                            progressDialog.dismiss();

                            mFirebaseUser = firebaseAuth.getCurrentUser();
                            databaseRef.child("Users").child(mFirebaseUser.getUid()).setValue(userInformation);
                            Toast.makeText(RegisterActivity.this,"Info Added Successfully !!",Toast.LENGTH_SHORT).show();

                            startIntent();
                        }
                        else{
                            Toast.makeText(RegisterActivity.this,"Unsuccessful Registration!!",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            return;
                        }
                    }
                });

    }

    private void startIntent() {

        Intent openHomeScreen = new Intent(RegisterActivity.this, HomeScreen.class);
        openHomeScreen.putExtra("currentUser",mFirebaseUser.getUid());
        startActivity(openHomeScreen);
    }

}
