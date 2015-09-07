package cn.hjf.gollumaccount.view;

import cn.hjf.gollumaccount.R;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 自定义下拉刷新ListView
 * @author xfujohn
 *
 */
public class PullListView extends RelativeLayout {
    
    private static final String TAG = "PullListView";
    
    private ListView mListView;
    private View mFooterView; //底部加载视图
    private LinearLayout mFooterViewLayout; //底部加载视图布局
    private Scroller mScroller;
    private GestureDetector mGestureDetector;
    private ScrollStatus mStatus = ScrollStatus.SCROLL; 
    
    private RefreshMode mRefreshMode;
    
    private float mLastMotionY = 0;
    
    private OnRefreshListener mOnRefreshListener;
    
//    private int mCurrentScrollDistance;
    
    private enum ScrollStatus {
        REFRESH_DOWN, //下拉刷新
        REFRESH_UP, //上拉刷新
        SCROLL //ListView滚动
    }
    
    private enum RefreshMode {
        UP, //上拉
        DOWN, //下拉
        BOTH //上下拉
    }
    
    public interface OnRefreshListener {
        public abstract void onPullUpRefresh();
        public abstract void onPullDownRefresh();
    }

    public PullListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mListView = new ListView(context, attrs, defStyle);
        mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mListView.setSelector(R.color.transparent);
        mListView.setDividerHeight(3);
        
        mScroller = new Scroller(context);
        mGestureDetector = new GestureDetector(context, new CustomGestureListener());
        
        mRefreshMode = RefreshMode.UP;
        
//        this.setBackgroundResource(R.color.transparent);
//        this.setOrientation(LinearLayout.VERTICAL);
        
        //绑定footerView，加载视图
        mFooterView = LayoutInflater.from(context).inflate(R.layout.view_footer_loading, null);
        mFooterViewLayout = new LinearLayout(context);
        mFooterViewLayout.addView(mFooterView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mListView.addFooterView(mFooterViewLayout);
        mFooterView.setVisibility(View.GONE);
        
        addView(mListView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        
        
        mFooterView = LayoutInflater.from(context).inflate(R.layout.view_footer_loading, null);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        mFooterView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        lp.bottomMargin = -mFooterView.getMeasuredHeight();
        mFooterView.setLayoutParams(lp);
        addView(mFooterView);
    }

    public PullListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullListView(Context context) {
        this(context, null, 0);
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.i("O_O", "Intercept mStatus : " + mStatus);
        boolean result = false;
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
//            mCurrentScrollDistance = 0;
            mLastMotionY = event.getY();
            result = mGestureDetector.onTouchEvent(event);
            break;
        case MotionEvent.ACTION_MOVE:
            computeStatus();
            //下拉
            if (event.getY() - mLastMotionY > 0) {
                if (mStatus == ScrollStatus.REFRESH_DOWN) {
                    if (mRefreshMode == RefreshMode.UP) {
                        result = false;
                    } else {
                        result = true;
                    }
                }
                else {
                    result = false;
                }
            } 
            //上拉
            else if (event.getY() - mLastMotionY < 0) {
                if (mStatus == ScrollStatus.REFRESH_UP) {
                    result = true;
                }
                else {
                    result = false;
                }
            }
            
            mLastMotionY = event.getY();
            
            
            break;
        default:
            result = false;
            break;
        }
        return result;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.e("O_O", "mStatus : " + mStatus);
        super.onTouchEvent(event);
        boolean result = false;
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            result = false;
            break;
        case MotionEvent.ACTION_MOVE:
            result = mGestureDetector.onTouchEvent(event);
          break;
        case MotionEvent.ACTION_UP :
            Log.i("O_O", "mScroller.getFinalY() : " +  mScroller.getFinalY());
            if (mScroller.getFinalY() >= mFooterView.getMeasuredHeight()) {
                smoothScrollTo(0, mFooterView.getMeasuredHeight());
                mOnRefreshListener.onPullUpRefresh();
            } else {
                smoothScrollTo(0, 0);
            }
            break;
        default:
            break;
//            return mGestureDetector.onTouchEvent(event);
        }
        return result;
    }
    
    //调用此方法滚动到目标位置
    public void smoothScrollTo(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(dx, dy);
    }
    
    //调用此方法设置滚动的相对偏移
    public void smoothScrollBy(int dx, int dy) {
        //设置mScroller的滚动偏移量
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);
        invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
    }
    
    /**
     * 计算当前状态
     */
    private void computeStatus() {
        Log.d("O_O", "---");
        Log.i("O_O", "mListView.getLastVisiblePosition() : " + mListView.getLastVisiblePosition());
        Log.i("O_O", "mListView.getAdapter().getCount() - 1 : " + (mListView.getAdapter().getCount() - 1));
        Log.i("O_O", "mListView.getChildAt(last).getBottom() : " + mListView.getChildAt(mListView.getLastVisiblePosition() - mListView.getFirstVisiblePosition()).getBottom());
        Log.i("O_O", "mListView.getBottom() : " + mListView.getBottom());
        if (mListView.getFirstVisiblePosition() == 0 && mListView.getChildAt(0).getTop() == 0) {
            mStatus = ScrollStatus.REFRESH_DOWN;
        } else if (mListView.getLastVisiblePosition() == mListView.getAdapter().getCount() - 1 
                && mListView.getChildAt(mListView.getLastVisiblePosition() - mListView.getFirstVisiblePosition()).getBottom() == mListView.getBottom()) {
            mStatus = ScrollStatus.REFRESH_UP;
        } else {
            mStatus = ScrollStatus.SCROLL;
        }
    }
    
    public void reset() {
        smoothScrollTo(0, 0);
    }
    
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.mOnRefreshListener = onRefreshListener;
    }
    
    @Override
    public void computeScroll() {
        //先判断mScroller滚动是否完成
        if (mScroller.computeScrollOffset()) {
        
            //这里调用View的scrollTo()完成实际的滚动
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //必须调用该方法，否则不一定能看到滚动效果
            postInvalidate();
        } else {
        }
        super.computeScroll();
    }
    
    public void setAdapter(ListAdapter adapter) {
        mListView.setAdapter(adapter);
    }
    
    public void setEmptyView(View emptyView) {
        mListView.setEmptyView(emptyView);
    }
    
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListView.setOnItemClickListener(listener);
    }

    /**
     * 手势监听器
     * @author xfujohn
     *
     */
    class CustomGestureListener implements GestureDetector.OnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
//            Log.d("O_O", "onDown : ");
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
            int dis = (int)((distanceY-0.5)/2);
//            mCurrentScrollDistance = mCurrentScrollDistance + Math.abs(dis);
            smoothScrollBy(0, dis);
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
