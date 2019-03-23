package coach.coach;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class TimerService extends IntentService {
    private ConnectivityManager connectivityManager;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Notifcation");
    NotificationCompat.Builder nb;
    NotificationManager nm;
    private DataSnapshot dataSnap=null;
    int IdNotifcation=1;
    Boolean Sticky=false,FirstLook=true;
    int WakeUpNotifictioncount=0,NotifictionSec=60;
    private boolean connected,connectedlog;
    private String username;

    public TimerService()
    {
        super("Timer Service");
    }
    public void onCreate(){

    super.onCreate();
    Log.v("timer","Timer service has started");
    }

    public int onStartCommand(Intent intent,int flags,int startId){
        super.onStartCommand(intent,flags,startId);
        Sticky=false;
        Log.v("timer","onn");
        return START_STICKY;
    };


    @Override
    protected void onHandleIntent(Intent intent) {

       nb = new NotificationCompat.Builder(getBaseContext());



        if (intent==null){
            Sticky=true;
            SharedPreferences prefs = getSharedPreferences("service", MODE_PRIVATE);
            username = prefs.getString("username","");
            Boolean SignOut = prefs.getBoolean("signout",true);

            if (!SignOut){
            databaseReference=databaseReference.child(username);
            Log.v("timerusername",username+SignOut);

            //TODO למחוק את כל הילד בשם צאט ביציאה מהאפליקציה כדי שלא סתם ישלח הודעות
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
                    if (WakeUpNotifictioncount>=NotifictionSec)
                {
                    Log.v("timer","WakeUpNotifictioncount"+" finish");
                    WakeUpNotifictioncount=0;
                    nb = new NotificationCompat.Builder(getApplicationContext());
                    nb.setContentText("המאמנים מחכים לך");
                    nb.setContentTitle("תזכורת להתאמן");
                    nb.setSmallIcon(R.drawable.unimage);
                    nm =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    nm.notify(IdNotifcation,nb.build());
                    IdNotifcation++;
                }
                    try {
                        Thread.sleep(1000);
                        Log.v("timer","WakeUpNotifictioncount"+" "+WakeUpNotifictioncount);
                        WakeUpNotifictioncount++;
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
                    if (WakeUpNotifictioncount>=NotifictionSec)
                    {
                        Log.v("timer","WakeUpNotifictioncount"+" finish");
                        WakeUpNotifictioncount=0;
                        nb = new NotificationCompat.Builder(getApplicationContext());
                        nb.setContentText("המאמנים מחכים לך");
                        nb.setContentTitle("תזכורת להתאמן");
                        nb.setSmallIcon(R.drawable.unimage);
                        nm =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        nm.notify(IdNotifcation,nb.build());
                        IdNotifcation++;
                    }
                    try {
                        Thread.sleep(1000);
                        Log.v("timer","WakeUpNotifictioncount"+" "+WakeUpNotifictioncount);
                        WakeUpNotifictioncount++;
                    } catch (Exception e) {
                    }

                    if (!connectedlog) {
                        Log.v("timer","ConnectingTrue");
                        connectedlog=true;
                    }
                    connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                    if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        //we are connected to a network
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
                                            nb = new NotificationCompat.Builder(getApplicationContext());
                                            nb.setContentText(help.substring(0, help.indexOf(",")) + " עדכן את התוכנית אימון");
                                            nb.setContentTitle("תוכנית אימון התעדכנה");
                                            nb.setSmallIcon(R.drawable.unimage);
                                            nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                            nm.notify(IdNotifcation, nb.build());
                                            IdNotifcation++;
                                            while (help.indexOf(",") != -1) {

                                                help = help.substring(help.indexOf(",") + 1);
                                                if (help.indexOf(",") != -1) {
                                                    Log.v("timer", help.substring(0, help.indexOf(",")));
                                                    nb = new NotificationCompat.Builder(getApplicationContext());
                                                    nb.setContentText(help.substring(0, help.indexOf(",")) + " עדכן את התוכנית אימון");
                                                    nb.setContentTitle("תוכנית אימון התעדכנה");
                                                    nb.setSmallIcon(R.drawable.unimage);
                                                    nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                    nm.notify(IdNotifcation, nb.build());
                                                    IdNotifcation++;
                                                } else {
                                                    if (!help.equals("")){
                                                        Log.v("timer", help);
                                                    nb = new NotificationCompat.Builder(getApplicationContext());
                                                    nb.setContentText(help + " עדכן את התוכנית אימון");
                                                    nb.setContentTitle("תוכנית אימון התעדכנה");
                                                    nb.setSmallIcon(R.drawable.unimage);
                                                    nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                    nm.notify(IdNotifcation, nb.build());
                                                    IdNotifcation++;}

                                                }

                                            }
                                        } else {
                                            Log.v("timer", help);
                                            nb = new NotificationCompat.Builder(getApplicationContext());
                                            nb.setContentText(help + " עדכן את התוכנית אימון");
                                            nb.setContentTitle("תוכנית אימון התעדכנה");
                                            nb.setSmallIcon(R.drawable.unimage);
                                            nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                            nm.notify(IdNotifcation, nb.build());
                                            IdNotifcation++;

                                        }
                                        DatabaseReference deleteProgramAlarm = FirebaseDatabase.getInstance().getReference().child("Notifcation").child(username).child("ProgramAlarm");
                                        deleteProgramAlarm.removeValue();
                                    }
                                    if (dataSnapshot.hasChild("Chat")) {
                                        String help = dataSnapshot.child("Chat").getValue().toString();
                                        if (help.indexOf(",") != -1) {
                                            Log.v("timer", help.substring(0, help.indexOf(",")));
                                            nb = new NotificationCompat.Builder(getApplicationContext());
                                            nb.setContentText(help.substring(0, help.indexOf(",")) + " שלח הודעה חדשה");
                                            nb.setContentTitle("הודעה חדשה התקבלה");
                                            nb.setSmallIcon(R.drawable.unimage);
                                            nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                            nm.notify(IdNotifcation, nb.build());
                                            IdNotifcation++;
                                            while (help.indexOf(",") != -1) {
                                                help = help.substring(help.indexOf(",") + 1);
                                                if (help.indexOf(",") != -1) {
                                                    Log.v("timer", help.substring(0, help.indexOf(",")));
                                                    nb = new NotificationCompat.Builder(getApplicationContext());
                                                    nb.setContentText(help.substring(0, help.indexOf(",")) + " שלח הודעה חדשה");
                                                    nb.setContentTitle("הודעה חדשה התקבלה");
                                                    nb.setSmallIcon(R.drawable.unimage);
                                                    nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                    nm.notify(IdNotifcation, nb.build());
                                                    IdNotifcation++;
                                                } else {
                                                    if (!help.equals("")){
                                                    Log.v("timer", help);
                                                    nb = new NotificationCompat.Builder(getApplicationContext());
                                                    nb.setContentText(help + " שלח הודעה חדשה");
                                                    nb.setContentTitle("הודעה חדשה התקבלה");
                                                    nb.setSmallIcon(R.drawable.unimage);
                                                    nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                    nm.notify(IdNotifcation, nb.build());
                                                    IdNotifcation++;}

                                                }

                                            }
                                        } else {
                                            Log.v("timer", help);
                                            nb = new NotificationCompat.Builder(getApplicationContext());
                                            nb.setContentText(help + " שלח הודעה חדשה");
                                            nb.setContentTitle("הודעה חדשה התקבלה");
                                            nb.setSmallIcon(R.drawable.unimage);
                                            nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                            nm.notify(IdNotifcation, nb.build());
                                            IdNotifcation++;

                                        }
                                        DatabaseReference deleteChat = FirebaseDatabase.getInstance().getReference().child("Notifcation").child(username).child("Chat");
                                        deleteChat.removeValue();

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

            }/*
            int time = 50;
            for (int i =0; i<time;i++){
                Log.v("timer","i (intent is null) = "+i);

            try {
            Thread.sleep(1000);
            }
            catch (Exception e) {

            }
            }
            NotificationCompat.Builder nb = new NotificationCompat.Builder(this);
            nb.setContentText("Timer done...");
            nb.setContentTitle("Hi!");
            nb.setSmallIcon(R.drawable.unimage);

            NotificationManager nm =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.notify(1,nb.build());*/
            return;
        }}
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
}
