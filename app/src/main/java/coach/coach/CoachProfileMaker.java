package coach.coach;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.regex.Pattern;

/**
 * Activity just for Coaches - Coach profile maker.
 */
public class CoachProfileMaker extends AppCompatActivity {

    /**
     * The Etcoachage.
     */
    EditText etcoachage, /**
     * The Etcoachwhere.
     */
    etcoachwhere, /**
     * The Etcoachtime.
     */
    etcoachtime, /**
     * The Etcoachdescription.
     */
    etcoachdescription;
    /**
     * The Cbcoachburnfat.
     */
    CheckBox cbcoachburnfat, /**
     * The Cbcoachgym.
     */
    cbcoachgym, /**
     * The Cbcoachstreet.
     */
    cbcoachstreet, /**
     * The Cbcoachhome.
     */
    cbcoachhome, /**
     * The Cbcoachdistance.
     */
    cbcoachdistance, /**
     * The Cbcoachspeed.
     */
    cbcoachspeed;
    /**
     * The Intent.
     */
    Intent intent, /**
     * The Main activity intent.
     */
    MainActivityIntent;
    /**
     * The Reference.
     */
    DatabaseReference reference;
    /**
     * The Username.
     */
    String username, /**
     * The Professionalization.
     */
    Professionalization=",", /**
     * The Gender.
     */
    Gender;
    /**
     * The Data snap.
     */
    DataSnapshot dataSnap;
    /**
     * The Switchcoachgender.
     */
    Switch switchcoachgender;
    private DatabaseReference databaseReference;
    /**
     * The Ivcoachimage.
     */
    ImageView ivcoachimage;
    private static int RESULT_LOAD_IMAGE = 1;
    /**
     * The Image string.
     */
    String ImageString="0";
    /**
     * The Bitmap.
     */
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
                coach = new Coach(username,dataSnap.child("ProfileCoach").child(username),dataSnap.child("Rating").child(username));;
                if (!coach.getAge().equals("0"))
                {
                    ivcoachimage.setImageBitmap(coach.getImage());
                    ImageString = BitmapToString(coach.getImage());
                    etcoachage.setText(coach.getAge());
                    etcoachwhere.setText(coach.getWhere());
                    etcoachtime.setText(coach.getTime());
                    etcoachdescription.setText(coach.getDescription());
                    if (coach.getGender().equals("נקבה"))
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
        }, 50);
    }

    /**
     * Update Coach profile.
     * @param view the view
     */
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
            Gender ="זכר";

        }
        else {
            Gender ="נקבה";
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

    /**
     * Change coach profile image.
     *
     * @param view the view
     */
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
            ivcoachimage.setImageBitmap(bitmap);

        }
    }

    /**
     * Gets resized bitmap.
     *
     * @param image   the image
     * @param maxSize the max size
     * @return the resized bitmap
     */
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

    /**
     * Bitmap to string.
     *
     * @param bitmap image
     * @return the string value of the image
     */
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
