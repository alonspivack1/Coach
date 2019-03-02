package coach.coach;



import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressBar progressBar;
    EditText editTextEmail, editTextPassword,editTextUser,etPhoneNo,etCode;
    Button SMSbtn;
    DatabaseReference reference,reference2,reference3;
    int numflag1,numflag2,numflag3,numflag4;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference,databaseReference2;
    String CoachAnswer="",UserAnswer="";
    Random rn;
    Intent LogInIntent,UserIntent,CoachIntent,intentCredits;
    int codeInt;
    String smsNum;
    String codeString="";
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("הרשמה");

        FirebaseDatabase reference;
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextUser = (EditText)findViewById(R.id.editTextUser);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        UserIntent = new Intent(this,UserProfileMaker.class);
        CoachIntent = new Intent(this,CoachProfileMaker.class);


        spinner = (Spinner) findViewById(R.id.spinner);
        String[] SpinnerStrings = new String[3];
        SpinnerStrings[0]="Select Type:";
        SpinnerStrings[1]="User";
        SpinnerStrings[2]="Coach";

        ArrayAdapter<String>SpinnerAdapter = new ArrayAdapter<String>(SignUpActivity.this,android.R.layout.simple_list_item_1,SpinnerStrings);
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(SpinnerAdapter);

        SMSbtn = (Button) findViewById(R.id.btnSendSMS);
        etPhoneNo = (EditText) findViewById(R.id.etPhoneNo);
        etCode = (EditText) findViewById(R.id.etCode);
        rn = (Random) new Random();

        LogInIntent= new Intent(this,LogInActivity.class);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.buttonSignUp).setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);


        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("CoachNames")) {
                    Map<String, Object> objectMap = (HashMap<String, Object>) dataSnapshot.child("CoachNames").getValue();
                    CoachAnswer = objectMap.toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("UserNames")) {
                    Map<String, Object> objectMap = (HashMap<String, Object>) dataSnapshot.child("UserNames").getValue();
                UserAnswer = objectMap.toString();}
               // Log.e("Coach:",CoachAnswer);
             //   Log.e("User:",UserAnswer);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final String username = editTextUser.getText().toString();
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email");
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
        if (username.length() < 2) {
            editTextUser.setError("Minimum lenght of username should be 2");
            editTextUser.requestFocus();
            return;
        }
        if (username.length() > 10) {
            editTextUser.setError("Max lenght of username should be 10");
            editTextUser.requestFocus();
            return;
        }
        if (password.length() > 16) {
            editTextPassword.setError("Max lenght of password should be 16");
            editTextPassword.requestFocus();
            return;
        }
        if(!Pattern.matches("[A-Za-z0-9]+", username))
        {
            editTextUser.setError("Use just letters and numbers");
            editTextUser.requestFocus();
            return;
        }


        numflag1 =CoachAnswer.indexOf(", "+username+"=");
        if(numflag1==-1)
        {
            numflag1 =CoachAnswer.toLowerCase().indexOf("{"+username.toLowerCase()+"=");
            if(numflag1!=-1)
            {
            editTextUser.setError("This name is taken, choose other name");
            editTextUser.requestFocus();
            return;
            }
        }

        numflag3 =UserAnswer.indexOf(", "+username+"=");
        if(numflag3==-1)
        {
            numflag3 =UserAnswer.toLowerCase().indexOf("{"+username.toLowerCase()+"=");
            if(numflag3!=-1)
            {
                editTextUser.setError("This name is taken, choose other name");
                editTextUser.requestFocus();
                return;
            }
        }
        else {
            editTextUser.setError("This name is taken, choose other name");
            editTextUser.requestFocus();
            return;
        }

        if (spinner.getSelectedItem().toString().equals("Select Type:"))
        {
            Toast.makeText(this,"Choose type",Toast.LENGTH_LONG).show();
            return;
        }

        if (!codeString.equals(etCode.getText().toString())) {
            etCode.setError("Wrong sms code");
            etCode.requestFocus();
            return;
        }





        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    String userid = firebaseUser.getUid();
                    if (spinner.getSelectedItem().toString().equals("User"))
                    {
                        reference2 = FirebaseDatabase.getInstance().getReference("UserNames");
                        reference2.child(username).setValue(smsNum+","+userid+","+username+",");

                    }
                    if (spinner.getSelectedItem().toString().equals("Coach"))
                    {
                        reference2 = FirebaseDatabase.getInstance().getReference("CoachNames");
                        reference2.child(username).setValue(smsNum+","+userid+","+username+",");
                    }

                  //  reference2.child(username).setValue(smsNum);
                  //  HashMap<String, String> hashMap2 = new HashMap<>();
                //    hashMap2.put(username,"");
                    //reference2.setValue(hashMap2).addOnCompleteListener(new OnCompleteListener<Void>() {
                        /*reference2.child(username).setValue(smsNum).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            task.isComplete();
                            startActivity(LogInIntent);
                            finish();


                        }
                    });*/
                        if (spinner.getSelectedItem().toString().equals("User"))
                        {
                            reference = FirebaseDatabase.getInstance().getReference("ProfileUser").child(username);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("Age","0" );
                            hashMap.put("Weight","0" );
                            hashMap.put("Height","0" );
                            hashMap.put("PracticeTime", "0");
                            hashMap.put("Goal",",");
                            hashMap.put("Equipment", "0");
                            hashMap.put("Gender","Male" );
                            hashMap.put("Description", "0");
                            //   hashMap.put("username",username);
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isComplete()) {
                                        UserIntent.putExtra("username",username+"");
                                        startActivity(UserIntent);
                                        finish();
                                    }
                                }
                            });
                        }
                        else
                            {
                                reference = FirebaseDatabase.getInstance().getReference("ProfileCoach").child(username);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("Age","0" );
                                hashMap.put("StudyPlace","0" );
                                hashMap.put("Professionalization","," );
                                hashMap.put("Description", "0");
                                hashMap.put("Gender","Male" );
                                hashMap.put("CoachTime", "0");
                                reference3=FirebaseDatabase.getInstance().getReference("Rating").child(username);
                                HashMap<String, String> hashMap2 = new HashMap<>();
                                hashMap2.put("RatersNumber",""+0 );
                                hashMap2.put("Rating", ""+0);
                                reference3.setValue(hashMap2);//TODO לבדוק אם צריך להוסיף oncompletelistener
                                //   hashMap.put("username",username);
                                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isComplete()) {
                                            CoachIntent.putExtra("username",username);
                                            startActivity(CoachIntent);
                                            finish();
                                        }
                                    }
                                });
                            }

                } else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }
    private void sendSMS(String phoneNumber, String message)
    {

       SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    public void sendcode(View view) {
        smsNum = etPhoneNo.getText().toString();
        if (smsNum.length()>0)
        {
            numflag2 = UserAnswer.indexOf("="+smsNum+",");
            if (numflag2==-1)
            {
                    numflag4 = CoachAnswer.indexOf("="+smsNum+",");
                    if (numflag4==-1)
                    {
                            codeInt=rn.nextInt(90000)+10000;
                            codeString="" + codeInt;
                            Log.e("smsNum",smsNum);
                            Log.e("codeString",codeString);
                            sendSMS(smsNum, codeString);
                    }
                    else {
                        etPhoneNo.setError("This number is used");
                        etPhoneNo.requestFocus();
                    }

            }
            else {
                etPhoneNo.setError("This number is used");
                etPhoneNo.requestFocus();
            }


        }
        else{
            Toast.makeText(getBaseContext(),
                    "Enter phone number.",
                    Toast.LENGTH_SHORT).show();
        }



    }





    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSignUp:
                try {
                    registerUser();
                }
                catch (Exception e)
                {
                    Toast.makeText(this, "Verify your phone number",Toast.LENGTH_LONG).show();
                }
                //Log.e("keys",spinner.getSelectedItem().toString());
                break;

            case R.id.textViewLogin:
               // Log.e("keys",Answer);
                startActivity(LogInIntent);
                finish();
                break;
        }
    }
}