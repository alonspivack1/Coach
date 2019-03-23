package coach.coach;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

public class CoachProfileMaker extends AppCompatActivity {

    EditText etcoachage,etcoachwhere,etcoachtime,etcoachdescription;
    CheckBox cbcoachburnfat,cbcoachgym,cbcoachstreet,cbcoachhome,cbcoachdistance,cbcoachspeed;
    Intent intent,MainActivityIntent;
    DatabaseReference reference;
    String username,Professionalization=",",Gender;
    DataSnapshot dataSnap;
    Switch switchcoachgender;
    private DatabaseReference databaseReference;
    ImageView ivcoachimage;
    private static int RESULT_LOAD_IMAGE = 1;
    String ImageString="0";
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_profile_maker);
        setTitle("עדכון פרופיל");

        intent = getIntent();
        username=intent.getStringExtra("username");

        ivcoachimage = (ImageView)findViewById(R.id.ivcoachimage);


        MainActivityIntent = new Intent(this,MainActivity.class);


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

        switchcoachgender = findViewById(R.id.switchcoachgender);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               dataSnap = dataSnapshot;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Coach coach;
                coach = new Coach(username,dataSnap.child("ProfileCoach").child(username));;
                if (!coach.getAge().equals("0"))
                {
                    ivcoachimage.setImageBitmap(coach.getImage());
                    etcoachage.setText(coach.getAge());
                    etcoachwhere.setText(coach.getWhere());
                    etcoachtime.setText(coach.getTime());
                    etcoachdescription.setText(coach.getDescription());
                    if (coach.getGender().equals("Female"))
                    {
                        switchcoachgender.setChecked(false);
                    }
                    if (coach.getProfessionalization().indexOf("שריפת שומנים")!=-1)
                    {
                        cbcoachburnfat.setChecked(true);
                    }
                    if (coach.getProfessionalization().indexOf("אימוני כוח בחדר כושר")!=-1)
                    {
                        cbcoachgym.setChecked(true);
                    }
                    if (coach.getProfessionalization().indexOf("אימוני כוח בסטרייט")!=-1)
                    {
                        cbcoachstreet.setChecked(true);
                    }
                    if (coach.getProfessionalization().indexOf("אימונים בבית")!=-1)
                    {
                        cbcoachhome.setChecked(true);
                    }
                    if (coach.getProfessionalization().indexOf("שיפור מרחק בריצות")!=-1)
                    {
                        cbcoachdistance.setChecked(true);
                    }
                    if (coach.getProfessionalization().indexOf("שיפור מהירות בריצות")!=-1)
                    {
                        cbcoachspeed.setChecked(true);
                    }

                }


            }
        }, 1000);
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

        Professionalization="";
        if (cbcoachburnfat.isChecked())
        {
            Professionalization+=","+cbcoachburnfat.getText().toString();
        }
        if (cbcoachgym.isChecked())
        {
            Professionalization+=","+cbcoachgym.getText().toString();
        }
        if (cbcoachstreet.isChecked())
        {
            Professionalization+=","+cbcoachstreet.getText().toString();
        }
        if (cbcoachhome.isChecked())
        {
            Professionalization+=","+cbcoachhome.getText().toString();
        }
        if (cbcoachdistance.isChecked())
        {
            Professionalization+=","+cbcoachdistance.getText().toString();
        }
        if (cbcoachspeed.isChecked())
        {
            Professionalization+=","+cbcoachspeed.getText().toString();
        }

        if(!Pattern.matches("[0-9.]+", etcoachage.getText().toString()))
        {
            etcoachage.setError("Use just numbers");
            etcoachage.requestFocus();
            return;
        }

        if(!Pattern.matches("[A-Zא-תa-z0-9!@#$%*(),. ]+", etcoachwhere.getText().toString()))
        {
            etcoachwhere.setError("Just letters, numbers and !@#$%*(),. symbols accepted");
            etcoachwhere.requestFocus();
            return;
        }

        if(!Pattern.matches("[A-Zא-תa-z0-9!@#$%*(),. ]+", etcoachtime.getText().toString()))
        {
            etcoachtime.setError("Just letters, numbers and !@#$%*(),. symbols accepted");
            etcoachtime.requestFocus();
            return;
        }


        if(!Pattern.matches("[A-Zא-תa-z0-9!@#$%*(),. ]+", etcoachdescription.getText().toString()))
        {
            etcoachdescription.setError("Just letters, numbers and !@#$%*(),. symbols accepted");
            etcoachdescription.requestFocus();
            return;
        }

        if (switchcoachgender.isChecked())
        {
            Gender = "Male";

        }
        else {
            Gender ="Female";
        }
        if (Professionalization.length()==0)
        {
            Professionalization=",";
        }



        reference = FirebaseDatabase.getInstance().getReference("ProfileCoach").child(username);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Age",etcoachage.getText().toString());
        hashMap.put("Gender",Gender );
        hashMap.put("Professionalization",Professionalization);
        hashMap.put("StudyPlace",etcoachwhere.getText().toString());
        hashMap.put("Description",etcoachdescription.getText().toString());
        hashMap.put("CoachTime",etcoachtime.getText().toString());
        hashMap.put("Image",ImageString);
        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    MainActivityIntent.putExtra("username",username);
                    MainActivityIntent.putExtra("type","Coach");
                    startActivity(MainActivityIntent);
                    finish();
                }
            }
        });
    }
    public void ChangeCoachImage(View view) {
        Intent getImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getImageIntent .setType("image/*");
        startActivityForResult(getImageIntent ,  RESULT_LOAD_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode== RESULT_LOAD_IMAGE  && resultCode == RESULT_OK) {
            Uri fullPhotoUri = data.getData();
            try {
              bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fullPhotoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bitmap=getResizedBitmap(bitmap,200);
            ImageString = BitmapToString(bitmap);
            Log.e("Bitmap", ImageString);
            ivcoachimage.setImageBitmap(bitmap);

        }
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    public static String BitmapToString(Bitmap bitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String temp = Base64.encodeToString(b, Base64.DEFAULT);
            return temp;
        } catch (NullPointerException e) {
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

}
