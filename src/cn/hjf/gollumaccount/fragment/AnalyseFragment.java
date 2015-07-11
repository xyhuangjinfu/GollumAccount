package cn.hjf.gollumaccount.fragment;

import cn.hjf.gollumaccount.FragmentIdConsts;
import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.activity.MainActivity;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class AnalyseFragment extends Fragment {
	
	private Button mByItemButton;
	private Button mByMonthButton;
	private OnAnalyseCallback mListener;
	
	public interface OnAnalyseCallback {
		public abstract void onByItemClick();
		public abstract void onByMonthClick();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_analyse, null);
		
		initView(view);
		
		mByItemButton.setOnClickListener(mByItemButtonClickListener);
		mByMonthButton.setOnClickListener(mByMonthButtonClickListener);
		
		return view;
	}
	
	OnClickListener mByItemButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mListener.onByItemClick();
		}
	};
	
	OnClickListener mByMonthButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mListener.onByMonthClick();
		}
	};
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mListener = (OnAnalyseCallback) activity;
		((MainActivity) activity).refreshMenuForFragment(FragmentIdConsts.FRAGMENT_ANALYSE);
	}
	
	public void initView(View view) {
		mByItemButton = (Button) view.findViewById(R.id.btn_pie_by_item);
		mByMonthButton = (Button) view.findViewById(R.id.btn_line_by_month);
	}
}
