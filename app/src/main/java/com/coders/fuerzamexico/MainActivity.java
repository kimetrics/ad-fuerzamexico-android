package com.coders.fuerzamexico;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.coders.fuerzamexico.steps.RootStepper;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private TextView lbAddress;
    private TextView btContinue;
    private TextView btGoToMyLocation;
    private Location currentLocation;
    private boolean isAnimated;
    private Address currentAddress;
    private PlaceAutocompleteFragment autocompleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lbAddress = (TextView) findViewById(R.id.lbAddress);
        btContinue = (TextView) findViewById(R.id.btContinue);
        btGoToMyLocation = (TextView)findViewById(R.id.btGoToMyLocation);

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        mapFragment = (SupportMapFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.mapview);


        loadMap();

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Location location = new Location("");
                location.setLatitude(place.getLatLng().latitude);
                location.setLongitude(place.getLatLng().longitude);
                currentLocation = location;
            }

            @Override
            public void onError(Status status) {

            }
        });

        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentAddress != null){
                    startActivityForResult(new Intent(getApplicationContext(),
                            RootStepper.class).putExtra("ADDRESS", currentAddress)
                            .putExtra("LOCATION", currentLocation), 10001);
                }else{
                    Toast.makeText(MainActivity.this, "Selecciona la ubicación del reporte", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btGoToMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentLocation != null){
                    goToMyLocation(currentLocation);
                }
            }
        });
    }

    private void loadMap(){
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                checkPermissions();

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    return;
                }



                map = googleMap;
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(false);

                map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {
                        currentLocation = location;
                        if(!isAnimated) {
                            goToMyLocation(location);
                            isAnimated = true;
                        }
                    }
                });

                map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        getAddressesFromLocation(new LatLng(map.getCameraPosition().target.latitude,
                                map.getCameraPosition().target.longitude), 1, new OnAddressListener() {
                            @Override
                            public void onAddresses(final List<Address> addresses) {
                                if(addresses.size() > 0){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Address address = addresses.get(0);
                                                currentAddress = address;
                                                String mAddress = address.getAddressLine(0);
                                                autocompleteFragment.setText(mAddress);
                                                lbAddress.setText(mAddress);
                                            }catch (NullPointerException ne){

                                            }
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailed(String error) {

                            }
                        });
                    }
                });
            }
        });
    }

    private void goToMyLocation(Location location){
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                location.getLongitude()), 20.0f);
        map.animateCamera(cu);
    }

    interface OnAddressListener {
        void onAddresses(List<Address> address);
        void onFailed(String error);
    }

    /**
     * Get Addresses from custom location.
     *
     * @param latLng
     * @return
     */
    public void getAddressesFromLocation(final LatLng latLng, final int maxResults, final OnAddressListener listener){


        Tasks.executeInBackground(this, new BackgroundWork<List<Address>>() {
            @Override
            public List<Address> doInBackground() throws Exception {
                Geocoder geocoder = new Geocoder(getApplicationContext());
                return geocoder.getFromLocation(latLng.latitude, latLng.longitude, maxResults);
            }
        }, new Completion<List<Address>>() {
            @Override
            public void onSuccess(Context context, List<Address> result) {
                if(listener != null){
                    listener.onAddresses(result);
                }
            }
            @Override
            public void onError(Context context, Exception e) {
                if(listener != null){
                    listener.onFailed(e.getMessage());
                }
            }
        });
    }

    private void checkPermissions(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        100);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 10001 && requestCode == 10001){
            finish();
        }
    }
}
