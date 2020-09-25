package com.babbangona.mspalybookupgrade.transporter.views.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView scanner;

    /**
     * If they eventually add to scan more QR codes,
     * use the view-model implementation in LMD distribution app.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scanner = new ZXingScannerView(this);
        setContentView(scanner);
    }

    @Override
    protected void onResume() {
        super.onResume();
        scanner.setResultHandler(this);
        scanner.startCamera();
        setTitle("Scan Transporter Voucher");
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanner.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        String[] cleanedData = result.getText().split("\\*"); //gets the scanned data without the * after

        //Process the data.
        String[] data = cleanedData[0].split(",");

        if (data[0].charAt(0) == 'V' && data[0].length() == 10) {
            setResult(RESULT_OK, new Intent()
                    .putExtra("VOUCHER_ID", data[0])
                    .putExtra("STATUS", "SUCCESS"));
        } else {
            setResult(RESULT_OK, new Intent()
                    .putExtra("STATUS", "FAILED"));
        }
        finish();
    }
}