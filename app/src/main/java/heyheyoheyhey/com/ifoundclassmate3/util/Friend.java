package heyheyoheyhey.com.ifoundclassmate3.util;

import android.os.Parcel;
import android.os.Parcelable;

import heyheyoheyhey.com.ifoundclassmate3.ScheduleItem;

/**
 * Created by vera on 15-3-27.
 */
public class Friend implements Parcelable {
    public String id;

    public Friend(String id){
        this.id = id;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
    }

    public String getId(){
        return id;
    }
    public static final Parcelable.Creator<Friend> CREATOR = new Parcelable.Creator<Friend>() {
        public Friend createFromParcel(Parcel in) {
            return new Friend(in);
        }
        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };

    public Friend(Parcel in) {
        this.id = in.readString();
    }
}
