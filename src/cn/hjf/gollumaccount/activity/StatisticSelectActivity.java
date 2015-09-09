package cn.hjf.gollumaccount.activity;

import java.util.Arrays;
import java.util.List;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.adapter.StatisticSelectMenuAdapter;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment.HEAD_TYPE;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 消费统计选择界面
 * 
 * @author huangjinfu
 * 
 */
public class StatisticSelectActivity extends BaseActivity implements CommonHeaderFragment.ICallback{
    
    private CommonHeaderFragment mTitleFragment; //顶部标题栏
    private ListView mMenuList; //菜单列表
    private StatisticSelectMenuAdapter mStatisticSelectMenuAdapter; //菜单显示适配器
    private List<String> mMenuString; //菜单列表

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_select);
        
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
        mMenuList = (ListView) findViewById(R.id.lv_statistic_select_menu);
    }

    /**
     * 初始化各控件的值
     */
    @Override
    protected void initValue() {
        mMenuString = Arrays.asList(getResources().getStringArray(R.array.statistic_select_menu));
        mStatisticSelectMenuAdapter = new StatisticSelectMenuAdapter(this, mMenuString);
        mMenuList.setAdapter(mStatisticSelectMenuAdapter);
    }

    /**
     * 初始化各控件的事件
     */
    @Override
    protected void initEvent() {
        mMenuList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                switch (position) {
                case 0:
                    Intent intent0 = new Intent(StatisticSelectActivity.this, TypeStatisticActivity.class);
                    StatisticSelectActivity.this.startActivity(intent0);
                    break;
                case 1:
                    Intent intent1 = new Intent(StatisticSelectActivity.this, MonthStatisticActivity.class);
                    StatisticSelectActivity.this.startActivity(intent1);
                    break;
                default:
                    break;
                }
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
