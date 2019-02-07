package coach.coach;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

public class CoachProfileMaker extends AppCompatActivity {

    EditText etcoachage,etcoachwhere,etcoachtime,etcoachdescription;
    CheckBox cbcoachburnfat,cbcoachgym,cbcoachstreet,cbcoachhome,cbcoachdistance,cbcoachspeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_profile_maker);

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
}
