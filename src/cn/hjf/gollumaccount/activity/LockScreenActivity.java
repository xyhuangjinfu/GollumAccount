package cn.hjf.gollumaccount.activity;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.util.Md5;
import cn.hjf.gollumaccount.util.SharedPreferencesUtil;
import cn.hjf.gollumaccount.view.LockView;
import cn.hjf.gollumaccount.view.LockView.OnInputListener;
import cn.hjf.gollumaccount.view.LockView.Position;
import android.R.color;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

public class LockScreenActivity extends BaseActivity {
    
    private static final String PW = "6B1DCB1217F37815251CA42F38886F2A";
    
    private LockView mLockView;
    
    private String mPassword;
    
    private Position[] mPositions;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        
        initView();
        initValue();
        initEvent();
        
        
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        mPassword = SharedPreferencesUtil.getSharedPreferences(this).getString("password", null);
    }

    @Override
    protected void initView() {
        mLockView = (LockView) findViewById(R.id.v_lock);
    }

    @Override
    protected void initValue() {
    }

    @Override
    protected void initEvent() {
        mLockView.setOnInputListener(new OnInputListener() {
            @Override
            public void OnInputCompleted(Position[] inputResult) {
                mPositions = inputResult;
                if (PW.equals(getPassword(inputResult))) {
                    startActivity(new Intent(LockScreenActivity.this, MainActivity.class));
                    finish();
                } else {
                    mLockView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLockView.drawCircles(mPositions, Color.RED);
                        }
                    }, 1000);
                }
            }
        });
    }
    
    private String getPassword(Position[] inputResult) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mPositions.length; i++) {
            sb.append(mPositions[i].row);
            sb.append(mPositions[i].column);
        }
        return Md5.md5(sb.toString());
    }

    
    @Override
    protected boolean needFinishAnimate() {
        return false;
    }
    
}
