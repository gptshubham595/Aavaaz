package com.newsapp.aavaaz.app.thirdpage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.newsapp.aavaaz.app.R;
import com.newsapp.aavaaz.app.secondpage.Homeis;

public class NewsHomeisFull extends AppCompatActivity implements GestureDetector.OnDoubleTapListener,GestureDetector.OnGestureListener {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    TextView heading,desc;
    ProgressDialog load;

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_homeis_full);
        mAuth=FirebaseAuth.getInstance();
        heading=findViewById(R.id.heading);
        desc=findViewById(R.id.description);
        gestureDetector = new GestureDetector(this);        load=new ProgressDialog(this);
        int i=super.getIntent().getExtras().getInt("k");
        String in=i+"";
        getheading(in);
        //Toast.makeText(getApplicationContext(),longdes,Toast.LENGTH_SHORT).show();
        getdesc(in);


    }

    private void getdesc(String in) { load.setTitle("Wait");
     load.setMessage("Getting the latest news for you..");load.show();
        DatabaseReference mdesc = FirebaseDatabase.getInstance().getReference().child("Homeis").child(in).child("content").child("des");
                 mdesc.keepSynced(true); mdesc. addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
        load.dismiss();        // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(!dataSnapshot.exists()){}
                else{String value = dataSnapshot.getValue(String.class);
                    desc.setText(value);}
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }
    private void getheading(String in) { 
        DatabaseReference mheading = FirebaseDatabase.getInstance().getReference().child("Homeis").child(in).child("content").child("heading");
// Read from the database
        mheading.keepSynced(true);  mheading.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(!dataSnapshot.exists()){}
                else{String value = dataSnapshot.getValue(String.class);
                    heading.setText(value);}
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Intent a = new Intent(getApplicationContext(),Homeis.class);
         
        a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);   startActivity(a);
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
	
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(getApplicationContext(),Homeis.class);
         
        a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);   startActivity(a);
    }
}


