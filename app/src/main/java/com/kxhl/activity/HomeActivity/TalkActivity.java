package com.kxhl.activity.HomeActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import util.RoundedImageView;
import util.SaveData;
import util.TitleUtil;
import util.UrlLIst;
import view.LoadingDialog;

/**
 * Created by Administrator on 2017/1/17.
 */
public class TalkActivity extends Activity {
    private ListView lv_talk;
    private List<String> logos;
    private List<String> names;
    private List<String> times;
    private List<String> aIds;//预约id
    private LoadingDialog dialog;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            msg.what=00;
            dialog.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);
        new TitleUtil(this).setTitleName("星级评价").setLeftImage(R.drawable.happy_mine_back).
                setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        lv_talk = (ListView) findViewById(R.id.lv_talk);
    }


    @Override
    protected void onResume() {
        if(Config.hasInternet(this)){
            dialog=new LoadingDialog(this);
            dialog.show();
            getService((String) SaveData.get(this, Config.USERID, ""));
        }else{
            Toast.makeText(this,"网络错误!",Toast.LENGTH_SHORT).show();
        }
        super.onResume();
    }

    public void getService(String userId) {
        RequestParams params = new RequestParams();
        params.put("id", userId);
        KxhlRestClient.post(UrlLIst.INDEX_STAR, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("星级评价数据", response.toString());
                if (statusCode == 200) {
                    try {
                        handler.sendEmptyMessage(00);
                        JSONArray array = response.getJSONArray("star");
                        getJson(array);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(TalkActivity.this, "网络错误!", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void getJson(JSONArray array) throws JSONException {
        logos = new ArrayList<>();
        names = new ArrayList<>();
        times = new ArrayList<>();
        aIds = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            String logo = object.getString("logo");
            String name = object.getString("name");
            String time = object.getString("time");
            String aid = object.getString("aid");
            logos.add(i, logo);
            names.add(i, name);
            times.add(i, time);
            aIds.add(i, aid);
        }
        lv_talk.setAdapter(new TalkAdapter(this, logos, names, times, aIds));

    }

    private class TalkAdapter extends BaseAdapter {
        LayoutInflater inflater;
        List<String> lo;
        List<String> na;
        List<String> ti;
        List<String> aid;
        Context context;

        public TalkAdapter(Context context, List<String> lo, List<String> na, List<String> ti, List<String> aid) {
            inflater = LayoutInflater.from(context);
            this.context = context;
            this.lo = lo;
            this.na = na;
            this.ti = ti;
            this.aid = aid;
        }

        @Override
        public int getCount() {
            return na.size();
        }

        @Override
        public Object getItem(int position) {
            return na.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHoder hoder;
            if (convertView == null) {
                hoder = new ViewHoder();
                convertView = inflater.inflate(R.layout.item_talk, null);
                hoder.tv_name = (TextView) convertView.findViewById(R.id.item_talk_tv);
                hoder.tv_time = (TextView) convertView.findViewById(R.id.item_talk_tv2);
                hoder.iv_logo = (RoundedImageView) convertView.findViewById(R.id.item_talk_iv);
                hoder.btn_image = (ImageView) convertView.findViewById(R.id.item_talk_iv2);
                convertView.setTag(hoder);
            } else {
                hoder = (ViewHoder) convertView.getTag();
            }
            hoder.tv_name.setText(na.get(position));
            hoder.tv_time.setText(ti.get(position));
            hoder.btn_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, StartTalkActivity.class);
                    i.putExtra("aid", aid.get(position));
                    i.putExtra("name",na.get(position));
                    i.putExtra("logo",lo.get(position));
                    context.startActivity(i);
                }
            });
            Glide.with(context).load(lo.get(position)).asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            hoder.iv_logo.setImageBitmap(resource);
                        }
                    });
            return convertView;
        }
    }

    private class ViewHoder {
        RoundedImageView iv_logo;
        TextView tv_name, tv_time;
        ImageView btn_image;
    }
}
