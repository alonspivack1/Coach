package coach.coach;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by User on 3/14/2017.
 */

public class CoachListAdapter extends ArrayAdapter<Coach> {

    private static final String TAG = "CoachListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;


    private static class ViewHolder {

        TextView tvlistname;
        TextView tvlistage;
        TextView tvlisttime;
        TextView tvlistwhere;
        TextView tvlistprofessionalization;
        TextView tvlistdescription;
        TextView tvlistgender;
    }


    public CoachListAdapter(Context context, int resource, ArrayList<Coach> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String name = getItem(position).getName();
        String age = getItem(position).getAge();
        String time = getItem(position).getTime();
        String where = getItem(position).getWhere();
        String professionalization = getItem(position).getProfessionalization();
        String description = getItem(position).getDescription();
        String gender = getItem(position).getGender();
        String details =getItem(position).getDetails();


        Coach coach = new Coach(name,details);
        //Coach coach = new Coach(name,age,time,where,professionalization,description);
        final View result;

        ViewHolder holder;


        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.tvlistname = (TextView) convertView.findViewById(R.id.tvlistname);
            holder.tvlistage = (TextView) convertView.findViewById(R.id.tvlistage);
            holder.tvlisttime = (TextView) convertView.findViewById(R.id.tvlisttime);
            holder.tvlistwhere = (TextView) convertView.findViewById(R.id.tvlistwhere);
            holder.tvlistprofessionalization = (TextView) convertView.findViewById(R.id.tvlistprofessionalization);
            holder.tvlistdescription = (TextView) convertView.findViewById(R.id.tvlistdescription);
            holder.tvlistgender = (TextView)convertView.findViewById(R.id.tvlistgender);

            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        lastPosition = position;

       /* holder.name.setText(person.getName());
        holder.birthday.setText(person.getBirthday());
        holder.sex.setText(person.getSex());*/
        holder.tvlistname.setText("שם: "+coach.getName());
        holder.tvlistage.setText("גיל: " + coach.getAge());
        holder.tvlisttime.setText("כמה זמן מאמן: "+coach.getTime());
        holder.tvlistwhere.setText("למד ב: "+coach.getWhere());
        holder.tvlistprofessionalization.setText("התמקצעות: "+coach.getProfessionalization());
        holder.tvlistgender.setText("מין: "+coach.getGender());
        holder.tvlistdescription.setText("תיאור קצר: "+coach.getDescription());
        return convertView;
    }
}

























