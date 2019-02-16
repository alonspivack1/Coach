package coach.coach;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.os.Bundle;

import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;


public class FragmentSearch extends Fragment implements View.OnClickListener {
    DatabaseReference databaseReference;
    String CoachProfiles = "", HelpString = "";
    TextView fragtv;
    Boolean FirstOnClick = true;
    ListView listView;
    DataSnapshot dataSnap;
    Coach[] coaches = new Coach[8];
    int i = 0;
    AlertDialog.Builder adb;
    int positionadb=0;
    String username,type,UserCoachCheack;
    DatabaseReference reference,reference2,reference3,reference4;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> objectMap = (HashMap<String, Object>) dataSnapshot.child("CoachNames").getValue();
                dataSnap = dataSnapshot;
                CoachProfiles = objectMap.toString();
                //fragtv.setText(CoachProfiles+"");
                // i++;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        fragtv = (TextView) v.findViewById(R.id.fragtv);
        Button b = (Button) v.findViewById(R.id.button);
        b.setOnClickListener(this);
        listView = (ListView) v.findViewById(R.id.listView);

        MainActivity activity = (MainActivity) getActivity();
        username = activity.getUsername();
        type = activity.getType();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                positionadb=position;
                Toast.makeText(getActivity(),
                        "Click ListItem Number " + position, Toast.LENGTH_LONG)
                        .show();

                Log.e("age: ", coaches[position].getAge()+"         "+dataSnap.child("CoachNames").child(coaches[position].getName()).getValue().toString());
                Dialog();
                Log.e("username",username);
            }
        });



        try {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    RefreshCoaches();
                }
            }, 250);
        } catch (Exception e) {
            System.out.println("An exception!");
        }


        return v;
    }

    private void RefreshCoaches() {

        Log.e("1","0");
        if (FirstOnClick){
            HelpString=CoachProfiles;
            Log.e("1","1");}
        while (HelpString.indexOf(", ")!=-1)
        {
            if (FirstOnClick)
            {
                Log.e("1","2");
                UserCoachCheack = dataSnap.child("UserNames").child(username).getValue().toString();
                if (UserCoachCheack.indexOf((","+HelpString.substring(1,HelpString.indexOf("=")))+",")==-1) {
                    FirstOnClick=false;
                    //HelpString=CoachProfiles;
                    Log.e("FULL",HelpString);
                    coaches[i] = new Coach(HelpString.substring(1,HelpString.indexOf("=")),dataSnap.child("ProfileCoach").child(HelpString.substring(1,HelpString.indexOf("="))).getValue().toString());
                    //  coaches[0] = new Coach("coach","1","1","1","1","1");
                    Log.e("Answer",HelpString.substring(1,HelpString.indexOf("=")));
                    i++;

                    Log.e("Dealits",dataSnap.child("ProfileCoach").child(HelpString.substring(1,HelpString.indexOf("="))).getValue().toString());}
                else
                {
                    FirstOnClick=false;
                }
            }
            else
            {
                Log.e("1","3");
                if (((HelpString.indexOf(", ")!=-1)))//i<coaches.length&&
                {
                    Log.e("1","4");
                    HelpString = HelpString.substring(HelpString.indexOf(", ")+2);
                    Log.e("FULL",HelpString);
                    Log.e("1",UserCoachCheack);
                    if (UserCoachCheack.indexOf((","+HelpString.substring(0,HelpString.indexOf("=")))+",")==-1)
                    {
                        Log.e("1","5");
                        coaches[i] = new Coach(HelpString.substring(0,HelpString.indexOf("=")),dataSnap.child("ProfileCoach").child(HelpString.substring(0,HelpString.indexOf("="))).getValue().toString());
                        // coaches[1] = new Coach("pp","1","1","1","1","1");
                        Log.e("Answer",HelpString.substring(0,HelpString.indexOf("=")));

                        i++;


                        Log.e("Dealits",dataSnap.child("ProfileCoach").child(HelpString.substring(0,HelpString.indexOf("="))).getValue().toString());
                    }



                }
            }}
        if (HelpString.indexOf(", ")==-1){
            Log.e("Array","Start");
            ArrayList<Coach> coachesList = new ArrayList<>();
            for (int j=0; j<i; j++)
            {
                coachesList.add(coaches[j]);
            }

            CoachListAdapter adapter = new CoachListAdapter(getActivity(), R.layout.customlayoutcoachprofile, coachesList);
            listView.setAdapter(adapter);
            return;
        }
    }


    public void Dialog()
    {
        adb = new  AlertDialog.Builder(getActivity());
        adb.setTitle("בקשת קשר");
        adb.setMessage("האם אתה רוצה לשלוח בקשת קשר למאמן: "+coaches[positionadb].getName());
        adb.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {


                String userdata = dataSnap.child("UserNames").child(username).getValue().toString();
                reference = FirebaseDatabase.getInstance().getReference("UserNames");
                reference.child(username).setValue(userdata+coaches[positionadb].getName()+",");

                String coachdata = dataSnap.child("CoachNames").child(coaches[positionadb].getName()).getValue().toString();
                reference2 = FirebaseDatabase.getInstance().getReference("CoachNames");
                reference2.child(coaches[positionadb].getName()).setValue(coachdata+username+",");

                reference3 = FirebaseDatabase.getInstance().getReference("ChatRoom");
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("sender",username);
                hashMap.put("receiver",coaches[positionadb].getName());
                hashMap.put("message","startchat");
                //.child(username+"&"+(coaches[positionadb].getName())
                reference3.child(username+"&"+(coaches[positionadb].getName())).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            reference4 = FirebaseDatabase.getInstance().getReference("ProgramRoom");
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("message","Empty program");
                            //.child(username+"&"+(coaches[positionadb].getName())
                            reference4.child(username+"&"+(coaches[positionadb].getName())).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isComplete()) {

                                        getActivity().finish();
                                        getActivity().overridePendingTransition(0, 0);
                                        startActivity(getActivity().getIntent());
                                        getActivity().overridePendingTransition(0, 0);

                                        Log.e("SendFriend","SendFriend");
                                    }
                                }
                            });
                        }
                    }
                });

                   /* getActivity().finish();
                    getActivity().overridePendingTransition(0, 0);
                    startActivity(getActivity().getIntent());
                    getActivity().overridePendingTransition(0, 0);

                    Log.e("SendFriend","SendFriend");*/

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
    @Override
    public void onClick(View v) {
        /*switch (v.getId()) {
            case R.id.button:
                Log.e("1","0");
                if (FirstOnClick){
                HelpString=CoachProfiles;
                    Log.e("1","1");}
                while (HelpString.indexOf(", ")!=-1)
                {
                if (FirstOnClick)
                {
                    Log.e("1","2");
                    UserCoachCheack = dataSnap.child("UserNames").child(username).getValue().toString();
                    if (UserCoachCheack.indexOf((","+HelpString.substring(1,HelpString.indexOf("=")))+",")==-1) {
                    FirstOnClick=false;
                    //HelpString=CoachProfiles;
                    Log.e("FULL",HelpString);
                    coaches[i] = new Coach(HelpString.substring(1,HelpString.indexOf("=")),dataSnap.child("ProfileCoach").child(HelpString.substring(1,HelpString.indexOf("="))).getValue().toString());
                  //  coaches[0] = new Coach("coach","1","1","1","1","1");
                    Log.e("Answer",HelpString.substring(1,HelpString.indexOf("=")));
                    i++;

                    Log.e("Dealits",dataSnap.child("ProfileCoach").child(HelpString.substring(1,HelpString.indexOf("="))).getValue().toString());}
                    else
                        {
                            FirstOnClick=false;
                        }
                }
                else
                {
                    Log.e("1","3");
                if (((HelpString.indexOf(", ")!=-1)))//i<coaches.length&&
                {
                    Log.e("1","4");
                    HelpString = HelpString.substring(HelpString.indexOf(", ")+2);
                    Log.e("FULL",HelpString);
                    Log.e("1",UserCoachCheack);
                    if (UserCoachCheack.indexOf((","+HelpString.substring(0,HelpString.indexOf("=")))+",")==-1)
                    {
                        Log.e("1","5");
                        coaches[i] = new Coach(HelpString.substring(0,HelpString.indexOf("=")),dataSnap.child("ProfileCoach").child(HelpString.substring(0,HelpString.indexOf("="))).getValue().toString());
                        // coaches[1] = new Coach("pp","1","1","1","1","1");
                        Log.e("Answer",HelpString.substring(0,HelpString.indexOf("=")));

                        i++;


                        Log.e("Dealits",dataSnap.child("ProfileCoach").child(HelpString.substring(0,HelpString.indexOf("="))).getValue().toString());
                    }



                }
                }}
                if (HelpString.indexOf(", ")==-1){
                Log.e("Array","Start");
                ArrayList<Coach> coachesList = new ArrayList<>();
                for (int j=0; j<i; j++)
                {
                    coachesList.add(coaches[j]);
                }

                CoachListAdapter adapter = new CoachListAdapter(getActivity(), R.layout.customlayoutcoachprofile, coachesList);
                listView.setAdapter(adapter);
                return;
            }
                break;

        }*/
    }



}
