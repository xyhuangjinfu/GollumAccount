package cn.hjf.gollumaccount.activity;

import java.util.Calendar;

import cn.hjf.gollumaccount.FragmentIdConsts;
import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.business.ConsumeItemService;
import cn.hjf.gollumaccount.fragment.AboutFragment;
import cn.hjf.gollumaccount.fragment.AddItemFragment;
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
import android.content.Intent;
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
	private NavigationDrawerFragment mNavigationDrawerFragment; //����Fragment

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle; //ActionBar�ı���

	private int mCurrentFragment; // ��ǰ����ֵ�Fragment
	private ItemManagerFragment mItemManagerFragment; //��Ŀ����Fragment
	private ConsumeFragment mConsumeFragment; //��Ѽ�¼Fragment
	private AnalyseFragment mAnalyseFragment; //���ͳ��Fragment
	private AboutFragment mAboutFragment; //������ϢFragment
	private AddItemFragment mAddItemFragment; //�����ĿFragment
	private RecordDetailViewFragment mRecordDetailViewFragment; //�����ϸ��Ϣ�鿴Fragment
	private ItemCompareFragment mItemCompareFragment; //�����Ͳ鿴ͼ��
	private MonthCompareFragment mMonthCompareFragment; //���·ݲ鿴ͼ��
	private ModifyRecordFragment mModifyRecordFragment; //�޸ļ�¼����
	
	private FragmentManager mFragmentManager; //Fragment������������Fragment������Ƴ�ȹ���
	
	private ConsumeItemService mConsumeItemService; //ConsumeItemҵ���߼�����
	
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
//	 * Fragment��Attach��Activity��ʱ�򱻵���
//	 * @param number ��Attach��Fragment ID
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
	 * ��ջ���ջ�е�Fragment�����л����뵼������ʱ����ã������´δ򿪳���
	 */
	private void clearBackStack() {
//		// �����Ѽ�¼��Fragment
//		if (mAddRecordFragment != null) {
//			mFragmentManager.beginTransaction().remove(mAddRecordFragment)
//					.commit();
//
//			int backStackCount = mFragmentManager.getBackStackEntryCount();
//			for (int i = 0; i < backStackCount; i++) {
//				mFragmentManager.popBackStack();
//			}
//			mAddRecordFragment = null;
//		}
		// �鿴��Ѽ�¼��Fragment
		if (mRecordDetailViewFragment != null) {
			mFragmentManager.beginTransaction()
					.remove(mRecordDetailViewFragment).commit();
			int backStackCount1 = mFragmentManager.getBackStackEntryCount();
			for (int i = 0; i < backStackCount1; i++) {
				mFragmentManager.popBackStack();
			}
			mRecordDetailViewFragment = null;
		}
		
		// ������ͳ�Ƶ�Fragment
		mFragmentManager.beginTransaction().remove(mItemCompareFragment)
				.commit();
		int backStackCount2 = mFragmentManager.getBackStackEntryCount();
		for (int i = 0; i < backStackCount2; i++) {
			mFragmentManager.popBackStack();
		}
		// ���·�ͳ�Ƶ�Fragment
		mFragmentManager.beginTransaction().remove(mMonthCompareFragment)
				.commit();
		int backStackCount3 = mFragmentManager.getBackStackEntryCount();
		for (int i = 0; i < backStackCount3; i++) {
			mFragmentManager.popBackStack();
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
		mRecordDetailViewFragment = new RecordDetailViewFragment(record);
		mFragmentManager
		.beginTransaction()
		.replace(R.id.container,
				mRecordDetailViewFragment).addToBackStack(null).commit();
		
	}

	/**
	 * �����ϸ��Ϣ���淵��ʱ������
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
	 * �����������Ͳ鿴ͼ��
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
	 * ������·ݲ鿴ͼ��
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
	 * ���·ݲ鿴���淵��
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
	 * ������鿴���淵��
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
	 * �����޸�һ����Ѽ�¼���
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
	 * �޸���Ѽ�¼���ύ�󱻵���
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
