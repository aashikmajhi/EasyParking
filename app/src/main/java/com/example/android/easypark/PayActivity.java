package com.example.android.easypark;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PayActivity extends AppCompatActivity  implements View.OnClickListener{

    TextView entTime ;
    TextView exTime;
    TextView cost;
    Button payButton;
    
    String currentUserId;
    String entryTime;
    String exitTime;
    String costFare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        entTime = findViewById(R.id.entryTime);
        exTime = findViewById(R.id.exitTime);
        cost = findViewById(R.id.cost);
        payButton = findViewById(R.id.exitParkingButton);
        
        payButton.setOnClickListener(this);
        
        Bundle extras = getIntent().getExtras();

        if(extras != null){
            currentUserId = extras.getString("currentUserId");
            entryTime = extras.getString("entryTime");
            exitTime = extras.getString("exitTime");
        }

        costFare = ParkingLogInfo.calculateCost(entryTime, exitTime);

        entTime.setText(entryTime);
        exTime.setText(exitTime);
        cost.setText(costFare);
    }

    @Override
    public void onClick(View view) {
        sendIntent();
    }

    private void sendIntent() {

        Intent i = new Intent(PayActivity.this,FinalActivity.class);
        startActivity(i);
    }
}
