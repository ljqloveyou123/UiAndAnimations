package com.ljq.ui.greedy_snake;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;

import com.ljq.ui.R;

import java.lang.ref.WeakReference;

/**
 * 贪食蛇view
 * Created by 刘镓旗 on 2017/9/7.
 */

public class GreedySnakeCustomView extends View {
    private int mItemSize;
    private Paint mPaint;
    private Rect mCurrentRect;
    private int mDrawIndex;
    private boolean mAnimationStart;
    private CustomHandler mHandler;

    private int[] mColorArray = new int[]{
            Color.parseColor("#00000000"),//1
            Color.parseColor("#00000000"),//2
            Color.parseColor("#E7F6E1"),//3
            Color.parseColor("#DAF0CE"),//4
            Color.parseColor("#C8EFB4"),//5
            Color.parseColor("#B8E8A0"),//6
            Color.parseColor("#A8DF8D"),//7
            Color.parseColor("#9BD67C"),//8
            Color.parseColor("#8ACB68"),//9
            Color.parseColor("#7CBF5A"),//10
            Color.parseColor("#70B54E"),//11
            Color.parseColor("#64A842"),//12
            Color.parseColor("#5C9F3B"),//13
            Color.parseColor("#539234"),//14
            Color.parseColor("#4C882E"),//15
            Color.parseColor("#47802A"),//16
            Color.parseColor("#417626"),//17
            Color.parseColor("#34601D"),//18
            Color.parseColor("#294D17"),//19
            Color.parseColor("#224013"),//20
    };
    private int mWidth;
    private int mHeight;
    private int mMeasuWidth;
    private int mMEasuHeight;
    private int mDrawXStart;
    private int mDrawYStart;
    private Animation.AnimationListener mListener;

    public GreedySnakeCustomView(Context context) {
        this(context, null);
    }

    public GreedySnakeCustomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GreedySnakeCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.getResources().obtainAttributes(attrs, R.styleable.GreedySnakeCustomView);
            //方块大小
            mItemSize = typedArray.getDimensionPixelSize(R.styleable.GreedySnakeCustomView_rect_size_,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 30, getResources().getDisplayMetrics()));
            int id = typedArray.getResourceId(R.styleable.GreedySnakeCustomView_colors, R.array.custom_rect_colors);
            //方块颜色数组
            mColorArray = context.getResources().getIntArray(id);
            typedArray.recycle();
        }else {
            mItemSize = 10;
        }
        Log.e("itemsize = ","itemsize " + mItemSize);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mCurrentRect = new Rect(0, 0, mItemSize, mItemSize);
        mDrawIndex = 1;
        mHandler = new CustomHandler(this);

    }


    public void setItemSize(int itemSize) {
        mItemSize = itemSize;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int drawTag = mDrawIndex;
        for (int i = 0; i < mColorArray.length; i++) {
            if (i <= 5) {
                //第一行
                drawTop(i);
            } else if (i > 5 && i <= 10) {
                //右侧竖着
                drawRight(i);
            } else if (i > 10 && i <= 15) {
                //底部
                drawBottom(i);
            } else {
//                左侧
                drawLeft(i);
            }

            if (drawTag > 0) {
                int index = mColorArray.length - drawTag;
                mPaint.setColor(mColorArray[index]);
                drawTag--;
            } else {
                mPaint.setColor(mColorArray[i - mDrawIndex]);
            }

            canvas.drawRect(mCurrentRect, mPaint);
        }
        startAnimation();
    }

    private void drawLeft(int i) {
        mCurrentRect.left = mDrawXStart;
        mCurrentRect.right = mItemSize + mDrawXStart;
        mCurrentRect.top = mItemSize * (20 - i) + mDrawYStart;
        mCurrentRect.bottom = mItemSize * (21 - i) + mDrawYStart;
    }

    private void drawBottom(int i) {
        mCurrentRect.top = mItemSize * 5 + mDrawYStart;
        mCurrentRect.bottom = mItemSize * 6 + mDrawYStart;
        mCurrentRect.left = mItemSize * (15 - i) + mDrawXStart;
        mCurrentRect.right = mItemSize * (16 - i) + mDrawXStart;
    }

    private void drawRight(int i) {
        mCurrentRect.left = mItemSize * 5 + mDrawXStart;
        mCurrentRect.right = mItemSize * 6 + mDrawXStart;
        mCurrentRect.top = mItemSize * (i - 5) + mDrawYStart;
        mCurrentRect.bottom = mItemSize * (i - 4) + mDrawYStart;
    }


    private void drawTop(int i) {
        mCurrentRect.top = mDrawYStart;
        mCurrentRect.bottom = mItemSize + mDrawYStart;
        mCurrentRect.left = mItemSize * i + mDrawXStart;
        mCurrentRect.right = mItemSize * (i + 1) + mDrawXStart;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        mMeasuWidth = mItemSize * 6;
        mMEasuHeight = mItemSize * 6;
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : mMeasuWidth,
                heightMode == MeasureSpec.EXACTLY ? heightSize : mMEasuHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mCurrentRect = new Rect(0, 0, mItemSize, mItemSize);

        mDrawXStart = mWidth / 2 - mMeasuWidth / 2;
        mDrawYStart = mHeight / 2 - mMEasuHeight / 2;
        Log.e("mDrawXStart", "mDrawXStart 2 = " + mDrawXStart);
        Log.e("mDrawYStart", "mDrawYStart 2 = " + mDrawYStart);
    }

    public void startAnimation() {
        if (!mAnimationStart) {
            if (mHandler != null) {
                mAnimationStart = true;
                mHandler.sendEmptyMessageDelayed(0, 50);
                Log.e("发送消息了","发送消息了");
            }
        }
    }

    public void stopAnimation() {
        if (mAnimationStart) {
            if (mHandler != null) {
                mAnimationStart = false;
                mHandler.removeCallbacksAndMessages(null);
            }
        }
    }


    private static class CustomHandler extends Handler {
        private WeakReference<GreedySnakeCustomView> reference;

        private CustomHandler(GreedySnakeCustomView view) {
            reference = new WeakReference<GreedySnakeCustomView>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            GreedySnakeCustomView view = reference.get();
            if (view != null) {
                if (view.mDrawIndex == 20) {
                    view.mDrawIndex = 1;
                } else {
                    view.mDrawIndex++;
                }
                view.invalidate();
                sendEmptyMessageDelayed(0, 50);
            }
        }
    }


    public float limitValue(float a, float b) {
        float valve = 0;
        final float min = Math.min(a, b);
        final float max = Math.max(a, b);
        valve = valve > min ? valve : min;
        valve = valve < max ? valve : max;
        return valve;
    }

    public void setAnimationListener(Animation.AnimationListener listener) {
        mListener = listener;
    }

    @Override
    public void onAnimationStart() {
        super.onAnimationStart();
        if (mListener != null) {
            mListener.onAnimationStart(getAnimation());
        }
    }

    @Override
    public void onAnimationEnd() {
        super.onAnimationEnd();
        if (mListener != null) {
            mListener.onAnimationEnd(getAnimation());
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
