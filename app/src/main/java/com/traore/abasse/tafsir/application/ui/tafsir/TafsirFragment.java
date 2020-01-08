package com.traore.abasse.tafsir.application.ui.tafsir;

import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.traore.abasse.tafsir.application.R;
import com.traore.abasse.tafsir.application.utility.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TafsirFragment extends Fragment {

    private RecyclerView recycler;
    private TextView tv_toolbar_duration;
    private TextView tv_toolbar_titre;
    private TextView tv_toolbar_currentDuration;
    private SeekBar  seekBar ;
    private ImageView iv_toolbar_play,iv_toolbar_previous,iv_toolbar_next;



    private ArrayList<Audio> listeMusic;
    private AudioAdapter adapter;
    private int currentSelection;

    private MediaPlayer player;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tafsir, container, false);
        initialisation(root);
        return root;
    }

    private void initialisation(View root){

        recycler =  root.findViewById(R.id.recycler_play_audio);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        tv_toolbar_duration = root.findViewById(R.id.tv_toolbar_duration);
        tv_toolbar_titre = root.findViewById(R.id.tv_toolbar_titre);
        tv_toolbar_currentDuration = root.findViewById(R.id.tv_toolbar_currentDuration);
        seekBar = root.findViewById(R.id.sb_toolbar_seekbar);
        seekBar.setOnSeekBarChangeListener(seekbarchange);
        iv_toolbar_next = root.findViewById(R.id.iv_toolbar_next);
        iv_toolbar_play = root.findViewById(R.id.iv_toolbar_play);
        iv_toolbar_play.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.ic_play_circle_outline_white_24dp));
        iv_toolbar_previous = root.findViewById(R.id.iv_toolbar_previous);
        iv_toolbar_previous.setOnClickListener(previous);
        iv_toolbar_next.setOnClickListener(next);
        iv_toolbar_play.setOnClickListener(playon);
        listeMusic = new ArrayList<>();




        adapter = new AudioAdapter(listeMusic,getContext());
        recycler.setAdapter(adapter);
        adapter.setOnclickItemListener(adapterClick);

        player = new MediaPlayer();
        player.setOnPreparedListener(play);


        loadMusicFromCard();
        sorliste();



    }




    private void sorliste(){

        Collections.sort(listeMusic, new Comparator<Audio>() {
            @Override
            public int compare(Audio o1, Audio o2) {
                return o1.getTitre().compareTo(o2.getTitre());
            }
        });


    }


    //methode de click sur iteme de liste
    private AudioAdapter.onClickItemListener adapterClick = new AudioAdapter.onClickItemListener() {
        @Override
        public void onClickItem(Audio audio, int position) {

            changeSelected(position);
            playMusic(audio);
        }
    };

    private void changeSelected(int index){

        adapter.notifyItemChanged(adapter.getSelectedIndex());
        adapter.setSelectedIndex(index);
        adapter.notifyItemChanged(adapter.getSelectedIndex());
    }

    private void loadMusicFromCard(){

        listeMusic.clear();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        Cursor cursor = getActivity().getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){

            if(cursor.moveToFirst()){

                do {

                    String titre = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    if (titre.contains("Tafsir_Cheikh")){

                        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                        String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                        Audio audio = new Audio(titre,path,duration);


                        listeMusic.add(audio);

                    }


                }while(cursor.moveToNext());



            }

            cursor.close();



        }


    }


    private void playMusic(Audio audio){

        tv_toolbar_titre.setText(audio.getTitre().replace("_"," ").replace(".ogg",""));
        tv_toolbar_duration.setText(Utility.converted(Integer.parseInt(audio.getDuration())));
        player.stop();
        player.reset();
        currentSelection = listeMusic.indexOf(audio);
        //player.release();

        try {
            player.setDataSource(audio.getPath());
            player.prepareAsync();
            seekBar.setMax(Integer.parseInt(audio.getDuration())/1000);
            final Handler handler = new Handler();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    tv_toolbar_currentDuration.setText(Utility.converted(player.getCurrentPosition()));
                    seekBar.setProgress(player.getCurrentPosition()/1000);
                    handler.postDelayed(this,1000);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private MediaPlayer.OnPreparedListener play = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            toggle(mp);
        }
    };


    private void toggle(final MediaPlayer player1){

        if ( player1.isPlaying()){

            player1.stop();
            player1.reset();
            iv_toolbar_play.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.ic_play_circle_outline_white_24dp));
            //player1.release();

        }else {

            player1.start();
            iv_toolbar_play.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.ic_pause_circle_outline_white_24dp));


        }
    }


    private SeekBar.OnSeekBarChangeListener seekbarchange = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (player != null && fromUser){

                player.seekTo(progress*1000);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


    @Override
    public void onDestroy() {
        player.stop();
        player.reset();
        //player.release();
        super.onDestroy();
    }

    private View.OnClickListener previous = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            currentSelection--;
            if (currentSelection >= 0){

                Audio audio = listeMusic.get(currentSelection);
                playMusic(audio);
                changeSelected(currentSelection);
            }else {
                Audio audio = listeMusic.get(listeMusic.size()-1);
                playMusic(audio);
                changeSelected(listeMusic.size()-1);
            }
        }
    };


    private View.OnClickListener next = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            nextSelection();
        }
    };


    private View.OnClickListener playon = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (player.isPlaying()){

                player.pause();
                iv_toolbar_play.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.ic_play_circle_outline_white_24dp));
            }else{

                player.start();
                iv_toolbar_play.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.ic_pause_circle_outline_white_24dp));
            }
        }
    };

    private void nextSelection(){

        currentSelection++;
        if (currentSelection < listeMusic.size()){

            Audio audio = listeMusic.get(currentSelection);
            playMusic(audio);
            changeSelected(currentSelection);
        }else {
            Audio audio = listeMusic.get(0);
            playMusic(audio);
            changeSelected(0);
        }
    }


}