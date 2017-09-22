package com.coders.fuerzamexico.steps;

import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.coders.fuerzamexico.MainActivity;
import com.coders.fuerzamexico.R;
import com.coders.fuerzamexico.steps.incidence.IncidenceItem;
import com.coders.fuerzamexico.steps.singleadapter.SingleItem;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.DateTime;

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
        Address addressInfo = mStepper.getExtras().getParcelable("ADDRESS");

        String name = mStepper.getExtras().getString("NAME");
        String email = mStepper.getExtras().getString("EMAIL");
        String phone = mStepper.getExtras().getString("PHONE");


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reportsRef = database.getReference("reports");
        String uuid = UUID.randomUUID().toString();

        GeoFire geoFire = new GeoFire(reportsRef);
        geoFire.setLocation(uuid,
                new GeoLocation(addressInfo.getLatitude(), addressInfo.getLongitude()));

        reportsRef = reportsRef.child(uuid);

        DatabaseReference addressRef = reportsRef.child("address");
        addressRef.child("state").setValue(addressInfo.getAdminArea());
        addressRef.child("city").setValue(addressInfo.getSubAdminArea());
        addressRef.child("address_name").setValue(addressInfo.getAddressLine(0));
        addressRef.child("cp").setValue(addressInfo.getPostalCode());

        DatabaseReference userRef = reportsRef.child("reported_by");
        userRef.child("name").setValue(name);
        userRef.child("email").setValue(email);
        userRef.child("phone").setValue(phone);

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
            formRef.child(incidenceItem.getTitle()).setValue(incidenceItem.isSelected());
        }

        formRef.child("victims").setValue(numVic);
        formRef.child("dead").setValue(numDead);
        formRef.child("hab").setValue(numHab);

        formRef.child("comments").setValue(comments);

        getActivity().setResult(10001);
        getActivity().finish();
    }
}
