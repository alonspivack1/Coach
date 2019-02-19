package coach.coach;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentChat extends Fragment{


    DatabaseReference databaseReference;
    DataSnapshot dataSnap;
    Coach[] coaches = new Coach[3];
    User[] users = new User[3];
    int i =0;
    String username,type,sub,help;
    ArrayList<Coach> coachesList;
    ArrayList<User> usersList;
    ListView listView;
    String receiver,room;

    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container,false);
        listView = (ListView) view.findViewById(R.id.chatlist);
        coachesList = new ArrayList<>();
        usersList = new ArrayList<>();

        MainActivity activity = (MainActivity) getActivity();
        username = activity.getUsername();
        type = activity.getType();


        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnap = dataSnapshot;
                //fragtv.setText(CoachProfiles+"");
                // i++;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (type.equals("User"))
                {
                    Intent ChatIntent = new Intent(getActivity(),Chat.class);
                    receiver=coaches[position].getName();
                    room = username+"&"+receiver;
                    ChatIntent.putExtra("receiver",receiver);
                    ChatIntent.putExtra("sender",username);
                    ChatIntent.putExtra("room",room);
                    startActivity(ChatIntent);
                    Log.e("receiver",receiver);
                    Log.e("sender",username);
                    Log.e("room",room);
                }
                if (type.equals("Coach"))
                {
                    receiver=users[position].getName();
                    room = receiver+"&"+username;
                    Intent ChatIntent = new Intent(getActivity(),Chat.class);
                    ChatIntent.putExtra("receiver",receiver);
                    ChatIntent.putExtra("sender",username);
                    ChatIntent.putExtra("room",room);
                    startActivity(ChatIntent);
                    Log.e("receiver",receiver);
                    Log.e("sender",username);
                    Log.e("room",room);
                }
            }
        });

        try {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (type.equals("User")) {
                        UserRefreshProgramlist();
                    }
                    if (type.equals("Coach"))
                    {
                        CoachRefreshProgramlist();
                    }
                }
            }, 100);
        } catch (Exception e) {
            System.out.println("An exception!");
        }

        return view;

    }

    public void UserRefreshProgramlist(){
        Log.e("Program",dataSnap.child("UserNames").child(username).getValue().toString());

        //Coach coach = new Coach("pp","{{StudyPlace}=באר שבע, {Professionalization}=,שריפת שומנים,אימוני כוח בחדר כושר,אימוני כוח בסטרייט,אימונים בבית,שיפור מרחק בריצות,שיפור מהירות בריצות,, {Age}=26, {CoachTime}=שנתיים, {Gender}=Male, {Description}=אין מה לתאר}");
        help =dataSnap.child("UserNames").child(username).getValue().toString();
        sub = help.substring((help.indexOf(","+username+","))+2+username.length(),help.length());
        Log.e("ProgramFull",sub);


        int index = sub.indexOf(",");
        Log.e("Index",index+"");
        Log.e("sub", sub);
        while (index >= 0) {

            Log.e("Name", sub.substring(0, index));
            coaches[i] = new Coach(sub.substring(0, index),dataSnap.child("ProfileCoach").child(sub.substring(0, index)).getValue().toString());
            coachesList.add(coaches[i]);
            i++;
            sub = sub.substring(index + 1);
            Log.e("sub", sub);
            index = sub.indexOf(",");
            Log.e("Index", index + "");



        }
        //coachesList.add(coach);


        CoachListAdapter adapter = new CoachListAdapter(getActivity(), R.layout.customlayoutcoachprofile, coachesList);

        listView.setAdapter(adapter);
    }
    public void CoachRefreshProgramlist(){
        Log.e("Program",dataSnap.child("CoachNames").child(username).getValue().toString());

        //Coach coach = new Coach("pp","{{StudyPlace}=באר שבע, {Professionalization}=,שריפת שומנים,אימוני כוח בחדר כושר,אימוני כוח בסטרייט,אימונים בבית,שיפור מרחק בריצות,שיפור מהירות בריצות,, {Age}=26, {CoachTime}=שנתיים, {Gender}=Male, {Description}=אין מה לתאר}");
        help =dataSnap.child("CoachNames").child(username).getValue().toString();
        sub = help.substring((help.indexOf(","+username+","))+2+username.length(),help.length());
        Log.e("ProgramFull",sub);


        int index = sub.indexOf(",");
        Log.e("Index",index+"");
        Log.e("sub", sub);
        while (index >= 0) {

            Log.e("Name", sub.substring(0, index));
            users[i] = new User(sub.substring(0, index),dataSnap.child("ProfileUser").child(sub.substring(0, index)).getValue().toString());
            Log.e("USER!","Name:"+sub.substring(0, index));
            Log.e("USER!","User:"+dataSnap.child("ProfileUser").child(sub.substring(0, index)).getValue().toString());
            Log.e("Name",users[i].getName());
            Log.e("Height",users[i].getHeight());
            Log.e("Gender",users[i].getGender());
            Log.e("Weight",users[i].getWeight());
            Log.e("Description",users[i].getDescription());
            Log.e("Time",users[i].getTime());
            Log.e("Item",users[i].getItem());
            Log.e("Age",users[i].getAge());
            Log.e("Goal",users[i].getGoal());
            Log.e("Details",users[i].getDetails());

            usersList.add(users[i]);
            i++;
            sub = sub.substring(index + 1);
            Log.e("sub", sub);
            index = sub.indexOf(",");
            Log.e("Index", index + "");



        }
        //coachesList.add(coach);


        UserListAdapter adapter = new UserListAdapter(getActivity(), R.layout.customlayoutuserprofile, usersList);

        listView.setAdapter(adapter);
    }
}