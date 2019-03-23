package coach.coach;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
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
    TextView tvuserbmi;
    DataSnapshot dataSnap;
    Switch switchusergender;
    ImageView ivuserimage;
    private static int RESULT_LOAD_IMAGE = 1;
    String ImageString="0";
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
                    etuserage.setText(user.getAge());
                    etuserweight.setText(user.getWeight());
                    etuserheight.setText(user.getHeight());
                    etusertime.setText(user.getTime());
                    etuseritem.setText(user.getItem());
                    double bmi = Double.valueOf(user.getWeight())/((Double.valueOf(user.getHeight())/100)*(Double.valueOf(user.getHeight()))/100);
                    tvuserbmi.setText("BMI:"+String.valueOf(bmi));
                    etuserdescription.setText(user.getDescription());
                    if (user.getGender().equals("Female"))
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
        }, 1000);
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
            Gender = "Male";

        }
        else {
            Gender ="Female";
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
                    startActivity(MainActivityIntent);
                    finish();
                }
            }
        });
    }

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
            Log.e("Bitmap", ImageString);
            ivuserimage.setImageBitmap(bitmap);

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
