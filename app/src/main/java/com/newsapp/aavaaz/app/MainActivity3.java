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
import com.newsapp.aavaaz.app.activity.SampleActivity;
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

public class MainActivity3 extends AppCompatActivity {
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
        setContentView(R.layout.activity_main3);

        city = super.getIntent().getExtras().getString("city");

        state = super.getIntent().getExtras().getString("sate");
        country = super.getIntent().getExtras().getString("country");
        latitude = super.getIntent().getExtras().getString("lat");
        longitude = super.getIntent().getExtras().getString("lon");
        //Toast.makeText(getApplicationContext(),city,Toast.LENGTH_SHORT).show();
        mAuth = FirebaseAuth.getInstance();

        String id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID) + "@gmail.com";
        aid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        loginUser(id,pass);

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
                    Intent a=new Intent(getApplicationContext(),MainActivity2.class);
                    startActivity(a);
                    CustomIntent.customType(MainActivity3.this,"fadein-to-fadeout");
                }
            }
        };
        a.start();

    }

    private void go2(){
        Thread a=new Thread(){
            @Override
            public void run() {
                try{
                    sleep(10);
                }
                catch (Exception e){e.printStackTrace();}
                finally {
                    Intent a=new Intent(getApplicationContext(),Locationout.class);
                    startActivity(a);
                    CustomIntent.customType(MainActivity3.this,"fadein-to-fadeout");
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
           else{go2();
           }
            }
        });
    }

}