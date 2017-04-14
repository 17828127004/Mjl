package com.kxhl.activity.HomeActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.ImageConfig;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.foamtrace.photopicker.intent.PhotoPreviewIntent;
import com.kxhl.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import util.Config;
import util.ImageTools;
import util.KxhlRestClient;
import util.SaveData;
import util.TimeUtil;
import util.TitleUtil;
import util.UrlLIst;
import util.file.Bimp;

/**
 * Created by Administrator on 2017/2/8.
 */
public class UploadActivity extends Activity {
    private ImageCaptureManager captureManager;//相机拍照处理类
    private static final int REQUEST_CAMERA_CODE = 11;
    private static final int REQUEST_PREVIEW_CODE = 22;
    private ArrayList<String> imagePaths = null;
    private GridView gridView;
    private View parentView;
    private ImageView iv_addPhoto;
    private EditText et_uploadContent;
    private int columnWidth;
    private GridAdapter gridAdapter;
    private List<File> files;
    TitleUtil titleUtil;
    private int upload=0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 200:
                    //点击发送时，没有收到文件数据
                    titleUtil.setRightTvOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String data = TimeUtil.getTime();
                            if (TextUtils.isEmpty(et_uploadContent.getText())) {
                                Toast.makeText(UploadActivity.this, "文字或图片不能为空", Toast.LENGTH_SHORT).show();
                            }else if(imagePaths.size()==0){
                                Toast.makeText(UploadActivity.this, "图片不能为空", Toast.LENGTH_SHORT).show();
                            }else if(et_uploadContent.getText().length()>0&&imagePaths.size()>0){
                                try {
                                    for (int i = 0; i < imagePaths.size(); i++) {
                                        Bitmap bitmap = ImageTools.getLoacalBitmap(imagePaths.get(i));
//                                        files.add(ImageTools.savePhotoToSDCard(ImageTools.comp(bitmap),imagePaths.get(i)));
                                        files.add(ImageTools.savePhotoToSDCard(ImageTools.compressImageFromFile(imagePaths.get(i)),imagePaths.get(i)));
                                    }
                                    upFile(files, data);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                uploadContent((String) SaveData.get(UploadActivity.this, Config.USERID, ""), et_uploadContent.getText().toString()
                                        , data);
                            }
                        }
                    });
                break;
                case 300:
                    upload++;
                            if(upload==files.size()){
            startActivity(new Intent(UploadActivity.this,GrowUpPhotoActivity.class));
            Toast.makeText(UploadActivity.this,"上传成功！",Toast.LENGTH_SHORT).show();
        }
                    break;
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = getLayoutInflater().inflate(R.layout.activity_photoupload, null);
        setContentView(parentView);
        Config.setTranslucent(this);
        files = new ArrayList<>();
        getTitleUtil();
        gridView = (GridView) findViewById(R.id.noScrollgridview);
        et_uploadContent = (EditText) findViewById(R.id.et_uploadContent);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        Init();

        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 5 ? 5 : cols;
        gridView.setNumColumns(cols);

        // Item Width
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
        columnWidth = (screenWidth - columnSpace * (cols - 1)) / cols;
        // preview
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhotoPreviewIntent intent = new PhotoPreviewIntent(UploadActivity.this);
                intent.setCurrentItem(position);
                intent.setPhotoPaths(imagePaths);
                startActivityForResult(intent, REQUEST_PREVIEW_CODE);
            }
        });
    }

    public void upFile(List<File> fs, String time) throws FileNotFoundException {
        for (int i = 0; i < fs.size(); i++) {

            uploadImage((String) SaveData.get(UploadActivity.this, Config.USERID, ""), fs.get(i), time);
        }

    }

    public void Init() {
        iv_addPhoto = (ImageView) findViewById(R.id.iv_addPhoto);
        iv_addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                PhotoPickerIntent intent = new PhotoPickerIntent(UploadActivity.this);
                intent.setSelectModel(SelectModel.MULTI);
                intent.setShowCarema(true); // 是否显示拍照
                intent.setMaxTotal(9); // 最多选择照片数量，默认为9
                intent.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
                ImageConfig config = new ImageConfig();
                config.minHeight = 400;
                config.minWidth = 400;
                config.mimeType = new String[]{"image/jpeg", "image/png", "image/jpg"};
//                config.minSize = 1024; // 1Mb
                intent.setImageConfig(config);
                startActivityForResult(intent, REQUEST_CAMERA_CODE);
            }
        });

    }

    public void getTitleUtil() {
        titleUtil = new TitleUtil(this);
        titleUtil.setTitleName("成长相册");
        titleUtil.setLeftImage(R.drawable.happy_mine_back);
        titleUtil.setRightText(R.string.send);
        titleUtil.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case REQUEST_CAMERA_CODE:
                    loadAdpater(data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT));
                    break;
                // 预览
                case REQUEST_PREVIEW_CODE:
                    loadAdpater(data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT));
                    break;
                // 调用相机拍照
                case ImageCaptureManager.REQUEST_TAKE_PHOTO:
                    if (captureManager.getCurrentPhotoPath() != null) {
                        captureManager.galleryAddPic();

                        ArrayList<String> paths = new ArrayList<>();
                        paths.add(captureManager.getCurrentPhotoPath());
                        loadAdpater(paths);
                    }
                    break;

            }
        }
    }

    private void loadAdpater(ArrayList<String> paths) {
        if (imagePaths == null) {
            imagePaths = new ArrayList<>();
        }
        imagePaths.clear();
        imagePaths.addAll(paths);

        try {
            JSONArray obj = new JSONArray(imagePaths);
            Log.e("------***", obj.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }


        if (gridAdapter == null) {
            gridAdapter = new GridAdapter(imagePaths);
            gridView.setAdapter(gridAdapter);
        } else {
            gridAdapter.notifyDataSetChanged();

        }
        handler.sendEmptyMessage(200);
    }

    public void uploadContent(String userId, String content, String time) {
        RequestParams params = new RequestParams();
        params.put("id", userId);
        params.put("text_record", content);
        params.put("time_record", time);
        KxhlRestClient.post(UrlLIst.MINE_GROW_TEXT, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    Log.i("上传文字成功返回!", response.toString());
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(UploadActivity.this, responseString, Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void uploadImage(String userId, File f, String time) throws FileNotFoundException {
        RequestParams params = new RequestParams();
        params.put("id", userId);
        params.put("image", f);
        params.put("time_record", time);
        Log.i("成长相册上传数据", params.toString());
        KxhlRestClient.post(UrlLIst.MINE_GROW_UPLOAD, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    Log.i("成长相册上传成功返回信息", response.toString());
                    try {
                        if(response.getString("stat").equals("200")){

                            handler.sendEmptyMessage(300);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(UploadActivity.this, "网络错误!", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private class GridAdapter extends BaseAdapter {
        private ArrayList<String> listUrls;

        public GridAdapter(ArrayList<String> listUrls) {
            this.listUrls = listUrls;
        }

        @Override
        public int getCount() {
            return listUrls.size();
        }

        @Override
        public String getItem(int position) {
            return listUrls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_image, null);
                imageView = (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(imageView);
                // 重置ImageView宽高
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(columnWidth, columnWidth);
                imageView.setLayoutParams(params);
            } else {
                imageView = (ImageView) convertView.getTag();
            }
            Glide.with(UploadActivity.this)
                    .load(new File(getItem(position)))
                    .placeholder(R.mipmap.default_error)
                    .error(R.mipmap.default_error)
                    .centerCrop()
                    .crossFade()
                    .into(imageView);
            return convertView;
        }
    }
}
