package com.coders.fuerzamexico.steps;

import android.location.Address;
import android.os.Bundle;
import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.DotStepper;

/**
 * Created by usuario on 21/09/17.
 */

public class RootStepper extends DotStepper {

    private Address address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Formulario");

        Bundle b = getIntent().getExtras();
        address = b.getParcelable("ADDRESS");

        addStep(createFragment(new InfoStep()));
        addStep(createFragment(new UserStep()));
        addStep(createFragment(new DamageStep()));
        addStep(createFragment(new IncidenceStep()));
        addStep(createFragment(new StatusStep()));
        addStep(createFragment(new CommentStep()));

        super.onCreate(savedInstanceState);
    }

    private AbstractStep createFragment(AbstractStep fragment) {
        Bundle b = new Bundle();
        b.putParcelable("ADDRESS", address);
        fragment.setArguments(b);
        return fragment;
    }

}
