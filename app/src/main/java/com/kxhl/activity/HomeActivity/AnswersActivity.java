package com.kxhl.activity.HomeActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Size;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kxhl.R;
import com.kxhl.activity.MainActivity;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.AnswersAdapter;
import util.Config;
import util.KxhlRestClient;
import util.SaveData;
import util.TitleUtil;
import util.UrlLIst;
import view.LoadingDialog;

/**
 * Created by Administrator on 2017/1/18.
 */
public class AnswersActivity extends Activity {
    private ListView lv_answers;
    private AnswersAdapter adapter;

    private List<String> mHeads;
    private List<String> mNames;
    private List<String> mTimes;
    private List<String> mContents;
    private List<String> mAskId;
    private PopupWindow mPopup;
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
        setContentView(R.layout.activity_answers);
        TitleUtil title = new TitleUtil(this);

        title.setTitleName("你问我答").setLeftImage(R.drawable.happy_mine_back).
                setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).setRightImage(R.drawable.ic_jia).setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });
        lv_answers = (ListView) findViewById(R.id.lv_answers);
    }

    @Override
    protected void onStart() {
        super.onStart();
        dialog=new LoadingDialog(this);
        getService((String) SaveData.get(this, Config.USERID, ""));
    }

    public void getService(String userId) {
        RequestParams params = new RequestParams();
        params.put("id", userId);
        KxhlRestClient.post(UrlLIst.INDEX_ASK, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("你问我答首页", response.toString());
                if (statusCode == 200) {
                    try {
                        getJson(response.getJSONArray("ask"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(AnswersActivity.this, "网络错误!", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void getJson(JSONArray array) throws JSONException {
        mHeads = new ArrayList<>();
        mAskId = new ArrayList<>();
        mNames = new ArrayList<>();
        mTimes = new ArrayList<>();
        mContents = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            String head = object.getString("path");
            String name = object.getString("name");
            String time = object.getString("time");
            String id = object.getString("id");
            String text = object.getString("text");
            mHeads.add(i, head);
            mNames.add(i, name);
            mTimes.add(i, time);
            mContents.add(i, text);
            mAskId.add(i, id);
        }
        adapter = new AnswersAdapter(this, mHeads, mNames, mTimes, mContents, mAskId);
        lv_answers.setAdapter(adapter);
        handler.sendEmptyMessage(00);
    }

    public void showPopup(View v) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.item_pop, null);
        LinearLayout ll_popup_answer = (LinearLayout) view.findViewById(R.id.ll_popup_answer);
        LinearLayout ll_popup_myAnswers = (LinearLayout) view.findViewById(R.id.ll_popup_myAnswer);
        LinearLayout ll_popup_myQuiz = (LinearLayout) view.findViewById(R.id.ll_popu_myQuiz);
        int w = getWindowManager().getDefaultDisplay().getWidth();
        int h = getWindowManager().getDefaultDisplay().getHeight();
        mPopup = new PopupWindow(view, w / 2, (int) (h / 5.2));
        mPopup.setFocusable(true);
//    mPopup.setOutsideTouchable(true);
        mPopup.showAsDropDown(v, 150, 40);
        ll_popup_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AnswersActivity.this, QuizActivity.class));
            }
        });
        ll_popup_myAnswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AnswersActivity.this, MyAnswersActivity.class));

            }
        });
        ll_popup_myQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AnswersActivity.this, MyQuizActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPopup != null) {
            mPopup.dismiss();
        }
    }

    public class AnswersAdapter extends BaseAdapter {
        List<String> head;
        List<String> name;
        List<String> time;
        List<String> content;
        List<String> askId;
        Context context;
        LayoutInflater inflater;

        public AnswersAdapter(Context context, List<String> head, List<String> name,
                              List<String> time, List<String> content, List<String> askId) {
            this.inflater = LayoutInflater.from(context);
            this.context = context;
            this.head = head;
            this.name = name;
            this.time = time;
            this.content = content;
            this.askId = askId;
        }

        @Override
        public int getCount() {
            return name.size();
        }

        @Override
        public Object getItem(int position) {
            return name.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHoder hoder;
            if (convertView == null) {
                hoder = new ViewHoder();
                convertView = inflater.inflate(R.layout.item_answer, null);
                hoder.ll_layout = (LinearLayout) convertView.findViewById(R.id.ll_layout);
                hoder.iv_head = (ImageView) convertView.findViewById(R.id.item_ivAsk);

                hoder.tv_name = (TextView) convertView.findViewById(R.id.item_answerName);
                hoder.tv_time = (TextView) convertView.findViewById(R.id.item_answerTime);
                hoder.tv_text = (TextView) convertView.findViewById(R.id.item_answerContent);
                hoder.tv_ask = (TextView) convertView.findViewById(R.id.item_answerTv);
                convertView.setTag(hoder);
            } else {
                hoder = (ViewHoder) convertView.getTag();
            }
            hoder.tv_name.setText(name.get(position));
            hoder.tv_text.setText(content.get(position));
            hoder.tv_time.setText(time.get(position));
            Glide.with(context).load(head.get(position)).into(hoder.iv_head);

            hoder.tv_ask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(AnswersActivity.this,EditActivity.class);
                    i.putExtra("pr_id",askId.get(position));
                    startActivity(i);
                }
            });
            return convertView;
        }

        private class ViewHoder {
            ImageView iv_head;
            TextView tv_name, tv_time, tv_text, tv_ask;
            LinearLayout ll_layout;
        }
    }

}
