package com.coders.fuerzamexico.steps.incidence;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

/**
 * Created by usuario on 21/09/17.
 */

public class IncidenceItem implements Parcelable {
    private @DrawableRes int icon;
    private String title;
    private String description;
    private boolean selected;

    protected IncidenceItem(Parcel in) {
        icon = in.readInt();
        title = in.readString();
        description = in.readString();
        selected = in.readByte() != 0;
    }

    public static final Creator<IncidenceItem> CREATOR = new Creator<IncidenceItem>() {
        @Override
        public IncidenceItem createFromParcel(Parcel in) {
            return new IncidenceItem(in);
        }

        @Override
        public IncidenceItem[] newArray(int size) {
            return new IncidenceItem[size];
        }
    };

    public int getIcon() {
        return icon;
    }

    public void setIcon(@DrawableRes int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public IncidenceItem(int icon, String title, String description, boolean selected) {
        this.icon = icon;
        this.title = title;
        this.description = description;
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(icon);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeByte((byte) (selected ? 1 : 0));
    }
}
