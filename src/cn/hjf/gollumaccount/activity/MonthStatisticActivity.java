package cn.hjf.gollumaccount.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.asynctask.StatisticMonthByTypeTask;
import cn.hjf.gollumaccount.businessmodel.ConsumeType;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment.HEAD_TYPE;
import cn.hjf.gollumaccount.util.TimeUtil;
import cn.hjf.gollumaccount.view.LoadingDialog;
import cn.hjf.gollumaccount.view.SpinnerDialog;
import cn.hjf.gollumaccount.view.SpinnerDialog.ICallback;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * 按消费月份进行统计的界面
 * 
 * @author huangjinfu
 * 
 */
public class MonthStatisticActivity extends BaseActivity implements
        StatisticMonthByTypeTask.OnStatisticMonthByTypeListener,
        CommonHeaderFragment.ICallback {
    
    private static final int REQ_CODE_SELECT_TYPE = 0;

    private TextView mYearTextView; // 选择年份
    private TextView mTypeTextView; // 选择类型
    private LineChart mLineChart; // 线图
    private TextView mSumTextView; // 总金额

    private ArrayList<String> mYearData; // 年份数据
    private Typeface mTypeface; // 图标显示的样式

    private float mSumPrice = 0; // 各分类的总额
    /**
     * 顶部标题栏
     */
    private CommonHeaderFragment mTitleFragment;
    private SpinnerDialog mYearSelectDialog;
    private LoadingDialog mLoadingDialog;
    
    private Calendar mStatisticYear;
    private ConsumeType mConsumeType;
    
    public MonthStatisticActivity() {
        mStatisticYear = Calendar.getInstance();
        mConsumeType = new ConsumeType();
        mConsumeType.setId(0);
        mConsumeType.setName("汇总");
        mConsumeType.setType(ConsumeType.Type.CUSTOME);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_month_compare);
        
        initTitle();
        initView();
        initValue();
        initEvent();
        
        mLoadingDialog.show();
        new StatisticMonthByTypeTask(this, this).execute(mStatisticYear, mConsumeType);

    }
    
    /**
     * 初始化顶部导航栏
     */
    @Override
    public void initTitle() {
        mTitleFragment = (CommonHeaderFragment) mFragmentManager.findFragmentById(R.id.title_month_statistic);
        mTitleFragment.setHeadBtnType(HEAD_TYPE.LEFT_BACK_TEXT,HEAD_TYPE.RIGHT_NULL);
        mTitleFragment.setHeadText(R.string.title_back, R.string.title_analyse_month, null);
        mTitleFragment.setCallback(this);
    }

    /**
     * 初始化各控件
     */
    @Override
    protected void initView() {
        mYearTextView = (TextView) findViewById(R.id.tv_line_year);
        mTypeTextView = (TextView) findViewById(R.id.tv_line_type);
        mSumTextView = (TextView) findViewById(R.id.tv_sum);
        mLineChart = (LineChart) findViewById(R.id.lc_by_month);
        mYearSelectDialog = new SpinnerDialog(this, R.style.transparent_dialog1);
        mLoadingDialog = new LoadingDialog(this, R.style.translucent_dialog);
    }

    /**
     * 初始化各控件的值
     */
    @Override
    protected void initValue() {
        initYearData();
        
        mTypeface = Typeface.createFromAsset(this.getAssets(),
                "OpenSans-Bold.ttf");
        
        mYearSelectDialog.setData(mYearData);
        
        mYearTextView.setText(mYearData.get(0));
        
        mTypeTextView.setText(mConsumeType.getName());
    }

    /**
     * 初始化各控件的事件
     */
    @Override
    protected void initEvent() {
        mYearTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mYearSelectDialog.show();
            }
        });

        mTypeTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MonthStatisticActivity.this, TypeSelectActivity.class);
                intent.putExtra(TypeSelectActivity.PAGE_TYPE, TypeSelectActivity.PageType.STATISTIC);
                MonthStatisticActivity.this.startActivityForResult(intent, REQ_CODE_SELECT_TYPE);
            }
        });
        
        mYearSelectDialog.setCallback(new ICallback() {
            @Override
            public void onItemClick(int positon) {
                mYearSelectDialog.cancel();
                mYearTextView.setText(mYearData.get(positon));
                mStatisticYear.set(Calendar.YEAR, Integer.valueOf(mYearData.get(positon)));
                mLoadingDialog.show();
                new StatisticMonthByTypeTask(MonthStatisticActivity.this, MonthStatisticActivity.this).execute(mStatisticYear, mConsumeType);
            }
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQ_CODE_SELECT_TYPE) {
                mConsumeType = data.getParcelableExtra("consume_type");
                if (mConsumeType != null) {
                    mTypeTextView.setText(mConsumeType.getName());
                    mLoadingDialog.show();
                    new StatisticMonthByTypeTask(MonthStatisticActivity.this, MonthStatisticActivity.this).execute(mStatisticYear, mConsumeType);
                }
            }
        }
    }

    /**
     * 计算总额，用来显示
     * 
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
     * 初始化年份选择的数据
     */
    private void initYearData() {
        // 初始化年份下拉列表数据
        mYearData = new ArrayList<String>();
        for (int i = TimeUtil.getNowYear(); i >= 1980; i--) {
            mYearData.add(String.valueOf(i));
        }
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
        chart.setDrawYValues(true); // 在图表上显示具体值ֵ
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
            // chart.setBackgroundColor(Color.rgb(89, 199, 250));
            chart.setBackgroundColor(Color.rgb(109, 202, 236));
            chart.setData(data);
            // mSumTextView.setText("�� �" + chart.getAverage() *
            // chart.getValueCount());
            mSumTextView.setText("总 额：" + mSumPrice);
            mSumTextView.setVisibility(View.VISIBLE);
        } else {
            Log.i("hjf", "无数据");
            chart.setBackgroundColor(Color.WHITE);
            chart.clear();
            mSumTextView.setText("");
            mSumTextView.setVisibility(View.GONE);
        }

        // // add data
        // chart.setData(data);
        // // get the legend (only possible after setting data)
        // Legend l = chart.getLegend();
        // // modify the legend ...
        // // l.setPosition(LegendPosition.LEFT_OF_CHART);
        // l.setForm(LegendForm.CIRCLE);
        // l.setFormSize(6f);
        // l.setTextColor(Color.WHITE);
        // l.setTypeface(mTypeface);
        //
        // YLabels y = chart.getYLabels();
        // y.setTextColor(Color.WHITE);
        // y.setTypeface(mTypeface);
        // y.setLabelCount(4);
        //
        // XLabels x = chart.getXLabels();
        // x.setTextColor(Color.WHITE);
        // x.setTypeface(mTypeface);

        // animate calls invalidate()...
        chart.setOnTouchListener(null);
        chart.animateXY(1000, 1000);// ����Ч��
        chart.invalidate();
    }

    /**
     * 根据数据库查询出来的Map对象，生成LineData对象
     * 
     * @param map
     * @return
     */
    private LineData getData(Map<Integer, Double> map) {
        LineData data = null;
//        if (isHaveData(map)) {
            // 初始化 x、y轴的值ֵ
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
//        }
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

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {
    }

    @Override
    public void onStatisticMonthByTypeCompleted(Map<Integer, Double> result) {
        LineData lineData = getData(result);
        showLineChart(mLineChart, lineData);
        mLoadingDialog.cancel();
    }

}
