package heyheyoheyhey.com.ifoundclassmate3;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import heyheyoheyhey.com.ifoundclassmate3.util.CalendarExtract;

/**
 * Created by Av23 on 2015-02-13.
 */
public class User implements Parcelable {
    private String id;
    private String userName;
    private String password;
    private ArrayList<ScheduleItem> scheduleItems;
    private CalendarExtract calendarextract;
    private ArrayList<Group> groups;

    private ArrayList<String> friends;

    // necessary for parcelable
    public int describeContents() {
        return 0;
    }

    // writes to parcel
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.id);
        out.writeString(this.userName);
        out.writeString(this.password);
        //TODO: add other user fields when implemented.
        out.writeList(scheduleItems);
        out.writeList(groups);
        out.writeList(friends);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel in) {
        this.id = in.readString();
        this.userName = in.readString();
        this.password = in.readString();
        this.scheduleItems = new ArrayList<ScheduleItem>();
        this.scheduleItems = (ArrayList<ScheduleItem>) in.readArrayList(ScheduleItem.class.getClassLoader());
        this.groups = new ArrayList<Group>();
        this.groups = (ArrayList<Group>) in.readArrayList(Group.class.getClassLoader());
        this.friends = new ArrayList<String>();
        this.friends = (ArrayList<String>) in.readArrayList(String.class.getClassLoader());
    }

    // create user from saved file (note: groups not saved)
    public User(File userFile, Context context) {
        this.id = null;
        this.scheduleItems = new ArrayList<ScheduleItem>();
        int ch;
        StringBuffer fileContent = new StringBuffer("");
        FileInputStream fis;
        try {
            fis = context.openFileInput( userFile.getName() );
            try {
                while( (ch = fis.read()) != -1)
                    fileContent.append((char)ch);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String data = new String(fileContent);
        int idPos = data.indexOf("USER:") + 5;
        int idEnd = data.indexOf("\n", idPos);
        String newId = data.substring(idPos, idEnd);
        System.out.println("reading from file: your id is: " + newId);
        this.id = newId;

        idPos = data.indexOf("USERNAME:") + 9;
        idEnd = data.indexOf("\n", idPos);
        this.userName = data.substring(idPos, idEnd);

        idPos = data.indexOf("PASSWORD:") + 9;
        idEnd = data.indexOf("\n", idPos);
        this.password = data.substring(idPos, idEnd);

        if (idEnd + 1 < data.length()) {
            String schedule = data.substring(idEnd + 1);
            while (schedule.length() > 0) {
                String temp = schedule.substring(0, schedule.indexOf("\n"));
                schedule = schedule.substring(schedule.indexOf("\n") + 1);
                if (temp.equals("COURSEITEM")) {

                    CourseItem courseItem = new CourseItem(schedule.substring(0, schedule.indexOf("\n")));
                    this.scheduleItems.add(courseItem);
                }
                schedule = schedule.substring(schedule.indexOf("\n") + 1);
            }
        }
        // Groups are not saved on disk
        this.groups = new ArrayList<Group>();
        // friends currently not saved on disk
        this.friends = new ArrayList<String>();
    }
    public User(String id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.scheduleItems = new ArrayList<ScheduleItem>();
        this.groups = new ArrayList<Group>();
        this.friends = new ArrayList<String>();
    }

    public String getId() { return this.id; }
    public String getUsername() {
        return this.userName;
    }
    public String getPassword() { return this.password; }

    public void writeToDisk(Context context) {
        String path = id;
        String toWrite = buildUser();
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(path, Context.MODE_PRIVATE);
            outputStream.write(toWrite.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String buildUser() {
        String toWrite;
        toWrite = "USER:" + this.id + "\n";
        toWrite += "USERNAME:" + this.userName + "\n";
        toWrite += "PASSWORD:" + this.password + "\n";
        //TODO: add schedule
        for (ScheduleItem scheduleItem : scheduleItems) {
            toWrite += scheduleItem.getSaveString();
        }
        return toWrite;
    }

    public void addScheduleItem(ScheduleItem scheduleItem) {
        this.scheduleItems.add(scheduleItem);
    }

    public void removeScheduleItem(ScheduleItem scheduleItem) {
        this.scheduleItems.remove(scheduleItem);
    }

    public ArrayList<ScheduleItem> getScheduleItems() {
        return this.scheduleItems;
    }

    public void addToGroup(Group group) { this.groups.add(group); }

    public ArrayList<Group> getGroups() { return this.groups; }

    public void SetCalendar(Context cxt){
        Log.d("Before create",this.scheduleItems.size()+"");
        calendarextract = new CalendarExtract(cxt);
        Log.d("Before add",this.scheduleItems.size()+"");
        this.addScheduleItem(calendarextract);
        Log.d("After add",this.scheduleItems.size()+" " + this.getId());
    }
    public void addFriend(String friend) { this.friends.add(friend); }

    public ArrayList<String> getFriends() { return this.friends; }
}
