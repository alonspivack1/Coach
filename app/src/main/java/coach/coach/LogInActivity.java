package coach.coach;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LogInActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    String Answer,x="bb";
    int pl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Map<String, Object> objectMap = (HashMap<String, Object>) dataSnapshot.child("Users").child(user.getUid()).child("username").getValue();
                Map<String, Object> objectMap = (HashMap<String, Object>) dataSnapshot.child("Names").getValue();
                Answer = objectMap.toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


       /* mDatabase = FirebaseDatabase.getInstance().getReference();
        String username= "cc" ;
        mDatabase.child("Names").equalTo(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null && dataSnapshot.getChildren()!=null &&
                        dataSnapshot.getChildren().iterator().hasNext()){
                    Log.e("exists","exists");
                    //Username exists
                }
                else {
                    //Username does not exist
                    Log.e("not exist","not exist");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });*/

       /* String myString = "Aa1 90";
        if(Pattern.matches("[A-Za-z0-9]+", myString))
        {
            Toast.makeText(this,"string has only alphabets",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this,"It has other characters",Toast.LENGTH_LONG).show();
        }*/

                /*Intent intent = new Intent (this, SignUpActivity.class);
                 startActivity(intent);*/
    }

    public void AAAAAA(View view) {
        //Toast.makeText(this,Answer,Toast.LENGTH_LONG).show();

        pl =Answer.indexOf("=, "+x);
        if(pl==-1)
        {
            Toast.makeText(this,Answer+"Can be username"+pl,Toast.LENGTH_LONG).show();
            Log.e("keys",Answer);
        }
        else
        {
            Toast.makeText(this,Answer+"Cant be username"+pl,Toast.LENGTH_LONG).show();
            Log.e("keys",Answer);

        }


    }
}
