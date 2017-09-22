package com.coders.fuerzamexico;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coders.fuerzamexico.steps.incidence.IncidenceAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by javier on 22/09/17.
 */

public class IncidenceDialogAdapter extends RecyclerView.Adapter<IncidenceDialogAdapter.IncidenceDialogHolder>{

    private Context context;
    private ArrayList<IncidenceDialogItem> items;

    public IncidenceDialogAdapter(Context context, ArrayList<IncidenceDialogItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public IncidenceDialogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dialog_incidence, parent, false);
        return new IncidenceDialogHolder(itemView);
    }

    @Override
    public void onBindViewHolder(IncidenceDialogHolder holder, int position) {
        IncidenceDialogItem item = items.get(position);
        Picasso.with(context).load(item.getIcon()).into(holder.imgIncidence);
        holder.lbTitle.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class IncidenceDialogHolder extends RecyclerView.ViewHolder{

        private ImageView imgIncidence;
        private TextView lbTitle;

        public IncidenceDialogHolder(View itemView) {
            super(itemView);
            lbTitle = itemView.findViewById(R.id.lbTitle);
            imgIncidence = itemView.findViewById(R.id.imgIncidence);
        }
    }
}
