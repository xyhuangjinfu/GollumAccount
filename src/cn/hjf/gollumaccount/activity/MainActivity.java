package cn.hjf.gollumaccount.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.adapter.ConsumeRecordAdapter;
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
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends BaseActivity implements
		SideMenuFragment.NavigationDrawerCallbacks,
		CommonHeaderFragment.ICallback,
		LoadConsumeRecordTask.LoadConsumeRecordListener,
		StatisticSumAsyncTask.OnStatisticSumListener {
    
    private static final int REQ_CODE_QUERY_INFO = 0; //请求修改查询信息请求码
    private static final int REQ_CODE_ADD_RECORD = 1; //请求新建消费记录请求码
    private static final int REQ_CODE_VIEW_RECORD = 2; //请求查看消费记录请求码
    private static final int NUM_PER_PAGE = 10; // 每页查询的数量

	private SideMenuFragment mSideMenuFragment; //侧滑菜单
    private CommonHeaderFragment mTitleFragment; //顶部标题栏

    private TextView mCurrentMonthSum; //当月累计消费金额
    private Button mAddButton; //记一笔按钮
    private Button mQueryButton; //查询按钮
    private PullListView mRecordListView; //消费记录显示ListView
	private View mEmptyView; //ListView没有数据时显示的界面
	
	private LoadingDialog mLoadingDialog; //加载对话框
	
	private List<ConsumeRecord> mRecords; // 查询出来的数据记录
	private ConsumeRecordAdapter mConsumeRecordAdapter; // 消费记录列表显示的适配器
	
	private boolean mIsNoMoreData = false; //当前查询条件是否还有更多数据，true-没有更多数据
	private boolean mIsInRefresh = false; //是否在刷新状态，true-还在刷新
	private boolean mIsQueryChanged = false; //查询条件是否改变，true-查询条件被改变了
	
	private QueryInfo mQueryInfo; //查询信息
	
    /**
     * 上一次按下返回键的时间，不考虑关闭侧边栏的动作
     */
    private long mLastBackTime;
    /**
     * 退出程序的时间阈值，两次返回键间隔
     */
    private static final long EXIT_TIME = 2000;
	
	
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
		
		mLoadingDialog.show();
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
        
        mLoadingDialog = new LoadingDialog(this, R.style.translucent_dialog);
        
        mRecordListView = (PullListView) findViewById(R.id.ptflv_consume_list);
        mRecordListView.setPullMode(PullListView.PULL_UP);
        
        //绑定空视图
        mEmptyView = findViewById(R.id.ly_no_data);
        mRecordListView.setEmptyView(mEmptyView);
        
    }

    @Override
    protected void initValue() {
        mQueryInfo.setPageNumber(1);
        mQueryInfo.setPageSize(NUM_PER_PAGE);
        mConsumeRecordAdapter = new ConsumeRecordAdapter(this, mRecords);
        mRecordListView.setAdapter(mConsumeRecordAdapter);
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
//                mQueryInfo.setPageNumber(mQueryInfo.getPageNumber() + 1);
//                loadData();
            }
        });
        
//        mRecordListView.setOnScrollListener(new OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//             // 当不滚动时  
//                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
//                    // 判断是否滚动到底部  
//                    if (view.getLastVisiblePosition() == view.getCount() - 1) {  
//                        //还在刷新中，直接返回
//                        if (mIsInRefresh) {
//                            return;
//                        }
//                        //没有更多数据，提示
//                        if (mIsNoMoreData) {
//                            Toast.makeText(MainActivity.this, "没有更多数据了", 0).show();
//                            return;
//                        }
//                        mFooterView.setVisibility(View.VISIBLE);
//                        mQueryInfo.setPageNumber(mQueryInfo.getPageNumber() + 1);
//                        loadData();
//                    }  
//                }
//            }
//            
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem,
//                    int visibleItemCount, int totalItemCount) {
//            }
//        });
    }
	
	
	/**
	 * 加载数据
	 */
	private void loadData() {
	    mIsInRefresh = true;
	    new LoadConsumeRecordTask(this, this).execute(new QueryInfo[]{mQueryInfo});
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		switch (position) {
		case 0:
			break;
		case 1:
			Intent intentStatisticSelect = new Intent(this, StatisticSelectActivity.class);
			this.startActivity(intentStatisticSelect);
			break;
		case 2:
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
        mIsQueryChanged = true;
        mIsNoMoreData = false;
        mQueryInfo.setPageNumber(1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQ_CODE_QUERY_INFO) {
                mQueryInfo = data.getParcelableExtra(QueryActivity.QUERY_INFO);
                if (mQueryInfo != null) {
                    refreshQueryStatus();
                    mLoadingDialog.show();
                    loadData();
                }
            }
            if (requestCode == REQ_CODE_ADD_RECORD || requestCode == REQ_CODE_VIEW_RECORD) {
                mIsQueryChanged = true;
                mLoadingDialog.show();
                loadData();
                new StatisticSumAsyncTask(this, this).execute(Calendar.getInstance());
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
            Toast.makeText(getApplicationContext(), "再按一次返回键退出", Toast.LENGTH_SHORT).show();
            return;
        }
    }
    
    @Override
    protected boolean needFinishAnimate() {
        return false;
    }

    @Override
    public void OnLoadRecordCompleted(List<ConsumeRecord> consumeRecords) {
        mIsInRefresh = false;
        if (consumeRecords.size() == 0) {
            mIsNoMoreData = true;
//            mFooterView.setVisibility(View.GONE);
        }
        if (mIsQueryChanged) {
            mRecords.clear();
            mIsQueryChanged = false;
        }
        mRecords.addAll(consumeRecords);
        mConsumeRecordAdapter.notifyDataSetChanged();
        mLoadingDialog.cancel();
        mRecordListView.reset();
    }

    @Override
    public void onStatisticSumCompleted(Double sum) {
        mCurrentMonthSum.setText(NumberUtil.formatValue(sum));
    }

}
