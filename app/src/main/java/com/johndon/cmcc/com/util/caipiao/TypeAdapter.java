package com.johndon.cmcc.com.util.caipiao;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by root on 11/20/17.
 */

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.TypeHolder>{
    private List<String> mListType;
    private Context mContext;
    private ItemClickListener mItemClickListener;

    public TypeAdapter(Context context,List<String> listType) {
        this.mListType = listType;
        this.mContext = context;
    }

    @Override
    public TypeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.type_item,parent,false);
        TypeHolder typeHolder = new TypeHolder(view);
        return typeHolder;
    }

    @Override
    public void onBindViewHolder(TypeHolder holder, final int position) {
        holder.tv.setText(mListType.get(position));
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.clickItem(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListType.size();
    }

    class TypeHolder extends RecyclerView.ViewHolder{
        TextView tv;
        public TypeHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_type_item);
        }
    }

    public ItemClickListener getmItemClickListener() {
        return mItemClickListener;
    }

    public void setmItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface ItemClickListener{
        public void clickItem(int position);
    }


}
