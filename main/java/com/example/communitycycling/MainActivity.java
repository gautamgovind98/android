package com.example.communitycycling;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;


public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST = 1;
    Button send_button;
    EditText send_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        send_button = (Button)findViewById(R.id.send_button_id);
        send_text = (EditText)findViewById(R.id.send_text_id);
        // Check GPS is enabled
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
            finish();
        }


        // Check location permission is granted - if it is, start
        // the service, otherwise request the permission
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            startTrackerService();
            send_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    String str = send_text.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), DisplayActivity.class);
                    intent.putExtra("message_key", str);
                    startActivity(intent);
                }
            });
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }
    }

    public void startTrackerService() {
        startService(new Intent(this, TrackerService.class));
        //startService(new Intent(this, DisplayActivity.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Start the service when the permission is granted
            startTrackerService();
        } else {
            finish();
        }
    }
}
