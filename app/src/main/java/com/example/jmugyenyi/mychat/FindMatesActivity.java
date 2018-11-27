package com.example.jmugyenyi.mychat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class FindMatesActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar mToolbar;
    private RecyclerView recyclerView;
    private RecyclerView FindFriendsRecyclerList;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_mates);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        FindFriendsRecyclerList = findViewById(R.id.find_mates_recycler_list);
        FindFriendsRecyclerList.setLayoutManager(new LinearLayoutManager(this));

        initialiseFields();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Find Mates");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Contacts> options =
                new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(databaseReference,Contacts.class).build();

        FirebaseRecyclerAdapter<Contacts,FindMatesViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contacts, FindMatesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindMatesViewHolder holder, int position, @NonNull Contacts model) {
                holder.username.setText(model.getName());
                holder.status.setText(model.getStatus());
                Picasso.get().load(model.getImage()).into(holder.profileImage);
            }

            @NonNull
            @Override
            public FindMatesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.housemates_display_layout,viewGroup,false);

                FindMatesViewHolder viewHolder = new FindMatesViewHolder(view);
                return viewHolder;
            }
        };

        FindFriendsRecyclerList.setAdapter(adapter);
        adapter.startListening();
    }

    private void initialiseFields() {
        mToolbar = findViewById(R.id.find_mates_toolbar);
        recyclerView = findViewById(R.id.find_mates_recycler_list);
    }

    public static class FindMatesViewHolder extends RecyclerView.ViewHolder{

        TextView username , status;
        CircleImageView profileImage;
        public FindMatesViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.user_profile_name);
            status = itemView.findViewById(R.id.user_profile_status);
            profileImage = itemView.findViewById(R.id.housemates_profile_image);
        }

    }
}
