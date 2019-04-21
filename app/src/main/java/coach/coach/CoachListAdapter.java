package coach.coach;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;


/**
 * make list of Coaches
 */
public class CoachListAdapter extends ArrayAdapter<Coach> {

    private static final String TAG = "CoachListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;



    private static class ViewHolder {

        /**
         * The Tvlistname.
         */
        TextView tvlistname;
        /**
         * The Tvlistage.
         */
        TextView tvlistage;
        /**
         * The Tvlisttime.
         */
        TextView tvlisttime;
        /**
         * The Tvlistwhere.
         */
        TextView tvlistwhere;
        /**
         * The Tvlistprofessionalization.
         */
        TextView tvlistprofessionalization;
        /**
         * The Tvlistdescription.
         */
        TextView tvlistdescription;
        /**
         * The Tvlistgender.
         */
        TextView tvlistgender;
        /**
         * The Rblistrate.
         */
        RatingBar rblistrate;
        /**
         * The Database reference.
         */
        DatabaseReference databaseReference;
        /**
         * The Tvlistrate.
         */
        TextView tvlistrate;
        /**
         * The Ivlistimage.
         */
        ImageView ivlistimage;

    }


    /**
     * Instantiates a new Coach list adapter.
     *
     * @param context  the context
     * @param resource the resource
     * @param objects  the Coach Array
     */
    public CoachListAdapter(Context context, int resource, ArrayList<Coach> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        String name = getItem(position).getName();
        DataSnapshot details =getItem(position).getDetails();
        Coach coach = new Coach(name,details);


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
            holder.ivlistimage = (ImageView) convertView.findViewById(R.id.ivlistimage);


            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        lastPosition = position;


        holder.tvlistname.setText("שם: "+coach.getName());
        holder.tvlistage.setText("גיל: " + coach.getAge());
        holder.tvlisttime.setText("ותק באימון: "+coach.getTime());
        holder.tvlistwhere.setText("מקום התמקצעות: "+coach.getWhere());
        holder.tvlistprofessionalization.setText("התמקצעות: "+coach.getProfessionalization());
        holder.tvlistgender.setText("מין: "+coach.getGender());
        if (coach.getImage()==null)
        {
            holder.ivlistimage.setImageResource(R.drawable.unimage);

        }
        else {
            holder.ivlistimage.setImageBitmap(coach.getImage());

        }
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

























