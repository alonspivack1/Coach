package coach.coach;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Fragment of all Coaches/Users you can chat with.
 */
public class FragmentChat extends Fragment{


    /**
     * The Database reference.
     */
    DatabaseReference databaseReference;
    /**
     * The Data snap.
     */
    DataSnapshot dataSnap;
    /**
     * The Coaches.
     */
    Coach[] coaches;
    /**
     * The Users.
     */
    User[] users;
    /**
     * The .
     */
    int i =0;
    /**
     * The Username.
     */
    String username, /**
     * The Type.
     */
    type, /**
     * The Sub.
     */
    sub, /**
     * The Help.
     */
    help;
    /**
     * The Coaches list.
     */
    ArrayList<Coach> coachesList;
    /**
     * The Users list.
     */
    ArrayList<User> usersList;
    /**
     * The List view.
     */
    ListView listView;
    /**
     * The Receiver.
     */
    String receiver, /**
     * The Room.
     */
    room;
    /**
     * The Adb.
     */
    AlertDialog.Builder adb;
    /**
     * The Positionadb.
     */
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
                try {
                    Log.e("coachchild",coaches.length+"");

                }
                catch (Exception e)
                {
                    coaches = new Coach[Integer.parseInt(dataSnapshot.child("CoachNames").getChildrenCount()+"")];

                }
                try {
                    Log.e("userchild",users.length+"");

                }
                catch (Exception e)
                {
                    users = new User[Integer.parseInt(dataSnapshot.child("UserNames").getChildrenCount()+"")];

                }
                dataSnap = dataSnapshot;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ConnectivityManager connectivityManager;
                connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    positionadb=position;
                    Dialog();
                }
                else {
                    Toast.makeText(getActivity(),"אין חיבור לאינטרנט",Toast.LENGTH_LONG).show();
                }
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

    /**
     * Dialog Check if the user is sure he wants to delete the contact with the coach.
     */
    public void Dialog(){
        adb = new  AlertDialog.Builder(getActivity());
        adb.setTitle("ביטול קשר");
        if (type.equals("User")){
            adb.setMessage("האם אתה רוצה לבטל קשר עם המאמן: "+coaches[positionadb].getName());}
            else
        {adb.setMessage("האם אתה רוצה לבטל קשר עם המתאמן: "+users[positionadb].getName());}

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
                    UpdateUserNameChange = UpdateUserNameChange.replace("," + coaches[positionadb].getName() + ",", ",");
                    FirebaseDatabase.getInstance().getReference().child("UserNames").child(username).setValue(UpdateUserNameChange);

                    String CoachNameChange = dataSnap.child("CoachNames").child(coaches[positionadb].getName()).getValue().toString();
                    String UpdateCoachNameChange = CoachNameChange;
                    UpdateCoachNameChange = UpdateCoachNameChange.replace("," + username + ",", ",");
                    FirebaseDatabase.getInstance().getReference().child("CoachNames").child(coaches[positionadb].getName()).setValue(UpdateCoachNameChange);




                    try {
                        SharedPreferences Coaches = getActivity().getSharedPreferences("Coaches", MODE_PRIVATE);
                        String updatecoachesnames =Coaches.getString("coachesnames","");

                        updatecoachesnames=","+updatecoachesnames;
                        String coachdelete=coaches[positionadb].getName();
                        int help = updatecoachesnames.indexOf(","+coachdelete+",");
                        updatecoachesnames=updatecoachesnames.substring(0,help)+updatecoachesnames.substring(help+1+coachdelete.length());
                        updatecoachesnames=updatecoachesnames.substring(1);
                        SharedPreferences.Editor editorcoaches =  getActivity().getSharedPreferences("Coaches", MODE_PRIVATE).edit();
                        editorcoaches.putString("coachesnames",updatecoachesnames);
                        editorcoaches.apply();
                    }
                    catch (Exception e)
                    {

                    }


                    try {
                        String room = username+"&"+coaches[positionadb].getName();
                        SharedPreferences.Editor editorprograms =  getActivity().getSharedPreferences("Programs", MODE_PRIVATE).edit();
                        editorprograms.remove(room);
                        editorprograms.apply();
                    }
                    catch (Exception e)
                    {

                    }

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
                    UpdateCoachNameChange = UpdateCoachNameChange.replace("," + users[positionadb].getName() + ",", ",");
                    FirebaseDatabase.getInstance().getReference().child("CoachNames").child(username).setValue(UpdateCoachNameChange);

                    String UserNameChange = dataSnap.child("UserNames").child(users[positionadb].getName()).getValue().toString();
                    String UpdateUserNameChange = UserNameChange;
                    UpdateUserNameChange = UpdateUserNameChange.replace("," + username + ",", ",");
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

    /**
     * Refreshes all coaches that have contact with them.
     */
    public void UserRefreshProgramlist(){
        help =dataSnap.child("UserNames").child(username).getValue().toString();
        sub = help.substring((help.indexOf(","+username+","))+2+username.length());
        int index = sub.indexOf(",");

        while (index >= 0) {

            coaches[i] = new Coach(sub.substring(0, index),dataSnap.child("ProfileCoach").child(sub.substring(0, index)),dataSnap.child("Rating").child(sub.substring(0, index)));
            coachesList.add(coaches[i]);
            i++;
            sub = sub.substring(index + 1);
            index = sub.indexOf(",");

        }

        CoachListAdapter adapter = new CoachListAdapter(getActivity(), R.layout.customlayoutcoachprofile, coachesList);

        listView.setAdapter(adapter);
    }

    /**
     * Refreshes all users that have contact with them.
     */
    public void CoachRefreshProgramlist(){


        help =dataSnap.child("CoachNames").child(username).getValue().toString();
        sub = help.substring((help.indexOf(","+username+","))+2+username.length(),help.length());


        int index = sub.indexOf(",");
        while (index >= 0) {
            users[i] = new User(sub.substring(0, index),dataSnap.child("ProfileUser").child(sub.substring(0, index)));
            usersList.add(users[i]);
            i++;
            sub = sub.substring(index + 1);
            index = sub.indexOf(",");
        }
        UserListAdapter adapter = new UserListAdapter(getActivity(), R.layout.customlayoutuserprofile, usersList);
        listView.setAdapter(adapter);
    }
}