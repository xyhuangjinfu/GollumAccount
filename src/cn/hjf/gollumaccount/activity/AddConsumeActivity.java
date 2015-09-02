package cn.hjf.gollumaccount.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.business.ConsumeItemService;
import cn.hjf.gollumaccount.business.ConsumeRecordManagerBusiness;
import cn.hjf.gollumaccount.business.ConsumeRecordService;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment.HEAD_TYPE;
import cn.hjf.gollumaccount.model.ConsumeRecord;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
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
 * 添加消费记录界面
 * 
 * @author huangjinfu
 * 
 */
public class AddConsumeActivity extends BaseActivity implements CommonHeaderFragment.ICallback{

    private EditText mConsumeNameEditText; // 消费名称
    private EditText mConsumePriceEditText; // 消费金额
    private Spinner mConsumeTypeSpinner; // 消费类型
    private EditText mConsumeRemarksEditText; // 备注信息
    private Button mCreateButton; // 创建按钮
    private TextView mConsumeDateTextView; // 消费日期
    private TextView mConsumeTimeTextView; // 消费时间
    private DatePickerDialog mDatePickerDialog; // 消费日期选择对话框
    private TimePickerDialog mTimePickerDialog; // 消费时间选择对话框
    private boolean mDateFlag = false; // 是否已经选择日期，true-已经选择
    private boolean mTimeFlag = false; // 是否已经选择时间，true-已经选择
    private int[] mConsumeTime = new int[5]; // 消费时间，0-年、1-月、2-日、3-时、4-分
    private ArrayAdapter<String> mArrayAdapter; // 消费类型控件的适配器
    private ConsumeRecordService mConsumeRecordService; // 消费记录业务逻辑类
    private ArrayList<String> mTypes; // 消费类型数据
    private ConsumeItemService mConsumeItemService; // 消费类型业务逻辑类
    
    private ConsumeRecordManagerBusiness mConsumeRecordManagerBusiness;
    /**
     * 顶部标题栏
     */
    private CommonHeaderFragment mTitleFragment;
    
    public AddConsumeActivity() {
        mConsumeRecordManagerBusiness = new ConsumeRecordManagerBusiness(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_record);
//        mConsumeRecordService = new ConsumeRecordService(this);
//        mConsumeItemService = new ConsumeItemService(this);
        
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
        mConsumeTypeSpinner = (Spinner) findViewById(R.id.spn_record_item);
        mConsumeRemarksEditText = (EditText) findViewById(R.id.et_record_remarks);
        mCreateButton = (Button) findViewById(R.id.btn_record_create);
        mConsumeDateTextView = (TextView) findViewById(R.id.et_record_date);
        mConsumeTimeTextView = (TextView) findViewById(R.id.et_record_time);
    }

    /**
     * 初始化控件的值
     */
    @Override
    protected void initValue() {
//        mTypes = mConsumeItemService.getAllItemName();
        mArrayAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner,
                mTypes);
        mConsumeTypeSpinner.setAdapter(mArrayAdapter);
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
//                    mConsumeRecordService.saveRecord(getConsume());
                    mConsumeRecordManagerBusiness.addRecord(getConsume());
                    AddConsumeActivity.this.finish();
                }
            }
        });

        mConsumeDateTextView.setOnClickListener(OnDateClickListener);
        mConsumeTimeTextView.setOnClickListener(OnTimeClickListener);
    }

    OnClickListener OnDateClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            mDatePickerDialog = new DatePickerDialog(AddConsumeActivity.this,
                    new OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                int monthOfYear, int dayOfMonth) {
                            mDateFlag = true;
                            mConsumeTime[0] = year;
                            mConsumeTime[1] = monthOfYear;
                            mConsumeTime[2] = dayOfMonth;
                            mConsumeDateTextView.setText(year + "-"
                                    + (monthOfYear + 1) + "-" + dayOfMonth);
                        }
                    }, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            mDatePickerDialog.show();
        }
    };

    OnClickListener OnTimeClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            mTimePickerDialog = new TimePickerDialog(AddConsumeActivity.this,
                    new OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                int minute) {
                            mTimeFlag = true;
                            mConsumeTime[3] = hourOfDay;
                            mConsumeTime[4] = minute;
                            mConsumeTimeTextView.setText(hourOfDay + ":"
                                    + minute + ":" + "00");
                        }
                    }, calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE), false);
            mTimePickerDialog.show();
        }
    };

    /**
     * 根据界面的数据，构建消费记录对象
     * 
     * @return
     */
    private ConsumeRecord getConsume() {
        ConsumeRecord record = new ConsumeRecord();
        record.setRecordName(mConsumeNameEditText.getText().toString());
        record.setRecordPrice(Float.valueOf(mConsumePriceEditText.getText()
                .toString()));
        record.setRecordTypeId(mConsumeTypeSpinner.getSelectedItemPosition() + 1);
        record.setRecordRemark(mConsumeRemarksEditText.getText().toString());
        record.setConsumeTime(this.getConsumeTime().getTime());
        record.setCreateTime(System.currentTimeMillis());
        return record;
    }

    /**
     * 根据日期和时间选择控件的值，生成消费时间
     * 
     * @return
     */
    private Date getConsumeTime() {
        int year = mConsumeTime[0];
        int month = mConsumeTime[1];
        int day = mConsumeTime[2];
        int hour = mConsumeTime[3];
        int minute = mConsumeTime[4];
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        Date date = calendar.getTime();
        return date;

    }

    /**
     * 验证界面输入数据的合法性
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
        } else if (!mDateFlag) {
            Toast.makeText(this, "请选择消费日期", Toast.LENGTH_SHORT).show();
            return result;
        } else if (!mTimeFlag) {
            Toast.makeText(this, "请选择消费时间", Toast.LENGTH_SHORT).show();
            return result;
        } else {
            result = true;
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

}
