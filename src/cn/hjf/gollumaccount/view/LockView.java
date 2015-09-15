package cn.hjf.gollumaccount.view;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 九宫格视图
 * @author huangjinfu
 *
 */
public class LockView extends View {
    
    private static final int RADIUS_OUTER   = 100; //大圆半径
    private static final int RADIUS_INNER   = 95; //小圆半径
    private static final int RADIUS_CENTER  = 5; //圆心半径
    
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
    private Runnable mResetRunnable; //状态重置的runnable对象，用来取消状态重置
    
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
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mBackGroudPaint = new Paint();
        mBackGroudPaint.setAntiAlias(true);
        mBackGroudPaint.setColor(COLOR_BACKGROUD);
        
        mNormalPaint = new Paint();
        mNormalPaint.setAntiAlias(true);
        mNormalPaint.setColor(COLOR_NORMAL);
        
        mHighlightPaint = new Paint();
        mHighlightPaint.setAntiAlias(true);
        mHighlightPaint.setStrokeWidth(5f);
        mHighlightPaint.setColor(COLOR_NORMAL_HIGHLIGHT);
        
        mCircles = new Circle[3][3];
        mCurrentPoint = new Point();
        mSelectedCircles = new ArrayList<Circle>();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.d("O_O", "event.getAction() : " + event.getAction());
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            //重置状态
            reset();
            removeCallbacks(mResetRunnable);
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
            Log.i("O_O",  "ACTION_UP");
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
        case MotionEvent.ACTION_CANCEL:
            Log.e("O_O", "ACTION_CANCEL");
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
     * 重置状态
     */
    public void resetDelayed(long delayMillis) {
        mResetRunnable = new Runnable() {
            @Override
            public void run() {
                reset();
            }
        };
        postDelayed(mResetRunnable, delayMillis);
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
        mGap = getWidth() * 3 / 10;
    }
    
    /**
     * 计算每个格子的原点坐标
     */
    private void computePoint() {
        int marginTop = (getHeight() - mGap * 2) / 2;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                mCircles[i][j] = new Circle(new Position(i, j), new Point( (getWidth() - 2 * mGap)/2 + j * mGap , marginTop));
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
                drawDirection(canvas, mSelectedCircles.get(i), mSelectedCircles.get(i+1));
            }
            canvas.drawLine(mSelectedCircles.get(mSelectedCircles.size() - 1).center.x, mSelectedCircles.get(mSelectedCircles.size() - 1).center.y, 
                    mCurrentPoint.x, mCurrentPoint.y, mHighlightPaint);
        }
        
        mHighlightPaint.setColor(COLOR_NORMAL_HIGHLIGHT);
    }
    
    /**
     * 画两个格子之间的方向指示器
     * @param canvas
     * @param start
     * @param end
     */
    private void drawDirection(Canvas canvas, Circle start, Circle end) {
        Point rightAnglePoint;
        Point midpointOfBottom;
        
        //水平
        if (start.center.y == end.center.y) {
            
            if (start.center.x > end.center.x) {
                midpointOfBottom = new Point(start.center.x - 20, start.center.y);
                rightAnglePoint = new Point(start.center.x - 40, start.center.y);
            } else {
                midpointOfBottom = new Point(start.center.x + 20, start.center.y);
                rightAnglePoint = new Point(start.center.x + 40, start.center.y);
            }
            
            drawTriangle(canvas, rightAnglePoint, midpointOfBottom);
            return;
        }
        //竖直
        if (start.center.x == end.center.x) {
            if (start.center.y > end.center.y) {
                midpointOfBottom = new Point(start.center.x, start.center.y - 20);
                rightAnglePoint = new Point(start.center.x, start.center.y - 40);
            } else {
                midpointOfBottom = new Point(start.center.x, start.center.y + 20);
                rightAnglePoint = new Point(start.center.x, start.center.y + 40);
            }
            
            drawTriangle(canvas, rightAnglePoint, midpointOfBottom);
            return;
        }
        //斜
        double dis = Math.sqrt(Math.pow(start.center.x - end.center.x, 2) + Math.pow(start.center.y - end.center.y, 2));
        double xg = end.center.x - start.center.x;
        double yg = end.center.y - start.center.y;
        midpointOfBottom = new Point((int)(start.center.x + xg / dis * 20), (int)(start.center.y + yg / dis * 20));
        rightAnglePoint = new Point((int)(start.center.x + xg / dis * 40), (int)(start.center.y + yg / dis * 40));
      
      drawTriangle(canvas, rightAnglePoint, midpointOfBottom);
        
    }
    
    /**
     * 根据直角顶点和底边中点，画直角三角形
     * @param canvas
     * @param rightAnglePoint
     * @param midpointOfBottom
     */
    private void drawTriangle(Canvas canvas, Point rightAnglePoint, Point midpointOfBottom) {
        //底边中点和直角点在水平线
        if (rightAnglePoint.y == midpointOfBottom.y) {
            int distance = Math.abs(rightAnglePoint.x - midpointOfBottom.x);
            Point p1 = new Point(midpointOfBottom.x, midpointOfBottom.y + distance);
            Point p2 = new Point(midpointOfBottom.x, midpointOfBottom.y - distance);
            
            Path p = new Path();
            p.moveTo(rightAnglePoint.x, rightAnglePoint.y);
            p.lineTo(p1.x, p1.y);
            p.lineTo(p2.x, p2.y);
            p.close();
            
            canvas.drawPath(p, mHighlightPaint);
            return;
        }
        //底边中点和直角点在垂直线
        if (rightAnglePoint.x == midpointOfBottom.x) {
            int distance = Math.abs(rightAnglePoint.y - midpointOfBottom.y);
            Point p1 = new Point(midpointOfBottom.x + distance, midpointOfBottom.y);
            Point p2 = new Point(midpointOfBottom.x - distance, midpointOfBottom.y);
            
            Path p = new Path();
            p.moveTo(rightAnglePoint.x, rightAnglePoint.y);
            p.lineTo(p1.x, p1.y);
            p.lineTo(p2.x, p2.y);
            p.close();
            
            canvas.drawPath(p, mHighlightPaint);
            return;
        }
        //底边中点和直角点不在水平和垂直线
        
        double gradient = (rightAnglePoint.y - midpointOfBottom.y) * 1.0 / (rightAnglePoint.x - midpointOfBottom.x);
        Log.i("O_O", "gradient : " + gradient);
        double diatance = Math.sqrt(Math.pow(rightAnglePoint.y - midpointOfBottom.y, 2) + Math.pow(rightAnglePoint.x - midpointOfBottom.x, 2));
        Log.i("O_O", "diatance : " + diatance);
        double angle = Math.toDegrees (Math.atan (gradient));
        Log.i("O_O", "angle : " + angle);
        
        if (angle > 0) {
            if (rightAnglePoint.y - midpointOfBottom.y > 0 && rightAnglePoint.x - midpointOfBottom.x > 0) {
                angle = angle - 90;
            } else if (rightAnglePoint.y - midpointOfBottom.y < 0 && rightAnglePoint.x - midpointOfBottom.x < 0) {
                angle = 90 + angle;
            }
        } else if (angle < 0) {
            if (rightAnglePoint.y - midpointOfBottom.y > 0 && rightAnglePoint.x - midpointOfBottom.x < 0) {
                angle = 90 + angle;
            } else if (rightAnglePoint.y - midpointOfBottom.y < 0 && rightAnglePoint.x - midpointOfBottom.x > 0) {
                angle = angle - 90;
            }
        }
        
        canvas.save();
        canvas.translate(midpointOfBottom.x, midpointOfBottom.y);
        canvas.rotate((float) angle);
        
        Point p1 = new Point((int)diatance, 0);
        Point p2 = new Point(-(int)diatance, 0);
        
        Path p = new Path();
        p.moveTo(0, (int)diatance);
        p.lineTo(p1.x, p1.y);
        p.lineTo(p2.x, p2.y);
        p.close();
        
        canvas.drawPath(p, mHighlightPaint);
        
        canvas.restore();
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
