package com.kxhl.activity.HomeActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import util.Config;
import util.KxhlRestClient;
import util.ListViewForScrollView;
import util.SaveData;
import util.TitleUtil;
import util.UrlLIst;
import util.entity.Grow;
import util.entity.Pic;
import view.LoadingDialog;

/**
 * Created by Administrator on 2017/1/20.
 */
public class GrowUpPhotoActivity extends Activity {
    private ListViewForScrollView lv_growUpPhoto;
    private ImageView iv_growUP_PhotoCamera;
    private String mUserHead;//头像
    private String mUserName;
    private ImageView mIv_head;
    private TextView mTv_name;
    private List<Grow> list_data;
    private LoadingDialog dialog;
    private Handler mhander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            msg.what = 00;
            switch (msg.what) {
                case 00:
                    mTv_name.setText(mUserName);
                    Glide.with(GrowUpPhotoActivity.this).load(mUserHead).into(mIv_head);
                    dialog.dismiss();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_growup_photo);
        new TitleUtil(this).setTitleName("成长相册").setLeftImage(R.drawable.happy_mine_back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        iv_growUP_PhotoCamera = (ImageView) findViewById(R.id.iv_growUP_PhotoCamera);
        mIv_head = (ImageView) findViewById(R.id.iv_growUP_PhotoHeard);
        mTv_name = (TextView) findViewById(R.id.tv_growup_name);
        iv_growUP_PhotoCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GrowUpPhotoActivity.this, UploadActivity.class));
            }
        });
        lv_growUpPhoto = (ListViewForScrollView) findViewById(R.id.lv_grow_photo);
        lv_growUpPhoto.setFocusable(false);
//        pushService();

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(Config.hasInternet(this)){
            dialog = new LoadingDialog(this);
            dialog.show();
            pushService();
        }else{
            Toast.makeText(this,"网络错误！",Toast.LENGTH_SHORT).show();
        }
    }

    public void pushService() {
        RequestParams params = new RequestParams();
        params.put("id", SaveData.get(this, Config.USERID, ""));
        KxhlRestClient.post(UrlLIst.MINE_GROW, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    Log.i("成长记录", response.toString());
                    try {
                        getJson(response.getJSONArray("grow"));
                        mUserHead = response.getString("path");
                        mUserName = response.getString("name");
                        mhander.sendEmptyMessage(00);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(GrowUpPhotoActivity.this, "网络错误！", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void getJson(JSONArray array) throws JSONException {
        list_data = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            Grow grow = new Grow();
            grow.setText(object.getString("text_record"));
            grow.setTime(object.getString("time_record"));
            List<Pic> pic = new ArrayList<>();
            JSONArray array1 = object.getJSONArray("pic");
            for (int j = 0; j < array1.length(); j++) {
                Pic pic1 = new Pic();
                pic1.setPic(array1.getString(j));
                pic.add(pic1);
            }
            grow.setPic(pic);
            list_data.add(grow);

        }
        if (list_data.get(0).getTime().equals("")) {

        } else {
            lv_growUpPhoto.setAdapter(new GrowUpAdapter(GrowUpPhotoActivity.this, list_data));
        }

        lv_growUpPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(GrowUpPhotoActivity.this, SeePhotoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(SeePhotoActivity.EXTRA_IMAGE_URLS, (Serializable) list_data.get(position).getPic());
//                i.putStringArrayListExtra(EXTRA_PHOTOS,);
                bundle.putInt(SeePhotoActivity.EXTRA_IMAGE_INDEX, position);
                i.putExtras(bundle);
                Log.i("传了哪些数据", list_data.get(position).getPic().toString());
                startActivity(i);

            }
        });
    }

    private class GrowUpAdapter extends BaseAdapter {
        LayoutInflater inflater;
        List<Grow> list_datas;
        Context context;

        public GrowUpAdapter(Context context, List<Grow> list_datas) {
            inflater = LayoutInflater.from(context);
            this.list_datas = list_datas;
            this.context = context;
            Log.i("",list_datas.toString());
        }


        @Override
        public int getCount() {
            return list_datas.size();
        }

        @Override
        public Object getItem(int position) {
            return list_datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public android.view.View getView(final int position, android.view.View convertView, ViewGroup parent) {
            ViewHoder1 hoder1=null;
            ViewHoder2 hoder2=null;
            ViewHoder3 hoder3=null;
            ViewHoder4 hoder4=null;
            if(convertView == null){
    if(list_datas.get(position).getPic().size() == 1){
        hoder1=new ViewHoder1();
        convertView = inflater.inflate(R.layout.item_growup, null);
        hoder1.tv_day1 = (TextView) convertView.findViewById(R.id.tv_growup_day);
        hoder1.tvmonth1 = (TextView) convertView.findViewById(R.id.tv_growup_month);
        hoder1.tv_text1 = (TextView) convertView.findViewById(R.id.tv_group_text);
        hoder1.iv1 = (ImageView) convertView.findViewById(R.id.iv_grop1);
        convertView.setTag(hoder1);

    }else if(list_datas.get(position).getPic().size() == 2){
        hoder2 = new ViewHoder2();
        convertView = inflater.inflate(R.layout.item_growup2, null);
        hoder2.tv_day2 = (TextView) convertView.findViewById(R.id.tv_growup_day);
        hoder2.tvmonth2 = (TextView) convertView.findViewById(R.id.tv_growup_month);
        hoder2.tv_text2 = (TextView) convertView.findViewById(R.id.tv_group_text);
        hoder2.iv2_1 = (ImageView) convertView.findViewById(R.id.iv_grop2_1);
        hoder2.iv2_2 = (ImageView) convertView.findViewById(R.id.iv_grop2_2);
        convertView.setTag(hoder2);
    }else if(list_datas.get(position).getPic().size() == 3){
        hoder3 = new ViewHoder3();
        convertView = inflater.inflate(R.layout.item_growup3, null);
        hoder3.tv_day3 = (TextView) convertView.findViewById(R.id.tv_growup_day);
        hoder3.tvmonth3 = (TextView) convertView.findViewById(R.id.tv_growup_month);
        hoder3.tv_text3 = (TextView) convertView.findViewById(R.id.tv_group_text);
        hoder3.iv3_1 = (ImageView) convertView.findViewById(R.id.iv_grop3_1);
        hoder3.iv3_2 = (ImageView) convertView.findViewById(R.id.iv_grop3_2);
        hoder3.iv3_3 = (ImageView) convertView.findViewById(R.id.iv_grop3_33);
        convertView.setTag(hoder3);
    }else if(list_datas.get(position).getPic().size() >=4){
        hoder4=new ViewHoder4();
        convertView = inflater.inflate(R.layout.item_growup4, null);
        hoder4.tv_day4 = (TextView) convertView.findViewById(R.id.tv_growup_day4);
        hoder4.tvmonth4 = (TextView) convertView.findViewById(R.id.tv_growup_month);
        hoder4.tv_text4 = (TextView) convertView.findViewById(R.id.tv_group_text);
        hoder4.iv4_1 = (ImageView) convertView.findViewById(R.id.iv_grop4_1);
        hoder4.iv4_2 = (ImageView) convertView.findViewById(R.id.iv_grop4_2);
        hoder4.iv4_3 = (ImageView) convertView.findViewById(R.id.iv_grop4_3);
        hoder4.iv4_4 = (ImageView) convertView.findViewById(R.id.iv_grop4_4);
        convertView.setTag(hoder4);
    }else {
        if(list_datas.get(position).getPic().size() == 1){
            hoder1 = (ViewHoder1) convertView.getTag();
        }else if(list_datas.get(position).getPic().size() == 2){
            hoder2 = (ViewHoder2) convertView.getTag();
        }else if(list_datas.get(position).getPic().size() ==3){
            hoder3 = (ViewHoder3) convertView.getTag();
        }else if(list_datas.get(position).getPic().size() >=4){
            hoder4 = (ViewHoder4) convertView.getTag();
        }
    }
                if(list_datas.get(position).getPic().size() == 1){
                    hoder1.tv_day1.setText(list_datas.get(position).getTime().substring(8, 10));
                    String mo = list_datas.get(position).getTime().substring(5, 6);
                    String th = list_datas.get(position).getTime().substring(6, 7);
                    if (mo.equals("0")) {
                        hoder1.tvmonth1.setText(th + "月");
                    } else {
                        hoder1.tvmonth1.setText(mo + th + "月");
                    }
                    hoder1.tv_text1.setText(list_datas.get(position).getText());

                    Glide.with(context).load(list_data.get(position).getPic().get(0).getPic()).into(hoder1.iv1);
                }else  if(list_datas.get(position).getPic().size() == 2){
                    hoder2.tv_day2.setText(list_datas.get(position).getTime().substring(8, 10));
                    String mo = list_datas.get(position).getTime().substring(5, 6);
                    String th = list_datas.get(position).getTime().substring(6, 7);
                    if (mo.equals("0")) {
                        hoder2.tvmonth2.setText(th + "月");
                    } else {
                        hoder2.tvmonth2.setText(mo + th + "月");
                    }
                    hoder2.tv_text2.setText(list_datas.get(position).getText());
                    Glide.with(context).load(list_data.get(position).getPic().get(0).getPic()).into(hoder2.iv2_1);
                    Glide.with(context).load(list_data.get(position).getPic().get(1).getPic()).into(hoder2.iv2_2);
                }else if(list_datas.get(position).getPic().size() == 3){
                    hoder3.tv_day3.setText(list_datas.get(position).getTime().substring(8, 10));
                    String mo = list_datas.get(position).getTime().substring(5, 6);
                    String th = list_datas.get(position).getTime().substring(6, 7);
                    if (mo.equals("0")) {
                        hoder3.tvmonth3.setText(th + "月");
                    } else {
                        hoder3.tvmonth3.setText(mo + th + "月");
                    }
                    hoder3.tv_text3.setText(list_datas.get(position).getText());
                    Glide.with(context).load(list_data.get(position).getPic().get(0).getPic()).into(hoder3.iv3_1);
                    Glide.with(context).load(list_data.get(position).getPic().get(1).getPic()).into(hoder3.iv3_2);
                    Glide.with(context).load(list_data.get(position).getPic().get(2).getPic()).into(hoder3.iv3_3);
                }else if(list_datas.get(position).getPic().size() >=4){
                    hoder4.tv_day4.setText(list_datas.get(position).getTime().substring(8, 10));
                    String mo = list_datas.get(position).getTime().substring(5, 6);
                    String th = list_datas.get(position).getTime().substring(6, 7);
                    if (mo.equals("0")) {
                        hoder4.tvmonth4.setText(th + "月");
                    } else {
                        hoder4.tvmonth4.setText(mo + th + "月");
                    }
                    hoder4.tv_text4.setText(list_datas.get(position).getText());
                    Glide.with(context).load(list_data.get(position).getPic().get(0).getPic()).into(hoder4.iv4_1);
                    Glide.with(context).load(list_data.get(position).getPic().get(1).getPic()).into(hoder4.iv4_2);
                    Glide.with(context).load(list_data.get(position).getPic().get(2).getPic()).into(hoder4.iv4_3);
                    Glide.with(context).load(list_data.get(position).getPic().get(3).getPic()).into(hoder4.iv4_4);
                }
            }

            return convertView;
        }

        class ViewHoder1 {
            TextView tv_day1, tvmonth1, tv_text1;
            ImageView iv1;
        }

        class ViewHoder2 {
            TextView tv_day2, tvmonth2, tv_text2;
            ImageView iv2_1, iv2_2;
        }

        class ViewHoder3 {
            TextView tv_day3, tvmonth3, tv_text3;
            ImageView iv3_1, iv3_2, iv3_3;
        }

        class ViewHoder4 {
            TextView tv_day4, tvmonth4, tv_text4;
            ImageView iv4_1, iv4_2, iv4_3, iv4_4;
        }

    }

}