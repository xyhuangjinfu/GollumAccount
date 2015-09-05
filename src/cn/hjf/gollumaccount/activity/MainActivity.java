package cn.hjf.gollumaccount.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.adapter.ConsumeRecordAdapter;
import cn.hjf.gollumaccount.asynctask.IConsumeRecordOperateListener;
import cn.hjf.gollumaccount.asynctask.LoadConsumeRecordTask;
import cn.hjf.gollumaccount.business.ConsumeRecordManagerBusiness;
import cn.hjf.gollumaccount.businessmodel.ConsumeRecord;
import cn.hjf.gollumaccount.businessmodel.ConsumeType;
import cn.hjf.gollumaccount.businessmodel.QueryInfo;
import cn.hjf.gollumaccount.dialog.LoadDialog;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment.HEAD_TYPE;
import cn.hjf.gollumaccount.fragment.SideMenuFragment;
import cn.hjf.gollumaccount.view.LoadingDialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ViewGroup;
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
		IConsumeRecordOperateListener {
    
    private static final int REQ_CODE_QUERY_INFO = 0; //请求修改查询信息请求码
    private static final int NUM_PER_PAGE = 7; // 每页查询的数量

	private SideMenuFragment mSideMenuFragment; //侧滑菜单
    private CommonHeaderFragment mTitleFragment; //顶部标题栏

    private TextView mCurrentMonthSum; //当月累计消费金额
    private Button mAddButton; //记一笔按钮
    private Button mQueryButton; //查询按钮
	private ListView mRecordListView; //消费记录显示ListView
	private View mEmptyView; //ListView没有数据时显示的界面
	private View mFooterView; //底部加载视图
	private LinearLayout mFooterViewLayout; //底部加载视图布局
	
	private LoadingDialog mLoadingDialog; //加载对话框
	
	private List<ConsumeRecord> mRecords; // 查询出来的数据记录
	private ConsumeRecordAdapter mConsumeRecordAdapter; // 消费记录列表显示的适配器
	
	private boolean mIsNoMoreData = false; //当前查询条件是否还有更多数据，true-没有更多数据
	private boolean mIsInRefresh = false; //是否在刷新状态，true-还在刷新
	private boolean mIsQueryChanged = false; //查询条件是否改变，true-查询条件被改变了
	
	private QueryInfo mQueryInfo; //查询信息
	
	
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
        mAddButton = (Button) findViewById(R.id.btn_add);
        mQueryButton = (Button) findViewById(R.id.btn_query);
        
        mLoadingDialog = new LoadingDialog(this, R.style.translucent_dialog);
        
        mRecordListView = (ListView) findViewById(R.id.ptflv_consume_list);
        
        //绑定空视图
        mEmptyView = findViewById(R.id.ly_no_data);
        mRecordListView.setEmptyView(mEmptyView);
        
        //绑定footerView，加载视图
        mFooterView = LayoutInflater.from(this).inflate(R.layout.view_footer_loading, null);
        mFooterViewLayout = new LinearLayout(this);
        mFooterViewLayout.addView(mFooterView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mRecordListView.addFooterView(mFooterViewLayout);
        mFooterView.setVisibility(View.GONE);
        
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
                startActivity(intent);
            }
        });
        
        mQueryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QueryActivity.class);
                intent.putExtra("query_info", mQueryInfo);
                startActivityForResult(intent, REQ_CODE_QUERY_INFO);
            }
        });
        
        mRecordListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Intent intent = new Intent(MainActivity.this, ConsumeDetailActivity.class);
                intent.putExtra(ConsumeDetailActivity.CONSUME_RECORD, mRecords.get(position));
                startActivity(intent);
            }
        });
        
        mRecordListView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
             // 当不滚动时  
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
                    // 判断是否滚动到底部  
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {  
                        //还在刷新中，直接返回
                        if (mIsInRefresh) {
                            return;
                        }
                        //没有更多数据，提示
                        if (mIsNoMoreData) {
                            Toast.makeText(MainActivity.this, "没有更多数据了", 0).show();
                            return;
                        }
                        mFooterView.setVisibility(View.VISIBLE);
                        mQueryInfo.setPageNumber(mQueryInfo.getPageNumber() + 1);
                        loadData();
                    }  
                }
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount) {
            }
        });
    }
	
	
	/**
	 * 加载数据
	 */
	private void loadData() {
	    mIsInRefresh = true;
//	    mFooterView.setVisibility(View.VISIBLE);
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
            Intent intentAbout = new Intent(this, AboutActivity.class);
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
                mQueryInfo = data.getParcelableExtra("query_info");
                if (mQueryInfo != null) {
                    refreshQueryStatus();
                    mLoadingDialog.show();
                    loadData();
                }
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
    public void OnRecordLoadCompleted(List<ConsumeRecord> consumeRecords) {
        mIsInRefresh = false;
        if (consumeRecords.size() == 0) {
            mIsNoMoreData = true;
            mFooterView.setVisibility(View.GONE);
        }
        if (mIsQueryChanged) {
            mRecords.clear();
            mIsQueryChanged = false;
        }
        mRecords.addAll(consumeRecords);
        mConsumeRecordAdapter.notifyDataSetChanged();
        mLoadingDialog.cancel();
    }

}
