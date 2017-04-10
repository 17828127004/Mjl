package com.kxhl.activity.myActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import util.SaveData;
import util.TimeUtil;
import util.TitleUtil;
import util.UrlLIst;
import view.LoadingDialog;

/**
 * Created by Administrator on 2017/1/16.
 */
public class TimetingActivity  extends Activity{
    private ListView mlv;
    private  List<String> mPaths;//图片
    private List<String> mNames;//名字
    private List<String> mTimeTings;//预约状态
    private List<String> mTimes;//营业时间
    private List<String> mInfor;//信息
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_timeting);
        new TitleUtil(this).setTitleName("我的预约").setLeftImage(R.drawable.happy_mine_back).
                setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                   finish();
                    }
                });

        mlv=(ListView)findViewById(R.id.lv_myTimeting);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Config.hasInternet(this)){
            dialog=new LoadingDialog(this);
            dialog.show();
            pullService();
        }else {
            Toast.makeText(this,"网络错误！",Toast.LENGTH_SHORT).show();
        }

    }

    public void pullService(){
        dialog.dismiss();
        RequestParams params=new RequestParams();
        params.put("id",SaveData.get(this, Config.USERID,""));
        KxhlRestClient.post(UrlLIst.MINE_APPOINTMENT,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(statusCode==200){

                    Log.i("我的预约",response.toString());
                try {
                    JSONArray array=response.getJSONArray("appoint");
                    getJson(array);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(TimetingActivity.this,"网络错误!",Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
    public void getJson(JSONArray array) throws JSONException {
        mNames=new ArrayList<>();
        mTimes=new ArrayList<>();
        mPaths=new ArrayList<>();
        mTimeTings=new ArrayList<>();
        mInfor=new ArrayList<>();

        for(int i=0;i<array.length();i++){
        JSONObject object=array.getJSONObject(i);
            String name=object.getString("name");
            String pic=object.getString("pic");
            String time=object.getString("appointment_time");
            String timeTing=object.getString("appointment_stat");
            String infor=object.getString("appointment_info");
            mNames.add(i,name);
            mPaths.add(i,pic);
            mTimes.add(i,time);
            mTimeTings.add(i,timeTing);
            mInfor.add(i,infor);
        }

        mlv.setAdapter(new MyTimeAdapter(this,mPaths,mNames,mTimeTings,mTimes,mInfor));
    }
private class MyTimeAdapter extends BaseAdapter{
    LayoutInflater inflater;
    public List<String> paths;//图片
    public List<String> names;//名字
    public List<String> timeTing;//预约状态
    public List<String> times;//营业时间
    public List<String> infors;
    public Context context;
    public MyTimeAdapter(Context context,List<String> paths,List<String> names,List<String> timeTing,List<String> times,List<String> infors){
        inflater=LayoutInflater.from(context);
        this.context=context;
        this.paths=paths;
        this.names=names;
        this.timeTing=timeTing;
        this.times=times;
    this.infors=infors;
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
    public android.view.View getView(int position, android.view.View convertView, ViewGroup parent) {
        ViewHoder hoder;
       if(convertView==null){
           hoder=new ViewHoder();
           convertView=inflater.inflate(R.layout.item_my_timeting,null);
           hoder.tv_name=(TextView)convertView.findViewById(R.id.item_myTime_tv);
           hoder.tv_time=(TextView)convertView.findViewById(R.id.item_myTime_time);
           hoder.tv_timeTing=(TextView)convertView.findViewById(R.id.item_myTime_timeTing);
           hoder.iv_logo=(ImageView)convertView.findViewById(R.id.item_myTime_imag);
           hoder.tv_infor=(TextView)convertView.findViewById(R.id.item_timetingInfor);
            convertView.setTag(hoder);
       }else{
           hoder=(ViewHoder)convertView.getTag();
       }
        hoder.tv_name.setText(names.get(position));
        hoder.tv_time.setText(TimeUtil.getStrTime(times.get(position)));

        if(timeTing.get(position).equals("预约中")||timeTing.get(position).equals("确认中")){
            hoder.tv_timeTing.setText(timeTing.get(position));
            hoder.tv_timeTing.setTextColor(Color.rgb(240,96,96));
        }else{
            hoder.tv_timeTing.setText(timeTing.get(position));
            hoder.tv_timeTing.setTextColor(Color.rgb(159,159,159));
        }
        Glide.with(context).load(paths.get(position)).into(hoder.iv_logo);
        hoder.tv_infor.setText(infors.get(position));
        return convertView;
    }
}
    private class ViewHoder{
        TextView tv_name;
        TextView tv_time;
        TextView tv_timeTing;
        ImageView iv_logo;
        TextView tv_infor;
    }
}
