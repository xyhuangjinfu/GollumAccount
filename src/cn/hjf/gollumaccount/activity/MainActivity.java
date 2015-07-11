package cn.hjf.gollumaccount.activity;

import java.util.Calendar;

import cn.hjf.gollumaccount.FragmentIdConsts;
import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.business.ConsumeItemService;
import cn.hjf.gollumaccount.fragment.AboutFragment;
import cn.hjf.gollumaccount.fragment.AddItemFragment;
import cn.hjf.gollumaccount.fragment.AddRecordFragment;
import cn.hjf.gollumaccount.fragment.AnalyseFragment;
import cn.hjf.gollumaccount.fragment.ConsumeFragment;
import cn.hjf.gollumaccount.fragment.ItemCompareFragment;
import cn.hjf.gollumaccount.fragment.ItemManagerFragment;
import cn.hjf.gollumaccount.fragment.MonthCompareFragment;
import cn.hjf.gollumaccount.fragment.NavigationDrawerFragment;
import cn.hjf.gollumaccount.fragment.RecordDetailViewFragment;
import cn.hjf.gollumaccount.fragment.ModifyRecordFragment;
import cn.hjf.gollumaccount.model.ConsumeRecord;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.WindowManager;
import android.support.v4.widget.DrawerLayout;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks,
		AddRecordFragment.OnAddRecordCallback,
		ConsumeFragment.OnConsumeFragmentCallback,
		RecordDetailViewFragment.OnViewRecordCallback,
		AnalyseFragment.OnAnalyseCallback,
		ItemCompareFragment.OnItemCompareCallback,
		MonthCompareFragment.OnMonthCompareCallback,
		ModifyRecordFragment.OnModifyRecordCallback {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment; //抽屉Fragment

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle; //ActionBar的标题

	private int mCurrentFragment; // 当前所呈现的Fragment
	private ItemManagerFragment mItemManagerFragment; //账目管理Fragment
	private ConsumeFragment mConsumeFragment; //消费记录Fragment
	private AnalyseFragment mAnalyseFragment; //消费统计Fragment
	private AboutFragment mAboutFragment; //关于信息Fragment
	private AddItemFragment mAddItemFragment; //添加账目Fragment
	private AddRecordFragment mAddRecordFragment; //添加消费记录Fragment
	private RecordDetailViewFragment mRecordDetailViewFragment; //消费详细信息查看Fragment
	private ItemCompareFragment mItemCompareFragment; //按类型查看图表
	private MonthCompareFragment mMonthCompareFragment; //按月份查看图表
	private ModifyRecordFragment mModifyRecordFragment; //修改记录界面
	
	private FragmentManager mFragmentManager; //Fragment管理器，负责Fragment的添加移除等工作
	
	private ConsumeItemService mConsumeItemService; //ConsumeItem业务逻辑对象
	
	public MainActivity() {
		mItemManagerFragment = new ItemManagerFragment();
		mConsumeFragment = new ConsumeFragment();
		mAnalyseFragment = new AnalyseFragment();
		mAboutFragment = new AboutFragment();
		mAddItemFragment = new AddItemFragment();
		mItemCompareFragment = new ItemCompareFragment();
		mMonthCompareFragment = new MonthCompareFragment();
		mModifyRecordFragment = new ModifyRecordFragment();
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		
		//实例化NavigationDrawer对象
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		//获得ActionBar的标题
		mTitle = getTitle();

		// Set up the drawer.
		//设置抽屉
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		//标记当前显示的是哪个Fragment，初始为消费记录显示列表Fragment
		mCurrentFragment = FragmentIdConsts.FRAGMENT_CONSUME;
		
		//实例化ConsumeItem业务逻辑对象
		mConsumeItemService = new ConsumeItemService(this);
		//初始化ConsumeItem的值
		mConsumeItemService.initConsumeItem();
		
			
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
//		menu.setHeaderTitle("操作");
//		this.getMenuInflater().inflate(R.menu.context_menu_consume_list, menu);
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		mFragmentManager = getSupportFragmentManager();
		switch (position) {
		case 0:
			clearBackStack();
			
			mFragmentManager
			.beginTransaction()
			.replace(R.id.container,
					mConsumeFragment).commit();
			break;
		case 1:
			clearBackStack();
			
			mFragmentManager
			.beginTransaction()
			.replace(R.id.container,
					mAnalyseFragment).commit();
			break;
		case 2:
			clearBackStack();
			
			mFragmentManager
			.beginTransaction()
			.replace(R.id.container,
					mAboutFragment).commit();
			break;

		default:
			break;
		}
		
	}

//	/**
//	 * Fragment被Attach到Activity的时候被调用
//	 * @param number 被Attach的Fragment ID
//	 */
//	public void onSectionAttached(int number) {
//		mCurrentSection = number;
//		switch (number) {
//		case 1:
//			mTitle = getString(R.string.title_consume);
//			break;
//		case 2:
//			mTitle = getString(R.string.title_item_manager);
//			break;
//		case 3:
//			mTitle = getString(R.string.title_analyse);
//			break;
//		case 4:
//			mTitle = getString(R.string.title_about);
//			break;
//		case 5:
//			mTitle = getString(R.string.title_add_record);
//			break;
//		case 6:
//			mTitle = getString(R.string.title_view_record);
//			break;
//		}
//	}
	
	/**
	 * 请求为指定的Fragment刷新 Menu
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
	 * 更新 ActionBar状态
	 */
	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.bg_button_press));
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
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
			new AlertDialog.Builder(this).setView(view).setNegativeButton("取消", null).setPositiveButton("创建", null).create().show();
			break;
		case R.id.action_consume_add:
			mAddRecordFragment = new AddRecordFragment();
			mFragmentManager
			.beginTransaction()
			.replace(R.id.container,
					mAddRecordFragment).addToBackStack(null).commit();
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
				new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_info).setTitle("退出")
				.setMessage("确定退出咕噜账本？").setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).setPositiveButton("确定", new OnClickListener() {
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
	 * 清空回退栈中的Fragment，在切换抽屉导航栏的时候调用，以免下次打开出错
	 */
	private void clearBackStack() {
		// 添加消费记录的Fragment
		if (mAddRecordFragment != null) {
			mFragmentManager.beginTransaction().remove(mAddRecordFragment)
					.commit();

			int backStackCount = mFragmentManager.getBackStackEntryCount();
			for (int i = 0; i < backStackCount; i++) {
				mFragmentManager.popBackStack();
			}
			mAddRecordFragment = null;
		}
		// 查看消费记录的Fragment
		if (mRecordDetailViewFragment != null) {
			mFragmentManager.beginTransaction()
					.remove(mRecordDetailViewFragment).commit();
			int backStackCount1 = mFragmentManager.getBackStackEntryCount();
			for (int i = 0; i < backStackCount1; i++) {
				mFragmentManager.popBackStack();
			}
			mRecordDetailViewFragment = null;
		}
		
		// 按类型统计的Fragment
		mFragmentManager.beginTransaction().remove(mItemCompareFragment)
				.commit();
		int backStackCount2 = mFragmentManager.getBackStackEntryCount();
		for (int i = 0; i < backStackCount2; i++) {
			mFragmentManager.popBackStack();
		}
		// 按月份统计的Fragment
		mFragmentManager.beginTransaction().remove(mMonthCompareFragment)
				.commit();
		int backStackCount3 = mFragmentManager.getBackStackEntryCount();
		for (int i = 0; i < backStackCount3; i++) {
			mFragmentManager.popBackStack();
		}

	}
	
	/**
	 * --------------------------------------------各Fragment的事件回调方法-----------------------------------------------------
	 */

	/**
	 * 新建消费记录取消时被调用
	 */
	@Override
	public void onRecordCanceled() {
		mFragmentManager
		.beginTransaction()
		.remove(mAddRecordFragment).commit();
		
		int backStackCount = mFragmentManager.getBackStackEntryCount();
		for(int i = 0; i < backStackCount; i++) {   
			mFragmentManager.popBackStack();
		}
		
		mAddRecordFragment = null;
	}

	/**
	 * 新的消费记录被创建的时候被调用
	 */
	@Override
	public void onRecordCreated(ConsumeRecord record) {
		mConsumeFragment.setmNeedRefreshFlag(true);
		mFragmentManager
		.beginTransaction()
		.remove(mAddRecordFragment).commit();
		
		int backStackCount = mFragmentManager.getBackStackEntryCount();
		for(int i = 0; i < backStackCount; i++) {   
			mFragmentManager.popBackStack();
		}
		mAddRecordFragment = null;
		
	}

	/**
	 * 消费记录被点击的时候被调用
	 */
	@Override
	public void onConsumeItemClick(ConsumeRecord record) {
		mRecordDetailViewFragment = new RecordDetailViewFragment(record);
		mFragmentManager
		.beginTransaction()
		.replace(R.id.container,
				mRecordDetailViewFragment).addToBackStack(null).commit();
		
	}

	/**
	 * 消费详细信息界面返回时被调用
	 */
	@Override
	public void onViewRecordReturn() {
		mFragmentManager
		.beginTransaction()
		.remove(mRecordDetailViewFragment).commit();
		
		int backStackCount = mFragmentManager.getBackStackEntryCount();
		for(int i = 0; i < backStackCount; i++) {   
			mFragmentManager.popBackStack();
		}
		
		mRecordDetailViewFragment = null;
		
	}

	/**
	 * 点击按消费类型查看图表
	 */
	@Override
	public void onByItemClick() {
		mFragmentManager
		.beginTransaction()
//		.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
		.replace(R.id.container,
				mItemCompareFragment).addToBackStack(null).commit();
		
	}

	/**
	 * 点击按月份查看图表
	 */
	@Override
	public void onByMonthClick() {
		mFragmentManager
		.beginTransaction()
//		.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
		.replace(R.id.container,
				mMonthCompareFragment).addToBackStack(null).commit();
	}

	/**
	 * 按月份查看界面返回
	 */
	@Override
	public void onMonthCompareReturn() {
		mFragmentManager
		.beginTransaction()
		.remove(mMonthCompareFragment).commit();
		
		int backStackCount = mFragmentManager.getBackStackEntryCount();
		for(int i = 0; i < backStackCount; i++) {   
			mFragmentManager.popBackStack();
		}
		
	}

	/**
	 * 按分类查看界面返回
	 */
	@Override
	public void onItemCompareReturn() {
		mFragmentManager
		.beginTransaction()
		.remove(mItemCompareFragment).commit();
		
		int backStackCount = mFragmentManager.getBackStackEntryCount();
		for(int i = 0; i < backStackCount; i++) {   
			mFragmentManager.popBackStack();
		}
	}

	/**
	 * 请求修改一个消费记录数据
	 */

	@Override
	public void onModifyCanceled() {
		mFragmentManager
		.beginTransaction()
		.remove(mModifyRecordFragment).commit();
		
		int backStackCount = mFragmentManager.getBackStackEntryCount();
		for(int i = 0; i < backStackCount; i++) {   
			mFragmentManager.popBackStack();
		}
	}

	@Override
	public void onModifySubmit(ConsumeRecord record) {
		mConsumeFragment.setmNeedRefreshFlag(true);
		mFragmentManager
		.beginTransaction()
		.remove(mModifyRecordFragment).commit();
		
		int backStackCount = mFragmentManager.getBackStackEntryCount();
		for(int i = 0; i < backStackCount; i++) {   
			mFragmentManager.popBackStack();
		}
	}

	@Override
	public void onConsumeItemModify(ConsumeRecord record) {
		mModifyRecordFragment.init(record);
		mFragmentManager
		.beginTransaction()
		.replace(R.id.container,
				mModifyRecordFragment).addToBackStack(null).commit();
		
	}

	/**
	 * 修改消费记录，提交后被调用
	 */
	@Override
	public void onViewRecordModify() {
		mConsumeFragment.setmNeedRefreshFlag(true);
		mFragmentManager
		.beginTransaction()
		.remove(mRecordDetailViewFragment).commit();
		
		int backStackCount = mFragmentManager.getBackStackEntryCount();
		for(int i = 0; i < backStackCount; i++) {   
			mFragmentManager.popBackStack();
		}
		mRecordDetailViewFragment = null;
	}

}
