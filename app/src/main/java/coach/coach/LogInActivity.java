package coach.coach;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.util.SparseArray;
import android.view.LayoutInflater;
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
import com.google.android.gms.tasks.OnFailureListener;
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
import java.util.Map;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;
    EditText editTextEmail, editTextPassword,editTextUserName;
    ProgressBar progressBar;
    String UserNames="",CoachNames="";
    String type;
    DataSnapshot DataSnap;
    Intent userIntent,coachIntent,MainActivityIntent,intentCredits;
    String SPusername="nousername",SPtype="notype",SPemail="noemail",SPpassword="nopassword";
    CheckBox cbAutoLogIn;
    boolean OneTimeSP=true;

    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        setTitle("התחברות");
        userIntent = new Intent(this,UserProfileMaker.class);
        coachIntent = new Intent(this,CoachProfileMaker.class);
        MainActivityIntent = new Intent(this,MainActivity.class);
        cbAutoLogIn = (CheckBox) findViewById(R.id.cbAutoLogIn);

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
                        Log.e("CoachNames", CoachNames);
                    }
                    if (dataSnapshot.hasChild("UserNames")) {

                        Map<String, Object> objectMap2 = (HashMap<String, Object>) dataSnapshot.child("UserNames").getValue();
                        UserNames = objectMap2.toString();
                        Log.e("UserNames", UserNames);
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
                }}

                // databaseReference.child("Rating").child("pa").child("RatersNames").child("aa").setValue("4");
                // databaseReference.child("Rating").child("pa").child("RatersNames").child("aaa").setValue("6");
               /* if (dataSnapshot.child("Rating").child("pa").child("RatersNames").hasChild("aaa"))
                {
                    Log.e("aaa","NoNull");
                }
                else
                {                    Log.e("aaa","Null");
                }
                if (!dataSnapshot.child("Rating").child("pa").child("RatersNames").hasChild("ap"))
                {
                    Log.e("ap","Null");
                }
                Log.e("RatersNames",dataSnapshot.child("Rating").child("pp").child("RatersNames").child("aa").getValue().toString());
                Log.e("RatersNumber",dataSnapshot.child("Rating").child("pp").child("RatersNumber").getValue().toString());
                Log.e("Rating",dataSnapshot.child("Rating").child("pp").child("Rating").getValue().toString());*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        textViewSignup.setOnClickListener(this);
        textViewForgotPassword.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        String st = item.getTitle().toString();
            if (st.equals("קרדיטים"))
            {
                intentCredits = new Intent(this, Credits.class);
                startActivity(intentCredits);
            }
        return true;
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

                    //finish();
                    /*
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);*/
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
                            Log.e("WORK!","WORK!");

                        }
                        else
                        {
/*                            User user = new User (username,(DataSnap.child("ProfileUser").child(username).getValue().toString()));
                            Log.e("Full",user.getDetails());*/

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
                                    Log.e("WORK!!","WORK!!");


                                }
                                else
                                {
                                    /*
                                    Coach coach = new Coach("pp",DataSnap.child("ProfileCoach").child(username).getValue().toString());
                                    Log.e("Age",DataSnap.child("ProfileCoach").child(username).getValue().toString());
                                    Log.e("Age",coach.getAge());*/


                                    /*for (int index = DataSnap.child("ProfileCoach").child(username).getValue().toString().indexOf("=");
                                         index >= 0;
                                         index = DataSnap.child("ProfileCoach").child(username).getValue().toString().indexOf("=", index + 1))
                                    {
                                        Log.e("Answers",index+"");
                                    }*/


                                    coachIntent.putExtra("username",username);
                                    startActivity(coachIntent);
                                    finish();
                                }
                            }
                        else{
                            //Toast.makeText(this,"Email or password or username, is wrong",Toast.LENGTH_LONG).show();
                            Log.e("ERROR","Email or password or username, is wrong");
                            }
                        }


                }
                else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

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
                            Log.e("WORK!!!1","WORK!!!1");


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
                                Log.e("WORK!!!!1","WORK!!!!");



                            }
                            else
                            {

                                coachIntent.putExtra("username",username);
                                startActivity(coachIntent);
                                finish();
                            }
                        }
                        else{
                            Log.e("ERROR","Email or password or username, is wrong");
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

}