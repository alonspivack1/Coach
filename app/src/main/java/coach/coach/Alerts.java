package coach.coach;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;

public class Alerts extends AppCompatActivity {
    ListView alertslist;
    ArrayList<HashMap<String,String>> listprogram = new ArrayList<HashMap<String,String>>();
    ArrayList<HashMap<String,String>> listchat = new ArrayList<HashMap<String,String>>();

    SimpleAdapter adapterprogram,adapterchat;
    HashMap<String,String> item;
    String chat,program;
    Button btnclearalert;
    ToggleButton tbalert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);
        SharedPreferences alerts = getSharedPreferences("Alerts", MODE_PRIVATE);
        Log.e("ALERTS!",alerts.getString("chat","null"));
        Log.e("ALERTS!",alerts.getString("program","null"));
        chat=alerts.getString("chat","null");
        program=alerts.getString("program","null");
        tbalert=(ToggleButton)findViewById(R.id.tbalert);
        btnclearalert = (Button)findViewById(R.id.btnclearalert);
            alertslist = findViewById(R.id.alertslist);


            adapterprogram = new SimpleAdapter(this, listprogram, R.layout.twolines,
                new String[] { "sender","receiver","sendertime","receivertime"},
                new int[] {R.id.sendmessage, R.id.receivemessage,R.id.sendertime,R.id.receivetime});
            adapterchat = new SimpleAdapter(this, listchat, R.layout.twolines,
                new String[] { "sender","receiver","sendertime","receivertime"},
                new int[] {R.id.sendmessage, R.id.receivemessage,R.id.sendertime,R.id.receivetime});
             alertslist.setAdapter(adapterprogram);

            if (!chat.equals("null")){
             int chatint = countChar(chat,',');
                Log.e("ChatCount",chatint+"");

            for (int i=0;i<chatint;i++){
                Log.e("INFOstart",chat);
                item = new HashMap<String, String>();
                item.put("sender", chat.substring(0, chat.indexOf("-")));
                chat = chat.substring(chat.indexOf("-"+1));
                Log.e("INFO-cut",chat);
                item.put("sendertime", chat.substring(0, chat.indexOf(",")));
                chat = chat.substring(chat.indexOf(",")+1);
                Log.e("INFO,cut",chat);

                item.put("receiver", "");
                item.put("receivertime","");
                listchat.add(item);
             }}

        if (!program.equals("null")){

            int programint = countChar(program,',');
             Log.e("ProgramCount",programint+"");
        for (int j=0;j<programint;j++){
            Log.e("programnumber","j="+j);
            Log.e("programINFOstart",program);
            item = new HashMap<String, String>();
            item.put("receiver", program.substring(0, program.indexOf("-")));
            program = program.substring(program.indexOf("-"+1));
            Log.e("programINFO-cut",program);
            item.put("receivertime", program.substring(0, program.indexOf(",")));
            program = program.substring(program.indexOf(",")+1);
            Log.e("programINFO,cut",program);

            item.put("sender", "");
            item.put("sendertime", "");

            listprogram.add(item);
        }

    }
        tbalert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    alertslist.setAdapter(adapterchat);
                }
                else
                {
                    alertslist.setAdapter(adapterprogram);
                }
            }
        });
        btnclearalert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tbalert.isChecked())
                {
                    chat="";
                    listchat.clear();
                    SharedPreferences.Editor editor = getSharedPreferences("Alerts", MODE_PRIVATE).edit();
                    editor.putString("chat","");
                    editor.apply();
                    alertslist.setAdapter(adapterchat);

                }
                else {
                    program="";
                    listprogram.clear();
                    SharedPreferences.Editor editor = getSharedPreferences("Alerts", MODE_PRIVATE).edit();
                    editor.putString("program","");
                    editor.apply();
                    alertslist.setAdapter(adapterprogram);


                }
            }
        });
    }
    public int countChar(String str, char c)
    {
        int count = 0;

        for(int i=0; i < str.length(); i++)
        {    if(str.charAt(i) == c)
            count++;
        }

        return count;
    }
}
