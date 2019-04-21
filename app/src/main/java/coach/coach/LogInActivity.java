package coach.coach;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Log-In Activity
 */
public class LogInActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * The M auth.
     */
    FirebaseAuth mAuth;
    /**
     * The Edit text email.
     */
    EditText editTextEmail, /**
     * The Edit text password.
     */
    editTextPassword, /**
     * The Edit text user name.
     */
    editTextUserName;
    /**
     * The Progress bar.
     */
    ProgressBar progressBar;
    /**
     * The User names.
     */
    String UserNames="", /**
     * The Coach names.
     */
    CoachNames="";
    /**
     * The Type.
     */
    String type="";
    /**
     * The Data snap.
     */
    DataSnapshot DataSnap;
    /**
     * The User intent.
     */
    Intent userIntent, /**
     * The Coach intent.
     */
    coachIntent, /**
     * The Main activity intent.
     */
    MainActivityIntent, /**
     * The Intent credits.
     */
    intentCredits;
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
    /**
     * The Cb auto log in.
     */
    CheckBox cbAutoLogIn;
    /**
     * The One time sp.
     */
    boolean OneTimeSP=true;
    /**
     * The Connected.
     */
    boolean connected=true;
    /**
     * The Mymenu.
     */
    Menu mymenu;
    private ConnectivityManager connectivityManager;
    /**
     * The P blogin.
     */
    ProgressBar PBlogin;

    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        setTitle("התחברות");
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }
        if (connected) {
            SharedPreferences FirstLogIn = getSharedPreferences("FirstLogIn", MODE_PRIVATE);
            Log.e("FirstLogIn",FirstLogIn.getBoolean("firstlogin",true)+"");
            if (FirstLogIn.getBoolean("firstlogin",true))
            {
                addAutoStartup();
                SharedPreferences.Editor firstLogIneditor = getSharedPreferences("FirstLogIn", MODE_PRIVATE).edit();
                firstLogIneditor.putBoolean("firstlogin",false);
                firstLogIneditor.apply();
            }




        userIntent = new Intent(this,UserProfileMaker.class);
        coachIntent = new Intent(this,CoachProfileMaker.class);
        MainActivityIntent = new Intent(this,MainActivity.class);
        cbAutoLogIn = (CheckBox) findViewById(R.id.cbAutoLogIn);

        PBlogin = (ProgressBar)findViewById(R.id.PBlogin);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextUserName = findViewById(R.id.editTextUserName);
        progressBar = findViewById(R.id.progressbar);

        final TextView textViewSignup = (TextView)findViewById(R.id.textViewSignup);
        final TextView textViewForgotPassword = (TextView)findViewById(R.id.textViewForgotPassword);
        final Button buttonLogin = (Button)findViewById(R.id.buttonLogin);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnap = dataSnapshot;



                if (dataSnapshot.hasChild("CoachNames")) {
                        Map<String, Object> objectMap = (HashMap<String, Object>) dataSnapshot.child("CoachNames").getValue();
                        CoachNames = objectMap.toString();
                    }
                    if (dataSnapshot.hasChild("UserNames")) {

                        Map<String, Object> objectMap2 = (HashMap<String, Object>) dataSnapshot.child("UserNames").getValue();
                        UserNames = objectMap2.toString();
                    }
                if (OneTimeSP){
                    OneTimeSP=false;
                SharedPreferences prefs = getSharedPreferences("AutoLogIn", MODE_PRIVATE);
                SPusername = prefs.getString("username", "nousername");
                SPtype = prefs.getString("type","notype");
                SPemail = prefs.getString("email","noemail");
                SPpassword = prefs.getString("password","nopassword");
                if ((!SPusername.equals("nousername"))&&(!SPtype.equals("notype"))&&(!SPemail.equals("noemail"))&&(!SPpassword.equals("nopassword")))
                {
                    editTextEmail.setVisibility(View.GONE);
                    editTextPassword.setVisibility(View.GONE);
                    editTextUserName.setVisibility(View.GONE);
                    cbAutoLogIn.setVisibility(View.GONE);
                    buttonLogin.setVisibility(View.GONE);
                    textViewSignup.setVisibility(View.GONE);
                    textViewForgotPassword.setVisibility(View.GONE);
                    AutoLogIn();
                    PBlogin.setVisibility(View.GONE);

                }
                else
                {
                    SharedPreferences Programs = getSharedPreferences("Programs", Context.MODE_PRIVATE);
                    Programs.edit().clear().commit();
                    SharedPreferences Coaches = getSharedPreferences("Coaches", Context.MODE_PRIVATE);
                    Coaches.edit().clear().commit();
                    SharedPreferences Alerts = getSharedPreferences("Alerts", Context.MODE_PRIVATE);
                    Alerts.edit().clear().commit();

                    editTextEmail.setVisibility(View.VISIBLE);
                    editTextPassword.setVisibility(View.VISIBLE);
                    editTextUserName.setVisibility(View.VISIBLE);
                    cbAutoLogIn.setVisibility(View.VISIBLE);
                    buttonLogin.setVisibility(View.VISIBLE);
                    textViewSignup.setVisibility(View.VISIBLE);
                    textViewForgotPassword.setVisibility(View.VISIBLE);
                    PBlogin.setVisibility(View.GONE);



                }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        textViewSignup.setOnClickListener(this);
        textViewForgotPassword.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
    }
        else
        {
            Intent intent = new Intent(this,ListProgramNoInternet.class);
            startActivity(intent);
            finish();
        }
}



    private void userLogin() {
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum lenght of password should be 6");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {

                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    String userid = firebaseUser.getUid();
                    String username = editTextUserName.getText().toString();
                    if (UserNames.indexOf(userid+","+username+",")!=-1)
                    {
                        type="User";
                    }
                    if (CoachNames.indexOf(userid+","+editTextUserName.getText().toString()+",")!=-1)
                    {
                        type="Coach";
                    }
                    if (cbAutoLogIn.isChecked())
                    {
                        SharedPreferences.Editor editor = getSharedPreferences("AutoLogIn", MODE_PRIVATE).edit();
                        editor.putString("username", username);
                        editor.putString("type", type);
                        editor.putString("email", email);
                        editor.putString("password", password);
                        editor.apply();
                    }
                    if (type.equals("User"))
                    {
                        String UserData = DataSnap.child("ProfileUser").child(username).child("Age").getValue().toString();

                        if (!UserData.equals("0"))
                        {
                            MainActivityIntent.putExtra("username",username);
                            MainActivityIntent.putExtra("type",type);
                            startActivity(MainActivityIntent);
                            finish();

                        }
                        else
                        {


                            userIntent.putExtra("username",username);
                            startActivity(userIntent);
                            finish();
                        }
                    }
                    else {
                        if (type.equals("Coach"))
                            {
                                String CoachData = DataSnap.child("ProfileCoach").child(username).child("Age").getValue().toString();

                                if (!CoachData.equals("0"))
                                {
                                    MainActivityIntent.putExtra("username",username);
                                    MainActivityIntent.putExtra("type",type);
                                    startActivity(MainActivityIntent);
                                    finish();


                                }
                                else
                                {



                                    coachIntent.putExtra("username",username);
                                    startActivity(coachIntent);
                                    finish();
                                }
                            }
                        else{
                            Toast.makeText(getBaseContext(),"Email or password or username, is wrong",Toast.LENGTH_LONG).show();
                            }
                        }


                }
                else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Auto log in.
     */
    public void AutoLogIn()
    {
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(SPemail,SPpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {

                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    String userid = firebaseUser.getUid();
                    String username = SPusername;
                    if (UserNames.indexOf(userid+","+username+",")!=-1)
                    {
                        String UserData = DataSnap.child("ProfileUser").child(username).child("Age").getValue().toString();

                        if (!UserData.equals("0"))
                        {
                            finish();
                            MainActivityIntent.putExtra("username",username);
                            MainActivityIntent.putExtra("type","User");
                            startActivity(MainActivityIntent);


                        }
                        else
                        {
                            userIntent.putExtra("username",username);
                            startActivity(userIntent);
                            finish();
                        }
                    }
                    else {
                        if (CoachNames.indexOf(userid+","+username+",")!=-1)
                        {
                            String CoachData = DataSnap.child("ProfileCoach").child(username).child("Age").getValue().toString();

                            if (!CoachData.equals("0"))
                            {
                                MainActivityIntent.putExtra("username",username);
                                MainActivityIntent.putExtra("type","Coach");
                                startActivity(MainActivityIntent);
                                finish();



                            }
                            else
                            {

                                coachIntent.putExtra("username",username);
                                startActivity(coachIntent);
                                finish();
                            }
                        }
                        else{
                            Toast.makeText(getBaseContext(),"Email or password or username, is wrong",Toast.LENGTH_LONG).show();
                        }
                    }


                }
                else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewSignup:
                finish();
                startActivity(new Intent(this, SignUpActivity.class));
                finish();
                break;

            case R.id.buttonLogin:
                userLogin();
                break;
            case R.id.textViewForgotPassword:

                startActivity(new Intent(this, ResetPassword.class));
                finish();
                break; }
    }

    private void addAutoStartup() {

        try {
            Intent intent = new Intent();
            String manufacturer = android.os.Build.MANUFACTURER;
            Toast.makeText(this,"כדי לקבל התראות באפליקציה, צריך לאשר הפעלה אוטומטית",Toast.LENGTH_LONG).show();
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            } else if ("Honor".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            }
            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if  (list.size() > 0) {
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.e("ErrorAddAutoStartup" , String.valueOf(e));
        }
    }

}