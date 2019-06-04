package coach.coach;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Activity just for Users - see the list of training programs that the coaches have create for you - in a state without internet.
 */
public class ListProgramNoInternet extends AppCompatActivity {
    /**
     * The Nointenrnetprogramlist.
     */
    ListView nointenrnetprogramlist;
    /**
     * The Intent.
     */
    Intent intent;
    /**
     * The Connected.
     */
    boolean connected=false;
    /**
     * The Connectivity manager.
     */
    ConnectivityManager connectivityManager;

    /**
     * The Username.
     */
    String username="", /**
     * The Coachesnames.
     */
    coachesnames="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_program_no_internet);
        setTitle("מסך ללא אינטרנט");
        intent = new Intent(this,ProgramNoInternet.class);
        nointenrnetprogramlist = findViewById(R.id.nointenrnetprogramlist);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.textlistnointernet, R.id.textviewnointernet);
        SharedPreferences Coaches = getSharedPreferences("Coaches", MODE_PRIVATE);
        coachesnames =Coaches.getString("coachesnames","");
        username =Coaches.getString("username","");
        if (!username.equals(""))
        {
            setTitle("רשימת תוכניות אימון - ללא אינטרנט");
        }

        while (coachesnames.indexOf(",")!=-1)
        {
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

    /**
     * find network to back to MainActivity
     *
     * @param view the view
     */
    public void RefreshNetwork(View view) {
        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }
        else{
            connected = false;
        }
        if (connected)
        {
            Intent intent = new Intent(this,LogInActivity.class);
            finish();
            startActivity(intent);
        }
    }
}