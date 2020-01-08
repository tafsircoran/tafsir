package com.traore.abasse.tafsir.application.ui.tafsir;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.traore.abasse.tafsir.application.R;

import java.util.ArrayList;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.MyViewHolder> {

    private ArrayList<Audio> liste;
    private Context context;
    private onClickItemListener listener;
    private int selectedIndex;


    public AudioAdapter(ArrayList<Audio> liste, Context context) {
        this.liste = liste;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_audio,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (selectedIndex==position){
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
            holder.im_active.setVisibility(View.VISIBLE);
        }else {

            holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.colorTransparent));
            holder.im_active.setVisibility(View.GONE);

        }
        Audio audio = liste.get(position);
        holder.titre.setText(audio.getTitre().replace("_"," ").replace(".ogg",""));
        holder.bind(listener,audio);
    }

    @Override
    public int getItemCount() {
        return liste.size();
    }

    public void setOnclickItemListener(onClickItemListener listener){
        this.listener = listener;
    }

    public interface onClickItemListener{

        void onClickItem(Audio audio, int position);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView im_youssouf,im_active;
        private TextView titre;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            im_active = itemView.findViewById(R.id.iv_row_audio_active);
            im_youssouf = itemView.findViewById(R.id.iv_row_audio_youssouf);
            titre = itemView.findViewById(R.id.tv_row_audio_titre);
        }


        public void bind(final onClickItemListener listener, final Audio audio){

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickItem(audio,getLayoutPosition());
                }
            });
        }
    }
}
