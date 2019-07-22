package com.example.mahmud.travelmate.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mahmud.travelmate.Interface.GoToMapListener;
import com.example.mahmud.travelmate.POJO.NearBy.Result;
import com.example.mahmud.travelmate.R;

import java.util.ArrayList;
import java.util.List;

public class NearByItemRvAdapter extends RecyclerView.Adapter<NearByItemRvAdapter.NearByItemVH>{
    private List<Result> results = new ArrayList<>();
    private Context context;
    private GoToMapListener goToMapListener;

    public NearByItemRvAdapter(Context context,List<Result> results) {
        this.results = results;
        this.context = context;
        goToMapListener = (GoToMapListener) context;
    }

    @NonNull
    @Override
    public NearByItemVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_nearby_type_item_layout,viewGroup,false);
        return new NearByItemVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NearByItemVH nearByItemVH, final int i) {
        nearByItemVH.nameTV.setText(results.get(i).getName());
        nearByItemVH.descTV.setText(results.get(i).getVicinity());
        nearByItemVH.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMapListener.goToMapWithItemDesc(results.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    class NearByItemVH extends RecyclerView.ViewHolder{
        TextView nameTV,descTV;
        public NearByItemVH(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.single_name_tv_mgnbf);
            descTV = itemView.findViewById(R.id.single_desc_tv_mgnbf);
        }
    }
}
