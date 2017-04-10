package fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kxhl.R;
import com.kxhl.activity.HomeActivity.StoreActivity;
import com.kxhl.activity.LoginActivity;
import com.kxhl.activity.findActivity.AllUserActivity;
import com.kxhl.activity.findActivity.DayActivity;
import com.kxhl.activity.findActivity.HotActivity;
import com.kxhl.activity.findActivity.NumStoreActivity;
import com.kxhl.activity.findActivity.RanKingActivity;
import com.kxhl.activity.findActivity.StorePushActivity;
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
 * Created by Administrator on 2017/1/9.
 */
public class FindFragment extends Fragment implements View.OnClickListener {
    private View layoutView;
    private GridView gv_find_hot;//热推
    private GridView gv_find_dayPrice;//每日特价
    private GridView gv_find_users;//都在使用
    private LinearLayout ll_find_store;//积分商城
    private LinearLayout ll_find_storeHot;//门店热榜
    private LinearLayout ll_find_storePull;//店家推荐
    private LinearLayout ll_find_storeHot1;//门店热榜1
    private LinearLayout ll_find_more;
    private LinearLayout ll_find_more1;
    private LinearLayout ll_find_more2;
    private List<String> mLogos;//热
    private List<String> mNames;//热
    private List<String> mAbouts;//热
    private List<String> mUrls;//热
    private List<String> mDayNames;//特价
    private List<String> mDayPaths;//特价
    private List<String> mDayUrls;//特价
    private List<String> mAllNames;//都在用
    private List<String> mAllPaths;//都在用
    private List<String> mAllUrls;//都在用
    private LoadingDialog dialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            msg.what = 00;
            dialog.dismiss();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_find, container, false);
        new TitleUtil(layoutView).setTitleName("发现");
        initView();

        return layoutView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public void initView() {
        ll_find_store = (LinearLayout) layoutView.findViewById(R.id.ll_find_store);
        ll_find_storeHot = (LinearLayout) layoutView.findViewById(R.id.ll_find_storeHot);
        ll_find_storePull = (LinearLayout) layoutView.findViewById(R.id.ll_find_storePull);
        ll_find_storeHot1 = (LinearLayout) layoutView.findViewById(R.id.ll_find_storeHot1);
        ll_find_more = (LinearLayout) layoutView.findViewById(R.id.ll_find_more);
        ll_find_more1 = (LinearLayout) layoutView.findViewById(R.id.ll_find_more1);
        ll_find_more2 = (LinearLayout) layoutView.findViewById(R.id.ll_find_more2);
        gv_find_hot = (GridView) layoutView.findViewById(R.id.gv_find_hot);
        gv_find_dayPrice = (GridView) layoutView.findViewById(R.id.gv_find_dayPrice);
        gv_find_users = (GridView) layoutView.findViewById(R.id.gv_find_users);
    }

    public void initData() {
        ll_find_store.setOnClickListener(this);
        ll_find_storeHot.setOnClickListener(this);
        ll_find_storePull.setOnClickListener(this);
        ll_find_storeHot1.setOnClickListener(this);
        ll_find_more.setOnClickListener(this);
        ll_find_more1.setOnClickListener(this);
        ll_find_more2.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        dialog = new LoadingDialog(getActivity());
        if (Config.hasInternet(getActivity())) {
            dialog.show();
        } else {
            Toast.makeText(getActivity(), "网络错误！", Toast.LENGTH_SHORT).show();
        }
        getFindHot();
        super.onResume();
    }

    public void getFindHot() {
        RequestParams params = new RequestParams();
        KxhlRestClient.post(UrlLIst.FOUND_INDEX, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    Log.i("发现热推", response.toString());
                    try {
                        getJsonHot(response.getJSONArray("f_index"));
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
        KxhlRestClient.post(UrlLIst.FOUND_SP_OFFER, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    Log.i("发现特价", response.toString());
                    try {
                        getJsonDay(response.getJSONArray("sp_offer"));
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
        KxhlRestClient.post(UrlLIst.FOUND_ALL_USED, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    Log.i("都在用", response.toString());
                    try {
                        getJsonAll(response.getJSONArray("all_used"));
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

    public void getJsonHot(JSONArray array) throws JSONException {
        mLogos = new ArrayList<>();
        mNames = new ArrayList<>();
        mAbouts = new ArrayList<>();
        mUrls = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            mLogos.add(i, object.getString("logo"));
            mNames.add(i, object.getString("name"));
            mAbouts.add(i, object.getString("about"));
            mUrls.add(i, object.getString("url"));
        }
        gv_find_hot.setAdapter(new FindHotAdapter(getActivity(), mNames, mLogos, mAbouts));
        gv_find_hot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), HotActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("hot", mUrls.get(position));
                i.putExtras(bundle);
                startActivity(i);
            }
        });
    }

    public void getJsonDay(JSONArray jsonArray) throws JSONException {
        mDayNames = new ArrayList<>();
        mDayPaths = new ArrayList<>();
        mDayUrls = new ArrayList<>();
        for (int j = 0; j < jsonArray.length(); j++) {
            JSONObject jsonObject = jsonArray.getJSONObject(j);
            mDayNames.add(j, jsonObject.getString("name"));
            mDayPaths.add(j, jsonObject.getString("path"));
            mDayUrls.add(j, jsonObject.getString("url"));
        }
        gv_find_dayPrice.setAdapter(new FindHotAdapter(getActivity(), mDayNames, mDayPaths, null));
        gv_find_dayPrice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), DayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("day", mDayUrls.get(position));
                i.putExtras(bundle);
                startActivity(i);
            }
        });
    }

    public void getJsonAll(JSONArray jsonArray) throws JSONException {
        mAllNames = new ArrayList<>();
        mAllPaths = new ArrayList<>();
        mAllUrls = new ArrayList<>();
        for (int k = 0; k < jsonArray.length(); k++) {
            JSONObject object = jsonArray.getJSONObject(k);
            mAllNames.add(k, object.getString("name"));
            mAllPaths.add(k, object.getString("path"));
            mAllUrls.add(k, object.getString("url"));
        }
        gv_find_users.setAdapter(new FindHotAdapter(getActivity(), mAllNames, mAllPaths, null));
        handler.sendEmptyMessage(00);
        gv_find_users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), AllUserActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("all", mAllUrls.get(position));
                i.putExtras(bundle);
                startActivity(i);
            }
        });
    }

    @Override
    public void onClick(View v) {
        String userId= (String) SaveData.get(getActivity(),Config.USERID,"");
        switch (v.getId()) {
            case R.id.ll_find_store://积分商城
                if(userId.equals("")){
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }else {
                    startActivity(new Intent(getActivity(), NumStoreActivity.class));
                }
                break;
            case R.id.ll_find_storeHot://门店热榜
                startActivity(new Intent(getActivity(), RanKingActivity.class));
                break;
            case R.id.ll_find_storePull://店家推荐
                if(userId.equals("")){
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }else {
                    startActivity(new Intent(getActivity(), StorePushActivity.class));
                }
                break;
            case R.id.ll_find_storeHot1://门店热榜
                Toast.makeText(getActivity(),"敬请期待......",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_find_more://查看更多
                break;
            case R.id.ll_find_more1://查看更多
                break;
            case R.id.ll_find_more2://查看更多
                break;
        }
    }
    @Override
    public void onDestroy() {
        try{
            dialog.dismiss();
        }catch (Exception e) {

        }
        super.onDestroy();
    }
    private class FindHotAdapter extends BaseAdapter {
        LayoutInflater inflater;
        List<String> names;
        List<String> logos;
        List<String> abouts;
        Context context;

        public FindHotAdapter(Context context, List<String> names, List<String> logos, List<String> abouts) {
            inflater = LayoutInflater.from(context);
            this.context = context;
            this.names = names;
            this.logos = logos;
            this.abouts = abouts;
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
        public View getView(int position, android.view.View convertView, ViewGroup parent) {
            ViewHoder hoder;
            if (convertView == null) {
                hoder = new ViewHoder();
                convertView = inflater.inflate(R.layout.item_find_hot, null);
                hoder.iv = (ImageView) convertView.findViewById(R.id.item_iv_find1);
                hoder.tv_name = (TextView) convertView.findViewById(R.id.item_tv_find_name1);
                hoder.tv_about = (TextView) convertView.findViewById(R.id.item_tv_find_about);
                convertView.setTag(hoder);
            } else {
                hoder = (ViewHoder) convertView.getTag();
            }
            Glide.with(context).load(logos.get(position)).error(R.drawable.ic_chid).into(hoder.iv);

            hoder.tv_name.setText(names.get(position));
            if (abouts == null) {
            } else {
                hoder.tv_about.setText(abouts.get(position));
            }
            return convertView;
        }
    }

    private class ViewHoder {
        ImageView iv;
        TextView tv_name, tv_about;
    }
}
