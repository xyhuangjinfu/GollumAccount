package cn.hjf.gollumaccount.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.Legend.LegendForm;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;

import cn.hjf.gollumaccount.FragmentIdConsts;
import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.activity.MainActivity;
import cn.hjf.gollumaccount.asynctask.MonthCompareTask;
import cn.hjf.gollumaccount.business.ConsumeItemService;
import cn.hjf.gollumaccount.dialog.LoadDialog;
import cn.hjf.gollumaccount.util.TimeUtil;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.TextView;

/**
 * 按月份查看的线图界面
 * 
 * @author huangjinfu
 * 
 */
public class MonthCompareFragment extends Fragment implements
		MonthCompareTask.OnMonthCompareSuccessCallback {

	private Spinner mYearSpinner; // 选择年份
	private Spinner mItemSpinner; // 选择年份
	private LineChart mLineChart; // 线图
	private TextView mSumTextView; // 总金额
	private Button mReturnButton; // 返回按钮

	private OnMonthCompareCallback mListener; // 与Activity进行事件交互的对象
	private Context mContext; // 上下文对象
	private ConsumeItemService mConsumeItemService; //分类实体的业务逻辑对象

	private ArrayList<String> mYearData; // 年份数据
	private ArrayList<String> mItemData; // 分类数据
	private int mAnalyseYear = 0; // 要分析的年份
	private int mAnalyseItem = 0; // 要分析的分类
	private Typeface mTypeface; // 图标显示的样式
	
	private float mSumPrice = 0; //各分类的总额
	
	

	/**
	 * 与Activity进行事件交互的接口
	 * 
	 * @author huangjinfu
	 * 
	 */
	public interface OnMonthCompareCallback {
		public abstract void onMonthCompareReturn(); //点击返回按钮时被回调
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true); //通知系统，有菜单，需要系统更新Menu

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		//加载布局文件
		View view = inflater.inflate(R.layout.fragment_month_compare, null);
		//实例化业务逻辑对象
		mConsumeItemService = new ConsumeItemService(this.getActivity());
		//实例化View控件
		initView(view);
		//初始化Spinner的数据
		initSpinnerData();
		//初始化要分析的年份和分类
		Calendar calendar = Calendar.getInstance();
		mAnalyseYear = calendar.get(Calendar.YEAR);
		mAnalyseItem = mItemData.size();
		//设置图标显示字体
		mTypeface = Typeface.createFromAsset(mContext.getAssets(),
				"OpenSans-Bold.ttf");
		//Spinner设置Adapter
		mYearSpinner.setAdapter(new ArrayAdapter<>(mContext,
				R.layout.item_spinner, mYearData));
		mItemSpinner.setAdapter(new ArrayAdapter<>(mContext,
				R.layout.item_spinner, mItemData));
		//Spinner和Button设置事件监听
		mYearSpinner.setOnItemSelectedListener(mYearSelectedListener);
		mItemSpinner.setOnItemSelectedListener(mItemSelectedListener);
		mReturnButton.setOnClickListener(mReturnListener);
		//初始化Spinner选择位置，默认初始为选择“汇总”
		mItemSpinner.setSelection(mAnalyseItem - 1, true);
		//返回View，用来显示
		return view;
	}

	/**
	 * 返回按钮事件监听
	 */
	OnClickListener mReturnListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mListener.onMonthCompareReturn();
		}
	};

	/**
	 * 年份数据选择监听
	 */
	OnItemSelectedListener mYearSelectedListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			//显示加载对话框
			LoadDialog.show(mContext);
			//初始化年份和分类的值，用来加载数据
			mAnalyseYear = TimeUtil.getNowYear() - position;
			Integer[] paras = new Integer[2];
			paras[0] = mAnalyseYear;
			paras[1] = mAnalyseItem;
			//新建AsyncTask加载数据
			new MonthCompareTask(mContext, MonthCompareFragment.this)
					.execute(paras);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};
	
	/**
	 * 分类数据选择监听
	 */
	OnItemSelectedListener mItemSelectedListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			//显示加载对话框
			LoadDialog.show(mContext);
			//初始化年份和分类的值，用来加载数据
			mAnalyseItem = position + 1;
			Integer[] paras = new Integer[2];
			paras[0] = mAnalyseYear;
			paras[1] = mAnalyseItem;
			//新建AsyncTask加载数据
			new MonthCompareTask(mContext, MonthCompareFragment.this)
					.execute(paras);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mListener = (OnMonthCompareCallback) activity; // 设置回调监听对象
		mContext = activity; // 初始化上下文对象
		((MainActivity) activity)
				.refreshMenuForFragment(FragmentIdConsts.FRAGMENT_MONTH_COMPARE); // 请求刷新ActionBar
	};

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null; // 释放回调监听器
		((MainActivity) mContext)
				.refreshMenuForFragment(FragmentIdConsts.FRAGMENT_ANALYSE); // 请求刷新ActionBar
	}

	@Override
	public void onMonthCompareSuccess(HashMap<Integer, Double> result) {
		//关闭加载对话框
		LoadDialog.close();
		mSumPrice = getSumPrice(result);
		//根据查询结果，生成线图的数据集
		LineData data = getData(result);
		//传入要显示的图表控件和数据，显示图表
		showLineChart(mLineChart, data);

	}
	
	/**
	 * 计算总额，用来显示
	 * @param result
	 * @return
	 */
	private float getSumPrice(HashMap<Integer, Double> result) {
		float sum = 0;
		float price = 0;
		if (isHaveData(result)) {
			for (int j = 0; j < result.size(); j++) {
				if ((result.get(j) != null) && (result.get(j) != 0)) {
					price = (float) ((double) result.get(j));
				} else {
					price = 0;
				}
				Log.i("hjf", "price:" + price);
				sum = sum + price;
			}
		}
		return sum;
	}

	/**
	 * 初始化View控件
	 * 
	 * @param view
	 */
	private void initView(View view) {
		mYearSpinner = (Spinner) view.findViewById(R.id.spn_line_year);
		mItemSpinner = (Spinner) view.findViewById(R.id.spn_line_item);
		mSumTextView = (TextView) view.findViewById(R.id.tv_sum);
		mLineChart = (LineChart) view.findViewById(R.id.lc_by_month);
		mReturnButton = (Button) view
				.findViewById(R.id.btn_month_compare_return);
	}

	/**
	 * 初始化下拉列表的数据
	 */
	private void initSpinnerData() {
		//初始化年份下拉列表数据
		mYearData = new ArrayList<String>();
		for (int i = TimeUtil.getNowYear(); i >= 1980; i--) {
			mYearData.add(String.valueOf(i));
		}
		//初始化分类下拉列表数据
		mItemData = new ArrayList<String>();
		mItemData = mConsumeItemService.getAllItemName();
		mItemData.add("汇总");
	}

	/**
	 * 传入图表和数据，进行设置并显示
	 * 
	 * @param chart
	 * @param data
	 */
	private void showLineChart(LineChart chart, LineData data) {
		// if enabled, the chart will always start at zero on the y-axis
		chart.setStartAtZero(true);
		// disable the drawing of values into the chart
		// chart.setDrawYValues(false);
		chart.setDrawYValues(true); // 在图表上显示具体值
		chart.setValueTextColor(Color.WHITE);
		chart.setDrawBorder(false);
		// no description text
		chart.setDescription("");
		// chart.setNoDataTextDescription("You need to provide data for the chart.");
		chart.setNoDataText("您本年的此分类没有消费数据！");
		// enable / disable grid lines
		chart.setDrawVerticalGrid(false);
		// mChart.setDrawHorizontalGrid(false);
		//
		// enable / disable grid background
		chart.setDrawGridBackground(false);
		chart.setGridColor(Color.WHITE & 0x70FFFFFF);
		chart.setGridWidth(1.25f);
		// enable touch gestures
		chart.setTouchEnabled(true);
		// enable scaling and dragging
		chart.setDragEnabled(true);
		chart.setScaleEnabled(true);
		// if disabled, scaling can be done on x- and y-axis separately
		chart.setPinchZoom(false);
		chart.setValueTypeface(mTypeface);

		
		if (data != null) {
			Log.i("hjf", "有数据");
//			chart.setBackgroundColor(Color.rgb(89, 199, 250));
			chart.setBackgroundColor(Color.rgb(109, 202, 236));
			chart.setData(data);
//			mSumTextView.setText("总 额：" + chart.getAverage() * chart.getValueCount());
			mSumTextView.setText("总 额：" + mSumPrice);
			mSumTextView.setVisibility(View.VISIBLE);
		} else {
			Log.i("hjf", "无数据");
			chart.setBackgroundColor(Color.WHITE);
			chart.clear();
			mSumTextView.setText("");
			mSumTextView.setVisibility(View.GONE);
		}

//		 // add data
//		 chart.setData(data);
//		 // get the legend (only possible after setting data)
//		 Legend l = chart.getLegend();
//		 // modify the legend ...
//		 // l.setPosition(LegendPosition.LEFT_OF_CHART);
//		 l.setForm(LegendForm.CIRCLE);
//		 l.setFormSize(6f);
//		 l.setTextColor(Color.WHITE);
//		 l.setTypeface(mTypeface);
//		
//		 YLabels y = chart.getYLabels();
//		 y.setTextColor(Color.WHITE);
//		 y.setTypeface(mTypeface);
//		 y.setLabelCount(4);
//		
//		 XLabels x = chart.getXLabels();
//		 x.setTextColor(Color.WHITE);
//		 x.setTypeface(mTypeface);

		// animate calls invalidate()...
		chart.setOnTouchListener(null);
		chart.animateXY(1000, 1000);// 动画效果
		chart.invalidate();
	}

	/**
	 * 根据数据库查询出来的Map对象，生成LineData对象
	 * 
	 * @param map
	 * @return
	 */
	private LineData getData(HashMap<Integer, Double> map) {
		LineData data = null;
		if (isHaveData(map)) {
			// 初始化 x、y轴的值
			ArrayList<String> xVals = new ArrayList<String>();
			ArrayList<Entry> yVals = new ArrayList<Entry>();
			for (int j = 0; j < map.size(); j++) {
				xVals.add(String.valueOf(j + 1));
				if ((map.get(j) != null) && (map.get(j) != 0)) {
					float price = (float) ((double) map.get(j));
					yVals.add(new Entry(price, j));
				} else {
					yVals.add(new Entry(0, j));
				}
			}
			LineDataSet dataSet = new LineDataSet(yVals, null);
			dataSet.setLineWidth(1.75f);
			dataSet.setCircleSize(3f);
			dataSet.setColor(Color.WHITE);
			dataSet.setCircleColor(Color.WHITE);
			dataSet.setHighLightColor(Color.WHITE);

			ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
			dataSets.add(dataSet);
			data = new LineData(xVals, dataSets);
		}
		return data;
	}

	/**
	 * 验证查询的结果是否没有数据的结果，如果所有的值都是0，则就是没有数据
	 * 
	 * @param map
	 * @return true表示有消费数据
	 */
	private boolean isHaveData(HashMap<Integer, Double> map) {
		boolean result = false;
		for (int i = 0; i < map.size(); i++) {
			if (map.get(i) != 0) {
				result = true;
			}
		}
		return result;
	}

}
