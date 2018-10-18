package com.example.android.easypark;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class Qrcode extends AppCompatActivity {

    SurfaceView cp;
    TextView txt;
    TextView ctime;
    BarcodeDetector bar;
    CameraSource cameraSource;
    final int RequestCameraPermissionId = 1001;
    String currentUserId;
    String currentUserName;
    String status;
    String previousTime, time;
    DatabaseReference userDatabaseRef;
    ParkingLogInfo parkingLogInfo;
    ArrayList<String> validQrCodes = new ArrayList<>(Arrays.asList("Alpha Mall", "Gota Park", "Law Garden"));

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionId: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    try{
                    cameraSource.start(cp.getHolder());
                }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        cp = (SurfaceView) findViewById(R.id.cameraPreview);
        txt = (TextView) findViewById(R.id.txtResult);
        ctime = (TextView)findViewById(R.id.time);

        bar = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(this, bar).setRequestedPreviewSize(640, 480).build();

        userDatabaseRef = FirebaseDatabase.getInstance().getReference();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            currentUserId = extras.getString("currentUserId");

            if(extras.getInt("entry")==1){
                status = "Entry";
            }
            else{
                status = "Exit";
            }
        }

        //AddEvent

        cp.getHolder().addCallback(new SurfaceHolder.Callback() {
            @SuppressLint("MissingPermission")
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //request runtime permission
                    ActivityCompat.requestPermissions(Qrcode.this,new String[]{android.Manifest.permission.CAMERA},
                            RequestCameraPermissionId);
                    return;
                }
                try {
                    cameraSource.start(cp.getHolder());
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                 cameraSource.stop();
            }
        });

        bar.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if(qrcodes.size() != 0){
                    txt.post(new Runnable() {
                        @Override
                        public void run() {
                            //Create Vibrate
                            Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(300);
                           // System.out.println("-----------------QRCODE------------" + qrcodes.valueAt(0).displayValue);
                            if(validQrCodes.contains(qrcodes.valueAt(0).displayValue)){
                                txt.setText(qrcodes.valueAt(0).displayValue);
                                Calendar cal = Calendar.getInstance();
                                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                                time = format.format(cal.getTime());
                                ctime.setText(previousTime);
                                ctime.setVisibility(View.VISIBLE);


                                if(status.equals("Entry")){

                                    previousTime = time;

                                    parkingLogInfo = new ParkingLogInfo(status,time,qrcodes.valueAt(0).displayValue,"0");

                                    userDatabaseRef.child("Users").child(currentUserId).child("ParkingLogs").child(currentUserId+time).setValue(parkingLogInfo);

                                    userDatabaseRef.child("motor_status").setValue(1);

                                    Intent i = new Intent(Qrcode.this, EntryActivity.class);
                                    i.putExtra("currentUserId", currentUserId);
                                    i.putExtra("parkingPlace",qrcodes.valueAt(0).displayValue );
                                    i.putExtra("entryTime", time);
                                    startActivity(i);

                                }else{

                                    parkingLogInfo = new ParkingLogInfo(status,time,qrcodes.valueAt(0).displayValue,ParkingLogInfo.calculateCost(previousTime, time));

                                    userDatabaseRef.child("Users").child(currentUserId).child("ParkingLogs").child(currentUserId+time).setValue(parkingLogInfo);

                                    userDatabaseRef.child("motor_status").setValue(1);

                                    Intent towardsScanning = new Intent(Qrcode.this, PayActivity.class);
                                    towardsScanning.putExtra("currentUserId", currentUserId);
                                    startActivity(towardsScanning);
                                }

                            }
                            else{
                                Toast.makeText(Qrcode.this, "Scan the verified code", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

    }
}
