package com.cookandroid.material;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class CameraActivity extends AppCompatActivity {
    Button btn_scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        setTitle("Barcode Scanner");
        btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(v->
        {
            scanCode();
        });
    }
    public static class barcodeNum {
        public String num = "";

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }
    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() !=null)
        {

            AlertDialog.Builder builder = new AlertDialog.Builder( CameraActivity.this);
            builder.setTitle("상품 바코드");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    barcodeNum barNum = new barcodeNum();
                    barNum.setNum(result.getContents());
                    final String text = result.getContents().toString();
                    Intent intent = new Intent(getApplicationContext(),  api.class);
                    intent.putExtra("TEXT", text);
                    startActivity(intent);

                }

            }).show();
        }
    });
}
