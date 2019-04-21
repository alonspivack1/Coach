package coach.coach;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import com.google.firebase.database.DataSnapshot;

/**
 * Create new Coach.
 */
public class Coach {
    private String name;
    private String age;
    private String time;
    private String where;
    private String description;
    private String professionalization;
    private String gender;
    private Bitmap image;
    private DataSnapshot details;

    /**
     * Instantiates a new Coach.
     *
     * @param name         the name of the coach
     * @param dataSnapshot the data snapshot of the coach
     */
    public Coach (String name,DataSnapshot dataSnapshot)
    {
        this.name = name;
        this.age = dataSnapshot.child("Age").getValue().toString();
        this.time = dataSnapshot.child("CoachTime").getValue().toString();
        this.where = dataSnapshot.child("StudyPlace").getValue().toString();
        this.description = dataSnapshot.child("Description").getValue().toString();
        this.professionalization = dataSnapshot.child("Professionalization").getValue().toString().substring(1);
        this.gender = dataSnapshot.child("Gender").getValue().toString();
        this.image = StringToBitmap(dataSnapshot.child("Image").getValue().toString());
        this.details = dataSnapshot;


    }


    /**
     * Gets name.
     *
     * @return the name of the coach.
     */
    public  String getName() {
        return this.name;
    }

    /**
     * Gets age.
     *
     * @return the age of the coach.
     */
    public String getAge() {
        return this.age;
    }

    /**
     * Gets time.
     *
     * @return the amount of time the coach trains.
     */
    public String getTime() {
        return this.time;
    }

    /**
     * Gets where.
     *
     * @return Where the coach learned to train.
     */
    public String getWhere() {
        return this.where;
    }

    /**
     * Gets description.
     *
     * @return the description about the coach.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Gets professionalization.
     *
     * @return the professionalism of the coach.
     */
    public String getProfessionalization() {
        return this.professionalization;
    }

    /**
     * Gets details.
     *
     * @return the datasnapshot of the coach.
     */
    public DataSnapshot getDetails()
    {
        return this.details;
    }

    /**
     * Gets image.
     *
     * @return the profile image of the coach.
     */
    public Bitmap getImage() {
        return this.image;
    }

    /**
     * Gets gender.
     *
     * @return the gender of the coach.
     */
    public  String getGender() {
        if (this.gender.equals("Male"))
        {
            return "זכר";
        }
        else
        {
            return "נקבה";
        }
    }
    private static Bitmap StringToBitmap(String encodedString) {
        if (encodedString.equals("0"))
        {
            return null;
        }
        else{
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (NullPointerException e) {
            e.getMessage();
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }
    }


}