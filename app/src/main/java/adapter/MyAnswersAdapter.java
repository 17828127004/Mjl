package adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kxhl.R;
import com.kxhl.activity.HomeActivity.MyAnswersActivity;

import java.util.List;

import util.Config;
import util.SaveData;

/**
 * Created by Administrator on 2017/1/22.
 */
public class MyAnswersAdapter extends BaseAdapter {
    LayoutInflater inflater;
    private List<String> texts;//回答的内容
    private List<String> stats;//问题状态，1 采纳 0未采纳
    private List<String> pr_ids;//问题id
    private List<String> times;//回答时间
    private List<String> pr_times;//提问时间
    private List<String> pr_texts;//问题内容
    private List<String> pr_names;//提问用户名
    private List<String> pr_paths;//提问头像
//    private List<String> names;//用户名字
    private String paths;//用户头像
    private List<String> counts;//回答数
    Context context;

    public MyAnswersAdapter(Context context, List<String> texts, List<String> stats, List<String> pr_ids, List<String> times,
                            List<String> pr_times, List<String> pr_texts, List<String> pr_names, List<String> pr_paths,
                            String paths,List<String> counts) {
        inflater = LayoutInflater.from(context);
        this.context=context;
        this.texts = texts;
        this.stats = stats;
        this.pr_ids = pr_ids;
        this.times = times;
        this.pr_times = pr_times;
        this.pr_texts = pr_texts;
        this.pr_names = pr_names;
        this.pr_paths = pr_paths;
        this.paths=paths;
        this.counts = counts;
    }



    @Override
    public int getCount() {
        return pr_names.size();
    }

    @Override
    public Object getItem(int position) {
        return pr_names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoder hoder;
        if (convertView == null) {
            hoder=new ViewHoder();
            convertView = inflater.inflate(R.layout.item_my_answers, null);
            hoder.iv_name=(ImageView)convertView.findViewById(R.id.item_answer_pic);
            hoder.iv_useName=(ImageView)convertView.findViewById(R.id.item_answer_useName);
            hoder.tv_name=(TextView) convertView.findViewById(R.id.item_answer_Name);
            hoder.tv_time=(TextView)convertView.findViewById(R.id.item_answer_Time);
            hoder.tv_text=(TextView)convertView.findViewById(R.id.item_answer_Content);
            hoder.tv_count=(TextView)convertView.findViewById(R.id.item_answer_count);
            hoder.tv_stat=(TextView)convertView.findViewById(R.id.item_answer_stat);
            hoder.tv_useName=(TextView)convertView.findViewById(R.id.item_answerName);
            hoder.tv_useTime=(TextView)convertView.findViewById(R.id.item_answerTime);
            hoder.tv_useText=(TextView)convertView.findViewById(R.id.item_answerContent);
            convertView.setTag(hoder);
        }else{
            hoder=(ViewHoder)convertView.getTag();
        }
        Glide.with(context).load(pr_paths.get(position)).into(hoder.iv_name);
        Glide.with(context).load(paths).into(hoder.iv_useName);
        hoder.tv_name.setText(pr_names.get(position));
        hoder.tv_time.setText(pr_times.get(position));
        hoder.tv_text.setText(pr_texts.get(position));
        hoder.tv_count.setText("回答 "+counts.get(position));
       switch(stats.get(position)){
           case "1":
               hoder.tv_stat.setText("已采纳");
               hoder.tv_stat.setTextColor(Color.rgb(237,107,111));
               break;
           case "0":
               hoder.tv_stat.setText("未采纳");
               hoder.tv_stat.setTextColor(Color.rgb(189,189,189));
               break;
       }
        hoder.tv_useName.setText(SaveData.get(context,Config.USERNAME,"").toString());
        hoder.tv_useTime.setText(times.get(position));
        hoder.tv_useText.setText(texts.get(position));
        return convertView;
    }
    private class ViewHoder{
        ImageView iv_name,iv_useName;
        TextView tv_name,tv_time,tv_text,tv_count,tv_stat,tv_useName,tv_useTime,tv_useText;
    }
}
