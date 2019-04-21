package coach.coach;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.chinalwb.are.render.AreTextView;

/**
 * Activity just for Users - see the training programs that the coach have create for you - in a state without internet.
 */
public class ProgramNoInternet extends AppCompatActivity {

    /**
     * The Room.
     */
    String room="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_no_internet);
        setTitle("תוכנית אימון - ללא אינטרנט");
        AreTextView arenointernet = (AreTextView)findViewById(R.id.arenointernet);
        Intent intent = getIntent();
        room = intent.getStringExtra("room");

        SharedPreferences Programs = getSharedPreferences("Programs", MODE_PRIVATE);
        final String data = Programs.getString(room,"<html><body><p><b><i><u><span style=\"text-decoration:line-through;\">​</span></u></i></b><b><i><u><span style=\"text-decoration:line-through;\"><i><b><u><span style=\"text-decoration:line-through;\">המאמן שלך עדין לא עדכן את התוכנית אימון שלך</span></u></b></i></span></u></i></b></p>\n</body></html>");
        arenointernet.fromHtml(data);


    }
}
