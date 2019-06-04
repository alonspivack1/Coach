package coach.coach;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
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
 * Activity just for Users - User profile maker.
 */
public class UserProfileMaker extends AppCompatActivity {

    /**
     * The Etuserage.
     */
    EditText etuserage, /**
     * The Etuserweight.
     */
    etuserweight, /**
     * The Etuserheight.
     */
    etuserheight, /**
     * The Etusertime.
     */
    etusertime, /**
     * The Etuseritem.
     */
    etuseritem, /**
     * The Etuserdescription.
     */
    etuserdescription;
    /**
     * The Cbuserburnfat.
     */
    CheckBox cbuserburnfat, /**
     * The Cbusergym.
     */
    cbusergym, /**
     * The Cbuserstreet.
     */
    cbuserstreet, /**
     * The Cbuserhome.
     */
    cbuserhome, /**
     * The Cbuserdistance.
     */
    cbuserdistance, /**
     * The Cbuserspeed.
     */
    cbuserspeed;
    /**
     * The Reference.
     */
    DatabaseReference reference;
    private DatabaseReference databaseReference;
    /**
     * The Intent.
     */
    Intent intent, /**
     * The Main activity intent.
     */
    MainActivityIntent;
    /**
     * The Username.
     */
    String username, /**
     * The Goal.
     */
    Goal=",", /**
     * The Gender.
     */
    Gender;
    /**
     * The Tvuserbmi.
     */
    TextView tvuserbmi;
    /**
     * The Data snap.
     */
    DataSnapshot dataSnap;
    /**
     * The Switchusergender.
     */
    Switch switchusergender;
    /**
     * The Ivuserimage.
     */
    ImageView ivuserimage;
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
        setContentView(R.layout.activity_user_profile_maker);
        setTitle("עדכון פרופיל");

        intent = getIntent();
        username=intent.getStringExtra("username");

        MainActivityIntent = new Intent(this,MainActivity.class);

        ivuserimage = (ImageView)findViewById(R.id.ivuserimage);

        tvuserbmi = findViewById(R.id.tvuserbmi);

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


        etuserweight.setHint("משקל (קילו)");
        etuserheight.setHint("גובה (סנטימטרים)");

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
                User user;
                user = new User(username,dataSnap.child("ProfileUser").child(username));
                if (!user.getAge().equals("0"))
                {
                    ivuserimage.setImageBitmap(user.getImage());
                    ImageString = BitmapToString(user.getImage());
                    etuserage.setText(user.getAge());
                    etuserweight.setText(user.getWeight());
                    etuserheight.setText(user.getHeight());
                    etusertime.setText(user.getTime());
                    etuseritem.setText(user.getItem());
                    try{
                        float bmi = Float.valueOf(user.getWeight())/((Float.valueOf(user.getHeight())/100)*(Float.valueOf(user.getHeight()))/100);
                        if (bmi<1000) {
                            String health="";
                            if (bmi <= 18.5) {
                                health="(תת משקל)";
                            }
                            if (bmi > 18.5 && bmi <= 25) {
                                health="(משקל תקין)";
                            }
                            if (bmi > 25 && bmi <= 29.9) {
                                health="(משקל עודף)";
                            }
                            if (bmi>29.9&&bmi<=34.9) {
                                health="(השמנה בנונית)";
                            }
                            if (bmi>34.9&&bmi<=39.9) {
                                health="(השמנה חמורה)";
                            }
                            if (bmi>39.9) {
                                health="(השמנה חמורה מאוד)";
                            }
                            tvuserbmi.setText("BMI:" + String.valueOf(bmi)+health);
                        }
                        else {
                            tvuserbmi.setText("");
                        }
                    }
                    catch (Exception e)
                    {
                        tvuserbmi.setText("");
                    }
                    etuserdescription.setText(user.getDescription());
                    if (user.getGender().equals("נקבה"))
                    {
                        switchusergender.setChecked(false);
                    }
                    if (user.getGoal().indexOf("שריפת שומנים")!=-1)
                    {
                        cbuserburnfat.setChecked(true);
                    }
                    if (user.getGoal().indexOf("אימוני כוח בחדר כושר")!=-1)
                    {
                        cbusergym.setChecked(true);
                    }
                    if (user.getGoal().indexOf("אימוני כוח בסטרייט")!=-1)
                    {
                        cbuserstreet.setChecked(true);
                    }
                    if (user.getGoal().indexOf("אימונים בבית")!=-1)
                    {
                        cbuserhome.setChecked(true);
                    }
                    if (user.getGoal().indexOf("שיפור מרחק בריצות")!=-1)
                    {
                        cbuserdistance.setChecked(true);
                    }
                    if (user.getGoal().indexOf("שיפור מהירות בריצות")!=-1)
                    {
                        cbuserspeed.setChecked(true);
                    }

                }

            }
        }, 50);
        etuserheight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    String Weight = etuserweight.getText().toString();
                    String Height = etuserheight.getText().toString();

                    float bmi = Float.valueOf(Weight)/((Float.valueOf(Height)/100)*(Float.valueOf(Height))/100);
                    if (bmi<1000) {
                        String health="";
                        if (bmi <= 18.5) {
                            health="(תת משקל)";
                        }
                        if (bmi > 18.5 && bmi <= 25) {
                            health="(משקל תקין)";
                        }
                        if (bmi > 25 && bmi <= 29.9) {
                            health="(משקל עודף)";
                        }
                        if (bmi>29.9&&bmi<=34.9) {
                            health="(השמנה בנונית)";
                        }
                        if (bmi>34.9&&bmi<=39.9) {
                            health="(השמנה חמורה)";
                        }
                        if (bmi>39.9) {
                            health="(השמנה חמורה מאוד)";
                        }
                        tvuserbmi.setText("BMI:" + String.valueOf(bmi)+health);
                    }
                    else {
                        tvuserbmi.setText("");
                    }
                }
                catch (Exception e)
                {
                    tvuserbmi.setText("");
                }


            }
        });
        etuserweight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    String Weight = etuserweight.getText().toString();
                    String Height = etuserheight.getText().toString();

                    float bmi = Float.valueOf(Weight)/((Float.valueOf(Height)/100)*(Float.valueOf(Height))/100);
                    if (bmi<1000) {
                        String health="";
                        if (bmi <= 18.5) {
                            health="(תת משקל)";
                        }
                        if (bmi > 18.5 && bmi <= 25) {
                            health="(משקל תקין)";
                        }
                        if (bmi > 25 && bmi <= 29.9) {
                            health="(משקל עודף)";
                        }
                        if (bmi>29.9&&bmi<=34.9) {
                            health="(השמנה בנונית)";
                        }
                        if (bmi>34.9&&bmi<=39.9) {
                            health="(השמנה חמורה)";
                        }
                        if (bmi>39.9) {
                            health="(השמנה חמורה מאוד)";
                        }
                        tvuserbmi.setText("BMI:" + String.valueOf(bmi)+health);
                    }
                    else {
                        tvuserbmi.setText("");
                    }
                }
                catch (Exception e)
                {
                    tvuserbmi.setText("");
                }


            }
        });
    }


    /**
     * Update User profile.
     * @param view the view
     */
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
        Goal="";
        if (cbuserburnfat.isChecked())
        {
            Goal+=","+cbuserburnfat.getText().toString();
        }
        if (cbusergym.isChecked())
        {
            Goal+=","+cbusergym.getText().toString();
        }
        if (cbuserstreet.isChecked())
        {
            Goal+=","+cbuserstreet.getText().toString();
        }
        if (cbuserhome.isChecked())
        {
            Goal+=","+cbuserhome.getText().toString();
        }
        if (cbuserdistance.isChecked())
        {
            Goal+=","+cbuserdistance.getText().toString();
        }
        if (cbuserspeed.isChecked())
        {
            Goal+=","+cbuserspeed.getText().toString();
        }
        if(!Pattern.matches("[0-9.]+", etuserage.getText().toString()))
        {
            etuserage.setError("Use just numbers");
            etuserage.requestFocus();
            return;
        }
        if(!Pattern.matches("[0-9.]+.", etuserweight.getText().toString()))
        {
            etuserweight.setError("Use just numbers");
            etuserweight.requestFocus();
            return;
        }
        if(!Pattern.matches("[0-9]+", etuserheight.getText().toString()))
        {
            etuserheight.setError("Use just numbers");
            etuserheight.requestFocus();
            return;
        }
        if(!Pattern.matches("[A-Zא-תa-z0-9!@#$%*(),. ]+", etusertime.getText().toString()))
        {
            etusertime.setError("Just letters, numbers and !@#$%*(),. symbols accepted");
            etusertime.requestFocus();
            return;
        }
        if(!Pattern.matches("[A-Zא-תa-z0-9!@#$%*(),. ]+", etuseritem.getText().toString()))
        {
            etuseritem.setError("Just letters, numbers and !@#$%*(),. symbols accepted");
            etuseritem.requestFocus();
            return;
        }
        if(!Pattern.matches("[A-Zא-תa-z0-9!@#$%*(),. ]+", etuserdescription.getText().toString()))
        {
            etuserdescription.setError("Just letters, numbers and !@#$%*(),. symbols accepted");
            etuserdescription.requestFocus();
            return;
        }

        if (switchusergender.isChecked())
        {
            Gender = "זכר";

        }
        else {
            Gender ="נקבה";
        }

        if (Goal.length()==0)
        {
            Goal=",";
        }

        reference = FirebaseDatabase.getInstance().getReference("ProfileUser").child(username);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Age",etuserage.getText().toString());
        hashMap.put("Gender",Gender );
        hashMap.put("Weight",etuserweight.getText().toString());
        hashMap.put("Height",etuserheight.getText().toString());
        hashMap.put("PracticeTime",etusertime.getText().toString());
        hashMap.put("Goal",Goal);
        hashMap.put("Equipment",etuseritem.getText().toString());
        hashMap.put("Description",etuserdescription.getText().toString());
        hashMap.put("Image",ImageString);
        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    MainActivityIntent.putExtra("username",username);
                    MainActivityIntent.putExtra("type","User");
                    float bmi = Float.valueOf(etuserweight.getText().toString())/((Float.valueOf(etuserheight.getText().toString())/100)*(Float.valueOf(etuserheight.getText().toString()))/100);
                    Toast.makeText(getBaseContext(),"הBMI שלך הוא "+bmi,Toast.LENGTH_LONG).show();
                    startActivity(MainActivityIntent);
                    finish();
                }
            }
        });
    }

    /**
     * Change user profile image.
     *
     * @param view the view
     */
    public void ChangeUserImage(View view) {
        Intent getImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getImageIntent .setType("image/*");
        startActivityForResult(getImageIntent ,  RESULT_LOAD_IMAGE   );
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
            ivuserimage.setImageBitmap(bitmap);

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
