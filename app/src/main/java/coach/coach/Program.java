package coach.coach;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.github.mthli.knife.KnifeText;

public class Program extends AppCompatActivity {

    private KnifeText knife;
    Intent intent;
    String receiver,sender;
    DatabaseReference databaseReference;
    DatabaseReference Reference=FirebaseDatabase.getInstance().getReference().child("Notifcation");
    Boolean UpdateNotifiction=false;
    String NotificationProgramAlarm="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);
        setTitle("כתיבת תוכנית");
        intent = getIntent();
        receiver=intent.getStringExtra("receiver");
        sender=intent.getStringExtra("sender");
        Reference = Reference.child(receiver);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("ProgramRoom").child(receiver+"&"+sender);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("KnifeText",dataSnapshot.child("Data").getValue().toString());
                // knife.fromHtml(dataSnapshot.child("Data").getValue().toString());
                knife.fromHtml(dataSnapshot.child("Data").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.v("NotificationProgram","1");
                if (dataSnapshot.hasChild("ProgramAlarm"))
                {
                    Log.v("NotificationProgram","2");

                    NotificationProgramAlarm=dataSnapshot.child("ProgramAlarm").getValue().toString();
                    if ((NotificationProgramAlarm).indexOf(sender+",")!=-1)
                    {
                        Log.v("NotificationProgram","3");

                        if (((NotificationProgramAlarm).indexOf(","+sender+",")!=-1)||((NotificationProgramAlarm).indexOf(sender+",")==0))
                        {
                            Log.v("NotificationProgram","4");

                            UpdateNotifiction=false;

                        }
                        else{
                            Log.v("NotificationProgram","5");

                            UpdateNotifiction=true;

                        }
                    }
                    else
                    {
                        Log.v("NotificationProgram","6");

                        UpdateNotifiction=true;

                    }
                }
                else {
                    Log.v("NotificationProgram","7");

                    UpdateNotifiction=true;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        knife = (KnifeText) findViewById(R.id.knife);
        knife.setSelection(knife.getEditableText().length());

        setupBold();
        setupItalic();
        setupUnderline();
        setupStrikethrough();
        setupBullet();
        setupQuote();
        setUpundo();
        setUpredo();
    }

    private void setupBold() {
        ImageButton bold = (ImageButton) findViewById(R.id.bold);

        bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.bold(!knife.contains(KnifeText.FORMAT_BOLD));
            }
        });

        bold.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(Program.this, R.string.toast_bold, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupItalic() {
        ImageButton italic = (ImageButton) findViewById(R.id.italic);

        italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.italic(!knife.contains(KnifeText.FORMAT_ITALIC));
            }
        });

        italic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(Program.this, R.string.toast_italic, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupUnderline() {
        ImageButton underline = (ImageButton) findViewById(R.id.underline);

        underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.underline(!knife.contains(KnifeText.FORMAT_UNDERLINED));
            }
        });

        underline.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(Program.this, R.string.toast_underline, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupStrikethrough() {
        ImageButton strikethrough = (ImageButton) findViewById(R.id.strikethrough);

        strikethrough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.strikethrough(!knife.contains(KnifeText.FORMAT_STRIKETHROUGH));
            }
        });

        strikethrough.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(Program.this, R.string.toast_strikethrough, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupBullet() {
        ImageButton bullet = (ImageButton) findViewById(R.id.bullet);

        bullet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.bullet(!knife.contains(KnifeText.FORMAT_BULLET));
            }
        });


        bullet.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(Program.this, R.string.toast_bullet, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupQuote() {
        ImageButton quote = (ImageButton) findViewById(R.id.quote);

        quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.quote(!knife.contains(KnifeText.FORMAT_QUOTE));
            }
        });

        quote.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(Program.this, R.string.toast_quote, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
    private void setUpundo() {
        ImageButton undo = (ImageButton) findViewById(R.id.undo);

        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.undo();
            }
        });

        undo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(Program.this, R.string.toast_undo, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setUpredo() {
        ImageButton redo = (ImageButton) findViewById(R.id.redo);

        redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.redo();
            }
        });

        redo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(Program.this, R.string.toast_redo, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }




    public void SendProgram(View view) {
        databaseReference.child("Data").setValue(knife.toHtml());
        knife.fromHtml(knife.toHtml());
        if (UpdateNotifiction)
        {
            Reference.child("ProgramAlarm").setValue(NotificationProgramAlarm+sender+",");
        }
    }
}