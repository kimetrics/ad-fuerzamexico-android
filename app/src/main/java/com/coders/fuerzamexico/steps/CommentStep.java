package com.coders.fuerzamexico.steps;

import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.coders.fuerzamexico.R;
import com.coders.fuerzamexico.steps.incidence.IncidenceItem;
import com.coders.fuerzamexico.steps.singleadapter.SingleItem;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

/**
 * Created by usuario on 21/09/17.
 */

public class CommentStep extends AbstractStep{
    @Override
    public String name() {
        return "Comentarios";
    }

    @Override
    public boolean isOptional() {
        return true;
    }

    private EditText txtComments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step_comments, container, false);
        txtComments = v.findViewById(R.id.txtComments);
        if(getArguments().containsKey("UUID")){
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference damageReference = database.getReference("reports")
                    .child(getArguments().getString("UUID")).child("form").child("comments");
            damageReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String comments = dataSnapshot.getValue().toString();
                    txtComments.setText(comments);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        return v;
    }

    @Override
    public void onNext() {
        super.onNext();
        String comments = txtComments.getText().toString();
        ArrayList<SingleItem> damageSelections = mStepper.getExtras().getParcelableArrayList("DAMAGE_SELECTIONS");
        int numHab = mStepper.getExtras().getInt("NUM_HAB");
        int numVic = mStepper.getExtras().getInt("NUM_VIC");
        int numDead = mStepper.getExtras().getInt("NUM_DEAD");
        ArrayList<IncidenceItem> incidences = mStepper.getExtras().getParcelableArrayList("INCIDENCES");
        int status = mStepper.getExtras().getInt("STATUS");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reportsRef = database.getReference("reports");
        String uuid = UUID.randomUUID().toString();

        if(getArguments().containsKey("UUID")){
            uuid = getArguments().getString("UUID");
        }

        Location location = null;

        if(getArguments().containsKey("LOCATION")){
            location = getArguments().getParcelable("LOCATION");
        }

        if(mStepper.getExtras().containsKey("ADDRESS")) {
            Address addressInfo = mStepper.getExtras().getParcelable("ADDRESS");
            Location l = new Location("");
            l.setLatitude(addressInfo.getLatitude());
            l.setLongitude(addressInfo.getLongitude());
        }

        GeoFire geoFire = new GeoFire(reportsRef);
        geoFire.setLocation(uuid, new GeoLocation(location.getLatitude(), location.getLongitude()));

        reportsRef = reportsRef.child(uuid);

        if(mStepper.getExtras().containsKey("ADDRESS")) {
            Address addressInfo = mStepper.getExtras().getParcelable("ADDRESS");
            DatabaseReference addressRef = reportsRef.child("address");
            addressRef.child("state").setValue(addressInfo.getAdminArea());
            addressRef.child("city").setValue(addressInfo.getSubAdminArea());
            addressRef.child("address_name").setValue(addressInfo.getAddressLine(0));
            addressRef.child("cp").setValue(addressInfo.getPostalCode());
        }

        if(mStepper.getExtras().containsKey("NAME")) {
            String name = mStepper.getExtras().getString("NAME");
            String email = mStepper.getExtras().getString("EMAIL");
            String phone = mStepper.getExtras().getString("PHONE");

            DatabaseReference userRef = reportsRef.child("reported_by");
            userRef.child("name").setValue(name);
            userRef.child("email").setValue(email);
            userRef.child("phone").setValue(phone);
        }

        reportsRef.child("time").setValue(new DateTime().toString());
        reportsRef.child("status").setValue(status);

        DatabaseReference formRef = reportsRef.child("form");

        Iterator<SingleItem> damages = damageSelections.iterator();
        while (damages.hasNext()){
            SingleItem item = damages.next();
            formRef.child("damage").child(item.getTitle()).setValue(item.isSelected());
        }

        Iterator<IncidenceItem> iterator = incidences.iterator();
        while (iterator.hasNext()){
            IncidenceItem incidenceItem = iterator.next();
            formRef.child("incident").child(incidenceItem.getTitle()).setValue(incidenceItem.isSelected());
        }

        formRef.child("victims").setValue(numVic);
        formRef.child("dead").setValue(numDead);
        formRef.child("hab").setValue(numHab);

        formRef.child("comments").setValue(comments);

        getActivity().setResult(10001);
        getActivity().finish();
    }
}
