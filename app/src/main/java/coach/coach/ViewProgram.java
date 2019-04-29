package coach.coach;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.Toast;

import com.chinalwb.are.render.AreTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Activity just for Users - View on the training program.
 */
public class ViewProgram extends AppCompatActivity {
    /**
     * The Intent.
     */
    Intent intent;
    /**
     * The Receiver.
     */
    String receiver, /**
     * The Sender.
     */
    sender;
    /**
     * The Database reference.
     */
    DatabaseReference databaseReference, /**
     * The Database reference 2.
     */
    databaseReference2;
    /**
     * The Rb view program.
     */
    RatingBar rbViewProgram;
    /**
     * The Rated in past.
     */
    Boolean RatedInPast=false;
    /**
     * The Raters number.
     */
    int RatersNumber=0;
    /**
     * The Rating.
     */
    float Rating=0;
    /**
     * The Rate.
     */
    float Rate=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_program);
        rbViewProgram = (RatingBar)findViewById(R.id.rbViewProgram);
        setTitle("תוכנית אישית מהמאמן");
        final AreTextView areTextView = findViewById(R.id.areTextView);
        areTextView.setTextColor(getResources().getColor(R.color.colorTextARE));
        areTextView.setBackgroundResource(R.drawable.backgroundman);
        intent = getIntent();
        receiver=intent.getStringExtra("receiver");
        sender=intent.getStringExtra("sender");
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Rating").child(sender);
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (!RatedInPast)
                {if (dataSnapshot.hasChild("RatersNames"))
                {
                    Rating = Float.valueOf(dataSnapshot.child("Rating").getValue().toString());
                    if (dataSnapshot.child("RatersNames").hasChild(receiver))
                    {
                        RatedInPast=true;
                        Rate=Float.valueOf(dataSnapshot.child("RatersNames").child(receiver).getValue().toString());
                        rbViewProgram.setRating(Rate);
                    }
                    else {
                        RatersNumber=Integer.parseInt(dataSnapshot.child("RatersNumber").getValue().toString());
                    }
                }}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        rbViewProgram.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                ConnectivityManager connectivityManager;
                connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    if (!RatedInPast)
                    {
                        RatedInPast=true;
                        databaseReference2.child("RatersNumber").setValue(RatersNumber+1);

                    }
                    databaseReference2.child("RatersNames").child(receiver).setValue(rating);
                    Rating = Rating+rating-Rate;
                    Rate=rating;
                    databaseReference2.child("Rating").setValue(Rating);
                }
                else {
                    Toast.makeText(getBaseContext(),"אין חיבור לאינטרנט, הדירוג לא בוצע",Toast.LENGTH_LONG).show();
                }


            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("ProgramRoom").child(receiver+"&"+sender);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("Data")){
                Log.e("Text",dataSnapshot.child("Data").getValue().toString());
                try {
                    areTextView.fromHtml(dataSnapshot.child("Data").getValue().toString());

                }
                catch (Exception e)
                {}

                    SharedPreferences.Editor editor = getSharedPreferences("Programs", MODE_PRIVATE).edit();
                    editor.putString(receiver+"&"+sender,dataSnapshot.child("Data").getValue().toString());
                    editor.apply();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
