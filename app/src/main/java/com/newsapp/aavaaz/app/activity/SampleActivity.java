package com.newsapp.aavaaz.app.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.newsapp.aavaaz.app.MainActivity;
import com.newsapp.aavaaz.app.MainActivity2;
import com.newsapp.aavaaz.app.secondpage.Homeis;
import com.newsapp.aavaaz.app.start.Start1;
import com.newsapp.aavaaz.base.LocationBaseActivity;
import com.newsapp.aavaaz.configuration.Configurations;
import com.newsapp.aavaaz.configuration.LocationConfiguration;
import com.newsapp.aavaaz.constants.FailType;
import com.newsapp.aavaaz.constants.ProcessType;
import com.newsapp.aavaaz.app.Locationout;
import com.newsapp.aavaaz.app.R;
import com.newsapp.aavaaz.app.SamplePresenter;
import com.newsapp.aavaaz.app.SamplePresenter.SampleView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import maes.tech.intentanim.CustomIntent;

public class SampleActivity extends LocationBaseActivity implements SampleView {
    private FirebaseAuth mAuth;
    private FusedLocationProviderClient mFusedLocationClient;
    LocationManager locationManager;
    private LocationCallback mLocationCallback;
    String pass = "123456789";
    public static String city = "1", state = "1", country = "1", longitude = "1", latitude = "1", id, aid;
    private ProgressDialog progressDialog;
    TextView locationText;
    public static String location;
    private SamplePresenter samplePresenter;
    private DatabaseReference mDatabase, mi;
    Thread a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_display_layout);

        locationText = (TextView) findViewById(R.id.locationText);
        samplePresenter = new SamplePresenter(this);
        String id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID) + "@gmail.com";
        String pass="123456789";
        mAuth = FirebaseAuth.getInstance();
        getLocation();
     loginUser(id,pass);
    }

    private void loginUser(final String id, String password) {

        mAuth.signInWithEmailAndPassword(id, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(getApplicationContext(), "Logged  In", Toast.LENGTH_SHORT).show();
                    Intent a = new Intent(getApplicationContext(), Homeis.class);
                    a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                    CustomIntent.customType(SampleActivity.this,"fadein-to-fadeout");
                  

                } else {
                    getLocation();
                    //register_user(aid, id, pass, latitude, longitude, city, state, country);

                    //Toast.makeText(getApplicationContext(), "Error : " + task_result, Toast.LENGTH_LONG).show();
                }
            }
        });


    }


private void go(){
    a=new Thread(){
        @Override
        public void run() {
            try{
                sleep(200);
            }
            catch (Exception e){e.printStackTrace();}
            finally {
                Intent a=new Intent(getApplicationContext(),Locationout.class);
                startActivity(a);
                CustomIntent.customType(SampleActivity.this,"fadein-to-fadeout");
            }
        }
    };
    a.start();

}
    @Override
    protected void onDestroy() {
        super.onDestroy();
        go();
        samplePresenter.destroy();
    }

    @Override
    public LocationConfiguration getLocationConfiguration() {
        return Configurations.defaultConfiguration("Give me the permission!", "Would you mind to turn GPS on?");
    }

    @Override
    public void onLocationChanged(Location location) {
        samplePresenter.onLocationChanged(location);
    }

    @Override
    public void onLocationFailed(@FailType int failType) {
        samplePresenter.onLocationFailed(failType);
		
    }

    @Override
    public void onProcessTypeChanged(@ProcessType int processType) {
        samplePresenter.onProcessTypeChanged(processType);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getLocationManager().isWaitingForLocation()
                && !getLocationManager().isAnyDialogShowing()) {
            displayProgress();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissProgress();
    }

    private void displayProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
            progressDialog.setMessage("Getting location...");
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public String getText() {
        return locationText.getText().toString();
    }

    @Override
    public void setText(String text) {
        locationText.setText(text);
    }

    @Override
    public void updateProgress(String text) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.setMessage(text);
        }
    }

    @Override
    public void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
             go();
        }
    }

}