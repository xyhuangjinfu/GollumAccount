package cn.hjf.gollumaccount.view;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.businessmodel.ConsumeRecord;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.Toast;

public class SwipeListView extends ListView {
    
    private float mLastX;
    private float mLastY;
    
    private View mMotionView;
    private View mLastMotionView;
    
    private Button mDeleteButton;
    
    private SwipeStatus mStatus;
    
    private ConsumeRecord mConsumeRecord;
    
    private Scroller mScroller;
    
    private int mOffset;
    
    private MyThread mThread = new MyThread("jiankong");
    
    
    private enum SwipeStatus {
        NONE,
        SWIPE
    }

    public SwipeListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScroller = new Scroller(context);
    }

    public SwipeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    public SwipeListView(Context context) {
        super(context);
        mScroller = new Scroller(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.i("O_O", "ListView : " + event.getAction());
        
        
        boolean result = false;
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            
            
            if (mStatus == SwipeStatus.SWIPE) {
                
                post(new MyRunnable());
                Log.i("O_O", "post");
//                post(new MyRunnable());
                
                mScroller.startScroll(mMotionView.getScrollX(), 0, 0, 0);
                mStatus = SwipeStatus.NONE;
            }
            
//            mMotionView = null;
            
            
            result = super.onTouchEvent(event);
            mLastX = event.getX();
            mLastY = event.getY();
            break;
        case MotionEvent.ACTION_MOVE:
            //判断当前是左右滑动还是上下滑动，左右滑动，对上层屏蔽touch事件
            float deltax = Math.abs(event.getX() - mLastX);
            float delaty = Math.abs(event.getY() - mLastY);
            
            if (deltax - 0 > delaty ) { //左右
                
                result = true;
                // 计算左右侧滑偏移量
                float delta = mLastX - event.getX();
                mLastX = event.getX();
                mLastY = event.getY();
                
                if (mMotionView == null) {
                    final int motionPosition = pointToPosition((int)mLastX, (int)mLastY);
                    mMotionView = this.getChildAt(motionPosition - getFirstVisiblePosition());
                }
                
                
                
                
                if (mDeleteButton == null) {
                    mDeleteButton = (Button) mMotionView.findViewById(R.id.btn_delete);
                    
                    if (mOffset == 0) {
                        mOffset = mDeleteButton.getWidth();
                    }
                    
                    mDeleteButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            mConsumeRecord = (ConsumeRecord) SwipeListView.this.getItemAtPosition(motionPosition - getFirstVisiblePosition());
                            
                            if (mDeleteButton.getTag() == null) {
                                Log.i("O_O", "mDeleteButton.getTag() == null");
                            }
                            
                            Toast.makeText(getContext(), (String)mDeleteButton.getTag(), 0).show();
                        }
                    });
                }

                // 向左侧滑
                if (delta >= 0) {
                    mStatus = SwipeStatus.SWIPE;
                    if (mMotionView != null) {
                        mMotionView.setPressed(false);
                        if (mMotionView.getScrollX() < mDeleteButton.getWidth()) {
                            if (mMotionView.getScrollX() + (int)delta > mOffset) {
                                delta = mDeleteButton.getWidth() - mMotionView.getScrollX();
                            }
                            mMotionView.scrollBy((int)delta, 0);
                        }
                    }
                } else { // 向右侧滑
                    if (mMotionView != null) {
                        mMotionView.setPressed(false);
                        if (mMotionView.getScrollX() > 0) {
                            if (mMotionView.getScrollX() < -(int)delta) {
                                mMotionView.scrollBy(mMotionView.getScrollX(), 0);
                            } else {
                                mMotionView.scrollBy((int)delta, 0);
                            }
                        }
                    }
                }
                
            } else { //上下
                reset();
                result = super.onTouchEvent(event);
            }
            break;
        default:
            reset();
            
            if (mStatus == SwipeStatus.SWIPE) {
                result = true;
            } else {
                result = super.onTouchEvent(event);
            }
            break;
        }
        return result;
    }
    
    private void reset() {
        if (mMotionView != null) {
            if (mMotionView.getScrollX() >= mDeleteButton.getWidth() / 2) {
                mMotionView.scrollTo(mOffset, 0);
                mStatus = SwipeStatus.SWIPE;
            } else {
                mMotionView.scrollTo(0, 0);
                mStatus = SwipeStatus.NONE;
            }
        }
    }
    
    
    
    
    private class MyRunnable implements Runnable {
        @Override
        public void run() {
            Log.i("O_O", "run");
            if (mScroller.computeScrollOffset()) {
                if (mMotionView != null) {
                    Log.i("O_O", "scrollTo : " + mScroller.getCurrX());
                    Log.i("O_O", "scrollTo : " + mScroller.getFinalX());
                    mMotionView.scrollTo(mScroller.getCurrX(), 0);
                    postDelayed(new MyRunnable(), 1000);
                }
            } else {
              mMotionView = null;
            }
        }
    }
    
    
    
    private class MyThread extends Thread {
        public MyThread(String threadName) {
            super(threadName);
        }
        @Override
        public void run() {
            while (true) {
                if (mScroller != null && mScroller.computeScrollOffset()) {
                    if (mMotionView != null) {
                        mMotionView.scrollBy(mScroller.getCurrX(), 0);
                    }
                }
            }
        }
    }
}
