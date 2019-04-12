package coach.coach;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import io.github.mthli.knife.KnifeText;

public class ProgramNoInternet extends AppCompatActivity {

    private KnifeText knifenointernet;
    String room="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_no_internet);
        knifenointernet = (KnifeText)findViewById(R.id.knifenointernet);
        Intent intent = getIntent();
        room = intent.getStringExtra("room");
        Log.e("room",room);

        SharedPreferences Programs = getSharedPreferences("Programs", MODE_PRIVATE);
        final String data = Programs.getString(room,"<ul><li><blockquote><b><i><u><del>&#1492;&#1502;&#1488;&#1502;&#1503; &#1506;&#1491;&#1497;&#1497;&#1503; &#1500;&#1488; &#1513;&#1500;&#1495; </del></u></i></b><b><i><u><del><u>&#1514;&#1493;&#1499;&#1504;&#1497;&#1514;</u></del></u></i></b></blockquote></li></ul>");
                knifenointernet.fromHtml(data);


    }
}
