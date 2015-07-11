package cn.hjf.gollumaccount.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.hjf.gollumaccount.FragmentIdConsts;
import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.activity.MainActivity;
import cn.hjf.gollumaccount.business.ConsumeItemService;
import cn.hjf.gollumaccount.business.ConsumeRecordService;
import cn.hjf.gollumaccount.model.ConsumeRecord;
import cn.hjf.gollumaccount.util.TimeUtil;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.TextView.BufferType;

/**
 * ���Ѽ�¼��ϸ��Ϣ�鿴����
 * @author huangjinfu
 *
 */
public class RecordDetailViewFragment extends Fragment {
	
	private EditText mRecordNameEditText; //��¼����
	private EditText mRecordPriceEditText; //���ѽ��
	private Spinner mRecordItemSpinner; //�������
	private TextView mRecordDateTextView; //��������
	private TextView mRecordTimeTextView; //����ʱ��
	private TextView mRecordCreateTimeTextView; //��¼����ʱ��
	private EditText mRecordRemarksEditText; //��ע��Ϣ
	private Button mReturnButton; //���ذ�ť
	private Button mOperateButton; //�޸Ļ����ύ��ť
	
	private DatePickerDialog mDatePickerDialog;
	private TimePickerDialog mTimePickerDialog;
	
	private OnViewRecordCallback mListener; //�¼�������
	
	private ConsumeRecord mConsumeRecord = null; //Ҫ��ʾ������
	private int[] mRecordTime = new int[5];
	
	private ConsumeItemService mConsumeItemService; //ConsumeItemʵ��ҵ���߼�����
	private ConsumeRecordService mConsumeRecordService; //ConsumeRecordServiceʵ��ҵ���߼�����
	
	private ArrayList<String> mItemNames; //Spinner����
	private ArrayAdapter mArrayAdapter; //mRecordItemSpinner��������
	
	private boolean mButtonFlag; //falseΪ�޸Ĳ�����trueΪ�ύ�޸Ĳ���
	
	private TextView mSpinnerTextView;
	
	private boolean mDateModifyFlag = false; //�޸�����ʱ�������Ƿ��޸ĵı�־
	private boolean mTimeModifyFlag = false; //�޸�����ʱ��ʱ���Ƿ��޸ĵı�־
	
	
	/**
	 * �����ṩ�Ļص��ӿ�
	 * @author huangjinfu
	 *
	 */
	public interface OnViewRecordCallback {
		public abstract void onViewRecordReturn();
		public abstract void onViewRecordModify();
	}
	
	public RecordDetailViewFragment(ConsumeRecord record) {
		this.mConsumeRecord = record;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		View view = inflater.inflate(R.layout.fragment_view_record, null);
		
		mItemNames = mConsumeItemService.getAllItemName();
		
		initView(view);
		
		initViewValues();
		
		
		
		mReturnButton.setOnClickListener(mReturnButtonClickListener);
		mOperateButton.setOnClickListener(mOperateButtonClickListener);
		
		mRecordDateTextView.setOnClickListener(OnDateClickListener);
		mRecordTimeTextView.setOnClickListener(OnTimeClickListener);
		

		
		setViewUsable(false);
		
		return view;
	}
	
OnClickListener OnDateClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Calendar calendar = Calendar.getInstance();
			mDatePickerDialog = new DatePickerDialog(getActivity(), new OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					mDateModifyFlag = true;
					mRecordTime[0] = year;
					mRecordTime[1] = monthOfYear;
					mRecordTime[2] = dayOfMonth;
					mRecordDateTextView.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
				}
			}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			mDatePickerDialog.show();
		}
	};
	
	OnClickListener OnTimeClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Calendar calendar = Calendar.getInstance();
			mTimePickerDialog = new TimePickerDialog(getActivity(), new OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					mTimeModifyFlag = true;
					mRecordTime[3] = hourOfDay;
					mRecordTime[4] = minute;
					mRecordTimeTextView.setText(hourOfDay+":"+minute+":"+"00");
				}
			}, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
			mTimePickerDialog.show();
		}
	};
	
	/**
	 * ������ť�¼�����
	 */
	OnClickListener mOperateButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mButtonFlag) { //�ύ����
				if (validateInputValues()) {
					mOperateButton.setEnabled(false);
					mConsumeRecordService.updateRecord(constructRecord());
					mListener.onViewRecordModify();
				}
				
			} else { //�޸�
				setViewUsable(true);
				mButtonFlag = true;
				mOperateButton.setText("�ύ");
			}
			
		}
	};
	
	/**
	 * ���ذ�ť�¼�����
	 */
	OnClickListener mReturnButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mReturnButton.setEnabled(false);
			mListener.onViewRecordReturn();
		}
	};
	
	/**
	 * ������Ҫ�����µ����Ѽ�¼����
	 * @return
	 */
	private ConsumeRecord constructRecord() {
		this.mConsumeRecord.setRecordName(mRecordNameEditText.getText().toString());
		this.mConsumeRecord.setRecordPrice(Float.valueOf(mRecordPriceEditText.getText().toString()));
		this.mConsumeRecord.setRecordItem(mRecordItemSpinner.getSelectedItemPosition() + 1);
		this.mConsumeRecord.setRecordRemarks(mRecordRemarksEditText.getText().toString());
		this.mConsumeRecord.setRecordTime(this.getRecordTime().getTime());
		this.mConsumeRecord.setCreateTime(System.currentTimeMillis());
		return this.mConsumeRecord;
	}
	
	/**
	 * ����DatePicker �� TimePicker ������Ѽ�¼����ʱ��
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
		Log.i("hjf", "year:" + year);
		Log.i("hjf", "month:" + month);
		Log.i("hjf", "day:" + day);
		Log.i("hjf", "hour:" + hour);
		Log.i("hjf", "minute:" + minute);
		
		Calendar calendar2 = Calendar.getInstance(); 
		calendar2.set(year, month, day, hour, minute);
		Date date = calendar2.getTime();
		return date;
		
	}
	
	/**
	 * ��֤�û�������Ϣ�ĺϷ��ԣ���Ҫ�ǿ����ݵ���֤�����Ҹ�����ʾ
	 * @return false:���Ϸ�������
	 */
	private boolean validateInputValues() {
		boolean result = false;
		if (mRecordNameEditText.getText().toString().equals("") || mRecordNameEditText.getText().toString() == null) {
			Toast.makeText(this.getActivity(), "��������Ϊ�գ�", Toast.LENGTH_SHORT).show();
			return result;
		} else if (mRecordPriceEditText.getText().toString().equals("") || mRecordPriceEditText.getText().toString() == null) {
			Toast.makeText(this.getActivity(), "���ѽ��Ϊ�գ�", Toast.LENGTH_SHORT).show();
			return result;
		} else {
			result = true;
		}
		return result;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mConsumeItemService = new ConsumeItemService(activity);
		mConsumeRecordService = new ConsumeRecordService(activity);
		mListener = (OnViewRecordCallback) activity;
		((MainActivity) activity).refreshMenuForFragment(FragmentIdConsts.FRAGMENT_VIEW_RECORD);
	}
	
	@Override
	public void onDetach() {
		Log.i("hjf", "RecordDetailViewFragment - onDetach");
		super.onDetach();
		this.mConsumeRecord = null;
		((MainActivity) this.getActivity()).refreshMenuForFragment(FragmentIdConsts.FRAGMENT_CONSUME);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	/**
	 * ��ʼ��Ҫ��ʾ����Ϣ��¼
	 * @param record
	 */
	public void init(ConsumeRecord record) {
		this.mConsumeRecord = record;
		
	}
	
	/**
	 * ��ʼ�����ؼ�
	 * @param view
	 */
	private void initView(View view) {
		mRecordNameEditText = (EditText) view.findViewById(R.id.et_record_name_detail);
		mRecordPriceEditText = (EditText) view.findViewById(R.id.et_record_price_detail);
		mRecordItemSpinner = (Spinner) view.findViewById(R.id.spn_record_item_detail);
		mRecordDateTextView = (TextView) view.findViewById(R.id.tv_record_date_detail);
		mRecordTimeTextView = (TextView) view.findViewById(R.id.tv_record_time_detail);
		mRecordCreateTimeTextView = (TextView) view.findViewById(R.id.tv_record_create_time_detail);
		mRecordRemarksEditText = (EditText) view.findViewById(R.id.et_record_remarks_detail);
		mReturnButton = (Button) view.findViewById(R.id.btn_record_return);
		mOperateButton = (Button) view.findViewById(R.id.btn_record_operate);
		
		
	}
	
	/**
	 * �����ؼ���ֵ
	 */
	private void initViewValues() {
		mRecordNameEditText.setText(this.mConsumeRecord.getRecordName());
		mRecordPriceEditText.setText(String.valueOf(this.mConsumeRecord.getRecordPrice()));
		mArrayAdapter = new ArrayAdapter(this.getActivity(), R.layout.item_spinner, mItemNames);
		mRecordItemSpinner.setAdapter(mArrayAdapter);
		mRecordItemSpinner.setSelection(this.mConsumeRecord.getRecordItem() - 1);
		mRecordDateTextView.setText(TimeUtil.getDateString(this.mConsumeRecord.getRecordTime()));
		mRecordTimeTextView.setText(TimeUtil.getShowTimeString(this.mConsumeRecord.getRecordTime()));
		mRecordCreateTimeTextView.setText(TimeUtil.getTimeString(this.mConsumeRecord.getCreateTime()));
		if (("".equals(this.mConsumeRecord.getRecordRemarks())) || (this.mConsumeRecord.getRecordRemarks() == null)) {
			mRecordRemarksEditText.setHint("��");
		} else {
			mRecordRemarksEditText.setText(this.mConsumeRecord.getRecordRemarks());
		}	
		
	}
	
	/**
	 * ���ø��ؼ��Ŀ�����
	 * @param usable
	 */
	private void setViewUsable(boolean usable) {
		mRecordNameEditText.setEnabled(usable);
		mRecordPriceEditText.setEnabled(usable);
		mRecordItemSpinner.setEnabled(usable);
		mRecordDateTextView.setClickable(usable);
		mRecordTimeTextView.setClickable(usable);
		mRecordCreateTimeTextView.setClickable(usable);
		mRecordRemarksEditText.setEnabled(usable);
		if (usable) {
			mRecordRemarksEditText.setHint("��ע��Ϣ(50��������)");
			mRecordDateTextView.setTextColor(getResources().getColor(R.color.font_black));
			mRecordTimeTextView.setTextColor(getResources().getColor(R.color.font_black));
		} else {
			mRecordDateTextView.setTextColor(getResources().getColor(R.color.font_gray));
			mRecordTimeTextView.setTextColor(getResources().getColor(R.color.font_gray));
			mRecordCreateTimeTextView.setTextColor(getResources().getColor(R.color.font_gray));
		}
		
	}
	
	
	@Override
	protected void finalize() throws Throwable {
		Log.i("hjf", "���ٶ���.....");
		super.finalize();
		Log.i("hjf", "���ٶ���.....");
	}

}
