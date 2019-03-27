package coach.coach;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Settings extends AppCompatActivity {

    CheckBox cbsettingsphone,cbsettingsservice;
    boolean service=true,phone=false;
    String Phone="";
    DatabaseReference databasePhone;
    String type,username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("הגדרות");
        cbsettingsphone=(CheckBox)findViewById(R.id.cbsettingsphone);
        cbsettingsservice=(CheckBox)findViewById(R.id.cbsettingsservice);
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        service = prefs.getBoolean("service",true);
        phone = prefs.getBoolean("phone",false);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        type=intent.getStringExtra("type");

        if (type.equals("User"))
        {
            databasePhone = FirebaseDatabase.getInstance().getReference().child("UserNames").child(username);
        }
        else
        {
            databasePhone = FirebaseDatabase.getInstance().getReference().child("CoachNames").child(username);
        }

        databasePhone.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Phone = dataSnapshot.getValue().toString();
                Log.e("PHONE",Phone);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (service)
        {
            cbsettingsservice.setChecked(true);
        }
        if (phone)
        {
            cbsettingsphone.setChecked(true);
        }

    }

    public void SaveSettings(View view) {
            if (cbsettingsservice.isChecked())
            {
                service=true;

            }
            else
            {
                service=false;
            }
            if (cbsettingsphone.isChecked())
            {
                if (Phone.charAt(0)!='=')
                {
                    Log.e("PHONEB",Phone);
                    Phone = "="+Phone;
                    Log.e("PHONEA",Phone);

                    databasePhone.setValue(Phone);
                }
                phone=true;
            }
            else {
                if (Phone.charAt(0)=='=')
                {
                    Log.e("PHONEB",Phone);
                    Phone=Phone.substring(1,Phone.length());
                    Log.e("PHONEA",Phone);
                    databasePhone.setValue(Phone);
                }
                phone=false;
            }
            SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
            editor.putBoolean("service",service);
            editor.putBoolean("phone", phone);
            editor.apply();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
