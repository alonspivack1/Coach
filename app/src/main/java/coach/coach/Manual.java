package coach.coach;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Manual extends AppCompatActivity {
String manual = "מדריך למשתמש מסוג מאמן:\n" +
        "בכניסה הראשונה לאפליקציה אם בבעלותך מכשיר מסוג xiaomi/oppo/vivo/Letv/Honor תצטרך לאשר לאפליקציה הרשאה \"הפעלה אוטומטית\".\n" +
        "לאחר הכניסה לאפליקציה יפתח מסך ההתחברות, לחץ על הכיתוב \"הרשמה\" ומלא את הפרטים של המשתמש שלך ובסוג המשתמש בחר \"מאמן\", לאחר ביצוע ההרשמה האפליקציה תעבור למסך יצירת פרופיל, מלא את הפרטים המדויקים ככל האפשר, כדי שלמתאמנים יהיה נוח ביותר לחפש אותך. לאחר יצירת הפרופיל האפליקציה תעבור למסך הראשי של האפליקציה, שם יהיו 3 כפתורי ניווט - צאטים,תוכנית אישית והתראות.\n" +
        "כאשר תלחץ על כפתור הניווט \"התראות\", יוצגו כל ההתראות על הבקשות קשר החדשות שנשלחו אליך ולאחר לחיצה על הכיתוב \"בקשות קשר חדשות\", יוצגו כל ההתראות על ההודעות החדשות שנשלחו אליך.\n" +
        "לחיצה על כפתור הניווט \"תכנית אישית\", יוצגו כל הפרופילים של המתאמנים אצלך, לחיצה על אחד מהפרופילים תעביר את האפליקציה למסך של יצירת תוכנית אישית למתאמן.\n" +
        "לחיצה על כפתור הניווט \"צאטים\", יוצגו כל הפרופילים של המתאמנים אצלך, לחיצה על אחד מהפרופילים תעביר את האפליקציה למסך של צאט פרטי עם המתאמן.\n" +
        "בנוסף לכפתורי הניווט יהיה סמל של 3 נקודות בצד שמאל למעלה של המסך, לחיצה על סמל זה יפתח תפריט שדרכו יהיה אפשרות לעדכן את הפרופיל,לעדכן הגדרות, לשלוח מייל למפתח, להגיע למסך קרדיטים ולהתנתק מהמשתמש, לא לשכוח במקרה של התנתקות ההתראות לא יעבדו, ובמקרה של התחברות חוזרת יש צורך לאשר התחברות אוטומטית למשתמש כדי לקבל התראות.\n" +
        "\n" +
        "\n" +
        "\n" +
        "\n" +
        "\n" +
        "\n" +
        "\n" +
        "\n" +
        "מדריך למשתמש מסוג מתאמן:\n" +
        "בכניסה הראשונה לאפליקציה אם בבעלותך מכשיר מסוג xiaomi/oppo/vivo/Letv/Honor תצטרך לאשר לאפליקציה הרשאה \"הפעלה אוטומטית\".\n" +
        "לאחר הכניסה לאפליקציה יפתח מסך ההתחברות, לחץ על הכיתוב \"הרשמה\" ומלא את הפרטים של המשתמש שלך ובסוג המשתמש בחר \"מתאמן\", לאחר ביצוע ההרשמה האפליקציה תעבור למסך יצירת פרופיל, מלא את הפרטים המדויקים ככל האפשר, כדי שלמאמנים שלך יהיה את המידע המדויק ביותר עליך כדי ליצור לך תוכנית אימון מותאמת.\n" +
        "לאחר יצירת הפרופיל האפליקציה תעבור למסך הראשי של האפליקציה, שם יהיו 4 כפתורי ניווט - צאטים,תוכנית אישית,חיפוש מאמנים והתראות.\n" +
        "כאשר תלחץ על כפתור הניווט \"התראות\", יוצגו כל ההתראות על התוכניות אימון שהתעדכנו ולאחר לחיצה על הכיתוב \"תוכניות אימון\", יוצגו כל ההתראות על ההודעות החדשות שנשלחו אליך.\n" +
        "לאחר לחיצה על כפתור הניווט \"חיפוש מאמנים\", יוצגו כל הפרופילים של המאמנים שקיימים באפליקציה, במסך קיים גם חיפוש מדוייק יותר לפי הפרטים שתמלא, לאחר לחיצה על פרופיל של מאמן \"יקפוץ\" חלון שישאל אם אתה רוצה לשלוח בקשת קשר למאמן זה, לאחר אישור, המאמן יתווסף למאמנים שלך ויהיה לך אפשרות לדבר איתו בצאט פרטי ולקבל ממנו תוכנית אימון מותאמת אישית.\n" +
            "לאחר לחיצה על כפתור הניווט \"תוכנית אישית\", יוצגו כל הפרופילים של המאמנים שלך, לחיצה על אחד מהפרופילים תעביר את האפליקציה למסך של צפייה בתוכנית האישית שהמאמן יצר לך.\n" +
            "לאחר לחיצה על כפתור הניווט \"צאטים\", יוצגו כל הפרופילים של המאמנים שלך, לחיצה על אחד מהפרופילים תעביר את האפליקציה למסך של צאט פרטי עם המאמן.\n" +
        "בנוסף לכפתורי הניווט יהיה סמל של 3 נקודות בצד שמאל למעלה של המסך, לחיצה על סמל זה יפתח תפריט שדרכו יהיה אפשרות לעדכן את הפרופיל,לעדכן הגדרות, לשלוח מייל למפתח, להגיע למסך קרדיטים ולהתנתק מהמשתמש, לא לשכוח במקרה של התנתקות ההתראות לא יעבדו, ובמקרה של התחברות חוזרת יש צורך לאשר התחברות אוטומטית למשתמש כדי לקבל התראות.\n";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        TextView tvManual = findViewById(R.id.tvmanual);
        tvManual.setText(manual);
    }
    @Override
    public void onBackPressed() {
        finish();
        Intent intentlogin = new Intent(this,LogInActivity.class);
        startActivity(intentlogin);
    }
}