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


    public User(String name, String details)
    {
        this.name = name;
        this.details = details;
        int index = details.indexOf("=");
        while (index >= 0) {
            Log.e("Answers",index+"");
            answer[i]=index;
            this.i++;
            index = details.indexOf("=", index + 1);
        }


        this.height = details.substring(answer[0]+1,details.indexOf(", {Gen"));
        this.gender = details.substring(answer[1]+1,details.indexOf(", {Des"));
        this.description = details.substring(answer[2]+1,details.indexOf(", {Wei"));
        this.weight = details.substring(answer[3]+1,details.indexOf(", {Pra"));
        this.time = details.substring(answer[4]+1,details.indexOf(", {Equ"));
        this.item = details.substring(answer[5]+1,details.indexOf(", {Age"));
        this.age = details.substring(answer[6]+1,details.indexOf(", {Goa"));
        this.goal = details.substring(answer[7]+2,details.length()-2);
        Log.e("Full",details);
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