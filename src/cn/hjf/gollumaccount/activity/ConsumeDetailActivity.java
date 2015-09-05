package cn.hjf.gollumaccount.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.businessmodel.ConsumeRecord;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment.HEAD_TYPE;
import cn.hjf.gollumaccount.util.TimeUtil;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * 消费记录详细信息界面/信息修改界面
 * 
 * @author huangjinfu
 * 
 */
public class ConsumeDetailActivity extends BaseActivity implements CommonHeaderFragment.ICallback {

    public static final String CONSUME_RECORD = "consume_record";

    private EditText mConsumeNameEditText; // 消费名称
    private EditText mConsumePriceEditText; // 消费金额
    private TextView mConsumeTypeTextView; // 消费类型
    private RelativeLayout mTypeLayout; // 消费类型点击布局
    private TextView mConsumeDateTextView; // 消费日期
    private TextView mConsumeTimeTextView; // 消费时间
    private TextView mConsumeCreateTimeTextView; // 消费记录创建时间
    private EditText mConsumeRemarksEditText; // 备注信息
    private Button mOperateButton; // 修改按钮
    
    private DatePickerDialog mDatePickerDialog; // 消费日期选择对话框
    private TimePickerDialog mTimePickerDialog; // 消费时间选择对话框
    
    private ConsumeRecord mConsumeRecord = null; // 消费记录对象
    private boolean mButtonFlag; // false-修改，true-提交
    /**
     * 顶部标题栏
     */
    private CommonHeaderFragment mTitleFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_record);
        
        if (getIntent().getParcelableExtra(CONSUME_RECORD) == null) {
            finish();
            return;
        } else {
            mConsumeRecord = (ConsumeRecord) getIntent().getParcelableExtra(CONSUME_RECORD);
        }
        
        initTitle();
        initView();
        initValue();
        initEvent();
        
        setViewUsable(false);
    }
    
    /**
     * 初始化顶部导航栏
     */
    @Override
    public void initTitle() {
        mTitleFragment = (CommonHeaderFragment) mFragmentManager.findFragmentById(R.id.title_view_record);
        mTitleFragment.setHeadBtnType(HEAD_TYPE.LEFT_BACK_TEXT,HEAD_TYPE.RIGHT_NULL);
        mTitleFragment.setHeadText(R.string.title_back, R.string.title_view_record, null);
        mTitleFragment.setCallback(this);
    }

    /**
     * 初始化各控件
     * 
     * @param view
     */
    @Override
    protected void initView() {
        mConsumeNameEditText = (EditText) findViewById(R.id.et_record_name_detail);
        mConsumePriceEditText = (EditText) findViewById(R.id.et_record_price_detail);
        mConsumeDateTextView = (TextView) findViewById(R.id.tv_record_date_detail);
        mConsumeTypeTextView = (TextView) findViewById(R.id.tv_record_type);
        mTypeLayout = (RelativeLayout) findViewById(R.id.rl_record_type);
        mConsumeTimeTextView = (TextView) findViewById(R.id.tv_record_time_detail);
        mConsumeCreateTimeTextView = (TextView) findViewById(R.id.tv_record_create_time_detail);
        mConsumeRemarksEditText = (EditText) findViewById(R.id.et_record_remarks_detail);
        mOperateButton = (Button) findViewById(R.id.btn_record_operate);
    }

    /**
     * 初始化控件的值
     */
    @Override
    protected void initValue() {
        mConsumeNameEditText.setText(this.mConsumeRecord.getRecordName());
        mConsumePriceEditText.setText(String.valueOf(this.mConsumeRecord
                .getRecordPrice()));
        
        mConsumeTypeTextView.setText(mConsumeRecord.getRecordType().getName());
        mConsumeDateTextView.setText(TimeUtil.getDateString(this.mConsumeRecord.getConsumeTime()));
        mConsumeTimeTextView.setText(TimeUtil.getTimeString(this.mConsumeRecord.getConsumeTime()));
        mConsumeCreateTimeTextView.setText(TimeUtil.getDateTimeString(this.mConsumeRecord.getCreateTime()));
        if (("".equals(this.mConsumeRecord.getRecordRemark()))
                || (this.mConsumeRecord.getRecordRemark() == null)) {
            mConsumeRemarksEditText.setHint("备注信息");
        } else {
            mConsumeRemarksEditText.setText(this.mConsumeRecord
                    .getRecordRemark());
        }

    }

    /**
     * 初始化各控件的事件
     */
    @Override
    protected void initEvent() {
        mOperateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mButtonFlag) { // 提交
                    if (validateInput()) {
                        mOperateButton.setEnabled(false);
//                        mConsumeRecordService.updateRecord(constructRecord());
                        ConsumeDetailActivity.this.finish();
                    }
                } else { // 修改
                    setViewUsable(true);
                    mButtonFlag = true;
                    mOperateButton.setText("提交");
                }
            }
        });

        mConsumeDateTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                mDatePickerDialog = new DatePickerDialog(
                        ConsumeDetailActivity.this, new OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                    int monthOfYear, int dayOfMonth) {
                                mConsumeRecord.getConsumeTime().set(Calendar.YEAR, year);
                                mConsumeRecord.getConsumeTime().set(Calendar.MONTH, monthOfYear);
                                mConsumeRecord.getConsumeTime().set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                mConsumeDateTextView.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, calendar.get(Calendar.YEAR), calendar
                                .get(Calendar.MONTH), calendar
                                .get(Calendar.DAY_OF_MONTH));
                mDatePickerDialog.show();
            }
        });

        mConsumeTimeTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                mTimePickerDialog = new TimePickerDialog(
                        ConsumeDetailActivity.this, new OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view,
                                    int hourOfDay, int minute) {
                                mConsumeRecord.getConsumeTime().set(Calendar.HOUR_OF_DAY, hourOfDay);
                                mConsumeRecord.getConsumeTime().set(Calendar.MINUTE, minute);
                                mConsumeTimeTextView.setText(hourOfDay + ":" + minute + ":" + "00");
                            }
                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar
                                .get(Calendar.MINUTE), false);
                mTimePickerDialog.show();
            }
        });
    }

    /**
     * 构建消费记录对象
     * 
     * @return
     */
    private void constructRecord() {
        mConsumeRecord.setRecordName(mConsumeNameEditText.getText().toString());
        mConsumeRecord.setRecordPrice(mConsumePriceEditText.getText().toString());
        mConsumeRecord.setRecordRemark(mConsumeRemarksEditText.getText().toString());
    }

    /**
     * 验证界面的输入合法性
     * 
     * @return
     */
    private boolean validateInput() {
        boolean result = false;
        if (mConsumeNameEditText.getText().toString().equals("")
                || mConsumeNameEditText.getText().toString() == null) {
            Toast.makeText(this, "消费名称为空", Toast.LENGTH_SHORT).show();
            return result;
        } else if (mConsumePriceEditText.getText().toString().equals("")
                || mConsumePriceEditText.getText().toString() == null) {
            Toast.makeText(this, "消费金额为空", Toast.LENGTH_SHORT).show();
            return result;
        } else {
            result = true;
        }
        return result;
    }

    /**
     * 设置各控件状态为可编辑状态
     * 
     * @param usable
     */
    private void setViewUsable(boolean usable) {
        mConsumeNameEditText.setEnabled(usable);
        mConsumePriceEditText.setEnabled(usable);
        mConsumeDateTextView.setClickable(usable);
        mConsumeTimeTextView.setClickable(usable);
        mConsumeCreateTimeTextView.setClickable(usable);
        mConsumeRemarksEditText.setEnabled(usable);
        if (usable) {
            mConsumeRemarksEditText.setHint("备注信息(50字以内))");
            mConsumeDateTextView.setTextColor(getResources().getColor(
                    R.color.font_black));
            mConsumeTimeTextView.setTextColor(getResources().getColor(
                    R.color.font_black));
        } else {
            mConsumeDateTextView.setTextColor(getResources().getColor(
                    R.color.font_gray));
            mConsumeTimeTextView.setTextColor(getResources().getColor(
                    R.color.font_gray));
            mConsumeCreateTimeTextView.setTextColor(getResources().getColor(
                    R.color.font_gray));
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
