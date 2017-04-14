package view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.kxhl.R;


/**
 * Created by Administrator on 2017/3/1.
 */
public class LoadingDialog extends Dialog {
    private ImageView iv;

    public LoadingDialog(Context context) {
        super(context);
        /**设置对话框背景透明*/
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.loadingxml);
        iv=(ImageView)findViewById(R.id.iv_loading);
        setCanceledOnTouchOutside(false);
        Animation animation=new ScaleAnimation(1.1f, 0.9f, 1.1f, 0.9f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(250);
        animation.setRepeatCount(-1);
        iv.setAnimation(animation);
        animation.setRepeatMode(Animation.REVERSE);
//        iv.clearAnimation();
        iv.startAnimation(animation);

    }
}
