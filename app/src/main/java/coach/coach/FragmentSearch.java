package coach.coach;

import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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


/**
 * Fragment just for Users - Fragment to search a Coach.
 */
public class FragmentSearch extends Fragment implements View.OnClickListener {
    /**
     * The Database reference.
     */
    DatabaseReference databaseReference;
    /**
     * The Coach profiles.
     */
    String CoachProfiles = "", /**
     * The Help string.
     */
    HelpString = "";
    /**
     * The First on click.
     */
    Boolean FirstOnClick = true, /**
     * The Accept dialog.
     */
    AcceptDialog=true;
    /**
     * The List view.
     */
    ListView listView;
    /**
     * The Data snap.
     */
    DataSnapshot dataSnap;
    /**
     * The Coaches list 2.
     */
    ArrayList<Coach> coachesList2;
    /**
     * The Coaches.
     */
    Coach[] coaches;
    /**
     * The .
     */
    int i = 0;
    /**
     * The Ii.
     */
    int ii = 1;
    /**
     * The Time.
     */
    String time="";
    /**
     * The Adb.
     */
    AlertDialog.Builder adb;
    /**
     * The Positionadb.
     */
    int positionadb=0;
    /**
     * The Username.
     */
    String username, /**
     * The Type.
     */
    type, /**
     * The User coach cheack.
     */
    UserCoachCheack;
    /**
     * The Reference.
     */
    DatabaseReference reference, /**
     * The Reference 2.
     */
    reference2, /**
     * The Reference 3.
     */
    reference3, /**
     * The Reference 4.
     */
    reference4, /**
     * The Reference notification.
     */
    referenceNotification;
    /**
     * The Search in list view.
     */
    Button SearchInListView, /**
     * The Reset search in list view.
     */
    ResetSearchInListView;
    /**
     * The Et search in list view.
     */
    EditText etSearchInListView;
    /**
     * The String coach search.
     */
    String stringCoachSearch;
    /**
     * The Takecoach.
     */
    boolean takecoach=false;
    /**
     * The Adapter.
     */
    CoachListAdapter adapter;
    /**
     * The Coaches list.
     */
    ArrayList<Coach> coachesList;
    /**
     * The Coachhelp.
     */
    Coach coachhelp;
    /**
     * The Cbsearchburnfat.
     */
    CheckBox cbsearchburnfat, /**
     * The Cbsearchdistance.
     */
    cbsearchdistance, /**
     * The Cbsearchgym.
     */
    cbsearchgym, /**
     * The Cbsearchhome.
     */
    cbsearchhome, /**
     * The Cbsearchspeed.
     */
    cbsearchspeed, /**
     * The Cbsearchstreet.
     */
    cbsearchstreet;
    /**
     * The Burnfat.
     */
    boolean burnfat=false, /**
     * The Distance.
     */
    distance=false, /**
     * The Gym.
     */
    gym=false, /**
     * The Home.
     */
    home=false, /**
     * The Speed.
     */
    speed=false, /**
     * The Street.
     */
    street=false;
    /**
     * The Burnfat.
     */
    String Burnfat="שריפת שומנים", /**
     * The Distance.
     */
    Distance="שיפור מרחק בריצות", /**
     * The Gym.
     */
    Gym="אימוני כוח בחדר כושר", /**
     * The Home.
     */
    Home="אימונים בבית", /**
     * The Speed.
     */
    Speed="שיפור מהירות בריצות", /**
     * The Street.
     */
    Street="אימוני כוח בסטרייט";

    /**
     * The C bcoachcheack.
     */
    boolean CBcoachcheack=true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        referenceNotification = FirebaseDatabase.getInstance().getReference().child("Notification");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnap = dataSnapshot;
                if (dataSnapshot.hasChild("CoachNames")) {
                    try {
                        Log.e("coachchild",coaches.length+"");

                    }
                    catch (Exception e)
                    {
                        coaches = new Coach[Integer.parseInt(dataSnapshot.child("CoachNames").getChildrenCount()+"")];

                    }
                    CoachProfiles = dataSnapshot.child("CoachNames").getValue().toString();
                }




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
                Dialog();
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
        if (FirstOnClick){
            HelpString=CoachProfiles;
            UserCoachCheack = dataSnap.child("UserNames").child(username).getValue().toString();
            if (UserCoachCheack.indexOf((","+HelpString.substring(1,HelpString.indexOf("=")))+",")==-1) {
                FirstOnClick=false;
                coachhelp = new Coach(HelpString.substring(1,HelpString.indexOf("=")),dataSnap.child("ProfileCoach").child(HelpString.substring(1,HelpString.indexOf("="))));
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
                            ii++;
                        }
                        else {
                            CBcoachcheack=true;
                        }
                    }
                }

            }


            else
            {
                FirstOnClick=false;
            }
        }
        while (HelpString.indexOf(", ")!=-1)
        {

            if (((HelpString.indexOf(", ")!=-1)&&(ii<coaches.length)))
            {
                HelpString = HelpString.substring(HelpString.indexOf(", ")+2);
                if (UserCoachCheack.indexOf((","+HelpString.substring(0,HelpString.indexOf("=")))+",")==-1)
                {
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
                                ii++;
                            }
                            else {
                                CBcoachcheack=true;
                            }

                    }}

                }



            }
        }
        if (HelpString.indexOf(", ")==-1){
            coachesList2 = new ArrayList<>();
            if (takecoach)
            {
                coachesList2.add(coaches[0]);
                for (int j=1; j<ii; j++)
                {
                    coachesList2.add(coaches[j]);


                }
            }
            else
            {
                for (int j=1; j<ii; j++)
                {

                    coachesList2.add(coaches[j]);

                }
            }


            adapter = new CoachListAdapter(getActivity(), R.layout.customlayoutcoachprofile, coachesList2);
            listView.setAdapter(adapter);



            return;
        }
    }

    private void RefreshCoaches() {

        if (FirstOnClick){
            HelpString=CoachProfiles;
            UserCoachCheack = dataSnap.child("UserNames").child(username).getValue().toString();
            if (UserCoachCheack.indexOf((","+HelpString.substring(1,HelpString.indexOf("=")))+",")==-1) {
                FirstOnClick = false;
                coaches[i] = new Coach(HelpString.substring(1, HelpString.indexOf("=")), dataSnap.child("ProfileCoach").child(HelpString.substring(1, HelpString.indexOf("="))));
                i++;

            }
            else
            {
                FirstOnClick=false;
            }
        }
        while (HelpString.indexOf(", ")!=-1)
        {

                if (((HelpString.indexOf(", ")!=-1)&&(i<coaches.length)))
                {
                    HelpString = HelpString.substring(HelpString.indexOf(", ")+2);
                    if (UserCoachCheack.indexOf((","+HelpString.substring(0,HelpString.indexOf("=")))+",")==-1)
                    {
                        coaches[i] = new Coach(HelpString.substring(0,HelpString.indexOf("=")),dataSnap.child("ProfileCoach").child(HelpString.substring(0,HelpString.indexOf("="))));

                        i++;


                    }



                }
            }
        if (HelpString.indexOf(", ")==-1){
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


    /**
     * Dialog Check if the user is sure he wants to contact the coach.
     */
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
                if (minute>=10){
                    time = String.valueOf(hourofday)+":"+String.valueOf(minute);}
                else
                {
                    time = String.valueOf(hourofday)+":0"+String.valueOf(minute);
                }

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("sender",username);
                hashMap.put("receiver",coaches[positionadb].getName());
                hashMap.put("message","נשלחה בקשת קשר");
                hashMap.put("time",time);
                final String CoachName=coaches[positionadb].getName();
                reference3.child(username+"&"+(CoachName)).child("1").setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    referenceNotification.child(CoachName).child("ProgramAlarm").setValue(username);
                                    reference3.child(username+"&"+CoachName).child("num").child("num").setValue(2);
                                    reference4 = FirebaseDatabase.getInstance().getReference("ProgramRoom");
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    String htmlnewprogram="<html><body><p><b><i><u><span style=\"text-decoration:line-through;\">​</span></u></i></b><b><i><u><span style=\"text-decoration:line-through;\"><i><b><u><span style=\"text-decoration:line-through;\">המאמן שלך עדין לא עדכן את התוכנית אימון שלך</span></u></b></i></span></u></i></b></p>\n</body></html>";
                                    hashMap.put("Data",htmlnewprogram);
                                    reference4.child(username+"&"+CoachName).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                            }, 100);

                        }
                    }
                });


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
    }

}
