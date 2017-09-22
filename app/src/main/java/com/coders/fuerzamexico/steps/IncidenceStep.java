package com.coders.fuerzamexico.steps;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coders.fuerzamexico.R;
import com.coders.fuerzamexico.steps.incidence.IncidenceAdapter;
import com.coders.fuerzamexico.steps.incidence.IncidenceItem;
import com.coders.fuerzamexico.steps.incidence.OnIncidenceClickListener;
import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by usuario on 21/09/17.
 */

public class IncidenceStep extends AbstractStep {
    @Override
    public String name() {
        return "Necesidad";
    }

    @Override
    public boolean isOptional() {
        return false;
    }

    private RecyclerView incidenceList;
    private ArrayList<IncidenceItem> items;
    private IncidenceAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step_incidence, container, false);
        incidenceList = v.findViewById(R.id.incidenceList);
        incidenceList.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadList();
        return v;
    }

    private void loadList(){
        items = new ArrayList<>();

        items.add(new IncidenceItem(R.drawable.hold, "Voluntarios",
                "Se necesitan manos para ayudar", false));

        items.add(new IncidenceItem(R.drawable.light, "Energía eléctrica",
                "Ayuda para que se reestablezca la energía eléctrica", false));

        items.add(new IncidenceItem(R.drawable.meat, "Víveres",
                "Necesidad de alimentos, productos de higiene, etc.", false));

        items.add(new IncidenceItem(R.drawable.emergency_kit, "Medicamentos",
                "Medicinas como Alcohol, Oxígeno, Dropamina, etc.", false));

        items.add(new IncidenceItem(R.drawable.heart, "Rescate",
                "Gente atrapada entre escombros", false));

        items.add(new IncidenceItem(R.drawable.tools, "Herramientas",
                "Herramientas y material de construcción", false));
        items.add(new IncidenceItem(R.drawable.loader, "Maquinaria",
                "Necesidad de remover grandes cantidades de escombros", false));

        items.add(new IncidenceItem(R.drawable.bus, "Transporte",
                "Transportar personas", false));

        if(getArguments().containsKey("UUID")){
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference damageReference = database.getReference("reports")
                    .child(getArguments().getString("UUID")).child("form").child("incident");
            damageReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String, Boolean> data = (HashMap<String, Boolean>) dataSnapshot.getValue();
                    Iterator<String> iterator = data.keySet().iterator();
                    while (iterator.hasNext()){
                        String title = iterator.next();
                        boolean isSelected = data.get(title);

                        Iterator<IncidenceItem> mIterator = items.iterator();
                        while (mIterator.hasNext()){
                            IncidenceItem mItem = mIterator.next();
                            if(title.equals(mItem.getTitle())){
                                mItem.setSelected(isSelected);
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        adapter = new IncidenceAdapter(getActivity(), items, new OnIncidenceClickListener() {
            @Override
            public void onClick(IncidenceItem item) {
                Iterator<IncidenceItem> iterator = items.iterator();
                while (iterator.hasNext()){
                    IncidenceItem mItem = iterator.next();
                    if(item.getTitle().equals(mItem.getTitle())){
                        mItem.setSelected(!mItem.isSelected());
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        incidenceList.setAdapter(adapter);
    }

    @Override
    public boolean nextIf() {
        Iterator<IncidenceItem> iterator = items.iterator();
        ArrayList<Boolean> selections = new ArrayList<>();
        while (iterator.hasNext()){
            IncidenceItem mItem = iterator.next();
            if(mItem.isSelected()){
                selections.add(true);
            }
        }

        boolean isValid = selections.size() > 0;
        return isValid;
    }

    @Override
    public void onNext() {
        super.onNext();
        mStepper.getExtras().putParcelableArrayList("INCIDENCES", items);
    }

    @Override
    public String error() {
        return "<b>Error</b> <small>Selecciona al menos una incidencia</small>";
    }
}
