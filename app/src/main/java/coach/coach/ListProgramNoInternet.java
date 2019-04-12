package coach.coach;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListProgramNoInternet extends AppCompatActivity {
ListView nointenrnetprogramlist;
Intent intent;
String username="",coachesnames="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_program_no_internet);
        intent = new Intent(this,ProgramNoInternet.class);
        nointenrnetprogramlist = findViewById(R.id.nointenrnetprogramlist);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1);
        SharedPreferences Coaches = getSharedPreferences("Coaches", MODE_PRIVATE);
        coachesnames =Coaches.getString("coachesnames","");
        username =Coaches.getString("username","");

        while (coachesnames.indexOf(",")!=-1)
        {
            Log.e("NAMES!",coachesnames.substring(0,coachesnames.indexOf(",")));
            adapter.add(coachesnames.substring(0,coachesnames.indexOf(",")));

            coachesnames = coachesnames.substring(coachesnames.indexOf(",")+1);

        }

        nointenrnetprogramlist.setAdapter(adapter);

        nointenrnetprogramlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                intent.putExtra("room",username+"&"+adapter.getItem(position));
                startActivity(intent);
            }
        });
    }

}