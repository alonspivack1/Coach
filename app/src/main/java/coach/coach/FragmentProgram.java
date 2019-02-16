package coach.coach;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentProgram extends Fragment{

    DatabaseReference databaseReference;
    DataSnapshot dataSnap;
    Coach[] coaches = new Coach[3];
    int i =0;
    String username,type,sub,help;
    ArrayList<Coach> coachesList;
    ListView listView;

    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_program, container,false);
        listView = (ListView) view.findViewById(R.id.programlist);
        coachesList = new ArrayList<>();

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



        try {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    RefreshProgramlist();
                }
            }, 100);
        } catch (Exception e) {
            System.out.println("An exception!");
        }

        return view;

    }

    public void RefreshProgramlist(){
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
}