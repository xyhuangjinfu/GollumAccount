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
    private SwipeStatus mDownStatus;
    
    private ConsumeRecord mConsumeRecord;
    
    private Scroller mScroller;
    
    private int mOffset;
    
    private GestureDetector mGestureDetector;
    
    private OnViewClickListener mListener;
    
    private PressRunnable mPressRunnable;
    
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
        mDownStatus = mStatus;
        mScroller = new Scroller(context);
        mGestureDetector = new GestureDetector(context, new MyGestureListener());
    }
    
    private class PressRunnable implements Runnable {
        @Override
        public void run() {
            if (mMotionView != null) {
                mMotionView.setPressed(true);
            }
        }
        
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            mLastX = event.getX();
            mLastY = event.getY();
            mDownStatus = mStatus;
            //如果当前有某个item处于SWIPE状态，那么，还原状态，不处理后续事件。
            if (mStatus == SwipeStatus.SWIPED) {
                smoothScrollTo(0, 0);
                return false;
            }
            //如果当前有某个item处于SWIPING状态，空处理掉事件，不处理后续事件。
            if (mStatus == SwipeStatus.SWIPING_MANUAL || mStatus == SwipeStatus.SWIPING_AUTO) {
                return false;
            }
            
            findMotionView(event);
            
            if (mPressRunnable == null) {
                mPressRunnable = new PressRunnable();
            }
            
            postDelayed(mPressRunnable, 100);
            return true;
//            break;
        case MotionEvent.ACTION_MOVE:
            
            //判断当前是左右滑动还是上下滑动，左右滑动，对上层屏蔽touch事件
            float deltax = Math.abs(event.getX() - mLastX);
            float deltay = Math.abs(event.getY() - mLastY);
            
            // 计算左右侧滑偏移量
            int dx = (int) (mLastX - event.getX());
            int dy = (int) (mLastY - event.getY());
            mLastX = event.getX();
            mLastY = event.getY();
            findMotionView(event);
            if (mMotionView == null) {
                return true;
            }
            mMotionView.setPressed(false);
            if (deltax > deltay + 3 ) { //左右
                //左滑
                if (dx >= 3) {
                    dx = dx > (mOffset - mScroller.getFinalX()) ? (mOffset - mScroller.getFinalX()) : dx;
                    mScroller.setFinalX(mScroller.getFinalX() + dx);
                    mStatus = SwipeStatus.SWIPING_MANUAL;
                    mMotionView.scrollBy(dx, 0);
                    mMotionView.setPressed(false);
                    if (mMotionView.getScrollX() == mOffset) {
                        mStatus = SwipeStatus.SWIPED;
                    } else if (mMotionView.getScrollX() == 0) {
                        mStatus = SwipeStatus.NONE;
                    }
                }
                //右滑
                else if (dx <= -3) {
                    dx = -dx > mScroller.getFinalX() ? -mScroller.getFinalX() : dx;
                    mScroller.setFinalX(mScroller.getFinalX() + dx);
                    mStatus = SwipeStatus.SWIPING_MANUAL;
                    mMotionView.scrollBy(dx, 0);
                    mMotionView.setPressed(false);
                    if (mMotionView.getScrollX() == mOffset) {
                        mStatus = SwipeStatus.SWIPED;
                    } else if (mMotionView.getScrollX() == 0) {
                        mStatus = SwipeStatus.NONE;
                    }
                }
                return true;
            } else {
                if (mStatus != SwipeStatus.NONE) {
                    return true;
                }
            }
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
            removeCallbacks(mPressRunnable);
            //如果不是初始状态，修正位置，处理掉该事件。
            if (!mStatus.equals(SwipeStatus.NONE)) {
                fixPosition();
                return true;
            } else {
                mMotionView = null;
            }
            break;
        default:
            break;
        }
      
        return super.onTouchEvent(event);
    }
    
    /**
     * 根据触摸位置找到当前的MotionView
     */
    private void findMotionView(MotionEvent event) {
        //得到当前操作的对象
        if (mMotionView == null) {
            
            final int motionPosition = pointToPosition((int)event.getX(), (int)event.getY());
            mMotionView = this.getChildAt(motionPosition - getFirstVisiblePosition());
            if (mMotionView == null) {
                return;
            }
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
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mMotionView != null) {
                    mMotionView.setPressed(false);
                }
            }
        }, 200);
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
