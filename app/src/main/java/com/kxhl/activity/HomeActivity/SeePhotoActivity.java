package com.kxhl.activity.HomeActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.kxhl.R;

import java.util.ArrayList;
import java.util.List;

import util.entity.Pic;

/**
 * Created by Administrator on 2017/2/28.
 */
public class SeePhotoActivity extends Activity implements ViewPager.OnPageChangeListener{
    private List<Pic> pics;
    private List<String> urls;
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    /**
     * ViewPager
     */
    private ViewPager viewPager;

    /**
     * 装点点的ImageView数组
     */
    private ImageView[] tips;

    /**
     * 装ImageView数组
     */
    private ImageView[] mImageViews;

    /**
     * 图片资源id
     */
    private int[] imgIdArray ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seephoto);
        Intent i = getIntent();
        pics = new ArrayList<>();
        urls = new ArrayList<String>();
        pics = (List<Pic>) i.getSerializableExtra(EXTRA_IMAGE_URLS);
        for (int j = 0; j < pics.size(); j++) {
            urls.add(j, pics.get(j).getPic());
        }
        ViewGroup group = (ViewGroup)findViewById(R.id.viewGroup);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        //载入图片资源ID
//        imgIdArray = new int[]{R.drawable.item01, R.drawable.item02, R.drawable.item03, R.drawable.item04,
//                R.drawable.item05,R.drawable.item06, R.drawable.item07, R.drawable.item08};


        //将点点加入到ViewGroup中
        tips = new ImageView[urls.size()];
        for(int j=0; j<tips.length; j++){
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(20,20));
            tips[j] = imageView;
            if(j == 0){
                tips[j].setBackgroundResource(R.drawable.dot_focused);
            }else{
                tips[j].setBackgroundResource(R.drawable.dot_normal);
            }

            group.addView(imageView);
        }


        //将图片装载到数组中
        mImageViews = new ImageView[urls.size()];
        for(int j=0; j<mImageViews.length; j++){
            ImageView imageView = new ImageView(this);
            mImageViews[j] = imageView;
//            imageView.setBackgroundResource(imgIdArray[j]);
            Glide.with(this).load(urls.get(j)).thumbnail(0.1f).into(imageView);
        }
if(mImageViews.length==1){
    //设置Adapter
    viewPager.setAdapter(new MyAdapter());
    //设置监听，主要是设置点点的背景
    viewPager.setOnPageChangeListener(this);
    //设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
//    viewPager.setCurrentItem((mImageViews.length) * 100);
}else {
    //设置Adapter
    viewPager.setAdapter(new MyAdapter());
    //设置监听，主要是设置点点的背景
    viewPager.setOnPageChangeListener(this);
    //设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
    viewPager.setCurrentItem((mImageViews.length) * 100);
}
    }

    /**
     *
     * @author xiaanming
     *
     */
    public class MyAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
//            ((ViewPager)container).removeView(mImageViews[position % mImageViews.length]);

        }

        /**
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
         */
        @Override
        public Object instantiateItem(View container, int position) {
            try {
                ((ViewPager)container).addView(mImageViews[position % mImageViews.length], 0);
            }catch(Exception e){
                //handler something
            }
            return mImageViews[position % mImageViews.length];
        }



    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        setImageBackground(arg0 % mImageViews.length);
    }

    /**
     * 设置选中的tip的背景
     * @param selectItems
     */
    private void setImageBackground(int selectItems){
        for(int i=0; i<tips.length; i++){
            if(i == selectItems){
                tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            }else{
                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
        }
    }

}
