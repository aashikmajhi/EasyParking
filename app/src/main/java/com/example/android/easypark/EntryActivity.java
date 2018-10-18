package com.example.android.easypark;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EntryActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView userName;
    private TextView parkingArea;
    private Button exitParking;
    private String currentUserId;
    private String currentUserName;
    private String parkingPlace;
    private UserInformation userInfo;
    private String entryTime;
    private String exitTime;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        userName = findViewById(R.id.userName);
        parkingArea = findViewById(R.id.parkingPlaceDisplay);
        exitParking = findViewById(R.id.exitParkingButton);

        exitParking.setOnClickListener(this);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                    System.out.println("----------------KEY-----------------------"+userSnapshot.getKey());
                    if(userSnapshot.getKey().equals(currentUserId)){
                        userInfo = userSnapshot.getValue(UserInformation.class);
                        currentUserName = userInfo.getName();
                        System.out.println("----------------USERNAME-----------------------" + currentUserName);
                        userName.setText(currentUserName);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            currentUserId = extras.getString("currentUserId");
            parkingPlace = extras.getString("parkingPlace");
            entryTime = extras.getString("entryTime");
        }

        parkingArea.setText(parkingPlace);
    }

    @Override
    public void onClick(View view) {
        sendIntent();
    }

    private void sendIntent() {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        exitTime = format.format(cal.getTime());

        Intent towardsScanning = new Intent(EntryActivity.this, PayActivity.class);
        towardsScanning.putExtra("currentUserId", currentUserId);
        towardsScanning.putExtra("entryTime", entryTime);
        towardsScanning.putExtra("exitTime", exitTime);
        startActivity(towardsScanning);
    }
}
