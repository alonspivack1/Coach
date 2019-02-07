package coach.coach;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class UserProfileMaker extends AppCompatActivity {

    EditText etuserage,etuserweight,etuserheight,etusertime,etuseritem,etuserdescription;
    CheckBox cbuserburnfat,cbusergym,cbuserstreet,cbuserhome,cbuserdistance,cbuserspeed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_maker);

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

}
