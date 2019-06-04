package coach.coach;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import com.google.firebase.database.DataSnapshot;

/**
 * Create new User.
 */
public class User {
    private String name;
    private String age;
    private String weight;
    private String height;
    private String time;
    private String item;
    private String description;
    private String goal;
    private String gender;
    private Bitmap image;
    private DataSnapshot details;

    /**
     * Instantiates a new User.
     *
     * @param name         the name of the user.
     * @param dataSnapshot the data snapshot of the user.
     */
    public User (String name,DataSnapshot dataSnapshot)
    {
        this.name = name;
        this.age = dataSnapshot.child("Age").getValue().toString();
        this.weight = dataSnapshot.child("Weight").getValue().toString();
        this.height = dataSnapshot.child("Height").getValue().toString();
        this.time = dataSnapshot.child("PracticeTime").getValue().toString();
        this.item = dataSnapshot.child("Equipment").getValue().toString();
        this.description = dataSnapshot.child("Description").getValue().toString();
        this.goal = dataSnapshot.child("Goal").getValue().toString().substring(1);
        this.gender = dataSnapshot.child("Gender").getValue().toString();
        this.image = StringToBitmap(dataSnapshot.child("Image").getValue().toString());
        this.details = dataSnapshot;


    }


    /**
     * Gets name.
     *
     * @return the name of the user.
     */
    public  String getName() {
        return this.name;
    }

    /**
     * Get height.
     *
     * @return the height of the user.
     */
    public String getHeight(){
        return this.height;
    }

    /**
     * Gets gender.
     *
     * @return the gender of the user.
     */
    public  String getGender() {
        return this.gender;
    }

    /**
     * Get weight.
     *
     * @return the weight of the user.
     */
    public String getWeight(){
        return this.weight;
    }

    /**
     * Get BMI.
     *
     * @return the bmi of the user.
     */
    public String getBMI(){
        float bmi = Float.valueOf(this.weight)/((Float.valueOf(this.height)/100)*(Float.valueOf(this.height))/100);
        if (bmi<1000) {
            String health="";
            if (bmi <= 18.5) {
                health="תת משקל";
            }
            if (bmi > 18.5 && bmi <= 25) {
                health="משקל תקין";
            }
            if (bmi > 25 && bmi <= 29.9) {
                health="משקל עודף";
            }
            if (bmi>29.9&&bmi<=34.9) {
                health="השמנה בנונית";
            }
            if (bmi>34.9&&bmi<=39.9) {
                health="השמנה חמורה";
            }
            if (bmi>39.9) {
                health="השמנה חמורה מאוד";
            }
            return health;
        }
        else {
            return "";
        }
    }

    /**
     * Gets description.
     *
     * @return the description of the user.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Gets time.
     *
     * @return How many time the user is practicing
     */
    public String getTime() {
        return this.time;
    }

    /**
     * Get items.
     *
     * @return the training equipment the user has at home
     */
    public String getItem(){
        return this.item;
    }

    /**
     * Gets image.
     *
     * @return the profile image of the user.
     */
    public Bitmap getImage() {
        return this.image;
    }

    /**
     * Gets age.
     *
     * @return the age of the user.
     */
    public String getAge() {
        return this.age;
    }

    /**
     * Get goal.
     *
     * @return the goal of the user.
     */
    public String getGoal(){
        return this.goal;
    }

    /**
     * Gets details.
     *
     * @return the data snapshot of the user.
     */
    public DataSnapshot getDetails()
    {
        return this.details;
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