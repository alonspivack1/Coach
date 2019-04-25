package coach.coach;


import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Room of the chat.
 */
public class Chat extends AppCompatActivity {
    /**
     * The Intent.
     */
    Intent intent;
    /**
     * The Last date massage.
     */
    String lastdatemassage="";
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
    /**
     * The Item.
     */
    HashMap<String,String> item;
    /**
     * The List.
     */
    ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
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
    Boolean FirstRefresh=true;
    /**
     * The Et message text.
     */
    EditText etMessageText;
    /**
     * The Message string.
     */
    String MessageString;
    /**
     * The Time.
     */
    String time="";
    /**
     * The Message num.
     */
    int MessageNum;
    /**
     * The Notification chat.
     */
    String NotificationChat="";
    /**
     * The Chatbuttonsend.
     */
    ImageButton chatbuttonsend;
    /**
     * The Flagint.
     */
    int FLAGINT=1;
    /**
     * The Update notifiction.
     */
    Boolean UpdateNotifiction;
    /**
     * The Phone.
     */
    String Phone="";
    /**
     * The Reference.
     */
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


        ChatReference = FirebaseDatabase.getInstance().getReference().child("ChatRoom");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("ChatRoom").child(room);
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

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RefreshMessages(); } }, 100);


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
            String mesasgetime =dataSnap.child(String.valueOf(MessageNum)).child("time").getValue().toString();
            String messagedate = dataSnap.child(String.valueOf(MessageNum)).child("date").getValue().toString();
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

        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Refresh all the history of the chat.
     */
    public void RefreshMessages()
    {
        if (FirstRefresh){
            FirstRefresh=false;
            for (int i=0; i<MessageNum-1; i++)
            {
                item = new HashMap<String,String>();
                String messagedata =dataSnap.child(String.valueOf(i+1)).child("message").getValue().toString();
                String messagesender = dataSnap.child(String.valueOf(i+1)).child("sender").getValue().toString();
                String messagetime =dataSnap.child(String.valueOf(i+1)).child("time").getValue().toString();
                String messagedate = dataSnap.child(String.valueOf(i+1)).child("date").getValue().toString();
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
                    item.put("sendertime"," "+messagetime+" ");
                    item.put("datemassage",messagedate);
                    list.add(item);
                }
                else {
                    item.put("receiver",messagedata);
                    item.put("sender","");
                    item.put("receivertime"," "+messagetime+" ");
                    item.put("sendertime","");
                    item.put("datemassage",messagedate);
                    list.add(item);
                }

            }
            chatlvmessages.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }


    }
    private void Message(String sender, String receiver, final String message) {
        etMessageText.setText("");
        Calendar cal = Calendar.getInstance();
        int minute = cal.get(Calendar.MINUTE);
        int hourofday = cal.get(Calendar.HOUR_OF_DAY);
        int month = cal.get(Calendar.MONTH);
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
        newmessage.put("receiver",receiver);
        newmessage.put("message",message);
        newmessage.put("time",time);
        newmessage.put("date",date);


        ChatReference.child(room).child(""+MessageNum).setValue(newmessage);
        MessageNum++;
        ChatReference.child(room).child("num").child("num").setValue(MessageNum);
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
