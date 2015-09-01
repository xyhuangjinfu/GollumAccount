package cn.hjf.gollumaccount.activity;

import java.util.ArrayList;
import java.util.List;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.adapter.ConsumeTypeAdapter;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment.HEAD_TYPE;
import cn.hjf.gollumaccount.model.ConsumeType;
import android.os.Bundle;
import android.widget.GridView;

/**
 * 消费类型选择界面
 * @author xfujohn
 *
 */
public class TypeSelectActivity extends BaseActivity implements CommonHeaderFragment.ICallback {
    
    /**
     * 顶部标题栏
     */
    private CommonHeaderFragment mTitleFragment;
    
    private GridView mTypeView;
    private ConsumeTypeAdapter mAdapter;
    private List<ConsumeType> mTypeData;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_select);
        
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
        mTitleFragment = (CommonHeaderFragment) mFragmentManager.findFragmentById(R.id.title_type_select);
        mTitleFragment.setHeadBtnType(HEAD_TYPE.LEFT_BACK_TEXT,HEAD_TYPE.RIGHT_NULL);
        mTitleFragment.setHeadText(R.string.title_back, R.string.title_type_select, null);
        mTitleFragment.setCallback(this);
    }

    @Override
    protected void initView() {
        mTypeView = (GridView) findViewById(R.id.gv_type);
    }

    @Override
    protected void initValue() {
        String[] types = this.getResources().getStringArray(R.array.consume_types);
        if (mTypeData == null) {
            mTypeData = new ArrayList<ConsumeType>();
        }
        for (int i = 0; i < types.length; i++) {
            mTypeData.add(new ConsumeType(types[i]));
        }
        mAdapter = new ConsumeTypeAdapter(this, mTypeData);
        mTypeView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {
    }

}
