package cn.hjf.gollumaccount.view;

import cn.hjf.gollumaccount.R;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
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
    private View mHeaderView; //顶部加载视图
    private LinearLayout mFooterViewLayout; //底部加载视图布局
    private Scroller mScroller;
    private GestureDetector mGestureDetector;
//    private ScrollStatus mStatus = ScrollStatus.SCROLL; 
    
//    private RefreshMode mRefreshMode;
    
    private float mLastMotionY = 0;
    
    private OnRefreshListener mOnRefreshListener;
    
    private static final int ALIGN_TO_TOP = 0x00000001;
    private static final int ALIGN_TO_TOP_MASK = 0x00000001;
    private static final int ALIGN_TO_BOTTOM = 0x00000002;
    private static final int ALIGN_TO_BOTTOM_MASK = 0x00000002;
    private static final int ALIGN_TO_CENTER = 0x00000004;
    private static final int ALIGN_TO_CENTER_MASK = 0x00000004;
    private int mListViewStatusFlag = 0;
    
    private static final int PULL_DOWN = 0x00000001;
    private static final int PULL_DOWN_MASK = 0x00000001;
    private static final int PULL_UP = 0x00000002;
    private static final int PULL_UP_MASK = 0x00000002;
    private static final int PULL_BOTH = 0x00000003;
    private static final int PULL_BOTH_MASK = 0x00000003;
    private int mPullModeFlag = 0;
    
    private RefreshStatus mRefreshStatus;
    
//    private enum ScrollStatus {
//        REFRESH_DOWN, //下拉刷新
//        REFRESH_UP, //上拉刷新
//        SCROLL //ListView滚动
//    }
    
    private enum RefreshStatus {
        UP, //上拉刷新
        DOWN //下拉刷新
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
        mListView.setDividerHeight(2);
//        float px = (float) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
//        Log.i("O_O", "px : " + px);
//        mListView.setDividerHeight(px);
        
        mScroller = new Scroller(context);
        mGestureDetector = new GestureDetector(context, new CustomGestureListener());
        
//        mRefreshMode = RefreshMode.UP;
        
        mPullModeFlag = PULL_UP;
        
//        this.setBackgroundResource(R.color.transparent);
//        this.setOrientation(LinearLayout.VERTICAL);
        
        //绑定footerView，加载视图
//        mFooterView = LayoutInflater.from(context).inflate(R.layout.view_footer_loading, null);
//        mFooterViewLayout = new LinearLayout(context);
//        mFooterViewLayout.addView(mFooterView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//        mListView.addFooterView(mFooterViewLayout);
//        mFooterView.setVisibility(View.GONE);
        
        addView(mListView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        
      //绑定footerView，加载视图
        mFooterView = LayoutInflater.from(context).inflate(R.layout.view_footer_loading, null);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//        mFooterView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//        lp.bottomMargin = -mFooterView.getMeasuredHeight();
        mFooterView.setLayoutParams(lp);
        
        mFooterView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        RelativeLayout.LayoutParams lp1 = (LayoutParams) mFooterView.getLayoutParams();
        lp1.bottomMargin = -mFooterView.getMeasuredHeight();
        mFooterView.setLayoutParams(lp1);
        
        addView(mFooterView);
        
        //绑定headerView，加载视图
        mHeaderView = LayoutInflater.from(context).inflate(R.layout.view_footer_loading, null);
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        mHeaderView.setLayoutParams(lp2);
        
        mHeaderView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        RelativeLayout.LayoutParams lp3 = (LayoutParams) mHeaderView.getLayoutParams();
        lp3.topMargin = -mHeaderView.getMeasuredHeight();
        mHeaderView.setLayoutParams(lp3);
        
        addView(mHeaderView);
    }

    public PullListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullListView(Context context) {
        this(context, null, 0);
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.i("O_O", "Intercept mStatus : " );
        boolean result = false;
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            mLastMotionY = event.getY();
            result = mGestureDetector.onTouchEvent(event);
            break;
        case MotionEvent.ACTION_MOVE:
            
            computeStatus();
            //当前为下拉动作
            if (event.getY() - mLastMotionY > 0) {
                //ListView处于顶端(ListView滑动到最上面)
                if ((mListViewStatusFlag & ALIGN_TO_TOP_MASK) == ALIGN_TO_TOP) {
                    //Pull模式允许下拉，拦截事件，执行下拉操作
                    if ((mPullModeFlag & PULL_DOWN_MASK) == PULL_DOWN) {
                        result = true;
                    }
                }
            } 
            //当前为上拉动作
            else if (event.getY() - mLastMotionY < 0) {
                //ListView处于底端(ListView滑动到最下面)
                if ((mListViewStatusFlag & ALIGN_TO_BOTTOM_MASK) == ALIGN_TO_BOTTOM) {
                    //Pull模式允许上拉，拦截事件，执行上拉操作
                    if ((mPullModeFlag & PULL_UP_MASK) == PULL_UP) {
                        result = true;
                    }
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
            result = true;
            break;
        case MotionEvent.ACTION_MOVE:
            computeStatus();
            //当前为下拉动作
            if (event.getY() - mLastMotionY > 0) {
                //ListView处于顶端(ListView滑动到最上面)
                if ((mListViewStatusFlag & ALIGN_TO_TOP_MASK) == ALIGN_TO_TOP) {
                    //Pull模式允许下拉，拦截事件，执行下拉操作
                    if ((mPullModeFlag & PULL_DOWN_MASK) == PULL_DOWN) {
                        result = mGestureDetector.onTouchEvent(event);
                    }
                }
            } 
            //当前为上拉动作
            else if (event.getY() - mLastMotionY < 0) {
                //ListView处于底端(ListView滑动到最下面)
                if ((mListViewStatusFlag & ALIGN_TO_BOTTOM_MASK) == ALIGN_TO_BOTTOM) {
                    //Pull模式允许上拉，拦截事件，执行上拉操作
                    if ((mPullModeFlag & PULL_UP_MASK) == PULL_UP) {
                        result = mGestureDetector.onTouchEvent(event);
                    }
                }
            }
//            result = mGestureDetector.onTouchEvent(event);
          break;
        case MotionEvent.ACTION_UP :
            //上拉刷新
            if (mScroller.getFinalY() >= mFooterView.getMeasuredHeight()) {
                smoothScrollTo(0, mFooterView.getMeasuredHeight());
                mOnRefreshListener.onPullUpRefresh();
            } 
            //下拉刷新
            else if (mScroller.getFinalY() <= -mHeaderView.getMeasuredHeight()) {
                smoothScrollTo(0, -mHeaderView.getMeasuredHeight());
                mOnRefreshListener.onPullDownRefresh();
            }
            //没有刷新
            else {
                smoothScrollTo(0, 0);
            }
            break;
        default:
            break;
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
        mListViewStatusFlag = 0;
        Log.d("O_O", "---");
        Log.i("O_O", "mListView.getLastVisiblePosition() : " + mListView.getLastVisiblePosition());
        Log.i("O_O", "mListView.getAdapter().getCount() - 1 : " + (mListView.getAdapter().getCount() - 1));
        Log.i("O_O", "mListView.getChildAt(last).getBottom() : " + mListView.getChildAt(mListView.getLastVisiblePosition() - mListView.getFirstVisiblePosition()).getBottom());
        Log.i("O_O", "mListView.getBottom() : " + mListView.getBottom());
        if (mListView.getFirstVisiblePosition() == 0 && mListView.getChildAt(0).getTop() == 0) {
//            mStatus = ScrollStatus.REFRESH_DOWN;
            mListViewStatusFlag = mListViewStatusFlag | ALIGN_TO_TOP;
        } else if (mListView.getLastVisiblePosition() == mListView.getAdapter().getCount() - 1 
                && mListView.getChildAt(mListView.getLastVisiblePosition() - mListView.getFirstVisiblePosition()).getBottom() == mListView.getBottom()) {
//            mStatus = ScrollStatus.REFRESH_UP;
            mListViewStatusFlag = mListViewStatusFlag | ALIGN_TO_BOTTOM;
        } else {
//            mStatus = ScrollStatus.SCROLL;
            mListViewStatusFlag = mListViewStatusFlag | ALIGN_TO_CENTER;
        }
//        Log.i("O_O", "mListViewStatusFlag : " + mListViewStatusFlag);
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
