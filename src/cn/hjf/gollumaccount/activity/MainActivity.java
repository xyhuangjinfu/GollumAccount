package cn.hjf.gollumaccount.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.adapter.ConsumeRecordAdapter;
import cn.hjf.gollumaccount.asynctask.DeleteConsumeRecordTask;
import cn.hjf.gollumaccount.asynctask.LoadConsumeRecordTask;
import cn.hjf.gollumaccount.asynctask.StatisticSumAsyncTask;
import cn.hjf.gollumaccount.businessmodel.ConsumeRecord;
import cn.hjf.gollumaccount.businessmodel.QueryInfo;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment.HEAD_TYPE;
import cn.hjf.gollumaccount.fragment.SideMenuFragment;
import cn.hjf.gollumaccount.util.NumberUtil;
import cn.hjf.gollumaccount.view.LoadingDialog;
import cn.hjf.gollumaccount.view.PullListView;
import cn.hjf.gollumaccount.view.PullListView.OnRefreshListener;
import cn.hjf.gollumaccount.view.SwipeListView;
import cn.hjf.gollumaccount.view.SwipeListView.OnViewClickListener;
import cn.hjf.gollumaccount.view.TipDialog;
import cn.hjf.gollumaccount.view.TipDialog.OnTipDialogClickListener;
import cn.hjf.gollumaccount.view.ToastUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends BaseActivity implements
		SideMenuFragment.NavigationDrawerCallbacks,
		CommonHeaderFragment.ICallback,
		LoadConsumeRecordTask.LoadConsumeRecordListener,
		StatisticSumAsyncTask.OnStatisticSumListener,
		DeleteConsumeRecordTask.OnDeleteConsumeRecordListener {
    
    private static final int REQ_CODE_QUERY_INFO  = 0; //请求修改查询信息请求码
    private static final int REQ_CODE_ADD_RECORD  = 1; //请求新建消费记录请求码
    private static final int REQ_CODE_VIEW_RECORD = 2; //请求查看消费记录请求码
    private static final int NUM_PER_PAGE = 20; // 每页查询的数量
    private static final long EXIT_TIME = 2000; //退出程序的时间阈值，两次返回键间隔

	private SideMenuFragment mSideMenuFragment; //侧滑菜单
    private CommonHeaderFragment mTitleFragment; //顶部标题栏
    private LoadingDialog mLoadingDialog; //加载对话框
    private TipDialog mTipDialog; //删除提示对话框

    private TextView mCurrentMonthSum; //当月累计消费金额
    private Button mAddButton; //记一笔按钮
    private Button mQueryButton; //查询按钮
    private PullListView mRecordListView; //消费记录显示ListView
	private View mEmptyView; //ListView没有数据时显示的界面
	
	private List<ConsumeRecord> mRecords; // 查询出来的数据记录
	private ConsumeRecordAdapter mConsumeRecordAdapter; // 消费记录列表显示的适配器
	
	private boolean mNeedClearData = false; //是否需要清空已有的数据集合，true-需要
	private QueryInfo mQueryInfo; //查询信息
    private long mLastBackTime; //上一次按下返回键的时间，不考虑关闭侧边栏的动作
    
    private int mDeletePosition; //被删除消费记录的位置
	
	
	public MainActivity() {
	    mQueryInfo = new QueryInfo();
	    mRecords = new ArrayList<ConsumeRecord>();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initSideMenu();
		
		initTitle();
		initView();
		initValue();
		initEvent();
		
		mRecordListView.setPull(PullListView.PullMode.UP);
		loadData();
		new StatisticSumAsyncTask(this, this).execute(Calendar.getInstance());
	}
	
	   /**
     * 初始化侧滑菜单
     */
    private void initSideMenu() {
        mSideMenuFragment = (SideMenuFragment) mFragmentManager.findFragmentById(R.id.navigation_drawer);
        mSideMenuFragment.setUp(R.id.navigation_drawer,(DrawerLayout) findViewById(R.id.drawer_layout));
    }
	
    /**
     * 初始化顶部导航栏
     */
	@Override
    public void initTitle() {
        mTitleFragment = (CommonHeaderFragment) mFragmentManager.findFragmentById(R.id.title_main);
        mTitleFragment.setHeadBtnType(HEAD_TYPE.LEFT_MENU, HEAD_TYPE.RIGHT_NULL);
        mTitleFragment.setHeadText(null, R.string.title_main, null);
        mTitleFragment.setCallback(this);
    }
	
    @Override
    protected void initView() {
        mCurrentMonthSum = (TextView) findViewById(R.id.tv_current_month);
        mAddButton = (Button) findViewById(R.id.btn_add);
        mQueryButton = (Button) findViewById(R.id.btn_query);
        
        mRecordListView = (PullListView) findViewById(R.id.ptflv_consume_list);
        mRecordListView.setPullMode(PullListView.PULL_UP);
        
        //绑定空视图
        mEmptyView = findViewById(R.id.ly_no_data);
        mRecordListView.setEmptyView(mEmptyView);
        
        mLoadingDialog = new LoadingDialog(this, R.style.translucent_dialog);
        mLoadingDialog.setCancelable(false);
        
        mTipDialog = new TipDialog(this, R.style.transparent_dialog1);
    }

    @Override
    protected void initValue() {
        mQueryInfo.setPageNumber(1);
        mQueryInfo.setPageSize(NUM_PER_PAGE);
        mConsumeRecordAdapter = new ConsumeRecordAdapter(this, mRecords);
        mRecordListView.setAdapter(mConsumeRecordAdapter);
        
        mTipDialog.setTitle(getString(R.string.tip_delete_record));
    }
    
    @Override
    protected void initEvent() {
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddConsumeActivity.class);
                startActivityForResult(intent, REQ_CODE_ADD_RECORD);
            }
        });
        
        mQueryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QueryActivity.class);
                intent.putExtra(QueryActivity.QUERY_INFO, mQueryInfo);
                startActivityForResult(intent, REQ_CODE_QUERY_INFO);
            }
        });
        
        mRecordListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Intent intent = new Intent(MainActivity.this, ConsumeDetailActivity.class);
                intent.putExtra(ConsumeDetailActivity.CONSUME_RECORD, mRecords.get(position));
                startActivityForResult(intent, REQ_CODE_VIEW_RECORD);
            }
        });
        
        mRecordListView.setOnRefreshListener(new OnRefreshListener() {
            
            @Override
            public void onPullUpRefresh() {
                mQueryInfo.setPageNumber(mQueryInfo.getPageNumber() + 1);
                loadData();
            }
            
            @Override
            public void onPullDownRefresh() {
            }
        });
        
        ((SwipeListView)mRecordListView.getListView()).setOnViewClickListener(new OnViewClickListener() {
            @Override
            public void onViewClick(int viewId, int position) {
                if (viewId == R.id.btn_delete) {
                    mDeletePosition = position;
                    mTipDialog.show();
                }
            }
        });
        
        mTipDialog.setOnTipDialogClickListener(new OnTipDialogClickListener() {
            @Override
            public void onPositiveClick() {
                mLoadingDialog.show();
                new DeleteConsumeRecordTask(MainActivity.this, MainActivity.this).execute(mRecords.get(mDeletePosition));
            }
            @Override
            public void onNegativeClick() {
            }
        });
    }
	
	
	/**
	 * 加载数据
	 */
	private void loadData() {
	    new LoadConsumeRecordTask(this, this).execute(new QueryInfo[]{mQueryInfo});
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		switch (position) {
		case 0:
			Intent intentStatisticSelect = new Intent(this, StatisticSelectActivity.class);
			this.startActivity(intentStatisticSelect);
			break;
		case 1:
            Intent intentAbout = new Intent(this, SettingActivity.class);
            this.startActivity(intentAbout);
            break;
		default:
			break;
		}
		
	}

    /**
     * 刷新查询状态
     */
    private void refreshQueryStatus() {
        mQueryInfo.setPageNumber(1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //更改查询条件，清空数据集，从第一页按已有的QueryInfo进行查询
            if (requestCode == REQ_CODE_QUERY_INFO) {
                mQueryInfo = data.getParcelableExtra(QueryActivity.QUERY_INFO);
                if (mQueryInfo != null) {
                    refreshQueryStatus();
                    mNeedClearData = true;
                    mRecordListView.setPull(PullListView.PullMode.UP);
                    loadData();
                }
                return;
            }
            //添加消费记录，清空数据集，从第一页按空的QueryInfo进行查询
            if (requestCode == REQ_CODE_ADD_RECORD || requestCode == REQ_CODE_VIEW_RECORD) {
                mNeedClearData = true;
                mRecordListView.setPull(PullListView.PullMode.UP);
                //清空QueryInfo
                mQueryInfo.setPageNumber(1);
                mQueryInfo.setEndTime(null);
                mQueryInfo.setStartTime(null);
                mQueryInfo.setType(null);
                mQueryInfo.setName(null);
                loadData();
                //计算本月金额总计
                new StatisticSumAsyncTask(this, this).execute(Calendar.getInstance());
                return;
            }
        }
    }

    @Override
    public void onLeftClick() {
        if (mSideMenuFragment.isDrawerOpen()) {
            mSideMenuFragment.close();
        } else {
            mSideMenuFragment.open();
        }
    }

    @Override
    public void onRightClick() {
    }
    
    @Override
    public void onBackPressed() {
        if (mSideMenuFragment.isDrawerOpen()) {
            mSideMenuFragment.close();
            return;
        }
        if ( (System.currentTimeMillis() - mLastBackTime) <= EXIT_TIME) {
            finish();
            return;
        } else {
            mLastBackTime = System.currentTimeMillis();
            ToastUtil.showToast(getApplicationContext(), getResources().getString(R.string.tip_exit_app), Toast.LENGTH_SHORT);
            return;
        }
    }
    
    @Override
    protected boolean needFinishAnimate() {
        return false;
    }

    @Override
    public void OnLoadRecordCompleted(List<ConsumeRecord> consumeRecords) {
        if (consumeRecords.size() == 0) {
            ToastUtil.showToast(this, getResources().getString(R.string.tip_no_more_data), 0);
        }
        if (mNeedClearData) {
            mRecords.clear();
            mNeedClearData = false;
        }
        mRecords.addAll(consumeRecords);
        mConsumeRecordAdapter.notifyDataSetChanged();
        mRecordListView.reset();
    }

    @Override
    public void onStatisticSumCompleted(Double sum) {
        mCurrentMonthSum.setText(NumberUtil.formatValue(sum));
    }

    @Override
    public void OnDeleteConsumeRecordCompleted(boolean isCreateSuccess) {
        mLoadingDialog.cancel();
        if (isCreateSuccess) {
            ToastUtil.showToast(this, String.format(getString(R.string.tip_delete_record_success), mRecords.get(mDeletePosition).getRecordName()), Toast.LENGTH_SHORT);
            mRecords.remove(mDeletePosition);
            mConsumeRecordAdapter.notifyDataSetChanged();
        } else {
            ToastUtil.showToast(this, String.format(getString(R.string.tip_delete_record_fail), mRecords.get(mDeletePosition).getRecordName()), Toast.LENGTH_SHORT);
        }
        
    }

}
