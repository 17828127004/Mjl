package fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.kxhl.R;
import com.kxhl.activity.HomeActivity.AnswersActivity;
import com.kxhl.activity.HomeActivity.GrowUpPhotoActivity;
import com.kxhl.activity.HomeActivity.LineTimeActivity;
import com.kxhl.activity.HomeActivity.MsgActivity;
import com.kxhl.activity.HomeActivity.NewsActivity;
import com.kxhl.activity.HomeActivity.NewsTwoActivity;
import com.kxhl.activity.HomeActivity.SeePhotoPushActivity;
import com.kxhl.activity.HomeActivity.SeeVRActivity;
import com.kxhl.activity.HomeActivity.TalkActivity;
import com.kxhl.activity.HomeActivity.WebViewActivity;
import com.kxhl.activity.LoginActivity;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.stx.xhb.xbanner.XBanner;

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
import util.UPMarqueeView;
import util.UrlLIst;
import view.CircleImageView;
import view.LoadingDialog;
import view.MyImg;

/**
 * Created by Administrator on 2017/1/6.
 */
public class HomePageFragment extends Fragment implements View.OnClickListener {
    //    private LinearLayout ll_home
    private ImageView iv_homeTimeting;//在线预约
    private ImageView iv_homeTalk;//星级评价
    private ImageView iv_homePhoto;//成长相册
    private ImageView iv_homeMsg;//消息中心
    private ImageView iv_homeAnswer;//你问我答
    private View layoutView;
    private WebView wv_home;
    private List<String> data;
    private List<View> views;
    private List<String> mUrls;
    private LoadingDialog dialog;
    private ScrollView mSv;
    private String mWebINdex = "0";
    private List<String> mBannerUrl = new ArrayList<>();
    private LinearLayout ll_photo;//成长相册布局
    private List<String> mPhotoUrl;//成长相册图片url
    private List<String> mPhotoName;//成长相册name
    private List<String> mPhotoPath;//成长相册path
    private List<String> mPhotoPic;//成长相册pic
    private MyImg iv_home_vr,iv_home_vr1, iv_home_vr2, iv_home_vr3;
    private UPMarqueeView uv;
    private LayoutInflater mInflater;
    // 显示轮播图片
    private XBanner mBannerNet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_homepage, container, false);
        new TitleUtil(layoutView).setTitleName("首页");
        dialog = new LoadingDialog(getActivity());
        dialog.show();
        mInflater = LayoutInflater.from(getActivity());
        return layoutView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initParam();
        getBanner();
        getWeb();
        getPhoto();
        getVip((String) SaveData.get(getActivity(), Config.USER_PHONE, ""));
    }
    @Override
    public void onResume() {
        super.onResume();
        mSv.smoothScrollTo(0, 20);
        mBannerNet.startAutoPlay();
        if (mWebINdex.equals("1")) {
            wv_home.reload();
            mWebINdex = "0";
        }
        getNews();
    }

    /**
     * 实例化控件
     */
    private void initParam() {
        iv_homeTimeting = (ImageView) layoutView.findViewById(R.id.iv_homeTimeting);
        iv_homeTalk = (ImageView) layoutView.findViewById(R.id.iv_homeTalk);
        iv_homePhoto = (ImageView) layoutView.findViewById(R.id.iv_homePhoto);
        iv_homeMsg = (ImageView) layoutView.findViewById(R.id.iv_homeMsg);
        iv_homeAnswer = (ImageView) layoutView.findViewById(R.id.iv_homeAnswer);
        wv_home = (WebView) layoutView.findViewById(R.id.wv_home);
        mSv = (ScrollView) layoutView.findViewById(R.id.home_sv);
        ll_photo = (LinearLayout) layoutView.findViewById(R.id.ll_home);
        iv_home_vr = (MyImg) layoutView.findViewById(R.id.iv_home_vr);
        iv_home_vr1 = (MyImg) layoutView.findViewById(R.id.iv_home_vr1);
        iv_home_vr2 = (MyImg) layoutView.findViewById(R.id.iv_home_vr2);
        iv_home_vr3 = (MyImg) layoutView.findViewById(R.id.iv_home_vr3);
        mBannerNet=(XBanner)layoutView.findViewById(R.id.banner);
        uv=(UPMarqueeView)layoutView.findViewById(R.id.uv);
        iv_home_vr.setColor(0x38000000);
        iv_home_vr1.setColor(0x38000000);
        iv_home_vr2.setColor(0x38000000);
        iv_home_vr3.setColor(0x38000000);
        iv_homeTimeting.setOnClickListener(this);
        iv_homeTalk.setOnClickListener(this);
        iv_homeAnswer.setOnClickListener(this);
        iv_homeMsg.setOnClickListener(this);
        iv_homePhoto.setOnClickListener(this);
        iv_home_vr.setOnClickListener(this);
        iv_home_vr1.setOnClickListener(this);
        iv_home_vr2.setOnClickListener(this);
        iv_home_vr3.setOnClickListener(this);
    }


    /**
     * 获取首页banner数据
     */
    public void getBanner() {
        RequestParams params = new RequestParams();
        KxhlRestClient.post(UrlLIst.INDEX_SHUFFING, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    Log.i("首页banner", response.toString());
                    try {
                        JSONArray array = response.getJSONArray("pic");
                        for (int i = 0; i < array.length(); i++) {
                            mBannerUrl.add(i, array.getString(i));
                        }
                mBannerNet.setData(mBannerUrl,null);
                        mBannerNet.setmAdapter(new XBanner.XBannerAdapter() {
                            @Override
                            public void loadBanner(XBanner banner, View view, int position) {
                                Glide.with(getActivity()).load(mBannerUrl.get(position)).into((ImageView) view);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("首页banner失败", responseString);
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void getPhoto() {
        RequestParams params = new RequestParams();
        KxhlRestClient.post(UrlLIst.INDEX_GROW_SHOW, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    Log.i("获取首页相册数据", response.toString());
                    try {
                        getJson(response.getJSONArray("grow_show"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void getJson(JSONArray array) throws JSONException {
        mPhotoUrl = new ArrayList<>();
        mPhotoName = new ArrayList<>();
        mPhotoPath = new ArrayList<>();
        mPhotoPic = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            mPhotoUrl.add(i, object.getString("url"));
            mPhotoName.add(i, object.getString("name"));
            mPhotoPath.add(i, object.getString("path"));
            mPhotoPic.add(i, (String) object.getJSONArray("pic").get(0));
        }
        for (int j = 0; j < mPhotoName.size(); j++) {
            View view = mInflater.inflate(R.layout.item_home_photo, null);
            MyImg imageView = (MyImg) view.findViewById(R.id.item_iv_home);
            imageView.setColor(0x38000000);
            Glide.with(getActivity()).load(mPhotoPic.get(j)).into(imageView);
            final CircleImageView circleImage = (CircleImageView) view.findViewById(R.id.item_ci_home);
            Glide.with(getActivity()).load(mPhotoPath.get(j)).asBitmap().
                    into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            circleImage.setImageBitmap(resource);
                        }
                    });
            TextView tv = (TextView) view.findViewById(R.id.item_tv_home);
            tv.setText(mPhotoName.get(j));

            final int finalJ = j;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), SeePhotoPushActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("photo", mPhotoUrl.get(finalJ));
                    i.putExtras(bundle);
                    startActivity(i);
                }
            });
            ll_photo.addView(view);
        }
    }


    public void getWeb() {
        WebSettings webSettings = wv_home.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);// 设置支持缩放
        webSettings.setSupportZoom(false);// 不支持缩放
        webSettings.setUseWideViewPort(false);// 将图片调整到适合webview大小
        webSettings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小
        wv_home.loadUrl("http://www.chaojimatou.com/mobile/goods_cat.php?c_id=1731&nohead=1");

        wv_home.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    dialog.dismiss();
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        wv_home.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                mWebINdex = "1";
            }
        });
    }

    /**
     * 初始化界面程序
     */
    private void initView() {
        setView();
        uv.setViews(views);
        /**
         * 设置item_view的监听
         */
        uv.setOnItemClickListener(new UPMarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
////                Toast.makeText(getActivity(), "你点击了第几个items" + position, Toast.LENGTH_SHORT).show();
//                Intent i=new Intent(getActivity(),NewsTwoActivity.class);
//                    Bundle bundle=new Bundle();
//                    bundle.putString("newTwo",mUrls.get(position));
//                    i.putExtras(bundle);
//                    startActivity(i);
            }
        });
    }

    /**
     * 初始化需要循环的View
     * 为了灵活的使用滚动的View，所以把滚动的内容让用户自定义
     * 假如滚动的是三条或者一条，或者是其他，只需要把对应的布局，和这个方法稍微改改就可以了，
     */
    private void setView() {
        for (int i = 0; i < data.size(); i = i + 2) {
            final int position = i;
            //设置滚动的单个布局
            LinearLayout moreView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_view, null);
            //初始化布局的控件
            TextView tv1 = (TextView) moreView.findViewById(R.id.tv1);
            TextView tv2 = (TextView) moreView.findViewById(R.id.tv2);

            /**
             * 设置监听
             */
            moreView.findViewById(R.id.rl).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(getActivity(), position + "你点击了" + data.get(position).toString(), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getActivity(), NewsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("new", mUrls.get(position));
                    i.putExtras(bundle);
                    startActivity(i);
                }
            });
            /**
             * 设置监听
             */
            moreView.findViewById(R.id.rl2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(getActivity(), position + "你点击了" + data.get(position).toString(), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getActivity(), NewsTwoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("newTwo", mUrls.get(position));
                    i.putExtras(bundle);
                    startActivity(i);
                }
            });
            tv1.setText("");
            tv2.setText("");
            if (tv1.getText().toString().equals(data.get(i).toString())) {

            } else {
                //进行对控件赋值
                tv1.setText(data.get(i).toString());
                if (data.size() > i + 1) {
                    //因为淘宝那儿是两条数据，但是当数据是奇数时就不需要赋值第二个，所以加了一个判断，还应该把第二个布局给隐藏掉
                    tv2.setText(data.get(i + 1).toString());
                } else {
                    moreView.findViewById(R.id.rl2).setVisibility(View.GONE);
                }

                //添加到循环滚动数组里面去
                views.add(moreView);
            }

        }
    }

    /**
     * 初始化数据
     */
    private void initdata(JSONArray array) throws JSONException {
        if (data == null && mUrls == null) {
            data = new ArrayList<>();
            mUrls = new ArrayList<>();
            views = new ArrayList<>();
        } else {
            data.clear();
            mUrls.clear();
            views.clear();
        }
        if (data.size() == 0) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                data.add(i, object.getString("title"));
                mUrls.add(i, object.getString("url"));
            }
            initView();
        }
    }

    @Override
    public void onClick(View v) {
        String s = (String) SaveData.get(getActivity(), Config.USERID, "");
        String vip = (String) SaveData.get(getActivity(), Config.VIP_CHECK, "");
        switch (v.getId()) {
            case R.id.iv_homeTimeting:
                    startActivity(new Intent(getActivity(), LineTimeActivity.class));
                break;
            case R.id.iv_homeTalk:
                if (s.equals("")) {
                    Toast.makeText(getActivity(), "请先登录!", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(getActivity(), TalkActivity.class));
                }

                break;
            case R.id.iv_homeAnswer:
                if (s.equals("")) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else if (vip.equals("200")) {
                    startActivity(new Intent(getActivity(), AnswersActivity.class));
                } else if (vip.equals("400")) {
                    Toast.makeText(getActivity(), "请升级为会员", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.iv_homeMsg:
                    startActivity(new Intent(getActivity(), MsgActivity.class));
                break;
            case R.id.iv_homePhoto:
                if (s.equals("")) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), GrowUpPhotoActivity.class));
                }

                break;
            case R.id.iv_home_vr:
            case R.id.iv_home_vr1:
            case R.id.iv_home_vr2:
            case R.id.iv_home_vr3:
                startActivity(new Intent(getActivity(), SeeVRActivity.class));
                break;
        }
    }

    public void getVip(String userPhone) {
        RequestParams params = new RequestParams();
        params.put("phone", userPhone);
        KxhlRestClient.post(UrlLIst.FOUND_CHECK_VIP, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    Log.i("判断vip", response.toString());
                    try {
                        String vip = response.getString("vip");
                        SaveData.put(getActivity(), Config.VIP_CHECK, vip);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    /**
     * 获取首页新闻数据
     */
    public void getNews() {
        RequestParams params = new RequestParams();
        KxhlRestClient.post(UrlLIst.INDEX_INDEX, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    Log.i("新闻首页", response.toString());
                    try {
                        initdata(response.getJSONArray("index"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    @Override
    public void onDestroy() {
        mBannerNet.stopAutoPlay();
        try {
            dialog.dismiss();
        } catch (Exception e) {

        }
        super.onDestroy();
    }


}
