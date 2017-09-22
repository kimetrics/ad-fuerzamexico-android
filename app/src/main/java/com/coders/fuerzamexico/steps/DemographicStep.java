package com.coders.fuerzamexico.steps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.coders.fuerzamexico.R;
import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by usuario on 21/09/17.
 */

public class DemographicStep extends AbstractStep{
    @Override
    public String name() {
        return "Datos demográficos";
    }

    @Override
    public boolean isOptional() {
        return true;
    }

    private EditText txtNumHabitants;
    private EditText txtNumVictims;
    private EditText txtNumDead;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step_demographic, container, false);
        txtNumHabitants = v.findViewById(R.id.txtNumHabitants);
        txtNumHabitants.setVisibility(View.GONE);
        txtNumVictims = v.findViewById(R.id.txtNumVictims);
        txtNumDead = v.findViewById(R.id.txtNumDead);

        if(getArguments().containsKey("UUID")){
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference damageReference = database.getReference("reports")
                    .child(getArguments().getString("UUID")).child("form");

            damageReference.child("dead").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    txtNumDead.setText(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            damageReference.child("hab").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    txtNumHabitants.setText(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            damageReference.child("victims").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    txtNumVictims.setText(dataSnapshot.getValue().toString());
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
        int numHab = 0;
        if(!txtNumHabitants.getText().toString().isEmpty()) {
            numHab = Integer.parseInt(txtNumHabitants.getText().toString());
        }

        int numVic = 0;
        if(!txtNumVictims.getText().toString().isEmpty()) {
            numVic = Integer.parseInt(txtNumVictims.getText().toString());
        }

        int numDead = 0;
        if(!txtNumDead.getText().toString().isEmpty()) {
            numDead = Integer.parseInt(txtNumDead.getText().toString());
        }

        mStepper.getExtras().putInt("NUM_HAB", numHab);
        mStepper.getExtras().putInt("NUM_VIC", numVic);
        mStepper.getExtras().putInt("NUM_DEAD", numDead);
    }
}
