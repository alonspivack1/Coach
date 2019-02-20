package coach.coach;

import android.util.Log;

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
    private String details;
    int i=0;
    int[] answer = new int[8];


    public User(String name, String detailss)
    {
        this.name = name;
        this.details = detailss.substring(0,detailss.length()-1)+", {";
        int index = details.indexOf("=");
        while (index >= 0) {
            Log.e("Answers",index+"");
            answer[i]=index;
            this.i++;
            index = details.indexOf("=", index + 1);
        }
        Log.e("details",details);

        Log.e("height",details.substring(details.indexOf("{Height}=")+9,details.indexOf(", {",details.indexOf("{Height}=")+9)));
        this.height = details.substring(details.indexOf("{Height}=")+9,details.indexOf(", {",details.indexOf("{Height}=")+9));

        Log.e("gender",details.substring(details.indexOf("{Gender}=")+9,details.indexOf(", {",details.indexOf("{Gender}=")+9)));
        this.gender = details.substring(details.indexOf("{Gender}=")+9,details.indexOf(", {",details.indexOf("{Gender}=")+9));

        Log.e("description",details.substring(details.indexOf("{Description}=")+14,details.indexOf(", {",details.indexOf("{Description}=")+14)));
        this.description = details.substring(details.indexOf("{Description}=")+14,details.indexOf(", {",details.indexOf("{Description}=")+14));

        Log.e("weight",details.substring(details.indexOf("{Weight}=")+9,details.indexOf(", {",details.indexOf("{Weight}=")+9)));
        this.weight = details.substring(details.indexOf("{Weight}=")+9,details.indexOf(", {",details.indexOf("{Weight}=")+9));

        Log.e("time",  this.time = details.substring(details.indexOf("{PracticeTime}=")+16,details.indexOf(", {",details.indexOf("{PracticeTime}=")+16)));
        this.time = details.substring(details.indexOf("{PracticeTime}=")+16,details.indexOf(", {",details.indexOf("{PracticeTime}=")+16));

        Log.e("item",details.substring(details.indexOf("{Equipment}=")+12,details.indexOf(", {",details.indexOf("{Equipment}=")+12)));
        this.item = details.substring(details.indexOf("{Equipment}=")+12,details.indexOf(", {",details.indexOf("{Equipment}=")+12));

        Log.e("age",details.substring(details.indexOf("{Age}=")+6,details.indexOf(", {",details.indexOf("{Age}=")+6)));
        this.age = details.substring(details.indexOf("{Age}=")+6,details.indexOf(", {",details.indexOf("{Age}=")+6));

        Log.e("goal",details.substring(details.indexOf("{Goal}=")+7,details.indexOf(", {",details.indexOf("{Goal}=")+7)));
        this.goal = details.substring(details.indexOf("{Goal}=")+7,details.indexOf(", {",details.indexOf("{Goal}=")+7));
    }

    public  String getName() {
        return this.name;
    }
    public String getHeight(){
        return this.height;
    }
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
    public String getWeight(){
        return this.weight;
    }
    public String getDescription() {
        return this.description;
    }
    public String getTime() {
        return this.time;
    }
    public String getItem(){
        return this.item;
    }

    public String getAge() {
        return this.age;
    }
    public String getGoal(){
        return this.goal;
    }

    public String getDetails()
    {
        return this.details;
    }


}