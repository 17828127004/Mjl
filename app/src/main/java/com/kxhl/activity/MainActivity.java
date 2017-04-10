package com.kxhl.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kxhl.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import adapter.FragmentsAdapter;
import fragment.FindFragment;
import fragment.HomePageFragment;
import fragment.MyFragment;
import fragment.TimetingFragment;

import static adapter.FragmentsAdapter.OnRgsExtraCheckedChangedListener;

public class MainActivity extends FragmentActivity {
    private RadioGroup main_radiogroup;
    private RadioButton rb_home;
    private RadioButton rb_find;
    private RadioButton rb_timeting;
    private RadioButton rb_my;
    private List<Fragment> fragments = new ArrayList<Fragment>();

    //    private Fragment tab_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragments.add(new HomePageFragment());
        fragments.add(new FindFragment());
        fragments.add(new TimetingFragment());
        fragments.add(new MyFragment());
        initView();
        FragmentsAdapter tabAdapter = new FragmentsAdapter(this, fragments, R.id.tab_content, main_radiogroup);
        tabAdapter.setOnRgsExtraCheckedChangedListener(new OnRgsExtraCheckedChangedListener() {
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
                switch (checkedId) {
                    case R.id.rb_home:
                        rb_home.setBackgroundResource(R.drawable.shouye);
                        rb_find.setBackgroundResource(R.drawable.faxian_no);
                        rb_timeting.setBackgroundResource(R.drawable.yuyue_no);
                        rb_my.setBackgroundResource(R.drawable.wode_no);
                        break;
                    case R.id.rb_find:
                        rb_home.setBackgroundResource(R.drawable.shouye_no);
                        rb_find.setBackgroundResource(R.drawable.faxian);
                        rb_timeting.setBackgroundResource(R.drawable.yuyue_no);
                        rb_my.setBackgroundResource(R.drawable.wode_no);
                        break;
                    case R.id.rb_timeting:
                        rb_home.setBackgroundResource(R.drawable.shouye_no);
                        rb_find.setBackgroundResource(R.drawable.faxian_no);
                        rb_timeting.setBackgroundResource(R.drawable.yuyue);
                        rb_my.setBackgroundResource(R.drawable.wode_no);
                        break;
                    case R.id.rb_my:
                        rb_home.setBackgroundResource(R.drawable.shouye_no);
                        rb_find.setBackgroundResource(R.drawable.faxian_no);
                        rb_timeting.setBackgroundResource(R.drawable.yuyue_no);
                        rb_my.setBackgroundResource(R.drawable.wode);
                        break;
                }
            }
        });
    }

    public void initView() {
        main_radiogroup = (RadioGroup) findViewById(R.id.main_radiogroup);
        rb_home = (RadioButton) findViewById(R.id.rb_home);
        rb_find = (RadioButton) findViewById(R.id.rb_find);
        rb_timeting = (RadioButton) findViewById(R.id.rb_timeting);
        rb_my = (RadioButton) findViewById(R.id.rb_my);
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click(); //调用双击退出函数
        }
        return false;
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }
}
