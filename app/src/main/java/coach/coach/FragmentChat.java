package coach.coach;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
    Coach[] coaches;
    User[] users;
    int i =0;
    String username,type,sub,help;
    ArrayList<Coach> coachesList;
    ArrayList<User> usersList;
    ListView listView;
    String receiver,room;
    AlertDialog.Builder adb;
    int positionadb;
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
                Log.e("coachchild",dataSnapshot.child("CoachNames").getChildrenCount() + "");
                Log.e("userchild",dataSnapshot.child("UserNames").getChildrenCount() + "");
                try {
                    Log.e("coachchild??",coaches.length+"");

                }
                catch (Exception e)
                {
                    coaches = new Coach[Integer.parseInt(dataSnapshot.child("CoachNames").getChildrenCount()+"")];

                }
                try {
                    Log.e("userchild??",users.length+"");

                }
                catch (Exception e)
                {
                    users = new User[Integer.parseInt(dataSnapshot.child("UserNames").getChildrenCount()+"")];

                }
                dataSnap = dataSnapshot;
                //fragtv.setText(CoachProfiles+"");
                // i++;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                positionadb=position;
                Dialog();
                return true;
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
                    ChatIntent.putExtra("type",type);
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
                    ChatIntent.putExtra("type",type);
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
    public void Dialog(){
        adb = new  AlertDialog.Builder(getActivity());
        adb.setTitle("ביטול קשר");
        if (type.equals("User")){
            adb.setMessage("האם אתה לבטל קשר עם המאמן: "+coaches[positionadb].getName());}
            else
        {adb.setMessage("האם אתה לבטל קשר עם המתאמן: "+users[positionadb].getName());}

        adb.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                if (type.equals("User")) {
                    DatabaseReference ChatRoomDelete = FirebaseDatabase.getInstance().getReference().child("ChatRoom").child(username + "&" + coaches[positionadb].getName());
                    ChatRoomDelete.removeValue();
                    DatabaseReference ProgramRoomDelete = FirebaseDatabase.getInstance().getReference().child("ProgramRoom").child(username + "&" + coaches[positionadb].getName());
                    ProgramRoomDelete.removeValue();

                    String UserNameChange = dataSnap.child("UserNames").child(username).getValue().toString();
                    String UpdateUserNameChange = UserNameChange;
                    Log.e("UpdateUserNameChange", UpdateUserNameChange);
                    UpdateUserNameChange = UpdateUserNameChange.replace("," + coaches[positionadb].getName() + ",", ",");
                    Log.e("UpdateUserNameChange", UpdateUserNameChange);
                    FirebaseDatabase.getInstance().getReference().child("UserNames").child(username).setValue(UpdateUserNameChange);

                    String CoachNameChange = dataSnap.child("CoachNames").child(coaches[positionadb].getName()).getValue().toString();
                    String UpdateCoachNameChange = CoachNameChange;
                    Log.e("UpdateCoachNameChange", UpdateCoachNameChange);
                    UpdateCoachNameChange = UpdateCoachNameChange.replace("," + username + ",", ",");
                    Log.e("UpdateCoachNameChange", UpdateCoachNameChange);
                    FirebaseDatabase.getInstance().getReference().child("CoachNames").child(coaches[positionadb].getName()).setValue(UpdateCoachNameChange);

                    getActivity().finish();
                    getActivity().overridePendingTransition(0, 0);
                    startActivity(getActivity().getIntent());
                    getActivity().overridePendingTransition(0, 0);
                }
                else {
                    DatabaseReference ChatRoomDelete = FirebaseDatabase.getInstance().getReference().child("ChatRoom").child(users[positionadb].getName() + "&" + username);
                    ChatRoomDelete.removeValue();
                    DatabaseReference ProgramRoomDelete = FirebaseDatabase.getInstance().getReference().child("ProgramRoom").child(users[positionadb].getName() + "&" + username);
                    ProgramRoomDelete.removeValue();

                    String CoachNameChange = dataSnap.child("CoachNames").child(username).getValue().toString();
                    String UpdateCoachNameChange = CoachNameChange;
                    Log.e("UpdateCoachNameChange", UpdateCoachNameChange);
                    UpdateCoachNameChange = UpdateCoachNameChange.replace("," + users[positionadb].getName() + ",", ",");
                    Log.e("UpdateCoachNameChange", UpdateCoachNameChange);
                    FirebaseDatabase.getInstance().getReference().child("CoachNames").child(username).setValue(UpdateCoachNameChange);

                    String UserNameChange = dataSnap.child("UserNames").child(users[positionadb].getName()).getValue().toString();
                    String UpdateUserNameChange = UserNameChange;
                    Log.e("UpdateUserNameChange", UpdateUserNameChange);
                    UpdateUserNameChange = UpdateUserNameChange.replace("," + username + ",", ",");
                    Log.e("UpdateUserNameChange", UpdateUserNameChange);
                    FirebaseDatabase.getInstance().getReference().child("UserNames").child(users[positionadb].getName()).setValue(UpdateUserNameChange);

                    getActivity().finish();
                    getActivity().overridePendingTransition(0, 0);
                    startActivity(getActivity().getIntent());
                    getActivity().overridePendingTransition(0, 0);
                }



            }
        });
        adb.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.create();
        adb.show();
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
            coaches[i] = new Coach(sub.substring(0, index),dataSnap.child("ProfileCoach").child(sub.substring(0, index)));
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
            users[i] = new User(sub.substring(0, index),dataSnap.child("ProfileUser").child(sub.substring(0, index)));
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
            Log.e("Details",users[i].getDetails().toString());

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