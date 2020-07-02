package com.example.showlrc.songlistdetails;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.showlrc.R;
import com.example.showlrc.intmusic.IntMusicBean;
import com.example.showlrc.usercenter.function;

import java.util.List;

public class ListdetailAdapter extends RecyclerView.Adapter<ListdetailAdapter.ListdetailViewHolder> implements View.OnClickListener {
    private static final String TAG = "ListdetailAdapter";
    Context context;
    List<IntMusicBean> ldDatas;

    /*    OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }*/
////////////////////////////
    //item里面有多个控件可以点击
    public enum ViewName {
        ITEM,
        delete,
        addtolove
    }

    //自定义一个回调接口来实现Click和LongClick事件
    public interface OnItemClickListener {
        void OnItemClick(View view, int position);

        void onItemClick(View v, ViewName practise, int position);
    }

    private OnItemClickListener ldOnItemClickListener;//声明自定义的接口

    //定义方法并暴露给外面的调用者
    public void setmOnItemClickListener(OnItemClickListener listener) {
        this.ldOnItemClickListener = listener;
    }

///////////////////////////

    public ListdetailAdapter(songlistdetail context, List<IntMusicBean> mDatas) {
        this.context = context;
        this.ldDatas = mDatas;
        Log.i(TAG, "ListdetailAdapter");
    }

    @NonNull
    @Override
    public ListdetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_listdetails, parent, false);
        ListdetailViewHolder holder = new ListdetailViewHolder(view);
        Log.i(TAG, "onCreateViewHolder");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListdetailViewHolder holder, final int position) {
        IntMusicBean listdetailBean = ldDatas.get(position);
        String idbyme = ""+listdetailBean.getIdbyme();
        holder.song_idbyme_details.setText(idbyme);
        holder.songname_details.setText(listdetailBean.getSongname());
        holder.singer_details.setText(listdetailBean.getSingername());

        holder.itemView.setTag(position);
        holder.deletefromlist_details.setTag(position);
        holder.addtolove_details.setTag(position);

        Log.i(TAG, "onBindViewHolder");
    }

    @Override
    public int getItemCount() {
        return ldDatas.size();
    }

    class ListdetailViewHolder extends RecyclerView.ViewHolder {
        TextView song_idbyme_details, songname_details,singer_details;
        ImageButton deletefromlist_details,addtolove_details;

        public ListdetailViewHolder(View itemView) {
            super(itemView);
            song_idbyme_details = itemView.findViewById(R.id.song_idbyme_details);
            songname_details = itemView.findViewById(R.id.songname_details);
            singer_details = itemView.findViewById(R.id.singer_details);
            deletefromlist_details = itemView.findViewById(R.id.deletefromlist_details);
            addtolove_details= itemView.findViewById(R.id.addtolove_details);

            itemView.setOnClickListener(ListdetailAdapter.this);
            deletefromlist_details.setOnClickListener(ListdetailAdapter.this);
            addtolove_details.setOnClickListener(ListdetailAdapter.this);

            Log.i(TAG, "MusiclistViewHolder");
        }
    }
//=======================以下为item中的button控件点击事件处理===================================


    @Override
    public void onClick(View v) {
        int position = (int) v.getTag(); //getTag()获取数据
        if (ldOnItemClickListener != null) {
            switch (v.getId()) {
                default:
                    ldOnItemClickListener.OnItemClick(v, position);
                    break;
                case R.id.deletefromlist_details:
                    ldOnItemClickListener.onItemClick(v, ViewName.delete, position);
                    break;
                case R.id.addtolove_details:
                    ldOnItemClickListener.onItemClick(v, ViewName.addtolove, position);
                    break;

            }
        }
    }
}
