package com.coders.fuerzamexico;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.coders.fuerzamexico.steps.IncidenceStep;
import com.coders.fuerzamexico.steps.incidence.IncidenceAdapter;
import com.coders.fuerzamexico.steps.incidence.IncidenceItem;
import com.coders.fuerzamexico.steps.incidence.OnIncidenceClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class IconsActivity extends AppCompatActivity {

    private RecyclerView iconList;
    private ArrayList<IncidenceItem> items;
    private IncidenceAdapter adapter;
    private ImageView btClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icons);
        iconList = (RecyclerView)findViewById(R.id.iconList);
        iconList.setLayoutManager(new LinearLayoutManager(this));
        btClose = (ImageView)findViewById(R.id.btClose);
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loadList();
    }

    private void loadList(){
        items = IncidenceStep.getIncidences();
        adapter = new IncidenceAdapter(this, items, null);
        iconList.setAdapter(adapter);
    }
}
