package coach.coach;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
/*
*       Calendar cal = Calendar.getInstance();
        int minute = cal.get(Calendar.MINUTE);
        int hourofday = cal.get(Calendar.HOUR_OF_DAY);
        String time = String.valueOf(hourofday)+":"+String.valueOf(minute);
        Log.e("time",time);
*/
public class Chat extends AppCompatActivity {
    Intent intent;
    String receiver,sender,room,type;
    ListView chatlvmessages;
    SimpleAdapter adapter;
    HashMap<String,String> item;
    ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
    DatabaseReference databaseReference,ChatReference,databasePhone;
    DataSnapshot dataSnap;
    Boolean FirstRefresh=true;
    EditText etMessageText;
    String MessageString;
    int MessageNum;
    String NotificationChat="";
    ImageButton chatbuttonsend;
    int FLAGINT=1;
    Boolean UpdateNotifiction;
    String Phone="";
    DatabaseReference Reference=FirebaseDatabase.getInstance().getReference().child("Notification");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setTitle("צאט");


        intent = getIntent();
        receiver=intent.getStringExtra("receiver");
        sender=intent.getStringExtra("sender");
        room=intent.getStringExtra("room");
        type=intent.getStringExtra("type");

        Reference = Reference.child(receiver);

        chatbuttonsend = (ImageButton)findViewById(R.id.chatbuttonsend);
        etMessageText = (EditText)findViewById(R.id.etMessageText);
        chatlvmessages = (ListView)findViewById(R.id.chatlvmessages);
        chatlvmessages.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        chatlvmessages.setStackFromBottom(true);

       /* TextView textView = (TextView)findViewById(R.id.receivemessage);
        textView.setHint("asdfasdfasdfasdfsd");*/
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

                Log.e("PHONEE",dataSnapshot.toString());
                Phone = dataSnapshot.child(receiver).getValue().toString();
                Log.e("PHONEE",Phone);
                Phone=Phone.substring(0,Phone.indexOf(","));

            }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
        });


        ChatReference = FirebaseDatabase.getInstance().getReference().child("ChatRoom");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("ChatRoom").child(room);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnap = dataSnapshot;
                if (dataSnapshot.hasChild("num")) { //TODO שלא יקרוס אחרי מחיקת קשר
                    MessageString = dataSnapshot.child("num").getValue().toString();
                    MessageString = MessageString.substring(5, MessageString.length() - 1);
                    Log.e("MessageString", MessageString);
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
                    /*if (FLAGINT%2!=0){
                        try
                        {
                            Thread.sleep(400);
                            RefreshMessages();
                            FLAGINT++;
                        }
                        catch(InterruptedException ex)
                        {
                            Thread.currentThread().interrupt();
                        }}**


                   /* if (FirstRefresh){
                        MessageNumNow=MessageNum;
                    }*/
                    /*if (FirstRefresh)
                    {
                        /*String j =dataSnapshot.child(room).child("25").getValue().toString();
                        Log.e("datasnap",j);
                        Log.e("datasnapm=",j.substring(9,j.length()-21-receiver.length()-sender.length()));
                        Log.e("datasnapr=",j.substring(j.length()-10-receiver.length()-sender.length(),j.lastIndexOf(",")));
                        Log.e("datasnaps=",j.substring(j.lastIndexOf(",")+9,j.length()-1));
                        MessageString = dataSnapshot.child(room).child("num").getValue().toString();
                    MessageString = MessageString.substring(5,MessageString.length()-1);
                    Log.e("MessageString",MessageString);
                    MessageNum = Integer.parseInt(MessageString);
                        RefreshMessages();
                    }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.v("NotificationChat","1");
                if (dataSnapshot.hasChild("Chat"))
                {
                    Log.v("NotificationChat","2");

                    NotificationChat=dataSnapshot.child("Chat").getValue().toString();
                    if ((NotificationChat).indexOf(sender+",")!=-1)
                    {
                        Log.v("NotificationChat","3");

                        if (((NotificationChat).indexOf(","+sender+",")!=-1)||((NotificationChat).indexOf(sender+",")==0))
                        {
                            Log.v("NotificationChat","4");

                            UpdateNotifiction=false;

                        }
                        else{
                            Log.v("NotificationChat","5");

                            UpdateNotifiction=true;

                        }
                    }
                    else
                    {
                        Log.v("NotificationChat","6");

                        UpdateNotifiction=true;

                    }
                }
                else {
                    Log.v("NotificationChat","7");

                    UpdateNotifiction=true;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        adapter = new SimpleAdapter(this, list,
                R.layout.twolines,
                new String[] { "sender","receiver","sendertime","receivertime"},
                new int[] {R.id.sendmessage, R.id.receivemessage,R.id.sendertime,R.id.receivetime});

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RefreshMessages(); } }, 100);


    }

    public void Refresh()
    {
        FLAGINT++;
        if (FLAGINT%2==0)
        {
            item = new HashMap<String,String>();

            String messagedata =dataSnap.child(String.valueOf(MessageNum)).child("message").getValue().toString();
            String mesasgesender =dataSnap.child(String.valueOf(MessageNum)).child("sender").getValue().toString();
            String mesasgetime =dataSnap.child(String.valueOf(MessageNum)).child("time").getValue().toString();

            if (mesasgesender.equals(sender))
            {
                Log.e("Time","3");
                item.put("sender",messagedata);
               // item.put("receiver","");
              //  item.put("receivertime","");
                item.put("sendertime"," "+mesasgetime+" ");
                list.add(item);
            }
            else {
                Log.e("Time","4");
                item.put("receiver",messagedata);
              //  item.put("sender","");
                item.put("receivertime"," "+mesasgetime+" ");

               // item.put("sendertime","");

                list.add(item);
            }
            /*item = new HashMap<String,String>();
                String messagedata =dataSnap.child(String.valueOf(i+1)).child("message").getValue().toString();
                String mesasgesender = dataSnap.child(String.valueOf(i+1)).child("sender").getValue().toString();
                if (mesasgesender.equals(sender))
                {
                    Log.e("Time","3");
                    item.put("sender",messagedata);
                    item.put("receiver","");
                    list.add(item);
                }
                else {
                    Log.e("Time","4");
                    item.put("receiver",messagedata);
                    item.put("sender","");
                    list.add(item);
                }*/
        /*    if (Emulator){
        Log.e("Refresh","num="+MessageNum);
            item = new HashMap<String,String>();
            String messagedata =dataSnap.child(String.valueOf(MessageNum)).getValue().toString();
            if ((messagedata.substring(messagedata.lastIndexOf(",")+9,messagedata.length()-1)).equals(sender))
            {
                Log.e("Time","3");
                item.put("sender",messagedata.substring(9,messagedata.length()-21-receiver.length()-sender.length()));
                item.put("receiver","");
                list.add(item);
            }
            else {
                Log.e("Time","4");
                item.put("receiver",messagedata.substring(9,messagedata.length()-21-receiver.length()-sender.length()));
                item.put("sender","");
                list.add(item);
            }

        }
        else
            {
                item = new HashMap<String,String>();
                String messagedata =dataSnap.child(String.valueOf(MessageNum)).getValue().toString();
                if ((messagedata.substring(10,messagedata.indexOf(","))).equals(receiver))
                {
                    Log.e("Time","3");
                    item.put("receiver","");
                    item.put("sender",messagedata.substring(receiver.length()+sender.length()+29,messagedata.length()-1));
                    list.add(item);

                }
                else {
                    Log.e("Time","4");
                    item.put("sender","");
                    item.put("receiver",messagedata.substring(receiver.length()+sender.length()+29,messagedata.length()-1));
                    list.add(item);
                }
            }*/
        }
        //chatlvmessages.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void RefreshMessages()
    {
        if (FirstRefresh){
            FirstRefresh=false;
            for (int i=0; i<MessageNum-1; i++)
            {
                item = new HashMap<String,String>();
                String messagedata =dataSnap.child(String.valueOf(i+1)).child("message").getValue().toString();
                String mesasgesender = dataSnap.child(String.valueOf(i+1)).child("sender").getValue().toString();
                String mesasgetime =dataSnap.child(String.valueOf(i+1)).child("time").getValue().toString();
                if (mesasgesender.equals(sender))
                {
                    Log.e("Time","3");
                    item.put("sender",messagedata);
                    item.put("receiver","");
                    item.put("receivertime","");
                    item.put("sendertime"," "+mesasgetime+" ");
                    list.add(item);
                }
                else {
                    Log.e("Time","4");
                    item.put("receiver",messagedata);
                    item.put("sender","");
                    item.put("receivertime"," "+mesasgetime+" ");
                    item.put("sendertime","");
                    list.add(item);
                }

            }
            chatlvmessages.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            /*if (Emulator){
            for (int i=0; i<MessageNum-1; i++)
            {
                item = new HashMap<String,String>();
                String messagedata =dataSnap.child(String.valueOf(i+1)).getValue().toString();
                Log.e("ItemInfoMessagedata1",messagedata);
                Log.e("ItemInfoSender1",messagedata.substring(messagedata.lastIndexOf(",")+9,messagedata.length()-1));
                Log.e("ItemInfoMessage1",messagedata.substring(9,messagedata.length()-21-receiver.length()-sender.length()));
                if ((messagedata.substring(messagedata.lastIndexOf(",")+9,messagedata.length()-1)).equals(sender))
                {
                    Log.e("Time","3");
                    item.put("sender",messagedata.substring(9,messagedata.length()-21-receiver.length()-sender.length()));
                    item.put("receiver","");
                    list.add(item);
                }
                else {
                    Log.e("Time","4");
                    item.put("receiver",messagedata.substring(9,messagedata.length()-21-receiver.length()-sender.length()));
                    item.put("sender","");
                    list.add(item);
                }

            }
            chatlvmessages.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
            else{
                for (int i=0; i<MessageNum-1; i++)
                {
                    item = new HashMap<String,String>();

                    String messagedata =dataSnap.child(String.valueOf(i+1)).getValue().toString();
                    Log.e("ItemInfoMessagedata2",messagedata);
                    Log.e("ItemInfoReciver2",messagedata.substring(10,messagedata.indexOf(",")));
                    Log.e("ItemInfoMessage2",messagedata.substring(receiver.length()+sender.length()+29,messagedata.length()-1));

                    if ((messagedata.substring(10,messagedata.indexOf(","))).equals(receiver))
                    {
                        Log.e("Time","3");
                        item.put("receiver","");
                        item.put("sender",messagedata.substring(receiver.length()+sender.length()+29,messagedata.length()-1));
                        list.add(item);

                    }
                    else {
                        Log.e("Time","4");
                        item.put("sender","");
                        item.put("receiver",messagedata.substring(receiver.length()+sender.length()+29,messagedata.length()-1));
                        list.add(item);
                    }

                }
                chatlvmessages.setAdapter(adapter);
                adapter.notifyDataSetChanged();
        }*/

        }


    }
    private void Message(String sender, String receiver, final String message) {
        etMessageText.setText("");

        Calendar cal = Calendar.getInstance();
        int minute = cal.get(Calendar.MINUTE);
        int hourofday = cal.get(Calendar.HOUR_OF_DAY);
        String time = String.valueOf(hourofday)+":"+String.valueOf(minute);
        Log.e("time",time);

        HashMap<String, String> newmessage = new HashMap<>();
        newmessage.put("sender",sender);
        newmessage.put("receiver",receiver);
        newmessage.put("message",message);
        newmessage.put("time",time);


        ChatReference.child(room).child(""+MessageNum).setValue(newmessage);
        MessageNum++;
        ChatReference.child(room).child("num").child("num").setValue(MessageNum);
        if (UpdateNotifiction)
        {
            Reference.child("Chat").setValue(NotificationChat+sender+",");
        }
        /*ChatReference.child(room).child(""+MessageNum).setValue(newmessage).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()){
                    MessageNum++;
                    ChatReference.child(room).child("num").child("num").setValue(MessageNum);
                    chatlvmessages.setSelection(MessageNum);
                    item = new HashMap<String,String>();
                    item.put( "sender", message);
                    item.put( "receiver", "");
                    list.add( item) ;
                    adapter.notifyDataSetChanged();



                }
            }});*/
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


    public void SendMessage(View view) {
        if (!etMessageText.getText().toString().equals("")) {
            Message(sender, receiver, etMessageText.getText().toString());

        }
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat, menu);
        if (Phone.charAt(0)=='=')
        {
            menu.add(Phone.substring(1)+"").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return true;
    }

}
