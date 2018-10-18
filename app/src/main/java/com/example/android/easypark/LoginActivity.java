package com.example.android.easypark;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private EditText password;
    private Button logInButton;

    private String emailId;
    private String userPassword;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        logInButton = findViewById(R.id.email_log_in_button);

        progressDialog = new ProgressDialog(this);

        logInButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        emailId = email.getText().toString().trim();
        userPassword = password.getText().toString().trim();

        if(TextUtils.isEmpty(emailId) || TextUtils.isEmpty(userPassword)){
            Toast.makeText(this,"Enter the Credentials...", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Signing In....");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(emailId, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressDialog.dismiss();
                        if(task.isSuccessful()) {
                            firebaseUser = firebaseAuth.getCurrentUser();
                            System.out.println("----------------CURRENT USER ID -----------" + firebaseUser.getUid());
                            startIntent();
                        }
                    }
                });
    }

    private void startIntent() {

        Intent openHomeScreen = new Intent(LoginActivity.this, HomeScreen.class);
        openHomeScreen.putExtra("currentUser",firebaseUser.getUid());
        startActivity(openHomeScreen);
    }

}

