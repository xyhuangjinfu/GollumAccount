package cn.hjf.gollumaccount.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.adapter.MonthStatisticAdapter;
import cn.hjf.gollumaccount.asynctask.StatisticMonthByTypeTask;
import cn.hjf.gollumaccount.businessmodel.ConsumeType;
import cn.hjf.gollumaccount.businessmodel.MonthStatisticData;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment.HEAD_TYPE;
import cn.hjf.gollumaccount.util.NumberUtil;
import cn.hjf.gollumaccount.util.TimeUtil;
import cn.hjf.gollumaccount.view.LoadingDialog;
import cn.hjf.gollumaccount.view.SpinnerDialog;
import cn.hjf.gollumaccount.view.SpinnerDialog.ICallback;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 查看某一年，某一个类型，按消费月份进行统计的界面
 * 
 * @author huangjinfu
 * 
 */
public class MonthStatisticActivity extends BaseActivity implements
        StatisticMonthByTypeTask.OnStatisticMonthByTypeListener,
        CommonHeaderFragment.ICallback {
    
    private static final int REQ_CODE_SELECT_TYPE = 0;
    
    private CommonHeaderFragment mTitleFragment; //顶部标题栏
    private LoadingDialog mLoadingDialog; //加载对话框

    private TextView mYearTextView; // 选择年份
    private TextView mTypeTextView; // 选择类型
    private TextView mSumTextView; // 总金额
    private LinearLayout mSumLayout; //金额总计布局
    private ListView mShowDataListView; //统计分析数据显示的列表
    private View mEmptyView; //ListView没有数据时显示的界面
    private SpinnerDialog mYearSelectDialog; //年份选择对话框
    
    private Calendar mStatisticYear; //统计的年份
    private ConsumeType mConsumeType; //统计的消费类型
    private MonthStatisticAdapter mMonthStatisticAdapter; //统计分析数据显示列表的适配器
    private List<MonthStatisticData> mStatisticDatas; ////按类型统计分析的数据
    private ArrayList<String> mYearData; // 年份数据
    
    
    public MonthStatisticActivity() {
        mStatisticYear = Calendar.getInstance();
        mConsumeType = new ConsumeType();
        mConsumeType.setId(0);
        mConsumeType.setName("汇总");
        mConsumeType.setType(ConsumeType.Type.CUSTOME);
        mStatisticDatas = new ArrayList<MonthStatisticData>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_statistic);
        
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
        mSumLayout = (LinearLayout) findViewById(R.id.ll_sum);
        mSumLayout.setVisibility(View.GONE);
        mSumTextView = (TextView) findViewById(R.id.tv_sum);
        mShowDataListView = (ListView) findViewById(R.id.lv_month_statistic);
        mYearTextView = (TextView) findViewById(R.id.tv_line_year);
        mTypeTextView = (TextView) findViewById(R.id.tv_line_type);
        mYearSelectDialog = new SpinnerDialog(this, R.style.transparent_dialog1);
        mLoadingDialog = new LoadingDialog(this, R.style.translucent_dialog);
        mLoadingDialog.setCancelable(false);
        
        //绑定空视图
        mEmptyView = findViewById(R.id.ly_no_data);
        mShowDataListView.setEmptyView(mEmptyView);
    }

    /**
     * 初始化各控件的值
     */
    @Override
    protected void initValue() {
        initYearData();
        
        mYearSelectDialog.setData(mYearData);
        
        mYearTextView.setText(mYearData.get(0));
        
        mTypeTextView.setText(mConsumeType.getName());
        
        mMonthStatisticAdapter = new MonthStatisticAdapter(this, mStatisticDatas);
        mShowDataListView.setAdapter(mMonthStatisticAdapter);
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
     * 得到列表显示的数据
     * @param result
     * @return
     */
    private List<MonthStatisticData> getShowData(Map<Integer, Double> result) {
        List<MonthStatisticData> datas = new ArrayList<MonthStatisticData>();
        double allSum = getAllSum(result);
        if (allSum != 0) {
            for (Map.Entry<Integer, Double> entry : result.entrySet()) {
                MonthStatisticData data = new MonthStatisticData();
                data.setConsumeMonth(entry.getKey());
                data.setTypeSum(entry.getValue());
                data.setAllSum(allSum);
                datas.add(data);
            }
        }
        return datas;
    }
    
    /**
     * 得到所有数据金额总和
     * @param result
     * @return
     */
    private double getAllSum(Map<Integer, Double> result) {
        double sum = 0d;
        for (Map.Entry<Integer, Double> entry : result.entrySet()) {
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
    public void onStatisticMonthByTypeCompleted(Map<Integer, Double> result) {
        mStatisticDatas.clear();
        List<MonthStatisticData> showData = getShowData(result);
        mStatisticDatas.addAll(showData);
        mMonthStatisticAdapter.notifyDataSetChanged();
        
        if (showData.size() != 0) {
            mSumLayout.setVisibility(View.VISIBLE);
            mSumTextView.setText(NumberUtil.formatValue(showData.get(0).getAllSum()));
        } else {
            mSumLayout.setVisibility(View.GONE);
        }
        
        mLoadingDialog.cancel();
    }

}
