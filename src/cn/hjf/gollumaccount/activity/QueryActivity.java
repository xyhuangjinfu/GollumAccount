package cn.hjf.gollumaccount.activity;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment.HEAD_TYPE;
import android.os.Bundle;
import android.widget.EditText;

public class QueryActivity extends BaseActivity implements CommonHeaderFragment.ICallback {
    
    /**
     * 顶部标题栏
     */
    private CommonHeaderFragment mTitleFragment;
    
    private EditText mConsumeName;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        
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
        mTitleFragment = (CommonHeaderFragment) mFragmentManager.findFragmentById(R.id.title_query_record);
        mTitleFragment.setHeadBtnType(HEAD_TYPE.LEFT_BACK_TEXT,HEAD_TYPE.RIGHT_NULL);
        mTitleFragment.setHeadText(R.string.title_back, R.string.title_query_record, null);
        mTitleFragment.setCallback(this);
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void initValue() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void initEvent() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {
    }

}
