package cn.hjf.gollumaccount.activity;

import cn.hjf.gollumaccount.FragmentIdConsts;
import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.activity.MainActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * 消费统计选择界面
 * 
 * @author huangjinfu
 * 
 */
public class StatisticSelectActivity extends BaseActivity {

    private Button mByItemButton;
    private Button mByMonthButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_analyse);
        initView();
        initValue();
        initEvent();
    }

    /**
     * 初始化各控件
     */
    @Override
    protected void initView() {
        mByItemButton = (Button) findViewById(R.id.btn_pie_by_item);
        mByMonthButton = (Button) findViewById(R.id.btn_line_by_month);
    }

    /**
     * 初始化各控件的值
     */
    @Override
    protected void initValue() {
    }

    /**
     * 初始化各控件的事件
     */
    @Override
    protected void initEvent() {
        mByItemButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticSelectActivity.this, TypeStatisticActivity.class);
                StatisticSelectActivity.this.startActivity(intent);
            }
        });

        mByMonthButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticSelectActivity.this, MonthStatisticActivity.class);
                StatisticSelectActivity.this.startActivity(intent);
            }
        });
    }
}
