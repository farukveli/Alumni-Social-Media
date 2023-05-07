package com.example.app_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RCAdapter2 extends RecyclerView.Adapter<RCAdapter2.RCViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private Context context;
    private ArrayList<RCModel2> modelArrayList;
    private int selected_item =-1;
    public RCAdapter2(Context context, ArrayList<RCModel2> modelArrayList,RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.modelArrayList = modelArrayList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public RCViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rc_item2,parent,false);
        return new RCViewHolder(v,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RCViewHolder holder, int position) {
        RCModel2 rcModel = modelArrayList.get(position);
        holder.rc_title.setText(rcModel.getTitle());
        holder.rc_common.setText(rcModel.getCommon());
        holder.rc_date.setText(rcModel.getDate());
        holder.rc_name.setText(rcModel.getName());
        if(rcModel.getImage() != null){
            Glide.with(context).load(rcModel.getImage().toString()).into(holder.rc_image);
        }

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public static class RCViewHolder extends RecyclerView.ViewHolder {
        ImageView rc_image;
        TextView rc_title, rc_common, rc_date, rc_name;
        public RCViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            rc_image = itemView.findViewById(R.id.rc_image);
            rc_title = itemView.findViewById(R.id.rc_title);
            rc_common = itemView.findViewById(R.id.rc_common);
            rc_date = itemView.findViewById(R.id.rc_date);
            rc_name= itemView.findViewById(R.id.rc_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if( recyclerViewInterface != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

        }
    }

}

