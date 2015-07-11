package cn.hjf.gollumaccount.fragment;

import cn.hjf.gollumaccount.FragmentIdConsts;
import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.activity.MainActivity;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutFragment extends Fragment {
	
	private TextView mVersionTextView; //版本信息
	private TextView mAuthorTextView; //作者信息
	private TextView mDeclareTextView; //声明信息
	
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
		View view = inflater.inflate(R.layout.fragment_about, null);
		
		initView(view);
		
		initViewValue();
		
		return view;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).refreshMenuForFragment(FragmentIdConsts.FRAGMENT_ABOUT);
	}
	
	private void initView(View view) {
		mVersionTextView = (TextView) view.findViewById(R.id.tv_version);
		mAuthorTextView = (TextView) view.findViewById(R.id.tv_author);
		mDeclareTextView = (TextView) view.findViewById(R.id.tv_declare);
	}
	
	private void initViewValue() {
		PackageManager pm = this.getActivity().getPackageManager();
		PackageInfo info = null;
		try {
			info = pm.getPackageInfo(this.getActivity().getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		mVersionTextView.setText(info.versionName);
		mAuthorTextView.setText("黄金夫");
		mDeclareTextView.setText("本应用所涉及的图片资源均来自互联网，本应用不做任何商业用途，不会侵犯图片资源所有者的利益。");
	}
}
