package coach.coach;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;
import java.util.List;

/**
 * The service saves alerts, sends a notification and sends a notification if the user has not logged in a day.
 */
public class BackgroundService extends IntentService {
    private ConnectivityManager connectivityManager;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Notification");
    private DataSnapshot dataSnap=null;
    /**
     * The First look.
     * Checks if this is the first look in Firebase
     */
    Boolean FirstLook=true;
    /**
     * The Wake up notificationcount.
     * counter for notification
     */
    int WakeUpNotificationcount=0,
    /**
     * The Notification sec.
     * number of seconds to send a wake up notification
     */
    NotificationSec=86400;
    /**
     * The Connected.
     * Checking internet connection
     */
    boolean connected;
    private String username;
    private NotificationManager mNotificationManager;


    /**
     * The Cal.
     */
    Calendar cal;
    /**
     * The Onoff.
     * Checks if need to initialize the service
     */
    int onoff;
    /**
     * The String alert title.
     */
    String StringAlertTitle="התראות חדשות",
    /**
     * The String alert text.
     */
    StringAlertText="התקבלו התראות חדשות באפליקציה";
    /**
     * The Minute.
     */
    int minute;
    /**
     * The Onoffref.
     */
    SharedPreferences onoffref;
    /**
     * The Hourofday.
     */
    int hourofday;
    /**
     * The Editor.
     */
    SharedPreferences.Editor editor;

    /**
     * The S pchat.
     */
    String SPchat,
    /**
     * The S pprogram.
     */
    SPprogram;
    /**
     * The Time.
     */
    String time="";

    /**
     * Instantiates a new Background service.
     */
    public BackgroundService()
    {
        super("BackgroundService");
    }
    public void onCreate(){
        super.onCreate();


        Log.v("timer","service has started");

    }

    public int onStartCommand(Intent intent,int flags,int startId){
        super.onStartCommand(intent,flags,startId);

        return START_STICKY;

    };

    /**
     * The service saves alerts, sends a notification and sends a notification if the user has not logged in a day.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v("onHandleIntent","start");

        onoffref = getSharedPreferences("BackgroundService", MODE_PRIVATE);
        onoff = onoffref.getInt("onoff",0);
        onoff=onoff+1;
        SharedPreferences.Editor editorr = getSharedPreferences("BackgroundService", MODE_PRIVATE).edit();
        editorr.putInt("onoff",onoff);
        editorr.apply();

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
            Log.v("username",username);


               FirebaseDatabase.getInstance().getReference().child("Notification").child(username).child("ProgramAlarm").setValue("");

               FirebaseDatabase.getInstance().getReference().child("Notification").child(username).child("Chat").setValue("");


            for (int i=0; i<1;)
            {
                    Log.v("onHandleIntent", "startloop");
                while (connected&&i<1){

                    if (WakeUpNotificationcount>=NotificationSec)
                    {
                        Log.v("WakeUpNotificationcount","WakeUpNotifictioncount finish");
                        WakeUpNotificationcount=0;
                        if (SendNotification()){
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
                        Intent ii = new Intent(getApplicationContext(), LogInActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, ii, 0);
                        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                        bigText.setBigContentTitle("הרבה זמן לא נכנסת לאפליקציה");
                        bigText.setSummaryText("תזכורת");
                        mBuilder.setContentIntent(pendingIntent);
                        mBuilder.setSmallIcon(R.drawable.logo);
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
                    }
                    }
                    try {
                        Thread.sleep(1000);
                        Log.v("WakeUpNotifictioncount","WakeUpNotifictioncount "+WakeUpNotificationcount);
                        WakeUpNotificationcount++;
                        SharedPreferences prefsss = getSharedPreferences("Alerts", MODE_PRIVATE);
                        SPprogram = prefsss.getString("program", "");
                        SPchat = prefsss.getString("chat", "");

                        onoffref = getSharedPreferences("BackgroundService", MODE_PRIVATE);
                        if (onoff!=onoffref.getInt("onoff",0))
                        {
                            i=1;
                        }
                    }
                    catch (Exception e) { }


                        Log.v("Connection","ConnectingTrue");

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
                                            if (minute >= 10) {
                                                time = String.valueOf(hourofday) + ":" + String.valueOf(minute);
                                            } else {
                                                time = String.valueOf(hourofday) + ":0" + String.valueOf(minute);
                                            }
                                            if (!help.substring(0, help.indexOf(",")).equals("")) {
                                                SPprogram = help.substring(0, help.indexOf(",")) + "-" + time + "," + SPprogram;
                                                SharedPreferences.Editor editor = getSharedPreferences("Alerts", MODE_PRIVATE).edit();
                                                editor.putString("program", SPprogram);
                                                editor.apply();
                                            }

                                            if (SendNotification()){
                                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
                                            Intent ii = new Intent(getApplicationContext(), LogInActivity.class);
                                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, ii, 0);
                                            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                                            bigText.setBigContentTitle(StringAlertText);
                                            Log.e("FIRST","1");

                                            bigText.setSummaryText(StringAlertTitle);
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
                                            mNotificationManager.notify(1, mBuilder.build());
                                        }



                                            while (help.indexOf(",") != -1) {

                                                help = help.substring(help.indexOf(",") + 1);
                                                if (help.indexOf(",") != -1) {

                                                    cal = Calendar.getInstance();
                                                    minute = cal.get(Calendar.MINUTE);
                                                    hourofday = cal.get(Calendar.HOUR_OF_DAY);
                                                    if (minute >= 10) {
                                                        time = String.valueOf(hourofday) + ":" + String.valueOf(minute);
                                                    } else {
                                                        time = String.valueOf(hourofday) + ":0" + String.valueOf(minute);
                                                    }
                                                    if (!help.substring(0, help.indexOf(",")).equals("")) {
                                                        SPprogram = help.substring(0, help.indexOf(",")) + "-" + time + "," + SPprogram;
                                                        editor = getSharedPreferences("Alerts", MODE_PRIVATE).edit();
                                                        editor.putString("program", SPprogram);
                                                        editor.apply();
                                                    }

                                                    if (SendNotification()){
                                                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
                                                        Intent ii = new Intent(getApplicationContext(), LogInActivity.class);
                                                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, ii, 0);
                                                        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                                                    bigText.setBigContentTitle(StringAlertText);
                                                        Log.e("FIRST","2");
                                                        bigText.setSummaryText(StringAlertTitle);
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
                                                    mNotificationManager.notify(1, mBuilder.build());
                                                }
                                                }
                                                else {
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
                                                        if (!help.equals("")) {
                                                            SPprogram = help + "-" + time + "," + SPprogram;
                                                            editor = getSharedPreferences("Alerts", MODE_PRIVATE).edit();
                                                            editor.putString("program", SPprogram);
                                                            editor.apply();
                                                        }


                                                        if (SendNotification()){
                                                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
                                                            Intent ii = new Intent(getApplicationContext(), LogInActivity.class);
                                                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, ii, 0);
                                                            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                                                            bigText.setBigContentTitle(StringAlertText);
                                                            Log.e("FIRST","3");

                                                            bigText.setSummaryText(StringAlertTitle);
                                                        mBuilder.setContentIntent(pendingIntent);
                                                        mBuilder.setSmallIcon(R.drawable.logo);
                                                        mBuilder.setPriority(Notification.PRIORITY_MAX);
                                                        mBuilder.setStyle(bigText);
                                                        mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                            String channelId = "YOUR_CHANNEL_ID";
                                                            NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
                                                            mNotificationManager.createNotificationChannel(channel);
                                                            mBuilder.setChannelId(channelId);
                                                        }
                                                        mNotificationManager.notify(1, mBuilder.build());
                                                    }}

                                                }

                                            }
                                            FirebaseDatabase.getInstance().getReference().child("Notification").child(username).child("ProgramAlarm").setValue("");

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
                                            if (!help.equals("")) {
                                                SPprogram = help + "-" + time + "," + SPprogram;
                                                editor = getSharedPreferences("Alerts", MODE_PRIVATE).edit();
                                                editor.putString("program", SPprogram);
                                                editor.apply();

                                            if (SendNotification()){

                                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
                                            Intent ii = new Intent(getApplicationContext(), LogInActivity.class);
                                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, ii, 0);
                                            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                                            bigText.setBigContentTitle(StringAlertText);
                                                Log.e("FIRST","4");

                                                bigText.setSummaryText(StringAlertTitle);
                                            mBuilder.setContentIntent(pendingIntent);
                                            mBuilder.setSmallIcon(R.drawable.logo);
                                            mBuilder.setPriority(Notification.PRIORITY_MAX);
                                            mBuilder.setStyle(bigText);
                                            mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                String channelId = "YOUR_CHANNEL_ID";
                                                NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
                                                mNotificationManager.createNotificationChannel(channel);
                                                mBuilder.setChannelId(channelId);
                                            }
                                            mNotificationManager.notify(1, mBuilder.build());
                                        }}
                                        FirebaseDatabase.getInstance().getReference().child("Notification").child(username).child("ProgramAlarm").setValue("");
                                        }

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
                                            if (!help.substring(0, help.indexOf(",")).equals("")) {
                                                SPchat = "התקבלה הודעה חדשה מ" + help.substring(0, help.indexOf(",")) + "-" + time + "," + SPchat;
                                                editor = getSharedPreferences("Alerts", MODE_PRIVATE).edit();
                                                editor.putString("chat", SPchat);
                                                editor.apply();
                                            }

                                            if (SendNotification()) {

                                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
                                                Intent ii = new Intent(getApplicationContext(), LogInActivity.class);
                                                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, ii, 0);
                                                NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                                                bigText.setBigContentTitle(StringAlertText);
                                                Log.e("FIRST","5");

                                                bigText.setSummaryText(StringAlertTitle);
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
                                                mNotificationManager.notify(1, mBuilder.build());
                                            }
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
                                                    if (!help.substring(0, help.indexOf(",")).equals("")) {

                                                        SPchat = "התקבלה הודעה חדשה מ" + help.substring(0, help.indexOf(",")) + "-" + time + "," + SPchat;
                                                        editor = getSharedPreferences("Alerts", MODE_PRIVATE).edit();
                                                        editor.putString("chat", SPchat);
                                                        editor.apply();
                                                    }
                                                    if (SendNotification()){
                                                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
                                                        Intent ii = new Intent(getApplicationContext(), LogInActivity.class);
                                                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, ii, 0);
                                                        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                                                        bigText.setBigContentTitle(StringAlertText);
                                                        Log.e("FIRST","6");

                                                        bigText.setSummaryText(StringAlertTitle);
                                                    mBuilder.setContentIntent(pendingIntent);
                                                    mBuilder.setSmallIcon(R.drawable.logo);
                                                    mBuilder.setPriority(Notification.PRIORITY_MAX);
                                                    mBuilder.setStyle(bigText);
                                                    mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                        String channelId = "YOUR_CHANNEL_ID";
                                                        NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
                                                        mNotificationManager.createNotificationChannel(channel);
                                                        mBuilder.setChannelId(channelId);
                                                    }
                                                    mNotificationManager.notify(1, mBuilder.build());}
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
                                                        if (!help.equals("")) {

                                                            SPchat = "התקבלה הודעה חדשה מ" + help + "-" + time + "," + SPchat;
                                                            editor = getSharedPreferences("Alerts", MODE_PRIVATE).edit();
                                                            editor.putString("chat", SPchat);
                                                            editor.apply();
                                                        }

                                                        if (SendNotification()){
                                                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
                                                            Intent ii = new Intent(getApplicationContext(), LogInActivity.class);
                                                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, ii, 0);
                                                            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                                                            bigText.setBigContentTitle(StringAlertText);
                                                            Log.e("FIRST","7");

                                                            bigText.setSummaryText(StringAlertTitle);
                                                        mBuilder.setContentIntent(pendingIntent);
                                                        mBuilder.setSmallIcon(R.drawable.logo);
                                                        mBuilder.setPriority(Notification.PRIORITY_MAX);
                                                        mBuilder.setStyle(bigText);
                                                        mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                            String channelId = "YOUR_CHANNEL_ID";
                                                            NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
                                                            mNotificationManager.createNotificationChannel(channel);
                                                            mBuilder.setChannelId(channelId);
                                                        }
                                                        mNotificationManager.notify(1, mBuilder.build());}
                                                    }
                                                }

                                            }
                                        } else {

                                            cal = Calendar.getInstance();
                                            minute = cal.get(Calendar.MINUTE);
                                            hourofday = cal.get(Calendar.HOUR_OF_DAY);
                                            if (minute >= 10) {
                                                time = String.valueOf(hourofday) + ":" + String.valueOf(minute);
                                            } else {
                                                time = String.valueOf(hourofday) + ":0" + String.valueOf(minute);
                                            }
                                            if (!help.equals("")) {

                                                SPchat = "התקבלה הודעה חדשה מ" + help + "-" + time + "," + SPchat;
                                                editor = getSharedPreferences("Alerts", MODE_PRIVATE).edit();
                                                editor.putString("chat", SPchat);
                                                editor.apply();

                                            if (SendNotification()){

                                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
                                            Intent ii = new Intent(getApplicationContext(), LogInActivity.class);
                                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, ii, 0);
                                            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                                            bigText.setBigContentTitle(StringAlertText);
                                                Log.e("FIRST","8");
                                                bigText.setSummaryText(StringAlertTitle);
                                            mBuilder.setContentIntent(pendingIntent);
                                            mBuilder.setSmallIcon(R.drawable.logo);
                                            mBuilder.setPriority(Notification.PRIORITY_MAX);
                                            mBuilder.setStyle(bigText);
                                            mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                String channelId = "YOUR_CHANNEL_ID";
                                                NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
                                                mNotificationManager.createNotificationChannel(channel);
                                                mBuilder.setChannelId(channelId);
                                            }
                                            mNotificationManager.notify(1, mBuilder.build());}
                                            }

                                        }
                                        FirebaseDatabase.getInstance().getReference().child("Notification").child(username).child("Chat").setValue("");
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
                        Log.v("error",e.toString());
                    }
                }
                while (!connected&&i<1)
                {

                    if (WakeUpNotificationcount>=NotificationSec)
                    {
                        Log.v("timer","WakeUpNotifictioncount finish");
                        WakeUpNotificationcount=0;
                        if (SendNotification()){
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
                        Intent ii = new Intent(getApplicationContext(), LogInActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, ii, 0);
                        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                        bigText.setBigContentTitle("הרבה זמן לא נכנסת לאפליקציה");
                        bigText.setSummaryText("תזכורת");
                        mBuilder.setContentIntent(pendingIntent);
                        mBuilder.setSmallIcon(R.drawable.logo);
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

                        mNotificationManager.notify(0, mBuilder.build());}


                    }
                    try {
                        Thread.sleep(1000);
                        Log.v("timer","WakeUpNotifictioncount "+WakeUpNotificationcount);
                        WakeUpNotificationcount++;
                        SharedPreferences prefsss = getSharedPreferences("Alerts", MODE_PRIVATE);
                        SPprogram = prefsss.getString("program", "");
                        SPchat = prefsss.getString("chat", "");

                        onoffref = getSharedPreferences("BackgroundService", MODE_PRIVATE);
                        if (onoff!=onoffref.getInt("onoff",0))
                        {
                            i=1;
                            SPchat="";
                            SPprogram="";
                        }
                    } catch (Exception e) {
                    }
                    Log.v("Connection","ConnectingFlase");
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

            return;

    }

    /**
     * Send notification boolean.
     *
     * @return if the user in the application, to know if need send notification.
     */
    public boolean SendNotification()
    {
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        Log.d("topActivity", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClassName());
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        return componentInfo.getPackageName().indexOf("coach.coach")==-1;
    }

    @Override
    public void onDestroy() {

        Log.v("service", "Destroy");
        onStartCommand(null,0,0);
    }

  }
