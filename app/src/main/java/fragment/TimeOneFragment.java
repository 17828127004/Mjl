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
public class TimeOneFragment extends Fragment {
    private View layoutView;
    private ListView mListView;

    public List<String> mNames;//店铺名字
    public List<String> mPaths;//店铺log
    public List<String> mTimes;//营业时间
    public List<String> mStoreId;//店铺id
    public List<String> mDistance;//距离
    public List<String> mStart;//星级
    private TimeTingAdapter adapter;
    private String timeDay;

        public TimeOneFragment(List<String> mNames, List<String> mPaths, List<String> mTimes,
                           List<String> mStoreId, List<String> mDistance, List<String> mStart) {
        this.mNames = mNames;
        this.mPaths = mPaths;
        this.mTimes = mTimes;
        this.mStoreId = mStoreId;
        this.mDistance = mDistance;
        this.mStart = mStart;
    }
//    public static TimeOneFragment newInstance(List<String> mNames, List<String> mPaths, List<String> mTimes,
//                                              List<String> mStoreId, List<String> mDistance, List<String> mStart) {
//        TimeOneFragment timeOneFragment = new TimeOneFragment();
//        Bundle bundle = new Bundle();
//        bundle.putStringArrayList("mNames", (ArrayList<String>) mNames);
//        bundle.putStringArrayList("mPaths", (ArrayList<String>) mPaths);
//        bundle.putStringArrayList("mTimes", (ArrayList<String>) mTimes);
//        bundle.putStringArrayList("mStoreId", (ArrayList<String>) mStoreId);
//        bundle.putStringArrayList("mDistance", (ArrayList<String>) mDistance);
//        bundle.putStringArrayList("mStart", (ArrayList<String>) mStart);
//        timeOneFragment.setArguments(bundle);
//        return timeOneFragment;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_time_page, null);
//        Bundle bundle=getArguments();
//        if(bundle!=null){
//            mNames=bundle.getStringArrayList("mNames");
//            mPaths=bundle.getStringArrayList("mPaths");
//            mNames=bundle.getStringArrayList("mTimes");
//            mNames=bundle.getStringArrayList("mStoreId");
//            mNames=bundle.getStringArrayList("mDistance");
//            mNames=bundle.getStringArrayList("mStart");
//        }
        mListView = (ListView) layoutView.findViewById(R.id.lv_page_timeting);
        timeDay = TimeUtil.nearDate(0);//one是当前的时间
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
