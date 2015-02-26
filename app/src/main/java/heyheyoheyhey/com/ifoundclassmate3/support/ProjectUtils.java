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
}
