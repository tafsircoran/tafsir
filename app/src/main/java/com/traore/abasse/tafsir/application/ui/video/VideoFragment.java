package com.traore.abasse.tafsir.application.ui.video;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traore.abasse.tafsir.application.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {

    public static final String PATH_YOUTUBE_VIDEO = "PATH";

    private RecyclerView recyclerView;
    private VideoAdapter adapter;
    private DatabaseReference reference;
    private ArrayList<Video> listevideo = new ArrayList<>();
    private Video videoInitiale;



    public VideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_video, container, false);
        initialisation(root);
        return root;
    }

    private void initialisation(View root) {

        videoInitiale = new Video("Cheikh El Hadj Youssoufou OUEDRAOGO \nVérifier votre connection internet !!!","","");
        listevideo.add(videoInitiale);
        recyclerView = root.findViewById(R.id.video_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new VideoAdapter(getActivity().getApplicationContext(),listevideo);
        recyclerView.setAdapter(adapter);
        adapter.setOnclickListener(click);
        reference = FirebaseDatabase.getInstance().getReference("video");
        reference.addChildEventListener(listener);

    }



    private ChildEventListener listener = new ChildEventListener() {

        @Override

        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               listevideo.remove(videoInitiale);

            if (dataSnapshot.exists()){

                Video video = dataSnapshot.getValue(Video.class);
                listevideo.add(video);
                adapter.notifyDataSetChanged();

            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private VideoAdapter.onClickListener click = new VideoAdapter.onClickListener() {
        @Override
        public void onClick(View view, Video video, int position) {
            Intent v = new Intent(getActivity().getApplicationContext(),YoutubeActivity.class);
            v.putExtra(PATH_YOUTUBE_VIDEO,video.getPath());
            startActivity(v);

        }
    };

}
