package coach.coach;


import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Room of the chat.
 */
public class Chat extends AppCompatActivity {
    EditText etMessageText;
    /**
     * The Intent.
     */
    Intent intent;
    /**
     * The Last date massage.
     */
    String lastdatemassage = "";
    /**
     * The Receiver.
     */
    String receiver,
    /**
     * The Sender.
     */
    sender,
    /**
     * The Room.
     */
    room,
    /**
     * The Type.
     */
    type;
    /**
     * The Chatlvmessages.
     */
    ListView chatlvmessages;
    /**
     * The Adapter.
     */
    SimpleAdapter adapter;
    int day;
    /**
     * The Item.
     */
    HashMap<String, String> item;
    /**
     * The List.
     */
    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    /**
     * The Database reference.
     */
    DatabaseReference databaseReference,
    /**
     * The Chat reference.
     */
    ChatReference,
    /**
     * The Database phone.
     */
    databasePhone;
    /**
     * The Data snap.
     */
    DataSnapshot dataSnap;
    /**
     * The First refresh.
     */
    Boolean FirstRefresh = true;
    /**
     * The Et message text.
     */
    /**
     * The Message string.
     */
    String MessageString;
    /**
     * The Time.
     */
    String time = "";
    /**
     * The Message num.
     */
    int MessageNum;
    /**
     * The Notification chat.
     */
    String NotificationChat = "";
    /**
     * The Chatbuttonsend.
     */
    ImageButton chatbuttonsend;
    /**
     * The Flagint.
     */
    int FLAGINT = 1;
    /**
     * The Update notifiction.
     */
    Boolean UpdateNotifiction;
    /**
     * The Phone.
     */
    String Phone = "";
    /**
     * The Reference.
     */
    DatabaseReference Reference = FirebaseDatabase.getInstance().getReference().child("Notification");
    int lastvisit=0,firstlastvisit=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setTitle("צאט");


        intent = getIntent();
        receiver = intent.getStringExtra("receiver");
        sender = intent.getStringExtra("sender");
        room = intent.getStringExtra("room");
        type = intent.getStringExtra("type");

        Reference = Reference.child(receiver);

        chatbuttonsend = (ImageButton) findViewById(R.id.chatbuttonsend);
        chatlvmessages = (ListView) findViewById(R.id.chatlvmessages);
        chatlvmessages.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        chatlvmessages.setStackFromBottom(true);

        etMessageText = (EditText) findViewById(R.id.etMessageText);

       if (type.equals("User"))
       {
           databasePhone = FirebaseDatabase.getInstance().getReference().child("CoachNames");
       }
       else
       {
           databasePhone = FirebaseDatabase.getInstance().getReference().child("UserNames");
       }

       databasePhone.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Phone = dataSnapshot.child(receiver).getValue().toString();
                Phone=Phone.substring(0,Phone.indexOf(","));

            }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
        });
        firstlastvisit=0;
        lastvisit=0;

        ChatReference = FirebaseDatabase.getInstance().getReference().child("ChatRoom");

        ChatReference.child(room).child("LastVisit").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                firstlastvisit=Integer.valueOf(dataSnapshot.child(sender).getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("ChatRoom").child(room).child("Chat");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnap = dataSnapshot;
                if (dataSnapshot.hasChild("num")) { //TODO שלא יקרוס אחרי מחיקת קשר
                    MessageString = dataSnapshot.child("num").getValue().toString();
                    MessageString = MessageString.substring(5, MessageString.length() - 1);
                    MessageNum = Integer.parseInt(MessageString);

                if (FirstRefresh){
                    RefreshMessages();}
                else
                {
                    Refresh();
                }
                }
                else{ //TODO כדי שאם הקשר יגמר ואז יחזור האפליקציה לא תקרוס בגלל שהצאט התחיל עם הבוליאן לא מאותחל לtrue
                    FirstRefresh=true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("Chat"))
                {
                    NotificationChat=dataSnapshot.child("Chat").getValue().toString();
                    if ((NotificationChat).indexOf(sender+",")!=-1)
                    {
                        if (((NotificationChat).indexOf(","+sender+",")!=-1)||((NotificationChat).indexOf(sender+",")==0))
                        {
                            UpdateNotifiction=false;

                        }
                        else{
                            UpdateNotifiction=true;

                        }
                    }
                    else
                    {
                        UpdateNotifiction=true;

                    }
                }
                else {
                    UpdateNotifiction=true;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        adapter = new SimpleAdapter(this, list,
                R.layout.twolines,
                new String[] { "sender","receiver","sendertime","receivertime","datemassage"},
                new int[] {R.id.sendmessage, R.id.receivemessage,R.id.sendertime,R.id.receivetime,R.id.datemessage});

        chatlvmessages.setStackFromBottom(true);


    }

    /**
     * Refreshing messages since entered to activity.
     */
    public void Refresh()
    {
        FLAGINT++;
        if (FLAGINT%2==0)
        {
            item = new HashMap<String,String>();

            String messagedata =dataSnap.child(String.valueOf(MessageNum)).child("message").getValue().toString();
            String mesasgesender =dataSnap.child(String.valueOf(MessageNum)).child("sender").getValue().toString();
            int timebefore,timeafter;
            String mesasgetime =dataSnap.child(String.valueOf(MessageNum)).child("time").getValue().toString();
            timebefore = Integer.parseInt(mesasgetime.substring(1,mesasgetime.indexOf(":")));
            SimpleDateFormat sourceFormattime = new SimpleDateFormat(" HH:mm ");
            sourceFormattime.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date parsedtime = null;
            try {
                parsedtime = sourceFormattime.parse(mesasgetime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            TimeZone tz = cal.getTimeZone();
            SimpleDateFormat destFormattime = new SimpleDateFormat(" HH:mm ");
            destFormattime.setTimeZone(tz);
            String result = destFormattime.format(parsedtime);
            mesasgetime=result;
            timeafter = Integer.parseInt(mesasgetime.substring(1,mesasgetime.indexOf(":")));
            String messagedate = dataSnap.child(String.valueOf(MessageNum)).child("date").getValue().toString();

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
            Date currentLocalTime = calendar.getTime();
            DateFormat date = new SimpleDateFormat("Z");
            String localTime = date.format(currentLocalTime);
            if (localTime.indexOf(0)=='-')
            {
                if (timeafter>timebefore)
                {

                    day = Integer.parseInt(messagedate.substring(0,messagedate.indexOf("/")));
                    if (day>1)
                    {
                        day=day-1;
                    }
                    else
                    {
                        day=30;
                    }
                    messagedate=day+messagedate.substring(messagedate.indexOf("/"));
                }
            }
            else
            {

                    if (timebefore>timeafter)
                    {

                        day = Integer.parseInt(messagedate.substring(0,messagedate.indexOf("/")));
                        if (day<30)
                        {
                            day=day+1;
                        }
                        else
                        {
                            day=1;
                        }
                        messagedate=day+messagedate.substring(messagedate.indexOf("/"));
                     }
            }




            if (lastdatemassage.equals(messagedate))
            {
                messagedate="";
            }
            else {
                lastdatemassage=messagedate;
                messagedate=" "+messagedate+" ";

            }
            if (mesasgesender.equals(sender))
            {
                item.put("datemassage",messagedate);
                item.put("sender",messagedata);
                item.put("sendertime",mesasgetime);
                list.add(item);
            }
            else {
                item.put("datemassage","");
                item.put("receiver",messagedata);
                item.put("receivertime",mesasgetime);


                list.add(item);
            }
            lastvisit++;
            if (InChat().equals("coach.coach.Chat")){
            FirebaseDatabase.getInstance().getReference().child("ChatRoom").child(room).child("LastVisit").child(sender).setValue(lastvisit);}

        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Refresh all the history of the chat.
     */
    public void RefreshMessages()
    {
        SimpleDateFormat sourceFormattime = new SimpleDateFormat(" HH:mm ");
        sourceFormattime.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        SimpleDateFormat destFormattime = new SimpleDateFormat(" HH:mm ");
        destFormattime.setTimeZone(tz);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
        Date currentLocalTime = calendar.getTime();
        if (FirstRefresh){
            FirstRefresh=false;
            for (int i=0; i<MessageNum-1; i++)
            {
                int timebefore,timeafter;

                item = new HashMap<String,String>();
                String messagedata =dataSnap.child(String.valueOf(i+1)).child("message").getValue().toString();
                String messagesender = dataSnap.child(String.valueOf(i+1)).child("sender").getValue().toString();
                String messagetime =dataSnap.child(String.valueOf(i+1)).child("time").getValue().toString();
                timebefore = Integer.parseInt(messagetime.substring(1,messagetime.indexOf(":")));
                Date parsedtime = null;
                try {
                    parsedtime = sourceFormattime.parse(messagetime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String result = destFormattime.format(parsedtime);
                messagetime=result;
                timeafter = Integer.parseInt(messagetime.substring(1,messagetime.indexOf(":")));
                String messagedate = dataSnap.child(String.valueOf(i+1)).child("date").getValue().toString();
                DateFormat date = new SimpleDateFormat("Z");
                String localTime = date.format(currentLocalTime);
                if (localTime.indexOf(0)=='-')
                {
                    if (timeafter>timebefore)
                    {

                        day = Integer.parseInt(messagedate.substring(0,messagedate.indexOf("/")));
                        if (day>1)
                        {
                            day=day-1;
                        }
                        else
                        {
                            day=30;
                        }
                        messagedate=day+messagedate.substring(messagedate.indexOf("/"));
                    }
                }
                else
                {

                    if (timebefore>timeafter)
                    {

                        day = Integer.parseInt(messagedate.substring(0,messagedate.indexOf("/")));
                        if (day<30)
                        {
                            day=day+1;
                        }
                        else
                        {
                            day=1;
                        }
                        messagedate=day+messagedate.substring(messagedate.indexOf("/"));
                    }
                }
                if (lastdatemassage.equals(messagedate))
                {
                    messagedate="";
                }
                else {
                    lastdatemassage=messagedate;
                    messagedate=" "+messagedate+" ";
                }

                if (messagesender.equals(sender))
                {
                    item.put("sender",messagedata);
                    item.put("receiver","");
                    item.put("receivertime","");
                    item.put("sendertime",messagetime);
                    item.put("datemassage",messagedate);
                    list.add(item);
                }
                else {
                    item.put("receiver",messagedata);
                    item.put("sender","");
                    item.put("receivertime",messagetime);
                    item.put("sendertime","");
                    item.put("datemassage",messagedate);
                    list.add(item);
                }

            }
            chatlvmessages.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            lastvisit =chatlvmessages.getAdapter().getCount();
            FirebaseDatabase.getInstance().getReference().child("ChatRoom").child(room).child("LastVisit").child(sender).setValue(lastvisit);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int scroll = (MessageNum-firstlastvisit-1)*-1;
                    if (scroll!=0)
                    {
                        chatlvmessages.smoothScrollByOffset(scroll);
                        Toast.makeText(getBaseContext(),"גלול למטה להודעות חדשות",Toast.LENGTH_LONG).show();

                    }

                }
            }, 100);

        }



    }
    private void Message(String sender, final String message)  {
        etMessageText.setText("");
        Calendar cal = Calendar.getInstance();
        DateFormat dfHH = new SimpleDateFormat("HH");
        DateFormat dfmm = new SimpleDateFormat("mm");
        String HH = dfHH.format(new Date());
        String mm = dfmm.format(new Date());
        DateFormat gdfHH = new SimpleDateFormat("HH");
        DateFormat gdfmm = new SimpleDateFormat("mm");
        gdfHH.setTimeZone(TimeZone.getTimeZone("GMT"));
        gdfmm.setTimeZone(TimeZone.getTimeZone("GMT"));
        String gHH = gdfHH.format(new Date());
        String gmm = gdfmm.format(new Date());
        int hourdif =Integer.parseInt(HH)-Integer.parseInt(gHH);
        int mindif =Integer.parseInt(mm)-Integer.parseInt(gmm);
        cal.add(Calendar.MINUTE,-1* mindif);
        cal.add(Calendar.HOUR_OF_DAY, -1*hourdif);


        int minute = cal.get(Calendar.MINUTE);
        int hourofday = cal.get(Calendar.HOUR_OF_DAY);
        int month = cal.get(Calendar.MONTH);
        month++;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        String date = String.valueOf(day)+"/"+String.valueOf(month)+"/"+String.valueOf(year);
        if (minute>=10){
            time = " "+String.valueOf(hourofday)+":"+String.valueOf(minute)+" ";}
        else
        {
            time =" "+String.valueOf(hourofday)+":0"+String.valueOf(minute)+" ";
        }


        HashMap<String, String> newmessage = new HashMap<>();
        newmessage.put("sender",sender);
        newmessage.put("message",message);
        newmessage.put("time",time);
        newmessage.put("date",date);


        ChatReference.child(room).child("Chat").child(""+MessageNum).setValue(newmessage);
        MessageNum++;
        ChatReference.child(room).child("Chat").child("num").child("num").setValue(MessageNum);
        if (UpdateNotifiction)
        {
            Reference.child("Chat").setValue(NotificationChat+sender+",");
        }

        etMessageText.setEnabled(false);
        chatbuttonsend.setEnabled(false);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                etMessageText.setEnabled(true);
                chatbuttonsend.setEnabled(true);
            }
        }, 410);
    }


    /**
     * Send message.
     *
     * @param view the view
     */
    public void SendMessage(View view) {
        if (!etMessageText.getText().toString().trim().equals("")) {
            ConnectivityManager connectivityManager;
            connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                Message(sender,etMessageText.getText().toString());

            }
            else {
                Toast.makeText(getBaseContext(),"אין חיבור לאינטרנט, ההודעה לא נשלחה",Toast.LENGTH_LONG).show();
            }
        }


    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat, menu);
        /*if (Phone.charAt(0)=='=')
        {
            menu.add(Phone.substring(1)+"").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }*/
        return true;
    }
    public String InChat()
    {
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        return componentInfo.getClassName();
    }

}
