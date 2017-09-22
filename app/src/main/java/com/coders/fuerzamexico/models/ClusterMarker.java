package com.coders.fuerzamexico.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by usuario on 21/09/17.
 */

public class ClusterMarker implements ClusterItem {
    private final LatLng mPosition;
    private String someID;

    public ClusterMarker(double lat, double lng, String id) {
        mPosition = new LatLng(lat, lng);
        someID = id;
    }

    public String getSomeID() {
        return someID;
    }

    public void setSomeID(String someID) {
        this.someID = someID;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}
