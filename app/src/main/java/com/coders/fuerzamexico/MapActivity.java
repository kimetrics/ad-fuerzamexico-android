package com.coders.fuerzamexico;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.coders.fuerzamexico.models.ClusterMarker;
import com.coders.fuerzamexico.models.Report;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mich on 9/21/17.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    final List<Report> reports = Collections.synchronizedList(new ArrayList<Report>());
    final List<Report> reportsPendingToAdd = Collections.synchronizedList(new ArrayList<Report>());
    private Location currentLocation;
    private boolean isAnimated;
    boolean mapReady;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        mapFragment.getMapAsync(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap = googleMap;
        mapReady = true;
        mClusterManager = new ClusterManager<ClusterMarker>(this, mMap);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if(!isAnimated) {
                    goToMyLocation(location);
                }

                currentLocation = location;
                isAnimated = true;
            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                Location l = new Location("");
                l.setLatitude(mMap.getCameraPosition().target.latitude);
                l.setLongitude(mMap.getCameraPosition().target.longitude);
                currentLocation = l;
                queryGeo();
                mClusterManager.onCameraIdle();
            }
        });

        loadMarkers();
    }

    private void goToMyLocation(Location location){
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                location.getLongitude()), 14.0f);
        mMap.animateCamera(cu);
    }

    private ArrayList<String> markers = new ArrayList<>();
    ClusterManager<ClusterMarker> mClusterManager;

    private void addMarker(Report report, String uuid){
        if(mapReady){

            if(!markers.contains(uuid)) {
                final ClusterMarker clusterMarker =
                        new ClusterMarker(report.getGeo().latitude, report.getGeo().longitude, uuid);
                mClusterManager.addItem(clusterMarker);
                markers.add(uuid);
                Log.e("SIZE", String.valueOf(markers.size()));
            }

            mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<ClusterMarker>() {
                @Override
                public boolean onClusterItemClick(ClusterMarker clusterMarker) {
                    String mUUID = clusterMarker.getSomeID();

                    return true;
                }
            });

            mClusterManager.cluster();

        }else {
            reportsPendingToAdd.add(report);
        }
    }


    private void queryGeo(){
        if(currentLocation != null) {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference reportsRef = database.getReference("reports");
            GeoFire geoFire = new GeoFire(reportsRef);
            GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(currentLocation.getLatitude(),
                    currentLocation.getLongitude()), 1);

            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(final String key, GeoLocation location) {
                    System.out.println(String.format("Key %s entered the search area at [%f,%f]",
                            key, location.latitude, location.longitude));

                    reportsRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            HashMap dataMap = (HashMap) dataSnapshot.getValue();
                            try{
                                Report report = new Report(dataMap);
                                reports.add(report);
                                addMarker(report, key);
                            }catch (ClassCastException e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onKeyExited(String key) {
                    System.out.println(String.format("Key %s is no longer in the search area", key));
                }

                @Override
                public void onKeyMoved(String key, GeoLocation location) {
                    System.out.println(String.format("Key %s moved within the search area to [%f,%f]", key, location.latitude, location.longitude));
                }

                @Override
                public void onGeoQueryReady() {
                    System.out.println("All initial data has been loaded and events have been fired!");
                }

                @Override
                public void onGeoQueryError(DatabaseError error) {
                    System.err.println("There was an error with this query: " + error);
                }
            });
        }

    }

    private void loadMarkers(){
        if(mapReady){
            synchronized (reportsPendingToAdd) {
                Iterator<Report> reportIterator = reportsPendingToAdd.iterator();
                while (reportIterator.hasNext()) {
                    Report report = reportIterator.next();
                    mMap.addMarker(new MarkerOptions().position(report.getGeo()).title(report.getName()));
                    Log.e("MARKER:", report.getGeo().toString());
                    reportIterator.remove();
                }
            }
        }
    }

}


