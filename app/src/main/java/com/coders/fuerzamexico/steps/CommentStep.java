package com.coders.fuerzamexico.steps;

import android.location.Address;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.coders.fuerzamexico.R;
import com.coders.fuerzamexico.steps.incidence.IncidenceItem;
import com.github.fcannizzaro.materialstepper.AbstractStep;

import java.util.ArrayList;

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
        ArrayList<Integer> damageSelections = mStepper.getExtras().getIntegerArrayList("DAMAGE_SELECTIONS");
        int numHab = mStepper.getExtras().getInt("NUM_HAB");
        int numVic = mStepper.getExtras().getInt("NUM_VIC");
        int numDead = mStepper.getExtras().getInt("NUM_DEAD");
        ArrayList<IncidenceItem> incidences = mStepper.getExtras().getParcelableArrayList("INCIDENCES");
        int status = mStepper.getExtras().getInt("STATUS");
        Address addressInfo = mStepper.getExtras().getParcelable("ADDRESS");

        String name = mStepper.getExtras().getString("NAME");
        String email = mStepper.getExtras().getString("EMAIL");
        String phone = mStepper.getExtras().getString("PHONE");
        // TODO: Send info to Firebase
        Toast.makeText(mStepper, name, Toast.LENGTH_SHORT).show();

        getActivity().finish();
    }
}
