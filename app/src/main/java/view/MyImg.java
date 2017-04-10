package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/3/15.
 * 自定义ImageView加蒙层效果
 */
public class MyImg extends ImageView{
    private int color;
    public void setColor(int color){
this.color=color;
        invalidate();
    }
    public MyImg(Context context) {
        super(context);
    }
public MyImg(Context context, AttributeSet attrs){
super(context,attrs);
}
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
            canvas.drawColor(color, PorterDuff.Mode.DARKEN);
    }
}
