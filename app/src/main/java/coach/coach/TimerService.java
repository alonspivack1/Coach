package coach.coach;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Notification
//Notifcation


public class TimerService extends IntentService {
    private ConnectivityManager connectivityManager;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Notification");
    private DataSnapshot dataSnap=null;
    int IdNotification=1;
    Boolean FirstLook=true;
    int WakeUpNotificationcount=0,NotificationSec=60;
    boolean connected;
    private String username;
    private NotificationManager mNotificationManager;
    NotificationCompat.BigTextStyle bigText;
    PendingIntent pendingIntent;
    Intent ii;
    Calendar cal;
    int onoff;
    int minute;
    SharedPreferences onoffref;
    int hourofday;
    SharedPreferences.Editor editor;

    String SPchat,SPprogram;
    String time="";
    NotificationCompat.Builder mBuilder;
    public TimerService()
    {
        super("Timer Service");
    }
    public void onCreate(){
        super.onCreate();


        Log.v("timer","Timer service has started");
        Log.v("timer","NOW!");

    }

    public int onStartCommand(Intent intent,int flags,int startId){
        super.onStartCommand(intent,flags,startId);

        return START_STICKY;

    };


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("timerstart","timerstart");

        onoffref = getSharedPreferences("TimerService", MODE_PRIVATE);
        onoff = onoffref.getInt("onoff",0);
        onoff=onoff+1;
        SharedPreferences.Editor editorr = getSharedPreferences("TimerService", MODE_PRIVATE).edit();
        editorr.putInt("onoff",onoff);
        editorr.apply();
            ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> runningTaskInfo = manager.getRunningTasks(1);
            ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
            Log.e("Activity",componentInfo.getPackageName());

        /*
        SharedPreferences prefs = getSharedPreferences("TimerService", MODE_PRIVATE);
        int onoff = prefs.getInt("onoff",0);*/
        /*if (service)
        {*/
        /*
        *         connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }
        else{
            connected = false;}
        SharedPreferences prefs = getSharedPreferences("service", MODE_PRIVATE);
        String username = prefs.getString("username","");
        boolean SignOut = prefs.getBoolean("signout",true);

        if(!SignOut)
        {
            databaseReference=databaseReference.child(username);

        }
        }
*/
        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }
        else{
            connected = false;
        }
        SharedPreferences prefs = getSharedPreferences("service", MODE_PRIVATE);
        username = prefs.getString("username","");
        Boolean SignOut = prefs.getBoolean("signout",true);
            SharedPreferences prefss = getSharedPreferences("Alerts", MODE_PRIVATE);
            SPchat = prefss.getString("chat", "");
            SPprogram = prefss.getString("program", "");



            if (!SignOut){
            databaseReference=databaseReference.child(username);
            Log.v("timerusername",username+SignOut);


                DatabaseReference deleteProgramAlarm = FirebaseDatabase.getInstance().getReference().child("Notification").child(username).child("ProgramAlarm");
                deleteProgramAlarm.removeValue();
                DatabaseReference deleteChat = FirebaseDatabase.getInstance().getReference().child("Notification").child(username).child("Chat");
                deleteChat.removeValue();

            for (int i=0; i<1;)
            {
                    Log.v("timer", "loop");
                while (connected&&i<1){

                    if (WakeUpNotificationcount>=NotificationSec)
                    {
                        Log.v("timer","WakeUpNotifictioncount"+" finish");
                        WakeUpNotificationcount=0;
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this.getApplicationContext(), "notify_001");
                        Intent ii = new Intent(this.getApplicationContext(), LogInActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);
                        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                        bigText.setBigContentTitle("הרבה זמן לא נכנסת לאפליקציה");
                        bigText.setSummaryText("תזכורת");
                        mBuilder.setContentIntent(pendingIntent);
                        mBuilder.setSmallIcon(R.drawable.logo);
                      /*  mBuilder.setContentTitle("Your Title");
                        mBuilder.setContentText("Your text");*/
                        mBuilder.setPriority(Notification.PRIORITY_MAX);
                        mBuilder.setStyle(bigText);
                        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            String channelId = "YOUR_CHANNEL_ID";
                            NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
                            mNotificationManager.createNotificationChannel(channel);
                            mBuilder.setChannelId(channelId);
                        }
                        mNotificationManager.notify(0, mBuilder.build());
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
                        onoffref = getSharedPreferences("TimerService", MODE_PRIVATE);
                        if (onoff!=onoffref.getInt("onoff",0))
                        {
                            i=1;
                        }
                    }
                    catch (Exception e) { }


                        Log.v("timerconnected","ConnectingTrue");

                    connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                    if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        connected = true;
                    }
                    else{
                        connected = false;}
                    try {
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if ((dataSnap != null && !dataSnapshot.toString().equals(dataSnap.toString()))||(FirstLook)) {
                                    FirstLook=false;
                                    if (dataSnapshot.hasChild("ProgramAlarm")) {
                                        String help = dataSnapshot.child("ProgramAlarm").getValue().toString();
                                        if (help.indexOf(",") != -1) {


                                            Calendar cal = Calendar.getInstance();
                                            int minute = cal.get(Calendar.MINUTE);
                                            int hourofday = cal.get(Calendar.HOUR_OF_DAY);
                                            if (minute>=10){
                                                time = String.valueOf(hourofday)+":"+String.valueOf(minute);}
                                            else
                                            {
                                                time = String.valueOf(hourofday)+":0"+String.valueOf(minute);
                                            }
                                            Log.e("time",time);
                                            SPprogram="תוכנית האימון עודכנה מהמאמן " + help.substring(0,help.indexOf(","))+"-"+time+","+SPprogram;
                                            SharedPreferences.Editor editor = getSharedPreferences("Alerts", MODE_PRIVATE).edit();
                                            editor.putString("program",SPprogram);
                                            editor.apply();

                                            Log.v("timer", help.substring(0, help.indexOf(",")));


                                            /*
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
                                            mNotificationManager.notify(2, mBuilder.build());*/

                                            while (help.indexOf(",") != -1) {

                                                help = help.substring(help.indexOf(",") + 1);
                                                if (help.indexOf(",") != -1) {

                                                    cal = Calendar.getInstance();
                                                    minute = cal.get(Calendar.MINUTE);
                                                    hourofday = cal.get(Calendar.HOUR_OF_DAY);
                                                    if (minute>=10){
                                                        time = String.valueOf(hourofday)+":"+String.valueOf(minute);}
                                                    else
                                                    {
                                                        time = String.valueOf(hourofday)+":0"+String.valueOf(minute);
                                                    }
                                                    Log.e("time",time);
                                                    SPprogram="תוכנית האימון עודכנה מהמאמן " + help.substring(0, help.indexOf(","))+"-"+time+","+SPprogram;
                                                    editor = getSharedPreferences("Alerts", MODE_PRIVATE).edit();
                                                    editor.putString("program",SPprogram);
                                                    editor.apply();

                                                    Log.v("timer", help.substring(0, help.indexOf(",")));
/*
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
                                                    mNotificationManager.notify(2, mBuilder.build());*/
                                                } else {
                                                    if (!help.equals("")) {

                                                        cal = Calendar.getInstance();
                                                        minute = cal.get(Calendar.MINUTE);
                                                        hourofday = cal.get(Calendar.HOUR_OF_DAY);
                                                        if (minute>=10){
                                                            time = String.valueOf(hourofday)+":"+String.valueOf(minute);}
                                                        else
                                                        {
                                                            time = String.valueOf(hourofday)+":0"+String.valueOf(minute);
                                                        }
                                                        Log.e("time",time);
                                                        SPprogram="תוכנית האימון עודכנה מהמאמן " + help+"-"+time+","+SPprogram;
                                                        editor = getSharedPreferences("Alerts", MODE_PRIVATE).edit();
                                                        editor.putString("program",SPprogram);
                                                        editor.apply();


                                                        Log.v("timer", help);
/*
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
                                                        mNotificationManager.notify(2, mBuilder.build());*/
                                                    }

                                                }

                                            }
                                        } else {
                                            cal = Calendar.getInstance();
                                            minute = cal.get(Calendar.MINUTE);
                                            hourofday = cal.get(Calendar.HOUR_OF_DAY);
                                            if (minute>=10){
                                                time = String.valueOf(hourofday)+":"+String.valueOf(minute);}
                                            else
                                            {
                                                time = String.valueOf(hourofday)+":0"+String.valueOf(minute);
                                            }
                                            Log.e("time",time);
                                            SPprogram="תוכנית האימון עודכנה מהמאמן " + help+"-"+time+","+SPprogram;
                                            editor = getSharedPreferences("Alerts", MODE_PRIVATE).edit();
                                            editor.putString("program",SPprogram);
                                            editor.apply();

                                            Log.v("timer", help);

                                           /* mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
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
                                            mNotificationManager.notify(2, mBuilder.build());*/
                                        }
                                        DatabaseReference deleteProgramAlarm = FirebaseDatabase.getInstance().getReference().child("Notification").child(username).child("ProgramAlarm");
                                        deleteProgramAlarm.removeValue();

                                    }
                                    if (dataSnapshot.hasChild("Chat")) {
                                        String help = dataSnapshot.child("Chat").getValue().toString();
                                        if (help.indexOf(",") != -1) {

                                            cal = Calendar.getInstance();
                                            minute = cal.get(Calendar.MINUTE);
                                            hourofday = cal.get(Calendar.HOUR_OF_DAY);
                                            if (minute>=10){
                                                time = String.valueOf(hourofday)+":"+String.valueOf(minute);}
                                            else
                                            {
                                                time = String.valueOf(hourofday)+":0"+String.valueOf(minute);
                                            }
                                            Log.e("time",time);
                                            SPchat="התקבלה הודעה חדשה מ" + help.substring(0, help.indexOf(","))+"-"+time+","+SPchat;
                                            editor = getSharedPreferences("Alerts", MODE_PRIVATE).edit();
                                            editor.putString("chat",SPchat);
                                            editor.apply();


                                            Log.v("timer", help.substring(0, help.indexOf(",")));

                                          /*  mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
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
                                            mNotificationManager.notify(IdNotification, mBuilder.build());*/
                                            while (help.indexOf(",") != -1) {
                                                help = help.substring(help.indexOf(",") + 1);
                                                if (help.indexOf(",") != -1) {

                                                    cal = Calendar.getInstance();
                                                    minute = cal.get(Calendar.MINUTE);
                                                    hourofday = cal.get(Calendar.HOUR_OF_DAY);
                                                    if (minute>=10){
                                                        time = String.valueOf(hourofday)+":"+String.valueOf(minute);}
                                                    else
                                                    {
                                                        time = String.valueOf(hourofday)+":0"+String.valueOf(minute);
                                                    }
                                                    Log.e("time",time);
                                                    SPchat="התקבלה הודעה חדשה מ" + help.substring(0, help.indexOf(","))+"-"+time+","+SPchat;
                                                    editor = getSharedPreferences("Alerts", MODE_PRIVATE).edit();
                                                    editor.putString("chat",SPchat);
                                                    editor.apply();

                                                    Log.v("timer", help.substring(0, help.indexOf(",")));

                                                    /*
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
                                                    mNotificationManager.notify(IdNotification, mBuilder.build());*/
                                                } else {
                                                    if (!help.equals("")) {


                                                        cal = Calendar.getInstance();
                                                        minute = cal.get(Calendar.MINUTE);
                                                        hourofday = cal.get(Calendar.HOUR_OF_DAY);
                                                        if (minute>=10){
                                                            time = String.valueOf(hourofday)+":"+String.valueOf(minute);}
                                                        else
                                                        {
                                                            time = String.valueOf(hourofday)+":0"+String.valueOf(minute);
                                                        }
                                                        Log.e("time",time);
                                                        SPchat="התקבלה הודעה חדשה מ" + help+"-"+time+","+SPchat;
                                                        editor = getSharedPreferences("Alerts", MODE_PRIVATE).edit();
                                                        editor.putString("chat",SPchat);
                                                        editor.apply();

                                                        Log.v("timer", help);
/*
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
                                                        mNotificationManager.notify(IdNotification, mBuilder.build());*/
                                                    }
                                                }

                                            }
                                        } else {

                                            cal = Calendar.getInstance();
                                            minute = cal.get(Calendar.MINUTE);
                                            hourofday = cal.get(Calendar.HOUR_OF_DAY);
                                            if (minute>=10){
                                                time = String.valueOf(hourofday)+":"+String.valueOf(minute);}
                                            else
                                            {
                                                time = String.valueOf(hourofday)+":0"+String.valueOf(minute);
                                            }
                                            Log.e("time",time);
                                            SPchat="התקבלה הודעה חדשה מ" + help+"-"+time+","+SPchat;
                                            editor = getSharedPreferences("Alerts", MODE_PRIVATE).edit();
                                            editor.putString("chat",SPchat);
                                            editor.apply();

                                            Log.v("timer", help);

                                            /*
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
                                            mNotificationManager.notify(IdNotification, mBuilder.build());*/


                                        }
                                        DatabaseReference deleteChat = FirebaseDatabase.getInstance().getReference().child("Notification").child(username).child("Chat");
                                        deleteChat.removeValue();

                                    }


                                }
                                dataSnap = dataSnapshot;



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
                while (!connected&&i<1)
                {

                    if (WakeUpNotificationcount>=NotificationSec)
                    {
                        Log.v("timer","WakeUpNotifictioncount"+" finish");
                        WakeUpNotificationcount=0;
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this.getApplicationContext(), "notify_001");
                        Intent ii = new Intent(this.getApplicationContext(), LogInActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);
                        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                        bigText.setBigContentTitle("הרבה זמן לא נכנסת לאפליקציה");
                        bigText.setSummaryText("תזכורת");
                        mBuilder.setContentIntent(pendingIntent);
                        mBuilder.setSmallIcon(R.drawable.logo);
                   /* mBuilder.setContentTitle("Your Title");
                    mBuilder.setContentText("Your text");*/
                        mBuilder.setPriority(Notification.PRIORITY_MAX);
                        mBuilder.setStyle(bigText);
                        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
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
                        onoffref = getSharedPreferences("TimerService", MODE_PRIVATE);
                        if (onoff!=onoffref.getInt("onoff",0))
                        {
                            i=1;
                        }
                    } catch (Exception e) {
                    }
                    Log.v("timerconnected","ConnectingFlase");
                    connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                    if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        connected = true;
                    }
                    else{
                        connected = false;}
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


    @Override
    public void onDestroy() {

        Log.v("timer", "DESTROY");
        onStartCommand(null,0,0);
        /*
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this.getApplicationContext(), "notify_001");
        Intent ii = new Intent(this.getApplicationContext(), LogInActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.setBigContentTitle("הרבה זמן לא נכנסת לאפליקציהDESTROY");
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

        mNotificationManager.notify(0, mBuilder.build());*/
       // onStartCommand(null, 0, 0);
    }

  }
//}