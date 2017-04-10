package adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kxhl.R;
import com.kxhl.activity.HomeActivity.AnswersActivity;
import com.kxhl.activity.MainActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 */
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHoder hoder;
        if (convertView == null) {
            hoder = new ViewHoder();
            convertView = inflater.inflate(R.layout.item_answer, null);
            hoder.ll_layout = (LinearLayout) convertView.findViewById(R.id.ll_layout);
            hoder.iv_head = (ImageView) convertView.findViewById(R.id.item_ivAsk);

            hoder.tv_name = (TextView) convertView.findViewById(R.id.item_answerName);
            hoder.tv_time = (TextView) convertView.findViewById(R.id.item_answerTime);
            hoder.tv_text = (TextView) convertView.findViewById(R.id.item_answerContent);
            hoder.tv_ask=(TextView)convertView.findViewById(R.id.item_answerTv) ;
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
//                Dialog dialog=new Dialog(context,R.style.dialog);
//                LayoutInflater inflater=LayoutInflater.from(context);
//                View view=inflater.inflate(R.layout.view_input_comment,null);
//                Window window=dialog.getWindow();
//                WindowManager.LayoutParams params=new WindowManager.LayoutParams();
//                window.setGravity(Gravity.BOTTOM);
//                params.y = 800;//设置y坐标
//                window.setAttributes(params);
//                dialog.setContentView(view);
//                dialog.setTitle(null);
//                dialog.show();
                PopupWindow popupWindow=new PopupWindow(context);
                LayoutInflater inflater=LayoutInflater.from(context);
                View view=inflater.inflate(R.layout.view_input_comment,null);
                popupWindow.setContentView(view);
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(v,Gravity.BOTTOM,0,0);
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
