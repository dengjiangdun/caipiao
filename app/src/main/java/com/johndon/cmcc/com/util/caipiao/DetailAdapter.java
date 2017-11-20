package com.johndon.cmcc.com.util.caipiao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by root on 11/20/17.
 */

public class DetailAdapter extends BaseAdapter {
    private Context mContext;
    private List<AwardDetail> mListAward;


    public DetailAdapter(Context context, List<AwardDetail> listAward) {
        this.mContext = context;
        this.mListAward = listAward;
    }

    @Override

    public int getCount() {
        return mListAward.size();
    }

    @Override
    public Object getItem(int position) {
        return mListAward.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.detail_item,parent,false);
        }

        viewHolder.tvNumber = (TextView) convertView.findViewById(R.id.tv_awards_number);
        viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_awards_price);
        viewHolder.tvAward = (TextView) convertView.findViewById(R.id.tv_awards);
        viewHolder.tvType = (TextView) convertView.findViewById(R.id.tv_type);
        AwardDetail awardDetail = mListAward.get(position);
        String number = String.valueOf(awardDetail.getAwardNumber());
        if (number != null) {
            viewHolder.tvNumber.setText("中奖注数:"+number);
        }

        String price = String.valueOf(awardDetail.getAwardPrice());
        if (price != null) {
            viewHolder.tvPrice.setText("中奖金额:"+price);
        }

        String award = String.valueOf(awardDetail.getAwards());
        if (award != null) {
            viewHolder.tvAward.setText("奖项:"+award);
        }

        String type = awardDetail.getType();
        if ( type != null) {
            viewHolder.tvType.setText("奖项类型:"+type);
        }

        return convertView;
    }

    private class ViewHolder{
        TextView tvNumber;
        TextView tvPrice;
        TextView tvAward;
        TextView tvType;
    }



}
