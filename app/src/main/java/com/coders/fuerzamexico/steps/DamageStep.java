package com.coders.fuerzamexico.steps;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.coders.fuerzamexico.R;
import com.coders.fuerzamexico.steps.singleadapter.OnSingleItemListener;
import com.coders.fuerzamexico.steps.singleadapter.SingleItem;
import com.coders.fuerzamexico.steps.singleadapter.SingleItemAdapter;
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

        items.add(new SingleItem(1, 0, "Hay derrumbes", false));
        items.add(new SingleItem(2, 0, "Daños casa-habitación", false));
        items.add(new SingleItem(3, 0, "Daños en vía pública", false));

        if(getArguments().containsKey("UUID")){
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference damageReference = database.getReference("reports")
                    .child(getArguments().getString("UUID")).child("form").child("damage");
            damageReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                   HashMap<String, Boolean> data = (HashMap<String, Boolean>) dataSnapshot.getValue();
                    Iterator<String> iterator = data.keySet().iterator();
                    while (iterator.hasNext()){
                        String title = iterator.next();
                        boolean isSelected = data.get(title);
                        Iterator<SingleItem> itemIterator = items.iterator();
                        while (itemIterator.hasNext()){
                            SingleItem item = itemIterator.next();
                            if(title.equals(item.getTitle())){
                                item.setSelected(isSelected);
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(mStepper, databaseError.toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }

        adapter = new SingleItemAdapter(getActivity(), items, new OnSingleItemListener() {
            @Override
            public void onClick(SingleItem item) {
                Iterator<SingleItem> iterator = items.iterator();
                while (iterator.hasNext()){
                    SingleItem mItem = iterator.next();
                    if(item.getTitle().equals(mItem.getTitle())){
                        mItem.setSelected(!mItem.isSelected());
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
        mStepper.getExtras().putParcelableArrayList("DAMAGE_SELECTIONS", items);
    }
}
