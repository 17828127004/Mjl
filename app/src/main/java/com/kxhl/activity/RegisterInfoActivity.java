package com.kxhl.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.kxhl.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import util.KxhlRestClient;
import util.TitleUtil;
import util.UrlLIst;

/**
 * Created by Administrator on 2017/2/14.
 */
public class RegisterInfoActivity extends Activity {
    private EditText et_name;//昵称
    private EditText et_passWord;//密码
    private EditText et_address;//地址
    private TextView tv_phone;//电话
    private EditText et_babyName;//宝宝姓名
    private TextView tv_babySex;//宝宝性别
    private TextView tv_babyBirthday;//宝宝生日
    private Button btn_login;//注册
    private String mchi_sex;
    private String mPhone;
    private View layoutView;
    private PopupWindow popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater=LayoutInflater.from(this);
        layoutView=inflater.inflate(R.layout.activity_registerinfo,null);
        setContentView(layoutView);
        new TitleUtil(this).setTitleName("注册").setLeftImage(R.drawable.happy_mine_back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        Intent i = getIntent();
        mPhone = i.getStringExtra("phone");
        initView();
        tv_phone.setText(mPhone);
tv_babySex.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        popupWindow=new PopupWindow(layoutView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,true);
        LayoutInflater inflater= LayoutInflater.from(RegisterInfoActivity.this);
        View view=inflater.inflate(R.layout.item_popus_sex,null);
        Button btn_man=(Button)view.findViewById(R.id.item_popus_man);
        Button btn_girl=(Button)view.findViewById(R.id.item_popus_girl);
        Button btn_cancel=(Button)view.findViewById(R.id.item_popus_cancel);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(layoutView, Gravity.BOTTOM,0,0);
        btn_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mchi_sex="1";
                tv_babySex.setText("男");
                popupWindow.dismiss();
            }

        });
        btn_girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mchi_sex="0";
                tv_babySex.setText("女");
                popupWindow.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }
});
        tv_babyBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.pickaddress.DatePickerDialog datePickerDialog=new view.pickaddress.DatePickerDialog(RegisterInfoActivity.this);
            datePickerDialog.setDialogMode(view.pickaddress.DatePickerDialog.DIALOG_MODE_BOTTOM);
                datePickerDialog.show();
                datePickerDialog.setDatePickListener(new view.pickaddress.DatePickerDialog.OnDatePickListener() {
                    @Override
                    public void onClick(String year, String month, String day) {
                        tv_babyBirthday.setText(year+"-"+month+"-"+day);
                    }
                });
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushData(mPhone,et_address.getText().toString(),et_babyName.getText().toString(),tv_babyBirthday.getText().toString()
                ,mchi_sex,et_passWord.getText().toString(),et_name.getText().toString());
//                pushData(mPhone,
//                        et_address.getText().toString(),
//                        et_babyName.getText().toString(), et_babyBirthday.getText().toString(),
//                        mchi_sex, mSex,
//                        et_passWord.getText().toString(),
//                        et_name.getText().toString());
            }
        });

    }

    public void initView() {
        et_name = (EditText) findViewById(R.id.et_resName);
        et_passWord = (EditText) findViewById(R.id.et_resPassWord);
        et_address = (EditText) findViewById(R.id.et_resAddress);
        tv_phone = (TextView) findViewById(R.id.tv_resPhone);
        et_babyName = (EditText) findViewById(R.id.et_resBabyName);
        tv_babySex = (TextView) findViewById(R.id.tv_resBabySex);
        tv_babyBirthday = (TextView) findViewById(R.id.tv_resBabyBirthday);
        btn_login = (Button) findViewById(R.id.btn_resLogin);
    }

    public void pushData(String phone, String address,
                         String chi_name, String chi_bir, String chi_sex
            , String password, String par_name) {
        RequestParams params = new RequestParams();
        params.put("phone", phone);
        params.put("address", address);
        params.put("chi_name", chi_name);
        params.put("chi_bir", chi_bir);
        params.put("chi_sex", chi_sex);
        params.put("password", password);
        params.put("name", par_name);
        Log.i("tag", params.toString());
        KxhlRestClient.post(UrlLIst.LOGIN_REGISTER, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    Log.i("tag", response.toString());
                    try {
                        if (response.getString("stat").equals("200")) {
                            Toast.makeText(RegisterInfoActivity.this, "注册成功!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterInfoActivity.this, LoginActivity.class));
                        } else {
                            Toast.makeText(RegisterInfoActivity.this, "注册失败!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(RegisterInfoActivity.this, "网络错误!", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
