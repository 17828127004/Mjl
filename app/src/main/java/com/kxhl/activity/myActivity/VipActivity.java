package com.kxhl.activity.myActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kxhl.R;
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
import view.LoadingDialog;

/**
 * Created by Administrator on 2017/2/8.
 */
public class VipActivity extends Activity {
    private ImageView iv_vipCard;
    private TextView tv_vipNumm;
    private TextView tv_vipDW;
    private Button btn_vip_card;
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
        setContentView(R.layout.activity_vip);
        new TitleUtil(this).setTitleName("会员卡").setLeftImage(R.drawable.happy_mine_back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                   finish();
                    }
                });
        Config.setTranslucent(this);
        iv_vipCard=(ImageView)findViewById(R.id.iv_vipCard);
        tv_vipNumm=(TextView)findViewById(R.id.tv_vipNumm);
        tv_vipDW=(TextView)findViewById(R.id.tv_vipDW);
        btn_vip_card=(Button)findViewById(R.id.btn_vip_card);
    }

    @Override
    protected void onResume() {
        dialog=new LoadingDialog(this);
        dialog.show();
        getService((String) SaveData.get(this, Config.USERID,""));
        super.onResume();
    }

    public void getService(String userId){
        RequestParams params=new RequestParams();
        params.put("id",userId);
        KxhlRestClient.post(UrlLIst.MINE_VIP_SHOW,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(statusCode==200){
                    Log.i("vip",response.toString());
                    try {
                        switch(response.getString("type")){
                            case "月卡":
                                iv_vipCard.setImageResource(R.drawable.vip_month_card);
                                tv_vipDW.setText("天");
                                break;
                            case "次卡":
                                iv_vipCard.setImageResource(R.drawable.vip_num_card);
                                tv_vipDW.setText("次");
                                break;
                            case "年卡":
                                iv_vipCard.setImageResource(R.drawable.vip_year_card);
                                tv_vipDW.setText("天");
                                break;
                            case "季卡":
                                iv_vipCard.setImageResource(R.drawable.vip_ji_card);
                                tv_vipDW.setText("天");
                                break;
                        }
                        btn_vip_card.setText(response.getString("number"));
                        tv_vipNumm.setText(response.getString("data"));

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
        handler.sendEmptyMessage(00);
    }
}
