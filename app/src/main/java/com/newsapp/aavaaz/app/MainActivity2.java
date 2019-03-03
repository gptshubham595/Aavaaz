package com.newsapp.aavaaz.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.newsapp.aavaaz.app.secondpage.Homeis;

import maes.tech.intentanim.CustomIntent;
public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

       Thread a=new Thread(){
@Override
public void run() {
        try{
        sleep(800);
        }
        catch (Exception e){e.printStackTrace();}
        finally {
            Intent a=new Intent(getApplicationContext(),Homeis.class);
            startActivity(a);
            CustomIntent.customType(MainActivity2.this,"fadein-to-fadeout");

        }
        }
        };
        a.start();
        }

        }
