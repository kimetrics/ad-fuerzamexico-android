package com.coders.fuerzamexico.steps;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.coders.fuerzamexico.R;
import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by usuario on 21/09/17.
 */

public class StatusStep extends AbstractStep {
    @Override
    public String name() {
        return "Status";
    }

    @Override
    public boolean isOptional() {
        return false;
    }

    private LinearLayout btStatusSuccess;
    private LinearLayout btStatusInProcess;
    private LinearLayout btStatusIncomplete;

    private ImageView imgStatusSuccess;
    private ImageView imgStatusInProcess;
    private ImageView imgStatusIncomplete;

    private int statusSelected = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step_status, container, false);

        btStatusSuccess = v.findViewById(R.id.btStatusSuccess);
        btStatusInProcess = v.findViewById(R.id.btStatusInProcess);
        btStatusIncomplete = v.findViewById(R.id.btStatusIncomplete);

        imgStatusSuccess = v.findViewById(R.id.imgStatusSuccess);
        imgStatusInProcess = v.findViewById(R.id.imgStatusInProcess);
        imgStatusIncomplete = v.findViewById(R.id.imgStatusIncomplete);

        btStatusSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSuccess();
            }
        });

        btStatusInProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInProcess();
            }
        });

        btStatusIncomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           checkIncomplete();
            }
        });

        if(getArguments().containsKey("UUID")){
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference damageReference = database.getReference("reports")
                    .child(getArguments().getString("UUID")).child("status");
            damageReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long status = (long) dataSnapshot.getValue();
                    switch ((int)status){
                        case 1:
                            checkSuccess();
                            break;
                        case 2:
                            checkInProcess();
                            break;
                        case 3:
                            checkIncomplete();
                            break;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        return v;
    }

    private void checkIncomplete(){
        statusSelected = 3;
        Picasso.with(getActivity()).load(R.drawable.error_enabled).into(imgStatusIncomplete);
        Picasso.with(getActivity()).load(R.drawable.success_disabled).into(imgStatusSuccess);
        Picasso.with(getActivity()).load(R.drawable.engineer_disabled).into(imgStatusInProcess);
        imgStatusIncomplete.setColorFilter(0);
        imgStatusSuccess.setColorFilter(getResources().getColor(R.color.colorDisabled));
        imgStatusInProcess.setColorFilter(getResources().getColor(R.color.colorDisabled));
    }

    private void checkInProcess(){
        statusSelected = 2;
        Picasso.with(getActivity()).load(R.drawable.success_disabled).into(imgStatusSuccess);
        Picasso.with(getActivity()).load(R.drawable.engineer_enabled).into(imgStatusInProcess);
        Picasso.with(getActivity()).load(R.drawable.error_disabled).into(imgStatusIncomplete);
        imgStatusInProcess.setColorFilter(0);
        imgStatusSuccess.setColorFilter(getResources().getColor(R.color.colorDisabled));
        imgStatusIncomplete.setColorFilter(getResources().getColor(R.color.colorDisabled));
    }

    private void checkSuccess(){
        statusSelected = 1;
        Picasso.with(getActivity()).load(R.drawable.success_enabled).into(imgStatusSuccess);
        Picasso.with(getActivity()).load(R.drawable.engineer_disabled).into(imgStatusInProcess);
        Picasso.with(getActivity()).load(R.drawable.error_disabled).into(imgStatusIncomplete);
        imgStatusSuccess.setColorFilter(0);
        imgStatusInProcess.setColorFilter(getResources().getColor(R.color.colorDisabled));
        imgStatusIncomplete.setColorFilter(getResources().getColor(R.color.colorDisabled));
    }

    @Override
    public void onNext() {
        super.onNext();
        mStepper.getExtras().putInt("STATUS", statusSelected);
    }
}
