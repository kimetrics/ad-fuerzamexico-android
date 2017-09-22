package com.coders.fuerzamexico.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mich on 9/21/17.
 */

public class Report<V, K> {
    public String topic;
    public String time;
    public long status;
    public LatLng latLng;
    public Map<V, K> reportedBy;
    public Map<V, K> address;
    public Map<V, K> form;


    public Report(HashMap dataMap) throws ClassCastException, NullPointerException{
        topic = (String) dataMap.get("type");
        time = (String) dataMap.get("topic");
        status = (Long) dataMap.get("status");
        reportedBy = (HashMap) dataMap.get("reported_by");
        address = (HashMap) dataMap.get("address");
        form = (HashMap) dataMap.get("form");
        ArrayList<Double> geo = (ArrayList) dataMap.get("l");
        latLng = new LatLng(geo.get(0), geo.get(1));
    }

    public LatLng getGeo(){
        return latLng;
    }

    public String getName() {
        if (topic != null) {
            return topic;
        }
        return "";
    }
}
