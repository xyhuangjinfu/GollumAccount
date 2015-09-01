package cn.hjf.gollumaccount.activity;

import java.util.ArrayList;
import java.util.Calendar;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import cn.hjf.gollumaccount.FragmentIdConsts;
import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.adapter.ConsumeRecordAdapter;
import cn.hjf.gollumaccount.asynctask.LoadConsumeRecordTask;
import cn.hjf.gollumaccount.business.ConsumeItemService;
import cn.hjf.gollumaccount.dialog.ConsumeQueryDialog;
import cn.hjf.gollumaccount.dialog.LoadDialog;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment.HEAD_TYPE;
import cn.hjf.gollumaccount.fragment.SideMenuFragment;
import cn.hjf.gollumaccount.model.ConsumeRecord;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends BaseActivity implements
		SideMenuFragment.NavigationDrawerCallbacks,
		CommonHeaderFragment.ICallback,
		LoadConsumeRecordTask.OnRecordLoadCallback,
        ConsumeQueryDialog.OnQueryListener{

	/**
	 * 侧滑菜单
	 */
	private SideMenuFragment mSideMenuFragment; //����Fragment

	   /**
     * 顶部标题栏
     */
    private CommonHeaderFragment mTitleFragment;

    private TextView mCurrentMonthSum;
    private Button mAdd;
    private Button mQuery;
	private PullToRefreshListView mRecordListView;
	private ListView mActualRecordListView;
	private View mEmptyView;
	
	    private boolean mIsInRefresh = false; // �Ƿ�����ִ��ˢ�²������������ִ��ˢ�²���������������ˢ�����
	    private int mQueryYear = 0; // ��ѯ���
	    private int mQueryMonth = 0; // ��ѯ�·�
	    
	    private ConsumeQueryDialog mConsumeQueryDialog; //��ѯ�Ի���
	    private int mCurrentQueryItem = 9; //��ǰ�鿴�ķ���
	    private ArrayList<ConsumeRecord> mRecords; // �����ʾ��Ѽ�¼�����
	    private boolean mNeedRefreshFlag = true; // �Ƿ���Ҫˢ��ҳ�����
	    private ConsumeRecordAdapter mConsumeRecordAdapter; // ListView��������
	    private int mCurrentPage = 1; // ��ǰ�����ʾ���ڼ�ҳ��Ĭ����ʾ��һҳ
	    private LoadConsumeRecordTask mLoadConsumeRecordTask; // ������Ѽ�¼��ݵ�AsyncTask
	    private static final int NUM_PER_PAGE = 10; // ÿҳ��ʾ���������
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initTitle();
		initSideMenu();
		initView();
		initValue();
		initEvent();
		
			
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
	
	/**
	 * 初始化侧滑菜单
	 */
	private void initSideMenu() {
        mSideMenuFragment = (SideMenuFragment) mFragmentManager.findFragmentById(R.id.navigation_drawer);
        mSideMenuFragment.setUp(R.id.navigation_drawer,(DrawerLayout) findViewById(R.id.drawer_layout));
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

    @Override
    protected void initView() {
        mAdd = (Button) findViewById(R.id.btn_add);
        mQuery = (Button) findViewById(R.id.btn_query);
        mRecordListView = (PullToRefreshListView) findViewById(R.id.ptflv_consume_list);
        mActualRecordListView = mRecordListView.getRefreshableView();
//        mActualRecordListView.setEmptyView(mEmptyView);
        mConsumeQueryDialog = new ConsumeQueryDialog(this);
        mConsumeQueryDialog.setOnQueryListener(this);
    }

    @Override
    protected void initValue() {
        mRecords = new ArrayList<ConsumeRecord>();
        mConsumeRecordAdapter = new ConsumeRecordAdapter(this,
                mRecords);
        mRecordListView.setAdapter(mConsumeRecordAdapter);

        mQueryYear = Calendar.getInstance().get(Calendar.YEAR);
        mQueryMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;

        if (mNeedRefreshFlag) {
            Integer[] params = new Integer[5];
            params[0] = mQueryYear;
            params[1] = mQueryMonth;
            params[2] = mCurrentPage;
            params[3] = NUM_PER_PAGE;
            params[4] = mCurrentQueryItem;
            Log.i("hjf", "ConsumeFragment - onCreateView - params[0]:"
                    + params[0]);
            Log.i("hjf", "ConsumeFragment - onCreateView - params[1]:"
                    + params[1]);
            Log.i("hjf", "ConsumeFragment - onCreateView - params[2]:"
                    + params[2]);
            Log.i("hjf", "ConsumeFragment - onCreateView - params[3]:"
                    + params[3]);
            Log.i("hjf", "ConsumeFragment - onCreateView - params[4]:"
                    + params[4]);
            if (!mIsInRefresh) {
                mIsInRefresh = true;
                LoadDialog.show(this);
                new LoadConsumeRecordTask(this, this)
                        .execute(params);
            }

        }

        mRecordListView.setOnItemClickListener(mItemClickListener);
        mRecordListView.setOnRefreshListener(mOnRefreshListener);

        this.registerForContextMenu(mRecordListView);
    }

    @Override
    protected void initEvent() {
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddConsumeActivity.class);
                startActivity(intent);
            }
        });
        
        mQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConsumeQueryDialog.show();
            }
        });
    }

    @Override
    public void onQuery(int year, int month, int item) {
        refreshData(year, month, item);
    }

    @Override
    public void onRecordLoadCompleted(ArrayList<ConsumeRecord> result) {
        LoadDialog.close();
        if (result.size() == 0) {
        } else {
            mCurrentPage++;
        }
        
        mRecords.addAll(result);
        mConsumeRecordAdapter.notifyDataSetChanged();
        mRecordListView.onRefreshComplete();
        mIsInRefresh = false;
        if (mNeedRefreshFlag) {
            mNeedRefreshFlag = false;
        }

    }
    
    
    /**
     * ˢ�����
     * 
     * @param year
     * @param month
     */
    public void refreshData(int year, int month, int item) {
        mCurrentQueryItem = item;
        mCurrentPage = 1;
        mQueryYear = year;
        mQueryMonth = month + 1;
        mRecords.clear();
        mConsumeRecordAdapter.notifyDataSetChanged();
        Integer[] params = new Integer[5];
        params[0] = mQueryYear;
        params[1] = mQueryMonth;
        params[2] = mCurrentPage;
        params[3] = NUM_PER_PAGE;
        params[4] = mCurrentQueryItem;
        Log.i("hjf", "ConsumeFragment - refreshData - params[0]:" + params[0]);
        Log.i("hjf", "ConsumeFragment - refreshData - params[1]:" + params[1]);
        Log.i("hjf", "ConsumeFragment - refreshData - params[2]:" + params[2]);
        Log.i("hjf", "ConsumeFragment - refreshData - params[3]:" + params[3]);
        Log.i("hjf", "ConsumeFragment - refreshData - params[4]:" + params[4]);
        if (!mIsInRefresh) {
            mIsInRefresh = true;
            LoadDialog.show(this);
            new LoadConsumeRecordTask(MainActivity.this,
                    MainActivity.this).execute(params);
        }
    }




    /**
     * ListView Item �����¼�
     */
    OnItemClickListener mItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            Intent intent = new Intent(MainActivity.this, ConsumeDetailActivity.class);
            intent.putExtra(ConsumeDetailActivity.RECORD, mRecords.get(position - 1));
            MainActivity.this.startActivity(intent);
            
        }
    };

    /**
     * ListView���������¼�
     */
    OnRefreshListener2<ListView> mOnRefreshListener = new OnRefreshListener2<ListView>() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            Integer[] params = new Integer[5];
            params[0] = mQueryYear;
            params[1] = mQueryMonth;
            params[2] = mCurrentPage;
            params[3] = NUM_PER_PAGE;
            params[4] = mCurrentQueryItem;
            Log.i("hjf", "ConsumeFragment - onPullUpToRefresh - params[0]:"
                    + params[0]);
            Log.i("hjf", "ConsumeFragment - onPullUpToRefresh - params[1]:"
                    + params[1]);
            Log.i("hjf", "ConsumeFragment - onPullUpToRefresh - params[2]:"
                    + params[2]);
            Log.i("hjf", "ConsumeFragment - onPullUpToRefresh - params[3]:"
                    + params[3]);
            Log.i("hjf", "ConsumeFragment - onPullUpToRefresh - params[4]:"
                    + params[4]);
            if (!mIsInRefresh) {
                mIsInRefresh = true;
                new LoadConsumeRecordTask(MainActivity.this,
                        MainActivity.this).execute(params);
            }

        }
    };


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

}
