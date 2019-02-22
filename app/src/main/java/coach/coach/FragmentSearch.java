package coach.coach;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.EditText;
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
    String CoachProfiles = "",UserProfiles ="", HelpString = "";
    TextView fragtv;
    Boolean FirstOnClick = true;
    ListView listView;
    DataSnapshot dataSnap;
    Coach coachedittext;
    Coach[] coaches = new Coach[8];
    User[] users = new User[8];
    int i = 0;
    AlertDialog.Builder adb;
    int positionadb=0;
    String username,type,UserCoachCheack;
    DatabaseReference reference,reference2,reference3,reference4;
    Button SearchInListView;
    EditText etSearchInListView;
    String stringCoachSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnap = dataSnapshot;
                CoachProfiles = dataSnapshot.child("CoachNames").getValue().toString();
                UserProfiles = dataSnapshot.child("UserNames").getValue().toString();

                //fragtv.setText(CoachProfiles+"");
                // i++;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        fragtv = (TextView) v.findViewById(R.id.fragtv);
        SearchInListView = v.findViewById(R.id.SearchInListView);
        SearchInListView.setOnClickListener(this);
        Button b = (Button) v.findViewById(R.id.button);
        etSearchInListView = v.findViewById(R.id.etSearchInListView);
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
                    if (type.equals("User")) {
                        RefreshCoaches();
                    }
                }
            }, 100);
        } catch (Exception e) {
            System.out.println("An exception!");
        }


        return v;
    }


    private void RefreshCoach() {
        FirstOnClick=true;
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
                    FirstOnClick = false;
                    //HelpString=CoachProfiles;
                    coaches[i] = new Coach(HelpString.substring(1, HelpString.indexOf("=")), dataSnap.child("ProfileCoach").child(HelpString.substring(1, HelpString.indexOf("="))).getValue().toString());
                    if (coaches[i].getName().equals(stringCoachSearch))
                    {
                        coachedittext=new Coach(HelpString.substring(1, HelpString.indexOf("=")), dataSnap.child("ProfileCoach").child(HelpString.substring(1, HelpString.indexOf("="))).getValue().toString());
                        Log.e("FINDD",coachedittext.getName());
                            /*ArrayList<Coach> coachesList2 = new ArrayList<>();
                            CoachListAdapter adapter2 = new CoachListAdapter(getActivity(), R.layout.customlayoutcoachprofile, coachesList2);
                            adapter2.add(coachedittext);
                            listView.setAdapter(adapter2);*/
                    }

                }
                else
                {
                    FirstOnClick=false;
                }
            }
            else
            {
                Log.e("1","3");
                if (((HelpString.indexOf(", ")!=-1)&&(i<coaches.length)))//i<coaches.length&&
                {
                    Log.e("1","4");
                    HelpString = HelpString.substring(HelpString.indexOf(", ")+2);
                    Log.e("1",UserCoachCheack);
                    if (UserCoachCheack.indexOf((","+HelpString.substring(0,HelpString.indexOf("=")))+",")==-1)
                    {
                        Log.e("1","5");
                        coaches[i] = new Coach(HelpString.substring(0,HelpString.indexOf("=")),dataSnap.child("ProfileCoach").child(HelpString.substring(0,HelpString.indexOf("="))).getValue().toString());
                        // coaches[1] = new Coach("pp","1","1","1","1","1");
                        if (coaches[i].getName().equals(stringCoachSearch))
                        {
                            coachedittext=new Coach(HelpString.substring(0,HelpString.indexOf("=")),dataSnap.child("ProfileCoach").child(HelpString.substring(0,HelpString.indexOf("="))).getValue().toString());
                            Log.e("FINDD",coachedittext.getName());
                            /*ArrayList<Coach> coachesList2 = new ArrayList<>();
                            CoachListAdapter adapter2 = new CoachListAdapter(getActivity(), R.layout.customlayoutcoachprofile, coachesList2);
                            adapter2.add(coachedittext);
                            listView.setAdapter(adapter2);*/
                        }



                        Log.e("Dealits",dataSnap.child("ProfileCoach").child(HelpString.substring(0,HelpString.indexOf("="))).getValue().toString());
                    }



                }
            }}
        if (HelpString.indexOf(", ")==-1){
            ArrayList<Coach> coachesList2 = new ArrayList<>();
            if (coachedittext!=null){
                coachesList2.add(coachedittext);}
                coaches[0]=coachedittext;
            CoachListAdapter adapter = new CoachListAdapter(getActivity(), R.layout.customlayoutcoachprofile, coachesList2);
            listView.setAdapter(adapter);
            return;
        }
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
                if (((HelpString.indexOf(", ")!=-1)&&(i<coaches.length)))//i<coaches.length&&
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
                hashMap.put("message","נשלחה בקשת קשר");
                //.child(username+"&"+(coaches[positionadb].getName())
                reference3.child(username+"&"+(coaches[positionadb].getName())).child("1").setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            reference3.child(username+"&"+(coaches[positionadb].getName())).child("num").child("num").setValue(2);
                            reference4 = FirebaseDatabase.getInstance().getReference("ProgramRoom");
                            HashMap<String, String> hashMap = new HashMap<>();
                            String htmlnewprogram="<ul><li><blockquote><b><i><u><del>&#1492;&#1502;&#1488;&#1502;&#1503; &#1506;&#1491;&#1497;&#1497;&#1503; &#1500;&#1488; &#1513;&#1500;&#1495; </del></u></i></b><b><i><u><del><u>&#1514;&#1493;&#1499;&#1504;&#1497;&#1514;</u></del></u></i></b></blockquote></li></ul>";
                            hashMap.put("Data",htmlnewprogram);
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
        switch (v.getId())
        {
            case R.id.SearchInListView:
                stringCoachSearch=etSearchInListView.getText().toString();
                listView.setAdapter(null);
                RefreshCoach();
                break;
        }
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
