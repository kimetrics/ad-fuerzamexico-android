package com.coders.fuerzamexico.steps.singleadapter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by usuario on 21/09/17.
 */

public class SingleItem implements Parcelable{
    private int id;
    private int icon;
    private String title;
    private boolean selected;

    protected SingleItem(Parcel in) {
        id = in.readInt();
        icon = in.readInt();
        title = in.readString();
        selected = in.readByte() != 0;
    }

    public static final Creator<SingleItem> CREATOR = new Creator<SingleItem>() {
        @Override
        public SingleItem createFromParcel(Parcel in) {
            return new SingleItem(in);
        }

        @Override
        public SingleItem[] newArray(int size) {
            return new SingleItem[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public SingleItem(int id, int icon, String title, boolean selected) {
        this.id = id;
        this.icon = icon;
        this.title = title;
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(icon);
        parcel.writeString(title);
        parcel.writeByte((byte) (selected ? 1 : 0));
    }
}
