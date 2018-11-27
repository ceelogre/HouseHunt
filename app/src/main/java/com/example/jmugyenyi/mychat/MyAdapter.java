package com.example.jmugyenyi.mychat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<String> stringList;

    public MyAdapter(List<String> items){
        this.stringList = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view =
         inflater.inflate(R.layout.list_item,viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.mtitle.setText(stringList.get(i));

    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mtitle;
        TextView mDescription;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mtitle = itemView.findViewById(R.id.item_title);
            mDescription = itemView.findViewById(R.id.item_desc);
            imageView = itemView.findViewById(R.id.image);

        }
    }
}
