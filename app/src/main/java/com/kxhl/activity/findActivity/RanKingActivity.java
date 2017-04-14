package com.kxhl.activity.findActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.kxhl.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import util.Config;
import util.KxhlRestClient;
import util.TitleUtil;
import util.UrlLIst;
import view.LoadingDialog;
import view.wheel.MyRlayout;

/**
 * Created by Administrator on 2017/2/8.
 */
public class RanKingActivity extends Activity {
    private ListView mLv;
    private List<String> mLogos;
    private List<String> mNames;
    private List<String> mStars;
    private List<String> mNums;
    private List<String> mTags;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        new TitleUtil(this).setTitleName("门店热榜").setLeftImage(R.drawable.happy_mine_back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        Config.setTranslucent(this);
        mLv = (ListView) findViewById(R.id.lv_ranKing);
        if(Config.hasInternet(this)){
            dialog=new LoadingDialog(this);
            dialog.show();
            getServicce();
        }else{
            Toast.makeText(this,"网络错误!",Toast.LENGTH_SHORT).show();
        }
    }

    public void getServicce() {
        RequestParams params = new RequestParams();
        KxhlRestClient.post(UrlLIst.FOUND_HOT, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("门店热榜", response.toString());
                if (statusCode == 200) {
                    try {
                        getJson(response.getJSONArray("f_hot"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(RanKingActivity.this, "网络错误!", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void getJson(JSONArray array) throws JSONException {
        mLogos = new ArrayList<>();
        mNames = new ArrayList<>();
        mNums = new ArrayList<>();
        mStars = new ArrayList<>();
        mTags = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            String logo = object.getString("logo");
            String name = object.getString("name");
            String star = object.getString("star");
            String num = object.getString("num");
            String tag=object.getString("tag");
            mLogos.add(i, logo);
            mNames.add(i, name);
            mNums.add(i, num);
            mStars.add(i, star);
            mTags.add(i,tag);
        }
        mLv.setAdapter(new RanKingAdapter(RanKingActivity.this,mNames,mLogos,mStars,mNums,mTags));
        dialog.dismiss();
    }

    private class RanKingAdapter extends BaseAdapter {
        LayoutInflater inflater;
        List<String> names;
        List<String> logos;
        List<String> stars;
        List<String> nums;
        List<String> tags;
        Context context;
        public RanKingAdapter(Context context, List<String> names, List<String> logos, List<String> stars, List<String> nums,List<String> tags) {
            inflater = LayoutInflater.from(context);
            this.context=context;
            this.names = names;
            this.logos = logos;
            this.stars = stars;
            this.nums = nums;
            this.tags=tags;
        }

        @Override
        public int getCount() {
            return names.size();
        }

        @Override
        public Object getItem(int position) {
            return names.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHoder hoder;
            if (convertView == null) {
                hoder = new ViewHoder();
                convertView = inflater.inflate(R.layout.item_ranking, null);
                hoder.rl_bj = (MyRlayout) convertView.findViewById(R.id.item_rlRanKing_bj);
                hoder.rl_bj.setColor(0x38000000);
                hoder.tv_name = (TextView) convertView.findViewById(R.id.item_tvRanking_name);
                hoder.tv_num = (TextView) convertView.findViewById(R.id.item_tvRanking_num);
                hoder.tv_start = (TextView) convertView.findViewById(R.id.item_tvRanking_start);
                hoder.tv_lab7=(TextView)convertView.findViewById(R.id.item_lab7);
                hoder.tv_lab8=(TextView)convertView.findViewById(R.id.item_lab8);
                hoder.tv_lab9=(TextView)convertView.findViewById(R.id.item_lab9);
                hoder.tv_lab10=(TextView)convertView.findViewById(R.id.item_lab10);
                hoder.tv_lab11=(TextView)convertView.findViewById(R.id.item_lab11);
                hoder.tv_lab12=(TextView)convertView.findViewById(R.id.item_lab12);
                convertView.setTag(hoder);
            } else {
                hoder = (ViewHoder) convertView.getTag();
            }
            hoder.tv_name.setText(names.get(position));
            hoder.tv_start.setText(stars.get(position));
            hoder.tv_num.setText("TOP"+nums.get(position));
            Glide.with(context).load(logos.get(position)).asBitmap().
                    into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                       hoder.rl_bj.setBackgroundDrawable(new BitmapDrawable(resource));
                        }
                    });
//            for(int i=0;i<tags.get(position).length();i++){
//                String aa[]=tags.get(position).split(",");
//                    for(int j=0;j<aa.length;j++) {
//                        Log.i("tagsss", aa[j]);
//                        switch(aa[j]){
//                            case"7":
//                                hoder.tv_lab7.setVisibility(View.VISIBLE);
//                                break;
//                            case "8":
//                                hoder.tv_lab8.setVisibility(View.VISIBLE);
//                                break;
//                            case "9":
//                                hoder.tv_lab9.setVisibility(View.VISIBLE);
//                                break;
//                            case "10":
//                                hoder.tv_lab10.setVisibility(View.VISIBLE);
//                                break;
//                            case "11":
//                                hoder.tv_lab11.setVisibility(View.VISIBLE);
//                                break;
//                            case "12":
//                                hoder.tv_lab12.setVisibility(View.VISIBLE);
//                                break;
//                        }
//                    }
//            }
            return convertView;
        }

        private class ViewHoder {
            MyRlayout rl_bj;
            TextView tv_num, tv_name, tv_start;
            TextView tv_lab7,tv_lab8,tv_lab9,tv_lab10,tv_lab11,tv_lab12;
        }
    }
}
