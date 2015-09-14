package cn.hjf.gollumaccount.view;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 九宫格视图
 * @author huangjinfu
 *
 */
public class LockView extends View {
    
    private static final int RADIUS_OUTER   = 100; //大圆半径
    private static final int RADIUS_INNER   = 90; //小圆半径
    private static final int RADIUS_CENTER  = 10; //圆心半径
    
    private static final int COLOR_NORMAL = 0xFF0099CC; //格子正常颜色
    private static final int COLOR_NORMAL_HIGHLIGHT = 0x880099CC; //格子正常颜色高亮
    private static final int COLOR_FAIL = 0xFFFF0000; //格子验证失败的颜色
    private static final int COLOR_BACKGROUD = 0xFFFFFFFF; //背景颜色
    
    private Paint mBackGroudPaint; //画格子的背景
    private Paint mNormalPaint; //画格子的大圆和小圆
    private Paint mHighlightPaint; //画高亮的格子
    
    private Circle[][] mCircles; //3x3的数组，放置9各格子
    private Point mCurrentPoint; //当前触摸位置
    private int mGap; //格子的间距
    private List<Circle> mSelectedCircles; //已经选择的格子
    private Vibrator mVibrator; //振动器
    private OnInputListener mListener; //输入结果监听器
    
    public interface OnInputListener {
        public abstract void OnInputCompleted(Position[] inputResult);
    }

    public LockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public LockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LockView(Context context) {
        super(context);
        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        
        mBackGroudPaint = new Paint();
        mBackGroudPaint.setAntiAlias(true);
        mBackGroudPaint.setColor(COLOR_BACKGROUD);
        
        mNormalPaint = new Paint();
        mNormalPaint.setAntiAlias(true);
        mNormalPaint.setColor(COLOR_NORMAL);
        
        mHighlightPaint = new Paint();
        mHighlightPaint.setAntiAlias(true);
        mHighlightPaint.setStrokeWidth(10f);
        mHighlightPaint.setColor(COLOR_NORMAL_HIGHLIGHT);
        
        mCircles = new Circle[3][3];
        mCurrentPoint = new Point();
        mSelectedCircles = new ArrayList<Circle>();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            //重置状态
            reset();
            //记录当前触摸位置
            mCurrentPoint.x = (int) event.getX();
            mCurrentPoint.y = (int) event.getY();
            //如果没有触摸到九宫格，返回，不处理后续事件
            Circle downPoint = getCircle(event);
            if (downPoint == null) {
                return false;
            }
            //选择触摸到的格子
            select(downPoint);
            break;
        case MotionEvent.ACTION_MOVE:
            //记录当前触摸位置
            mCurrentPoint.x = (int) event.getX();
            mCurrentPoint.y = (int) event.getY();
            //如果触碰到格子，选择触摸到的格子
            Circle downPoint1 = getCircle(event);
            if (downPoint1 != null) {
                select(downPoint1);
            }
            invalidate();
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
            //记录当前触摸位置
            mCurrentPoint.x = (int) event.getX();
            mCurrentPoint.y = (int) event.getY();
            //把输入返回给调用者
            if (mListener != null) {
                Position[] positions = new Position[mSelectedCircles.size()];
                for (int i = 0; i < mSelectedCircles.size(); i++) {
                    positions[i] = mSelectedCircles.get(i).position;
                }
                mListener.OnInputCompleted(positions);
            }
            //把最后结果呈现给出来
            drawCircles(COLOR_NORMAL_HIGHLIGHT);
            break;
        default:
            break;
        }
        return true;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawARGB(255, 255, 255, 255);
        computeGap();
        computePoint();
        drawDynamic(canvas);
    }
    
    /**
     * 选择某一个格子
     * @param circle
     */
    private void select(Circle circle) {
        if (!mSelectedCircles.contains(circle)) {
            mVibrator.vibrate(50);
            mSelectedCircles.add(circle);
            invalidate();
        }
        
    }
    
    /**
     * 重置状态
     */
    private void reset() {
        mBackGroudPaint.setColor(COLOR_BACKGROUD);
        mNormalPaint.setColor(COLOR_NORMAL);
        mHighlightPaint.setColor(COLOR_NORMAL_HIGHLIGHT);
        mSelectedCircles.clear();
        invalidate();
    }
    
    /**
     * 用指定的颜色绘制已经选择的圆
     * @param color
     */
    private void drawCircles(int color) {
        mHighlightPaint.setColor(color);
        if (mSelectedCircles.size() != 0) {
            mCurrentPoint.x = mSelectedCircles.get(mSelectedCircles.size() - 1).center.x;
            mCurrentPoint.y = mSelectedCircles.get(mSelectedCircles.size() - 1).center.y;
        }
        invalidate();
    }
    
    /**
     * 设置结果监听器
     * @param onInputListener
     */
    public void setOnInputListener(OnInputListener onInputListener) {
        this.mListener = onInputListener;
    }
    
    /**
     * 用指定的颜色绘制指定的圆
     * @param positions
     * @param color
     */
    public void drawCircles(Position[] positions, int color) {
        mHighlightPaint.setColor(color);
        mSelectedCircles.clear();
        for (int k = 0; k < positions.length; k++) {
            for (int i = 0; i < mCircles.length; i++) {
                for (int j = 0; j < mCircles[i].length; j++) {
                    if (positions[k].equals(mCircles[i][j].position) ) {
                        mSelectedCircles.add(mCircles[i][j]);
                    }
                }
            }
        }
        drawCircles(color);
    }
    
    /**
     * 计算格子之间的距离
     */
    private void computeGap() {
        mGap = getWidth() / 4;
    }
    
    /**
     * 计算每个格子的原点坐标
     */
    private void computePoint() {
        int marginTop = (getHeight() - mGap * 2) / 2;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                mCircles[i][j] = new Circle(new Position(i, j), new Point(mGap * (j + 1), marginTop));
            }
            marginTop += mGap;
        }
    }
    
    /**
     * 主要的绘制方法,负责绘制格子和线条
     * @param canvas
     */
    private void drawDynamic(Canvas canvas) {
        for (int i = 0; i < mCircles.length; i++) {
            for (int j = 0; j < mCircles[i].length; j++) {
                if (mSelectedCircles.contains(mCircles[i][j])) {
                    canvas.drawCircle(mCircles[i][j].center.x, mCircles[i][j].center.y, RADIUS_OUTER, mHighlightPaint);
                    canvas.drawCircle(mCircles[i][j].center.x, mCircles[i][j].center.y, RADIUS_INNER, mBackGroudPaint);
                    canvas.drawCircle(mCircles[i][j].center.x, mCircles[i][j].center.y, RADIUS_CENTER, mHighlightPaint);
                } else {
                    canvas.drawCircle(mCircles[i][j].center.x, mCircles[i][j].center.y, RADIUS_OUTER, mNormalPaint);
                    canvas.drawCircle(mCircles[i][j].center.x, mCircles[i][j].center.y, RADIUS_INNER, mBackGroudPaint);
                    canvas.drawCircle(mCircles[i][j].center.x, mCircles[i][j].center.y, RADIUS_CENTER, mNormalPaint);
                }
            }
        }
        
        if (mSelectedCircles.size() == 0) {
            return;
        }
        
        if (mSelectedCircles.size() == 1) {
            canvas.drawLine(mSelectedCircles.get(mSelectedCircles.size() - 1).center.x, mSelectedCircles.get(mSelectedCircles.size() - 1).center.y, 
                    mCurrentPoint.x, mCurrentPoint.y, mHighlightPaint);
        } else {
            for (int i = 0; i < mSelectedCircles.size() - 1; i++) {
                canvas.drawLine(mSelectedCircles.get(i).center.x, mSelectedCircles.get(i).center.y, 
                        mSelectedCircles.get(i + 1).center.x, mSelectedCircles.get(i + 1).center.y, mHighlightPaint);
            }
            canvas.drawLine(mSelectedCircles.get(mSelectedCircles.size() - 1).center.x, mSelectedCircles.get(mSelectedCircles.size() - 1).center.y, 
                    mCurrentPoint.x, mCurrentPoint.y, mHighlightPaint);
        }
        
        mHighlightPaint.setColor(COLOR_NORMAL_HIGHLIGHT);
    }
    
    /**
     * 如果当前触摸位置在某个格子中，就返回这个格子
     * @param event
     * @return
     */
    private Circle getCircle(MotionEvent event) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if( (event.getX() <= mCircles[i][j].center.x + RADIUS_OUTER) && (event.getX() >= mCircles[i][j].center.x - RADIUS_OUTER) ) {
                    if ((event.getY() <= mCircles[i][j].center.y + RADIUS_OUTER) && (event.getY() >= mCircles[i][j].center.y - RADIUS_OUTER)) {
                        return mCircles[i][j];
                    }
                }
            }
        }
        return null;
    }
    
    
    /**
     * 格子
     * @author huangjinfu
     */
    private class Circle {
        Position position;
        Point center;
        public Circle (Position position, Point center) {
            this.position = position;
            this.center = center;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Circle)) {
                return false;
            }
            Circle d = (Circle) o;
            if (this.position.equals(d.position) && this.center.equals(d.center)) {
                return true;
            } else {
                return false;
            }
            
        }
    }
    
    /**
     * 位置
     * @author huangjinfu
     */
    public class Position {
        public int row;
        public int column;
        public Position (int row, int column) {
            this.row = row;
            this.column = column;
        }
        @Override
        public String toString() {
            return "row : " + row + ", column : " + column;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Position)) {
                return false;
            }
            Position d = (Position) o;
            if (this.row == d.row && this.column == d.column) {
                return true;
            } else {
                return false;
            }
            
        }
    }
}
