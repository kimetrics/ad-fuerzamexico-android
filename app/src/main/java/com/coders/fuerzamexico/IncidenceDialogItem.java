package com.coders.fuerzamexico;

/**
 * Created by javier on 22/09/17.
 */

public class IncidenceDialogItem {
    private int icon;
    private String title;

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

    public IncidenceDialogItem(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }
}
