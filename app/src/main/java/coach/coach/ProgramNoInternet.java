package coach.coach;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.chinalwb.are.render.AreTextView;

public class ProgramNoInternet extends AppCompatActivity {

    String room="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_no_internet);
        AreTextView arenointernet = (AreTextView)findViewById(R.id.arenointernet);
        Intent intent = getIntent();
        room = intent.getStringExtra("room");
        Log.e("room",room);

        SharedPreferences Programs = getSharedPreferences("Programs", MODE_PRIVATE);
        final String data = Programs.getString(room,"<ul><li><blockquote><b><i><u><del>&#1492;&#1502;&#1488;&#1502;&#1503; &#1506;&#1491;&#1497;&#1497;&#1503; &#1500;&#1488; &#1513;&#1500;&#1495; </del></u></i></b><b><i><u><del><u>&#1514;&#1493;&#1499;&#1504;&#1497;&#1514;</u></del></u></i></b></blockquote></li></ul>");
        arenointernet.fromHtml(data);


    }
}
