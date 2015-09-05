package cn.hjf.gollumaccount.activity;

import java.util.Calendar;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.businessmodel.ConsumeType;
import cn.hjf.gollumaccount.businessmodel.QueryInfo;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment.HEAD_TYPE;
import cn.hjf.gollumaccount.util.TimeUtil;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * 查询条件输入界面
 * @author xfujohn
 *
 */
public class QueryActivity extends BaseActivity implements CommonHeaderFragment.ICallback {
    
    private static final int REQ_CODE_SELECT_TYPE = 0;
    
    private CommonHeaderFragment mTitleFragment; //顶部标题栏
    
    private EditText mConsumeName; //消费记录名称
    private TextView mConsumeType; //消费记录类型
    private RelativeLayout mConsumeTypeLayout; //消费记录类型布局
    private TextView mStartTime; //开始时间
    private TextView mEndTime; //结束时间
    private Button mQueryButton; //查询按钮
    
    private DatePickerDialog mDatePickerDialog; // 消费日期选择对话框
    
    private QueryInfo mQueryInfo; //要返回的查询信息
    private Calendar mStartCalendar; //查询开始时间
    private Calendar mEndCalendar; //查询结束时间
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        
        Intent intent = getIntent();
        if (intent != null) {
            mQueryInfo = intent.getParcelableExtra("query_info");
            if (mQueryInfo == null) {
                finish();
                return;
            }
        }
        
        initTitle();
        initView();
        initValue();
        initEvent();
    }
    
    /**
     * 初始化顶部导航栏
     */
    @Override
    public void initTitle() {
        mTitleFragment = (CommonHeaderFragment) mFragmentManager.findFragmentById(R.id.title_query_record);
        mTitleFragment.setHeadBtnType(HEAD_TYPE.LEFT_BACK_TEXT,HEAD_TYPE.RIGHT_NULL);
        mTitleFragment.setHeadText(R.string.title_back, R.string.title_query_record, null);
        mTitleFragment.setCallback(this);
    }

    @Override
    protected void initView() {
        mConsumeName = (EditText) findViewById(R.id.et_record_name);
        mConsumeType = (TextView) findViewById(R.id.tv_record_type);
        mConsumeTypeLayout = (RelativeLayout) findViewById(R.id.rl_record_type);
        mStartTime = (TextView) findViewById(R.id.tv_record_date_start);
        mEndTime = (TextView) findViewById(R.id.tv_record_time_end);
        mQueryButton = (Button) findViewById(R.id.btn_query);
    }

    @Override
    protected void initValue() {
        mConsumeName.setText(mQueryInfo.getName());
        mConsumeType.setText(mQueryInfo.getType() == null ? null : mQueryInfo.getType().getName());
        mStartTime.setText(mQueryInfo.getStartTime() == null ? null : TimeUtil.getDateString(mQueryInfo.getStartTime()));
        mEndTime.setText(mQueryInfo.getEndTime() == null ? null : TimeUtil.getDateString(mQueryInfo.getEndTime()));
    }

    @Override
    protected void initEvent() {
        mQueryButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                buildQueryInfo();
                Intent intent = new Intent();
                intent.putExtra("query_info", mQueryInfo);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        mConsumeTypeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QueryActivity.this, TypeSelectActivity.class);
                QueryActivity.this.startActivityForResult(intent, REQ_CODE_SELECT_TYPE);
            }
        });
        
        mStartTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                mDatePickerDialog = new DatePickerDialog(QueryActivity.this,
                        new OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                    int monthOfYear, int dayOfMonth) {
                                mStartTime.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);
                                mStartCalendar.set(Calendar.YEAR, year);
                                mStartCalendar.set(Calendar.MONTH, monthOfYear);
                                mStartCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            }
                        }, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                mDatePickerDialog.show();
            }
        });
        mEndTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                mDatePickerDialog = new DatePickerDialog(QueryActivity.this,
                        new OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                    int monthOfYear, int dayOfMonth) {
                                mEndTime.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);
                                mEndCalendar.set(Calendar.YEAR, year);
                                mEndCalendar.set(Calendar.MONTH, monthOfYear);
                                mEndCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            }
                        }, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                mDatePickerDialog.show();
            }
        });
    }
    
    /**
     * 构建查询信息
     */
    private void buildQueryInfo() {
        mQueryInfo.setStartTime(mStartCalendar);
        mQueryInfo.setEndTime(mEndCalendar);
        mQueryInfo.setName(mConsumeName.getText().toString());
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQ_CODE_SELECT_TYPE) {
                ConsumeType type = data.getParcelableExtra("consume_type");
                if (type != null) {
                    mConsumeType.setText(type.getName());
                    mQueryInfo.setType(type);
                }
            }
        }
    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {
    }

}
