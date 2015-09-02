package cn.hjf.gollumaccount.view;

import cn.hjf.gollumaccount.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;

public class LoadingDialog extends Dialog {
    

    public LoadingDialog(Context context, boolean cancelable,
            OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    public LoadingDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_loading);
        initView();
        initWindow();
    }
    
    private void initView() {
        
    }
    
    private void initWindow() {
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = LayoutParams.WRAP_CONTENT;
        params.height = LayoutParams.WRAP_CONTENT;
        window.setBackgroundDrawableResource(R.drawable.bg_black);
        window.setAttributes(params);
    }
}
