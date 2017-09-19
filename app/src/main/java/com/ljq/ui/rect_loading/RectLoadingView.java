package com.ljq.ui.rect_loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.ljq.ui.R;

/**
 * 四方块加载移动view
 * Created by 刘镓旗 on 2017/9/7.
 */

public class RectLoadingView extends View {

    private int mRect_Space;
    private int mRect_Size;
    private Paint mPaint;
    private int mRect_Color;
    private Rect mRect_1;
    private Rect mRect_2;
    private Rect mRect_4;
    private Rect mRect_3;
    private boolean mAnimationStart = false;
    private ValueAnimator mAnimator;

    public RectLoadingView(Context context) {
        this(context, null);
    }

    public RectLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    //初始化
    private void init(Context context, AttributeSet attrs) {
        //获取属性
        TypedArray typedArray = context.getResources().obtainAttributes(attrs, R.styleable.RectLoadingView);

        int attrsCount = typedArray.getIndexCount();

        for (int i = 0; i < attrsCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.RectLoadingView_rect_space:
                    mRect_Space = typedArray.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.RectLoadingView_rect_size:
                    mRect_Size = typedArray.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.RectLoadingView_rect_color:
                    mRect_Color = typedArray.getColor(attr, Color.parseColor("#619820"));
                    break;
            }
        }
        //释放
        typedArray.recycle();
        //创建画笔
        mPaint = new Paint();
        mPaint.setColor(mRect_Color);
        mPaint.setAntiAlias(true);

//        初始状态
        mRect_1 = new Rect(0, 0, mRect_Size, mRect_Size);

        mRect_2 = new Rect(0,
                mRect_Size + mRect_Space,
                mRect_Size,
                mRect_Size * 2 + mRect_Space);

        mRect_3 = new Rect(mRect_Size + mRect_Space,
                mRect_Size + mRect_Space,
                mRect_Size*2 + mRect_Space,
                mRect_Size * 2 + mRect_Space);

        mRect_4 = new Rect(mRect_Size + mRect_Space,
                mRect_Size * 2 + mRect_Space*2,
                mRect_Size * 2 + mRect_Space,
                mRect_Size * 3 + mRect_Space * 2);



    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //自己的宽度 = 两个方块+上间距
        int width = mRect_Size * 2 + mRect_Space;
        //高度是3个方块+两个间距
        int height = mRect_Size * 3 + mRect_Space * 2;

        //如果是固定的则使用，不固定使用自己的
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width,
                heightMode == MeasureSpec.EXACTLY ? heightSize : height);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!mAnimationStart){
            mAnimationStart = true;
            startLoadingAnimation();
        }
        canvas.drawRect(mRect_1, mPaint);
        canvas.drawRect(mRect_2, mPaint);
        canvas.drawRect(mRect_3, mPaint);
        canvas.drawRect(mRect_4, mPaint);

    }

    public void startLoadingAnimation() {

        Log.e("RectLoading","开启动画");
        if(mAnimator == null){
            mAnimator = ValueAnimator.ofObject(new RectEvaluator(mRect_Size,mRect_Space),0,1);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    RectLoadingMode rectLoadingMode = (RectLoadingMode) animation.getAnimatedValue();
                    mRect_1 = rectLoadingMode.rect_1;
                    mRect_2 = rectLoadingMode.rect_2;
                    mRect_3 = rectLoadingMode.rect_3;
                    mRect_4 = rectLoadingMode.rect_4;
                    invalidate();
                }
            });
            mAnimator.setDuration(1500);
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        }
        if(!mAnimator.isRunning()){
            mAnimator.start();
        }

    }

    //关闭动画
    public void stopLoadingAnimation(){
        if(mAnimator != null && mAnimator.isRunning()){
            mAnimator.cancel();
        }
    }

}
