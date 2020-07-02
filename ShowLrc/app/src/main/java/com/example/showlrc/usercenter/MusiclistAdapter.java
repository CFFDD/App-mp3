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

public class MusiclistAdapter extends RecyclerView.Adapter<MusiclistAdapter.MusiclistViewHolder>{
    private static final String TAG = "MusiclistAdapter";
    Context context;
    List<MusiclistBean> mDatas;

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        public void OnItemClick(View view, int position);
    }
    public MusiclistAdapter(function context, List<MusiclistBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        Log.i(TAG,"MusiclistAdapter");
    }

    @NonNull
    @Override
    public MusiclistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_songlist,parent,false);
        MusiclistViewHolder holder = new MusiclistViewHolder(view);
        Log.i(TAG,"onCreateViewHolder");
        return holder;
    }

/*    public int po=1;
    @Override
    public void onBindViewHolder(@NonNull MusiclistViewHolder holder, final int position) {
        po++;
        Log.i(TAG,"  po==="+po);
        Log.i(TAG,"mDatas.get(po).listname=="+mDatas.get(po).listname);
            if(mDatas.get(po).isIfcrator() == true){
                MusiclistBean listBean = mDatas.get(po);
                holder.listnameTV.setText(listBean.getListname());
                holder.countTV.setText(listBean.getCount());
                holder.coverImg.setImageBitmap(listBean.getCoverImg());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.OnItemClick(v,position);
                    }
                });
            }
        Log.i(TAG,"onBindViewHolder");
    }
    @Override
    public int getItemCount() {
        int count=0;
        for(int i=1;i<mDatas.size();i++){       //从一开始循环是因为，第一个歌单默认是  用户歌曲收藏列表
            if(mDatas.get(i).isIfcrator() == true){count++;}
        }
        Log.i(TAG,"getItemCount");
        return count;
    }


    */
    public void onBindViewHolder(@NonNull MusiclistViewHolder holder, final int position) {
        if(mDatas.get(position).isIfcrator() == true){
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
        }
        Log.i(TAG,"onBindViewHolder");
    }
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MusiclistViewHolder extends RecyclerView.ViewHolder{
        TextView listnameTV,countTV;
        ImageView coverImg;
        public MusiclistViewHolder(View itemView) {
            super(itemView);
            listnameTV = itemView.findViewById(R.id.albumname);
            countTV = itemView.findViewById(R.id.countsongs);
            coverImg = itemView.findViewById(R.id.albumimage);
            Log.i(TAG,"MusiclistViewHolder");
        }
    }
}
