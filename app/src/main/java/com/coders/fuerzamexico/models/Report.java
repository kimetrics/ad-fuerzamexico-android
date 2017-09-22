package com.coders.fuerzamexico.models;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mich on 9/21/17.
 */

public class Report<V, K> {
    public String topic;
    public String time;
    public String status;
    public Map<V, K> reportedBy;
    public Map<V, K> address;
    public Map<V, K> form;


    public Report(HashMap dataMap){
        topic = (String) dataMap.get("type");
        time = (String) dataMap.get("topic");
        status = (String) dataMap.get("status");
        reportedBy = (HashMap) dataMap.get("reported_by");
        address = (HashMap) dataMap.get("address");
        form = (HashMap) dataMap.get("form");
    }

    public LatLng getGeo(){
        HashMap geo = (HashMap) address.get("geo");
        Double latitude = (Double) geo.get("lat");
        Double longitude = (Double) geo.get("lon");
        return new LatLng(latitude, longitude);
    }

    public String getName() {
        if (topic != null) {
            return topic;
        }
        return "";
    }

    public boolean hasGeo() {
        if(address != null){
            HashMap geo = (HashMap) address.get("geo");
            Double latitude = (Double) geo.get("lat");
            Double longitude = (Double) geo.get("lon");

            if(latitude != null && longitude != null){
                Log.e("GEO:", latitude.toString() + ", " + longitude.toString());
                return true;
            }
        }
        return false;
    }
}
