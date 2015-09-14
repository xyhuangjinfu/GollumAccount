package cn.hjf.gollumaccount.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class LockView extends View {
    
    private Paint mPaint;
    
    private Point[][] mPoints;
    
    private Point mCurrentPoint;
    
    private int mGap;
    
    private List<Point> mSelectedPoint;
    
    private Vibrator mVibrator;
    
    private Context mContext;
    
    private OnInputListener mListener;
    
    public interface OnInputListener {
        public abstract void OnInputCompleted(String inputResult);
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

    private void init(Context context) {
        mContext = context;
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPoints = new Point[3][3];
        mCurrentPoint = new Point();
        mSelectedPoint = new ArrayList<Point>();
    }
    
    public void setOnInputListener(OnInputListener onInputListener) {
        this.mListener = onInputListener;
    }
    
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            
            mCurrentPoint.x = (int) event.getX();
            mCurrentPoint.y = (int) event.getY();
            
            Point downPoint = isInCircle(event);
            if (downPoint == null) {
                return false;
            }
            
            mSelectedPoint.clear();
            invalidate();
            
            select(downPoint);
            
            
            break;
            
        case MotionEvent.ACTION_MOVE:
            Log.i("O_O", "ACTION_MOVE");
            mCurrentPoint.x = (int) event.getX();
            mCurrentPoint.y = (int) event.getY();
            
            Point downPoint1 = isInCircle(event);
            if (downPoint1 != null) {
                select(downPoint1);
            }
            
            drawLine(event);
            

            
            
            
            
            
            break;
            
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
            Log.d("O_O", "ACTION_UP ACTION_CANCEL");
            mCurrentPoint.x = (int) event.getX();
            mCurrentPoint.y = (int) event.getY();
            
            
            
            
            mListener.OnInputCompleted("xxx");
            reset();
            break;

        default:
            break;
        }
        return true;
    }
    
    
    
    @Override
    protected void onDraw(Canvas canvas) {
        Log.e("O_O", "onDraw");
        canvas.drawARGB(255, 255, 255, 255);
        computeGap();
        computePoint();
        drawPoint(canvas);
    }
    
    
    private void select(Point point) {
        if (!mSelectedPoint.contains(point)) {
            mVibrator.vibrate(50);
            mSelectedPoint.add(point);
            Log.i("O_O", "add : " + point.toString());
            invalidate();
        }
        
    }
    
    private void reset() {
//        mSelectedPoint.clear();
//        postInvalidateDelayed(2000);
        if (mSelectedPoint.size() != 0) {
            mCurrentPoint.x = mSelectedPoint.get(mSelectedPoint.size() - 1).x;
            mCurrentPoint.y = mSelectedPoint.get(mSelectedPoint.size() - 1).y;
        }
        invalidate();
    }
    
    
    private void computeGap() {
        mGap = getWidth() / 4;
    }
    
    private void computePoint() {
        int marginTop = (getHeight() - mGap * 2) / 2;
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                mPoints[i][j] = new Point(mGap * (j + 1), marginTop);
            }
            marginTop += mGap;
        }
    }
    
    private void drawPoint(Canvas canvas) {
        //0xFF9933CC - selected
        //0xff0099cc - normal
        mPaint.setColor(0xFF9933CC);
        for (int i = 0; i < mPoints.length; i++) {
            for (int j = 0; j < mPoints[i].length; j++) {
                if (mSelectedPoint.contains(mPoints[i][j])) {
                    mPaint.setColor(0xFF9933CC);
                    canvas.drawCircle(mPoints[i][j].x, mPoints[i][j].y, 100, mPaint);
                    mPaint.setColor(0xffffffff);
                    canvas.drawCircle(mPoints[i][j].x, mPoints[i][j].y, 90, mPaint);
                    mPaint.setColor(0xFF9933CC);
                    canvas.drawCircle(mPoints[i][j].x, mPoints[i][j].y, 10, mPaint);
                } else {
                    mPaint.setColor(0xff0099cc);
                    canvas.drawCircle(mPoints[i][j].x, mPoints[i][j].y, 100, mPaint);
                    mPaint.setColor(0xffffffff);
                    canvas.drawCircle(mPoints[i][j].x, mPoints[i][j].y, 90, mPaint);
                    mPaint.setColor(0xff0099cc);
                    canvas.drawCircle(mPoints[i][j].x, mPoints[i][j].y, 10, mPaint);
                }
            }
        }
        
        
        if (mSelectedPoint.size() == 0) {
            return;
        }
        
        
        mPaint.setColor(0xFF9933CC);
        mPaint.setStrokeWidth(10f);
        if (mSelectedPoint.size() == 1) {
//          Log.i("O_O", "== 1 mSelectedPoint : " + mSelectedPoint.get(mSelectedPoint.size() - 1).toString());
//          Log.i("O_O", "== 1 mCurrentPoint : " + mCurrentPoint.toString());
            canvas.drawLine(mSelectedPoint.get(mSelectedPoint.size() - 1).x, mSelectedPoint.get(mSelectedPoint.size() - 1).y, 
                    mCurrentPoint.x, mCurrentPoint.y, mPaint);
        } else {
//          Log.i("O_O", ">= 2 mSelectedPoint : " + mSelectedPoint.get(mSelectedPoint.size() - 1).toString());
//          Log.i("O_O", ">= 2 mCurrentPoint : " + mCurrentPoint.toString());
            for (int i = 0; i < mSelectedPoint.size() - 1; i++) {
                canvas.drawLine(mSelectedPoint.get(i).x, mSelectedPoint.get(i).y, 
                        mSelectedPoint.get(i + 1).x, mSelectedPoint.get(i + 1).y, mPaint);
            }
            canvas.drawLine(mSelectedPoint.get(mSelectedPoint.size() - 1).x, mSelectedPoint.get(mSelectedPoint.size() - 1).y, 
                    mCurrentPoint.x, mCurrentPoint.y, mPaint);
        }
    }
    
    private void drawLine(MotionEvent event) {
        mCurrentPoint.x = (int) event.getX();
        mCurrentPoint.y = (int) event.getY();
        invalidate();
    }
    
    
    private Point isInCircle(MotionEvent event) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if( (event.getX() <= mPoints[i][j].x + 100) && (event.getX() >= mPoints[i][j].x - 100) ) {
                    if ((event.getY() <= mPoints[i][j].y + 100) && (event.getY() >= mPoints[i][j].y - 100)) {
                        return mPoints[i][j];
                    }
                }
            }
        }
        return null;
    }
    
}
