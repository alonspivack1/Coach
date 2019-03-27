package coach.coach;



import android.Manifest;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
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
    EditText codeet;
    String username,password,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("הרשמה");





        ActivityCompat.requestPermissions(SignUpActivity.this,
                new String[]{Manifest.permission.SEND_SMS},
                1);

        FirebaseDatabase reference;
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextUser = (EditText)findViewById(R.id.editTextUser);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        UserIntent = new Intent(this,UserProfileMaker.class);
        CoachIntent = new Intent(this,CoachProfileMaker.class);


        spinner = (Spinner) findViewById(R.id.spinner);
        String[] SpinnerStrings = new String[3];
        SpinnerStrings[0]="סוג המשתמש";
        SpinnerStrings[1]="מתאמן";
        SpinnerStrings[2]="מאמן";

        ArrayAdapter<String>SpinnerAdapter = new ArrayAdapter<String>(SignUpActivity.this,android.R.layout.simple_list_item_1,SpinnerStrings);
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(SpinnerAdapter);

        SMSbtn = (Button) findViewById(R.id.btnSendSMS);
        etPhoneNo = (EditText) findViewById(R.id.etPhoneNo);
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
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        username = editTextUser.getText().toString();
        if (email.isEmpty()) {
            editTextEmail.setError("לא מילאת איימל");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("האיימל שגוי");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("לא מילאת סיסמא");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("אורך הסיסמא לא יכול להיות פחות מ6 תווים");
            editTextPassword.requestFocus();
            return;
        }
        if (username.length() < 2) {
            editTextUser.setError("אורך שן המשתמש לא יכול להיות פחות מ2 תווים");
            editTextUser.requestFocus();
            return;
        }
        if (username.length() > 10) {
            editTextUser.setError("אורך שם המשתמש לא יכול להיות יותר מ10 תווים");
            editTextUser.requestFocus();
            return;
        }
        if (password.length() > 16) {
            editTextPassword.setError("אורך הסיסמא לא יכול להיות יותר מ16 תווים");
            editTextPassword.requestFocus();
            return;
        }
        if(!Pattern.matches("[A-Za-z0-9]+", username))
        {
            editTextUser.setError("אפשר להשתמש רק באותיות ומספרים");
            editTextUser.requestFocus();
            return;
        }


        numflag1 =CoachAnswer.indexOf(", "+username+"=");
        if(numflag1==-1)
        {
            numflag1 =CoachAnswer.toLowerCase().indexOf("{"+username.toLowerCase()+"=");
            if(numflag1!=-1)
            {
            editTextUser.setError("השם משתמש הזה משומש, בחר שם אחר");
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
                editTextUser.setError("השם משתמש הזה משומש, בחר שם אחר");
                editTextUser.requestFocus();
                return;
            }
        }
        else {
            editTextUser.setError("השם משתמש הזה משומש, בחר שם אחר");
            editTextUser.requestFocus();
            return;
        }

        if (spinner.getSelectedItem().toString().equals("סוג המשתמש"))
        {
            Toast.makeText(this,"בחר סוג המשתמש",Toast.LENGTH_LONG).show();
            return;
        }
        if (codeString.equals(""))
        {
            etPhoneNo.setError("לא נשלח קוד");
            etPhoneNo.requestFocus();
            return;
        }


        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        codeet = new EditText(this);
        codeet.setHint("קוד");
        codeet.setGravity(Gravity.CENTER_HORIZONTAL);
        alertDialog.setView(codeet);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"שלח", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (codeet.getText().toString().equals(codeString)){

                    StartSignUp();
                }
                else
                {
                  Log.e("Wrong","Worng code");
                  //TODO לשנות לטוסט
                }
            }

        });

        new Dialog(getApplicationContext());
        alertDialog.show();
    }

    public void StartSignUp()
    {

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    String userid = firebaseUser.getUid();
                    if (spinner.getSelectedItem().toString().equals("מתאמן"))
                    {
                        reference2 = FirebaseDatabase.getInstance().getReference("UserNames");
                        reference2.child(username).setValue(smsNum+","+userid+","+username+",");

                    }
                    if (spinner.getSelectedItem().toString().equals("מאמן"))
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
                    if (spinner.getSelectedItem().toString().equals("מתאמן"))
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
                        hashMap.put("Image", "0");

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
                        hashMap.put("Image", "0");

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
                        Toast.makeText(getApplicationContext(), "האיימל הזה כבר רשום", Toast.LENGTH_SHORT).show();

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
                        etPhoneNo.setError("מספר הפלאפון הזה משומש");
                        etPhoneNo.requestFocus();
                    }

            }
            else {
                etPhoneNo.setError("מספר הפלאפון הזה משומש");
                etPhoneNo.requestFocus();
            }


        }
        else{
            Toast.makeText(getBaseContext(),
                    "הכנס את מספר הפלאפון שלך",
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
                    Toast.makeText(this, "אמת את מספר הטלפון שלך",Toast.LENGTH_LONG).show();
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
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    startActivity(LogInIntent);
                    finish();
                    Toast.makeText(SignUpActivity.this, "כדי להירשם אתה צריך לאשר גישה להודעות", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}