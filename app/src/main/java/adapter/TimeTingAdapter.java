package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kxhl.R;

import java.math.BigDecimal;
import java.util.List;

import util.RoundedImageView;

/**
 * Created by Administrator on 2017/2/17.
 */
public class TimeTingAdapter extends BaseAdapter{
    List<String> paths;//店铺地址
    List<String> names;//店铺名字
    List<String> storeId;//店铺id
    List<String> times;//营业时间
    List<String> stars;//星级
    List<String> distances;//距离
    LayoutInflater inflater;
    Context context;
    public TimeTingAdapter(Context context, List<String> storeId, List<String> names, List<String> times,
                            List<String> stars, List<String> distances, List<String> paths){
        inflater = LayoutInflater.from(context);
        this.names = names;
        this.storeId = storeId;
        this.times = times;
        this.stars = stars;
        this.distances = distances;
        this.paths = paths;
        this.context=context;
    }

    @Override
    public int getCount() {
        return storeId.size();
    }

    @Override
    public Object getItem(int position) {
        return storeId.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public android.view.View getView(int position, android.view.View convertView, ViewGroup parent) {
        ViewHoder hoder;
        if (convertView == null) {
            hoder = new ViewHoder();
            convertView = inflater.inflate(R.layout.item_timelv, null);
            hoder.image = (ImageView) convertView.findViewById(R.id.item_riv_log);
            hoder.tv_name = (TextView) convertView.findViewById(R.id.item_timeName);
            hoder.tv_time = (TextView) convertView.findViewById(R.id.tv_item_timeTime);
            hoder.tv_distance = (TextView) convertView.findViewById(R.id.tv_item_timeDistance);
            hoder.iv1 = (ImageView) convertView.findViewById(R.id.item_iv1);
            hoder.iv2 = (ImageView) convertView.findViewById(R.id.item_iv2);
            hoder.iv3 = (ImageView) convertView.findViewById(R.id.item_iv3);
            hoder.iv4 = (ImageView) convertView.findViewById(R.id.item_iv4);
            hoder.iv5 = (ImageView) convertView.findViewById(R.id.item_iv5);
            convertView.setTag(hoder);
        } else {
            hoder = (ViewHoder) convertView.getTag();
        }
        hoder.tv_name.setText(names.get(position));
        hoder.tv_time.setText(times.get(position));
        BigDecimal b  =   new  BigDecimal(Float.parseFloat(distances.get(position))/1000);
        hoder.tv_distance.setText(b.setScale(1,  BigDecimal.ROUND_HALF_UP).floatValue()+"/km");
        switch(stars.get(position)){
            case "1":
                hoder.iv1.setImageResource(R.drawable.start);
                hoder.iv2.setImageResource(R.drawable.start_no);
                hoder.iv3.setImageResource(R.drawable.start_no);
                hoder.iv4.setImageResource(R.drawable.start_no);
                hoder.iv5.setImageResource(R.drawable.start_no);
                break;
            case "2":
                hoder.iv1.setImageResource(R.drawable.start);
                hoder.iv2.setImageResource(R.drawable.start);
                hoder.iv3.setImageResource(R.drawable.start_no);
                hoder.iv4.setImageResource(R.drawable.start_no);
                hoder.iv5.setImageResource(R.drawable.start_no);
                break;
            case "3":
                hoder.iv1.setImageResource(R.drawable.start);
                hoder.iv2.setImageResource(R.drawable.start);
                hoder.iv3.setImageResource(R.drawable.start);
                hoder.iv4.setImageResource(R.drawable.start_no);
                hoder.iv5.setImageResource(R.drawable.start_no);
                break;
            case "4":
                hoder.iv1.setImageResource(R.drawable.start);
                hoder.iv2.setImageResource(R.drawable.start);
                hoder.iv3.setImageResource(R.drawable.start);
                hoder.iv4.setImageResource(R.drawable.start);
                hoder.iv5.setImageResource(R.drawable.start_no);
                break;
            case "5":
                hoder.iv1.setImageResource(R.drawable.start);
                hoder.iv2.setImageResource(R.drawable.start);
                hoder.iv3.setImageResource(R.drawable.start);
                hoder.iv4.setImageResource(R.drawable.start);
                hoder.iv5.setImageResource(R.drawable.start);
                break;
        }
        Glide.with(context).load(paths.get(position)).error(R.drawable.talk).into(hoder.image);
        return convertView;
    }
    private class ViewHoder {
        ImageView image;
        TextView tv_name;
        TextView tv_time;
        TextView tv_distance;
        ImageView iv1, iv2, iv3, iv4, iv5;
    }

}
