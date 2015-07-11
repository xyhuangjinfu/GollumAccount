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
import cn.hjf.gollumaccount.fragment.ConsumeFragment;
import cn.hjf.gollumaccount.fragment.NavigationDrawerFragment;
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
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends BaseActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks,
		ConsumeFragment.OnConsumeFragmentCallback,
		LoadConsumeRecordTask.OnRecordLoadCallback,
        ConsumeQueryDialog.OnQueryListener{

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment; //����Fragment

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle; //ActionBar�ı���

	private int mCurrentFragment; // ��ǰ����ֵ�Fragment
	private ConsumeFragment mConsumeFragment; //��Ѽ�¼Fragment
	
	private FragmentManager mFragmentManager; //Fragment������������Fragment������Ƴ�ȹ���
	
	private ConsumeItemService mConsumeItemService; //ConsumeItemҵ���߼�����
	
	
	   private PullToRefreshListView mRecordListView; // ��Ѽ�¼��ʾ��ListView
	    private ListView mActualRecordListView; // ʵ�ʵ���Ѽ�¼��ʾ��ListView
	    private View mEmptyView; // ListView��û�����ʱ��ʾ��View
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
	
	public MainActivity() {
		mConsumeFragment = new ConsumeFragment();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		
		//ʵ��NavigationDrawer����
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		//���ActionBar�ı���
		mTitle = getTitle();

		// Set up the drawer.
		//���ó���
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		//��ǵ�ǰ��ʾ�����ĸ�Fragment����ʼΪ��Ѽ�¼��ʾ�б�Fragment
		mCurrentFragment = FragmentIdConsts.FRAGMENT_CONSUME;
		
		//ʵ��ConsumeItemҵ���߼�����
		mConsumeItemService = new ConsumeItemService(this);
		//��ʼ��ConsumeItem��ֵ
		mConsumeItemService.initConsumeItem();
		
		initView();
		initValue();
		initEvent();
		
			
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
//		menu.setHeaderTitle("����");
//		this.getMenuInflater().inflate(R.menu.context_menu_consume_list, menu);
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		mFragmentManager = getSupportFragmentManager();
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
	 * ����Ϊָ����Fragmentˢ�� Menu
	 * @param fragmentId
	 */
	public void refreshMenuForFragment(int fragmentId) {
		mCurrentFragment = fragmentId;
		Log.i("hjf", "MainActivity - refreshMenuForFragment - fragmentId:" + fragmentId);
		switch (fragmentId) {
		case FragmentIdConsts.FRAGMENT_CONSUME:
			mTitle = getString(R.string.title_consume);
			break;
		case FragmentIdConsts.FRAGMENT_ITEM_MANAGER:
			mTitle = getString(R.string.title_item_manager);
			break;
		case FragmentIdConsts.FRAGMENT_ANALYSE:
			mTitle = getString(R.string.title_analyse);
			break;
		case FragmentIdConsts.FRAGMENT_ABOUT:
			mTitle = getString(R.string.title_about);
			break;
		case FragmentIdConsts.FRAGMENT_ADD_RECORD:
			mTitle = getString(R.string.title_add_record);
			break;
		case FragmentIdConsts.FRAGMENT_VIEW_RECORD:
			mTitle = getString(R.string.title_view_record);
			break;
		case FragmentIdConsts.FRAGMENT_ITEM_COMPARE:
			mTitle = getString(R.string.title_analyse_item);
			break;
		case FragmentIdConsts.FRAGMENT_MONTH_COMPARE:
			mTitle = getString(R.string.title_analyse_month);
			break;
		case FragmentIdConsts.FRAGMENT_MODIFY_RECORD:
			mTitle = getString(R.string.title_modify_record);
			break;
		}
	}

	/**
	 * ���� ActionBar״̬
	 */
	public void restoreActionBar() {
//		ActionBar actionBar = getSupportActionBar();
//		actionBar.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.bg_button_press));
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//		actionBar.setDisplayShowTitleEnabled(true);
//		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i("hjf", "MainActivity - onCreateOptionsMenu - mCurrentFragment:" + mCurrentFragment);
		switch (mCurrentFragment) {
		case FragmentIdConsts.FRAGMENT_CONSUME:
			if (!mNavigationDrawerFragment.isDrawerOpen()) {
				// Only show items in the action bar relevant to this screen
				// if the drawer is not showing. Otherwise, let the drawer
				// decide what to show in the action bar.
				getMenuInflater().inflate(R.menu.menu_consume, menu);
				restoreActionBar();
				return true;
			}
			break;
		case FragmentIdConsts.FRAGMENT_ITEM_MANAGER:
			if (!mNavigationDrawerFragment.isDrawerOpen()) {
				getMenuInflater().inflate(R.menu.menu_item_manager, menu);
				restoreActionBar();
				return true;
			}
			break;
		case FragmentIdConsts.FRAGMENT_ANALYSE:
			if (!mNavigationDrawerFragment.isDrawerOpen()) {
				getMenuInflater().inflate(R.menu.menu_analyse, menu);
				restoreActionBar();
				return true;
			}
			break;
		case FragmentIdConsts.FRAGMENT_ABOUT:
			if (!mNavigationDrawerFragment.isDrawerOpen()) {
				getMenuInflater().inflate(R.menu.menu_about, menu);
				restoreActionBar();
				return true;
			}
			break;
		case FragmentIdConsts.FRAGMENT_ADD_RECORD:
			if (!mNavigationDrawerFragment.isDrawerOpen()) {
				getMenuInflater().inflate(R.menu.menu_add_record, menu);
				restoreActionBar();
				return true;
			}	
			break;
		case FragmentIdConsts.FRAGMENT_VIEW_RECORD:
			if (!mNavigationDrawerFragment.isDrawerOpen()) {
				getMenuInflater().inflate(R.menu.menu_view_record, menu);
				restoreActionBar();
				return true;
			}	
			break;
		case FragmentIdConsts.FRAGMENT_ITEM_COMPARE:
			if (!mNavigationDrawerFragment.isDrawerOpen()) {
				getMenuInflater().inflate(R.menu.menu_analyse_item, menu);
				restoreActionBar();
				return true;
			}	
			break;
		case FragmentIdConsts.FRAGMENT_MONTH_COMPARE:
			if (!mNavigationDrawerFragment.isDrawerOpen()) {
				getMenuInflater().inflate(R.menu.menu_analyse_month, menu);
				restoreActionBar();
				return true;
			}	
		case FragmentIdConsts.FRAGMENT_MODIFY_RECORD:
			if (!mNavigationDrawerFragment.isDrawerOpen()) {
				getMenuInflater().inflate(R.menu.menu_modify_record, menu);
				restoreActionBar();
				return true;
			}
			break;
		default:
			break;
		}
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
		switch (id) {
		case R.id.action_item_add:
//			mFragmentManager
//			.beginTransaction()
//			.replace(R.id.container,
//					mAddItemFragment).commit();
			View view = this.getLayoutInflater().inflate(R.layout.dialog_create_item, null);
			new AlertDialog.Builder(this).setView(view).setNegativeButton("ȡ��", null).setPositiveButton("����", null).create().show();
			break;
		case R.id.action_consume_add:
			this.startActivity(new Intent(this, AddConsumeActivity.class));
			break;
//		case R.id.action_query:
//			Calendar calendar = Calendar.getInstance();
//			new DatePickerDialog(this, new OnDateSetListener() {
//				@Override
//				public void onDateSet(DatePicker view, int year, int monthOfYear,
//						int dayOfMonth) {
//					mConsumeFragment.refreshData(year, monthOfYear);
//				}
//			}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
//			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		super.onKeyDown(keyCode, event);
		if ((mCurrentFragment == 1) || (mCurrentFragment == 2) || (mCurrentFragment == 3) || (mCurrentFragment == 4)) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_info).setTitle("�˳�")
				.setMessage("ȷ���˳������˱���").setNegativeButton("ȡ��", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).setPositiveButton("ȷ��", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						MainActivity.this.finish();
						android.os.Process.killProcess(android.os.Process.myPid());
					}
				}).create().show();
			}
			return true;
		} else {
			return true;
		}
		
	}
	
	
	/**
	 * --------------------------------------------��Fragment���¼��ص�����-----------------------------------------------------
	 */



	/**
	 * ��Ѽ�¼�������ʱ�򱻵���
	 */
	@Override
	public void onConsumeItemClick(ConsumeRecord record) {
	    Intent intent = new Intent(this, ConsumeDetailActivity.class);
	    intent.putExtra(ConsumeDetailActivity.RECORD, record);
	    this.startActivity(intent);
	}

    @Override
    public void onConsumeItemModify(ConsumeRecord record) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void initView() {
        mRecordListView = (PullToRefreshListView) findViewById(R.id.ptflv_consume_list);
        mActualRecordListView = mRecordListView.getRefreshableView();
        mActualRecordListView.setEmptyView(mEmptyView);
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
            // �첽�������
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

        // �������¼�
        mRecordListView.setOnItemClickListener(mItemClickListener);
        // �������������¼���ˢ�����
        mRecordListView.setOnRefreshListener(mOnRefreshListener);

        this.registerForContextMenu(mRecordListView);
    }

    @Override
    protected void initEvent() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onQuery(int year, int month, int item) {
        refreshData(year, month, item);
    }

    @Override
    public void onRecordLoadCompleted(ArrayList<ConsumeRecord> result) {
LoadDialog.close();
        
        //û�����
        if (result.size() == 0) {
            Toast.makeText(this, "û�и�����", Toast.LENGTH_SHORT).show();
        } else {
            // �����´μ���ҳ��
            mCurrentPage++;
        }
        
        // ��Ӳ�ѯ�����ݼ��У�֪ͨListView�������
        mRecords.addAll(result);
        mConsumeRecordAdapter.notifyDataSetChanged();
        // PullToRefreshListViewˢ�½���
        mRecordListView.onRefreshComplete();
        // �Ƿ�����ˢ����ݱ�־
        mIsInRefresh = false;
        // �Ƿ���Ҫˢ����ݱ�־
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



}
