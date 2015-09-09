package cn.hjf.gollumaccount.activity;

import java.util.Calendar;
import java.util.Date;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.asynctask.CreateConsumeRecordTask;
import cn.hjf.gollumaccount.businessmodel.ConsumeRecord;
import cn.hjf.gollumaccount.businessmodel.ConsumeType;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment.HEAD_TYPE;
import cn.hjf.gollumaccount.util.NumberUtil;
import cn.hjf.gollumaccount.view.LoadingDialog;
import cn.hjf.gollumaccount.view.ToastUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
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
import android.widget.Toast;

/**
 * 添加消费记录界面
 * 
 * @author huangjinfu
 * 
 */
public class AddConsumeActivity extends BaseActivity implements CommonHeaderFragment.ICallback,
CreateConsumeRecordTask.OnCreateConsumeRecordListener {

    private static final int REQ_CODE_SELECT_TYPE = 0; //请求选择消费类型请求码

    private CommonHeaderFragment mTitleFragment; //顶部标题栏
    private LoadingDialog mLoadingDialog; //加载对话框

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
    private Calendar mConsumeCalendar; //消费时间
    
    public AddConsumeActivity() {
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
        
        mLoadingDialog = new LoadingDialog(this, R.style.translucent_dialog);
        mLoadingDialog.setCancelable(false);
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
                    buildConsumeRecord();
                    mLoadingDialog.show();
                    new CreateConsumeRecordTask(AddConsumeActivity.this, AddConsumeActivity.this).execute(mConsumeRecord);
                }
            }
        });

        mConsumeDateTextView.setOnClickListener(OnDateClickListener);
        mConsumeTimeTextView.setOnClickListener(OnTimeClickListener);
        
        mTypeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddConsumeActivity.this, TypeSelectActivity.class);
                intent.putExtra(TypeSelectActivity.PAGE_TYPE, TypeSelectActivity.PageType.MANAGER);
                AddConsumeActivity.this.startActivityForResult(intent, REQ_CODE_SELECT_TYPE);
            }
        });
    }

    OnClickListener OnDateClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mDatePickerDialog = new DatePickerDialog(AddConsumeActivity.this,
                    AlertDialog.THEME_HOLO_LIGHT,
                    new OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                int monthOfYear, int dayOfMonth) {
                            mConsumeCalendar.set(Calendar.YEAR, year);
                            mConsumeCalendar.set(Calendar.MONTH, monthOfYear);
                            mConsumeCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            mConsumeDateTextView.setText(year + " - " + NumberUtil.formatTwoInt(monthOfYear + 1) + " - " + NumberUtil.formatTwoInt(dayOfMonth));
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
                    AlertDialog.THEME_HOLO_LIGHT,
                    new OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                int minute) {
                            mConsumeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            mConsumeCalendar.set(Calendar.MINUTE, minute);
                            mConsumeTimeTextView.setText(NumberUtil.formatTwoInt(hourOfDay) + " : " + NumberUtil.formatTwoInt(minute) + " : " + "00");
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
            ToastUtil.showToast(this, getResources().getString(R.string.tip_record_name_null), Toast.LENGTH_SHORT);
            return result;
        } else if (mConsumePriceEditText.getText().toString() == null
                || mConsumePriceEditText.getText().toString().equals("")) {
            ToastUtil.showToast(this, getResources().getString(R.string.tip_record_price_null), Toast.LENGTH_SHORT);
            return result;
        } else if (mConsumeDateTextView.getText().toString() == null
                || mConsumeDateTextView.getText().toString().equals("")) {
            ToastUtil.showToast(this, getResources().getString(R.string.tip_record_date_null), Toast.LENGTH_SHORT);
            return result;
        } else if (mConsumeTimeTextView.getText().toString() == null
                || mConsumeTimeTextView.getText().toString().equals("")) {
            ToastUtil.showToast(this, getResources().getString(R.string.tip_record_time_null), Toast.LENGTH_SHORT);
            return result;
        } else if (mConsumeTypeTextView.getText().toString() == null
                || mConsumeTypeTextView.getText().toString().equals("")) {
            ToastUtil.showToast(this, getResources().getString(R.string.tip_record_type_null), Toast.LENGTH_SHORT);
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
                mConsumeType = data.getParcelableExtra(TypeSelectActivity.CONSUME_TYPE);
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

    @Override
    public void OnCreateConsumeRecordCompleted(boolean isCreateSuccess) {
        mLoadingDialog.cancel();
        if (isCreateSuccess) {
            AddConsumeActivity.this.setResult(Activity.RESULT_OK);
            AddConsumeActivity.this.finish();
        } else {
            ToastUtil.showToast(this, getResources().getString(R.string.tip_create_consume_record_fail), Toast.LENGTH_SHORT);
        }
    }

}
