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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

/**
 * Main activity - the "home" activity of the application.
 */
public class MainActivity extends AppCompatActivity {

    LinearLayout LLayoutmain;
    /**
     * The Intent.
     */
    Intent intent, /**
     * The Intent settings.
     */
    intentSettings, /**
     * The Intent update profile.
     */
    intentUpdateProfile, /**
     * The Intent credits.
     */
    intentCredits, /**
     * The Intent mail.
     */
    intentMail;
    /**
     * The Username.
     */
    String username, /**
     * The Type.
     */
    type;
    /**
     * The Fragchat.
     */
    TextView fragchat;
    /**
     * The Frag int.
     */
    int FragInt=1;
    /**
     * The Myfragment.
     */
    public Fragment myfragment;
    private DatabaseReference databaseReference;
    private ConnectivityManager connectivityManager;

    /**
     * The Sign out.
     */
    Boolean SignOut=false;
    /**
     * The Connected.
     */
    boolean connected=true;
    /**
     * The S pusername.
     */
    String SPusername="nousername", /**
     * The S ptype.
     */
    SPtype="notype", /**
     * The S pemail.
     */
    SPemail="noemail", /**
     * The S ppassword.
     */
    SPpassword="nopassword";
    int CoachesNumber=0;
    Toolbar toolbar;
    TextView menuchat,menuprogram,menusearch,menualert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.inflateMenu(R.menu.menu_fragment);

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

            Intent service = new Intent(this,BackgroundService.class);
            SharedPreferences.Editor editor = getSharedPreferences("service", MODE_PRIVATE).edit();
            editor.putString("username", username);
            editor.putBoolean("signout", false);
            editor.apply();
            service.putExtra("time",15);
            startService(service);

        }
            LLayoutmain = (LinearLayout)findViewById(R.id.LLayoutmain);
            menuchat = (TextView)findViewById(R.id.menuchat);
            menuprogram = (TextView)findViewById(R.id.menuprogram);
            menusearch = (TextView)findViewById(R.id.menusearch);
            if (type.equals("Coach"))
            {
                menusearch.setVisibility(View.GONE);
                LLayoutmain.setWeightSum(3);
            }

            menualert = (TextView)findViewById(R.id.menualert);
            menuchat.setTextColor(Color.WHITE);
            menuprogram.setTextColor(Color.BLACK);
            menusearch.setTextColor(Color.BLACK);
            menualert.setTextColor(Color.BLACK);


            databaseReference = FirebaseDatabase.getInstance().getReference().child("UserNames");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (type.equals("User"))
                    {
                        String data = dataSnapshot.child(username).getValue().toString();
                        CoachesNumber = countChar(data,',')-3;
                    }}
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }});
        }
    else
        {
            Intent intent = new Intent(this,ListProgramNoInternet.class);
            startActivity(intent);
            finish();
        }}





    /**
     * Gets username.
     *
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets type.
     *
     * @return the type - Coach or User.
     */
    public String getType() {
        return type;
    }

    /**
     * Count char int.
     *
     * @param str The string for testing.
     * @param c   the char test.
     * @return The number of times the parameter c appears in the str string.
     */
    public int countChar(String str, char c)
    {
        int count = 0;

        for(int i=0; i < str.length(); i++)
        {    if(str.charAt(i) == c)
            count++;
        }

        return count;
    }





    public void Credits(MenuItem item) {
        intentCredits = new Intent(this, Credits.class);
        startActivity(intentCredits);
    }

    public void Settings(MenuItem item) {
        intentSettings = new Intent(this, Settings.class);
        intentSettings.putExtra("type",type);
        intentSettings.putExtra("username",username);
        startActivity(intentSettings);
    }

    public void Mail(MenuItem item) {
        intentMail = new Intent(this, Mail.class);
        startActivity(intentMail);
    }

    public void Profile(MenuItem item) {
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

    public void SignOut(MenuItem item) {
        SharedPreferences onoffref = getSharedPreferences("BackgroundService", MODE_PRIVATE);
        int onoff = onoffref.getInt("onoff",0);
        onoff=onoff+1;
        SharedPreferences.Editor editorr = getSharedPreferences("BackgroundService", MODE_PRIVATE).edit();
        editorr.putInt("onoff",onoff);
        editorr.apply();

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
        this.finish();
        finish();
        startActivity(new Intent(this,LogInActivity.class));
    }

    public void onClickChat(View view) {
        try {
            if (FragInt != 1) {
                menuchat.setTextColor(Color.WHITE);
                menuprogram.setTextColor(Color.BLACK);
                menusearch.setTextColor(Color.BLACK);
                menualert.setTextColor(Color.BLACK);
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

    public void onClickProgram(View view) {
        try{

            if (FragInt != 2) {

                menuchat.setTextColor(Color.BLACK);
                menuprogram.setTextColor(Color.WHITE);
                menusearch.setTextColor(Color.BLACK);
                menualert.setTextColor(Color.BLACK);

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

    public void onClickSearch(View view) {
        try{
            if (FragInt != 3) {
                if (type.equals("User")) {
                    if (CoachesNumber>6)
                    {
                        Toast.makeText(getBaseContext(),"אתה לא יכול לשלוח עוד בקשות קשר למאמנים, אי אפשר להיות בקשר עם יותר מ6 מאמנים",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        menuchat.setTextColor(Color.BLACK);
                        menuprogram.setTextColor(Color.BLACK);
                        menusearch.setTextColor(Color.WHITE);
                        menualert.setTextColor(Color.BLACK);
                        Thread.sleep(200);
                        FragInt = 3;
                        myfragment = new FragmentSearch();
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_switch, myfragment);
                        fragmentTransaction.commit();}

                }
            }
        }catch (Exception e) {
            System.out.println("An exception!");
        }
    }

    public void onClickAlert(View view) {
        try{
            if (FragInt != 4) {
                menuchat.setTextColor(Color.BLACK);
                menuprogram.setTextColor(Color.BLACK);
                menusearch.setTextColor(Color.BLACK);
                menualert.setTextColor(Color.WHITE);
                Thread.sleep(200);
                FragInt = 4;
                myfragment = new FragmentAlerts();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_switch, myfragment);
                fragmentTransaction.commit();
            }
        }catch (Exception e) {
            System.out.println("An exception!");
        }
    }
}