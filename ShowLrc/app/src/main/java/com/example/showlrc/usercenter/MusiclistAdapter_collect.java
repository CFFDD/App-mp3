package com.example.showlrc.usercenter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.showlrc.R;

import java.util.List;

public class MusiclistAdapter_collect extends RecyclerView.Adapter<MusiclistAdapter_collect.Musiclist_collect_ViewHolder>{
    private static final String TAG = "Adapter_collect";
    Context context;
    List<MusiclistBean> mDatas;

    MusiclistAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(MusiclistAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        public void OnItemClick(View view, int position);
    }
    public MusiclistAdapter_collect(function context, List<MusiclistBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        Log.i(TAG,"MusiclistAdapter_collect");
    }

    @NonNull
    @Override
    public MusiclistAdapter_collect.Musiclist_collect_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_songlist,parent,false);
        MusiclistAdapter_collect.Musiclist_collect_ViewHolder holder = new MusiclistAdapter_collect.Musiclist_collect_ViewHolder(view);
        Log.i(TAG,"onCreateViewHolder");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Musiclist_collect_ViewHolder holder, final int position) {
        if(mDatas.get(position).isIfcrator() == false){
            MusiclistBean listBean = mDatas.get(position);
            holder.listnameTV.setText(listBean.getListname());
            holder.countTV.setText(listBean.getCount());
            holder.coverImg.setImageBitmap(listBean.getCoverImg());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.OnItemClick(v,position);
                }
            });
            Log.i(TAG,"onBindViewHolder");
        }
    }



    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class Musiclist_collect_ViewHolder extends RecyclerView.ViewHolder{
        TextView listnameTV,countTV;
        ImageView coverImg;
        public Musiclist_collect_ViewHolder(View itemView) {
            super(itemView);
            listnameTV = itemView.findViewById(R.id.albumname);
            countTV = itemView.findViewById(R.id.countsongs);
            coverImg = itemView.findViewById(R.id.albumimage);
            Log.i(TAG,"MusiclistViewHolder");
        }
    }
}
