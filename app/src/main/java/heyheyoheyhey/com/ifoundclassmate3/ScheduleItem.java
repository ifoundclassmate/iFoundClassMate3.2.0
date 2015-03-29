package heyheyoheyhey.com.ifoundclassmate3;

import java.util.ArrayList;

/**
 * Created by Av23 on 2015-02-15.
 */


public abstract class ScheduleItem {
    // Users this schedule pertains to
    //User[] owners;

    public abstract ArrayList<ScheduleTime> getScheduleForDay(int day, int month, int year);

    public abstract String getSaveString();

    public class ScheduleTime implements Comparable<ScheduleTime> {
        public String eventName;
        public String description;
        public int startHours;
        public int startMins;
        public int endHours;
        public int endMins;
        public int length;
        int day, month, year;
        public ScheduleTime(int startHours, int startMins, int endHours, int endMins, int length, int day, int month, int year) {
            this.startHours = startHours;
            this.startMins = startMins;
            this.endHours = endHours;
            this.endMins = endMins;
            this.length = length;
            this.day = day;
            this.month = month;
            this.year = year;
        }
        public void setEventName(String eventName) {
            this.eventName = eventName;
        }
        public void setDescription(String description) {
            this.description = description;
        }

        // assume no conflicting timetables. also all in same day
        public int compareTo(ScheduleTime compare) {
            return (this.startHours - compare.startHours) * 100 + (this.startMins - compare.startMins);
        }

    }
}

