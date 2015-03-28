package heyheyoheyhey.com.ifoundclassmate3;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

// class representing a group meeting
public class MeetingItem extends ScheduleItem implements Parcelable{
    private int startHours, startMins, endHours, endMins;
    private int day, month, year;
    private int length;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(startHours);
        out.writeInt(startMins);
        out.writeInt(endHours);
        out.writeInt(endMins);
        out.writeInt(day);
        out.writeInt(month);
        out.writeInt(year);

    }

    public static final Parcelable.Creator<MeetingItem> CREATOR = new Parcelable.Creator<MeetingItem>() {
        public MeetingItem createFromParcel(Parcel in) {
            return new MeetingItem(in);
        }
        public MeetingItem[] newArray(int size) {
            return new MeetingItem[size];
        }
    };

    public MeetingItem(Parcel in) {
        startHours = in.readInt();
        startMins = in.readInt();
        endHours = in.readInt();
        endMins = in.readInt();
        day = in.readInt();
        month = in.readInt();
        year = in.readInt();
        this.length = (endHours - startHours) * 60 + (endMins - startMins);
    }

    public MeetingItem(int startHours, int startMins, int endHours, int endMins, int day, int month, int year) {
        this.startHours = startHours;
        this.startMins = startMins;
        this.endHours = endHours;
        this.endMins = endMins;
        this.day = day;
        this.month = month;
        this.year = year;
        this.length = (endHours - startHours) * 60 + (endMins - startMins);
    }

    @Override
    public ArrayList<ScheduleTime> getScheduleForDay(int day, int month, int year) {
        ArrayList<ScheduleTime> retVal = new ArrayList<>();
        if (this.day == day && this.month == month && this.year == year) {
            ScheduleTime myTime = new ScheduleTime(startHours, startMins, endHours, endMins, length, this.day, this.month, this.year);
            retVal.add(myTime);
        }
        return retVal;
    }

    @Override
    public String getSaveString() {
        // for now, assume meetings are not saved to disk
        return "";
    }

    public String getDisplayString() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        String cDayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        String cMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        int mDay = calendar.get(Calendar.DATE);
        int mYear = calendar.get(Calendar.YEAR);
        String test = cDayOfWeek + " " + cMonth + " " + mDay + ", " + mYear;

        int startTimeHours = startHours;
        String startTimeMins;
        if (startMins < 10) startTimeMins = "0" + Integer.toString(startMins);
        else startTimeMins = Integer.toString(startMins);
        int endTimeHours = endHours;
        String endTimeMins;
        if (endMins < 10) endTimeMins = "0" + Integer.toString(endMins);
        else endTimeMins = Integer.toString(endMins);

        String startTimeString;
        String endTimeString;
        if (startTimeHours == 12) {
            startTimeString = startTimeHours + ":" + startTimeMins + " pm";
        } else if (startTimeHours > 12) {
            startTimeHours -= 12;
            startTimeString = startTimeHours + ":" + startTimeMins + " pm";
        }
        else startTimeString = startTimeHours + ":" + startTimeMins + " am";

        if (endTimeHours == 12) {
            endTimeString = endTimeHours + ":" + endTimeMins + " pm";
        } else if (endTimeHours > 12) {
            endTimeHours -= 12;
            endTimeString = endTimeHours + ":" + endTimeMins + " pm";
        }
        else endTimeString = endTimeHours + ":" + endTimeMins + " am";

        return test + ", " + startTimeString + " - " + endTimeString;
    }


}
