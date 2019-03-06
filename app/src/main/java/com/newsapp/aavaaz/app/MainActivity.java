package com.newsapp.aavaaz.app;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.ValueEventListener;
import com.newsapp.aavaaz.app.secondpage.Homeis;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.newsapp.aavaaz.app.start.Start1;

import java.util.HashMap;

import maes.tech.intentanim.CustomIntent;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
    String pass = "123456789";
    TextView textView;
    private DatabaseReference mDatabase,mi,usersref;
    private FirebaseAuth mAuth;
    Dialog dialog;
    TextView heading;
    int i = 0;
    public static String aid,longitude="1",latitude="1",city="1",state="1",country="1",first;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitude = super.getIntent().getExtras().getString("lat");
        longitude = super.getIntent().getExtras().getString("lon");
        //Toast.makeText(getApplicationContext(),city,Toast.LENGTH_SHORT).show();
        mAuth = FirebaseAuth.getInstance();

        String id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID) + "@gmail.com";
        aid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);
        register_user(aid, id, pass, latitude,longitude,city,state,country);


    }

    private void register_user(final String display_name, final String email, final String password, final String lat,final String lon,final String city,final String state,final String country) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    String device_token = FirebaseInstanceId.getInstance().getToken();

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", display_name);
                    userMap.put("city", city);
                    userMap.put("state", state);
                    userMap.put("country", country);
                    userMap.put("longitude", lon);
                    userMap.put("latitude", lat);
                    userMap.put("device_token", device_token);

                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //Toast.makeText(getApplicationContext(), "Registered!!", Toast.LENGTH_LONG).show();
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
                                Intent a=new Intent(getApplicationContext(),Start1.class);
                                startActivity(a);
                                CustomIntent.customType(MainActivity.this,"fadein-to-fadeout");
                                finish();
                            }
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Logged In.", Toast.LENGTH_LONG).show();
                    loginUser(email,pass);
                }

            }
        });
    }
    private void go(){
        Thread a=new Thread(){
            @Override
            public void run() {
                try{
                    sleep(10);
                }
                catch (Exception e){e.printStackTrace();}
                finally {
                    Intent a=new Intent(getApplicationContext(),Homeis.class);
                    startActivity(a);
                    CustomIntent.customType(MainActivity.this,"fadein-to-fadeout");
                }
            }
        };
        a.start();

    }

    private void loginUser(final String id, String password) {

        mAuth.signInWithEmailAndPassword(id, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
           if(task.isSuccessful()){
               go();
           }
           else{register_user(aid, id, pass, latitude,longitude,city,state,country);
           }
            }
        });
    }

}