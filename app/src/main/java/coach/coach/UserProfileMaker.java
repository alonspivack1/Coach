package coach.coach;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class UserProfileMaker extends AppCompatActivity {

    EditText etuserage,etuserweight,etuserheight,etusertime,etuseritem,etuserdescription;
    CheckBox cbuserburnfat,cbusergym,cbuserstreet,cbuserhome,cbuserdistance,cbuserspeed;
    DatabaseReference reference;
    private DatabaseReference databaseReference;
    Intent intent,MainActivityIntent;
    String username,Goal=",",Gender;
    Switch switchusergender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_maker);

        intent = getIntent();
        username=intent.getStringExtra("username");

        MainActivityIntent = new Intent(this,MainActivity.class);

        etuserage = findViewById(R.id.etuserage);
        etuserweight = findViewById(R.id.etuserweight);
        etuserheight = findViewById(R.id.etuserheight);
        etusertime = findViewById(R.id.etusertime);
        etuseritem = findViewById(R.id.etuseritem);
        etuserdescription = findViewById(R.id.etuserdescription);

        cbuserburnfat = findViewById(R.id.cbuserburnfat);
        cbusergym = findViewById(R.id.cbusergym);
        cbuserstreet = findViewById(R.id.cbuserstreet);
        cbuserhome = findViewById(R.id.cbuserhome);
        cbuserdistance = findViewById(R.id.cbuserdistance);
        cbuserspeed = findViewById(R.id.cbuserspeed);

        switchusergender = findViewById(R.id.switchusergender);
        }

    public void UserSend(View view) {
        if (etuserage.getText().toString().length()==0||etuserage.getText().toString().equals("0"))
        {
            etuserage.setError("Fill your age");
            etuserage.requestFocus();
            return;
        }
        if (etuserweight.getText().toString().length()==0)
        {
            etuserweight.setError("Fill your weight");
            etuserweight.requestFocus();
            return;
        }
        if (etuserheight.getText().toString().length()==0)
        {
            etuserheight.setError("Fill your height");
            etuserheight.requestFocus();
            return;
        }
        if (etusertime.getText().toString().length()==0)
        {
            etusertime.setError("Fill how long have you been practicing?");
            etusertime.requestFocus();
            return;
        }
        if (etuseritem.getText().toString().length()==0)
        {
            etuseritem.setError("Fill your items");
            etuseritem.requestFocus();
            return;
        }

        if (etuserdescription.getText().toString().length()==0)
        {
            etuserdescription.setError("Fill short description");
            etuserdescription.requestFocus();
            return;
        }
        Goal=",";
        if (cbuserburnfat.isChecked())
        {
            Goal+=""+cbuserburnfat.getText().toString()+",";
        }
        if (cbusergym.isChecked())
        {
            Goal+=""+cbusergym.getText().toString()+",";
        }
        if (cbuserstreet.isChecked())
        {
            Goal+=""+cbuserstreet.getText().toString()+",";
        }
        if (cbuserhome.isChecked())
        {
            Goal+=""+cbuserhome.getText().toString()+",";
        }
        if (cbuserdistance.isChecked())
        {
            Goal+=""+cbuserdistance.getText().toString()+",";
        }
        if (cbuserspeed.isChecked())
        {
            Goal+=""+cbuserspeed.getText().toString()+",";
        }
        if(!Pattern.matches("[0-9.]+", etuserage.getText().toString()))
        {
            etuserage.setError("Use just numbers");
            etuserage.requestFocus();
            return;
        }
        if(!Pattern.matches("[0-9.]+.", etuserweight.getText().toString()))
        {
            etuserage.setError("Use just numbers");
            etuserage.requestFocus();
            return;
        }
        if(!Pattern.matches("[0-9]+", etuserheight.getText().toString()))
        {
            etuserage.setError("Use just numbers");
            etuserage.requestFocus();
            return;
        }
        if(!Pattern.matches("[A-Za-z0-9!@#$%*(),. ]+", etusertime.getText().toString()))
        {
            etusertime.setError("Just letters, numbers and !@#$%*(),. symbols accepted");
            etusertime.requestFocus();
            return;
        }
        if(!Pattern.matches("[A-Za-z0-9!@#$%*(),. ]+", etuseritem.getText().toString()))
        {
            etuseritem.setError("Just letters, numbers and !@#$%*(),. symbols accepted");
            etuseritem.requestFocus();
            return;
        }
        if(!Pattern.matches("[A-Za-z0-9!@#$%*(),. ]+", etuserdescription.getText().toString()))
        {
            etuserdescription.setError("Just letters, numbers and !@#$%*(),. symbols accepted");
            etuserdescription.requestFocus();
            return;
        }

        if (switchusergender.isChecked())
        {
            Gender = "Male";

        }
        else {
            Gender ="Female";
        }

        reference = FirebaseDatabase.getInstance().getReference("ProfileUser").child(username);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("{Age}",etuserage.getText().toString());
        hashMap.put("{Gender}",Gender );
        hashMap.put("{Weight}",etuserweight.getText().toString());
        hashMap.put("{Height}",etuserheight.getText().toString());
        hashMap.put("{PracticeTime}",etusertime.getText().toString());
        hashMap.put("{Goal}",Goal);
        hashMap.put("{Equipment}",etuseritem.getText().toString());
        hashMap.put("{Description}",etuserdescription.getText().toString());
        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    MainActivityIntent.putExtra("username",username);
                    MainActivityIntent.putExtra("type","User");
                    startActivity(MainActivityIntent);
                    finish();
                }
            }
        });
    }
}
