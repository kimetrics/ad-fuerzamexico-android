package com.coders.fuerzamexico.steps.singleadapter;

/**
 * Created by usuario on 21/09/17.
 */

public class SingleItem {
    private int id;
    private int icon;
    private String title;
    private boolean selected;

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
}
