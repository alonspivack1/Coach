package coach.coach;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import java.util.Map;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;
    EditText editTextEmail, editTextPassword,editTextUserName;
    ProgressBar progressBar;
    String UserNames="",CoachNames="";
    DataSnapshot DataSnap;
    Intent userIntent,coachIntent,MainActivityIntent;

    private DatabaseReference databaseReference,databaseReference2,databaseReference3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        userIntent = new Intent(this,UserProfileMaker.class);
        coachIntent = new Intent(this,CoachProfileMaker.class);
        MainActivityIntent = new Intent(this,MainActivity.class);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> objectMap = (HashMap<String, Object>) dataSnapshot.child("CoachNames").getValue();
                CoachNames = objectMap.toString();
                Log.e("CoachNames",CoachNames);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> objectMap = (HashMap<String, Object>) dataSnapshot.child("UserNames").getValue();
                UserNames = objectMap.toString();
                Log.e("UserNames",UserNames);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        databaseReference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnap = dataSnapshot;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextUserName = findViewById(R.id.editTextUserName);
        progressBar = findViewById(R.id.progressbar);

        findViewById(R.id.textViewSignup).setOnClickListener(this);
        findViewById(R.id.buttonLogin).setOnClickListener(this);

    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

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
                        Map<String, Object> objectMap = (HashMap<String, Object>) DataSnap.child("ProfileUser").child(username).getValue();
                        String UserData = objectMap.toString();
                        if (UserData.indexOf(", {Age}=0")==-1)
                        {
                            MainActivityIntent.putExtra("username",username);
                            MainActivityIntent.putExtra("type","User");
                            startActivity(MainActivityIntent);
                            finish();
                            Log.e("WORK!","WORK!!");

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
                        if (CoachNames.indexOf(userid+","+editTextUserName.getText().toString()+",")!=-1)
                            {
                                Map<String, Object> objectMap = (HashMap<String, Object>) DataSnap.child("ProfileCoach").child(username).getValue();
                                String CoachData = objectMap.toString();
                                if (CoachData.indexOf(", {Age}=0")==-1)
                                {
                                    MainActivityIntent.putExtra("username",username);
                                    MainActivityIntent.putExtra("type","Coach");
                                    startActivity(MainActivityIntent);
                                    finish();
                                    Log.e("WORK!","WORK!!");


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
        }
    }

}