package com.kxhl.activity.findActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
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
import util.TitleUtil;
import util.UrlLIst;
import view.LoadingDialog;

/**
 * Created by Administrator on 2017/2/8.
 */
public class StorePushActivity extends Activity {
    private ListView mlv;
    private List<String> mLogos;
    private List<String> mNames;
    private List<String> mTimes;
    private List<String> mUrls;
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
    //声明AMapLocationClientOption对象
    AMapLocationClientOption mLocationOption;
    private String mCity;
   private LoadingDialog dialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            msg.what = 0;
            getService((String) SaveData.get(StorePushActivity.this, Config.USERID, ""), mCity);
            dialog.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storepush);
        new TitleUtil(this).setTitleName("店家推荐").setLeftImage(R.drawable.happy_mine_back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        mlv = (ListView) findViewById(R.id.lv_storePush);

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(Config.hasInternet(this)){
            dialog=new LoadingDialog(this);
            dialog.show();
        }else{
            Toast.makeText(this,"网络错误！",Toast.LENGTH_SHORT).show();
        }
        mLocationClient = new AMapLocationClient(this);
        getLocation();
        mlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(StorePushActivity.this,"dian",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(StorePushActivity.this,ProjectActivity.class);
                i.putExtra("urls",mUrls.get(position));
                startActivity(i);
            }
        });
    }

    /**
     * 获取定位
     */
    public void getLocation() {
        //这里以ACCESS_COARSE_LOCATION为例
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);//自定义的code
        }

        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setNeedAddress(true);
        mLocationClient.startLocation();
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
//可在其中解析amapLocation获取相应内容。
                        mCity = aMapLocation.getCity();
                        mHandler.sendEmptyMessage(0);
                        mLocationClient.stopLocation();
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        };
        mLocationClient.setLocationListener(mLocationListener);
    }

    public void getService(String userId, String ct) {
        RequestParams params = new RequestParams();
        params.put("id", userId);
        params.put("city", ct);
        KxhlRestClient.post(UrlLIst.FOUND_SHOP_ACTIVITY, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("店家推荐", response.toString());
                if (statusCode == 200) {
                    try {
                        getJson(response.getJSONArray("f_shop_activity"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(StorePushActivity.this, "网络错误！", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void getJson(JSONArray array) throws JSONException {
        mLogos = new ArrayList<>();
        mNames = new ArrayList<>();
        mTimes = new ArrayList<>();
        mUrls = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            String logo = object.getString("pic");
            String name = object.getString("name");
            String time = object.getString("time");
            String url = object.getString("url");
            mLogos.add(i, logo);
            mTimes.add(i, time);
            mNames.add(i, name);
            mUrls.add(i, url);
        }
        StorePushAdapter adapter = new StorePushAdapter(StorePushActivity.this, mLogos, mNames, mTimes);
        mlv.setAdapter(adapter);
    }

    private class StorePushAdapter extends BaseAdapter {
        LayoutInflater inflater;
        List<String> logs;
        List<String> names;
        List<String> times;
        Context context;

        public StorePushAdapter(Context context, List<String> logs, List<String> names, List<String> times) {
            inflater = LayoutInflater.from(context);
            this.context = context;
            this.logs = logs;
            this.names = names;
            this.times = times;
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
            ViewHoder hoder;
            if (convertView == null) {
                hoder = new ViewHoder();
                convertView = inflater.inflate(R.layout.item_storepush, null);
                hoder.iv = (ImageView) convertView.findViewById(R.id.item_storeLogo);
                hoder.tv_name = (TextView) convertView.findViewById(R.id.item_storeName);
                hoder.tv_time = (TextView) convertView.findViewById(R.id.item_storeTime);
                convertView.setTag(hoder);
            } else {
                hoder = (ViewHoder) convertView.getTag();
            }
            hoder.tv_name.setText(names.get(position));
            hoder.tv_time.setText("活动时间: "+times.get(position));
            Glide.with(context).load(logs.get(position)).into(hoder.iv);
            return convertView;
        }

        private class ViewHoder {
            ImageView iv;
            TextView tv_name, tv_time;
        }
    }
}
