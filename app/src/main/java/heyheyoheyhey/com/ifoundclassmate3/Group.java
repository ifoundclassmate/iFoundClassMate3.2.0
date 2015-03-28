package heyheyoheyhey.com.ifoundclassmate3;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Av23 on 2015-02-21.
 */
public class Group implements Parcelable {
    private String id;
    private String description;
    private ArrayList<String> users;
    private ArrayList<MeetingItem> meetings;

    //Parcelable functions
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.id);
        out.writeString(this.description);
        out.writeList(users);
        MeetingItem[] temp = new MeetingItem[meetings.size()];
        meetings.toArray(temp);
        out.writeParcelableArray(temp, 2);
    }

    public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    private Group(Parcel in) {
        this.id = in.readString();
        this.description = in.readString();
        this.users = new ArrayList<String>();
        this.users = (ArrayList<String>) in.readArrayList(String.class.getClassLoader());
        MeetingItem[] temp = (MeetingItem[]) in.readParcelableArray(MeetingItem.class.getClassLoader());
        this.meetings = new ArrayList<MeetingItem>(Arrays.asList(temp));
    }

    public Group(String id, String description) {
        this.id = id;
        this.description = description;
        this.users = new ArrayList<String>();
        this.meetings = new ArrayList<MeetingItem>();
    }

    public void addUser(String id) {
        if (!users.contains(id)) {
            this.users.add(id);
        }
    }

    public String getDescription() {
        return this.description;
    }

    public String getId() {
        return this.id;
    }

    public ArrayList<String> getUsers() {
        return this.users;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<MeetingItem> getMeetings() { return this.meetings; }
}
