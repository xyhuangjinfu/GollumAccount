package cn.hjf.gollumaccount.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.adapter.ConsumeTypeAdapter;
import cn.hjf.gollumaccount.asynctask.ConsumeTypeOperateListener;
import cn.hjf.gollumaccount.asynctask.LoadConsumeTypeTask;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment.HEAD_TYPE;
import cn.hjf.gollumaccount.model.ConsumeType;
import cn.hjf.gollumaccount.view.LoadingDialog;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * 消费类型选择界面
 * 
 * @author xfujohn
 * 
 */
public class TypeSelectActivity extends BaseActivity implements
        CommonHeaderFragment.ICallback, ConsumeTypeOperateListener {

    private CommonHeaderFragment mTitleFragment; // 顶部标题栏
    private LoadingDialog mLoadingDialog; // 加载对话框

    private GridView mTypeView; // 消费类型列表
    private ConsumeTypeAdapter mAdapter; // 消费类型显示适配器
    private List<ConsumeType> mTypeData; // 消费类型数据

    public TypeSelectActivity() {
        mTypeData = new ArrayList<ConsumeType>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_select);

        initTitle();
        initView();
        initValue();
        initEvent();

        mLoadingDialog.show();
        new LoadConsumeTypeTask(this, this).execute();
    }

    /**
     * 初始化顶部导航栏
     */
    @Override
    public void initTitle() {
        mTitleFragment = (CommonHeaderFragment) mFragmentManager
                .findFragmentById(R.id.title_type_select);
        mTitleFragment.setHeadBtnType(HEAD_TYPE.LEFT_BACK_TEXT,
                HEAD_TYPE.RIGHT_NULL);
        mTitleFragment.setHeadText(R.string.title_back,
                R.string.title_type_select, null);
        mTitleFragment.setCallback(this);
    }

    @Override
    protected void initView() {
        mTypeView = (GridView) findViewById(R.id.gv_type);

        mLoadingDialog = new LoadingDialog(this, R.style.translucent_dialog);
        mLoadingDialog.setCancelable(false);
    }

    @Override
    protected void initValue() {
        mAdapter = new ConsumeTypeAdapter(this, mTypeData);
        mTypeView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        mTypeView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                if (mTypeData.get(position).getType() == ConsumeType.Type.CONTROL) {
                    Toast.makeText(getApplicationContext(), "暂时还不支持此功能", 0)
                            .show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("consume_type", mTypeData.get(position));
                    TypeSelectActivity.this.setResult(Activity.RESULT_OK,
                            intent);
                    TypeSelectActivity.this.finish();
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

    @Override
    public void OnTypeLoadCompleted(List<ConsumeType> consumeTypes) {
        mTypeData.addAll(consumeTypes);
        Collections.sort(mTypeData);
        mAdapter.notifyDataSetChanged();
        mLoadingDialog.cancel();
    }

}
