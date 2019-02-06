package com.newsapp.aavaaz.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Criteria;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.newsapp.aavaaz.app.activity.SampleActivity;
import com.newsapp.aavaaz.app.secondpage.Homeis;
import com.newsapp.aavaaz.app.start.Start1;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import maes.tech.intentanim.CustomIntent;

public class Locationout  extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    LocationManager locationManager;
    String pass = "123456789";
    private ProgressDialog progressDialog;
    TextView locationText;
    public static String location;
    private DatabaseReference mDatabase,mi,usersref;
    private FirebaseAuth mAuth;
    private SamplePresenter samplePresenter;
    Thread a;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    public static String cityName="1",stateName="1",countryName="1",longitude="1",latitude="1";
    private Marker marker;
    Button btnButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mAuth = FirebaseAuth.getInstance();

        String id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID) + "@gmail.com";
        String aid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        loginUser(id,pass);


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                requestLocation();
                Location mCurrentLocation = locationResult.getLastLocation();
                LatLng myCoordinates = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

                 cityName = getCityName(myCoordinates);
                 latitude = mCurrentLocation.getLatitude()+"";
                 longitude = mCurrentLocation.getLongitude()+"";
                 stateName = getStateName(myCoordinates);
                 countryName = getCountryName(myCoordinates);


          //      Toast.makeText(getApplicationContext(), latitude, Toast.LENGTH_SHORT).show();
            //    Toast.makeText(getApplicationContext(), longitude, Toast.LENGTH_SHORT).show();
              //  Toast.makeText(getApplicationContext(), cityName, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), stateName, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), countryName, Toast.LENGTH_SHORT).show();

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myCoordinates, 13.0f));
                if (marker == null) {
                    marker = mMap.addMarker(new MarkerOptions().position(myCoordinates));
                } else
                    marker.setPosition(myCoordinates);
            }
        };
        btnButton = findViewById(R.id.btnCurrentCity);
                //Toast.makeText(getApplicationContext(), latitude, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), longitude, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), cityName, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), stateName, Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), countryName, Toast.LENGTH_SHORT).show();

                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Log.d("mylog", "Not granted");
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    } else
                        requestLocation();
    Thread a;
        a=new Thread(){
            @Override
            public void run() {
                try{
                    sleep(200);
                }
                catch (Exception e){e.printStackTrace();}
                finally {Intent a=new Intent(getApplicationContext
                            (),MainActivity.class);
                    a.putExtra("city",cityName);
                    a.putExtra("state",stateName);
                    a.putExtra("country",countryName);
                    a.putExtra("lat",latitude);
                    a.putExtra("lon",longitude);
                    a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                    CustomIntent.customType(Locationout.this,"fadein-to-fadeout");
                }
            }
        };
        a.start();

                } else
                {requestLocation();
                }



        if (Build.VERSION.SDK_INT >= 23) {
            Log.d("mylog", "Getting Location Permission");
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("mylog", "Not granted");
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else
            {requestLocation();
}
        } else
        {requestLocation();      Thread  a=new Thread(){
            @Override
            public void run() {
                try{
                    sleep(500);
                }
                catch (Exception e){e.printStackTrace();}
                finally {Intent a=new Intent(getApplicationContext
                            (),MainActivity.class);
                    a.putExtra("city",cityName);
                    a.putExtra("state",stateName);
                    a.putExtra("country",countryName);
                    a.putExtra("lat",latitude);
                    a.putExtra("lon",longitude);
                    a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                    CustomIntent.customType(Locationout.this,"fadein-to-fadeout");
                }
            }
        };
        a.start();
}
    }
    private void loginUser(String id, String password) {

        mAuth.signInWithEmailAndPassword(id, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Logged 1st step", Toast.LENGTH_SHORT).show();
                    String current_user_id = mAuth.getCurrentUser().getUid();
                    String deviceToken = FirebaseInstanceId.getInstance().getToken();
                    DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                    DatabaseReference mshortdesc = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_id).child("first");
                    mshortdesc.keepSynced(true);

                    mshortdesc.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            if(!dataSnapshot.exists()){}
                            else{
                                final String value = dataSnapshot.getValue(String.class);
                                Thread a=new Thread(){
                                    @Override
                                    public void run() {
                                        try{
                                            sleep(500);
                                        }catch (Exception e){e.printStackTrace();}
                                        finally {
                                            if(value.equals("yes")){
                                                Intent a=new Intent(getApplicationContext(),Start1.class);
                                                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(a);

                                            }}
                                    }
                                };
                                a.start();

                            }

                        }


                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value

                        }
                    });
                    mUserDatabase.child(current_user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Logged  In", Toast.LENGTH_SHORT).show();
                            Intent a=new Intent(getApplicationContext(),Homeis.class);
                            a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(a);
                            finish();
                        }
                    });
                } else {

                    //Toast.makeText(getApplicationContext(), "Error : " + task_result, Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private String getCityName(LatLng myCoordinates) {
        String myCity = "";
        Geocoder geocoder = new Geocoder(Locationout.this, Locale.getDefault());
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
        Geocoder geocoder = new Geocoder(Locationout.this, Locale.getDefault());
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
        Geocoder geocoder = new Geocoder(Locationout.this, Locale.getDefault());
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
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
            cityName = getCityName(myCoordinates);
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
}
