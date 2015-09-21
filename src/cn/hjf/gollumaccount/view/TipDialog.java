package cn.hjf.gollumaccount.view;

import com.tencent.bugly.proguard.ab;

import cn.hjf.gollumaccount.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TipDialog extends Dialog {
    
    private RelativeLayout mRoot;
    private Context mContext;
    private TextView mPositive;
    private TextView mNegative;
    private TextView mTitleTextView;
    private OnTipDialogClickListener mListener;
    private String mTitle;
    
    public interface OnTipDialogClickListener {
        public abstract void onPositiveClick();
        public abstract void onNegativeClick();
    }

    public TipDialog(Context context, boolean cancelable,
            OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    public TipDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    public TipDialog(Context context) {
        super(context);
        mContext = context;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tip);
        
        initView();
        computeSize();
        initValue();
        initEvent();
        
    }
    
    private void initView() {
        mRoot = (RelativeLayout) findViewById(R.id.rl_root);
        mPositive = (TextView) findViewById(R.id.tv_right);
        mNegative = (TextView) findViewById(R.id.tv_left);
        mTitleTextView = (TextView) findViewById(R.id.tv_tip_title);
    }
    
    private void initValue() {
        mTitleTextView.setText(mTitle);
    }
    
    private void initEvent() {
        mPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
                if (mListener != null) {
                    mListener.onPositiveClick();
                }
            }
        });
        mNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
                if (mListener != null) {
                    mListener.onNegativeClick();
                }
            }
        });
    }
    
    private void computeSize() {
        FrameLayout.LayoutParams rllp = (FrameLayout.LayoutParams) mRoot
                .getLayoutParams();
        WindowManager windowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        // 宽度为屏幕的80%
        rllp.width = (display.getWidth()) * 80 / 100;
        mRoot.setLayoutParams(rllp);
    }
    
    public void setOnTipDialogClickListener(OnTipDialogClickListener onTipDialogClickListener) {
        this.mListener = onTipDialogClickListener;
    }
    
    public void setTitle(String title) {
        this.mTitle = title;
    }

}
