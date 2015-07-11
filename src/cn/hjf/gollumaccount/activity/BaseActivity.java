package cn.hjf.gollumaccount.activity;

import android.support.v4.app.FragmentActivity;

/**
 * 所有Activity的基类
 * @author huangjinfu
 *
 */
public abstract class BaseActivity extends FragmentActivity {

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
