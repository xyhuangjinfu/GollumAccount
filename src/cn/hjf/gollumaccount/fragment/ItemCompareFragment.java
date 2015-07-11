package cn.hjf.gollumaccount.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import cn.hjf.gollumaccount.FragmentIdConsts;
import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.activity.MainActivity;
import cn.hjf.gollumaccount.asynctask.ItemCompareTask;
import cn.hjf.gollumaccount.business.ConsumeItemService;
import cn.hjf.gollumaccount.business.ConsumeRecordService;
import cn.hjf.gollumaccount.dialog.LoadDialog;
import cn.hjf.gollumaccount.util.TimeUtil;
import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * 按消费类别查看的饼图界面
 * @author huangjinfu
 *
 */
public class ItemCompareFragment extends Fragment 
							implements ItemCompareTask.OnItemCompareSuccessCallback,
							OnChartValueSelectedListener {
	
	private Spinner mYearSpinner; //选择年份
	private Spinner mMonthSpinner; //选择月份
	private PieChart mPieChart; //饼图
	private Button mReturnButton; //返回按钮
	
	private ArrayList<String> mYearData ; //年份数据
	private ArrayList<String> mMonthData ; //月份数据
	
	private OnItemCompareCallback mListener; // 与Activity进行事件交互的对象
	private Context mContext; // 上下文对象
//	private ConsumeRecordService mConsumeRecordService; //ConsumeRecord业务逻辑对象
	private ConsumeItemService mConsumeItemService; //ConsumeItem业务逻辑对象
	
	private int mAnalyseYear = 0; //要分析的年份
	private int mAnalyseMonth = 0; //要分析的月份
	
	private float mSumPrice = 0; //各分类的总额
	
	
	public interface OnItemCompareCallback {
		public abstract void onItemCompareReturn();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_item_compare, null);
		
		initView(view);
		
		initSpinnerData();
		
		mYearSpinner.setAdapter(new ArrayAdapter<>(mContext, R.layout.item_spinner, mYearData));
		mMonthSpinner.setAdapter(new ArrayAdapter<>(mContext, R.layout.item_spinner, mMonthData));
		mMonthSpinner.setSelection(TimeUtil.getNowMonth(), true);
		
		mYearSpinner.setOnItemSelectedListener(mYearSelectedListener);
		mMonthSpinner.setOnItemSelectedListener(mMonthSelectedListener);
		mReturnButton.setOnClickListener(mReturnListener);
		
//		mConsumeRecordService = new ConsumeRecordService(mContext);
		mConsumeItemService = new ConsumeItemService(mContext);
		
		Calendar calendar = Calendar.getInstance();
		mAnalyseYear = calendar.get(Calendar.YEAR);
		mAnalyseMonth = calendar.get(Calendar.MONTH) + 1;
		
		return view;
	}
	
	OnClickListener mReturnListener = new OnClickListener() {	
		@Override
		public void onClick(View v) {
			mListener.onItemCompareReturn();
		}
	};
	
	OnItemSelectedListener mYearSelectedListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			LoadDialog.show(mContext);
			mAnalyseYear = Calendar.getInstance().get(Calendar.YEAR) - position;
			Long[] time = getAnalyseTime(mAnalyseYear, mAnalyseMonth);
			new ItemCompareTask(mContext, ItemCompareFragment.this).execute(time);
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {	
		}
	};
	
	OnItemSelectedListener mMonthSelectedListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			LoadDialog.show(mContext);
			mAnalyseMonth = 1 + position;
			Long[] time = getAnalyseTime(mAnalyseYear, mAnalyseMonth);
			new ItemCompareTask(mContext, ItemCompareFragment.this).execute(time);
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {	
		}
	};
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mListener = (OnItemCompareCallback) activity; // 设置回调监听对象
		mContext = activity; // 初始化上下文对象
		((MainActivity) activity).refreshMenuForFragment(FragmentIdConsts.FRAGMENT_ITEM_COMPARE); // 请求刷新ActionBar
	};
	
	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null; // 释放回调监听器
		((MainActivity) mContext).refreshMenuForFragment(FragmentIdConsts.FRAGMENT_ANALYSE); // 请求刷新ActionBar
	}
	
	/**
	 * 初始化View控件
	 * @param view
	 */
	private void initView(View view) {
		mYearSpinner = (Spinner) view.findViewById(R.id.spn_pie_year);
		mMonthSpinner = (Spinner) view.findViewById(R.id.spn_pie_month);
		mPieChart = (PieChart) view.findViewById(R.id.pc_by_month);
		mReturnButton = (Button) view.findViewById(R.id.btn_item_compare_return);
	}
	
	/**
	 * 初始化下拉列表的数据
	 */
	private void initSpinnerData() {
		mYearData = new ArrayList<String>();
		mMonthData = new ArrayList<String>();
		for (int i = TimeUtil.getNowYear(); i >= 1980 ; i--) {
			mYearData.add(String.valueOf(i));
		}
		for (int j = 1; j <= 12; j++) {
			mMonthData.add(String.valueOf(j));
		}
	}
	
	/**
	 * 传入数据，显示饼图
	 * @param map
	 */
	private void showPieChart(PieChart chart, PieData data) {
		//对饼图进行一些设置
		chart.setDescription(null);
		chart.setNoDataText("您本月没有消费数据！");
		chart.setOnChartValueSelectedListener(this);
		if (data == null) {
			Log.i("hjf", "没有数据");
			//清除数据
			chart.clear();
		} else {
			Log.i("hjf", "有数据");
			//绑定数据
			chart.setData(data);
//			chart.setCenterText("消费总额：" + chart.getValueCount() * chart.getAverage());
			chart.setCenterText("总 额：" + mSumPrice);
			chart.setCenterTextSize(15f);
		}
		//绘制饼图
//		chart.invalidate();
		chart.setOnTouchListener(null);//解决从有数据到无数据的状态下，点击图标会报NullPointerException的问题。
		chart.animateXY(1000, 1000);
		chart.invalidate();
	}
	
	/**
	 * 根据年份和月份生成分析时间，也就是月初和月末
	 * @param year
	 * @param month
	 * @return
	 */
	private Long[] getAnalyseTime(int year, int month) {
		Long[] time = new Long[2];
		
		Calendar cs = Calendar.getInstance();
		cs.set(year, month - 1, TimeUtil.getFirstDay(year, month), 0, 0, 0);
		time[0] = cs.getTime().getTime();
		
		Calendar ce = Calendar.getInstance();
		ce.set(year, month - 1, TimeUtil.getLastDay(year, month), 23, 59, 59);
		time[1] = ce.getTime().getTime();
		
		return time;
	}

	@Override
	public void onItemCompareSuccess(HashMap<Integer, Double> result) {
		LoadDialog.close();
		mSumPrice = getSumPrice(result);
		PieData data = getData(result);
		showPieChart(mPieChart, data);
		
	}
	
	/**
	 * 计算总额，用来显示
	 * @param result
	 * @return
	 */
	private float getSumPrice(HashMap<Integer, Double> result) {
		float sum = 0;
		if (isHaveData(result)) {
			for (int i = 0; i <= mConsumeItemService.findItemAll().size(); i++) {
				if ((result.get(i) != null) && (result.get(i) != 0)) {
					float price = (float)((double)result.get(i));
					Log.i("hjf", "price:" + price);
					sum = sum + price;
				} 
			}
		}
		return sum;
	}

	/**
	 * 验证查询的结果是否没有数据的结果，如果所有的值都是0，则就是没有数据
	 * 
	 * @param map
	 * @return true表示有消费数据
	 */
	private boolean isHaveData(HashMap<Integer, Double> map) {
		Log.i("hjf", "isHaveData");
		boolean result = false;
		for (int i = 0; i < mConsumeItemService.findItemAll().size(); i++) {
			if ((map.get(i) != null) && (map.get(i) != 0)) {
				Log.i("hjf", i+"---"+String.valueOf(map.get(i)));
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * 根据数据库查询出来的Map对象，生成PieData对象
	 * @param map
	 * @return
	 */
	private PieData getData(HashMap<Integer, Double> map) {
		PieData data = null;
		if (isHaveData(map)) {
			// 初始化 x、y轴的值
			ArrayList<String> xVals = new ArrayList<String>();
			ArrayList<Entry> yVals = new ArrayList<Entry>();
			for (int i = 0; i <= mConsumeItemService.findItemAll().size(); i++) {
				if ((map.get(i) != null) && (map.get(i) != 0)) {
					xVals.add(mConsumeItemService.findItemById(i).getItemName());
					float price = (float)((double)map.get(i));
					yVals.add(new Entry(price, i));
				} 
			}
			PieDataSet dataSet = new PieDataSet(yVals, null);
			// 指定每个值得颜色
			ArrayList<Integer> colors = new ArrayList<Integer>();
			for (int c : ColorTemplate.COLORFUL_COLORS)
				colors.add(c);
			for (int c : ColorTemplate.PASTEL_COLORS)
				colors.add(c);
			for (int c : ColorTemplate.VORDIPLOM_COLORS)
				colors.add(c);
			colors.add(ColorTemplate.getHoloBlue());
			dataSet.setColors(colors);
			// 创建PieData对象，绑定到饼图
			data = new PieData(xVals, dataSet);
		}
		return data;
	}

	@Override
	public void onValueSelected(Entry e, int dataSetIndex) {
		Log.i("hjf", "onValueSelected");
		
	}

	@Override
	public void onNothingSelected() {
		Log.i("hjf", "onNothingSelected");
		
	}
	

}
