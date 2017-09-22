package com.coders.fuerzamexico.models;

import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by usuario on 21/09/17.
 */

public class ClusterMarker implements ClusterItem {
    private LatLng location;
    private String someID;
    private Bitmap icon;
    private int status;

    public ClusterMarker(LatLng location, String someID, Bitmap icon, int status) {
        this.location = location;
        this.someID = someID;
        this.icon = icon;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LatLng getLocation() {
        return location;
    }

    public String getSomeID() {
        return someID;
    }

    public void setSomeID(String someID) {
        this.someID = someID;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    @Override
    public LatLng getPosition() {
        return location;
    }
}
