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
	
	private TextView mVersionTextView; //�汾��Ϣ
	private TextView mAuthorTextView; //������Ϣ
	private TextView mDeclareTextView; //������Ϣ
	
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
		mAuthorTextView.setText("�ƽ��");
		mDeclareTextView.setText("��Ӧ�����漰��ͼƬ��Դ�����Ի���������Ӧ�ò����κ���ҵ��;�������ַ�ͼƬ��Դ�����ߵ����档");
	}
}
