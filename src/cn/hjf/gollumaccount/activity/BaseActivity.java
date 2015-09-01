package cn.hjf.gollumaccount.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * 所有Activity的基类
 * 
 * @author huangjinfu
 * 
 */
public abstract class BaseActivity extends FragmentActivity {
    
    /**
     * v4包的Fragment管理器
     */
    protected FragmentManager mFragmentManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = this.getSupportFragmentManager();
    }
    
    /**
     * 初始化通用标题栏
     */
    protected void initTitle(){};
    /**
     * 初始化各控件
     */
    protected abstract void initView();

    /**
     * 初始化各控件的值
     */
    protected abstract void initValue();

    /**
     * 初始化各控件的事件
     */
    protected abstract void initEvent();
}
