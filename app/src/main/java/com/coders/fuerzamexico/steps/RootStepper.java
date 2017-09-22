package com.coders.fuerzamexico.steps;

import android.location.Address;
import android.os.Bundle;
import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.DotStepper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by usuario on 21/09/17.
 */

public class RootStepper extends DotStepper {

    private Address address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Formulario");

        Bundle b = getIntent().getExtras();
        if(!b.containsKey("UUID")) {
            addStep(createFragment(new InfoStep()));
            addStep(createFragment(new UserStep()));
        }

        addStep(createFragment(new DamageStep()));
        addStep(createFragment(new IncidenceStep()));
        addStep(createFragment(new DemographicStep()));
        addStep(createFragment(new StatusStep()));
        addStep(createFragment(new CommentStep()));

        super.onCreate(savedInstanceState);
    }

    private AbstractStep createFragment(AbstractStep fragment) {
        Bundle b = new Bundle();
        Bundle b1 = getIntent().getExtras();
        if(b1.containsKey("ADDRESS")) {
            b.putParcelable("ADDRESS", b1.getParcelable("ADDRESS"));
        }

        if(b1.containsKey("UUID")){
            b.putString("UUID", b1.getString("UUID"));
        }

        fragment.setArguments(b);
        return fragment;
    }

}
