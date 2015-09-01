package cn.hjf.gollumaccount.fragment;

import cn.hjf.gollumaccount.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 通用的顶部标题栏
 * @author kk
 *
 */
public class CommonHeaderFragment extends BaseFragment {

    private LinearLayout mLeftLayout; //返回操作
    private TextView mTitle; //标题
    private TextView mLeftText; //返回指示
    private ImageView mLeftIcon; //返回按钮资源
    private ICallback mCallback; //返回事件监听对象
    private RelativeLayout mParent;
    
    private TextView mRightText; //可选操作1
    private ImageView mRightIcon; //搜索按钮
    
    private View mView;
    
	private HEAD_TYPE mLeftType;
	private HEAD_TYPE mRightType;
    
    public enum HEAD_TYPE {
    	LEFT_NULL,
    	LEFT_BACK_TEXT,
    	LEFT_MENU,
    	RIGHT_NULL,
    	RIGHT_TEXT,
    	RIGHT_ICON,
    	LOGIN
    }

    /**
     * 内部事件通知接口
     * @author xfujohn
     *
     */
    public interface ICallback {
        public abstract void onLeftClick(); //点击返回
        public abstract void onRightClick(); //点击返回
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
    	mView = inflater.inflate(R.layout.fragment_common_header, null);
        initView(mView);
        initEvent();
  
        return mView;
    }

    /**
     * 初始化控件
     * @param view
     */
    private void initView(View view) {
        mLeftLayout = (LinearLayout) view.findViewById(R.id.left_linearlayout);
        mTitle = (TextView) view.findViewById(R.id.txt_title);
        mLeftText = (TextView) view.findViewById(R.id.left_txt);
        mLeftIcon = (ImageView) view.findViewById(R.id.left_image);
        mRightText = (TextView) view.findViewById(R.id.tv_operate1);
        mRightIcon = (ImageView) view.findViewById(R.id.right_image);
        mParent = (RelativeLayout) view.findViewById(R.id.fragment_common_header_parent);
    }

    /**
     * 初始化事件监听
     */
    private void initEvent() {
        mLeftLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    mCallback.onLeftClick();
                }
            }
        });
        mRightText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    mCallback.onRightClick();
                }
            }
        });
        mRightIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    mCallback.onRightClick();
                }
            }
        });
    }

    /**
     * 设置监听回调对象
     * @param callback
     */
    public void setCallback(ICallback callback) {
        this.mCallback = callback;
    }

    public void setHeadBtnType(HEAD_TYPE leftType, HEAD_TYPE rightType) {
    	mLeftType = leftType;
    	mRightType = rightType;
    	setVisibility(leftType);
    	setVisibility(rightType);
    }
    
    public void setHeadText(int txtLeftId, int txtTitleId, int txtRightId) {
    	mLeftText.setText(txtLeftId);
    	mTitle.setText(txtTitleId);
    	mRightText.setText(txtRightId);
    }
    
    public void setHeadText(int txtLeftId, String txtTitle, int txtRightId) {
    	mLeftText.setText(txtLeftId);
    	mTitle.setText(txtTitle);
    	mRightText.setText(txtRightId);
    }

    public void setHeadText(String txtLeft, String txtTitle, int txtRightId) {
    	mLeftText.setText(txtLeft);
    	mTitle.setText(txtTitle);
    	mRightText.setText(txtRightId);
    }
    public void setHeadText(String txtLeft, int txtTitleId, String txtRight) {
    	mLeftText.setText(txtLeft);
    	mTitle.setText(txtTitleId);
    	mRightText.setText(txtRight);
    }
    public void setHeadText(int txtLeftId, int txtTitleId, String txtRight) {
    	mLeftText.setText(txtLeftId);
    	mTitle.setText(txtTitleId);
    	mRightText.setText(txtRight);
    }
    
    private void setVisibility(HEAD_TYPE type) {
    	switch (type) {
		case LEFT_BACK_TEXT:
			mLeftText.setVisibility(View.VISIBLE);
			mLeftIcon.setVisibility(View.VISIBLE);
			mLeftIcon.setImageResource(R.drawable.title_back_btn);
			break;
			
		case LEFT_MENU:
			mLeftText.setVisibility(View.GONE);
			mLeftIcon.setVisibility(View.VISIBLE);
			mLeftIcon.setImageResource(R.drawable.btn_personal);
			break;
			
		case LEFT_NULL:
			mLeftText.setVisibility(View.GONE);
			mLeftIcon.setVisibility(View.GONE);
			break;
			
		case RIGHT_NULL:
			mRightText.setVisibility(View.GONE);
			break;
			
		case RIGHT_TEXT:
			mRightText.setVisibility(View.VISIBLE);
			break;
			
		case RIGHT_ICON:
			mRightText.setVisibility(View.GONE);
			mRightIcon.setVisibility(View.VISIBLE);
			break;
			
	     case LOGIN:
	        mRightText.setVisibility(View.GONE);
	        mTitle.setVisibility(View.GONE);
	        int backColor = 0x00000000;
	        int textColor = 0xff171c61;
	        mParent.setBackgroundColor(backColor);
	        mLeftText.setTextColor(textColor);
//	        mLeftIcon.setImageResource(R.drawable.head_btn_back_blue);
	        break;
			
		default:
			break;
		}
    }
    
    /**
     * 设置背景颜色
     * @param color
     */
    public void setBackground(int color) {
        mParent.setBackgroundColor(color);
    }
    
    /**
     * 设置文字颜色
     * @param color
     */
    public void setTextColor(int color) {
        mLeftText.setTextColor(color);
    }
}
