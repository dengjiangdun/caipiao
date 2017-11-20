package com.johndon.cmcc.com.util.caipiao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.androidkun.PullToRefreshRecyclerView;
import com.androidkun.callback.PullToRefreshListener;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.util.ArrayList;
import java.util.List;

import util.cmcc.johndon.com.show.CMCCProgressDialog;

public class MainActivity extends AppCompatActivity implements MyAdapter.ShareListner{
    private PullToRefreshRecyclerView mRv;
    private FinalHttp finalHttp;
    private List<Joke> mListJoke;
    private MyAdapter myAdapter;
    private CMCCProgressDialog cmccPD;
    private int mPage = 1;
    private boolean isReFresh = true;
    private String mtime = String.valueOf(System.currentTimeMillis()/1000);
    private static final String URL = "http://api.avatardata.cn/Joke/QueryJokeByTime?key=16194dd1c3b042818282fdb8f5769151&page=%d&rows=10&sort=desc&time=%s";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRv = (PullToRefreshRecyclerView) findViewById(R.id.rv_list);
        finalHttp = new FinalHttp();
        cmccPD = new CMCCProgressDialog(this);
        initRv();
        loadData();
    }

    private void initRv() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRv.setLayoutManager(linearLayoutManager);

        mListJoke = new ArrayList<>();
        myAdapter = new MyAdapter(MainActivity.this,R.layout.news_item,mListJoke);
        myAdapter.setShareListner(this);
        mRv.setAdapter(myAdapter);

        mRv.setPullToRefreshListener(new PullToRefreshListener() {
            @Override
            public void onRefresh() {
                mListJoke.clear();
                mPage = 1;
                mtime = String.valueOf(System.currentTimeMillis()/1000);
                isReFresh = true;
                loadData();
            }

            @Override
            public void onLoadMore() {
                mPage ++;
                isReFresh = false;
                loadData();
            }
        });
    }

    private void loadData() {
        cmccPD.show();
        Log.d("TAG", "loadData: "+mtime);
        finalHttp.get(String.format(URL, mPage,mtime), new AjaxCallBack<String>() {

            @Override
            public void onSuccess(final String s) {
                super.onSuccess(s);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dealResult(s);
                    }
                });

            }

            @Override
            public void onFailure(Throwable t, final int errorNo, final String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cmccPD.dismiss();
                        showShortToast(strMsg+errorNo);
                    }
                });
            }
        });
    }

    private void dealResult(String s) {
        cmccPD.dismiss();
        JSONObject jsonObject = JSON.parseObject(s);
        int code = jsonObject.getIntValue("error_code");
        String mgs = jsonObject.getString("reason");
        if (code == 0) {
            List<Joke> list = JSONArray.parseArray(jsonObject.getString("result"),Joke.class);
            mListJoke.addAll(list);
            myAdapter.notifyDataSetChanged();
            if (list.size() < 10) {
                showShortToast("没有更多可以加载");
            }
        } else {
            showShortToast(mgs);
        }

        if (isReFresh) {
            mRv.setRefreshComplete();
        } else {
            mRv.setLoadMoreComplete();
        }
    }


    private void showShortToast(String s) {
        Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.login) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void shareItem(Joke joke) {
        Intent textIntent = new Intent(Intent.ACTION_SEND);
        textIntent.setType("text/plain");
        textIntent.putExtra(Intent.EXTRA_TEXT,joke.getContent());
        startActivity(Intent.createChooser(textIntent, "分享"));

    }
}
