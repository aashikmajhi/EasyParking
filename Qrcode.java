package what.naiya.application;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Qrcode extends AppCompatActivity {

    SurfaceView cp;
    TextView txt;
    TextView ctime;
    BarcodeDetector bar;
    CameraSource cameraSource;
    final int RequestCameraPermissionId = 1001;

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
                            vibrator.vibrate(1000);
                            txt.setText(qrcodes.valueAt(0).displayValue);
                            Calendar cal = Calendar.getInstance();
                            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                            ctime.setText(format.format(cal.getTime()));
                            ctime.setVisibility(View.VISIBLE);




                        }
                    });
                }

            }
        });
    }
}
