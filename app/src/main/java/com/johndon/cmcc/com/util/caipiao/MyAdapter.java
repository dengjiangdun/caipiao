package com.johndon.cmcc.com.util.caipiao;

import android.content.Context;
import android.view.View;

import com.androidkun.adapter.BaseAdapter;
import com.androidkun.adapter.ViewHolder;

import java.util.List;

/**
 * Created by root on 11/10/17.
 */

public class MyAdapter extends BaseAdapter<Joke> {
    private List<Joke> mListJoke;
    private Context mContext;
    private ShareListner shareListner;

    public ShareListner getShareListner() {
        return shareListner;
    }

    public void setShareListner(ShareListner shareListner) {
        this.shareListner = shareListner;
    }

    public MyAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
        mContext = context;
        mListJoke = datas;
    }

    @Override
    public void convert(ViewHolder holder, final Joke joke) {

        holder.setText(R.id.tv_title,joke.getContent());
        holder.setText(R.id.tv_date,joke.getUpdatetime());
        holder.getView(R.id.tv_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shareListner != null) {
                    shareListner.shareItem(joke);
                }
            }
        });
    }

    public interface ShareListner{
        void shareItem(Joke joke);
    }

}
