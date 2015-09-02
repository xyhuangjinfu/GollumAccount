package cn.hjf.gollumaccount.activity;

import java.util.ArrayList;
import java.util.List;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.adapter.ConsumeTypeAdapter;
import cn.hjf.gollumaccount.business.ConsumeRecordManagerBusiness;
import cn.hjf.gollumaccount.business.ConsumeTypeManagerBusiness;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment.HEAD_TYPE;
import cn.hjf.gollumaccount.model.ConsumeType;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
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
    
    private ConsumeTypeManagerBusiness mConsumeTypeManagerBusiness;
    
    public TypeSelectActivity() {
        mConsumeTypeManagerBusiness = new ConsumeTypeManagerBusiness(this);
    }
    
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
        mTypeData = mConsumeTypeManagerBusiness.getAllType();
        mAdapter = new ConsumeTypeAdapter(this, mTypeData);
        mTypeView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        mTypeView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                if (position + 1 == mTypeData.size()) {
                    Toast.makeText(getApplicationContext(), "add", 0).show();
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
