package cn.hjf.gollumaccount.view;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.businessmodel.ConsumeRecord;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
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
    
    private GestureDetector mGestureDetector;
    
    private OnViewClickListener mListener;
    
    public interface OnViewClickListener {
        public abstract void onViewClick(int viewId, int position);
    }
    
    /**
     * 当前的Swipe状态
     * @author huangjinfu
     */
    private enum SwipeStatus {
        NONE , //起始位置，无动作，getScrollX() == 0
        SWIPED , //已经滑动到目标位置，getScrollX() == mOffset
        SWIPING_MANUAL , //手动滑动中
        SWIPING_AUTO //自动滑动中，Scroler控制
    }

    public SwipeListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public SwipeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SwipeListView(Context context) {
        super(context);
        init(context);
    }
    
    public void setOnViewClickListener(OnViewClickListener listener) {
        this.mListener = listener;
    }
    
    private void init(Context context) {
        mStatus = SwipeStatus.NONE;
        mScroller = new Scroller(context);
        mGestureDetector = new GestureDetector(context, new MyGestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = false;
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            Log.e("O_O", "down");
            mLastX = event.getX();
            mLastY = event.getY();
            
            
            /**
             * 如果当前有某个item处于SWIPE状态，那么，还原状态。
             */
            if (mStatus == SwipeStatus.SWIPED) {
                smoothScrollTo(0, 0);
                result = true;
            } else {
                result = super.onTouchEvent(event);
            }
            
            break;
        case MotionEvent.ACTION_MOVE:
            
            if (mStatus == SwipeStatus.SWIPING_AUTO) {
                return true;
            }
            
            Log.i("O_O", "move");
            //判断当前是左右滑动还是上下滑动，左右滑动，对上层屏蔽touch事件
            float deltax = Math.abs(event.getX() - mLastX);
            float deltay = Math.abs(event.getY() - mLastY);
            if (deltax > deltay + 3 ) { //左右
                
                result = true;
                // 计算左右侧滑偏移量
                int delta = (int) (mLastX - event.getX());
                mLastX = event.getX();
                mLastY = event.getY();
                findMotionView();
                //左滑
                if (delta >= 3) {
                    delta = delta > (mOffset - mScroller.getFinalX()) ? (mOffset - mScroller.getFinalX()) : delta;
                }
                //右滑
                else if (delta <= -3) {
                    delta = -delta > mScroller.getFinalX() ? -mScroller.getFinalX() : delta;
                }
                mScroller.setFinalX(mScroller.getFinalX() + delta);
                mStatus = SwipeStatus.SWIPING_MANUAL;
                mMotionView.scrollBy(delta, 0);
                mMotionView.setPressed(false);
                if (mMotionView.getScrollX() == mOffset) {
                    mStatus = SwipeStatus.SWIPED;
                } else if (mMotionView.getScrollX() == 0) {
                    mStatus = SwipeStatus.NONE;
                }
                
                
                
                
            } else { //上下，交给父类处理，滚动ListView
                if (mStatus == SwipeStatus.NONE) {
                    Log.w("O_O", "上下滑动");
                    result = super.onTouchEvent(event);
                }
            }
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
            Log.e("O_O", "up cancel");
            fixPosition();
            if (mStatus == SwipeStatus.NONE) {
                result = super.onTouchEvent(event);
            } else {
                mMotionView.setPressed(false);
                result = true;
            }
            break;
        default:
            break;
        }
      
      
      
        return true;
    }
    
    /**
     * 根据触摸位置找到当前的MotionView
     */
    private void findMotionView() {
        //得到当前操作的对象
        if (mMotionView == null) {
            final int motionPosition = pointToPosition((int)mLastX, (int)mLastY);
            mMotionView = this.getChildAt(motionPosition - getFirstVisiblePosition());
            mDeleteButton = (Button) mMotionView.findViewById(R.id.btn_delete);
            if (mOffset == 0) {
                mOffset = mDeleteButton.getWidth();
            }
            mDeleteButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDeleteButton.getTag() != null) {
                        if (mDeleteButton.getTag() != null && mListener != null) {
                            smoothScrollTo(0, 0);
                            mListener.onViewClick(mDeleteButton.getId(), (int)mDeleteButton.getTag());
                        }
                    }
                }
            });
        }
    }
    
    /**
     * 修正位置。
     * 如果隐藏View已经显示超过一半，就显示该View。
     * 如果隐藏View已经显示还没有超过一半，就隐藏该View。
     */
    private void fixPosition() {
        if (mMotionView == null) {
            return;
        }
        //显示过半，并且状态不是swipe
        if (mMotionView.getScrollX() >= mOffset / 2 && mStatus == SwipeStatus.SWIPING_MANUAL) {
            smoothScrollTo(mOffset, 0);
        }
        //显示不过半，并且状态不是swipe
        else if (mMotionView.getScrollX() < mOffset / 2 && mStatus == SwipeStatus.SWIPING_MANUAL) {
            smoothScrollTo(0, 0);
        }
    }
    
    /**
     * 平滑滚动到某处
     * @param destX 目标x
     * @param destY 目标y
     */
    private void smoothScrollTo(int destX, int destY) {
        mStatus = SwipeStatus.SWIPING_AUTO;
        int time = Math.abs(destX - mScroller.getFinalX()) * 250 / mOffset;
        mScroller.abortAnimation();
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), destX - mScroller.getFinalX(), mScroller.getFinalY() - destY, time);
        post(new SmoothScrollRunnable());
    }
    
    /**
     * 用来平滑滑动的Runnable对象
     * @author huangjinfu
     *
     */
    private class SmoothScrollRunnable implements Runnable {
        @Override
        public void run() {
            /**
             * 如果Scroller还在滚动，则更新MotionView的位置，否则，说明已经MotionView已经回到原来的位置，把状态置为NONE
             * 然后清空上一次的MotionView
             */
            if (mScroller.computeScrollOffset()) {
                if (mMotionView != null) {
                    mMotionView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
                    post(new SmoothScrollRunnable());
                }
            } else {
                if (mScroller.getFinalX() == 0) {
                    mStatus = SwipeStatus.NONE;
                    mMotionView = null;
                } else if (mScroller.getFinalX() == mOffset) {
                    mStatus = SwipeStatus.SWIPED;
                }
                
            }
        }
        
    }
    
    
    
    
    
    private class MyGestureListener implements GestureDetector.OnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }
        @Override
        public void onShowPress(MotionEvent e) {
            
        }
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                float distanceX, float distanceY) {
            return false;
        }
        @Override
        public void onLongPress(MotionEvent e) {
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY) {
            return false;
        }
        
    }
    
}
