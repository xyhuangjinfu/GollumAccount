package cn.hjf.gollumaccount.activity;

import java.util.Calendar;
import java.util.Date;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.business.ConsumeRecordManagerBusiness;
import cn.hjf.gollumaccount.businessmodel.ConsumeRecord;
import cn.hjf.gollumaccount.businessmodel.ConsumeType;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment.HEAD_TYPE;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * 添加消费记录界面
 * 
 * @author huangjinfu
 * 
 */
public class AddConsumeActivity extends BaseActivity implements CommonHeaderFragment.ICallback{

    private static final int REQ_CODE_SELECT_TYPE = 0;

    private CommonHeaderFragment mTitleFragment; //顶部标题栏

    private EditText mConsumeNameEditText; // 消费名称
    private EditText mConsumePriceEditText; // 消费金额
    private TextView mConsumeTypeTextView; // 消费类型
    private RelativeLayout mTypeLayout; // 消费类型点击布局
    private EditText mConsumerEditText; // 消费人
    private EditText mPayerEditText; // 付款人
    private TextView mConsumeDateTextView; // 消费日期
    private TextView mConsumeTimeTextView; // 消费时间
    private EditText mConsumeRemarkEditText; // 备注信息
    private Button mCreateButton; // 创建按钮

    private DatePickerDialog mDatePickerDialog; // 消费日期选择对话框
    private TimePickerDialog mTimePickerDialog; // 消费时间选择对话框

    private ConsumeType mConsumeType; // 消费类型
    private ConsumeRecord mConsumeRecord; //消费记录
    private ConsumeRecordManagerBusiness mConsumeRecordManagerBusiness; //消费记录管理业务逻辑
    private Calendar mConsumeCalendar; //消费时间
    
    public AddConsumeActivity() {
        mConsumeRecordManagerBusiness = new ConsumeRecordManagerBusiness(this);
        mConsumeRecord = new ConsumeRecord();
        mConsumeCalendar = Calendar.getInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_consume_record);
        
        initTitle();
        initView();
        initValue();
        initEvent();

    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mConsumeRecordManagerBusiness = null;
        mConsumeRecord = null;
        mConsumeCalendar = null;
    }

    
    /**
     * 初始化顶部导航栏
     */
    @Override
    public void initTitle() {
        mTitleFragment = (CommonHeaderFragment) mFragmentManager.findFragmentById(R.id.title_add_record);
        mTitleFragment.setHeadBtnType(HEAD_TYPE.LEFT_BACK_TEXT,HEAD_TYPE.RIGHT_NULL);
        mTitleFragment.setHeadText(R.string.title_back, R.string.title_add_record, null);
        mTitleFragment.setCallback(this);
    }
    
    /**
     * 初始化各控件
     */
    @Override
    protected void initView() {
        mConsumeNameEditText = (EditText) findViewById(R.id.et_record_name);
        mConsumePriceEditText = (EditText) findViewById(R.id.et_record_price);
        mConsumeTypeTextView = (TextView) findViewById(R.id.tv_record_type);
        mTypeLayout = (RelativeLayout) findViewById(R.id.rl_record_type);
        mConsumeRemarkEditText = (EditText) findViewById(R.id.et_record_remarks);
        mCreateButton = (Button) findViewById(R.id.btn_record_create);
        mConsumeDateTextView = (TextView) findViewById(R.id.et_record_date);
        mConsumeTimeTextView = (TextView) findViewById(R.id.et_record_time);
        mConsumerEditText = (EditText) findViewById(R.id.et_consumer);
        mPayerEditText = (EditText) findViewById(R.id.et_payer);
    }

    /**
     * 初始化控件的值
     */
    @Override
    protected void initValue() {
    }

    /**
     * 初始化各控件的事件
     */
    @Override
    protected void initEvent() {
        mCreateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    mCreateButton.setEnabled(false);
                    buildConsumeRecord();
                    mConsumeRecordManagerBusiness.addRecord(mConsumeRecord);
                    AddConsumeActivity.this.finish();
                }
            }
        });

        mConsumeDateTextView.setOnClickListener(OnDateClickListener);
        mConsumeTimeTextView.setOnClickListener(OnTimeClickListener);
        
        mTypeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddConsumeActivity.this, TypeSelectActivity.class);
                AddConsumeActivity.this.startActivityForResult(intent, REQ_CODE_SELECT_TYPE);
            }
        });
    }

    OnClickListener OnDateClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mDatePickerDialog = new DatePickerDialog(AddConsumeActivity.this,
                    new OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                int monthOfYear, int dayOfMonth) {
                            mConsumeCalendar.set(Calendar.YEAR, year);
                            mConsumeCalendar.set(Calendar.MONTH, monthOfYear);
                            mConsumeCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            mConsumeDateTextView.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        }
                    }, mConsumeCalendar.get(Calendar.YEAR),
                    mConsumeCalendar.get(Calendar.MONTH),
                    mConsumeCalendar.get(Calendar.DAY_OF_MONTH));
            mDatePickerDialog.show();
        }
    };

    OnClickListener OnTimeClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mTimePickerDialog = new TimePickerDialog(AddConsumeActivity.this,
                    new OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                int minute) {
                            mConsumeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            mConsumeCalendar.set(Calendar.MINUTE, minute);
                            mConsumeTimeTextView.setText(hourOfDay + ":" + minute + ":" + "00");
                        }
                    }, mConsumeCalendar.get(Calendar.HOUR_OF_DAY),
                    mConsumeCalendar.get(Calendar.MINUTE), false);
            mTimePickerDialog.show();
        }
    };

    /**
     * 根据界面的数据，构建消费记录对象
     * 
     * @return
     */
    private void buildConsumeRecord() {
        mConsumeRecord.setRecordName(mConsumeNameEditText.getText().toString());
        mConsumeRecord.setRecordPrice(mConsumePriceEditText.getText()
                .toString());
        mConsumeRecord.setRecordType(mConsumeType);
        mConsumeRecord.setRecordRemark(mConsumeRemarkEditText.getText().toString());
        mConsumeRecord.setConsumeTime(mConsumeCalendar);
        Calendar createCalendar = Calendar.getInstance();
        createCalendar.setTime(new Date(System.currentTimeMillis()));
        mConsumeRecord.setCreateTime(createCalendar);
        mConsumeRecord.setConsumer(mConsumerEditText.getText().toString());
        mConsumeRecord.setPayer(mPayerEditText.getText().toString());
    }

    /**
     * 验证界面输入数据的合法性
     * 
     * @return
     */
    private boolean validateInput() {
        boolean result = false;
        if (mConsumeNameEditText.getText().toString() == null
                || mConsumeNameEditText.getText().toString().equals("")) {
            Toast.makeText(this, "消费名称为空", Toast.LENGTH_SHORT).show();
            return result;
        } else if (mConsumePriceEditText.getText().toString() == null
                || mConsumePriceEditText.getText().toString().equals("")) {
            Toast.makeText(this, "消费金额为空", Toast.LENGTH_SHORT).show();
            return result;
        } else if (mConsumeDateTextView.getText().toString() == null
                || mConsumeDateTextView.getText().toString().equals("")) {
            Toast.makeText(this, "请选择消费日期", Toast.LENGTH_SHORT).show();
            return result;
        } else if (mConsumeTimeTextView.getText().toString() == null
                || mConsumeTimeTextView.getText().toString().equals("")) {
            Toast.makeText(this, "请选择消费时间", Toast.LENGTH_SHORT).show();
            return result;
        } else if (mConsumeTypeTextView.getText().toString() == null
                || mConsumeTypeTextView.getText().toString().equals("")) {
            Toast.makeText(this, "请选择消费类型", Toast.LENGTH_SHORT).show();
            return result;
        } else {
            result = true;
        }
        return result;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQ_CODE_SELECT_TYPE) {
                mConsumeType = data.getParcelableExtra("consume_type");
                if (mConsumeType != null) {
                    mConsumeTypeTextView.setText(mConsumeType.getName());
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
