package coach.coach;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        RatingBar rblistrate;
        DatabaseReference databaseReference;
        TextView tvlistrate;

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

        final ViewHolder holder;


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
            holder.rblistrate = (RatingBar) convertView.findViewById(R.id.rblistrate);
            holder.tvlistrate = (TextView) convertView.findViewById(R.id.tvlistrate);

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
        holder.tvlistwhere.setText("איפה למד לאמן: "+coach.getWhere());
        holder.tvlistprofessionalization.setText("התמקצעות: "+coach.getProfessionalization());
        holder.tvlistgender.setText("מין: "+coach.getGender());
        holder.tvlistdescription.setText("תיאור קצר: "+coach.getDescription());
        holder.databaseReference = FirebaseDatabase.getInstance().getReference().child("Rating").child(name);
        holder.databaseReference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int RatersNumber=Integer.parseInt(dataSnapshot.child("RatersNumber").getValue().toString());
                        float Rating = Float.valueOf(dataSnapshot.child("Rating").getValue().toString());
                        Rating = Rating/RatersNumber;
                        holder.rblistrate.setRating(Rating);
                        holder.tvlistrate.setText("("+RatersNumber+")");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        holder.rblistrate.setRating(0);
                        holder.tvlistrate.setText("(0)");

                    }
                });

        return convertView;
    }
}

























