package coach.coach;

import android.support.annotation.NonNull;
import android.os.Bundle;

import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class FragmentSearch extends Fragment implements View.OnClickListener {
    DatabaseReference databaseReference;
    String CoachProfiles = "", HelpString = "";
    TextView fragtv;
    Boolean FirstOnClick = true;
    ListView listView;
    DataSnapshot dataSnap;
    Coach[] coaches = new Coach[8];
    int i = 0;


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

        return v;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                if (FirstOnClick)
                {
                    FirstOnClick=false;
                    HelpString=CoachProfiles;
                    Log.e("FULL",HelpString);
                    coaches[i] = new Coach(HelpString.substring(1,HelpString.indexOf("=")),dataSnap.child("ProfileCoach").child(HelpString.substring(1,HelpString.indexOf("="))).getValue().toString());
                  //  coaches[0] = new Coach("coach","1","1","1","1","1");
                    Log.e("Answer",HelpString.substring(1,HelpString.indexOf("=")));
                    i++;

                    Log.e("Dealits",dataSnap.child("ProfileCoach").child(HelpString.substring(1,HelpString.indexOf("="))).getValue().toString());
                }
                else
                {
                if ((i<coaches.length&&(HelpString.indexOf(", ")!=-1)))//&&(ll.indexOf(", ")!=-1)
                {
                    HelpString = HelpString.substring(HelpString.indexOf(", ")+2);
                    Log.e("FULL",HelpString);
                    coaches[i] = new Coach(HelpString.substring(0,HelpString.indexOf("=")),dataSnap.child("ProfileCoach").child(HelpString.substring(0,HelpString.indexOf("="))).getValue().toString());
                   // coaches[1] = new Coach("pp","1","1","1","1","1");
                    Log.e("Answer",HelpString.substring(0,HelpString.indexOf("=")));

                    i++;


                    Log.e("Dealits",dataSnap.child("ProfileCoach").child(HelpString.substring(0,HelpString.indexOf("="))).getValue().toString());


                }
                else {

                    ArrayList<Coach> coachesList = new ArrayList<>();
                    for (int j=0; j<i; j++)
                    {
                        coachesList.add(coaches[j]);
                    }

                    CoachListAdapter adapter = new CoachListAdapter(getActivity(), R.layout.customlayoutcoachprofile, coachesList);
                    listView.setAdapter(adapter);
                    return;
                }}
                break;

        }
    }



}
