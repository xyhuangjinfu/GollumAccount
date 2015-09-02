package cn.hjf.gollumaccount.dialog;

import java.util.ArrayList;
import java.util.Calendar;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.business.ConsumeItemService;
import cn.hjf.gollumaccount.util.TimeUtil;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * ��Ѽ�¼��ѯ�Ի���
 * @author huangjinfu
 *
 */
public class ConsumeQueryDialog {
	
	private static AlertDialog mQueryDialog; //��Ѳ�ѯ�Ի���
	private View mDialogView; //�Ի�����صĲ����ļ�
	private Spinner mYearSpinner; //���ѡ��
	private Spinner mMonthSpinner; //�·�ѡ��
	private Spinner mItemSpinner; //����ѡ��
	private Button mCancelButton; //ȡ��ť
	private Button mQueryButton; //��ѯ��ť
	
	private Context mContext; //�����Ķ���
	private OnQueryListener mListener; //�¼��ص��ӿ�
	
	private ArrayList<String> mYearData ; //������
	private ArrayList<String> mMonthData ; //�·����
	private ArrayList<String> mItemData; //�������
	private ConsumeItemService mConsumeItemService; //����ʵ���ҵ���߼�����
	private int mYear; //���
	private int mMonth; //�·�
	private int mItem; //����
	
	/**
	 * �¼��ص��ӿ�
	 * @author huangjinfu
	 *
	 */
	public interface OnQueryListener {
		public abstract void onQuery(int year, int month, int item);
	}
	
	public ConsumeQueryDialog(Context context) {
		this.mContext = context;
	}
	
	/**
	 * ��ʾ�Ի���
	 * @param context
	 */
	public void show() {
		//���ز����ļ�
		mDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_query_consume, null);
		
		initView(mDialogView);
		
		initViewEvent();
		
		initViewValue();
		
		
		//��ʼ��Dialog
		if (mQueryDialog == null) {
			mQueryDialog = new AlertDialog.Builder(mContext).setView(mDialogView).create();
		}
		mQueryDialog.show();
	}
	
	/**
	 * ʵ����ؼ�
	 * @param view
	 */
	private void initView(View view) {
		mYearSpinner = (Spinner) view.findViewById(R.id.spn_query_dialog_year);
		mMonthSpinner = (Spinner) view.findViewById(R.id.spn_query_dialog_month);
		mItemSpinner = (Spinner) view.findViewById(R.id.spn_query_dialog_item);
		mCancelButton = (Button) view.findViewById(R.id.btn_query_dialog_cancel);
		mQueryButton = (Button) view.findViewById(R.id.btn_query_dialog_query);
	}
	
	/**
	 * ��ʼ�����ؼ���ֵ
	 */
	private void initViewValue() {
		//������
		mYearData = new ArrayList<String>();
		for (int i = TimeUtil.getNowYear(); i >= 1980 ; i--) {
			mYearData.add(String.valueOf(i));
		}
		mYearSpinner.setAdapter(new ArrayAdapter<>(mContext, R.layout.item_spinner, mYearData));
		
		//�·����
		mMonthData = new ArrayList<String>();
		for (int j = 1; j <= 12; j++) {
			mMonthData.add(String.valueOf(j));
		}
		mMonthSpinner.setAdapter(new ArrayAdapter<>(mContext, R.layout.item_spinner, mMonthData));
		mMonthSpinner.setSelection(TimeUtil.getNowMonth(), true);
		
		//�������
		mItemData = new ArrayList<String>();
//		mConsumeItemService = new ConsumeItemService(mContext); //ʵ��ҵ���߼�����
//		mItemData = mConsumeItemService.getAllItemName();
		mItemData.add("����");
		mItemSpinner.setAdapter(new ArrayAdapter<>(mContext, R.layout.item_spinner, mItemData));
		mItemSpinner.setSelection(mItemData.size() - 1, true);
	}
	
	/**
	 * ���ø��ؼ����¼�
	 */
	private void initViewEvent() {
		//ȡ��ť�¼�
		mCancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ConsumeQueryDialog.this.close();
				
			}
		});
		
		//��ѯ��ť�¼�
		mQueryButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ConsumeQueryDialog.this.close();
				if (ConsumeQueryDialog.this.mListener != null) {
					ConsumeQueryDialog.this.mListener.onQuery(mYear, mMonth, mItem);
				}
				
			}
		});
		
		//���ѡ���¼�
		mYearSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				mYear = Calendar.getInstance().get(Calendar.YEAR) - position;
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//�·�ѡ���¼�
		mMonthSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				mMonth = position;
//				mMonth = 1 + position;
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//����ѡ���¼�
		mItemSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				mItem = position + 1;
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	/**
	 * �رնԻ���
	 */
	private void close() {
		if ((mQueryDialog != null) && mQueryDialog.isShowing()) {
			mQueryDialog.cancel();
		}
	}
	
	/**
	 * ���ûص�����
	 * @param listener
	 */
	public void setOnQueryListener(OnQueryListener listener) {
		this.mListener = listener;
	}

}
