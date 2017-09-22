package com.coders.fuerzamexico.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by javier on 22/09/17.
 */

public class MarkerRenderer extends DefaultClusterRenderer<ClusterMarker> {
    public MarkerRenderer(Context context, GoogleMap map, ClusterManager<ClusterMarker> clusterManager) {
        super(context, map, clusterManager);
    }
    @Override
    protected void onClusterItemRendered(ClusterMarker clusterItem,
                                         Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);
        if(clusterItem.getIcon() != null) {
            BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(clusterItem.getIcon());
            marker.setIcon(descriptor);
        }

        switch (clusterItem.getStatus()){
            case 1:
                // SUCCESS
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                break;
            case 2:
                // IN PROCESS
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                break;
            case 3:
                // DAMAGE
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                break;
        }
    }
}
