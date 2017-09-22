package com.coders.fuerzamexico.steps.singleadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coders.fuerzamexico.R;

import java.util.ArrayList;

/**
 * Created by usuario on 21/09/17.
 */

public class SingleItemAdapter extends RecyclerView.Adapter<SingleItemAdapter.SingleHolder>{

    private Context context;
    private ArrayList<SingleItem> items;
    private OnSingleItemListener listener;

    public SingleItemAdapter(Context context, ArrayList<SingleItem> items, OnSingleItemListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @Override
    public SingleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_item, parent, false);
        return new SingleHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SingleHolder holder, int position) {
        final SingleItem item = items.get(position);
        holder.lbTitle.setText(item.getTitle());
        if(item.isSelected()){
            holder.checkButton.setVisibility(View.VISIBLE);
        }else{
            holder.checkButton.setVisibility(View.GONE);
        }
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

    class SingleHolder extends RecyclerView.ViewHolder{

        private TextView lbTitle;
        private ImageView checkButton;
        private LinearLayout mainContainer;

        public SingleHolder(View itemView) {
            super(itemView);
            checkButton = itemView.findViewById(R.id.checkButton);
            lbTitle = itemView.findViewById(R.id.lbTitle);
            mainContainer = itemView.findViewById(R.id.mainContainer);
        }
    }
}
