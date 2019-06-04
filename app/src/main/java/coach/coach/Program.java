package coach.coach;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.chinalwb.are.AREditText;
import com.chinalwb.are.styles.toolbar.IARE_Toolbar;
import android.support.annotation.ColorInt;
import android.text.style.ForegroundColorSpan;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentCenter;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentLeft;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentRight;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Bold;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_FontSize;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Hr;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Italic;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Link;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Quote;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Strikethrough;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Subscript;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Superscript;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Underline;
import com.chinalwb.are.styles.toolitems.IARE_ToolItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Activity just for Coaches - create training program to user.
 */
public class Program extends AppCompatActivity {
    private IARE_Toolbar mToolbar;

    private AREditText mEditText;
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
    DatabaseReference databaseReference;
    /**
     * The Reference.
     */
    DatabaseReference Reference=FirebaseDatabase.getInstance().getReference().child("Notification");
    /**
     * The Update notifiction.
     */
    Boolean UpdateNotifiction=false;
    /**
     * The Notification program alarm.
     */
    String NotificationProgramAlarm="";
    /**
     * The Double back to exit pressed once.
     */
    boolean doubleBackToExitPressedOnce = false;
    /**
     * The Auto save.
     */
    boolean AutoSave=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);
        setTitle("כתיבת תוכנית");
        mEditText = this.findViewById(R.id.arEditText);

        intent = getIntent();
        receiver=intent.getStringExtra("receiver");
        sender=intent.getStringExtra("sender");
        Reference = Reference.child(receiver);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("ProgramRoom").child(receiver+"&"+sender);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    Log.e("Text",dataSnapshot.child("Data").getValue().toString());
                    mEditText.fromHtml(dataSnapshot.child("Data").getValue().toString());

                }
                catch (Exception e)
                {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("ProgramAlarm"))
                {
                    NotificationProgramAlarm=dataSnapshot.child("ProgramAlarm").getValue().toString();
                    if ((NotificationProgramAlarm).indexOf(sender+",")!=-1)
                    {
                        if (((NotificationProgramAlarm).indexOf(","+sender+",")!=-1)||((NotificationProgramAlarm).indexOf(sender+",")==0))
                        {
                            UpdateNotifiction=false;

                        }
                        else{
                            UpdateNotifiction=true;

                        }
                    }
                    else
                    {
                        UpdateNotifiction=true;

                    }
                }
                else {
                    UpdateNotifiction=true;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        initToolbar();
    }

    private void initToolbar() {
        mToolbar = this.findViewById(R.id.areToolbar);
        IARE_ToolItem fontSize = new ARE_ToolItem_FontSize();
        IARE_ToolItem bold = new ARE_ToolItem_Bold();
        IARE_ToolItem italic = new ARE_ToolItem_Italic();
        IARE_ToolItem underline = new ARE_ToolItem_Underline();
        IARE_ToolItem strikethrough = new ARE_ToolItem_Strikethrough();
        IARE_ToolItem quote = new ARE_ToolItem_Quote();
        IARE_ToolItem hr = new ARE_ToolItem_Hr();
        IARE_ToolItem link = new ARE_ToolItem_Link();
        IARE_ToolItem subscript = new ARE_ToolItem_Subscript();
        IARE_ToolItem superscript = new ARE_ToolItem_Superscript();
        IARE_ToolItem left = new ARE_ToolItem_AlignmentLeft();
        IARE_ToolItem center = new ARE_ToolItem_AlignmentCenter();
        IARE_ToolItem right = new ARE_ToolItem_AlignmentRight();
        mToolbar.addToolbarItem(bold);
        mToolbar.addToolbarItem(italic);
        mToolbar.addToolbarItem(underline);
        mToolbar.addToolbarItem(strikethrough);
        mToolbar.addToolbarItem(quote);
        mToolbar.addToolbarItem(hr);
        mToolbar.addToolbarItem(link);
        mToolbar.addToolbarItem(subscript);
        mToolbar.addToolbarItem(superscript);
        mToolbar.addToolbarItem(fontSize);
        mToolbar.addToolbarItem(left);
        mToolbar.addToolbarItem(center);
        mToolbar.addToolbarItem(right);

        mEditText.setToolbar(mToolbar);
        mEditText.setTextColor(getResources().getColor(R.color.colorTextARE));
        mEditText.setBackgroundResource(R.drawable.backgroundman);

    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_program, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        String st = item.getTitle().toString();
        if (st.equals("שלח")) {
            AutoSave=false;
            databaseReference.child("Data").setValue(mEditText.getHtml());
            if (UpdateNotifiction)

            {

                Reference.child("ProgramAlarm").setValue(NotificationProgramAlarm+sender+",");
            }
            }


        return true;
    }
    @Override
    public void onBackPressed() {
        if (AutoSave) {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "לחץ שוב על הלחצן חזור כדי לצאת, התוכנית תשמר אוטומטית", Toast.LENGTH_SHORT).show();
            databaseReference.child("Data").setValue(mEditText.getHtml());
            if (UpdateNotifiction) {
                Reference.child("ProgramAlarm").setValue(NotificationProgramAlarm + sender + ",");
            }
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
        else {
            super.onBackPressed();

        }
    }

    @Override
    protected void onUserLeaveHint()
    {
        mEditText.fromHtml("String");
        if (AutoSave){
        databaseReference.child("Data").setValue(mEditText.getHtml());
        if (UpdateNotifiction)
        {
            Reference.child("ProgramAlarm").setValue(NotificationProgramAlarm+sender+",");
        }
        Toast.makeText(this, "השינויים נשמרו אוטמטית", Toast.LENGTH_SHORT).show();
        Log.d("onUserLeaveHint","Home button pressed");
    }}

}