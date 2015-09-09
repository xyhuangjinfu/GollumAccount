package cn.hjf.gollumaccount.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.adapter.ConsumeTypeAdapter;
import cn.hjf.gollumaccount.asynctask.LoadConsumeTypeTask;
import cn.hjf.gollumaccount.asynctask.CreateConsumeTypeTask;
import cn.hjf.gollumaccount.businessmodel.ConsumeType;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment.HEAD_TYPE;
import cn.hjf.gollumaccount.view.LoadingDialog;
import cn.hjf.gollumaccount.view.ToastUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
        CommonHeaderFragment.ICallback, LoadConsumeTypeTask.OnLoadConsumeTypeListener, CreateConsumeTypeTask.OnCreateConsumeTypeListener {
    
    public static final String PAGE_TYPE = "page_type"; //从Intent中接收PageType的key
    public static final String CONSUME_TYPE = "consume_type"; //从Intent中接收ConsumeType的key

    private CommonHeaderFragment mTitleFragment; // 顶部标题栏
    private LoadingDialog mLoadingDialog; // 加载对话框
    
    private AlertDialog mCreateTypeDialog; //添加消费类型对话框

    private GridView mTypeView; // 消费类型列表
    private ConsumeTypeAdapter mAdapter; // 消费类型显示适配器
    private List<ConsumeType> mTypeData; // 消费类型数据
    
    private PageType mPageType; //页面工作模式
    
    public enum PageType {
        MANAGER, //消费记录增删改查
        STATISTIC //消费记录统计
    }

    public TypeSelectActivity() {
        mTypeData = new ArrayList<ConsumeType>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_select);

        mPageType = (PageType) getIntent().getSerializableExtra(PAGE_TYPE);
        if (mPageType == null) {
            finish();
            return;
        }
        
        initTitle();
        initView();
        initValue();
        initEvent();

        mLoadingDialog.show();
        new LoadConsumeTypeTask(this, this).execute();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.release();
        mAdapter = null;
        mTypeData.clear();
        mTypeData = null;
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
        
        final EditText et = new EditText(this);
        et.setHint(R.string.hint_add_type);
        mCreateTypeDialog = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
        .setTitle(R.string.label_add_type)
        .setIcon(android.R.drawable.ic_dialog_info) 
        .setView(et)
        .setPositiveButton(getString(R.string.label_add), new OnClickListener() {  
            public void onClick(DialogInterface dialog, int which) {  
                String input = et.getText().toString();  
                if (input.equals("")) {  
                    ToastUtil.showToast(getApplicationContext(), getResources().getString(R.string.hint_type_name_null), Toast.LENGTH_LONG);
                    return;
                }
                if (input.length() > 4) {
                    ToastUtil.showToast(getApplicationContext(), getString(R.string.hint_type_name_length_long), Toast.LENGTH_LONG);
                    return;
                }
                else {
                    ConsumeType type = new ConsumeType(input, ConsumeType.Type.CUSTOME);
                    mLoadingDialog.show();
                    new CreateConsumeTypeTask(TypeSelectActivity.this, TypeSelectActivity.this).execute(type); 
                }  
            }  
        })  
        .setNegativeButton(getString(R.string.label_cancel), null).create();
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
                    mCreateTypeDialog.show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(CONSUME_TYPE, mTypeData.get(position));
                    TypeSelectActivity.this.setResult(Activity.RESULT_OK, intent);
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
    public void OnLoadConsumeTypeCompleted(List<ConsumeType> consumeTypes) {
        mTypeData.clear();
        mTypeData.addAll(consumeTypes);
        if (mPageType == PageType.STATISTIC) {
            ConsumeType allType = new ConsumeType();
            allType.setId(0);
            allType.setName(getResources().getString(R.string.label_all_type));
            allType.setType(ConsumeType.Type.CUSTOME);
            mTypeData.add(allType);
        }
        Collections.sort(mTypeData);
        mAdapter.notifyDataSetChanged();
        mLoadingDialog.cancel();
    }

    @Override
    public void OnCreateConsumeTypeCompleted(boolean isCreateSucess) {
        mLoadingDialog.cancel();
        if (isCreateSucess) {
            mLoadingDialog.show();
            new LoadConsumeTypeTask(TypeSelectActivity.this, TypeSelectActivity.this).execute(); 
        } else {
            ToastUtil.showToast(getApplicationContext(), getResources().getString(R.string.tip_create_consume_type_fail), Toast.LENGTH_LONG);
        }
    }

}
