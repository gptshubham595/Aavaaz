package com.newsapp.aavaaz.app.activity;

import android.Manifest;
import android.annotation.TargetApi;
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
    public static String city = "1", state = "1", country = "1", longitude = "1", latitude = "1",id,aid;
    private ProgressDialog progressDialog;
    TextView locationText;
    public static String location;
    private SamplePresenter samplePresenter;
    private DatabaseReference mDatabase,mi;
    Thread a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_display_layout);

        locationText = (TextView) findViewById(R.id.locationText);
        samplePresenter = new SamplePresenter(this);

        mAuth = FirebaseAuth.getInstance();

         id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID) + "@gmail.com";
         aid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("mylog", "Not granted");
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else
            {requestLocation();}

        }


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                requestLocation();
                Location mCurrentLocation = locationResult.getLastLocation();
                LatLng myCoordinates = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

                city = getCityName(myCoordinates);
                latitude = mCurrentLocation.getLatitude() + "";
                longitude = mCurrentLocation.getLongitude() + "";
                state = getStateName(myCoordinates);
                country = getCountryName(myCoordinates);

            }
        };
        getLocation();


    }

    private void register_user(final String display_name, final String email, final String password, final String lat,final String lon,final String city,final String state,final String country) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    final String uid = current_user.getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    String device_token = FirebaseInstanceId.getInstance().getToken();

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", display_name);
                    userMap.put("first","yes");
                    userMap.put("city", city);
                    userMap.put("state", state);
                    userMap.put("country", country);
                    userMap.put("longitude", lon);
                    userMap.put("latitude", lat);
                    userMap.put("latitude", lat);
                    userMap.put("device_token", device_token);

                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //Toast.makeText(getApplicationContext(), "Registered!!", Toast.LENGTH_LONG).show();
                                mi = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("first");
                                mi.setValue("no");
                                Intent a=new Intent(getApplicationContext(),Start1.class);
                                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(a);


                            }

                        }
                    });

                    mi = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Last");
                    HashMap<String, String> userMap1 = new HashMap<>();
                    userMap1.put("Sports", "1");
                    userMap1.put("Politics", "1");
                    userMap1.put("Agriculture", "1");
                    userMap1.put("Business", "1");
                    userMap1.put("Education", "1");
                    userMap1.put("Entertainment", "1");
                    userMap1.put("Homeis", "1");
                    userMap1.put("Gadgets", "1");
                    userMap1.put("International", "1");
                    userMap1.put("Lifestyle", "1");

                    mi.setValue(userMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //Toast.makeText(getApplicationContext(), "Registered!!", Toast.LENGTH_LONG).show();
                                finish();
                                } }});

                } else {
                    //Toast.makeText(getApplicationContext(), "Sorry.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void loginUser(final String id, String password) {

        mAuth.signInWithEmailAndPassword(id, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Logged  In", Toast.LENGTH_SHORT).show();
                    Intent a = new Intent(getApplicationContext(), Homeis.class);
                    a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                    finish();

                } else {
                    register_user(aid, id, pass, latitude,longitude,city,state,country);

                    //Toast.makeText(getApplicationContext(), "Error : " + task_result, Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private String getCityName(LatLng myCoordinates) {
        String myCity = "";
        Geocoder geocoder = new Geocoder(SampleActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(myCoordinates.latitude, myCoordinates.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            myCity = addresses.get(0).getLocality();
            Log.d("mylog", "Complete Address: " + addresses.toString());
            Log.d("mylog", "Address: " + address);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCity;
    }

    private String getStateName(LatLng myCoordinates) {
        String myCity = "";
        Geocoder geocoder = new Geocoder(SampleActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(myCoordinates.latitude, myCoordinates.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            myCity = addresses.get(0).getAdminArea();
            Log.d("mylog", "Complete Address: " + addresses.toString());
            Log.d("mylog", "Address: " + address);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCity;
    }

    private String getCountryName(LatLng myCoordinates) {
        String myCity = "";
        Geocoder geocoder = new Geocoder(SampleActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(myCoordinates.latitude, myCoordinates.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            myCity = addresses.get(0).getCountryName();
            Log.d("mylog", "Complete Address: " + addresses.toString());
            Log.d("mylog", "Address: " + address);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCity;
    }


    private void requestLocation() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);
        Log.d("mylog", "In Requesting Location");
        if (location != null && (System.currentTimeMillis() - location.getTime()) <= 1000 * 2) {
            LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
            city = getCityName(myCoordinates);
            //Toast.makeText(this, cityName, Toast.LENGTH_SHORT).show();
        } else {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setNumUpdates(1);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            Log.d("mylog", "Last location too old getting new location!");
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.requestLocationUpdates(locationRequest,
                    mLocationCallback, Looper.myLooper());
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginUser(id, pass);

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
            loginUser(id, pass);

        }
    }

}