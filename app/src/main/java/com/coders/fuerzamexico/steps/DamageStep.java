package com.coders.fuerzamexico.steps;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coders.fuerzamexico.R;
import com.coders.fuerzamexico.steps.incidence.IncidenceItem;
import com.coders.fuerzamexico.steps.singleadapter.OnSingleItemListener;
import com.coders.fuerzamexico.steps.singleadapter.SingleItem;
import com.coders.fuerzamexico.steps.singleadapter.SingleItemAdapter;
import com.github.fcannizzaro.materialstepper.AbstractStep;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by usuario on 21/09/17.
 */

public class DamageStep extends AbstractStep {
    @Override
    public String name() {
        return "Tipo de daño";
    }

    @Override
    public boolean isOptional() {
        return true;
    }

    private ArrayList<SingleItem> items;
    private SingleItemAdapter adapter;
    private RecyclerView damageList;
    private ArrayList<Integer> damageSelections = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step_damage, container, false);
        damageList = v.findViewById(R.id.damageList);
        damageList.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadData();
        return v;
    }

    private void loadData(){
        items = new ArrayList<>();
        SingleItem i1 = new SingleItem(1, 0, "Hay derrumbes", false);
        SingleItem i2 = new SingleItem(2, 0, "Daños casa-habitación", false);
        SingleItem i3 = new SingleItem(3, 0, "Daños en vía pública", false);

        items.add(i1);
        items.add(i2);
        items.add(i3);

        adapter = new SingleItemAdapter(getActivity(), items, new OnSingleItemListener() {
            @Override
            public void onClick(SingleItem item) {
                Iterator<SingleItem> iterator = items.iterator();
                while (iterator.hasNext()){
                    SingleItem mItem = iterator.next();
                    if(item.getTitle().equals(mItem.getTitle())){
                        mItem.setSelected(!mItem.isSelected());
                        if(mItem.isSelected()){
                            if(!damageSelections.contains(mItem.getId())){
                                damageSelections.add(mItem.getId());
                            }
                        }else{
                            damageSelections.remove(mItem.getIcon());
                        }

                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        damageList.setAdapter(adapter);
    }

    @Override
    public void onNext() {
        super.onNext();
        mStepper.getExtras().putIntegerArrayList("DAMAGE_SELECTIONS", damageSelections);
    }
}
