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
 * ���·ݲ鿴����ͼ����
 * 
 * @author huangjinfu
 * 
 */
public class MonthCompareFragment extends Fragment implements
		MonthCompareTask.OnMonthCompareSuccessCallback {

	private Spinner mYearSpinner; // ѡ�����
	private Spinner mItemSpinner; // ѡ�����
	private LineChart mLineChart; // ��ͼ
	private TextView mSumTextView; // �ܽ��
	private Button mReturnButton; // ���ذ�ť

	private OnMonthCompareCallback mListener; // ��Activity�����¼������Ķ���
	private Context mContext; // �����Ķ���
	private ConsumeItemService mConsumeItemService; //����ʵ���ҵ���߼�����

	private ArrayList<String> mYearData; // �������
	private ArrayList<String> mItemData; // ��������
	private int mAnalyseYear = 0; // Ҫ���������
	private int mAnalyseItem = 0; // Ҫ�����ķ���
	private Typeface mTypeface; // ͼ����ʾ����ʽ
	
	private float mSumPrice = 0; //��������ܶ�
	
	

	/**
	 * ��Activity�����¼������Ľӿ�
	 * 
	 * @author huangjinfu
	 * 
	 */
	public interface OnMonthCompareCallback {
		public abstract void onMonthCompareReturn(); //������ذ�ťʱ���ص�
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true); //֪ͨϵͳ���в˵�����Ҫϵͳ����Menu

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		//���ز����ļ�
		View view = inflater.inflate(R.layout.fragment_month_compare, null);
		//ʵ����ҵ���߼�����
		mConsumeItemService = new ConsumeItemService(this.getActivity());
		//ʵ����View�ؼ�
		initView(view);
		//��ʼ��Spinner������
		initSpinnerData();
		//��ʼ��Ҫ��������ݺͷ���
		Calendar calendar = Calendar.getInstance();
		mAnalyseYear = calendar.get(Calendar.YEAR);
		mAnalyseItem = mItemData.size();
		//����ͼ����ʾ����
		mTypeface = Typeface.createFromAsset(mContext.getAssets(),
				"OpenSans-Bold.ttf");
		//Spinner����Adapter
		mYearSpinner.setAdapter(new ArrayAdapter<>(mContext,
				R.layout.item_spinner, mYearData));
		mItemSpinner.setAdapter(new ArrayAdapter<>(mContext,
				R.layout.item_spinner, mItemData));
		//Spinner��Button�����¼�����
		mYearSpinner.setOnItemSelectedListener(mYearSelectedListener);
		mItemSpinner.setOnItemSelectedListener(mItemSelectedListener);
		mReturnButton.setOnClickListener(mReturnListener);
		//��ʼ��Spinnerѡ��λ�ã�Ĭ�ϳ�ʼΪѡ�񡰻��ܡ�
		mItemSpinner.setSelection(mAnalyseItem - 1, true);
		//����View��������ʾ
		return view;
	}

	/**
	 * ���ذ�ť�¼�����
	 */
	OnClickListener mReturnListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mListener.onMonthCompareReturn();
		}
	};

	/**
	 * �������ѡ�����
	 */
	OnItemSelectedListener mYearSelectedListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			//��ʾ���ضԻ���
			LoadDialog.show(mContext);
			//��ʼ����ݺͷ����ֵ��������������
			mAnalyseYear = TimeUtil.getNowYear() - position;
			Integer[] paras = new Integer[2];
			paras[0] = mAnalyseYear;
			paras[1] = mAnalyseItem;
			//�½�AsyncTask��������
			new MonthCompareTask(mContext, MonthCompareFragment.this)
					.execute(paras);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};
	
	/**
	 * ��������ѡ�����
	 */
	OnItemSelectedListener mItemSelectedListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			//��ʾ���ضԻ���
			LoadDialog.show(mContext);
			//��ʼ����ݺͷ����ֵ��������������
			mAnalyseItem = position + 1;
			Integer[] paras = new Integer[2];
			paras[0] = mAnalyseYear;
			paras[1] = mAnalyseItem;
			//�½�AsyncTask��������
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
		mListener = (OnMonthCompareCallback) activity; // ���ûص���������
		mContext = activity; // ��ʼ�������Ķ���
		((MainActivity) activity)
				.refreshMenuForFragment(FragmentIdConsts.FRAGMENT_MONTH_COMPARE); // ����ˢ��ActionBar
	};

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null; // �ͷŻص�������
		((MainActivity) mContext)
				.refreshMenuForFragment(FragmentIdConsts.FRAGMENT_ANALYSE); // ����ˢ��ActionBar
	}

	@Override
	public void onMonthCompareSuccess(HashMap<Integer, Double> result) {
		//�رռ��ضԻ���
		LoadDialog.close();
		mSumPrice = getSumPrice(result);
		//���ݲ�ѯ�����������ͼ�����ݼ�
		LineData data = getData(result);
		//����Ҫ��ʾ��ͼ��ؼ������ݣ���ʾͼ��
		showLineChart(mLineChart, data);

	}
	
	/**
	 * �����ܶ������ʾ
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
	 * ��ʼ��View�ؼ�
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
	 * ��ʼ�������б������
	 */
	private void initSpinnerData() {
		//��ʼ����������б�����
		mYearData = new ArrayList<String>();
		for (int i = TimeUtil.getNowYear(); i >= 1980; i--) {
			mYearData.add(String.valueOf(i));
		}
		//��ʼ�����������б�����
		mItemData = new ArrayList<String>();
		mItemData = mConsumeItemService.getAllItemName();
		mItemData.add("����");
	}

	/**
	 * ����ͼ������ݣ��������ò���ʾ
	 * 
	 * @param chart
	 * @param data
	 */
	private void showLineChart(LineChart chart, LineData data) {
		// if enabled, the chart will always start at zero on the y-axis
		chart.setStartAtZero(true);
		// disable the drawing of values into the chart
		// chart.setDrawYValues(false);
		chart.setDrawYValues(true); // ��ͼ������ʾ����ֵ
		chart.setValueTextColor(Color.WHITE);
		chart.setDrawBorder(false);
		// no description text
		chart.setDescription("");
		// chart.setNoDataTextDescription("You need to provide data for the chart.");
		chart.setNoDataText("������Ĵ˷���û���������ݣ�");
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
			Log.i("hjf", "������");
//			chart.setBackgroundColor(Color.rgb(89, 199, 250));
			chart.setBackgroundColor(Color.rgb(109, 202, 236));
			chart.setData(data);
//			mSumTextView.setText("�� �" + chart.getAverage() * chart.getValueCount());
			mSumTextView.setText("�� �" + mSumPrice);
			mSumTextView.setVisibility(View.VISIBLE);
		} else {
			Log.i("hjf", "������");
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
		chart.animateXY(1000, 1000);// ����Ч��
		chart.invalidate();
	}

	/**
	 * �������ݿ��ѯ������Map��������LineData����
	 * 
	 * @param map
	 * @return
	 */
	private LineData getData(HashMap<Integer, Double> map) {
		LineData data = null;
		if (isHaveData(map)) {
			// ��ʼ�� x��y���ֵ
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
	 * ��֤��ѯ�Ľ���Ƿ�û�����ݵĽ����������е�ֵ����0�������û������
	 * 
	 * @param map
	 * @return true��ʾ����������
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
