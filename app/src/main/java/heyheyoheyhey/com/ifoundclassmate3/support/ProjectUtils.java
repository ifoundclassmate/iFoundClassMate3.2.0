package heyheyoheyhey.com.ifoundclassmate3.support;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Av23 on 2015-02-25.
 */
public class ProjectUtils {
    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static String termIdToName(int id) {
        String retVal;
        int retYear = 0;

        // first digit is the century (1 = 21st century)
        int century = id / 1000;
        retYear += (century + 19) * 100;

        // middle 2 digits is the year (13 = 13th year)
        int year = id / 10 % 100;
        retYear += year;

        // last digit is term
        int term = id % 10;
        String retTerm;
        if (term == 1) {
            retTerm = "Winter";
        } else if (term == 5) retTerm = "Spring";
        else retTerm = "Fall";

        retVal = retTerm + " " + retYear;
        return retVal;
    }
}
