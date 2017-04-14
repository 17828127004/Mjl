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

import util.CircleImage;
import util.Config;
import util.KxhlRestClient;
import util.SaveData;
import util.TitleUtil;
import util.UrlLIst;
import view.LoadingDialog;

/**
 * Created by Administrator on 2017/1/18.
 */
public class MsgActivity extends Activity {
    private ListView lv_msg;
    private List<String> mMsgTexts;
    private List<String> mMsgTimes;
    private List<String> mMsgNames;
    private List<String> mMsgLogos;
    private List<String> mMsgStats;
    private MsgAdapter adapter;
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
        setContentView(R.layout.activity_msg);
        new TitleUtil(this).setTitleName("消息详情").setLeftImage(R.drawable.happy_mine_back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();

                    }
                });
        Config.setTranslucent(this);
        if(Config.hasInternet(this)){
            dialog=new LoadingDialog(this);
            dialog.show();
        }else {
            Toast.makeText(this,"网络错误！",Toast.LENGTH_SHORT).show();
        }
        lv_msg = (ListView) findViewById(R.id.lv_msg);
        getService((String) SaveData.get(this, Config.USERID, ""));
        lv_msg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(MsgActivity.this, MsgTwoActivity.class);
                i.putExtra("MsgName",mMsgNames.get(position));
                i.putExtra("MsgContent",mMsgTexts.get(position));
                startActivity(i);
            }
        });

    }

    public void getService(String userId) {
        RequestParams params = new RequestParams();
        params.put("id", userId);
        KxhlRestClient.post(UrlLIst.INDEX_MESSAGE, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    Log.i("消息中心", response.toString());
                    try {
                        getJson(response.getJSONArray("message"));
                        handler.sendEmptyMessage(00);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(MsgActivity.this, "网络错误!", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void getJson(JSONArray array) throws JSONException {
        mMsgLogos = new ArrayList<>();
        mMsgNames = new ArrayList<>();
        mMsgTexts = new ArrayList<>();
        mMsgTimes = new ArrayList<>();
        mMsgStats=new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            String text = object.getString("text");
            String name = object.getString("name");
            String time = object.getString("time");
            String logo = object.getString("logo");
            String stat=object.getString("stat");
            mMsgTimes.add(i, time);
            mMsgTexts.add(i, text);
            mMsgNames.add(i, name);
            mMsgLogos.add(i, logo);
            mMsgStats.add(i,stat);
        }
         adapter = new MsgAdapter(this,mMsgNames,mMsgTimes,mMsgTexts,mMsgLogos);
        lv_msg.setAdapter(adapter);

    }

    @Override
    protected void onDestroy() {
        dialog.dismiss();
        super.onDestroy();
    }

    private class MsgAdapter extends BaseAdapter {
        LayoutInflater inflater;
        List<String> msgName;
        List<String> msgTime;
        List<String> msgText;
        List<String> msgLogo;
        Context context;
        public MsgAdapter(Context context,List<String> msgName,List<String> msgTime,
                          List<String> msgText,List<String> msgLogo) {
            inflater = LayoutInflater.from(context);
            this.context=context;
            this.msgName=msgName;
            this.msgText=msgText;
            this.msgTime=msgTime;
            this.msgLogo=msgLogo;
            Log.i("消息中心", msgLogo.toString());
        }

        @Override
        public int getCount() {
            return msgName.size();
        }

        @Override
        public Object getItem(int position) {
            return msgName.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHoder hoder;
            if (convertView == null) {
                hoder=new ViewHoder();
                convertView = inflater.inflate(R.layout.item_msg, null);
                hoder.image=(CircleImage)convertView.findViewById(R.id.item_msg_cImage);
                hoder.tv_name=(TextView)convertView.findViewById(R.id.tv_msg_name);
                hoder.tv_content=(TextView)convertView.findViewById(R.id.tv_msg_content);
                hoder.tv_time=(TextView)convertView.findViewById(R.id.tv_msg_time);
                convertView.setTag(hoder);
            }else{
                hoder=(ViewHoder)convertView.getTag();
            }
            hoder.tv_content.setText(msgText.get(position));
            hoder.tv_time.setText(msgTime.get(position));
            hoder.tv_name.setText(msgName.get(position));
            Log.i("消息中心", msgLogo.get(position));
                Glide.with(context).load(msgLogo.get(position)).asBitmap().
                        into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                hoder.image.setImageBitmap(resource);
                            }
                        });


            return convertView;
        }
    }

    private class ViewHoder {
        CircleImage image;
        TextView tv_name, tv_content,tv_time  ;
    }
}
