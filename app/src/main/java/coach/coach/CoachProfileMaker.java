package coach.coach;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CoachProfileMaker extends AppCompatActivity {

    EditText etcoachage,etcoachwhere,etcoachtime,etcoachdescription;
    CheckBox cbcoachburnfat,cbcoachgym,cbcoachstreet,cbcoachhome,cbcoachdistance,cbcoachspeed;
    Intent intent;
    DatabaseReference reference;
    String username,Professionalization=",";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_profile_maker);

        intent = getIntent();
        username=intent.getStringExtra("username");

        etcoachage = findViewById(R.id.etcoachage);
        etcoachwhere = findViewById(R.id.etcoachwhere);
        etcoachtime = findViewById(R.id.etcoachtime);
        etcoachdescription = findViewById(R.id.etcoachdescription);

        cbcoachburnfat = findViewById(R.id.cbcoachburnfat);
        cbcoachgym = findViewById(R.id.cbcoachgym);
        cbcoachstreet = findViewById(R.id.cbcoachstreet);
        cbcoachhome = findViewById(R.id.cbcoachhome);
        cbcoachdistance = findViewById(R.id.cbcoachdistance);
        cbcoachspeed = findViewById(R.id.cbcoachspeed);
    }

    public void CoachSend(View view) {
        if (etcoachage.getText().toString().length()==0||etcoachage.getText().toString().equals("0"))
        {
            etcoachage.setError("Fill your age");
            etcoachage.requestFocus();
            return;
        }
        if (etcoachwhere.getText().toString().length()==0)
        {
            etcoachwhere.setError("Fill where did you learn");
            etcoachwhere.requestFocus();
            return;
        }
        if (etcoachtime.getText().toString().length()==0)
        {
            etcoachtime.setError("Fill how long have you been a coach?");
            etcoachtime.requestFocus();
            return;
        }
        if (etcoachdescription.getText().toString().length()==0)
        {
            etcoachdescription.setError("Fill short description");
            etcoachdescription.requestFocus();
            return;
        }

        Professionalization=",";
        if (cbcoachburnfat.isChecked())
        {
            Professionalization+=""+cbcoachburnfat.getText().toString()+",";
        }
        if (cbcoachgym.isChecked())
        {
            Professionalization+=""+cbcoachgym.getText().toString()+",";
        }
        if (cbcoachstreet.isChecked())
        {
            Professionalization+=""+cbcoachstreet.getText().toString()+",";
        }
        if (cbcoachhome.isChecked())
        {
            Professionalization+=""+cbcoachhome.getText().toString()+",";
        }
        if (cbcoachdistance.isChecked())
        {
            Professionalization+=""+cbcoachdistance.getText().toString()+",";
        }
        if (cbcoachspeed.isChecked())
        {
            Professionalization+=""+cbcoachspeed.getText().toString()+",";
        }

        reference = FirebaseDatabase.getInstance().getReference("ProfileCoach").child(username);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Age",etcoachage.getText().toString());
        hashMap.put("Professionalization",Professionalization);
        hashMap.put("StudyPlace",etcoachwhere.getText().toString());
        hashMap.put("Description",etcoachdescription.getText().toString());
        hashMap.put("CoachTime",etcoachtime.getText().toString());
        reference.setValue(hashMap);
    }
}
