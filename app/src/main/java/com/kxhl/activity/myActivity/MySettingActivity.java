package com.kxhl.activity.myActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.kxhl.R;
import com.kxhl.activity.LoginActivity;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import util.CircleImage;
import util.Config;
import util.GlideCacheUtil;
import util.ImageLoader;
import util.ImageTools;
import util.KxhlRestClient;
import util.SaveData;
import util.TitleUtil;
import util.UrlLIst;
import util.file.FileUtil;
import util.file.FileUtils;
import view.CircleImageView;

/**
 * Created by Administrator on 2017/1/10.
 */
public class MySettingActivity extends Activity implements View.OnClickListener {
    private CircleImageView cImage_mystting;
    private Button btn_exit;//退出至登录账号
    private LinearLayout ll_mySetting_setHeart;
    private RelativeLayout rl_mySetting_name;
    private RelativeLayout rl_mySetting_setPassword;
    private static final int REQUEST_CAMERA_CODE = 11;
    private static final int REQUEST_PREVIEW_CODE = 22;
    private ImageCaptureManager captureManager;//相机拍照处理类
    private ArrayList<String> imagePaths = null;
    private String headPath;//图片路径
    private TextView tv_name, tv_phone, tv_babyName, tv_babyOld, tv_headName,tv_vip;
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private File tempFile;
    private String tp = null;
    private String filePath;
    private File photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysetting);
        new TitleUtil(this).setTitleName("账户设置").setLeftImage(R.drawable.happy_mine_back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        Config.setTranslucent(this);
        initView();
        initEvent();
        Intent i = getIntent();
        Glide.with(this).load(i.getStringExtra("head")).asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        cImage_mystting.setImageBitmap(resource);
                    }
                });
    }


    public void initView() {
        cImage_mystting = (CircleImageView) findViewById(R.id.cImage_mystting);
        btn_exit = (Button) findViewById(R.id.btn_mySetting_exit);
        tv_headName = (TextView) findViewById(R.id.tv_mySetting_headName);
        tv_name = (TextView) findViewById(R.id.tv_mysettingName);
        tv_phone = (TextView) findViewById(R.id.tv_mySettingPhone);
        tv_babyName = (TextView) findViewById(R.id.tv_mySettingBabyName);
        tv_babyOld = (TextView) findViewById(R.id.tv_mySettingBabyOld);
        tv_vip=(TextView)findViewById(R.id.tv_mySetting_vip);
        ll_mySetting_setHeart = (LinearLayout) findViewById(R.id.ll_mySetting_setHeart);
        rl_mySetting_name = (RelativeLayout) findViewById(R.id.rl_mySetting_name);
        rl_mySetting_setPassword = (RelativeLayout) findViewById(R.id.rl_mySetting_setPassword);
        Glide.with(this).load((String) SaveData.get(this, Config.HEADPHOTO, "")).
                asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                cImage_mystting.setImageBitmap(resource);
            }
        });
    }

    public void initEvent() {
        ll_mySetting_setHeart.setOnClickListener(this);
        rl_mySetting_name.setOnClickListener(this);
        rl_mySetting_setPassword.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        getInfoService((String) SaveData.get(this, Config.USERID, ""));
        super.onResume();
    }

    public void getInfoService(String userId) {
        final RequestParams params = new RequestParams();
        params.put("id", userId);
        KxhlRestClient.post(UrlLIst.MINE_EDIT_INDEX, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    try {
                        Log.i("账户设置",response.toString());
                        String name = response.getString("name");
                        SaveData.put(getApplicationContext(), Config.USERNAME, name);
                        tv_headName.setText(name);
                        tv_name.setText(name);
                        String phone = response.getString("phone");
                        tv_phone.setText(phone);
                        SaveData.put(getApplicationContext(),Config.USER_PHONE,phone);
                       String path=response.getString("path");
                        SaveData.put(getApplicationContext(),Config.HEADPHOTO,path);
                        tv_babyName.setText(response.getString("chi_name"));
                        String old=response.getString("chi_bir");
                        tv_babyOld.setText(old);
                        String vipCard=response.getString("vipcard");
                        if(TextUtils.isEmpty(vipCard)){
                            tv_vip.setText("非会员");
                        }else{
                            tv_vip.setText(vipCard);
                        }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_mySetting_setHeart://更换头像
//                setHeardPhoto();
//                 激活系统图库，选择一张图片
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);

                break;
            case R.id.rl_mySetting_name://修改昵称
                startActivity(new Intent(MySettingActivity.this, SetNameActivity.class));
                break;
            case R.id.rl_mySetting_setPassword://修改密码
                startActivity(new Intent(MySettingActivity.this, SetPasswordActivity.class));
                break;
            case R.id.btn_mySetting_exit:
                startActivity(new Intent(MySettingActivity.this, LoginActivity.class));
                SaveData.remove(getApplicationContext(), Config.USERID);
                SaveData.remove(getApplicationContext(), Config.USERNAME);
                SaveData.remove(getApplicationContext(), Config.HEADPHOTO);
                SaveData.remove(getApplicationContext(), Config.USER_PHONE);
                SaveData.remove(getApplicationContext(), Config.VIP_CHECK);
                GlideCacheUtil.getInstance().clearImageAllCache(this);//清除Glide图片缓存
                finish();//按手机返回键不会返回到此页面

                break;
        }
    }


//    public void setHeardPhoto() {
//        PhotoPickerIntent intent = new PhotoPickerIntent(MySettingActivity.this);
//        intent.setSelectModel(SelectModel.SINGLE);
////        intent.setShowCarema(true);
////                intent.setImageConfig(null);
//        startActivityForResult(intent, REQUEST_CAMERA_CODE);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                // 选择照片
//                case REQUEST_CAMERA_CODE:
//                    loadAdpater(data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT));
//
//                    break;
//                // 预览
//                case REQUEST_PREVIEW_CODE:
//                    loadAdpater(data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT));
//                    break;
//                // 调用相机拍照
//                case ImageCaptureManager.REQUEST_TAKE_PHOTO:
//                    if (captureManager.getCurrentPhotoPath() != null) {
//                        captureManager.galleryAddPic();
//
//                        ArrayList<String> paths = new ArrayList<>();
//                        paths.add(captureManager.getCurrentPhotoPath());
//                        loadAdpater(paths);
//                    }
//                    break;
//
//            }
//        }
//    }
  /**
   * 剪切图片
   */
private void crop(Uri uri) {
    // 裁剪图片意图
    Intent intent = new Intent("com.android.camera.action.CROP");
    intent.setDataAndType(uri, "image/*");
    intent.putExtra("crop", "true");
    // 裁剪框的比例，1：1
    intent.putExtra("aspectX", 1);
    intent.putExtra("aspectY", 1);
    // 裁剪后输出图片的尺寸大小
    intent.putExtra("outputX", 250);
    intent.putExtra("outputY", 250);

    intent.putExtra("outputFormat", "JPEG");// 图片格式
    intent.putExtra("noFaceDetection", true);// 取消人脸识别
    intent.putExtra("return-data", true);
    // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
    startActivityForResult(intent, PHOTO_REQUEST_CUT);
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
            }
        } else if (requestCode == PHOTO_REQUEST_CUT) {
            // 从剪切图片返回的数据
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                cImage_mystting.setImageBitmap(bitmap);
                UUID uuid = UUID.randomUUID();
                String fileName = uuid + ".png";
                filePath = FileUtil.chatImagePath + "/" + fileName;
                photo = ImageTools.savePhotoToSDCard(bitmap, filePath);
//                String s=tempFile.getAbsolutePath();
//                Log.i("lujing",s);
//                File file=new File(s);
                try {
                    setHead((String) SaveData.get(MySettingActivity.this, Config.USERID, ""), photo);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            try {
                // 将临时文件删除
                tempFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);

    }

//    private void loadAdpater(ArrayList<String> paths) {
//        if (imagePaths == null) {
//            imagePaths = new ArrayList<>();
//        }
//        imagePaths.clear();
//        imagePaths.addAll(paths);
//
//        try {
//            JSONArray obj = new JSONArray(imagePaths);
//            Log.e("--", obj.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Bitmap bitmap = ImageTools.getLoacalBitmap(imagePaths.get(0));
//        cImage_mystting.setImageBitmap(ImageTools.comp(bitmap));
//
////        File f=new File(imagePaths.get(0));
//
//        try {
//            setHead((String) SaveData.get(MySettingActivity.this, Config.USERID, ""), ImageTools.savePhotoToSDCard(bitmap, imagePaths.get(0)));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//    }

    public void setHead(String userId, File f) throws FileNotFoundException {
        RequestParams params = new RequestParams();
        params.put("id", userId);
        params.put("image", f);
        Log.i("头像图片上传", params.toString());
        KxhlRestClient.post(UrlLIst.MINE_EDIT, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    Log.i("上传头像",response.toString());
                    try {
                        if (response.getString("stat").equals("200")) {
                            Toast.makeText(MySettingActivity.this, "上传成功!", Toast.LENGTH_SHORT).show();
                            SaveData.put(getApplication(), Config.HEADPHOTO, photo);
                        } else {
                            Toast.makeText(MySettingActivity.this, "上传失败!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(MySettingActivity.this, "网络错误!", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

}
