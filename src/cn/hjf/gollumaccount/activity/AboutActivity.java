package cn.hjf.gollumaccount.activity;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment;
import cn.hjf.gollumaccount.fragment.CommonHeaderFragment.HEAD_TYPE;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 关于信息界面
 * @author huangjinfu
 *
 */
public class AboutActivity extends BaseActivity implements CommonHeaderFragment.ICallback {
	
	private TextView mVersionTextView; //版本信息
	private TextView mAuthorTextView; //作者信息
	private TextView mDeclareTextView; //声明信息
    /**
     * 顶部标题栏
     */
    private CommonHeaderFragment mTitleFragment;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_about);
		
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
        mTitleFragment = (CommonHeaderFragment) mFragmentManager.findFragmentById(R.id.title_about);
        mTitleFragment.setHeadBtnType(HEAD_TYPE.LEFT_BACK_TEXT,HEAD_TYPE.RIGHT_NULL);
        mTitleFragment.setHeadText(R.string.title_back, R.string.title_about, null);
        mTitleFragment.setCallback(this);
    }

    @Override
    protected void initView() {
        mVersionTextView = (TextView) findViewById(R.id.tv_version);
        mAuthorTextView = (TextView) findViewById(R.id.tv_author);
        mDeclareTextView = (TextView) findViewById(R.id.tv_declare);
    }

    @Override
    protected void initValue() {
        PackageManager pm = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = pm.getPackageInfo(this.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        mVersionTextView.setText(info.versionName);
        mAuthorTextView.setText("黄金夫");
        mDeclareTextView.setText("本应用所涉及的图片资源均来自互联网，本应用不做任何商业用途，不会侵犯图片资源所有者的利益。");
    }

    @Override
    protected void initEvent() {
    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {
    }
}
