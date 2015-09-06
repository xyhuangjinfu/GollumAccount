package cn.hjf.gollumaccount.activity;

import java.util.Calendar;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.asynctask.UpdateConsumeRecordTask;
import cn.hjf.gollumaccount.businessmodel.ConsumeRecord;
import cn.hjf.gollumaccount.businessmodel.ConsumeType;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment.HEAD_TYPE;
import cn.hjf.gollumaccount.util.TimeUtil;
import cn.hjf.gollumaccount.view.LoadingDialog;
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
import android.widget.ImageView;
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
public class ConsumeDetailActivity extends BaseActivity implements 
CommonHeaderFragment.ICallback, UpdateConsumeRecordTask.OnUpdateConsumeRecordListener {

    public static final String CONSUME_RECORD = "consume_record";
    private static final int REQ_CODE_SELECT_TYPE = 0;
    
    private CommonHeaderFragment mTitleFragment; //顶部标题栏
    private LoadingDialog mLoadingDialog; //加载对话框

    private EditText mConsumeNameEditText; // 消费名称
    private EditText mConsumePriceEditText; // 消费金额
    private TextView mConsumeTypeTextView; // 消费类型
    private RelativeLayout mTypeLayout; // 消费类型点击布局
    private TextView mConsumeDateTextView; // 消费日期
    private TextView mConsumeTimeTextView; // 消费时间
    private TextView mConsumeCreateTimeTextView; // 消费记录创建时间
    private EditText mConsumeRemarksEditText; // 备注信息
    private Button mOperateButton; // 修改按钮
    private ImageView mRightImage; //修改类型的又箭头图标
    
    private DatePickerDialog mDatePickerDialog; // 消费日期选择对话框
    private TimePickerDialog mTimePickerDialog; // 消费时间选择对话框
    
    private ConsumeRecord mConsumeRecord = null; // 消费记录对象
    private PageStatus mPageStatus; //当前页面模式
    
    private enum PageStatus {
        VIEW, //预览模式，不可编辑
        EDIT //编辑模式，修改消费记录
    }

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
        mRightImage = (ImageView) findViewById(R.id.iv_ic_right);
        mLoadingDialog = new LoadingDialog(this, R.style.translucent_dialog);
        mLoadingDialog.setCancelable(false);
    }

    /**
     * 初始化控件的值
     */
    @Override
    protected void initValue() {
        changeToView();
    }
    
    OnClickListener consumeDateClickListener = new OnClickListener() {
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
    };
    
    OnClickListener ConsumeTimeClickListener = new OnClickListener() {
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
    };
    
    OnClickListener ConsumeTypeClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ConsumeDetailActivity.this, TypeSelectActivity.class);
            intent.putExtra(TypeSelectActivity.PAGE_TYPE, TypeSelectActivity.PageType.MANAGER);
            ConsumeDetailActivity.this.startActivityForResult(intent, REQ_CODE_SELECT_TYPE);
        }
    };

    /**
     * 初始化各控件的事件
     */
    @Override
    protected void initEvent() {
        mOperateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPageStatus == PageStatus.EDIT) { // 提交
                    if (validateInput()) {
                        mLoadingDialog.show();
                        constructRecord();
                        new UpdateConsumeRecordTask(ConsumeDetailActivity.this, ConsumeDetailActivity.this).execute(mConsumeRecord);
                    }
                } else if (mPageStatus == PageStatus.VIEW) { // 修改
                    changeToEdit();
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQ_CODE_SELECT_TYPE) {
                ConsumeType consumeType = data.getParcelableExtra(TypeSelectActivity.CONSUME_TYPE);
                if (consumeType != null) {
                    mConsumeRecord.setRecordType(consumeType);
                    mConsumeTypeTextView.setText(mConsumeRecord.getRecordType().getName());
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
    public void OnConsumeRecordUpdated(boolean result) {
        mLoadingDialog.cancel();
        ConsumeDetailActivity.this.finish();
    }
    
    /**
     * 改变当前页面状态为编辑模式
     */
    private void changeToEdit() {
        //设置内容
        mConsumeNameEditText.setText(mConsumeRecord.getRecordName());
        mConsumePriceEditText.setText(mConsumeRecord.getRecordPrice());
        mConsumeTypeTextView.setText(mConsumeRecord.getRecordType().getName());
        mConsumeDateTextView.setText(TimeUtil.getDateString(mConsumeRecord.getConsumeTime()));
        mConsumeTimeTextView.setText(TimeUtil.getTimeString(mConsumeRecord.getConsumeTime()));
        mConsumeCreateTimeTextView.setText(TimeUtil.getDateTimeString(mConsumeRecord.getCreateTime()));
        mConsumeRemarksEditText.setText(mConsumeRecord.getRecordRemark());
        //设置编辑框可用，显示光标
        mConsumeNameEditText.setEnabled(true);
        mConsumePriceEditText.setEnabled(true);
        mConsumeRemarksEditText.setEnabled(true);
        //修改UI
        mOperateButton.setText("提交");
        mRightImage.setVisibility(View.VISIBLE);
        //设置页面状态
        mPageStatus = PageStatus.EDIT;
        //绑定可用状态的事件
        mConsumeDateTextView.setOnClickListener(consumeDateClickListener);
        mConsumeTimeTextView.setOnClickListener(ConsumeTimeClickListener);
        mConsumeTypeTextView.setOnClickListener(ConsumeTypeClickListener);
    }
    
    /**
     * 改变当前页面状态为预览模式
     */
    private void changeToView() {
        //设置hint
        mConsumeNameEditText.setHint(mConsumeRecord.getRecordName());
        mConsumePriceEditText.setHint(mConsumeRecord.getRecordPrice());
        mConsumeTypeTextView.setHint(mConsumeRecord.getRecordType().getName());
        mConsumeDateTextView.setHint(TimeUtil.getDateString(mConsumeRecord.getConsumeTime()));
        mConsumeTimeTextView.setHint(TimeUtil.getTimeString(mConsumeRecord.getConsumeTime()));
        mConsumeCreateTimeTextView.setHint(TimeUtil.getDateTimeString(mConsumeRecord.getCreateTime()));
        mConsumeRemarksEditText.setHint(mConsumeRecord.getRecordRemark());
        //设置编辑框不可用，隐藏光标
        mConsumeNameEditText.setEnabled(false);
        mConsumePriceEditText.setEnabled(false);
        mConsumeRemarksEditText.setEnabled(false);
        //修改UI
        mOperateButton.setText("修改");
        mRightImage.setVisibility(View.GONE);
        //设置页面状态
        mPageStatus = PageStatus.VIEW;
        //解绑可用状态的事件
        mConsumeDateTextView.setOnClickListener(null);
        mConsumeTimeTextView.setOnClickListener(null);
        mConsumeTypeTextView.setOnClickListener(null);
    }

}
