package coach.coach;


import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.flags.Flag;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Intent intent,intentSettings,intentUpdateProfile,intentCredits,intentMail;
    String username,type,CoachProfiles="";
    TextView fragchat,fragtv;
    int FragInt=1;
    public Fragment myfragment;
    private DatabaseReference databaseReference;
    private ConnectivityManager connectivityManager;
    Button button1,button2,button3;
    Boolean SignOut=false;
    boolean connected=true;
    String SPusername="nousername",SPtype="notype",SPemail="noemail",SPpassword="nopassword";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");

        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }
        else{
            connected = false;
     }
        if (connected){
        intent = getIntent();
        username = intent.getStringExtra("username");
        type = intent.getStringExtra("type");

        SharedPreferences prefs = getSharedPreferences("AutoLogIn", MODE_PRIVATE);
        SPusername = prefs.getString("username", "nousername");
        SPtype = prefs.getString("type","notype");
        SPemail = prefs.getString("email","noemail");
        SPpassword = prefs.getString("password","nopassword");
        if ((!SPusername.equals("nousername"))&&(!SPtype.equals("notype"))&&(!SPemail.equals("noemail"))&&(!SPpassword.equals("nopassword")))
        {

            Intent service = new Intent(this,TimerService.class);
            SharedPreferences.Editor editor = getSharedPreferences("service", MODE_PRIVATE).edit();
            editor.putString("username", username);
            editor.putBoolean("signout", false);
            editor.apply();
            service.putExtra("time",15);
            startService(service);

        }



        button3 = (Button) findViewById(R.id.button3);
        button2 = (Button) findViewById(R.id.button2);
        button1 = (Button) findViewById(R.id.button1);
        button1.setVisibility(View.GONE);
        button2.setVisibility(View.GONE);
        button3.setVisibility(View.GONE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                button1.setVisibility(View.VISIBLE);
                button2.setVisibility(View.VISIBLE);
                if (type.equals("User")) {
                    button3.setVisibility(View.VISIBLE);
                }
            }
        }, 1100);


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
    else
        {
            Intent intent = new Intent(this,ListProgramNoInternet.class);
            startActivity(intent);
            finish();
        }}
    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        menu.add("התנתקות");
        menu.add("עדכון פרופיל");
        menu.add("שליחת מייל למפתח");
        menu.add("הגדרות");


        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        String st = item.getTitle().toString();
        if (st.equals("עדכון פרופיל")) {
            if (type.equals("Coach")) {
                intentUpdateProfile = new Intent(this, CoachProfileMaker.class);
                intentUpdateProfile.putExtra("username",username);
                startActivity(intentUpdateProfile);
            }
            if (type.equals("User")) {
                intentUpdateProfile = new Intent(this, UserProfileMaker.class);
                intentUpdateProfile.putExtra("username", username);
                startActivity(intentUpdateProfile);
            }
        }
        if (st.equals("קרדיטים"))
        {
            intentCredits = new Intent(this, Credits.class);
            startActivity(intentCredits);
        }
        if (st.equals("הגדרות"))
        {
            intentSettings = new Intent(this, Settings.class);
            intentSettings.putExtra("type",type);
            intentSettings.putExtra("username",username);

            startActivity(intentSettings);
        }
        if (st.equals("שליחת מייל למפתח"))
        {
            intentMail = new Intent(this, Mail.class);
            startActivity(intentMail);
        }
        if (st.equals("התנתקות"))
        {
            SharedPreferences.Editor editorsevice = getSharedPreferences("service", MODE_PRIVATE).edit();
            editorsevice.putString("username", username);
            editorsevice.putBoolean("signout", true);
            editorsevice.apply();

            SharedPreferences.Editor editor = getSharedPreferences("AutoLogIn", MODE_PRIVATE).edit();
            editor.putString("username", "nousername");
            editor.putString("type", "notype");
            editor.putString("email", "noemail");
            editor.putString("password", "nopassword");
            editor.apply();
            SignOut=true;
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this,LogInActivity.class));
        }
        return true;
    }
    public void FragmentOneClick(View view) throws InterruptedException {
        try {
        if (FragInt != 1) {
            Thread.sleep(200);
            FragInt = 1;
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
        if (FragInt != 2) {
            Thread.sleep(200);
            FragInt = 2;
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
            if (FragInt != 3) {
            if (type.equals("User")) {
                Thread.sleep(200);
                FragInt = 3;
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
                myfragment = new ();

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