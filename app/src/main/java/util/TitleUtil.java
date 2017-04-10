package util;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kxhl.R;

/**
 * Created by hyc on 2017/1/6.
 */
public class TitleUtil {
    private ImageView leftIv;
    private ImageView rightIv;
    private TextView betweenTv;
    private TextView rightTv;
    private TextView leftTv;
    private View mView;

    /**
     * 构造方法1：针对Activity 2:针对Fragment，3.针对FragmentActivity
     * @param
     * */

    public  TitleUtil(Activity ct){
        mView = ct.findViewById(R.id.titlepg);
        leftIv = (ImageView) mView.findViewById(R.id.titlepg_left_iv);
        leftTv = (TextView) mView.findViewById(R.id.titlepg_left_tv);
        rightIv = (ImageView) mView.findViewById(R.id.titlepg_right_iv);
        rightTv = (TextView) mView.findViewById(R.id.titlepg_right_tv);
        betweenTv = (TextView) mView.findViewById(R.id.titlepg_between_tv);
    }
    public  TitleUtil(FragmentActivity ct){
        mView = ct.findViewById(R.id.titlepg);
        leftIv = (ImageView) mView.findViewById(R.id.titlepg_left_iv);
        leftTv = (TextView) mView.findViewById(R.id.titlepg_left_tv);
        rightIv = (ImageView) mView.findViewById(R.id.titlepg_right_iv);
        rightTv = (TextView) mView.findViewById(R.id.titlepg_right_tv);
        betweenTv = (TextView) mView.findViewById(R.id.titlepg_between_tv);
    }

    public  TitleUtil(View ct){
        mView = ct.findViewById(R.id.titlepg);
        leftIv = (ImageView) mView.findViewById(R.id.titlepg_left_iv);
        leftTv = (TextView) mView.findViewById(R.id.titlepg_left_tv);
        rightIv = (ImageView) mView.findViewById(R.id.titlepg_right_iv);
        rightTv = (TextView) mView.findViewById(R.id.titlepg_right_tv);
        betweenTv = (TextView) mView.findViewById(R.id.titlepg_between_tv);
    }

    /**
     * 设置标题名
     * @param标题内容
     * */
    public TitleUtil setTitleName(String str){
        betweenTv.setVisibility(TextUtils.isEmpty(str) ? View.GONE : View.VISIBLE);
        betweenTv.setText(str);
        return this;
    }
    /**
     * 设置左侧图片(隐藏文字)
     * @param资源图片
     * */

    public TitleUtil setLeftImage(int imgId){
        leftTv.setVisibility(View.GONE);
        leftIv.setVisibility(imgId > 0 ? View.VISIBLE : View.GONE);
        leftIv.setImageResource(imgId);
        return this;
    }

    /**
     * 设置左侧文字(隐藏图片)
     * */
    public TitleUtil setLeftText(int textId){
        leftIv.setVisibility(View.GONE);
        leftTv.setVisibility(textId > 0 ? View.VISIBLE : View.GONE);
        leftTv.setText(textId);
        return this;
    }

    /**
     * 设置右侧图片（隐藏文字）
     * */
    public TitleUtil setRightImage(int imgId){
        rightTv.setVisibility(View.GONE);
        rightIv.setVisibility(imgId > 0 ? View.VISIBLE : View.GONE);
        rightIv.setImageResource(imgId);
        return this;
    }
    /**
     * 设置右侧文字(隐藏图片)
     * */
    public TitleUtil setRightText(int textId){
        rightIv.setVisibility(View.GONE);
        rightTv.setVisibility(textId > 0 ? View.VISIBLE : View.GONE);
        rightTv.setText(textId);
        return this;
    }

    /**
     * 设置左侧图片监听器
     * @param监听器对象
     * */
    public TitleUtil setLeftOnClickListener(View.OnClickListener listener){
        if(leftIv.getVisibility() == View.VISIBLE){
            leftIv.setOnClickListener(listener);
        }else if(leftIv.getVisibility() == View.VISIBLE){
            leftIv.setOnClickListener(listener);
        }
        return this;
    }


    /**
     * 设置左侧文字监听器
     * */
    public TitleUtil setLeftTvOnClickListener(View.OnClickListener listener){
        if(leftTv.getVisibility() == View.VISIBLE){
            leftTv.setOnClickListener(listener);
        }else if(leftTv.getVisibility() == View.VISIBLE){
            leftTv.setOnClickListener(listener);
        }
        return this;
    }

    /**
     * 设置右侧图片监听器
     * */
    public TitleUtil setRightOnClickListener(View.OnClickListener listener){
        if(rightIv.getVisibility() == View.VISIBLE){
            rightIv.setOnClickListener(listener);
        }else if(rightIv.getVisibility() == View.VISIBLE){
            rightIv.setOnClickListener(listener);
        }
        return this;

    }

    /**
     * 设置右侧文字监听器
     * */
    public TitleUtil setRightTvOnClickListener(View.OnClickListener listener){
        if(rightTv.getVisibility() == View.VISIBLE){
            rightTv.setOnClickListener(listener);
        }else if(rightTv.getVisibility() == View.VISIBLE){
            rightTv.setOnClickListener(listener);
        }
        return this;
    }
    /**
     * 设置其他颜色的标题背景色
     *
     */
    public TitleUtil setOtherBg(int bg){
        mView.setBackgroundResource(bg);
        return this;
    }
}
