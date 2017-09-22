package com.coders.fuerzamexico.steps.incidence;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coders.fuerzamexico.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by usuario on 21/09/17.
 */

public class IncidenceAdapter extends RecyclerView.Adapter<IncidenceAdapter.IncidenceHolder>{

    private Context context;
    private ArrayList<IncidenceItem> items;
    private OnIncidenceClickListener listener;

    public IncidenceAdapter(Context context, ArrayList<IncidenceItem> items, OnIncidenceClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }


    @Override
    public IncidenceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.incidence_item, parent, false);
        return new IncidenceHolder(itemView);
    }

    @Override
    public void onBindViewHolder(IncidenceHolder holder, int position) {
        final IncidenceItem item = items.get(position);
        holder.lbTitle.setText(item.getTitle());
        holder.lbDescription.setText(item.getDescription());

        if(item.isSelected()){
            holder.checkButton.setVisibility(View.VISIBLE);
        }else{
            holder.checkButton.setVisibility(View.GONE);
        }
        Picasso.with(context).load(item.getIcon()).into(holder.imgIcon);
        holder.mainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onClick(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class IncidenceHolder extends RecyclerView.ViewHolder{
        private TextView lbTitle;
        private TextView lbDescription;
        private LinearLayout mainContainer;
        private ImageView checkButton;
        private ImageView imgIcon;

        public IncidenceHolder(View itemView) {
            super(itemView);
            lbTitle = itemView.findViewById(R.id.lbTitle);
            lbDescription = itemView.findViewById(R.id.lbDescription);
            mainContainer = itemView.findViewById(R.id.mainContainer);
            checkButton = itemView.findViewById(R.id.checkButton);
            imgIcon = itemView.findViewById(R.id.imgIcon);
        }
    }
}
