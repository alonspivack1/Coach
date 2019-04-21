package coach.coach;



import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Main activity - the "home" activity of the application.
 */
public class MainActivity extends AppCompatActivity {

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
     * The Button 1.
     */
    Button button1, /**
     * The Button 2.
     */
    button2, /**
     * The Button 3.
     */
    button3, /**
     * The Button 4.
     */
    button4;
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

            Intent service = new Intent(this,BackgroundService.class);
            SharedPreferences.Editor editor = getSharedPreferences("service", MODE_PRIVATE).edit();
            editor.putString("username", username);
            editor.putBoolean("signout", false);
            editor.apply();
            service.putExtra("time",15);
            startService(service);

        }


        button4 = (Button) findViewById(R.id.button4);
        button3 = (Button) findViewById(R.id.button3);
        button2 = (Button) findViewById(R.id.button2);
        button1 = (Button) findViewById(R.id.button1);
        button1.setVisibility(View.GONE);
        button2.setVisibility(View.GONE);
        button3.setVisibility(View.GONE);
            button4.setVisibility(View.GONE);

            final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                button1.setVisibility(View.VISIBLE);
                button2.setVisibility(View.VISIBLE);
                button4.setVisibility(View.VISIBLE);
                if (type.equals("User")) {
                    button3.setVisibility(View.VISIBLE);
                }
            }
        }, 1100);



            databaseReference = FirebaseDatabase.getInstance().getReference().child("UserNames");

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (type.equals("User"))
                    {
                        String data = dataSnapshot.child(username).getValue().toString();
                        int CoachesNumber = countChar(data,',')-3;
                        if (CoachesNumber>6)
                        {
                            button3.setEnabled(false);
                        }

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


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
        return true;
    }

    /**
     * move to chat fragment.
     *
     * @param view the view
     */
    public void FragmentOneClick(View view) {
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

    /**
     * move to program fragment.
     *
     * @param view the view
     */
    public void FragmentTwoClick(View view) {

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

    /**
     * move to search fragment.
     * @param view the view
     */
    public void FragmentThirdClick(View view) {
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

    /**
     * move to alerts fragment.
     *
     * @param view the view
     */
    public void FragmentFourthClick(View view){

        try{
            if (FragInt != 4) {
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


}