package heyheyoheyhey.com.ifoundclassmate3.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.HashSet;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Pattern;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.CalendarContract;
import android.net.Uri;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.util.Log;

import heyheyoheyhey.com.ifoundclassmate3.ScheduleItem;

/**
 * Created by vera on 15-3-20.
 */
public class CalendarExtract extends ScheduleItem implements Parcelable {
    public ArrayList<ScheduleTime> calendarTime = new ArrayList<ScheduleTime>();
    public static final String[] FIELDS = { CalendarContract.Calendars._ID,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.VISIBLE };
    public static final Uri CALENDAR_URI = Uri.parse("content://com.android.calendar/calendars");

    ContentResolver contentResolver;
    Set<String> calendars = new HashSet<String>();

    @Override
    public ArrayList<ScheduleTime> getScheduleForDay(int day, int month, int year) {
        Log.d("Calendar", "getScheduleForDay" + day + " " + month + " " + year);
        ArrayList<ScheduleTime> daySchedule = new ArrayList<ScheduleTime>();
        Log.d("Calendar", calendarTime.size() + "");
        for(ScheduleTime st:calendarTime){
            if(st.getDay() == day && st.getMonth() == month && st.getYear() == year)
                daySchedule.add(st);
        }
        return daySchedule;
    }

    @Override
    public String getSaveString() {
        return "";
    }

    public CalendarExtract(Context ctx){
        contentResolver = ctx.getContentResolver();
        getCalendars();
    }

    public Set<String> getCalendars() {
        Log.d("CALENDAR NAME" , "OPEN");
        // Fetch a list of all calendars sync'd with the device and their display names
        Cursor cursor = contentResolver.query(CALENDAR_URI, FIELDS, null, null, null);

        try {
            Log.d("CALENDAR NAME" , ""+cursor.getCount());
            if(cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(0);
                    Log.d("CALENDAR NAME" , id);
                    String displayName = cursor.getString(1);
                    // This is actually a better pattern:
                    Boolean selected = !cursor.getString(2).equals("0");
                    calendars.add(id);
                }
            }
        } catch (AssertionError ex) {
            // TODO: log exception and bail
        }
        for (String id : calendars) {
            Uri.Builder builder = Uri.parse("content://com.android.calendar/instances/when").buildUpon();
            //Uri.Builder builder = Uri.parse("content://com.android.calendar/calendars").buildUpon();
            long now = new Date().getTime();

            ContentUris.appendId(builder, now - DateUtils.DAY_IN_MILLIS * 10000);
            ContentUris.appendId(builder, now + DateUtils.DAY_IN_MILLIS * 10000);

            Cursor eventCursor = contentResolver.query(builder.build(),
                    new String[]  { "title", "begin", "end", "allDay", "description"}, "CALENDAR_ID=" + 1,
                    null, "startDay ASC, startMinute ASC");

            System.out.println("eventCursor count="+eventCursor.getCount());
            if(eventCursor.getCount()>0)
            {

                if(eventCursor.moveToFirst())
                {
                    do
                    {
                        calendarTime.add(calen2Sched(eventCursor));
                    }
                    while(eventCursor.moveToNext());
                }
            }
            break;
        }
        return calendars;
    }

    private ScheduleTime calen2Sched(Cursor eventCursor){
        final String title = eventCursor.getString(0);
        final Date begin = new Date(eventCursor.getLong(1));
        final Date end = new Date(eventCursor.getLong(2));
        final Boolean allDay = !eventCursor.getString(3).equals("0");
        final String description = eventCursor.getString(4);


        System.out.println("Title:"+title);
        System.out.println("Begin:"+begin);
        System.out.println("End:"+end);
        System.out.println("All Day:"+allDay);
        System.out.println("Description:"+description);


        Calendar beginDate = Calendar.getInstance();
        beginDate.setTime(begin);
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(end);

        ScheduleTime scheduleTime =  new ScheduleTime(beginDate.get(Calendar.HOUR_OF_DAY), beginDate.get(Calendar.MINUTE),
                endDate.get(Calendar.HOUR_OF_DAY), endDate.get(Calendar.MINUTE),
                (int)((end.getTime() - begin.getTime()) / 1000 / 60),
                beginDate.get(Calendar.DAY_OF_MONTH), beginDate.get(Calendar.MONTH), beginDate.get(Calendar.YEAR)
                );
        scheduleTime.setEventName(title);
        scheduleTime.setDescription(description);
        return scheduleTime;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(calendarTime.size());
        for(ScheduleTime st: calendarTime) {
            st.writeToParcel(dest, flags);
        }
    }

    public static final Parcelable.Creator<CalendarExtract> CREATOR = new Parcelable.Creator<CalendarExtract>() {
        public CalendarExtract createFromParcel(Parcel in) {
            return new CalendarExtract(in);
        }
        public CalendarExtract[] newArray(int size) {
            return new CalendarExtract[size];
        }
    };

    public CalendarExtract(Parcel in) {
        int size = in.readInt();
        for(int i = 0; i < size; i ++) {
            calendarTime.add(new ScheduleTime(in));
        }
    }
}


