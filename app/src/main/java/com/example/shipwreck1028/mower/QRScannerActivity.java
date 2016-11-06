package com.example.shipwreck1028.mower;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScannerActivity extends Activity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    int REQUEST_CAMERA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showCamera();
    }

    /**
     * Check for camera permission and fire up the camera viewer
     * to start reading QR Codes using the ZXingScannerView.
     */
    public void showCamera(){
        // Don't allow the screen to fall asleep while they are trying
        // to scan a QR Code.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Check if they are running Marshmallow and if they are, ask for permission.
        // This is done because in Marshmallow and higher, you don't give the application
        // access on installation, you give it to them at runtime, which is good and bad because
        // now you have to check for permissions every time because they can now be revoked too.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Check to see if they have granted the application permission to use the camera
            // and to write to external storage to save the photo.
            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                // Run the QR Code Scanner using ZXingScannerView Library.
                mScannerView = new ZXingScannerView(this);
                setContentView(mScannerView);
                mScannerView.setResultHandler(this);
                mScannerView.startCamera();
            } else {
                // We were denied Camera permission so by Android requirements, we are supposed to
                // explain why we need them and then ask them for permission again.
                if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                    Toast.makeText(this, R.string.permission_explain, Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(requestCode == REQUEST_CAMERA){
            // Yay!!! We got permission! Start the camera for QR Scanning.
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                showCamera();
            } else {
                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                Intent sendStuff = new Intent(this, MainActivity.class);
                startActivity(sendStuff);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    public void handleResult(Result result){
        // This is where we do some sketchy business and we attach the result of the
        // QR Code Scan to the end of our new Intent, essentially passing the value
        // from window to window.
        if(result.getBarcodeFormat().toString().equals("qrcode")) {
            String qr_val = result.getText();
            Intent sendStuff = new Intent(this, MainActivity.class);
            sendStuff.putExtra("key", qr_val);
            startActivity(sendStuff);
        }else{
            mScannerView.resumeCameraPreview(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register ourselves as a handler for scan results.
        // Start camera on resume
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop camera on pause
        mScannerView.stopCamera();
    }


}
