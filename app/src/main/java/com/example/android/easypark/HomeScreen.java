package com.example.android.easypark;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener{

    private String currentUserId;
    private String currentUserName;
    private DatabaseReference user;
    private UserInformation userInfo;

    private TextView userNameDisplay;
    private Button enterParking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        user = FirebaseDatabase.getInstance().getReference("Users");
        userNameDisplay = findViewById(R.id.usernameDisplay);
        enterParking = findViewById(R.id.enterParkingButton);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            currentUserId = extras.getString("currentUser");
        }

        enterParking.setOnClickListener(this);

        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                    System.out.println("----------------KEY-----------------------"+userSnapshot.getKey());
                    if(userSnapshot.getKey().equals(currentUserId)){
                        userInfo = userSnapshot.getValue(UserInformation.class);
                        currentUserName = userInfo.getName();
                        System.out.println("----------------USERNAME-----------------------" + currentUserName);
                        userNameDisplay.setText(currentUserName);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Put some code here, it's not compulsory though !!
            }
        });

    }

    @Override
    public void onClick(View view) {
        startIntent();
    }

    private void startIntent() {
        Intent towardsScanning = new Intent(HomeScreen.this, Qrcode.class);
        towardsScanning.putExtra("currentUserId", currentUserId);
        towardsScanning.putExtra("entry",1);
        towardsScanning.putExtra("exit",0);
        startActivity(towardsScanning);
    }
}
