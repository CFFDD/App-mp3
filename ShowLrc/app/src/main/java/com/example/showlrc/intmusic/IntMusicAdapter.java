package com.example.showlrc.intmusic;

import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.showlrc.R;


import java.util.List;

public class IntMusicAdapter extends RecyclerView.Adapter<IntMusicAdapter.LocalMusicViewHolder> implements View.OnClickListener{
    private static final String TAG = "IntMusicAdapter";
    Context context;
    List<IntMusicBean> mDatas;


    /*OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }*/

    ////////////////////////////
    //item里面有多个控件可以点击
    public enum ViewName {
        ITEM,
        love
    }

    //自定义一个回调接口来实现Click和LongClick事件
    public interface OnItemClickListener {
        void OnItemClick(View view, int position);

        void onItemClick(View v, IntMusicAdapter.ViewName practise, int position);
    }

    private IntMusicAdapter.OnItemClickListener OnItemClickListener;//声明自定义的接口

    //定义方法并暴露给外面的调用者
    public void setOnItemClickListener(IntMusicAdapter.OnItemClickListener onItemClickListener) {
        this.OnItemClickListener = onItemClickListener;
    }

    ///////////////////////////
    @Override
    public void onClick(View v) {
        int position = (int) v.getTag(); //getTag()获取数据
        if (OnItemClickListener != null) {
            switch (v.getId()) {
                default:
                    OnItemClickListener.OnItemClick(v, position);
                    break;
                case R.id.lovethissong:
                    OnItemClickListener.onItemClick(v, ViewName.love, position);
                    break;
            }
        }
    }


    public IntMusicAdapter(MainActivity_IntMp3 context, List<IntMusicBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        Log.i(TAG,"IntMusicAdapter");
    }

    @NonNull
    @Override
    public LocalMusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_int_music,parent,false);
        LocalMusicViewHolder holder = new LocalMusicViewHolder(view);
        Log.i(TAG,"onCreateViewHolder");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LocalMusicViewHolder holder, final int position) {
        IntMusicBean musicBean = mDatas.get(position);
        holder.idTv.setText(""+musicBean.getIdbyme());
        holder.songTv.setText(musicBean.getSongname());
        holder.singerTv.setText(musicBean.getSingername());


        holder.itemView.setTag(position);
        holder.love.setTag(position);

        Log.i(TAG,"onBindViewHolder");
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class LocalMusicViewHolder extends RecyclerView.ViewHolder{
        TextView idTv,songTv,singerTv,albumTv,timeTv;
        ImageButton love;
        public LocalMusicViewHolder(View itemView) {
            super(itemView);
            idTv = itemView.findViewById(R.id.item_local_music_num);
            songTv = itemView.findViewById(R.id.item_local_music_song);
            singerTv = itemView.findViewById(R.id.item_local_music_singer);
            albumTv = itemView.findViewById(R.id.item_local_music_album);
            timeTv = itemView.findViewById(R.id.item_local_music_durtion);
            love = itemView.findViewById(R.id.lovethissong);

            itemView.setOnClickListener(IntMusicAdapter.this);
            love.setOnClickListener(IntMusicAdapter.this);

            Log.i(TAG,"LocalMusicViewHolder");
        }
    }
}
