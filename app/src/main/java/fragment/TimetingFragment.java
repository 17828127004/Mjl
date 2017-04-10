package fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.kxhl.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.MyFragmentPagerAdapter;
import util.Config;
import util.KxhlRestClient;
import util.SaveData;
import util.TimeUtil;
import util.TitleUtil;
import util.UrlLIst;
import view.LoadingDialog;

/**
 * Created by Administrator on 2017/1/9.
 */
public class TimetingFragment extends Fragment {
    private Resources resources;
    private View layoutView;
    private RelativeLayout rl_timeOne, rl_timeTwo, rl_timeThree, rl_timeFour, rl_timeFive;
    private TextView tv_month1, tv_day1, tv_month2, tv_day2, tv_month3, tv_day3, tv_month4, tv_day4, tv_month5, tv_day5;
    private ArrayList<Fragment> fragments;
    private ViewPager mVp;
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
    //声明AMapLocationClientOption对象
    AMapLocationClientOption mLocationOption;
    private String mCity;
    private String mLat;//经度
    private String mLng;//纬度
    private LoadingDialog dialog;

    public ArrayList<String> mNames;//店铺名字
    public List<String> mPaths;//店铺log
    public List<String> mTimes;//营业时间
    public List<String> mStoreId;//店铺id
    public List<String> mDistance;//距离
    public List<String> mStart;//星级
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    pushService((String) SaveData.get(getActivity(), Config.USERID, ""), mCity, mLat, mLng);
                    dialog.dismiss();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_timeting, container, false);
        new TitleUtil(layoutView).setTitleName("预约");
        resources = getResources();
        mLocationClient = new AMapLocationClient(getContext());
        initView(layoutView);
        initTime();

        return layoutView;
    }

    public void initView(View view) {
        rl_timeOne = (RelativeLayout) view.findViewById(R.id.rl_timeOne);
        rl_timeTwo = (RelativeLayout) view.findViewById(R.id.rl_timeTwo);
        rl_timeThree = (RelativeLayout) view.findViewById(R.id.rl_timeThree);
        rl_timeFour = (RelativeLayout) view.findViewById(R.id.rl_timeFour);
        rl_timeFive = (RelativeLayout) view.findViewById(R.id.rl_timeFive);
        tv_month1 = (TextView) view.findViewById(R.id.item_tvMonth1);
        tv_day1 = (TextView) view.findViewById(R.id.item_tvDay1);
        tv_month2 = (TextView) view.findViewById(R.id.item_tvMonth2);
        tv_day2 = (TextView) view.findViewById(R.id.item_tvDay2);
        tv_month3 = (TextView) view.findViewById(R.id.item_tvMonth3);
        tv_day3 = (TextView) view.findViewById(R.id.item_tvDay3);
        tv_month4 = (TextView) view.findViewById(R.id.item_tvMonth4);
        tv_day4 = (TextView) view.findViewById(R.id.item_tvDay4);
        tv_month5 = (TextView) view.findViewById(R.id.item_tvMonth5);
        tv_day5 = (TextView) view.findViewById(R.id.item_tvDay5);
        rl_timeOne.setOnClickListener(new MyOnClickListener(0));
        rl_timeTwo.setOnClickListener(new MyOnClickListener(1));
        rl_timeThree.setOnClickListener(new MyOnClickListener(2));
        rl_timeFour.setOnClickListener(new MyOnClickListener(3));
        rl_timeFive.setOnClickListener(new MyOnClickListener(4));
        mVp = (ViewPager) view.findViewById(R.id.vp_timeTing);
    }

    @Override
    public void onResume() {
        super.onResume();
        dialog = new LoadingDialog(getActivity());
        if (Config.hasInternet(getActivity())) {
            dialog.show();
        } else {
            Toast.makeText(getActivity(), "网络错误！", Toast.LENGTH_SHORT).show();
        }
        getLocation();
    }
    @Override
    public void onDestroy() {
        try{
            dialog.dismiss();
        }catch (Exception e) {

        }
        super.onDestroy();
    }
    class MyOnClickListener implements View.OnClickListener {
        int index = 0;

        public MyOnClickListener(int i) {
            this.index = i;
        }

        @Override
        public void onClick(View v) {
            mVp.setCurrentItem(index);
        }
    }

    class MyOnPageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    rl_timeOne.setBackgroundResource(R.drawable.timeing);
                    tv_month1.setTextColor(resources.getColor(R.color.time_title));
//                    tv_day1.setTextColor(resources.getColor(R.color.white));
                    tv_month2.setTextColor(resources.getColor(R.color.tixt));
//                    tv_day2.setTextColor(resources.getColor(R.color.time_txt));
                    tv_month3.setTextColor(resources.getColor(R.color.tixt));
//                    tv_day3.setTextColor(resources.getColor(R.color.time_txt));
                    tv_month4.setTextColor(resources.getColor(R.color.tixt));
//                    tv_day4.setTextColor(resources.getColor(R.color.time_txt));
                    tv_month5.setTextColor(resources.getColor(R.color.tixt));
//                    tv_day5.setTextColor(resources.getColor(R.color.time_txt));
                    rl_timeThree.setBackgroundResource(R.drawable.timeing_no);
                    rl_timeFour.setBackgroundResource(R.drawable.timeing_no);
                    rl_timeFive.setBackgroundResource(R.drawable.timeing_no);
                    rl_timeTwo.setBackgroundResource(R.drawable.timeing_no);
                    break;
                case 1:
                    rl_timeTwo.setBackgroundResource(R.drawable.timeing);
                    tv_month1.setTextColor(resources.getColor(R.color.tixt));
//                    tv_day1.setTextColor(resources.getColor(R.color.time_txt));
                    tv_month2.setTextColor(resources.getColor(R.color.time_title));
//                    tv_day2.setTextColor(resources.getColor(R.color.white));
                    tv_month3.setTextColor(resources.getColor(R.color.tixt));
//                    tv_day3.setTextColor(resources.getColor(R.color.time_txt));
                    tv_month4.setTextColor(resources.getColor(R.color.tixt));
//                    tv_day4.setTextColor(resources.getColor(R.color.time_txt));
                    tv_month5.setTextColor(resources.getColor(R.color.tixt));
//                    tv_day5.setTextColor(resources.getColor(R.color.time_txt));
                    rl_timeThree.setBackgroundResource(R.drawable.timeing_no);
                    rl_timeFour.setBackgroundResource(R.drawable.timeing_no);
                    rl_timeFive.setBackgroundResource(R.drawable.timeing_no);
                    rl_timeOne.setBackgroundResource(R.drawable.timeing_no);
                    break;
                case 2:
                    rl_timeTwo.setBackgroundResource(R.drawable.timeing_no);
                    rl_timeThree.setBackgroundResource(R.drawable.timeing);
                    tv_month1.setTextColor(resources.getColor(R.color.tixt));
//                    tv_day1.setTextColor(resources.getColor(R.color.time_txt));
                    tv_month2.setTextColor(resources.getColor(R.color.tixt));
//                    tv_day2.setTextColor(resources.getColor(R.color.time_txt));
                    tv_month3.setTextColor(resources.getColor(R.color.time_title));
//                    tv_day3.setTextColor(resources.getColor(R.color.white));
                    tv_month4.setTextColor(resources.getColor(R.color.tixt));
//                    tv_day4.setTextColor(resources.getColor(R.color.time_txt));
                    tv_month5.setTextColor(resources.getColor(R.color.tixt));
//                    tv_day5.setTextColor(resources.getColor(R.color.time_txt));
                    rl_timeFour.setBackgroundResource(R.drawable.timeing_no);
                    rl_timeFive.setBackgroundResource(R.drawable.timeing_no);
                    rl_timeOne.setBackgroundResource(R.drawable.timeing_no);
                    break;
                case 3:
                    rl_timeTwo.setBackgroundResource(R.drawable.timeing_no);
                    rl_timeThree.setBackgroundResource(R.drawable.timeing_no);
                    rl_timeFour.setBackgroundResource(R.drawable.timeing);
                    tv_month1.setTextColor(resources.getColor(R.color.tixt));
//                    tv_day1.setTextColor(resources.getColor(R.color.time_txt));
                    tv_month2.setTextColor(resources.getColor(R.color.tixt));
//                    tv_day2.setTextColor(resources.getColor(R.color.time_txt));
                    tv_month3.setTextColor(resources.getColor(R.color.tixt));
//                    tv_day3.setTextColor(resources.getColor(R.color.time_txt));
                    tv_month4.setTextColor(resources.getColor(R.color.time_title));
//                    tv_day4.setTextColor(resources.getColor(R.color.white));
                    tv_month5.setTextColor(resources.getColor(R.color.tixt));
//                    tv_day5.setTextColor(resources.getColor(R.color.time_txt));
                    rl_timeFive.setBackgroundResource(R.drawable.timeing_no);
                    rl_timeOne.setBackgroundResource(R.drawable.timeing_no);
                    break;
                case 4:
                    rl_timeTwo.setBackgroundResource(R.drawable.timeing_no);
                    rl_timeThree.setBackgroundResource(R.drawable.timeing_no);
                    rl_timeFour.setBackgroundResource(R.drawable.timeing_no);
                    rl_timeFive.setBackgroundResource(R.drawable.timeing);
                    tv_month1.setTextColor(resources.getColor(R.color.tixt));
//                    tv_day1.setTextColor(resources.getColor(R.color.time_txt));
                    tv_month2.setTextColor(resources.getColor(R.color.tixt));
//                    tv_day2.setTextColor(resources.getColor(R.color.time_txt));
                    tv_month3.setTextColor(resources.getColor(R.color.tixt));
//                    tv_day3.setTextColor(resources.getColor(R.color.time_txt));
                    tv_month4.setTextColor(resources.getColor(R.color.tixt));
//                    tv_day4.setTextColor(resources.getColor(R.color.time_txt));
                    tv_month5.setTextColor(resources.getColor(R.color.time_title));
//                    tv_day5.setTextColor(resources.getColor(R.color.white));
                    rl_timeOne.setBackgroundResource(R.drawable.timeing_no);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 初始化时间
     */
    public void initTime() {
        String oneMonth = TimeUtil.nearDate(0).substring(5, 7);
        tv_month1.setText(oneMonth + "月");
        String oneDay = TimeUtil.nearDate(0).substring(8, 10);
        tv_day1.setText(oneDay);
        String twoMonth = TimeUtil.nearDate(1).substring(5, 7);
        tv_month2.setText(twoMonth + "月");
        String twoDay = TimeUtil.nearDate(1).substring(8, 10);
        tv_day2.setText(twoDay);
        String threeMonth = TimeUtil.nearDate(2).substring(5, 7);
        tv_month3.setText(threeMonth + "月");
        String threeDay = TimeUtil.nearDate(2).substring(8, 10);
        tv_day3.setText(threeDay);
        String fourMonth = TimeUtil.nearDate(3).substring(5, 7);
        tv_month4.setText(fourMonth + "月");
        String fourDay = TimeUtil.nearDate(3).substring(8, 10);
        tv_day4.setText(fourDay);
        String fiveMonth = TimeUtil.nearDate(4).substring(5, 7);
        tv_month5.setText(fiveMonth + "月");
        String fiveDay = TimeUtil.nearDate(4).substring(8, 10);
        tv_day5.setText(fiveDay);
    }

    /**
     * 获取定位
     */
    public void getLocation() {
        //这里以ACCESS_COARSE_LOCATION为例
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
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
                        mLat = String.valueOf(aMapLocation.getLatitude());
                        mLng = String.valueOf(aMapLocation.getLongitude());
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


    public void pushService(String userId, String city, String lat, String lng) {
        RequestParams params = new RequestParams();

        params.put("id", userId);
        params.put("city", city);
        params.put("lat", lng);//服务器搞反了
        params.put("lng", lat);//服务器搞反了
        Log.i("定位上传位置", params.toString());
        KxhlRestClient.post(UrlLIst.APPOINTMENT_INDEX, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    try {
                        JSONArray array = response.getJSONArray("index");
                        DataJson(array);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("tag+++++....", response.toString());
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    /**
     * 解析json数组
     */
    public void DataJson(JSONArray array) throws JSONException {
        mNames = new ArrayList<>();
        mPaths = new ArrayList<>();
        mTimes = new ArrayList<>();
        mStoreId = new ArrayList<>();
        mStart = new ArrayList<>();
        mDistance = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = (JSONObject) array.get(i);
            String name = object.getString("name");
            String logo = object.getString("logo");
            String time = object.getString("time");
            String id = object.getString("id");
            String start = object.getString("star");
            String distance = object.getString("distance");
            mNames.add(i, name);
            mPaths.add(i, logo);
            mTimes.add(i, time);
            mStoreId.add(i, id);
            mStart.add(i, start);
            mDistance.add(i, distance);
        }
        fragments = new ArrayList<>();
        TimeOneFragment timeOneFragment =new  TimeOneFragment(mNames,mPaths,mTimes,mStoreId,mDistance,mStart);
        TimeTwoFragment timeTwoFragment = new TimeTwoFragment(mNames,mPaths,mTimes,mStoreId,mDistance,mStart);
        TimeThreeFragment timeThreeFragment = new TimeThreeFragment(mNames,mPaths,mTimes,mStoreId,mDistance,mStart);
        TimeFourFragment timeFourFragment = new TimeFourFragment(mNames,mPaths,mTimes,mStoreId,mDistance,mStart);
        TimeFiveFragment timeFiveFragment = new TimeFiveFragment(mNames,mPaths,mTimes,mStoreId,mDistance,mStart);

//        TimeOneFragment timeOneFragment = TimeOneFragment.newInstance(mNames,mPaths,mTimes,mStoreId,mDistance,mStart);
//        TimeTwoFragment timeTwoFragment = TimeTwoFragment.newInstance(mNames,mPaths,mTimes,mStoreId,mDistance,mStart);
//        TimeThreeFragment timeThreeFragment = TimeThreeFragment.newInstance(mNames,mPaths,mTimes,mStoreId,mDistance,mStart);
//        TimeFourFragment timeFourFragment = TimeFourFragment.newInstance(mNames,mPaths,mTimes,mStoreId,mDistance,mStart);
//        TimeFiveFragment timeFiveFragment = TimeFiveFragment.newInstance (mNames,mPaths,mTimes,mStoreId,mDistance,mStart);
        fragments.add(timeOneFragment);
        fragments.add(timeTwoFragment);
        fragments.add(timeThreeFragment);
        fragments.add(timeFourFragment);
        fragments.add(timeFiveFragment);
        mVp.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(), fragments));
        mVp.setOnPageChangeListener(new MyOnPageListener());
        mVp.setCurrentItem(0);
    }


}
