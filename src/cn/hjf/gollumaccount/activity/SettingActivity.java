package cn.hjf.gollumaccount.activity;

import java.util.Arrays;
import java.util.List;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.activity.MainActivity;
import cn.hjf.gollumaccount.adapter.SettingSelectMenuAdapter;
import cn.hjf.gollumaccount.adapter.StatisticSelectMenuAdapter;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 设置选择界面
 * 
 * @author huangjinfu
 * 
 */
public class SettingActivity extends BaseActivity implements CommonHeaderFragment.ICallback{
    
    private CommonHeaderFragment mTitleFragment; //顶部标题栏
    private ListView mMenuList; //菜单列表
    private SettingSelectMenuAdapter mSettingSelectMenuAdapter; //菜单显示适配器
    private List<String> mMenuString; //菜单列表

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_select);
        
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
        mTitleFragment = (CommonHeaderFragment) mFragmentManager.findFragmentById(R.id.title_setting_select);
        mTitleFragment.setHeadBtnType(HEAD_TYPE.LEFT_BACK_TEXT,HEAD_TYPE.RIGHT_NULL);
        mTitleFragment.setHeadText(R.string.title_back, R.string.title_setting, null);
        mTitleFragment.setCallback(this);
    }

    /**
     * 初始化各控件
     */
    @Override
    protected void initView() {
        mMenuList = (ListView) findViewById(R.id.lv_setting_select_menu);
    }

    /**
     * 初始化各控件的值
     */
    @Override
    protected void initValue() {
        mMenuString = Arrays.asList(getResources().getStringArray(R.array.setting_select_menu));
        mSettingSelectMenuAdapter = new SettingSelectMenuAdapter(this, mMenuString);
        mMenuList.setAdapter(mSettingSelectMenuAdapter);
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
                    Intent intent0 = new Intent(SettingActivity.this, AboutActivity.class);
                    SettingActivity.this.startActivity(intent0);
                    break;
                case 1:
//                    Intent intent1 = new Intent(SettingActivity.this, ThemeSettingActivity.class);
//                    SettingActivity.this.startActivity(intent1);
                    Toast.makeText(getApplicationContext(), "暂时还不支持此功能", 0)
                    .show();
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
