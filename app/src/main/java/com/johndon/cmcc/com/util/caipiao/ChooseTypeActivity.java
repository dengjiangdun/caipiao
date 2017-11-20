package com.johndon.cmcc.com.util.caipiao;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.util.ArrayList;
import java.util.List;

import util.cmcc.johndon.com.show.CMCCProgressDialog;

/**
 * Created by root on 11/20/17.
 */

public class ChooseTypeActivity extends AppCompatActivity implements TypeAdapter.ItemClickListener{

    private RecyclerView mRv;
    private CMCCProgressDialog mCmccProgressDialog;
    private FinalHttp mFinalHttp;
    private List<String> mListType;
    private TypeAdapter mTypeAdapter;

    private static final String KEY_TYPE = "type_name";
    private static final String URL = "http://apicloud.mob.com/lottery/list?key=227c0655ad97c";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_type);
        mCmccProgressDialog = new CMCCProgressDialog(this);
        mFinalHttp = new FinalHttp();
        mRv = (RecyclerView) findViewById(R.id.rv_type);
        mListType = new ArrayList<>();
        mTypeAdapter = new TypeAdapter(this,mListType);
        mTypeAdapter.setmItemClickListener(this);
        mRv.setHasFixedSize(true);
        LinearLayoutManager ll = new LinearLayoutManager(this);
        mRv.setLayoutManager(ll);
        mRv.setAdapter(mTypeAdapter);
        getTypeData();

    }

    private void getTypeData() {
        mCmccProgressDialog.show();
        mFinalHttp.get(URL, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(final String s) {
                super.onSuccess(s);
                mCmccProgressDialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dealResult(s);
                    }
                });
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    private void dealResult(String mgs) {
        JSONObject jsonObject = JSON.parseObject(mgs);
        String retCode = jsonObject.getString("retCode");
        if (retCode.equals("200")) {
            JSONArray jsonarray = JSONArray.parseArray(jsonObject.getString("result"));
            Object[] strs = jsonarray.toArray(); //json转为数组
            for(Object s:strs) {
                mListType.add((String) s);
            }
            mTypeAdapter.notifyDataSetChanged();
        } else {
            showShortToast(jsonObject.getString("msg"));
        }
    }

    private void showShortToast(String mgs) {
        Toast.makeText(this,mgs,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void clickItem(int position) {
        Intent intent = new Intent(ChooseTypeActivity.this,DetailActivity.class);
        intent.putExtra(KEY_TYPE,mListType.get(position));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
}
