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
    List<MusiclistBean_collect> mDatas;

    MusiclistAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(MusiclistAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        public void OnItemClick(View view, int position);
    }
    public MusiclistAdapter_collect(function context, List<MusiclistBean_collect> mDatas) {
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
   /* public int po1;        //在调用onBindViewHolder之前，getItemCount会先执行，获得第一个收藏歌单的位置po,据此显示收藏歌单的数据
    public int po2;
    public boolean iffirst=true;

    @Override
    public void onBindViewHolder(@NonNull Musiclist_collect_ViewHolder holder, final int position) {

        Log.i(TAG,"iffirst==="+iffirst);
        if(iffirst){po2=po1;po2++;iffirst=false;}       //如果是第一次执行该方法则给po2赋值
        // 每次创建一个item时getItemCount方法都会被调用一次，即po1值会是固定的
        // 所以要先判断是否第一次，不然每次获取到的都是同一个位置，会重复创建同一个歌单的item
        Log.i(TAG,"onBindViewHolder  po==="+po2);
        Log.i(TAG,"mDatas.get(po).listname=="+mDatas.get(po2).listname);
        if(mDatas.get(po2).isIfcrator() == false){        //从获得的第一个收藏歌单位置开始，寻找他人创建的歌单（用户收藏的歌单）
                MusiclistBean listBean = mDatas.get(po2);
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
        po2++;

    }



    @Override
    public int getItemCount() {
        po1=0;       //getItemCount()会先调用两遍，之后每创建item前还会调用一次，所以po不能赋值在外部
        int count=0;
        for(int i=1;i<mDatas.size();i++){       //从一开始循环是因为，第一个歌单默认是  用户歌曲收藏列表
            if(mDatas.get(i).isIfcrator() == false){count++;}       //统计收藏歌单的个数，据此创建item
            if (mDatas.get(i).isIfcrator() == true){po1++;}
            //统计自建歌单个数（自建歌单之后紧接着就是收藏歌单）
            // 最后会得到最末一个自建歌单的位置po,据此判断收藏歌单的位置
        }
        Log.i(TAG,"getItemCount");
        return count;
    }*/






    @Override
    public void onBindViewHolder(@NonNull Musiclist_collect_ViewHolder holder, final int position) {
        if(mDatas.get(position).isIfcrator() == false){        //从获得的第一个收藏歌单位置开始，寻找他人创建的歌单（用户收藏的歌单）
            MusiclistBean_collect listBean = mDatas.get(position);
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
