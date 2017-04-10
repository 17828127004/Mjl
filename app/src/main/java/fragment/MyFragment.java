package fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.kxhl.R;
import com.kxhl.activity.HomeActivity.GrowUpPhotoActivity;
import com.kxhl.activity.LoginActivity;
import com.kxhl.activity.findActivity.NumStoreActivity;
import com.kxhl.activity.myActivity.MySettingActivity;
import com.kxhl.activity.myActivity.ServiceActivity;
import com.kxhl.activity.myActivity.TimetingActivity;
import com.kxhl.activity.myActivity.VipActivity;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import util.Config;
import util.KxhlRestClient;
import util.SaveData;
import util.TitleUtil;
import util.UrlLIst;
import view.CircleImageView;

/**
 * Created by Administrator on 2017/1/6.
 */
public class MyFragment extends Fragment implements View.OnClickListener {
    private View layoutView;
    private ImageView cImage;
    private RelativeLayout rl_heart;//头像
    private RelativeLayout rl_my_timeting, rl_record, rl_store, rl_service_center;
    private TextView tv_name;
    private ImageView iv_vip;
    private TextView tv_card;//会员卡
    private TextView tv_coupon;//优惠
    private TextView tv_integral;//积分
    private String vipCard;
    private String head;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        layoutView = inflater.inflate((R.layout.fragment_my), container, false);
        new TitleUtil(layoutView).setTitleName("我的");
        initView();

        return layoutView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void onResume() {
        pullService((String) SaveData.get(getActivity(), Config.USERID, ""));
        super.onResume();
    }


    /**
     * 初始化控件
     */
    public void initView() {
        cImage = (ImageView) layoutView.findViewById(R.id.cImage);
        rl_heart = (RelativeLayout) layoutView.findViewById(R.id.rl_heart);
        rl_my_timeting = (RelativeLayout) layoutView.findViewById(R.id.rl_my_timeting);
        rl_record = (RelativeLayout) layoutView.findViewById(R.id.rl_record);
        rl_store = (RelativeLayout) layoutView.findViewById(R.id.rl_store);
        rl_service_center = (RelativeLayout) layoutView.findViewById(R.id.rl_service_center);
        tv_name = (TextView) layoutView.findViewById(R.id.textView);
        iv_vip = (ImageView) layoutView.findViewById(R.id.iv_vip);
        tv_card = (TextView) layoutView.findViewById(R.id.tv_card);
        tv_coupon = (TextView) layoutView.findViewById(R.id.tv_coupon);
        tv_integral = (TextView) layoutView.findViewById(R.id.tv_integral);

    }

    /**
     * 添加点击事件
     */
    public void initData() {
        rl_heart.setOnClickListener(this);
        rl_my_timeting.setOnClickListener(this);
        rl_record.setOnClickListener(this);
        rl_service_center.setOnClickListener(this);
        rl_store.setOnClickListener(this);
        tv_card.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent();
        String userId = (String) SaveData.get(getActivity(), Config.USERID, "");
        switch (v.getId()) {
            case R.id.rl_heart://基本信息
                if (userId.equals("")) {
                    i.setClass(getActivity(), LoginActivity.class);
                } else {
                    i.setClass(getActivity(), MySettingActivity.class);
                    i.putExtra("head", head);
                }
                startActivity(i);
                break;
            case R.id.rl_my_timeting://我的预约
                if (userId.equals("")) {
                    i.setClass(getActivity(), LoginActivity.class);
                } else {
                    i.setClass(getActivity(), TimetingActivity.class);
                }
                startActivity(i);

                break;
            case R.id.rl_record://成长相册
                if (userId.equals("")) {
                    i.setClass(getActivity(), LoginActivity.class);
                } else {
                    i.setClass(getActivity(), GrowUpPhotoActivity.class);
                }
                startActivity(i);


                break;
            case R.id.rl_service_center://服务中心
                i.setClass(getActivity(), ServiceActivity.class);
                startActivity(i);
                break;
            case R.id.rl_store://积分商城
                if (userId.equals("")) {
                    i.setClass(getActivity(), LoginActivity.class);
                } else {
                    i.setClass(getActivity(), NumStoreActivity.class);
                }
                startActivity(i);
                break;
            case R.id.tv_card:
                if (!TextUtils.isEmpty(vipCard)) {
                    i.setClass(getActivity(), VipActivity.class);
                    startActivity(i);
                }
                break;
        }
    }

    public void pullService(String userId) {
        RequestParams params = new RequestParams();
        params.put("id", userId);
        KxhlRestClient.post(UrlLIst.MINE_INDEX, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    Log.i("我的", response.toString());
                    try {
                        String name = response.getJSONObject("index").getString("name");
                        SaveData.put(getActivity(), Config.USERNAME, name);
                        tv_name.setText(name);
                        vipCard = response.getJSONObject("index").getString("vipcard");
                        if (vipCard.equals("")) {
                            tv_card.setText("(非会员)");
                            iv_vip.setVisibility(View.INVISIBLE);
                        } else if (!TextUtils.isEmpty(vipCard)) {
                            SaveData.put(getActivity(), Config.VIP_CARD, vipCard);
                            iv_vip.setVisibility(View.VISIBLE);
                            tv_card.setText(vipCard);
                        }
                        String point = response.getJSONObject("index").getString("point");
                        if (point.equals("")) {
                            tv_integral.setText("(0)");
                        } else {
                            tv_integral.setText(point + "分");
                            SaveData.put(getActivity(), Config.POINT, point);
                        }
                        head = response.getJSONObject("index").getString("path");

                        String favourable = response.getJSONObject("index").getString("favourable");
                        if (favourable.equals("") || favourable.equals("0")) {
                        } else {
                            tv_coupon.setText(favourable);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Glide.with(getActivity()).load(head).asBitmap().error(R.drawable.icon_header).
                            into(new SimpleTarget<Bitmap>() {
                                     @Override
                                     public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                         cImage.setImageBitmap(resource);
                                     }
                                 }
                            );
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getActivity(), "网络错误!", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
