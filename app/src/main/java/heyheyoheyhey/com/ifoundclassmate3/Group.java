package heyheyoheyhey.com.ifoundclassmate3;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Av23 on 2015-02-21.
 */
public class Group implements Parcelable {
    private String id;
    private String description;
    private ArrayList<String> users;

    //Parcelable functions
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.id);
        out.writeString(this.description);
        out.writeList(users);

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
    }

    public Group(String id, String description) {
        this.id = id;
        this.description = description;
        this.users = new ArrayList<String>();
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
}
