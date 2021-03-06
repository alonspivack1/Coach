package coach.coach;



import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.gsm.SmsManager;
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
import android.widget.Switch;
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

/**
 * The Sign up activity.
 */
public class  SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * The Progress bar.
     */
    ProgressBar progressBar;
    /**
     * The Edit text email.
     */
    EditText editTextEmail, /**
     * The Edit text password.
     */
    editTextPassword, /**
     * The Edit text user.
     */
    editTextUser, /**
     * The Et phone no.
     */
    etPhoneNo;
    /**
     * The Sm sbtn.
     */
    Button SMSbtn;
    /**
     * The Reference.
     */
    DatabaseReference reference, /**
     * The Reference 2.
     */
    reference2, /**
     * The Reference 3.
     */
    reference3;
    /**
     * The Numflag 1.
     */
    int numflag1, /**
     * The Numflag 2.
     */
    numflag2, /**
     * The Numflag 3.
     */
    numflag3, /**
     * The Numflag 4.
     */
    numflag4;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference,databaseReference2;
    /**
     * The Coach answer.
     */
    String CoachAnswer="", /**
     * The User answer.
     */
    UserAnswer="";
    /**
     * The Rn.
     */
    Random rn;
    /**
     * The Log in intent.
     */
    Intent LogInIntent, /**
     * The User intent.
     */
    UserIntent, /**
     * The Coach intent.
     */
    CoachIntent, /**
     * The Intent credits.
     */
    intentCredits;
    /**
     * The Code int.
     */
    int codeInt;
    /**
     * The Sms num.
     */
    String smsNum;
    /**
     * The Code string.
     */
    String codeString="";

    /**
     * The Codeet.
     */
    EditText codeet;
    /**
     * The Username.
     */
    String username, /**
     * The Password.
     */
    password, /**
     * The Email.
     */
    email;
    Switch switchsignup;
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
        switchsignup = (Switch)findViewById(R.id.switchsignup);
        UserIntent = new Intent(this,UserProfileMaker.class);
        CoachIntent = new Intent(this,CoachProfileMaker.class);





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
                    Toast.makeText(getBaseContext(),"הקוד לא נכון",Toast.LENGTH_LONG).show();

                }
            }

        });

        new Dialog(getApplicationContext());
        alertDialog.show();
    }

    /**
     * Start sign up.
     */
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
                    if (switchsignup.isChecked())
                    {
                        reference2 = FirebaseDatabase.getInstance().getReference("UserNames");
                        reference2.child(username).setValue(smsNum+","+userid+","+username+",");

                    }
                    else
                    {
                        reference2 = FirebaseDatabase.getInstance().getReference("CoachNames");
                        reference2.child(username).setValue(smsNum+","+userid+","+username+",");
                    }


                    if (switchsignup.isChecked())
                    {
                        reference = FirebaseDatabase.getInstance().getReference("ProfileUser").child(username);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("Age","0" );
                        hashMap.put("Weight","0" );
                        hashMap.put("Height","0" );
                        hashMap.put("PracticeTime", "0");
                        hashMap.put("Goal",",");
                        hashMap.put("Equipment", "0");
                        hashMap.put("Gender","זכר" );
                        hashMap.put("Description", "0");
                        hashMap.put("Image", "0");

                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                        SharedPreferences.Editor editor = getSharedPreferences("AutoLogIn", MODE_PRIVATE).edit();
                                        editor.putString("username", username);
                                        editor.putString("type", "User");
                                        editor.putString("email", email);
                                        editor.putString("password", password);
                                        editor.apply();
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
                        hashMap.put("Gender","זכר" );
                        hashMap.put("CoachTime", "0");
                        hashMap.put("Image", "0");

                        reference3=FirebaseDatabase.getInstance().getReference("Rating").child(username);
                        HashMap<String, String> hashMap2 = new HashMap<>();
                        hashMap2.put("RatersNumber",""+0 );
                        hashMap2.put("Rating", ""+0);
                        reference3.setValue(hashMap2);
                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    SharedPreferences.Editor editor = getSharedPreferences("AutoLogIn", MODE_PRIVATE).edit();
                                    editor.putString("username", username);
                                    editor.putString("type", "Coach");
                                    editor.putString("email", email);
                                    editor.putString("password", password);
                                    editor.apply();
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

    /**
     * Send code to the phone - to verify the phone
     *
     * @param view the view
     */
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
                break;

            case R.id.textViewLogin:
                startActivity(LogInIntent);
                finish();
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {


                    startActivity(LogInIntent);
                    finish();
                    Toast.makeText(SignUpActivity.this, "כדי להירשם אתה צריך לאשר גישה להודעות", Toast.LENGTH_LONG).show();
                }
                return;
            }


        }
    }
    @Override
    public void onBackPressed() {
        finish();
        Intent intentlogin = new Intent(this,LogInActivity.class);
        startActivity(intentlogin);
    }

}