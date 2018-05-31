package com.smb.smbtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smb.sbmlibrary.MediaItem;

import java.util.ArrayList;

/**
 * Created by zhijinjin (951507056@qq.com)
 * on 2018/4/23.
 */

public class MyRecycViewAdapter extends RecyclerView.Adapter<MyRecycViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<MediaItem> mData = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    public void setmData(ArrayList<MediaItem> data){
        this.mData = data;
    }

    public MyRecycViewAdapter(Context context){
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adpter, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTv.setText(mData.get(position).getName());
        holder.mTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onClick(position);
            }
        });
        holder.mTv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mOnItemClickListener.onLongClick(position);
                return true;
            }
        });
    }

    public MediaItem getItem(int position){
        return mData.get(position);
    }
    @Override
    public int getItemCount() {
        return  mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTv;
        public ViewHolder(View itemView) {
            super(itemView);
            mTv = (TextView) itemView.findViewById(R.id.item_tv);
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this. mOnItemClickListener=onItemClickListener;
    }
}
