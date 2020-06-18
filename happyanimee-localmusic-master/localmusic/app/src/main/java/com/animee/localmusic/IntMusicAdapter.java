package com.animee.localmusic;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class IntMusicAdapter extends RecyclerView.Adapter<IntMusicAdapter.LocalMusicViewHolder>{
    private static final String TAG = "IntMusicAdapter";
    Context context;
    List<Song>mDatas;

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        public void OnItemClick(View view, int position);
    }
    public IntMusicAdapter(MainActivity_IntMp3 context, List<Song> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        Log.i(TAG,"IntMusicAdapter");
    }

    @NonNull
    @Override
    public LocalMusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_local_music,parent,false);
        LocalMusicViewHolder holder = new LocalMusicViewHolder(view);
        Log.i(TAG,"onCreateViewHolder");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LocalMusicViewHolder holder, final int position) {
        Song musicBean = mDatas.get(position);
        holder.idTv.setText(musicBean.getSongid());
        holder.songTv.setText(musicBean.getSongname());
        holder.singerTv.setText(musicBean.getSingername());
        //holder.albumTv.setText(musicBean.getAlbum());
        //holder.timeTv.setText(musicBean.getDuration());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClick(v,position);
            }
        });
        Log.i(TAG,"onBindViewHolder");
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class LocalMusicViewHolder extends RecyclerView.ViewHolder{
        TextView idTv,songTv,singerTv,albumTv,timeTv;
        public LocalMusicViewHolder(View itemView) {
            super(itemView);
            idTv = itemView.findViewById(R.id.item_local_music_num);
            songTv = itemView.findViewById(R.id.item_local_music_song);
            singerTv = itemView.findViewById(R.id.item_local_music_singer);
            albumTv = itemView.findViewById(R.id.item_local_music_album);
            timeTv = itemView.findViewById(R.id.item_local_music_durtion);
            Log.i(TAG,"LocalMusicViewHolder");
        }
    }
}
