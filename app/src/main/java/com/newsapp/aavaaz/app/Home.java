package com.newsapp.aavaaz.app;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.newsapp.aavaaz.app.secondpage.Homeis;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import com.newsapp.aavaaz.app.secondpage.NewsAgriculture;
import com.newsapp.aavaaz.app.secondpage.NewsBusiness;
import com.newsapp.aavaaz.app.secondpage.NewsEducation;
import com.newsapp.aavaaz.app.secondpage.NewsEntertainment;
import com.newsapp.aavaaz.app.secondpage.NewsGadgets;
import com.newsapp.aavaaz.app.secondpage.NewsInternational;
import com.newsapp.aavaaz.app.secondpage.NewsLifestyle;
import com.newsapp.aavaaz.app.secondpage.NewsPolitics;
import com.newsapp.aavaaz.app.secondpage.NewsSports;

import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {
    String pass = "123456789";
    static String url;
    TextView textView;
    private DatabaseReference mDatabase,usersref;
    private FirebaseAuth mAuth;
    DatabaseReference alluserdatabaseReference;
    ProgressDialog load;
    RecyclerView   alluserlist;
    Dialog dialog;
    TextView heading,shortdesc,text;
    ImageView sports,politics,education,entertainment,lifestyle,gadgets,agriculture,business,international,trendingimage,homeis;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //heading=findViewById(R.id.heading);
        load=new ProgressDialog(this);
alluserlist=(RecyclerView) findViewById(R.id.recycler);
        alluserlist.setHasFixedSize(true);
        alluserlist.setLayoutManager(new LinearLayoutManager(this));
 alluserdatabaseReference=FirebaseDatabase.getInstance().getReference().child("Trending").child("content");
        alluserdatabaseReference.keepSynced(true);


        // text=findViewById(R.id.text);
     //   trendingimage=findViewById(R.id.trendingimage);
 //       getimage();
   //     getheading();

        sports=findViewById(R.id.sports);
        homeis=findViewById(R.id.homeis);
        politics=findViewById(R.id.politics);
        education=findViewById(R.id.education);
        entertainment=findViewById(R.id.entertainment);
        lifestyle=findViewById(R.id.lifestyle);
        gadgets=findViewById(R.id.gadgets);
        agriculture=findViewById(R.id.agriculture);
        business=findViewById(R.id.business);
        international=findViewById(R.id.international);
        homeis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(getApplicationContext(),Homeis.class);
                startActivity(a);
            }});
        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(getApplicationContext(),NewsSports.class);
                startActivity(a);
            }});

        politics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(getApplicationContext(),NewsPolitics.class);
                startActivity(a);
            }});

        education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(getApplicationContext(),NewsEducation.class);
                startActivity(a);
            }});

        entertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(getApplicationContext(),NewsEntertainment.class);
                startActivity(a);
            }});

        lifestyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(getApplicationContext(),NewsLifestyle.class);
                startActivity(a);
            }});
        gadgets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(getApplicationContext(),NewsGadgets.class);
                startActivity(a);
            }});
        agriculture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(getApplicationContext(),NewsAgriculture.class);
                startActivity(a);
            }});

        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(getApplicationContext(),NewsBusiness.class);
                startActivity(a);
            }});
        international.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(getApplicationContext(),NewsInternational.class);
                startActivity(a);
            }});

}
    private void getimage() {DatabaseReference mimage = FirebaseDatabase.getInstance().getReference().child("Trending").child("content").child("image");
        mimage.keepSynced(true);
        // Read from the database
        mimage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(!dataSnapshot.exists()){Toast.makeText(getApplicationContext(),"Sorry Nothing Left",Toast.LENGTH_SHORT).show(); }
                else{loadtext();
                    final String image1 = dataSnapshot.getValue().toString();
                    Picasso.get().load(image1).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.slide1).into(trendingimage, new Callback() {
                        @Override
                        public void onSuccess() {
							loadtext();
                        }
                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(image1).placeholder(R.drawable.slide1).into(trendingimage);
                        }
                    });

                    }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
				loadtext();
            }
        });}
    private void getheading() {

        load.setTitle("Wait");
        load.setMessage("Getting the latest news for you..");
        load.show();

        DatabaseReference mheading = FirebaseDatabase.getInstance().getReference().child("Trending").child("content").child("heading");
        mheading.keepSynced(true);
        // Read from the database
        mheading.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
        load.dismiss();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(!dataSnapshot.exists()){Toast.makeText(getApplicationContext(),"Sorry Nothing Left",Toast.LENGTH_SHORT).show(); }
                else{loadtext();
                    String value = dataSnapshot.getValue(String.class);
                    heading.setText(value);}
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
				loadtext();
            }
        });
        }
    private void loadtext() {
      text.setText("Trending News Here");
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<allusers, Alluserviewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<allusers, Alluserviewholder>(
                allusers.class,R.layout.activity_trend,Alluserviewholder.class,alluserdatabaseReference
        ) {
            @Override
            protected void populateViewHolder(Alluserviewholder viewHolder, allusers model, final int position) {
                viewHolder.setHeading(model.getHeading());
                viewHolder.setImage(model.getImage());
                viewHolder.setUrl(model.getUrl());
                final String userid=getRef(position).getKey();
                viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"click",Toast.LENGTH_SHORT).show();

                        //Intent Browser=new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                        //startActivity(Browser);
                    }
                });
            }
        };
        alluserlist.setAdapter(firebaseRecyclerAdapter);
    }
    public static class Alluserviewholder extends RecyclerView.ViewHolder{
        View mview;
        public Alluserviewholder(@NonNull View itemView) {
            super(itemView);
            mview=itemView;
        }
        public void setHeading(String heading){
            TextView name = (TextView) mview.findViewById(R.id.heading);
            name.setText(heading);
        }
        public void setUrl(String url2){
            url=url2;
            //TextView name = (TextView) mview.findViewById(R.id.heading);
            //name.setText(heading);
        }
        public void setImage(final String user_thumb_image){
            final ImageView thumb_image = (ImageView) mview.findViewById(R.id.trendingimage);
            Picasso.get().load(user_thumb_image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.earth).into(thumb_image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(user_thumb_image).placeholder(R.drawable.earth).into(thumb_image);
                }
            });
        }

    }

}