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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UserProfileMaker extends AppCompatActivity {

    EditText etuserage,etuserweight,etuserheight,etusertime,etuseritem,etuserdescription;
    CheckBox cbuserburnfat,cbusergym,cbuserstreet,cbuserhome,cbuserdistance,cbuserspeed;
    DatabaseReference reference;
    private DatabaseReference databaseReference;
    Intent intent;
    String username,Goal=",";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_maker);

        intent = getIntent();
        username=intent.getStringExtra("username");

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
        reference = FirebaseDatabase.getInstance().getReference("ProfileUser").child(username);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Age",etuserage.getText().toString());
        hashMap.put("Weight",etuserweight.getText().toString());
        hashMap.put("Height",etuserheight.getText().toString());
        hashMap.put("PracticeTime",etusertime.getText().toString());
        hashMap.put("Goal",Goal);
        hashMap.put("Equipment",etuseritem.getText().toString());
        hashMap.put("Description",etuserdescription.getText().toString());
        reference.setValue(hashMap);
    }
}
