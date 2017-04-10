package util;

import android.content.Context;
import android.graphics.*;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.lidroid.xutils.bitmap.core.AsyncDrawable;

/*
 * 重写CircleImage，继承ImageView
 */
public class CircleImage extends ImageView {
    public CircleImage(Context context) {
        super(context);
    }

    public CircleImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            Drawable drawable = getDrawable();
            if (drawable == null) {
                return;
            }

            if (getWidth() == 0 || getHeight() == 0) {
                return;
            }

            Bitmap b = null;
            if (drawable instanceof BitmapDrawable) {
                b = ((BitmapDrawable) drawable).getBitmap();
            } else if (drawable instanceof AsyncDrawable) {
                b = Bitmap
                        .createBitmap(
                                getWidth(),
                                getHeight(),
                                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                        : Bitmap.Config.RGB_565);
                Canvas canvas1 = new Canvas(b);
                // canvas.setBitmap(bitmap);
                drawable.setBounds(0, 0, getWidth(),
                        getHeight());
                drawable.draw(canvas1);
            }
            else
            {
                b = ((BitmapDrawable) drawable).getBitmap();
            }
            Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
            b = null;

            Bitmap roundBitmap = getCroppedBitmap(bitmap, getWidth());
            canvas.drawBitmap(roundBitmap, 0, 0, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /*
     * 对Bitmap裁剪，使其变成圆形，这步最关键
     */
    public Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;

        if (bmp.getWidth() != radius || bmp.getHeight() != radius)
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        else sbmp = bmp;

        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(), Bitmap.Config.ARGB_8888);
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xffffffff);

        Canvas c = new Canvas(output);
        c.drawARGB(0, 0, 0, 0);
        c.drawCircle(sbmp.getWidth() / 2, sbmp.getHeight() / 2, sbmp.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));

        // Rect dstrect = new Rect(0+getPaddingLeft(), 0+getPaddingTop(), sbmp.getWidth(), sbmp.getHeight());

        c.drawBitmap(sbmp, rect, rect, paint);

        return output;
    }
}