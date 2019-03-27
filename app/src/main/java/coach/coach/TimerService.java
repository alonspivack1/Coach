package coach.coach;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

//Notification
//Notifcation


public class TimerService extends IntentService {
    private ConnectivityManager connectivityManager;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Notification");
    private DataSnapshot dataSnap=null;
    int IdNotification=1;
    Boolean Sticky=false,FirstLook=true;
    int WakeUpNotificationcount=0,NotificationSec=86400;
    private boolean connected,connectedlog;
    private String username;
    boolean service=true;
    private NotificationManager mNotificationManager;
    NotificationCompat.BigTextStyle bigText;
    PendingIntent pendingIntent;
    Intent ii;
    NotificationCompat.Builder mBuilder;
    public TimerService()
    {

        super("Timer Service");
    }
    public void onCreate(){
    super.onCreate();


        Log.v("timer","Timer service has started");
        Log.v("timer","NOW!");
        onStartCommand(null,0,1);

    }

    public int onStartCommand(Intent intent,int flags,int startId){
        super.onStartCommand(intent,flags,startId);
        Sticky=false;
        return START_STICKY;
    };


    @Override
    protected void onHandleIntent(Intent intent) {

        /*if (service)
        {*/
        Log.v("timer","service: "+ service);



        intent=null;
        if (intent==null){
            Sticky=true;
            SharedPreferences prefs = getSharedPreferences("service", MODE_PRIVATE);
            username = prefs.getString("username","");
            Boolean SignOut = prefs.getBoolean("signout",true);


            if (!SignOut){
            databaseReference=databaseReference.child(username);
            Log.v("timerusername",username+SignOut);


                DatabaseReference deleteProgramAlarm = FirebaseDatabase.getInstance().getReference().child("Notification").child(username).child("ProgramAlarm");
                deleteProgramAlarm.removeValue();
                DatabaseReference deleteChat = FirebaseDatabase.getInstance().getReference().child("Notification").child(username).child("Chat");
                deleteChat.removeValue();


            connected = false;
            connectedlog=false;
            for (int i=0; i<1;)
            {
                    Log.v("timer", "loop");
                while (!connected&&i<1)
                {
                    if (!Sticky)
                    {
                        i++;
                    }
                    if (WakeUpNotificationcount>=NotificationSec)
                {
                    Log.v("timer","WakeUpNotifictioncount"+" finish");
                    WakeUpNotificationcount=0;
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(this.getApplicationContext(), "notify_001");
                    Intent ii = new Intent(this.getApplicationContext(), MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);

                    NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                    bigText.setBigContentTitle("הרבה זמן לא נכנסת לאפליקציה");
                    bigText.setSummaryText("תזכורת");

                    mBuilder.setContentIntent(pendingIntent);
                    mBuilder.setSmallIcon(R.drawable.logo);
                    mBuilder.setContentTitle("Your Title");
                    mBuilder.setContentText("Your text");
                    mBuilder.setPriority(Notification.PRIORITY_MAX);
                    mBuilder.setStyle(bigText);

                    mNotificationManager =
                            (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        String channelId = "YOUR_CHANNEL_ID";
                        NotificationChannel channel = new NotificationChannel(channelId,
                                "Channel human readable title",
                                NotificationManager.IMPORTANCE_DEFAULT);
                        mNotificationManager.createNotificationChannel(channel);
                        mBuilder.setChannelId(channelId);
                    }

                    mNotificationManager.notify(0, mBuilder.build());


                }
                    try {
                        Thread.sleep(1000);
                        Log.v("timer","WakeUpNotifictioncount"+" "+WakeUpNotificationcount);
                        WakeUpNotificationcount++;
                    } catch (Exception e) {
                    }
                    if (connectedlog) {
                        Log.v("timer","ConnectingFlase");
                        connectedlog=false;
                    }

                    connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                    if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        //we are connected to a network
                        connected = true;
                    }
                    else
                        connected = false;
                }
                while (connected&&i<1){
                    if (!Sticky)
                    {
                        i++;
                    }
                    if (WakeUpNotificationcount>=NotificationSec)
                    {
                        Log.v("timer","WakeUpNotifictioncount1"+" finish");
                        WakeUpNotificationcount=0;
                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(this.getApplicationContext(), "notify_001");
                        Intent ii = new Intent(this.getApplicationContext(), MainActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);

                        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                        bigText.setBigContentTitle("הרבה זמן לא נכנסת לאפליקציה");
                        bigText.setSummaryText("תזכורת");

                        mBuilder.setContentIntent(pendingIntent);
                        mBuilder.setSmallIcon(R.drawable.logo);
                        mBuilder.setContentTitle("Your Title");
                        mBuilder.setContentText("Your text");
                        mBuilder.setPriority(Notification.PRIORITY_MAX);
                        mBuilder.setStyle(bigText);

                        mNotificationManager =
                                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            String channelId = "YOUR_CHANNEL_ID";
                            NotificationChannel channel = new NotificationChannel(channelId,
                                    "Channel human readable title",
                                    NotificationManager.IMPORTANCE_DEFAULT);
                            mNotificationManager.createNotificationChannel(channel);
                            mBuilder.setChannelId(channelId);
                        }

                        mNotificationManager.notify(IdNotification, mBuilder.build());
                        IdNotification++;
                        /*bigText.setBigContentTitle("הרבה זמן לא נכנסת לאפליקציה");
                        bigText.setSummaryText("תזכורת");
                        mBuilder.setContentIntent(pendingIntent);
                        mBuilder.setSmallIcon(R.drawable.logo);
                        mBuilder.setPriority(Notification.PRIORITY_MAX);
                        mBuilder.setStyle(bigText);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            String channelId = "YOUR_CHANNEL_ID";
                            NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
                            mNotificationManager.createNotificationChannel(channel);
                            mBuilder.setChannelId(channelId);}
                        mNotificationManager.notify(IdNotification, mBuilder.build());
                        IdNotification++;*/
                    }
                    try {
                        Thread.sleep(1000);
                        Log.v("timer","WakeUpNotifictioncount"+" "+WakeUpNotificationcount);
                        WakeUpNotificationcount++;
                    } catch (Exception e) {
                    }

                    if (!connectedlog) {
                        Log.v("timer","ConnectingTrue");
                        connectedlog=true;
                    }
                    connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                    if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        connected = true;
                    }
                    else
                        connected = false;
                    try {
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (Sticky){
                                    Log.e("timersticky",Sticky+"");
                                if ((dataSnap != null && !dataSnapshot.toString().equals(dataSnap.toString()))||(FirstLook)) {
                                    FirstLook=false;
                                    if (dataSnapshot.hasChild("ProgramAlarm")) {
                                        String help = dataSnapshot.child("ProgramAlarm").getValue().toString();
                                        if (help.indexOf(",") != -1) {
                                            Log.v("timer", help.substring(0, help.indexOf(",")));
                                            mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
                                            ii = new Intent(getApplicationContext(), LogInActivity.class);
                                            pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, ii, 0);
                                            bigText = new NotificationCompat.BigTextStyle();
                                            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                            bigText.setBigContentTitle("תוכנית האימון עודכנה מהמאמן " + help.substring(0, help.indexOf(",")));
                                            mBuilder.setContentIntent(pendingIntent);
                                            mBuilder.setSmallIcon(R.drawable.logo);
                                            mBuilder.setPriority(Notification.PRIORITY_MAX);
                                            mBuilder.setStyle(bigText);
                                            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                String channelId = "YOUR_CHANNEL_ID";
                                                NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
                                                mNotificationManager.createNotificationChannel(channel);
                                                mBuilder.setChannelId(channelId);}
                                            mNotificationManager.notify(IdNotification, mBuilder.build());

                                            while (help.indexOf(",") != -1) {

                                                help = help.substring(help.indexOf(",") + 1);
                                                if (help.indexOf(",") != -1) {
                                                    Log.v("timer", help.substring(0, help.indexOf(",")));

                                                    mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
                                                    Intent ii = new Intent(getApplicationContext(), LogInActivity.class);
                                                    pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, ii, 0);
                                                    NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                                                    bigText.setBigContentTitle("תוכנית האימון עודכנה מהמאמן " + help.substring(0, help.indexOf(",")));
                                                    mBuilder.setContentIntent(pendingIntent);
                                                    mBuilder.setSmallIcon(R.drawable.logo);
                                                    mBuilder.setPriority(Notification.PRIORITY_MAX);
                                                    mBuilder.setStyle(bigText);
                                                    mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                        String channelId = "YOUR_CHANNEL_ID";
                                                        NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
                                                        mNotificationManager.createNotificationChannel(channel);
                                                        mBuilder.setChannelId(channelId);}
                                                    mNotificationManager.notify(IdNotification, mBuilder.build());
                                                } else {
                                                    if (!help.equals("")) {
                                                        Log.v("timer", help);

                                                        mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
                                                        Intent ii = new Intent(getApplicationContext(), LogInActivity.class);
                                                        pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, ii, 0);
                                                        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                                                        bigText.setBigContentTitle("תוכנית האימון עודכנה מהמאמן " + help);
                                                        mBuilder.setContentIntent(pendingIntent);
                                                        mBuilder.setSmallIcon(R.drawable.logo);
                                                        mBuilder.setPriority(Notification.PRIORITY_MAX);
                                                        mBuilder.setStyle(bigText);
                                                        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                            String channelId = "YOUR_CHANNEL_ID";
                                                            NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
                                                            mNotificationManager.createNotificationChannel(channel);
                                                            mBuilder.setChannelId(channelId);
                                                        }
                                                        mNotificationManager.notify(IdNotification, mBuilder.build());
                                                    }

                                                }

                                            }
                                        } else {
                                            Log.v("timer", help);
                                            mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
                                            Intent ii = new Intent(getApplicationContext(), LogInActivity.class);
                                            pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, ii, 0);
                                            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                                            bigText.setBigContentTitle("תוכנית האימון עודכנה מהמאמן " + help);
                                            mBuilder.setContentIntent(pendingIntent);
                                            mBuilder.setSmallIcon(R.drawable.logo);
                                            mBuilder.setPriority(Notification.PRIORITY_MAX);
                                            mBuilder.setStyle(bigText);
                                            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                String channelId = "YOUR_CHANNEL_ID";
                                                NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
                                                mNotificationManager.createNotificationChannel(channel);
                                                mBuilder.setChannelId(channelId);}
                                            mNotificationManager.notify(IdNotification, mBuilder.build());
                                        }
                                        DatabaseReference deleteProgramAlarm = FirebaseDatabase.getInstance().getReference().child("Notification").child(username).child("ProgramAlarm");
                                        deleteProgramAlarm.removeValue();
                                        IdNotification++;

                                    }
                                    if (dataSnapshot.hasChild("Chat")) {
                                        String help = dataSnapshot.child("Chat").getValue().toString();
                                        if (help.indexOf(",") != -1) {
                                            Log.v("timer", help.substring(0, help.indexOf(",")));
                                            mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
                                            Intent ii = new Intent(getApplicationContext(), LogInActivity.class);
                                            pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, ii, 0);
                                            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                                            bigText.setBigContentTitle("התקבלה הודעה חדשה מ" + help.substring(0, help.indexOf(",")));
                                            mBuilder.setContentIntent(pendingIntent);
                                            mBuilder.setSmallIcon(R.drawable.logo);
                                            mBuilder.setPriority(Notification.PRIORITY_MAX);
                                            mBuilder.setStyle(bigText);
                                            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                String channelId = "YOUR_CHANNEL_ID";
                                                NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
                                                mNotificationManager.createNotificationChannel(channel);
                                                mBuilder.setChannelId(channelId);}
                                            mNotificationManager.notify(IdNotification, mBuilder.build());
                                            while (help.indexOf(",") != -1) {
                                                help = help.substring(help.indexOf(",") + 1);
                                                if (help.indexOf(",") != -1) {
                                                    Log.v("timer", help.substring(0, help.indexOf(",")));
                                                    mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
                                                    ii = new Intent(getApplicationContext(), LogInActivity.class);
                                                    pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, ii, 0);
                                                    bigText = new NotificationCompat.BigTextStyle();
                                                    bigText.setBigContentTitle("התקבלה הודעה חדשה מ" + help.substring(0, help.indexOf(",")));
                                                    mBuilder.setContentIntent(pendingIntent);
                                                    mBuilder.setSmallIcon(R.drawable.logo);
                                                    mBuilder.setPriority(Notification.PRIORITY_MAX);
                                                    mBuilder.setStyle(bigText);
                                                    mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                        String channelId = "YOUR_CHANNEL_ID";
                                                        NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
                                                        mNotificationManager.createNotificationChannel(channel);
                                                        mBuilder.setChannelId(channelId);}
                                                    mNotificationManager.notify(IdNotification, mBuilder.build());
                                                } else {
                                                    if (!help.equals("")) {
                                                        Log.v("timer", help);

                                                        mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
                                                        ii = new Intent(getApplicationContext(), LogInActivity.class);
                                                        pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, ii, 0);
                                                        bigText = new NotificationCompat.BigTextStyle();
                                                        bigText.setBigContentTitle("התקבלה הודעה חדשה מ" + help);
                                                        mBuilder.setContentIntent(pendingIntent);
                                                        mBuilder.setSmallIcon(R.drawable.logo);
                                                        mBuilder.setPriority(Notification.PRIORITY_MAX);
                                                        mBuilder.setStyle(bigText);
                                                        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                            String channelId = "YOUR_CHANNEL_ID";
                                                            NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
                                                            mNotificationManager.createNotificationChannel(channel);
                                                            mBuilder.setChannelId(channelId); }
                                                        mNotificationManager.notify(IdNotification, mBuilder.build());
                                                    }
                                                }

                                            }
                                        } else {
                                            Log.v("timer", help);
                                            mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
                                            Intent ii = new Intent(getApplicationContext(), LogInActivity.class);
                                            pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, ii, 0);
                                            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                                            bigText.setBigContentTitle("התקבלה הודעה חדשה מ" + help);
                                            mBuilder.setContentIntent(pendingIntent);
                                            mBuilder.setSmallIcon(R.drawable.logo);
                                            mBuilder.setPriority(Notification.PRIORITY_MAX);
                                            mBuilder.setStyle(bigText);
                                            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                String channelId = "YOUR_CHANNEL_ID";
                                                NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
                                                mNotificationManager.createNotificationChannel(channel);
                                                mBuilder.setChannelId(channelId); }
                                            mNotificationManager.notify(IdNotification, mBuilder.build());


                                        }
                                        DatabaseReference deleteChat = FirebaseDatabase.getInstance().getReference().child("Notification").child(username).child("Chat");
                                        deleteChat.removeValue();
                                        IdNotification++;

                                    }


                                }
                                dataSnap = dataSnapshot;
                            }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {


                            }
                        });
                    }
                    catch (Exception e)
                    {
                        Log.v("timererror",e.toString());
                    }
                }

            }
                for (int i = 0; i < 1;) {
                    Log.v("timer", "i = " + i);
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                }
            }
            /*
            NotificationCompat.Builder nb = new NotificationCompat.Builder(this);
            nb.setContentText("Timer done...");
            nb.setContentTitle("Hi!");
            nb.setSmallIcon(R.drawable.unimage);

            NotificationManager nm =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.notify(1,nb.build());*/
            return;
        }
        else
        {
        for (int i = 0; i < 1;) {
            Log.v("timer", "i = " + i);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }}
    }

    @Override
    public void onDestroy() {
        Log.v("timer", "DESTROY");
        onStartCommand(null, 0, 0);
    }}
//}
