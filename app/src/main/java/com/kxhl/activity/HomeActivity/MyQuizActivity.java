package com.kxhl.activity.HomeActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.kxhl.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.MyQuizAdapter;
import util.Config;
import util.KxhlRestClient;
import util.SaveData;
import util.TitleUtil;
import util.UrlLIst;
import util.entity.Child;
import util.entity.Group;
import view.LoadingDialog;

/**
 * Created by Administrator on 2017/1/22.
 */
public class MyQuizActivity extends Activity{
    private ExpandableListView mExpandableListView;
    private MyQuizAdapter adapter;
    private String mPaths;//我的头像
    // 准备数据
    private List<Group> list_data;
    private LoadingDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_quiz);
        new TitleUtil(this).setTitleName("我的提问").setLeftImage(R.drawable.happy_mine_back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MyQuizActivity.this,AnswersActivity.class));
                    }
                });
        Config.setTranslucent(this);
        mExpandableListView= (ExpandableListView) findViewById(R.id.elv_myQuiz);
//        adapter=new MyQuizAdapter(this);
//        mExpandableListView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        getService((String) SaveData.get(MyQuizActivity.this, Config.USERID,""));
    }
    public void getService(String userId){
        RequestParams params=new RequestParams();
        params.put("id",userId);
        KxhlRestClient.post(UrlLIst.INDEX_MYASK,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if(statusCode==200){
                    Log.i("我的提问",response.toString());
                    try {
                        getJson(response.getJSONArray("myask"),response.getString("path"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(MyQuizActivity.this,"网络错误！",Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
    public void getJson(JSONArray array,String pic) throws JSONException {
        list_data = new ArrayList<>();
        for(int i=0;i<array.length();i++){
            JSONObject object=array.getJSONObject(i);
            Group group=new Group();
            group.setGroupImage(pic);
            group.setGroupText(object.getString("text"));
            group.setGroupTime(object.getString("time"));
            group.setGroupCount(object.getString("count"));
            List<Child> childs=new ArrayList<Child>();
            String count=object.getString("count");
           if(Integer.parseInt(count)>0) {
               JSONArray array1 = object.getJSONArray("answer");
               for (int j = 0; j < array1.length(); j++) {
                   JSONObject object1 = array1.getJSONObject(j);
                   Child child=new Child();
                   child.setChildtime(object1.getString("ans_time"));
                   child.setChildText(object1.getString("ans_text"));
                   child.setChildId(object1.getString("id"));
                   child.setChildName(object1.getString("ans_name"));
                   child.setChildPath(object1.getString("ans_path"));
                   child.setChildStat(object1.getString("ans_stat"));
                   childs.add(child);
               }
               group.getList_child().addAll(childs);
           }
            list_data.add(group);
        }

//        adapter=new MyQuizAdapter(MyQuizActivity.this,mTimes,mTexts,mCounts,mAns_times,mAns_texts
//                ,mAns_ids,mAns_names,mAns_paths,mAns_stats,mPaths);
        adapter=new MyQuizAdapter(MyQuizActivity.this,list_data);
        mExpandableListView.setAdapter(adapter);
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                dialog=new LoadingDialog(MyQuizActivity.this);
                dialog.show();
                getServiceStat(list_data.get(groupPosition).getList_child().get(childPosition).getChildId());
                return true;
            }
        });
    }
    public void getServiceStat( String statId) {
        RequestParams params = new RequestParams();
        params.put("id", statId);
        KxhlRestClient.post(UrlLIst.INDEX_CHOOSE, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    Log.i("采纳成功否", response.toString());
                    try {
                        switch(response.getString("stat")){
                            case "200":
                                Toast.makeText(MyQuizActivity.this,"采纳成功！",Toast.LENGTH_SHORT).show();

                                dialog.dismiss();
                                break;
                            case "400":
                                dialog.dismiss();
                                Toast.makeText(MyQuizActivity.this,"已采纳！",Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(MyQuizActivity.this, "服务器请求失败!", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
