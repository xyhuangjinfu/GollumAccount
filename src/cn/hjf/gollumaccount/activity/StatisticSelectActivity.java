package cn.hjf.gollumaccount.activity;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.activity.MainActivity;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment.HEAD_TYPE;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * 消费统计选择界面
 * 
 * @author huangjinfu
 * 
 */
public class StatisticSelectActivity extends BaseActivity implements CommonHeaderFragment.ICallback{

    private Button mByItemButton;
    private Button mByMonthButton;
    /**
     * 顶部标题栏
     */
    private CommonHeaderFragment mTitleFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_analyse);
        
        initTitle();
        initView();
        initValue();
        initEvent();
    }
    
    /**
     * 初始化顶部导航栏
     */
    @Override
    public void initTitle() {
        mTitleFragment = (CommonHeaderFragment) mFragmentManager.findFragmentById(R.id.title_statistic_select);
        mTitleFragment.setHeadBtnType(HEAD_TYPE.LEFT_BACK_TEXT,HEAD_TYPE.RIGHT_NULL);
        mTitleFragment.setHeadText(R.string.title_back, R.string.title_analyse, null);
        mTitleFragment.setCallback(this);
    }

    /**
     * 初始化各控件
     */
    @Override
    protected void initView() {
        mByItemButton = (Button) findViewById(R.id.btn_pie_by_item);
        mByMonthButton = (Button) findViewById(R.id.btn_line_by_month);
    }

    /**
     * 初始化各控件的值
     */
    @Override
    protected void initValue() {
    }

    /**
     * 初始化各控件的事件
     */
    @Override
    protected void initEvent() {
        mByItemButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticSelectActivity.this, TypeStatisticActivity.class);
                StatisticSelectActivity.this.startActivity(intent);
            }
        });

        mByMonthButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticSelectActivity.this, MonthStatisticActivity.class);
                StatisticSelectActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {
    }
}
