package com.traore.abasse.tafsir.application.ui.video;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;
import com.traore.abasse.tafsir.application.R;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {


    private Context context;
    private ArrayList<Video> listeVideo;
    private onClickListener listener;


    public VideoAdapter(Context context, ArrayList<Video> listeVideo) {
        this.context = context;
        this.listeVideo = listeVideo;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_video,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Video video = listeVideo.get(position);
        holder.textView.setText(video.getTitre());
        if (!video.getImagepath().equals(""))
        {
            Picasso.get()
                    .load(video.getImagepath())
                    .placeholder(ContextCompat.getDrawable(context,R.drawable.im_youssouf))
                    .error(ContextCompat.getDrawable(context,R.drawable.im_youssouf))
                    .into(holder.imageView);
        }else
        {
            Picasso.get()
                    .load(R.drawable.im_youssouf)
                    .into(holder.imageView);
        }


        holder.bind(listener,video);
    }

    @Override
    public int getItemCount() {
        return listeVideo.size();
    }

    public void setOnclickListener(onClickListener listener)
    {
        this.listener = listener;
    }

    public interface onClickListener{

        void onClick(View view,Video video,int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.video_row_image);
            textView = itemView.findViewById(R.id.video_titre);
        }

        public void bind(final onClickListener listener, final Video video)
        {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v,video,getLayoutPosition());
                }
            });
        }
    }
}
