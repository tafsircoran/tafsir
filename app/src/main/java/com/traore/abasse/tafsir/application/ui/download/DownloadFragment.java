package com.traore.abasse.tafsir.application.ui.download;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.traore.abasse.tafsir.application.R;

import java.util.ArrayList;

import static android.content.Context.DOWNLOAD_SERVICE;

public class DownloadFragment extends Fragment {


    private RecyclerView recycler;

    private DownloadAdapter adapter;
    private ArrayList<Download> downloadsListe;
    private ArrayList<String> listeMusic;
    private Handler handler = new Handler();
    private ProgressBar main_progressBar;
    private TextView main_indiction;
    private static final int ouveture_aucdio_activity = 1;
    private boolean t = true;

    DatabaseReference reference ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_download, container, false);

        initialisation(root);
        return root;
    }


    //methode d'initialisation des composants
    private void initialisation(View root){

        reference = FirebaseDatabase.getInstance().getReference("tafsir");
        reference.addChildEventListener(child);

        recycler = root.findViewById(R.id.recycler_download);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));


        downloadsListe = new ArrayList<>();
        listeMusic = new ArrayList<>();

        adapter = new DownloadAdapter(downloadsListe,getContext());
        recycler.setAdapter(adapter);

        main_progressBar = root.findViewById(R.id.main_progress);
        main_indiction = root.findViewById(R.id.main_indication);

        cheickPermission();
        loaded();
        main_progress();

    }



    private void main_progress(){

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (downloadsListe.size()>0)
                {
                    main_progressBar.setVisibility(View.GONE);
                    main_indiction.setVisibility(View.GONE);
                    return;
                }

                handler.postDelayed(this,1000);
            }
        });
    }




    private void cheickPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},123);
                return;
            }else { loaded();
                loadMusicFromCard();}
        }else {
            loaded();
            loadMusicFromCard();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){

            case 123:
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){

                    loaded();
                    loadMusicFromCard();
                }else{

                    Toast.makeText(getContext(),"permission non autorisée",Toast.LENGTH_SHORT).show();
                    cheickPermission();
                }
                break;

            default:

                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void loaded() {


        adapter.setOnClickListener(new DownloadAdapter.onClickListener() {
            @Override
            public void onClick(final Button b, View v, final Download download, int position) {
                loadMusicFromCard();
                // Utility.log(download.titre);



                if(!verifiePresenceTafsir(download.titre)) {
                    //t.start();
                    download(download.path, download.titre, "Téléchargement " + download.titre, Environment.DIRECTORY_MUSIC+"/TAFSIR", download.titre.replace(" ", "_"), "ogg");
                    b.setEnabled(false);
                   ;
                }else{
                    b.setText("Terminé");

                    b.setEnabled(false);

                }






            }
        });



    }





    private ChildEventListener child = new ChildEventListener() {




        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            if (dataSnapshot.exists()){


                Download download = dataSnapshot.getValue(Download.class);
                downloadsListe.add(download);


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

    private void download(String uri,String titre,String description,String directory,String name,String extension){

        DownloadManager downloadManager = (DownloadManager)getActivity().getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(uri));
        request.setTitle(titre);
        request.setDescription(description);
        request.setDestinationInExternalPublicDir(directory,name+"."+extension);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);


        downloadManager.enqueue(request);


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
                    listeMusic.add(titre);

                }while(cursor.moveToNext());

            }


        }
    }


    private boolean verifiePresenceTafsir(String titre){

        for(String name : listeMusic){


            if ((titre.replace(" ","_")+".ogg").equals(name)){

                return true;
            }



        }

        return false;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){

            main_progress();
        }
    }
}