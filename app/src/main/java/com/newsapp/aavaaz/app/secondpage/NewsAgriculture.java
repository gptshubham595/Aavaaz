package com.newsapp.aavaaz.app
.secondpage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import android.Manifest;

import com.newsapp.aavaaz.app.Home;


import com.newsapp.aavaaz.app.R;
import com.newsapp.aavaaz.app.thirdpage.NewsAgricultureFull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import maes.tech.intentanim.CustomIntent;

public class NewsAgriculture extends AppCompatActivity implements GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener {
    FirebaseUser cu;
    String image1;
    ProgressDialog pd;
    private DatabaseReference notification;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    ProgressDialog load;
 
    File imagepath;
    ImageView imageView;
    Button up, down, share;
    TextView heading, shortdesc,urllink;
    public static final int Notifyid = 1;
    String notf_head,url;
    public static final int SWIPE_THRESHOLD = 100;
    public static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private GestureDetector gestureDetector;
    ViewFlipper viewFlipper;
    ImageView img;
    Dialog dialog;
    boolean notify1=false;
    String value,url2;
    DatabaseReference mcheck;
    public static int i = 1, Stat = 0, tap = 0;
	VideoView video;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //==========================================================
   //==========================================================Webview
        setContentView(R.layout.activity_news_agriculture);
        urllink=findViewById(R.id.urllink);
        mAuth = FirebaseAuth.getInstance();
        urllink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"click",Toast.LENGTH_SHORT).show();
                Intent Browser=new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                startActivity(Browser);

            }
        });
		
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenh=displayMetrics.heightPixels;
        //int screenw=displayMetrics.widthPixels;
        int imgh=(int)(screenh* .4);
        //==============================================================
        //         i=super.getIntent().getExtras().getInt("i");
        //  Toast.makeText(getApplicationContext(),i+"",Toast.LENGTH_SHORT).show();

		////video=findViewById(R.id.////video);
		
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);
        /*if (isFirstRun) {
            //Toast.makeText(getApplicationContext(), "Registering You!!", Toast.LENGTH_LONG).show();
            dialog.setContentView(R.layout.instruction_dialog);
            dialog.show();

            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                    .putBoolean("isFirstRun", false).apply();
        }*/

//======================================================================
        heading = findViewById(R.id.heading);
        mAuth = FirebaseAuth.getInstance();

//======================================================================================
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();


//[========================= Added Now
        //========================================
  DatabaseReference mi = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Last").child("Agriculture");
        mi.keepSynced(true);

        mi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(!dataSnapshot.exists()){}
                else{String value = dataSnapshot.getValue(String.class);
                    i  =Integer.parseInt(value);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });


        //==========================================
        imageView=findViewById(R.id.button2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(getApplicationContext(),Home.class);
                startActivity(a);
            }
        });
        sendNotification(getApplicationContext());
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        share=findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(NewsAgriculture.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);    Bitmap bitmap=takescreen();
                saveBitmap(bitmap);
                shareit();
            }
        });


        up=findViewById(R.id.up);   img=findViewById(R.id.slide); img.getLayoutParams().height=imgh;
        up.setOnClickListener(new View.OnClickListener() {
            @Override  public void onClick(View v) {

                saveup();}});


        down=findViewById(R.id.down);
        down.setOnClickListener(new View.OnClickListener() {
            @Override  public void onClick(View v) {
                savedown();
                Toast.makeText(getApplicationContext(), "Down Voted!!", Toast.LENGTH_LONG).show();

            }});

        gestureDetector = new GestureDetector(this);
        load=new ProgressDialog(this);

        shortdesc=findViewById(R.id.desc);
        mAuth = FirebaseAuth.getInstance();Button tag = findViewById(R.id.tags);

        tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makedialog();

            }});



        mAuth=FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();

        geturl(); getsourceurl(); getheading();
        
		getimage();
		getshortdesc();
		

    }
//===========================================Added Now
    ///========================================================Till
    private void readi() {

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

        DatabaseReference mi = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Last").child("Agriculture");
        mi.keepSynced(true);

        mi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(!dataSnapshot.exists()){}
                else{String value = dataSnapshot.getValue(String.class);
                    i=Integer.parseInt(value);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }

    public void sendNotification(Context context){

        Intent a=new Intent(getApplicationContext(),NewsPolitics.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,a,0);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher_foreground);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher_foreground));
        builder.setContentTitle("You are Seeing the best News App");
        builder.setContentText("Aavaz");
        builder.setSubText("Tap to view" + "..");
        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Notifyid,builder.build());
    }

    private void saveup(){        String in=i+"";
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Upvoted").child("Agriculture").child(in);

        mDatabase.setValue("UP VOTED").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "UpVoted!!", Toast.LENGTH_LONG).show();
                } }
        });
    }
    private void savedown(){        String in=i+"";

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Downvoted").child("Agriculture").child(in);

        mDatabase.setValue("Down VOTED").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Down Voted!!", Toast.LENGTH_LONG).show();



                }

            }
        });


    }
    private Bitmap takescreen(){
        View root=findViewById(android.R.id.content).getRootView();
        root.setDrawingCacheEnabled(true);
        return root.getDrawingCache();

    }

    public void saveBitmap(Bitmap bitmap){
        imagepath=new File(Environment.getExternalStorageDirectory() +"/screenshot.png");
        FileOutputStream fos;
        String path;
        //File file=new File(path);
        try{
            fos=new FileOutputStream(imagepath);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
            fos.flush();
            fos.close();
        }catch(FileNotFoundException e){
        }
        catch(IOException e){}
    }
    public void shareit(){
        Uri path=FileProvider.getUriForFile(getBaseContext(),"com.newsapp.aavaaz.app",imagepath);
        Intent share=new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT,heading.getText()+"  जागरूक रहें। समय बचाओ। 60 शब्दों में समाचार पढ़ने के लिए Aavaaz डाउनलोड करें।http://bit.ly/newsaavaaz");
        share.putExtra(Intent.EXTRA_STREAM,path);
        share.setType("image/*");
        startActivity(Intent.createChooser(share,"Share..."));

    }
    private void makedialog2() {
        dialog.setContentView(R.layout.instruction_dialog);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.show();
    }
    private void makedialog(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.dialog));
        View mView = getLayoutInflater().inflate(R.layout.dialog_option, null);
        TextView sports,politics,education,entertainment,lifestyle,gadgets,agriculture,business,international,homeis;
        sports=mView.findViewById(R.id.sports);
        homeis=mView.findViewById(R.id.homeis);
        politics=mView.findViewById(R.id.politics);
        education=mView.findViewById(R.id.education);
        entertainment=mView.findViewById(R.id.entertainment);
        lifestyle=mView.findViewById(R.id.lifestyle);
        gadgets=mView.findViewById(R.id.gadget);
        agriculture=mView.findViewById(R.id.agriculture);
        business=mView.findViewById(R.id.business);
        international=mView.findViewById(R.id.international);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        homeis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(getApplicationContext(),Homeis.class);
                a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  startActivity(a);
            }});

        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(getApplicationContext(),NewsSports.class);
                a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  startActivity(a);
            }});

        politics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(getApplicationContext(),NewsPolitics.class);
                a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  startActivity(a);
            }});

        education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(getApplicationContext(),NewsEducation.class);
                a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  startActivity(a);
            }});

        entertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(getApplicationContext(),NewsEntertainment.class);
                a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  startActivity(a);
            }});

        lifestyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(getApplicationContext(),NewsLifestyle.class);
                a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  startActivity(a);
            }});
        gadgets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(getApplicationContext(),NewsGadgets.class);
                a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  startActivity(a);
            }});
        agriculture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(getApplicationContext(),NewsAgriculture.class);
                a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  startActivity(a);
            }});

        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(getApplicationContext(),NewsBusiness.class);
                a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  startActivity(a);
            }});
        international.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(getApplicationContext(),NewsInternational.class);
                a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  startActivity(a);
            }});
    }


    private void getimage() {

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

        DatabaseReference mi = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Last").child("Agriculture");
        mi.keepSynced(true);

        mi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(!dataSnapshot.exists()){}
                else{String value = dataSnapshot.getValue(String.class);
                    i=Integer.parseInt(value);
                    String in=value;

                    DatabaseReference mimage = FirebaseDatabase.getInstance().getReference().child("Agriculture").child(in).child("pic").child("id");
                    mimage.keepSynced(true);
                    mimage.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            if(!dataSnapshot.exists()) {

                            }
                            else{
                                final String image1 = dataSnapshot.getValue().toString();
                                Picasso.get().load(image1).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.slide1).into(img, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }
                                    @Override
                                    public void onError(Exception e) {
                                        Picasso.get().load(image1).placeholder(R.drawable.slide1).into(img);
                                    }
                                });
                            }
							
							//play();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

    }
	
	/*private void play() {
		FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

        DatabaseReference mi = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Last").child("Agriculture");
        mi.keepSynced(true);

        mi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(!dataSnapshot.exists()){}
                else{String value = dataSnapshot.getValue(String.class);
                    i=Integer.parseInt(value);
                    String in=value;
                                DatabaseReference mheading = FirebaseDatabase.getInstance().getReference().child("Agriculture").child(in).child("content").child("url");
                    mheading.keepSynced(true);
                    // Read from the database
                    mheading.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            if(!dataSnapshot.exists()){}
                            else{url = dataSnapshot.getValue(String.class);
                                load.setMessage("Loading..");
                                load.show();
		//MediaController media=new MediaController(NewsAgriculture.this);
		//media.setAnchorView(////video);
		//Uri uri=Uri.parse(url);

		//video.setMediaController(media);
		//video.setVideoURI(uri);
                                //video.seekTo(1);
		//video.requestFocus();
		//video.start();
         //video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        load.dismiss();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
	}*/
    private void getshortdesc() {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

        DatabaseReference mi = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Last").child("Agriculture");
        mi.keepSynced(true);

        mi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(!dataSnapshot.exists()){}
                else{String value = dataSnapshot.getValue(String.class);
                    i=Integer.parseInt(value);
                    String in=value;

                    DatabaseReference mshortdesc = FirebaseDatabase.getInstance().getReference().child("Agriculture").child(in).child("content").child("shortdesc");
                    mshortdesc.keepSynced(true);

                    mshortdesc.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            if(!dataSnapshot.exists()){}
                            else{String value = dataSnapshot.getValue(String.class);
                                shortdesc.setText(value);}
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

    }
    private void getheading() {

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

        DatabaseReference mi = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Last").child("Agriculture");
        mi.keepSynced(true);

        mi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(!dataSnapshot.exists()){}
                else{String value = dataSnapshot.getValue(String.class);
                    i=Integer.parseInt(value);
                    String in=value;
                    DatabaseReference mheading = FirebaseDatabase.getInstance().getReference().child("Agriculture").child(in).child("content").child("heading");
                    mheading.keepSynced(true);
                    // Read from the database
                    mheading.addValueEventListener(new ValueEventListener() {
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
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }
	private void getsourceurl() {

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

        DatabaseReference mi = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Last").child("Agriculture");
        mi.keepSynced(true);

        mi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(!dataSnapshot.exists()){}
                else{String value = dataSnapshot.getValue(String.class);
                    i=Integer.parseInt(value);
                    String in=value;
                    DatabaseReference mheading = FirebaseDatabase.getInstance().getReference().child("Agriculture").child(in).child("content").child("urlsource");
                    mheading.keepSynced(true);
                    // Read from the database
                    mheading.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            if(!dataSnapshot.exists()){}
                            else{url= dataSnapshot.getValue(String.class);
                    }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }
private void getsourceurlr() {
        load.setTitle("Wait");
        load.setMessage("Getting the latest news for you..");
        load.show();
        String in=i+"";
        DatabaseReference mheading = FirebaseDatabase.getInstance().getReference().child("Agriculture").child(in).child("content").child("urlsource");
// Read from the database
        mheading.keepSynced(true);
        mheading.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                load.dismiss();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(!dataSnapshot.exists()){  }
                else{url = dataSnapshot.getValue(String.class);
                    }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }
	
	private void getsourceurll() {
        load.setTitle("Wait");
        load.setMessage("Getting the latest news for you..");
        load.show();
        String in=i+"";
        DatabaseReference mheading = FirebaseDatabase.getInstance().getReference().child("Agriculture").child(in).child("content").child("urlsource");
// Read from the database
        mheading.keepSynced(true);
        mheading.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                load.dismiss();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(!dataSnapshot.exists()){ }
                else{url = dataSnapshot.getValue(String.class);
                    }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

private void geturl() {

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

        DatabaseReference mi = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Last").child("Agriculture");
        mi.keepSynced(true);

        mi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(!dataSnapshot.exists()){}
                else{String value = dataSnapshot.getValue(String.class);
                    i=Integer.parseInt(value);
                    String in=value;
                    DatabaseReference mheading = FirebaseDatabase.getInstance().getReference().child("Agriculture").child(in).child("content").child("urlread");
                    mheading.keepSynced(true);
                    // Read from the database
                    mheading.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            if(!dataSnapshot.exists()){}
                            else{String value = dataSnapshot.getValue(String.class);
                                urllink.setText(value);}
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }

    private void getimager() {
        
		String in=i+"";
        load.setTitle("Wait");
        load.setMessage("Getting the latest news for you..");
        load.show();

        DatabaseReference mimage = FirebaseDatabase.getInstance().getReference().child("Agriculture").child(in).child("pic").child("id");
        mimage.keepSynced(true);
        mimage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                load.dismiss();                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(!dataSnapshot.exists()){

                }
                else{
                    image1=dataSnapshot.getValue().toString();
                    Picasso.get().load(image1).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.slide1).into(img, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(image1).placeholder(R.drawable.slide1).into(img);
                        }
                    });
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void getshortdescr() {
        String in=i+"";
        DatabaseReference mshortdesc = FirebaseDatabase.getInstance().getReference().child("Agriculture").child(in).child("content").child("shortdesc");
        mshortdesc.keepSynced(true);

        mshortdesc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(!dataSnapshot.exists()){}
                else{ value = dataSnapshot.getValue(String.class);
                    shortdesc.setText(value);}
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }
    private void getheadingr() {
        load.setTitle("Wait");
        load.setMessage("Getting the latest news for you..");
        load.show();
        String in=i+"";
        DatabaseReference mheading = FirebaseDatabase.getInstance().getReference().child("Agriculture").child(in).child("content").child("heading");
// Read from the database
        mheading.keepSynced(true);
        mheading.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                load.dismiss();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(!dataSnapshot.exists()){  FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
        i++;
        DatabaseReference mi = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Last").child("Agriculture");
        mi.setValue(i+"").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                }
            }
        }); right();}
                else{String value = dataSnapshot.getValue(String.class);
                    heading.setText(value);}
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }
private void geturlr() {
        load.setTitle("Wait");
        load.setMessage("Getting the latest news for you..");
        load.show();
        String in=i+"";
        DatabaseReference mheading = FirebaseDatabase.getInstance().getReference().child("Agriculture").child(in).child("content").child("urlread");
// Read from the database
        mheading.keepSynced(true);
        mheading.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                load.dismiss();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(!dataSnapshot.exists()){ }
                else{String value = dataSnapshot.getValue(String.class);
                    urllink.setText(value);}
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    private void getimagel() {
        String in=i+"";
        load.setTitle("Wait");
        load.setMessage("Getting the latest news for you..");
        load.show();

        DatabaseReference mimage = FirebaseDatabase.getInstance().getReference().child("Agriculture").child(in).child("pic").child("id");
        mimage.keepSynced(true);
        mimage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                load.dismiss();                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(!dataSnapshot.exists()){

                }
                else{
                    final  String image1=dataSnapshot.getValue().toString();
                    Picasso.get().load(image1).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.slide1).into(img, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(image1).placeholder(R.drawable.slide1).into(img);
                        }
                    });
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void getshortdescl() {
        String in=i+"";
        DatabaseReference mshortdesc = FirebaseDatabase.getInstance().getReference().child("Agriculture").child(in).child("content").child("shortdesc");
        mshortdesc.keepSynced(true);

        mshortdesc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(!dataSnapshot.exists()){}
                else{String value = dataSnapshot.getValue(String.class);
                    shortdesc.setText(value);}
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }
    private void getheadingl() {
        load.setTitle("Wait");
        load.setMessage("Getting the latest news for you..");
        load.show();
        String in=i+"";
        DatabaseReference mheading = FirebaseDatabase.getInstance().getReference().child("Agriculture").child(in).child("content").child("heading");
// Read from the database
        mheading.keepSynced(true);
        mheading.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                load.dismiss();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(!dataSnapshot.exists()){ FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
        i--;
        DatabaseReference mi = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Last").child("Agriculture");
        mi.setValue(i+"").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                }
            }
        }); left();}
                else{String value = dataSnapshot.getValue(String.class);
                    heading.setText(value);}
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

private void geturlll() {
        load.setTitle("Wait");
        load.setMessage("Getting the latest news for you..");
        load.show();
        String in=i+"";
        DatabaseReference mheading = FirebaseDatabase.getInstance().getReference().child("Agriculture").child(in).child("content").child("urlread");
// Read from the database
        mheading.keepSynced(true);
        mheading.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                load.dismiss();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(!dataSnapshot.exists()){ }
                else{String value = dataSnapshot.getValue(String.class);
                    urllink.setText(value);}
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
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
    public boolean onFling(MotionEvent downevent, MotionEvent moveevent, float velocityX, float velocityY) {
        boolean result=false;
        float diffY=moveevent.getY() - downevent.getY();
        float diffX=moveevent.getX() - downevent.getX();
        if(Math.abs(diffX)>Math.abs(diffY)){
            //right or left swipe
            result=true;
            if(Math.abs(diffX)>SWIPE_THRESHOLD && Math.abs(velocityX)>SWIPE_VELOCITY_THRESHOLD ){
                if(diffX>0){onSwipeRight();}
                else {onSwipeLeft();}

            }

        }
        else{
            //up or down swipe
            result=true;
            if(Math.abs(diffY)>SWIPE_THRESHOLD && Math.abs(velocityY)>SWIPE_VELOCITY_THRESHOLD){
                if(diffY>0){onSwipeBottom();}
                else{onSwipeTop();}
            }
        }

        return result;
    }

    private void onSwipeRight() {
        ////Toast.makeText(getApplicationContext(),"Right swipe",//Toast.LENGTH_SHORT).show();


        decrementi();
        getheadingr();
		geturlr(); getsourceurlr();
        getimager();
        getshortdescr();
    }

    private void decrementi() {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
		i--;
        DatabaseReference mi = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Last").child("Agriculture");

        mi.setValue(i+"").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //         Toast.makeText(getApplicationContext(), "UpVoted!!", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void onSwipeLeft() {
        ////Toast.makeText(getApplicationContext(),"Right swipe",//Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),"Sub Topic",//Toast.LENGTH_SHORT).show();
        incrementi();
        getheadingl();
		geturlll(); getsourceurll();
        getimagel();
        getshortdescl();

    }

    private void incrementi() {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
        i++;
        DatabaseReference mi = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Last").child("Agriculture");
        mi.setValue(i+"").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                }
            }
        });
    }
    private void onSwipeTop() {
        Intent a=new Intent(getApplicationContext(),Homeis.class);     a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  startActivity(a);
        //overridePendingTransition(R.anim.slideintop,R.anim.slideoutdown);
        CustomIntent.customType(this,"bottom-to-up");
    }
    private void onSwipeBottom() {
        ////Toast.makeText(getApplicationContext(),"Top swipe",//Toast.LENGTH_SHORT).show();
        Intent a=new Intent(getApplicationContext(),NewsGadgets.class);
		   a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  startActivity(a);
        //overridePendingTransition(R.anim.slideintop,R.anim.slideoutdown);
        CustomIntent.customType(this,"up-to-bottom");
    }
    private void right(){
               Intent a=new Intent(getApplicationContext(),NewsGadgets.class);    a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  startActivity(a);
        //overridePendingTransition(R.anim.slideintop,R.anim.slideoutdown);
        CustomIntent.customType(this,"right-to-left");
    }
    private void left(){
      
        ////Toast.makeText(getApplicationContext(),"Top swipe",//Toast.LENGTH_SHORT).show();
        Intent a=new Intent(getApplicationContext(),Homeis.class);    a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  startActivity(a);
        //overridePendingTransition(R.anim.slideintop,R.anim.slideoutdown);
        CustomIntent.customType(this,"left-to-right");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Intent a = new Intent(getApplicationContext(),NewsAgricultureFull.class);
        a.putExtra("k",i);
        a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  startActivity(a);
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {

        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        tap++;
        if(tap==1){Toast.makeText(getApplicationContext(),"Press Back Button Once more ..",Toast.LENGTH_SHORT).show();}
        if(tap>1){finish(); System.exit(0);}
    }

}

