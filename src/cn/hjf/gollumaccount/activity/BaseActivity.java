package cn.hjf.gollumaccount.activity;

import cn.hjf.gollumaccount.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.MotionEvent;

/**
 * 所有Activity的基类
 * 
 * @author huangjinfu
 * 
 */
public abstract class BaseActivity extends FragmentActivity {
    /**
     * 上一次触摸屏幕按下动作的时间
     */
    private long mLastDownTouchTime = 0;
    /**
     * 下一次响应触摸事件的间隔，单位ms
     */
    private static final int CLICKABLE_INTERVAL = 500;
    /**
     * v4包的Fragment管理器
     */
    protected FragmentManager mFragmentManager;
    
    /**
     * 是否需要启动其他页面的动画，由子类决定
     * 
     * @return
     */
    protected boolean needStartAnimate() {
        return true;
    }
    
    /**
     * 是否需要结束当前页面的动画，由子类决定
     * 
     * @return
     */
    protected boolean needFinishAnimate() {
        return true;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = this.getSupportFragmentManager();
    }
    
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if (needStartAnimate()) {
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
        }
//        KeyBoardUtil.hideKeyboard(this);
    }
    
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        if (needStartAnimate()) {
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
        }
//        KeyBoardUtil.hideKeyboard(this);
    }

    @Override
    public void finish() {
        super.finish();
        if (needFinishAnimate()) {
            overridePendingTransition(R.anim.slide_in_left,
                    R.anim.slide_out_right);
        }
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            //两次 ACTION_DOWN 的时间小于阈值时（快速点击），消费掉事件，否则正常分发事件。
//            int timeInterval = (int) (System.currentTimeMillis() - mLastDownTouchTime);
//            if (timeInterval < CLICKABLE_INTERVAL) {
//                return true;
//            } else {
//                mLastDownTouchTime = System.currentTimeMillis();
//                return super.dispatchTouchEvent(ev);
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }
    
    /**
     * 初始化通用标题栏
     */
    protected void initTitle(){};
    /**
     * 初始化各控件，负责findView、new View() ，View对象属性设置的功能
     */
    protected abstract void initView();

    /**
     * 初始化各控件的值，负责根据不同条件，初始化各控件显示的值
     */
    protected abstract void initValue();

    /**
     * 初始化各控件的事件，负责根据不同条件，初始化各控件绑定的事件
     */
    protected abstract void initEvent();
}
