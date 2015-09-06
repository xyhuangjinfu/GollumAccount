package cn.hjf.gollumaccount.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.adapter.TypeStatisticAdapter;
import cn.hjf.gollumaccount.asynctask.TypeStatisticTask;
import cn.hjf.gollumaccount.businessmodel.ConsumeType;
import cn.hjf.gollumaccount.businessmodel.TypeStatisticData;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment.HEAD_TYPE;
import cn.hjf.gollumaccount.util.TimeUtil;
import cn.hjf.gollumaccount.view.LoadingDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 按消费类型进行统计的界面
 * 
 * @author huangjinfu
 * 
 */
public class TypeStatisticActivity extends BaseActivity implements
        TypeStatisticTask.OnTypeCompareListener,
        CommonHeaderFragment.ICallback {
    
    private CommonHeaderFragment mTitleFragment; //顶部标题栏
    private LoadingDialog mLoadingDialog; //加载对话框
    
    private TextView mStartDateTextView; // 统计开始时间
    private TextView mEndDateTextView; // 统计结束时间
    private PieChart mPieChart; // 饼图
    
    private Calendar mStartDate; //统计开始时间
    private Calendar mEndDate; //统计结束时间
    private DatePickerDialog mStartDatePickerDialog; // 统计开始日期选择对话框
    private DatePickerDialog mEndDatePickerDialog; // 统计结束日期选择对话框
    
    private Map<ConsumeType, Double> mStatisticData; //消费类型与金额总计映射的统计数据
    
    private ListView mShowDataListView; //统计分析数据显示的列表
    private TypeStatisticAdapter mTypeStatisticAdapter; //统计分析数据显示列表的适配器
    private List<TypeStatisticData> mStatisticDatas; ////按类型统计分析的数据
    
    private View mEmptyView; //ListView没有数据时显示的界面
    
    public TypeStatisticActivity() {
        mStartDate = Calendar.getInstance();
        mEndDate = Calendar.getInstance();
        mStatisticData = new HashMap<ConsumeType, Double>();
        mStatisticDatas = new ArrayList<TypeStatisticData>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_statistic);
        
        initTitle();
        initView();
        initValue();
        initEvent();
        
        mLoadingDialog.show();
        new TypeStatisticTask(this, this).execute(mStartDate, mEndDate);
    }
    
    /**
     * 初始化顶部导航栏
     */
    @Override
    public void initTitle() {
        mTitleFragment = (CommonHeaderFragment) mFragmentManager.findFragmentById(R.id.title_statistic_type);
        mTitleFragment.setHeadBtnType(HEAD_TYPE.LEFT_BACK_TEXT,HEAD_TYPE.RIGHT_NULL);
        mTitleFragment.setHeadText(R.string.title_back, R.string.title_analyse_item, null);
        mTitleFragment.setCallback(this);
    }
    
    @Override
    protected void initView() {
        mShowDataListView = (ListView) findViewById(R.id.lv_type_statistic);
        mPieChart = (PieChart) findViewById(R.id.pc_by_month);
        mStartDateTextView = (TextView) findViewById(R.id.tv_record_date_start);
        mEndDateTextView = (TextView) findViewById(R.id.tv_record_time_end);
        mLoadingDialog = new LoadingDialog(this, R.style.translucent_dialog);
        mLoadingDialog.setCancelable(false);
        
        //绑定空视图
        mEmptyView = findViewById(R.id.ly_no_data);
        mShowDataListView.setEmptyView(mEmptyView);
    }
    
    @Override
    protected void initValue() {
        mStartDateTextView.setText(TimeUtil.getDateString(mStartDate));
        mEndDateTextView.setText(TimeUtil.getDateString(mEndDate));
        mTypeStatisticAdapter = new TypeStatisticAdapter(this, mStatisticDatas);
        mShowDataListView.setAdapter(mTypeStatisticAdapter);
    }

    @Override
    protected void initEvent() {
        mStartDateTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartDatePickerDialog = new DatePickerDialog(TypeStatisticActivity.this,
                        new OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                    int monthOfYear, int dayOfMonth) {
                                mStartDate.set(Calendar.YEAR, year);
                                mStartDate.set(Calendar.MONTH, monthOfYear);
                                mStartDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                mStartDateTextView.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                mLoadingDialog.show();
                                new TypeStatisticTask(TypeStatisticActivity.this, TypeStatisticActivity.this).execute(mStartDate, mEndDate);
                            }
                        }, mStartDate.get(Calendar.YEAR),
                        mStartDate.get(Calendar.MONTH),
                        mStartDate.get(Calendar.DAY_OF_MONTH));
                mStartDatePickerDialog.show();
            }
        });
        mEndDateTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEndDatePickerDialog = new DatePickerDialog(TypeStatisticActivity.this,
                        new OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                    int monthOfYear, int dayOfMonth) {
                                mEndDate.set(Calendar.YEAR, year);
                                mEndDate.set(Calendar.MONTH, monthOfYear);
                                mEndDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                mEndDateTextView.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                mLoadingDialog.show();
                                new TypeStatisticTask(TypeStatisticActivity.this, TypeStatisticActivity.this).execute(mStartDate, mEndDate);
                            }
                        }, mEndDate.get(Calendar.YEAR),
                        mEndDate.get(Calendar.MONTH),
                        mEndDate.get(Calendar.DAY_OF_MONTH));
                mEndDatePickerDialog.show();
            }
        });
    }

    /**
     * 显示饼图
     * 
     * @param map
     */
    private void showPieChart(PieChart chart, PieData data) {
      //对饼图进行一些设置
        chart.setDescription(null);
        chart.setNoDataText("您本月没有消费数据！");
//        chart.setOnChartValueSelectedListener(this);
        if (data == null) {
            Log.i("hjf", "没有数据");
            chart.clear();
        } else {
            Log.i("hjf", "有数据");
            chart.setData(data);
            chart.setCenterTextSize(15f);
        }
      //绘制饼图
        // chart.invalidate();
        chart.setOnTouchListener(null);//解决从有数据到无数据的状态下，点击图标会报NullPointerException的问题。
        chart.animateXY(1000, 1000);
        chart.invalidate();
    }

    /**
     * 根据数据库查询出来的Map对象，生成PieData对象
     * 
     * @param map
     * @return
     */
    private PieData getData(Map<ConsumeType, Double> map) {
        PieData data = null;
//        if (isHaveData(map)) {
            // 初始化 x、y轴的
            ArrayList<String> xVals = new ArrayList<String>();
            ArrayList<Entry> yVals = new ArrayList<Entry>();
            
            for (Map.Entry<ConsumeType, Double> entry : map.entrySet()) {
                int i = 0;
                xVals.add(entry.getKey().getName());
                float price = (float) ((double) entry.getValue());
                yVals.add(new Entry(price, i++));
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
//        }
        return data;
    }
    
    /**
     * 得到列表显示的数据
     * @param result
     * @return
     */
    private List<TypeStatisticData> getShowData(Map<ConsumeType, Double> result) {
        List<TypeStatisticData> datas = new ArrayList<TypeStatisticData>();
        double allSum = getAllSum(result);
        for (Map.Entry<ConsumeType, Double> entry : result.entrySet()) {
            TypeStatisticData data = new TypeStatisticData();
            data.setConsumeType(entry.getKey());
            data.setTypeSum(entry.getValue());
            data.setAllSum(allSum);
            datas.add(data);
        }
        return datas;
    }
    
    /**
     * 得到所有数据金额总和
     * @param result
     * @return
     */
    private double getAllSum(Map<ConsumeType, Double> result) {
        double sum = 0d;
        for (Map.Entry<ConsumeType, Double> entry : result.entrySet()) {
            sum = sum + entry.getValue();
        }
        return sum;
    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {
    }

    @Override
    public void onItemCompareSuccess(Map<ConsumeType, Double> result) {
        PieData pieData = getData(result);
        showPieChart(mPieChart, pieData);
        
        mStatisticDatas.clear();
        mStatisticDatas.addAll(getShowData(result));
        mTypeStatisticAdapter.notifyDataSetChanged();
        
        mLoadingDialog.cancel();
    }

}
