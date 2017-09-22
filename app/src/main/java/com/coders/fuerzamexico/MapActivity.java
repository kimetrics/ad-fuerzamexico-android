package com.coders.fuerzamexico;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.coders.fuerzamexico.models.Report;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    final List<Report> reportsPendingToAdd = Collections.synchronizedList(new ArrayList<Report>());;
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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reportsRef = database.getReference("reports");

        reportsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                HashMap dataMap = (HashMap) dataSnapshot.getValue();
                Report report = new Report(dataMap);
                if(report.hasGeo()){
                    reports.add(report);
                    addMarker(report);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapReady = true;
        loadMarkers();
    }


    private void addMarker(Report report){
        if(mapReady){
            mMap.addMarker(new MarkerOptions().position(report.getGeo()).title(report.getName()));
        }else {
            reportsPendingToAdd.add(report);
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


