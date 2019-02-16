package coach.coach;

import android.util.Log;

public class Coach {
    private String name;
    private String age;
    private String time;
    private String where;
    private String description;
    private String professionalization;
    private String gender;
    private String details;
    int i=0;
    int[] answer = new int[6];

    public Coach(String name, String age,String time,String where,String description,String professionalization) {
        this.name = name;
        this.age = age;
        this.time = time;
        this.where = where;
        this.description = description;
        this.professionalization = professionalization;

    }
    public Coach(String name,String details)
    {
        this.details = details;
        int index = details.indexOf("=");
        while (index >= 0) {
            Log.e("Answers",index+"");
            answer[i]=index;
            this.i++;
            index = details.indexOf("=", index + 1);
        }

        this.name = name;
        this.where = details.substring(answer[0]+1,details.indexOf(", {Pro"));
        this.professionalization = details.substring(answer[1]+2,details.indexOf(", {Age")-1);
        this.age = details.substring(answer[2]+1,details.indexOf(", {Coa"));
        this.time = details.substring(answer[3]+1,details.indexOf(", {Gen"));
        this.gender = details.substring(answer[4]+1,details.indexOf(", {Des"));
        this.description = details.substring(answer[5]+1,details.length()-1);
        //this.description = details.substring(answer[5]+1,details.indexOf(details.length()-2));
        //this.description = details.substring(answer[5]+1,details.indexOf("}"));
    }

    public  String getName() {
        return this.name;
    }

    public String getAge() {
        return this.age;
    }
    public String getTime() {
        return this.time;
    }
    public String getWhere() {
        return this.where;
    }
    public String getDescription() {
        return this.description;
    }
    public String getProfessionalization() {
        return this.professionalization;
    }
    public String getDetails()
    {
        return this.details;
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

}