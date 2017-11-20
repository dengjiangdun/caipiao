package com.johndon.cmcc.com.util.caipiao;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
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

public class DetailActivity extends AppCompatActivity {
    private TextView mTvDate;
    private TextView mTvType;
    private TextView mTvNumber;
    private TextView mTvQici;
    private TextView mTvTotalMoney;
    private TextView mTvSellMoney;
    private ListView mLv;
    private String type;
    private FinalHttp mFinalHttp;
    private List<AwardDetail> mListDetail;
    private CMCCProgressDialog mCMCCProgressDialog;
    private String data;
    private String awardType;
    private StringBuilder number;
    private String qici;
    private String totalMoney;
    private String selMoney;
    private DetailAdapter mDetailAdapter;
    private static final String KEY_TYPE = "type_name";
    private static final String URL = "http://apicloud.mob.com/lottery/query?key=227c0655ad97c&name=%s";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        type = getIntent().getStringExtra(KEY_TYPE);

        mTvDate = (TextView) findViewById(R.id.tv_award_time);
        mTvType = (TextView) findViewById(R.id.tv_award_type);
        mTvNumber = (TextView) findViewById(R.id.tv_award_number);
        mTvQici = (TextView) findViewById(R.id.tv_award_qici);
        mTvTotalMoney = (TextView) findViewById(R.id.tv_award_total_money);
        mTvSellMoney = (TextView) findViewById(R.id.tv_award_sell_money);
        mLv = (ListView) findViewById(R.id.lv_detail);
        mFinalHttp = new FinalHttp();
        mListDetail = new ArrayList<>();
        mDetailAdapter = new DetailAdapter(this,mListDetail);
        mLv.setAdapter(mDetailAdapter);
        mCMCCProgressDialog = new CMCCProgressDialog(this);
        getData();
    }

    private void getData() {
        mCMCCProgressDialog.show();
        mFinalHttp.get(String.format(URL, type), new AjaxCallBack<String>() {
            @Override
            public void onSuccess(final String s) {
                super.onSuccess(s);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCMCCProgressDialog.dismiss();
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
                        mCMCCProgressDialog.dismiss();
                        showShortToast("Msg:"+strMsg+"errorNo:"+errorNo);
                    }
                });
            }
        });
    }

    private void dealResult(String mgs) {
        JSONObject js = JSON.parseObject(mgs);
        String retCode = js.getString("retCode");
        if (retCode.equals("200")) {
            JSONObject result = JSON.parseObject(js.getString("result"));
            data = result.getString("awardDateTime");
            awardType = result.getString("name");
            qici = result.getString("period");
            totalMoney = result.getString("pool");
            selMoney = result.getString("sales");
            JSONArray ja = JSONArray.parseArray(result.getString("lotteryNumber"));
            Object[] object = ja.toArray();
            number = new StringBuilder();
            for (Object s : object) {
                number.append(((String) s)+" ");
            }
            mListDetail.addAll(JSONArray.parseArray(result.getString("lotteryDetails"),AwardDetail.class));
            Log.d("TAG", "dealResult: "+mgs);
            updateView();
        } else {
            showShortToast(js.getString("msg"));
        }
    }

    private void showShortToast(String mgs) {
        Toast.makeText(this,mgs,Toast.LENGTH_SHORT).show();
    }

    private void updateView() {
        mTvDate.setText(data);
        mTvNumber.setText(number);
        mTvSellMoney.setText(selMoney);
        mTvTotalMoney.setText(totalMoney);
        mTvType.setText(awardType);
        mTvQici.setText(qici);

        mDetailAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(mLv);

    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() *
                (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
        listView.invalidate();
    }


}
