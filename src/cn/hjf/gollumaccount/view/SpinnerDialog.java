package cn.hjf.gollumaccount.view;

import java.util.List;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.adapter.SpinnerDialogAdapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

/**
 * 自定义的列表选择对话框
 * 
 * @author xfujohn
 * 
 */
public class SpinnerDialog extends Dialog {

    private static final String TAG = "SpinnerDialog";

    /**
     * 上下文对象
     */
    private Context mContext;
    /**
     * 使用此对话框的回调对象
     */
    private ICallback mCallback;
    /**
     * 显示数据的ListView的适配器
     */
    private SpinnerDialogAdapter mDialogselectadapter;
    /**
     * 显示数据的ListView
     */
    private ListView mSpinnerListView;
    /**
     * 显示的列表
     */
    private List<String> mList;

    /**
     * 与调用者交互的回调接口
     * 
     * @author xfujohn
     * 
     */
    public interface ICallback {
        /**
         * 被点击的位置
         * 
         * @param positon
         */
        public abstract void onItemClick(int positon);
    }

    public SpinnerDialog(Context context) {
        this(context, 0, null);
    }

    public SpinnerDialog(Context context, int theme) {
        this(context, theme, null);
    }

    public SpinnerDialog(Context context, int theme, List<String> list) {
        this(context, theme, list, null);
    }

    public SpinnerDialog(Context context, int theme, List<String> list,
            ICallback callback) {
        super(context, theme);
        this.mContext = context;
        this.mList = list;
        this.mCallback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_spinner);
        initView();
        initValue();
        initEvent();
        setSize();
    }

    /**
     * 初始化各控件
     */
    private void initView() {
        mSpinnerListView = (ListView) this
                .findViewById(R.id.dialog_select_list);
    }

    /**
     * 初始化各控件的值
     */
    private void initValue() {
        mDialogselectadapter = new SpinnerDialogAdapter(mContext, mList);
        mSpinnerListView.setAdapter(mDialogselectadapter);
    }

    /**
     * 初始化各控件的事件
     */
    private void initEvent() {
        mSpinnerListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                if (mCallback != null) {
                    mCallback.onItemClick(position);
                } else {
                    Log.e(TAG, "Callback must not be null!");
                }
            }
        });
    }

    /**
     * 设置对话框的尺寸
     */
    private void setSize() {
        RelativeLayout.LayoutParams rllp = (LayoutParams) mSpinnerListView
                .getLayoutParams();
        WindowManager windowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        // 宽度为屏幕的80%
        rllp.width = (display.getWidth()) * 80 / 100;
        // 高度为 屏幕的80% 和 所有内容高度 中的最小值
        rllp.height = ((display.getHeight()) * 80 / 100) < (getTotalHeightofListView(mSpinnerListView)) ? ((display
                .getHeight()) * 80 / 100)
                : (getTotalHeightofListView(mSpinnerListView));
        rllp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        mSpinnerListView.setLayoutParams(rllp);
    }

    /**
     * 设置回调监听对象
     * 
     * @param callback
     */
    public void setCallback(ICallback callback) {
        this.mCallback = callback;
    }
    
    /**
     * 设置显示的数据
     * @param lists
     */
    public void setData(List<String> lists) {
        this.mList = lists;
    }

    /**
     * 计算ListView的高度
     * 
     * @param listView
     * @return
     */
    public int getTotalHeightofListView(ListView listView) {
        Adapter adapter = listView.getAdapter();
        if (adapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View view = adapter.getView(i, null, listView);
            view.measure(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            totalHeight += view.getMeasuredHeight();
        }
        totalHeight = totalHeight
                + (listView.getDividerHeight() * (adapter.getCount() - 1));
        return totalHeight;
    }
}