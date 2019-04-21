package coach.coach;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;


/**
 * make list of Users
 */
public class UserListAdapter extends ArrayAdapter<User> {

    private static final String TAG = "UserListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;


    private static class ViewHolder {

        /**
         * The Usertvlistname.
         */
        TextView usertvlistname;
        /**
         * The Usertvlistage.
         */
        TextView usertvlistage;
        /**
         * The Usertvlistgender.
         */
        TextView usertvlistgender;
        /**
         * The Usertvlisttime.
         */
        TextView usertvlisttime;
        /**
         * The Usertvlistitem.
         */
        TextView usertvlistitem;
        /**
         * The Usertvlistgoal.
         */
        TextView usertvlistgoal;
        /**
         * The Usertvlistdescription.
         */
        TextView usertvlistdescription;
        /**
         * The Usertvlistheight.
         */
        TextView usertvlistheight;
        /**
         * The Usertvlistweight.
         */
        TextView usertvlistweight;
        /**
         * The Userivlistimage.
         */
        ImageView userivlistimage;

    }


    /**
     * Instantiates a new User list adapter.
     *
     * @param context  the context
     * @param resource the resource
     * @param objects  the User Array
     */
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
            holder.userivlistimage = (ImageView) convertView.findViewById(R.id.userivlistimage);



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
        if (user.getImage()==null)
        {
            holder.userivlistimage.setImageResource(R.drawable.unimage);

        }
        else {
            holder.userivlistimage.setImageBitmap(user.getImage());

        }
        holder.usertvlistdescription.setText("תיאור קצר: "+user.getDescription());
        return convertView;
    }
}

























