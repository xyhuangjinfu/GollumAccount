package cn.hjf.gollumaccount.view;

import cn.hjf.gollumaccount.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
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
    
    private ListView mListView; //内部包含的真实ListView
    private View mFooterView; //底部加载视图
    private View mHeaderView; //顶部加载视图
    private Scroller mScroller; //滚动控制器
    private GestureDetector mGestureDetector; //手势监听器
    
    private float mLastMotionY = 0; //上一个触摸事件的Y位置
    
    private OnRefreshListener mOnRefreshListener; //刷新事件监听器
    
    /**
     * 内部ListView当前的状态，判断当前动作是滑动ListView还是Pull时使用。
     * 如果当前状态为 ALIGN_TO_TOP ，除法下滑的事件，如果 {@link #mPullModeFlag} 允许下拉，
     * 就会进行下拉，而不是继续滑动ListView，（此时应该是 overScroll 状态）
     * 参考：{@link #computeStatus()}
     */
    private static final int ALIGN_TO_TOP = 0x00000001; //滑动到最顶部
    private static final int ALIGN_TO_TOP_MASK = 0x00000001; //判断是否在最顶部的Mask
    private static final int ALIGN_TO_BOTTOM = 0x00000002; //滑动到最底部
    private static final int ALIGN_TO_BOTTOM_MASK = 0x00000002; //判断是否在最底部的Mask
    private static final int ALIGN_TO_CENTER = 0x00000004; //滑动到中间位置(既不时最顶部，也不是最底部)
    private static final int ALIGN_TO_CENTER_MASK = 0x00000004; //判断是否在中间的Mask
    private int mListViewStatusFlag = 0; //内部ListView当前的状态
    
    /**
     * 当前ListView可以Pull的模式
     */
    public static final int PULL_DOWN = 0x00000001; //下拉模式
    private static final int PULL_DOWN_MASK = 0x00000001; //判断是否可以下拉的Mask
    public static final int PULL_UP = 0x00000002; //上拉模式
    private static final int PULL_UP_MASK = 0x00000002; //判断是否可以上拉的Mask
    public static final int PULL_BOTH = 0x00000003; //上下拉模式(上拉下拉都可以)
    private static final int PULL_BOTH_MASK = 0x00000003; //判断是否可以上下拉的Mask
    public static final int PULL_NONE = 0x00000004; //不可上拉下拉模式
    private int mPullModeFlag = 0; //当前ListView可以Pull的模式

    /**
     * 当前控件内部的状态，上拉,下拉,无动作
     * {@link PullMode}
     */
    private PullMode mPullMode;
    
    /**
     * 触发上下拉成功后，当前的pull模式
     * @author huangjinfu
     *
     */
    public enum PullMode {
        NONE, //没有动作
        UP, //上拉刷新
        DOWN //下拉刷新
    }
    
    /**
     * Pull事件监听器
     * @author huangjinfu
     *
     */
    public interface OnRefreshListener {
        /**
         * 上拉事件
         */
        public abstract void onPullUpRefresh();
        /**
         * 下拉事件
         */
        public abstract void onPullDownRefresh();
    }

    public PullListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mListView = new ListView(context);
        mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mListView.setSelector(R.color.transparent);
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setDividerHeight(2);
        
        addView(mListView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        
        //加载footerView，加载视图
        mFooterView = LayoutInflater.from(context).inflate(R.layout.view_footer_loading, null);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        mFooterView.setLayoutParams(lp);
        
        mFooterView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        RelativeLayout.LayoutParams lp1 = (LayoutParams) mFooterView.getLayoutParams();
        lp1.bottomMargin = -mFooterView.getMeasuredHeight();
        mFooterView.setLayoutParams(lp1);
        
        addView(mFooterView);
        
        //加载headerView，加载视图
        mHeaderView = LayoutInflater.from(context).inflate(R.layout.view_footer_loading, null);
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        mHeaderView.setLayoutParams(lp2);
        
        mHeaderView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        RelativeLayout.LayoutParams lp3 = (LayoutParams) mHeaderView.getLayoutParams();
        lp3.topMargin = -mHeaderView.getMeasuredHeight();
        mHeaderView.setLayoutParams(lp3);
        
        addView(mHeaderView);
        
        //实例化滚动控制器和手势监听器
        mScroller = new Scroller(context);
        mGestureDetector = new GestureDetector(context, new InternalGestureListener());
        
        //设置初始Pull模式,默认不可Pull
        mPullModeFlag = PULL_NONE;
    }

    public PullListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullListView(Context context) {
        this(context, null, 0);
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
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean result = false;
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            
            mLastMotionY = event.getY();
            //把事件交给手势监听器处理，防止出现事件跳跃问题(onScroll时，第一次的distance会很大)。
            result = mGestureDetector.onTouchEvent(event);
            
            break;
        case MotionEvent.ACTION_MOVE:
            
            //计算内部ListView状态
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
            //记录上一次触摸位置
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
        super.onTouchEvent(event);
        boolean result = false;
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            result = true;
            break;
        case MotionEvent.ACTION_MOVE:
            
            //计算内部ListView状态
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
            
          break;
        case MotionEvent.ACTION_UP :
            
            //上拉动作
            if (mScroller.getFinalY() >= mFooterView.getMeasuredHeight() && mPullMode != PullMode.UP) {
                mPullMode = PullMode.UP;
                smoothScrollTo(0, mFooterView.getMeasuredHeight());
                mOnRefreshListener.onPullUpRefresh();
            } 
            //下拉动作
            else if (mScroller.getFinalY() <= -mHeaderView.getMeasuredHeight() && mPullMode != PullMode.DOWN) {
                mPullMode = PullMode.DOWN;
                smoothScrollTo(0, -mHeaderView.getMeasuredHeight());
                mOnRefreshListener.onPullDownRefresh();
            }
            //还在上拉状态
            else if (mPullMode == PullMode.UP) {
                smoothScrollTo(0, mFooterView.getMeasuredHeight());
            }
            //还在下拉状态
            else if (mPullMode == PullMode.DOWN) {
                smoothScrollTo(0, -mHeaderView.getMeasuredHeight());
            }
            //没有动作
            else {
                smoothScrollTo(0, 0);
            }
            
            break;
        default:
            break;
        }
        return result;
    }
    
    /**
     * 调用此方法滚动到目标位置
     * @param fx
     * @param fy
     */
    public void smoothScrollTo(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(dx, dy);
    }
    
    /**
     * 调用此方法设置滚动的相对偏移
     * @param dx
     * @param dy
     */
    public void smoothScrollBy(int dx, int dy) {
        //设置mScroller的滚动偏移量
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);
        //这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
        invalidate();
    }
    
    /**
     * 计算内部ListView当前的状态
     * 参考：{@link #ALIGN_TO_TOP} {@link #ALIGN_TO_BOTTOM} {@link #ALIGN_TO_CENTER}
     */
    private void computeStatus() {
        mListViewStatusFlag = 0;
        //当前ListView没有数据
        if (mListView.getAdapter() == null || mListView.getAdapter().getCount() == 0) {
            mListViewStatusFlag = mListViewStatusFlag | ALIGN_TO_TOP | ALIGN_TO_BOTTOM;
            return;
        }
        //第一个可见View的位置为0，并且第一个可见View的顶部也为0，可以判断状态为 ALIGN_TO_TOP
        if (mListView.getFirstVisiblePosition() == 0 && mListView.getChildAt(0).getTop() == 0) {
            mListViewStatusFlag = mListViewStatusFlag | ALIGN_TO_TOP;
        }
        //最后一个可见View的位置为数据中的最后一个，并且最后一个可见View的底部跟ListView的底部相等，可以判断状态为 ALIGN_TO_BOTTOM
        if (mListView.getLastVisiblePosition() == mListView.getAdapter().getCount() - 1 
                && mListView.getChildAt(mListView.getLastVisiblePosition() - mListView.getFirstVisiblePosition()).getBottom() == mListView.getBottom()) {
            mListViewStatusFlag = mListViewStatusFlag | ALIGN_TO_BOTTOM;
        }
    }
    
    /**
     * 重置控件状态，隐藏footerView或者headerView
     */
    public void reset() {
        smoothScrollTo(0, 0);
        mPullMode = PullMode.NONE;
    }
    
    /**
     * 设置监听器
     * @param onRefreshListener
     */
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
        }
        super.computeScroll();
    }
    
    /**
     * 设置数据适配器
     * @param adapter
     */
    public void setAdapter(ListAdapter adapter) {
        mListView.setAdapter(adapter);
    }
    
    /**
     * 设置空视图
     * @param emptyView
     */
    public void setEmptyView(View emptyView) {
        mListView.setEmptyView(emptyView);
    }
    
    /**
     * 设置数据项click事件监听器
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListView.setOnItemClickListener(listener);
    }
    
    /**
     * 设置控件支持的Pull模式
     * 参考：{@link #PULL_DOWN} {@link #PULL_UP} {@link #PULL_BOTH} {@link #PULL_NONE}
     * @param pullMode
     */
    public void setPullMode(int pullMode) {
        this.mPullModeFlag = pullMode;
    }
    
    /**
     * 设置当前状态为Pull状态
     * @param pullMode
     */
    public void setPull(PullMode pullMode) {
        this.mPullMode = pullMode;
        if (mPullMode == PullMode.UP) {
            smoothScrollTo(0, mFooterView.getMeasuredHeight());
        } else if (mPullMode == PullMode.DOWN) {
            smoothScrollTo(0, -mHeaderView.getMeasuredHeight());
        }
    }

    /**
     * 手势监听器,用来滚动控件，达到Pull的效果
     * @author huangjinfu
     *
     */
    private class InternalGestureListener implements GestureDetector.OnGestureListener {
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
            int dis = (int)((distanceY-0.5)/2);
            smoothScrollBy(0, dis);
            return true;
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
