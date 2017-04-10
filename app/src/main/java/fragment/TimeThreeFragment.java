package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kxhl.R;
import com.kxhl.activity.HomeActivity.StoreActivity;

import java.util.List;

import adapter.TimeTingAdapter;
import util.TimeUtil;

/**
 * Created by Administrator on 2017/1/22.
 */
public class TimeThreeFragment extends Fragment {
    private View layoutView;
    private ListView mListView;
    private String timeDay;
    private List<String> mNames;//店铺名字
    private List<String> mPaths;//店铺log
    private List<String> mTimes;//营业时间
    private List<String> mStoreId;//店铺id
    private List<String> mDistance;//距离
    private List<String> mStart;//星级
    private TimeTingAdapter adapter;

        public TimeThreeFragment(List<String> mNames,List<String> mPaths,List<String> mTimes,
                           List<String> mStoreId,List<String> mDistance,List<String> mStart){
        this.mNames=mNames;
        this.mPaths=mPaths;
        this.mTimes=mTimes;
        this.mStoreId=mStoreId;
        this.mDistance=mDistance;
        this.mStart=mStart;
    }
//    public static TimeThreeFragment newInstance(List<String> mNames, List<String> mPaths, List<String> mTimes,
//                                                List<String> mStoreId, List<String> mDistance, List<String> mStart) {
//        TimeThreeFragment timeThreeFragment = new TimeThreeFragment();
//        return timeThreeFragment;
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_time_page, null);
        mListView = (ListView) layoutView.findViewById(R.id.lv_page_timeting);
        timeDay = TimeUtil.nearDate(2);//one是当前的时间
        adapter = new TimeTingAdapter(getActivity(), mStoreId, mNames, mTimes, mStart, mDistance, mPaths);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                Intent i = new Intent();
                i.setClass(getActivity(), StoreActivity.class);
                bundle.putCharSequence("time", timeDay);
                bundle.putCharSequence("storeId", mStoreId.get(position));
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        return layoutView;

    }

}