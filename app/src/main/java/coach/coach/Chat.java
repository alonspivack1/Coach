package coach.coach;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Chat extends AppCompatActivity {
    Intent intent;
    String receiver,sender,room;
    ListView chatlvmessages;
    SimpleAdapter adapter;
    HashMap<String,String> item;
    ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
    String[][]Messages = new String[1000][1000];
    DatabaseReference databaseReference;
    DataSnapshot dataSnap;
    Boolean StartListener=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        intent = getIntent();
        receiver=intent.getStringExtra("receiver");
        sender=intent.getStringExtra("sender");
        room=intent.getStringExtra("room");

        chatlvmessages = (ListView)findViewById(R.id.chatlvmessages);
        chatlvmessages.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        chatlvmessages.setStackFromBottom(true);


            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("ChatRoom").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dataSnap = dataSnapshot;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        adapter = new SimpleAdapter(this, list,
                R.layout.twolines,
                new String[] { "sender","receiver" },
                new int[] {R.id.sendmessage, R.id.receivemessage});

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                RefreshMessages(); } }, 100);

               item = new HashMap<String,String>();
               item.put( "sender", "אני השולח"+sender);
               item.put( "receiver", "");
               list.add( item) ;
               item = new HashMap<String,String>();
               item.put( "sender", "");
               item.put( "receiver", "אני המקבל"+receiver);
               list.add( item) ;
             chatlvmessages.setAdapter(adapter);

    }
    public void RefreshMessages()
    {
        StartListener=true;
        Log.e("DATA!",dataSnap.child(room).getValue().toString());


    }
}
