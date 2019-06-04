package coach.coach;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.ToggleButton;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

/**
 * Fragment alerts list.
 */
public class FragmentAlerts extends Fragment {
    /**
     * The Alertslist.
     */
    ListView alertslist;
    /**
     * The Listprogram.
     */
    ArrayList<HashMap<String,String>> listprogram = new ArrayList<HashMap<String,String>>();
    /**
     * The Listchat.
     */
    ArrayList<HashMap<String,String>> listchat = new ArrayList<HashMap<String,String>>();

    /**
     * The Adapterprogram.
     */
    SimpleAdapter adapterprogram,
    /**
     * The Adapterchat.
     */
    adapterchat;
    /**
     * The Item.
     */
    HashMap<String,String> item;
    /**
     * The Chat.
     */
    String chat,
    /**
     * The Program.
     */
    program;
    /**
     * The Btnclearalert.
     */
    Button btnclearalert;
    /**
     * The Tbalert.
     */
    ToggleButton tbalert;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_alerts, container, false);
        MainActivity activity = (MainActivity) getActivity();
        final String type = activity.getType();
        SharedPreferences alerts = getActivity().getSharedPreferences("Alerts", MODE_PRIVATE);
        chat=alerts.getString("chat","null");
        program=alerts.getString("program","null");
        tbalert=(ToggleButton)v.findViewById(R.id.tbalert);
        if (type.equals("Coach"))
        {
            tbalert.setTextOff("בקשות קשר חדשות");
            tbalert.setText("בקשות קשר חדשות");
        }
        btnclearalert = (Button)v.findViewById(R.id.btnclearalert);
            alertslist = v.findViewById(R.id.alertslist);


            adapterprogram = new SimpleAdapter(getActivity(), listprogram, R.layout.twolinesalert,
        new String[] { "alertmessage","alerttime"},
                new int[] {R.id.alertmessage,R.id.alerttime});

            adapterchat = new SimpleAdapter(getActivity(), listchat, R.layout.twolinesalert,
                new String[] { "alertmessage","alerttime"},
                new int[] {R.id.alertmessage,R.id.alerttime});
             alertslist.setAdapter(adapterprogram);


            if (!chat.equals("null")){
             int chatint = countChar(chat,',');
            for (int i=0;i<chatint;i++){
                item = new HashMap<String, String>();
                String name = chat.substring(0, chat.indexOf("-"));
                item.put("alertmessage", chat.substring(0, chat.indexOf("-")));
                chat = chat.substring(chat.indexOf("-")+1);
                item.put("alerttime", chat.substring(0, chat.indexOf(",")));
                chat = chat.substring(chat.indexOf(",")+1);
                    listchat.add(item);

             }}

        if (!program.equals("null")){

            int programint = countChar(program,',');
             if (type.equals("User")){
        for (int j=0;j<programint;j++){
            item = new HashMap<String, String>();
            String name = program.substring(0, program.indexOf("-"));
                item.put("alertmessage", "תוכנית האימון עודכנה מהמאמן " + program.substring(0, program.indexOf("-")));
                program = program.substring(program.indexOf("-") + 1);
                item.put("alerttime", program.substring(0, program.indexOf(",")));
                program = program.substring(program.indexOf(",") + 1);

            listprogram.add(item);
        }
             }
             else
             {
                 for (int j=0;j<programint;j++){

                     item = new HashMap<String, String>();
                     String name = program.substring(0, program.indexOf("-"));
                     item.put("alertmessage", "נשלחה בקשת קשר חדשה מהמשתמש "+program.substring(0, program.indexOf("-")));
                     program = program.substring(program.indexOf("-")+1);
                     item.put("alerttime", program.substring(0, program.indexOf(",")));
                     program = program.substring(program.indexOf(",")+1);


                         listprogram.add(item);
                 }
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
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("Alerts", MODE_PRIVATE).edit();
                    editor.putString("chat","");
                    editor.apply();
                    alertslist.setAdapter(adapterchat);



                }
                else {
                    program="";
                    listprogram.clear();
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("Alerts", MODE_PRIVATE).edit();
                    editor.putString("program","");
                    editor.apply();
                    alertslist.setAdapter(adapterprogram);

                }
                }
        });
        return v;
    }

    /**
     * Count char int.
     *
     * @param str The string for testing.
     * @param c   the char test.
     * @return The number of times the parameter c appears in the str string.
     */
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
