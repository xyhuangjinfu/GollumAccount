package cn.hjf.gollumaccount.activity;

import cn.hjf.gollumaccount.R;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

/**
 * 所有Activity的基类
 * 
 * @author huangjinfu
 * 
 */
public abstract class BaseActivity extends FragmentActivity {

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

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
