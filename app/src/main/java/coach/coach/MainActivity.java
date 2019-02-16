package coach.coach;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.flags.Flag;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Intent intent,intentSettings;
    String username,type,CoachProfiles="";
    TextView fragchat,fragtv;
    int FlagInt=1;
    public Fragment myfragment;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragtv = (TextView) findViewById(R.id.fragtv);

        intent = getIntent();
        username=intent.getStringExtra("username");
        type=intent.getStringExtra("type");
       /* if (type.equals("Coach")){

            databaseReference = FirebaseDatabase.getInstance().getReference();

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map<String, Object> objectMap = (HashMap<String, Object>) dataSnapshot.child("ProfileCoach").getValue();
                    CoachProfiles = objectMap.toString();
                    //fragtv.setText(CoachProfiles+"");
                  // i++;


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }*/
        //if (type.equals("User")) {}
        fragchat = (TextView) findViewById(R.id.fragchat);


    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    { MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        String st = item.getTitle().toString();
        if (st.equals("Settings")) {
            if (type.equals("Coach")) {
                intentSettings = new Intent(this, CoachProfileMaker.class);
                intentSettings.putExtra("username",username);
                startActivity(intentSettings);
            }
            if (type.equals("User")) {
                intentSettings = new Intent(this, UserProfileMaker.class);
                intentSettings.putExtra("username",username);
                startActivity(intentSettings);

            }

        }
        return true;
    }
    public void FragmentOneClick(View view) throws InterruptedException {
        try {
        if (FlagInt != 1) {
            Thread.sleep(200);
            FlagInt = 1;
            myfragment = new FragmentChat();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_switch, myfragment);
            fragmentTransaction.commit();
        }
    }catch (Exception e) {
        System.out.println("An exception!");
    }




    }
    public void FragmentTwoClick(View view) throws InterruptedException {

        try{
        if (FlagInt != 2) {
            Thread.sleep(200);
            FlagInt = 2;
            myfragment = new FragmentProgram();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_switch, myfragment);
            fragmentTransaction.commit();
        }
    }catch (Exception e) {
        System.out.println("An exception!");
    }


    }

    public void FragmentThirdClick(View view) throws InterruptedException {
        try{
            if (FlagInt != 3) {
            if (type.equals("User")) {
                Thread.sleep(200);
                FlagInt = 3;
                myfragment = new FragmentSearch();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_switch, myfragment);
                fragmentTransaction.commit();
            }
        }
    }catch (Exception e) {
        System.out.println("An exception!");
    }

    }
    public String getUsername() {
        return username;
    }
    public String getType() {
        return type;
    }
  /*
    public void ABC(View view) {
       // Toast.makeText(this,"Answer: "+CoachProfiles,Toast.LENGTH_LONG).show();
        fragtv.setText(CoachProfiles);
    }
*/
   /* @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnfragchat:
                myfragment = new FragmentChat();

                fm = getFragmentManager();
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_switch, myfragment);
                fragmentTransaction.commit();

                break;

            case R.id.btnfragprogram:
                myfragment = new FragmentProgram();

                fm = getFragmentManager();
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_switch, myfragment);
                fragmentTransaction.commit();
                break;
            case R.id.btnfragsearch:
                myfragment = new FragmentSearch();

                fm = getFragmentManager();
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_switch, myfragment);
                fragmentTransaction.commit();
                break;
        }
    }*/
}