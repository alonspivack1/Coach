package coach.coach;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.github.mthli.knife.KnifeText;

public class ViewProgram extends AppCompatActivity {
    Intent intent;
    String receiver,sender;
    DatabaseReference databaseReference;
    private KnifeText knife;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_program);
        intent = getIntent();
        receiver=intent.getStringExtra("receiver");
        sender=intent.getStringExtra("sender");
        knife = (KnifeText) findViewById(R.id.knife);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("ProgramRoom").child(receiver+"&"+sender);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("KnifeText",dataSnapshot.child("Data").getValue().toString());
               // knife.fromHtml(dataSnapshot.child("Data").getValue().toString());
                knife.fromHtml(dataSnapshot.child("Data").getValue().toString());
                knife.setActivated(false);
                knife.setFocusable(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
