package com.coders.fuerzamexico.steps;

import android.location.Address;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coders.fuerzamexico.R;
import com.github.fcannizzaro.materialstepper.AbstractStep;

/**
 * Created by usuario on 21/09/17.
 */

public class InfoStep extends AbstractStep {

    @Override
    public String name() {
        return "Datos del lugar";
    }

    @Override
    public boolean isOptional() {
        return false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step_info, container, false);

        Address address = getArguments().getParcelable("ADDRESS");
        ((TextView)v.findViewById(R.id.lbCP)).setText(address.getPostalCode());
        ((TextView)v.findViewById(R.id.lbState)).setText(address.getAdminArea());
        ((TextView)v.findViewById(R.id.lbCity)).setText(address.getSubAdminArea());
        ((TextView)v.findViewById(R.id.lbAddress)).setText(address.getAddressLine(0));

        mStepper.getExtras().putParcelable("ADDRESS", address);

        return v;
    }

}
