package util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.kxhl.R;

import java.util.List;

import util.entity.UPMarqueeViewData;

/**
 * 仿淘宝首页的 淘宝头条滚动的自定义View
 *
 * Created by mengwei on 2016/7/20.
 */
public class UPMarqueeView extends ViewFlipper {

    private Context mContext;
    private boolean isSetAnimDuration = false;
    private int interval = 3000;
    /**
     * 动画时间
     */
    private int animDuration = 500;

    public UPMarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        setFlipInterval(interval);
        Animation animIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_in);
        if (isSetAnimDuration) animIn.setDuration(animDuration);
        setInAnimation(animIn);
        Animation animOut = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_out);
        if (isSetAnimDuration) animOut.setDuration(animDuration);
        setOutAnimation(animOut);
    }


    /**
     * 设置循环滚动的View数组
     *
     * @param
     */
    public void setViews(final List<UPMarqueeViewData> datas) {
        if (datas == null || datas.size() == 0) return;
        removeAllViews();
        int size = datas.size();
        for (int i = 0; i < size; i += 2) {
            final int position = i;
            //根布局
            LinearLayout item = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_view, null);
            //设置监听
            item.findViewById(R.id.rl).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position);
                    }
                }
            });

            //控件赋值
            ((TextView) item.findViewById(R.id.tv1)).setText(datas.get(position).getValue());
            ((TextView) item.findViewById(R.id.title_tv1)).setText(datas.get(position).getTitle());
            //当数据是奇数时，最后那个item仅有一项
            if (position + 1 < size) {
                item.findViewById(R.id.rl2).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(position+1);
                        }
                    }
                });
                ((TextView) item.findViewById(R.id.tv2)).setText(datas.get(position + 1).getValue());
                ((TextView) item.findViewById(R.id.title_tv2)).setText(datas.get(position + 1).getTitle());
            } else item.findViewById(R.id.rl2).setVisibility(View.GONE);
            addView(item);
        }
        startFlipping();
    }

    /**
     * 点击
     */
    private OnItemClickListener onItemClickListener;

    /**
     * 设置监听接口
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * item_view的接口
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}