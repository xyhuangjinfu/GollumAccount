package cn.hjf.gollumaccount.fragment;

import java.util.ArrayList;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.model.ConsumeItem;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class AddItemFragment extends Fragment {
	
	private EditText mItemName;
	private Button mCancel;
	private Button mCreate;
	private ArrayList<String> mClassifies = new ArrayList<String>();
	private OnAddItemListener monAddItemListener;
	private ConsumeItem mConsumeItem;
	
	public interface OnAddItemListener {
		public abstract void onAddItemCanceled();
		public abstract void onAddItemCreated(ConsumeItem consumeItem);
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
		View view = inflater.inflate(R.layout.fragment_add_item, null);
		mItemName = (EditText) view.findViewById(R.id.et_item_name);
		mCancel = (Button) view.findViewById(R.id.btn_cancel);
		mCreate = (Button) view.findViewById(R.id.btn_create);
		
		mCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				monAddItemListener.onAddItemCanceled();
				
			}
		});
		
		mCreate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mConsumeItem = new ConsumeItem();
//				mConsumeItem.setItemName(mItemName.getText().toString());
				monAddItemListener.onAddItemCreated(mConsumeItem);
				
			}
		});
		
		
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		monAddItemListener = (OnAddItemListener)activity;
	}
	
	
}
