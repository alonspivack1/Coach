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
    public Coach(String name,String detailss)
    {
        Log.e("details",detailss);
        this.details = detailss.substring(0,detailss.length()-1)+", {";
        int index = details.indexOf("=");
        while (index >= 0) {
            Log.e("Answers",index+"");
            answer[i]=index;
            this.i++;
            index = details.indexOf("=", index + 1);
        }
        Log.e("details",details);

        this.name = name;
        Log.e("where",details.substring(details.indexOf("{StudyPlace}=")+13,details.indexOf(", {",details.indexOf("{StudyPlace}=")+13)));
        //this.where = details.substring(details.indexOf("{StudyPlace}=")+13,details.indexOf(", {",details.indexOf("{StudyPlace}=")+13));
        this.where = details.substring(details.indexOf("{StudyPlace}=")+13,details.indexOf(", {",details.indexOf("{StudyPlace}=")+13));
        Log.e("professionalization",details.substring(details.indexOf("{Professionalization}=")+23,details.indexOf(", {",details.indexOf("{Professionalization}=")+23)));
        this.professionalization =  details.substring(details.indexOf("{Professionalization}=")+23,details.indexOf(", {",details.indexOf("{Professionalization}=")+23));
        Log.e("age",details.substring(details.indexOf("{Age}=")+6,details.indexOf(", {",details.indexOf("{Age}=")+6)));
        this.age = details.substring(details.indexOf("{Age}=")+6,details.indexOf(", {",details.indexOf("{Age}=")+6));
        Log.e("time",details.substring(details.indexOf("{CoachTime}=")+12,details.indexOf(", {",details.indexOf("{CoachTime}=")+12)));
        this.time = details.substring(details.indexOf("{CoachTime}=")+12,details.indexOf(", {",details.indexOf("{CoachTime}=")+12));
        Log.e("gender",details.substring(details.indexOf("{Gender}=")+8,details.indexOf(", {",details.indexOf("{Gender}=")+8)));
        this.gender = details.substring(details.indexOf("{Gender}=")+8,details.indexOf(", {",details.indexOf("{Gender}=")+8));
        Log.e("description",details.substring(details.indexOf("{Description}=")+14,details.indexOf(", {",details.indexOf("{Description}=")+14)));
        this.description = details.substring(details.indexOf("{Description}=")+14,details.indexOf(", {",details.indexOf("{Description}=")+14));
        /*Log.e("where",details.substring(answer[0]+1,details.indexOf(", {Pro")));
        this.where = details.substring(answer[0]+1,details.indexOf(", {Pro"));
        Log.e("professionalization",details.substring(answer[1]+2,details.indexOf(", {Age")-1));
        this.professionalization = details.substring(answer[1]+2,details.indexOf(", {Age")-1);
        Log.e("age",details.substring(answer[2]+1,details.indexOf(", {Coa")));
        this.age = details.substring(answer[2]+1,details.indexOf(", {Coa"));
        Log.e("time",details.substring(answer[3]+1,details.indexOf(", {Gen")));
        this.time = details.substring(answer[3]+1,details.indexOf(", {Gen"));
        this.gender = details.substring(answer[4]+1,details.indexOf(", {Des"));
        this.description = details.substring(answer[5]+1,details.length()-1);*/
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