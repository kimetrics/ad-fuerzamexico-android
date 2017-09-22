package com.coders.fuerzamexico.steps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.coders.fuerzamexico.R;
import com.github.fcannizzaro.materialstepper.AbstractStep;

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
        txtNumVictims = v.findViewById(R.id.txtNumVictims);
        txtNumDead = v.findViewById(R.id.txtNumDead);
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

        getArguments().putInt("NUM_HAB", numHab);
        getArguments().putInt("NUM_VIC", numVic);
        getArguments().putInt("NUM_DEAD", numDead);
    }
}
