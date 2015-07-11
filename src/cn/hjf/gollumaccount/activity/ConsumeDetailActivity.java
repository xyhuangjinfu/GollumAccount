package cn.hjf.gollumaccount.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.business.ConsumeItemService;
import cn.hjf.gollumaccount.business.ConsumeRecordService;
import cn.hjf.gollumaccount.model.ConsumeRecord;
import cn.hjf.gollumaccount.util.TimeUtil;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * 消费记录详细信息界面/信息修改界面
 * 
 * @author huangjinfu
 * 
 */
public class ConsumeDetailActivity extends BaseActivity {

    public static final String RECORD = "record";

    private EditText mConsumeNameEditText; // 消费名称
    private EditText mConsumePriceEditText; // 消费金额
    private Spinner mConsumeTypeSpinner; // 消费类型
    private TextView mConsumeDateTextView; // 消费日期
    private TextView mConsumeTimeTextView; // 消费时间
    private TextView mConsumeCreateTimeTextView; // 消费记录创建时间
    private EditText mConsumeRemarksEditText; // 备注信息
    private Button mReturnButton; // 返回按钮
    private Button mOperateButton; // 修改按钮
    private DatePickerDialog mDatePickerDialog; // 消费日期选择对话框
    private TimePickerDialog mTimePickerDialog; // 消费时间选择对话框
    private ConsumeRecord mConsumeRecord = null; // 消费记录对象
    private int[] mRecordTime = new int[5];
    private ConsumeItemService mConsumeItemService; // 消费类型业务逻辑类
    private ConsumeRecordService mConsumeRecordService; // 消费记录业务逻辑类
    private ArrayList<String> mItemNames; // 消费类型数据
    private ArrayAdapter<String> mArrayAdapter; // 消费类型控件的适配器
    private boolean mButtonFlag; // false-修改，true-提交
    private boolean mDateModifyFlag = false; // 是否已经修改日期，true-已经修改־
    private boolean mTimeModifyFlag = false; // 是否已经修改时间，true-已经时间־

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_record);
        Intent intent = this.getIntent();
        if (intent.getSerializableExtra(RECORD) == null) {
            finish();
            return;
        } else {
            mConsumeRecord = (ConsumeRecord) intent.getSerializableExtra(RECORD);
        }
        mConsumeRecordService = new ConsumeRecordService(this);
        mConsumeItemService = new ConsumeItemService(this);
        initView();
        initValue();
        initEvent();
        setViewUsable(false);
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
        mConsumeTypeSpinner = (Spinner) findViewById(R.id.spn_record_item_detail);
        mConsumeDateTextView = (TextView) findViewById(R.id.tv_record_date_detail);
        mConsumeTimeTextView = (TextView) findViewById(R.id.tv_record_time_detail);
        mConsumeCreateTimeTextView = (TextView) findViewById(R.id.tv_record_create_time_detail);
        mConsumeRemarksEditText = (EditText) findViewById(R.id.et_record_remarks_detail);
        mReturnButton = (Button) findViewById(R.id.btn_record_return);
        mOperateButton = (Button) findViewById(R.id.btn_record_operate);
    }

    /**
     * 初始化控件的值
     */
    @Override
    protected void initValue() {
        mItemNames = mConsumeItemService.getAllItemName();
        mConsumeNameEditText.setText(this.mConsumeRecord.getRecordName());
        mConsumePriceEditText.setText(String.valueOf(this.mConsumeRecord
                .getRecordPrice()));
        mArrayAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner,
                mItemNames);
        mConsumeTypeSpinner.setAdapter(mArrayAdapter);
        mConsumeTypeSpinner
                .setSelection(this.mConsumeRecord.getRecordItem() - 1);
        mConsumeDateTextView.setText(TimeUtil.getDateString(this.mConsumeRecord
                .getRecordTime()));
        mConsumeTimeTextView.setText(TimeUtil
                .getShowTimeString(this.mConsumeRecord.getRecordTime()));
        mConsumeCreateTimeTextView.setText(TimeUtil
                .getTimeString(this.mConsumeRecord.getCreateTime()));
        if (("".equals(this.mConsumeRecord.getRecordRemarks()))
                || (this.mConsumeRecord.getRecordRemarks() == null)) {
            mConsumeRemarksEditText.setHint("备注信息");
        } else {
            mConsumeRemarksEditText.setText(this.mConsumeRecord
                    .getRecordRemarks());
        }

    }

    /**
     * 初始化各控件的事件
     */
    @Override
    protected void initEvent() {
        mReturnButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mReturnButton.setEnabled(false);
                ConsumeDetailActivity.this.finish();
            }
        });

        mOperateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mButtonFlag) { // 提交
                    if (validateInput()) {
                        mOperateButton.setEnabled(false);
                        mConsumeRecordService.updateRecord(constructRecord());
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
                                mDateModifyFlag = true;
                                mRecordTime[0] = year;
                                mRecordTime[1] = monthOfYear;
                                mRecordTime[2] = dayOfMonth;
                                mConsumeDateTextView.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);
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
                                mTimeModifyFlag = true;
                                mRecordTime[3] = hourOfDay;
                                mRecordTime[4] = minute;
                                mConsumeTimeTextView.setText(hourOfDay + ":"
                                        + minute + ":" + "00");
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
    private ConsumeRecord constructRecord() {
        this.mConsumeRecord.setRecordName(mConsumeNameEditText.getText()
                .toString());
        this.mConsumeRecord.setRecordPrice(Float.valueOf(mConsumePriceEditText
                .getText().toString()));
        this.mConsumeRecord.setRecordItem(mConsumeTypeSpinner
                .getSelectedItemPosition() + 1);
        this.mConsumeRecord.setRecordRemarks(mConsumeRemarksEditText.getText()
                .toString());
        this.mConsumeRecord.setRecordTime(this.getRecordTime().getTime());
        this.mConsumeRecord.setCreateTime(System.currentTimeMillis());
        return this.mConsumeRecord;
    }

    /**
     * 根据日期和时间选择控件的值，生成消费时间
     * 
     * @return
     */
    private Date getRecordTime() {
        int year = 0;
        int month = 0;
        int day = 0;
        int hour = 0;
        int minute = 0;
        Date d = new Date(this.mConsumeRecord.getRecordTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        if (mDateModifyFlag) {
            year = mRecordTime[0];
            month = mRecordTime[1];
            day = mRecordTime[2];
        } else {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }
        if (mTimeModifyFlag) {
            hour = mRecordTime[3];
            minute = mRecordTime[4];
        } else {
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
        }

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(year, month, day, hour, minute);
        Date date = calendar2.getTime();
        return date;

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
        mConsumeTypeSpinner.setEnabled(usable);
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

}
