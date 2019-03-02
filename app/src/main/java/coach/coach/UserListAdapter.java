package coach.coach;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

/**
 * Created by User on 3/14/2017.
 */
/*
        android:id="@+id/usertvlistname"

        android:id="@+id/usertvlistage"

        android:id="@+id/usertvlistgender"

        android:id="@+id/usertvlisttime"

        android:id="@+id/usertvlistitem"

        android:id="@+id/usertvlistgoal"

        android:id="@+id/usertvlistdescription"

*/

public class UserListAdapter extends ArrayAdapter<User> {

    private static final String TAG = "UserListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;


    private static class ViewHolder {

        TextView usertvlistname;
        TextView usertvlistage;
        TextView usertvlistgender;
        TextView usertvlisttime;
        TextView usertvlistitem;
        TextView usertvlistgoal;
        TextView usertvlistdescription;
        TextView usertvlistheight;
        TextView usertvlistweight;
    }


    public UserListAdapter(Context context, int resource, ArrayList<User> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        String name = getItem(position).getName();
        DataSnapshot details =getItem(position).getDetails();


        User user = new User(name,details);
        final View result;

        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.usertvlistname = (TextView) convertView.findViewById(R.id.usertvlistname);
            holder.usertvlistage = (TextView) convertView.findViewById(R.id.usertvlistage);
            holder.usertvlistgender = (TextView)convertView.findViewById(R.id.usertvlistgender);
            holder.usertvlistweight = (TextView) convertView.findViewById(R.id.usertvlistweight);
            holder.usertvlistheight = (TextView) convertView.findViewById(R.id.usertvlistheight);
            holder.usertvlisttime = (TextView) convertView.findViewById(R.id.usertvlisttime);
            holder.usertvlistitem = (TextView) convertView.findViewById(R.id.usertvlistitem);
            holder.usertvlistgoal = (TextView) convertView.findViewById(R.id.usertvlistgoal);
            holder.usertvlistdescription = (TextView) convertView.findViewById(R.id.usertvlistdescription);


            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        lastPosition = position;


        holder.usertvlistname.setText("שם: "+ user.getName());
        holder.usertvlistage.setText("גיל: " + user.getAge());
        holder.usertvlistgender.setText("מין: "+user.getGender());
        holder.usertvlistweight.setText("משקל: "+user.getWeight());
        holder.usertvlistheight.setText("גובה: "+user.getHeight());
        holder.usertvlisttime.setText("כמה זמן מתאמן: "+user.getTime());
        holder.usertvlistitem.setText("ציוד בבית: "+user.getItem());
        holder.usertvlistgoal.setText("מטרה: "+user.getGoal());
        holder.usertvlistdescription.setText("תיאור קצר: "+user.getDescription());
        return convertView;
    }
}

























