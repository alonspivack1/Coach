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
import android.widget.CheckBox;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;


public class FragmentSearch extends Fragment implements View.OnClickListener {
    DatabaseReference databaseReference;
    String CoachProfiles = "",HelpString = "";
    TextView fragtv;
    Boolean FirstOnClick = true,AcceptDialog=true;
    ListView listView;
    DataSnapshot dataSnap;
    Coach coachedittext;
    ArrayList<Coach> coachesList2;
    Coach[] coaches;
    int i = 0;
    int ii = 1;
    AlertDialog.Builder adb;
    int positionadb=0;
    String username,type,UserCoachCheack;
    DatabaseReference reference,reference2,reference3,reference4;
    Button SearchInListView,ResetSearchInListView;
    EditText etSearchInListView;
    String stringCoachSearch;
    boolean takecoach=false;
    CoachListAdapter adapter;
    ArrayList<Coach> coachesList;
    Coach coachhelp;
    CheckBox cbsearchburnfat,cbsearchdistance,cbsearchgym,cbsearchhome,cbsearchspeed,cbsearchstreet;
    boolean burnfat=false,distance=false,gym=false,home=false,speed=false,street=false;
    String Burnfat="שריפת שומנים",Distance="שיפור מרחק בריצות",Gym="אימוני כוח בחדר כושר",Home="אימונים בבית",Speed="שיפור מהירות בריצות",Street="אימוני כוח בסטרייט";

    boolean CBcoachcheack=true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("coachchild",dataSnapshot.child("CoachNames").getChildrenCount() + "");
                dataSnap = dataSnapshot;
                if (dataSnapshot.hasChild("CoachNames")) {
                    coaches=new Coach[Integer.parseInt(dataSnapshot.child("CoachNames").getChildrenCount()+"")];
                    CoachProfiles = dataSnapshot.child("CoachNames").getValue().toString();
                }

                //fragtv.setText(CoachProfiles+"");
                // i++;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        SearchInListView = v.findViewById(R.id.SearchInListView);
        ResetSearchInListView = v.findViewById(R.id.ResetSearchInListView);
        SearchInListView.setOnClickListener(this);
        ResetSearchInListView.setOnClickListener(this);
        etSearchInListView = v.findViewById(R.id.etSearchInListView);
        listView = (ListView) v.findViewById(R.id.listView);
        cbsearchburnfat = (CheckBox)v.findViewById(R.id.cbsearchburnfat);
        cbsearchdistance = (CheckBox)v.findViewById(R.id.cbsearchdistance);
        cbsearchgym = (CheckBox)v.findViewById(R.id.cbsearchgym);
        cbsearchhome = (CheckBox)v.findViewById(R.id.cbsearchhome);
        cbsearchspeed = (CheckBox)v.findViewById(R.id.cbsearchspeed);
        cbsearchstreet = (CheckBox)v.findViewById(R.id.cbsearchstreet);


        MainActivity activity = (MainActivity) getActivity();
        username = activity.getUsername();
        type = activity.getType();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                positionadb=position;
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
        Log.e("1","0");
        if (FirstOnClick){
            HelpString=CoachProfiles;
            Log.e("HelpString","HelpString="+HelpString);
            Log.e("CoachProfiles","CoachProfiles="+CoachProfiles);
            Log.e("CoachProfiles2","CoachProfiles2="+dataSnap.child("CoachNames").getValue().toString());
            Log.e("1","1");
            Log.e("1","2");
            UserCoachCheack = dataSnap.child("UserNames").child(username).getValue().toString();
            if (UserCoachCheack.indexOf((","+HelpString.substring(1,HelpString.indexOf("=")))+",")==-1) {
                FirstOnClick=false;
                //HelpString=CoachProfiles;
                Log.e("FULL",HelpString);
                coachhelp = new Coach(HelpString.substring(1,HelpString.indexOf("=")),dataSnap.child("ProfileCoach").child(HelpString.substring(1,HelpString.indexOf("="))));
                if (coachhelp.getName().toLowerCase().indexOf(stringCoachSearch)!=-1)
                {
                    if (coachhelp.getName().toLowerCase().equals(stringCoachSearch))
                    {
                        if (burnfat)
                        {
                            Log.e("CBCBCOACH","burnfat"+burnfat);

                            if (coachhelp.getProfessionalization().indexOf(Burnfat)==-1)
                            {
                                CBcoachcheack=false;
                            }
                        }
                        if (distance)
                        {
                            Log.e("CBCBCOACH","distance"+distance);
                            if (coachhelp.getProfessionalization().indexOf(Distance)==-1)
                            {
                                CBcoachcheack=false;
                            }
                        }
                        if (gym)
                        {
                            Log.e("CBCBCOACH","gym"+gym);
                            if (coachhelp.getProfessionalization().indexOf(Gym)==-1)
                            {
                                CBcoachcheack=false;
                            }
                        }
                        if (home)
                        {
                            Log.e("CBCBCOACH","home"+home);
                            if (coachhelp.getProfessionalization().indexOf(Home)==-1)
                            {
                                CBcoachcheack=false;
                            }
                        }
                        if (speed)
                        {
                            Log.e("CBCBCOACH","speed"+speed);
                            if (coachhelp.getProfessionalization().indexOf(Speed)==-1)
                            {
                                CBcoachcheack=false;
                            }
                        }
                        if (street)
                        {
                            Log.e("CBCBCOACH","street"+street);
                            if (coachhelp.getProfessionalization().indexOf(Street)==-1)
                            {
                                CBcoachcheack=false;
                            }
                        }
                        if (CBcoachcheack) {
                            takecoach = true;
                            coaches[0] = coachhelp;
                        }
                        else {
                            CBcoachcheack=true;
                        }
                    }
                    else {
                        if (burnfat)
                        {
                            if (coachhelp.getProfessionalization().indexOf(Burnfat)==-1)
                            {
                                CBcoachcheack=false;
                            }
                        }
                        if (distance)
                        {
                            if (coachhelp.getProfessionalization().indexOf(Distance)==-1)
                            {
                                CBcoachcheack=false;
                            }
                        }
                        if (gym)
                        {
                            if (coachhelp.getProfessionalization().indexOf(Gym)==-1)
                            {
                                CBcoachcheack=false;
                            }
                        }
                        if (home)
                        {
                            if (coachhelp.getProfessionalization().indexOf(Home)==-1)
                            {
                                CBcoachcheack=false;
                            }
                        }
                        if (speed)
                        {
                            if (coachhelp.getProfessionalization().indexOf(Speed)==-1)
                            {
                                CBcoachcheack=false;
                            }
                        }
                        if (street)
                        {
                            if (coachhelp.getProfessionalization().indexOf(Street)==-1)
                            {
                                CBcoachcheack=false;
                            }
                        }
                        if (CBcoachcheack) {
                            coaches[ii] = coachhelp;
                            Log.e("Answer", HelpString.substring(1, HelpString.indexOf("=")));
                            ii++;
                            Log.e("Dealits", dataSnap.child("ProfileCoach").child(HelpString.substring(1, HelpString.indexOf("="))).getValue().toString());
                        }
                        else {
                            CBcoachcheack=true;
                        }
                    }
                }

            }
                //  coaches[0] = new Coach("coach","1","1","1","1","1");


            else
            {
                FirstOnClick=false;
            }
        }
        while (HelpString.indexOf(", ")!=-1)
        {

            Log.e("1","3");
            if (((HelpString.indexOf(", ")!=-1)&&(ii<coaches.length)))//i<coaches.length&&
            {
                Log.e("1","4");
                HelpString = HelpString.substring(HelpString.indexOf(", ")+2);
                Log.e("FULL",HelpString);
                Log.e("1",UserCoachCheack);
                if (UserCoachCheack.indexOf((","+HelpString.substring(0,HelpString.indexOf("=")))+",")==-1)
                {
                    Log.e("1","5");
                    coachhelp = new Coach(HelpString.substring(0,HelpString.indexOf("=")),dataSnap.child("ProfileCoach").child(HelpString.substring(0,HelpString.indexOf("="))));
                    if (coachhelp.getName().toLowerCase().indexOf(stringCoachSearch)!=-1)
                    {
                        if (coachhelp.getName().toLowerCase().equals(stringCoachSearch))
                        {
                            if (burnfat)
                            {
                                if (coachhelp.getProfessionalization().indexOf(Burnfat)==-1)
                                {
                                    CBcoachcheack=false;
                                }
                            }
                            if (distance)
                            {
                                if (coachhelp.getProfessionalization().indexOf(Distance)==-1)
                                {
                                    CBcoachcheack=false;
                                }
                            }
                            if (gym)
                            {
                                if (coachhelp.getProfessionalization().indexOf(Gym)==-1)
                                {
                                    CBcoachcheack=false;
                                }
                            }
                            if (home)
                            {
                                if (coachhelp.getProfessionalization().indexOf(Home)==-1)
                                {
                                    CBcoachcheack=false;
                                }
                            }
                            if (speed)
                            {
                                if (coachhelp.getProfessionalization().indexOf(Speed)==-1)
                                {
                                    CBcoachcheack=false;
                                }
                            }
                            if (street)
                            {
                                if (coachhelp.getProfessionalization().indexOf(Street)==-1)
                                {
                                    CBcoachcheack=false;
                                }
                            }
                            if (CBcoachcheack) {
                                takecoach=true;
                                coaches[0]=coachhelp;
                            }
                            else {
                                CBcoachcheack=true;
                            }

                        }
                        else {
                            if (burnfat)
                            {
                                if (coachhelp.getProfessionalization().indexOf(Burnfat)==-1)
                                {
                                    CBcoachcheack=false;
                                }
                            }
                            if (distance)
                            {
                                if (coachhelp.getProfessionalization().indexOf(Distance)==-1)
                                {
                                    CBcoachcheack=false;
                                }
                            }
                            if (gym)
                            {
                                if (coachhelp.getProfessionalization().indexOf(Gym)==-1)
                                {
                                    CBcoachcheack=false;
                                }
                            }
                            if (home)
                            {
                                if (coachhelp.getProfessionalization().indexOf(Home)==-1)
                                {
                                    CBcoachcheack=false;
                                }
                            }
                            if (speed)
                            {
                                if (coachhelp.getProfessionalization().indexOf(Speed)==-1)
                                {
                                    CBcoachcheack=false;
                                }
                            }
                            if (street)
                            {
                                if (coachhelp.getProfessionalization().indexOf(Street)==-1)
                                {
                                    CBcoachcheack=false;
                                }
                            }
                            if (CBcoachcheack) {
                                coaches[ii] = new Coach(HelpString.substring(0,HelpString.indexOf("=")),dataSnap.child("ProfileCoach").child(HelpString.substring(0,HelpString.indexOf("="))));
                                Log.e("Answer",HelpString.substring(0,HelpString.indexOf("=")));
                                ii++;
                                Log.e("Dealits",dataSnap.child("ProfileCoach").child(HelpString.substring(0,HelpString.indexOf("="))).getValue().toString());
                            }
                            else {
                                CBcoachcheack=true;
                            }

                    }}

                }



            }
        }
        if (HelpString.indexOf(", ")==-1){
            Log.e("Array","Start");
            coachesList2 = new ArrayList<>();
            if (takecoach)
            {
                coachesList2.add(coaches[0]);
                for (int j=1; j<ii; j++)
                {
                    coachesList2.add(coaches[j]);
                    Log.e("NAME",coaches[j].getName());
                    Log.e("NUMBER",String.valueOf(j));

                }
            }
            else
            {
                for (int j=1; j<ii; j++)
                {
                    Log.e("ABC!J",j+"");
                    Log.e("ABC!ii",ii+"");

                    coachesList2.add(coaches[j]);
                    Log.e("NAME",coaches[j].getName());

                }
            }


            adapter = new CoachListAdapter(getActivity(), R.layout.customlayoutcoachprofile, coachesList2);
            listView.setAdapter(adapter);
            Log.e("NUMBERS",""+ii);
            if (coaches[0]==null)
            {
                Log.e("NUMBERS","NULL!");
            }
            else
            {
                Log.e("NUMBERS",coaches[0].getName());

            }


            return;
        }
    }

    private void RefreshCoaches() {

        Log.e("1","0");
        if (FirstOnClick){
            HelpString=CoachProfiles;
            Log.e("HelpString","HelpString="+HelpString);
            Log.e("CoachProfiles","CoachProfiles="+CoachProfiles);
            Log.e("CoachProfiles2","CoachProfiles2="+dataSnap.child("CoachNames").getValue().toString());
            Log.e("1","1");
            Log.e("1","2");
            UserCoachCheack = dataSnap.child("UserNames").child(username).getValue().toString();
            if (UserCoachCheack.indexOf((","+HelpString.substring(1,HelpString.indexOf("=")))+",")==-1) {
                FirstOnClick=false;
                //HelpString=CoachProfiles;
                Log.e("FULL",HelpString);
                coaches[i] = new Coach(HelpString.substring(1,HelpString.indexOf("=")),dataSnap.child("ProfileCoach").child(HelpString.substring(1,HelpString.indexOf("="))));
                //  coaches[0] = new Coach("coach","1","1","1","1","1");
                Log.e("Answer",HelpString.substring(1,HelpString.indexOf("=")));
                i++;

                Log.e("Dealits",dataSnap.child("ProfileCoach").child(HelpString.substring(1,HelpString.indexOf("="))).getValue().toString());}
            else
            {
                FirstOnClick=false;
            }
        }
        while (HelpString.indexOf(", ")!=-1)
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
                        coaches[i] = new Coach(HelpString.substring(0,HelpString.indexOf("=")),dataSnap.child("ProfileCoach").child(HelpString.substring(0,HelpString.indexOf("="))));
                        // coaches[1] = new Coach("pp","1","1","1","1","1");
                        Log.e("Answer",HelpString.substring(0,HelpString.indexOf("=")));

                        i++;


                        Log.e("Dealits",dataSnap.child("ProfileCoach").child(HelpString.substring(0,HelpString.indexOf("="))).getValue().toString());
                    }



                }
            }
        if (HelpString.indexOf(", ")==-1){
            Log.e("Array","Start");
            coachesList = new ArrayList<>();
            for (int j=0; j<i; j++)
            {
                coachesList.add(coaches[j]);
            }

            adapter = new CoachListAdapter(getActivity(), R.layout.customlayoutcoachprofile, coachesList);
            listView.setAdapter(adapter);
            return;
        }
    }


    public void Dialog()
    {if (AcceptDialog){
        adb = new  AlertDialog.Builder(getActivity());
        adb.setTitle("בקשת קשר");
        adb.setMessage("האם אתה רוצה לשלוח בקשת קשר למאמן: "+coaches[positionadb].getName());
        adb.setCancelable(false);
        adb.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                AcceptDialog=false;
                String userdata = dataSnap.child("UserNames").child(username).getValue().toString();
                reference = FirebaseDatabase.getInstance().getReference("UserNames");
                reference.child(username).setValue(userdata+coaches[positionadb].getName()+",");

                String coachdata = dataSnap.child("CoachNames").child(coaches[positionadb].getName()).getValue().toString();
                reference2 = FirebaseDatabase.getInstance().getReference("CoachNames");
                reference2.child(coaches[positionadb].getName()).setValue(coachdata+username+",");

                reference3 = FirebaseDatabase.getInstance().getReference("ChatRoom");


                Calendar cal = Calendar.getInstance();
                int minute = cal.get(Calendar.MINUTE);
                int hourofday = cal.get(Calendar.HOUR_OF_DAY);
                String time = String.valueOf(hourofday)+":"+String.valueOf(minute);
                Log.e("time",time);


                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("sender",username);
                hashMap.put("receiver",coaches[positionadb].getName());
                hashMap.put("message","נשלחה בקשת קשר");
                hashMap.put("time",time);

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
    }}
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.SearchInListView:
                    burnfat=false;
                    distance=false;
                    gym=false;
                    home=false;
                    speed=false;
                    street=false;

                    CBcoachcheack=true;
                    if (cbsearchburnfat.isChecked())
                    {
                        burnfat=true;
                    }
                    if (cbsearchdistance.isChecked())
                    {
                        distance=true;
                    }
                    if (cbsearchgym.isChecked())
                    {
                        gym=true;
                    }
                    if (cbsearchhome.isChecked())
                    {
                        home=true;
                    }
                    if (cbsearchspeed.isChecked())
                    {
                        speed=true;
                    }
                    if (cbsearchstreet.isChecked())
                    {
                        street=true;
                    }
                    FirstOnClick=true;
                    stringCoachSearch = etSearchInListView.getText().toString().toLowerCase();
                    listView.setAdapter(null);
                    RefreshCoach();
                    ii=1;
                    takecoach=false;
                    break;
            case R.id.ResetSearchInListView:
                etSearchInListView.setText("");
                cbsearchstreet.setChecked(false);
                cbsearchspeed.setChecked(false);
                cbsearchhome.setChecked(false);
                cbsearchgym.setChecked(false);
                cbsearchdistance.setChecked(false);
                cbsearchburnfat.setChecked(false);

                adapter = new CoachListAdapter(getActivity(), R.layout.customlayoutcoachprofile, coachesList);
                listView.setAdapter(adapter);
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
