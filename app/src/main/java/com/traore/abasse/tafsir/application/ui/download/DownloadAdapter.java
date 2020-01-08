package com.traore.abasse.tafsir.application.ui.download;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.traore.abasse.tafsir.application.R;

import java.util.ArrayList;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.MyHolderView> {

    private ArrayList<Download> liste;
    private Context context;
    private onClickListener listener;


    public DownloadAdapter(ArrayList<Download> liste, Context context) {
        this.liste = liste;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_download,parent,false);
        return new MyHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolderView holder, final int position) {

        final Download download = liste.get(position);
        holder.titre.setText(download.titre);
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(holder.btn,v,download,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return liste.size();
    }


    public void setOnClickListener(onClickListener listener){

        this.listener = listener;
    }


    public interface onClickListener{
        void onClick(Button b, View v, Download download, int position);
    }

    public class MyHolderView extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView titre;
        private Button btn;

        public MyHolderView(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_row_download_islam);
            titre = itemView.findViewById(R.id.tv_row_download_titre);
            btn = itemView.findViewById(R.id.btn_row_download);
        }
    }
}
