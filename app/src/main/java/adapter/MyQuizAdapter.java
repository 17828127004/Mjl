package adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kxhl.R;
import com.kxhl.activity.HomeActivity.MyQuizActivity;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

import util.Config;
import util.KxhlRestClient;
import util.ListViewForScrollView;
import util.SaveData;
import util.UrlLIst;
import util.entity.Group;

/**
 * Created by Administrator on 2017/1/22.
 */
public class MyQuizAdapter extends BaseExpandableListAdapter {
    LayoutInflater inflater;
    private List<String> times;//提问时间
    private List<String> texts;//提问内容
    private List<String> counts;//回答数
    private List<String> ans_times;//回答时间
    private List<String> ans_texts;//回答内容
    private List<String> ans_ids;//回答问题的id
    private List<String> ans_names;//回答问题的名字
    private List<String> ans_paths;//回答问题的头像
    private List<String> ans_stats;//状态 0是未采纳，1是采纳
    private String paths;
    private Context context;
    private List<Group> list_data;

    public MyQuizAdapter(Context context, List<String> times, List<String> texts, List<String> counts
            , List<String> ans_times, List<String> ans_texts, List<String> ans_ids, List<String> ans_names
            , List<String> ans_paths, List<String> ans_stats, String paths) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.times = times;
        this.texts = texts;
        this.counts = counts;
        this.ans_times = ans_times;
        this.ans_texts = ans_texts;
        this.ans_ids = ans_ids;
        this.ans_names = ans_names;
        this.ans_paths = ans_paths;
        this.ans_stats = ans_stats;
        this.paths = paths;
    }

    public MyQuizAdapter(Context context, List<Group> list_data) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.list_data = list_data;
    }

    @Override
    public int getGroupCount() {
        return list_data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list_data.get(groupPosition).getList_child().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list_data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list_data.get(groupPosition).getList_child().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHoderGroup group;
        if (convertView == null) {
            group = new ViewHoderGroup();
            convertView = inflater.inflate(R.layout.item_my_quiz, null);
            group.iv_pic = (ImageView) convertView.findViewById(R.id.iv_item_Group);
            group.tv_content = (TextView) convertView.findViewById(R.id.item_answerContent);
            group.tv_name = (TextView) convertView.findViewById(R.id.item_answerName);
            group.tv_time = (TextView) convertView.findViewById(R.id.item_answerTime);
            group.tv_count = (TextView) convertView.findViewById(R.id.item_groupCount);
            convertView.setTag(group);
        } else {
            group = (ViewHoderGroup) convertView.getTag();
        }
        Glide.with(context).load(list_data.get(groupPosition).getGroupImage()).into(group.iv_pic);
        group.tv_name.setText(SaveData.get(context, Config.USERNAME, "").toString());
        group.tv_time.setText(list_data.get(groupPosition).getGroupTime());
        group.tv_content.setText(list_data.get(groupPosition).getGroupText());
        group.tv_count.setText("回答" + list_data.get(groupPosition).getGroupCount());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHoderChild child;
        if (convertView == null) {
            child = new ViewHoderChild();
            convertView = inflater.inflate(R.layout.item_my_quiz_answers, null);
            child.tv_childName = (TextView) convertView.findViewById(R.id.item_answerName);
            child.iv_childStat = (ImageView) convertView.findViewById(R.id.iv_childStat);
            child.iv_childPic = (ImageView) convertView.findViewById(R.id.iv_child);
            child.tv_childText = (TextView) convertView.findViewById(R.id.item_answerContent);
            child.tv_childTime = (TextView) convertView.findViewById(R.id.item_answerTime);
            convertView.setTag(child);
        } else {
            child = (ViewHoderChild) convertView.getTag();
        }
        Glide.with(context).load(list_data.get(groupPosition).getList_child().get(childPosition).getChildPath()).into(child.iv_childPic);
        child.tv_childName.setText(list_data.get(groupPosition).getList_child().get(childPosition).getChildName());
        child.tv_childTime.setText(list_data.get(groupPosition).getList_child().get(childPosition).getChildtime());
        child.tv_childText.setText(list_data.get(groupPosition).getList_child().get(childPosition).getChildText());
        switch(list_data.get(groupPosition).getList_child().get(childPosition).getChildStat()){
            case "0":
                child.iv_childStat.setImageResource(R.drawable.accept);
                child.iv_childStat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                break;
            case "1":
                child.iv_childStat.setImageResource(R.drawable.accept_yes);
                break;
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }



    private class ViewHoderGroup {
        ImageView iv_pic;
        TextView tv_name, tv_time, tv_content, tv_count;

    }

    private class ViewHoderChild {
        ImageView iv_childPic, iv_childStat;
        TextView tv_childName, tv_childTime, tv_childText;
    }
}
