package com.ljq.ui.konck;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.ljq.ui.R;


/**
 * 小球钟摆回弹
 * Created by刘镓旗 on 2017/3/28.
 */

public class KnockLoadingView extends View {
    private int mGlobeNum;//小球个数
    private int mGlobeRadius;//小球半径
    private int mGlobeColor;//小球颜色
    private int mSwingRadius;//钟摆半径
    private Paint mPaint;//画笔
    private Point mLeftPoint;//左边小球
    private Point mRightPoint;//右边小球
    private boolean isStart;//第一个动画是否启动
    private boolean isStart2;//第二个动画是否启动
    private boolean isStart3;//第三动画是否启动
    private boolean isStart4;//第四个动画是否启动
    private boolean mAnitionStart;//动画是否开始执行了
    private boolean mAnitionStop;//判断是否停止动画了

    //右边向下动画
    private ValueAnimator anim = ValueAnimator.ofObject(new TypeEvaluator() {
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            //参数fraction用于表示动画的完成度，我们根据它来计算当前的动画值
            fraction = 1 - fraction;
            double angle = Math.toRadians(90 * fraction);
            int x = (int) ((mSwingRadius - mGlobeRadius) * Math.sin(angle));
            int y = (int) ((mSwingRadius - mGlobeRadius) * Math.cos(angle));
            Point point = new Point(x, y);
            return point;
        }
    }, new Point(), new Point());

    //左边向上动画
    private ValueAnimator anim2 = ValueAnimator.ofObject(new TypeEvaluator() {
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            //参数fraction用于表示动画的完成度，我们根据它来计算当前的动画值
            double angle = Math.toRadians(90 * fraction);
            int x = (int) ((mSwingRadius - mGlobeRadius) * Math.sin(angle));
            int y = (int) ((mSwingRadius - mGlobeRadius) * Math.cos(angle));
            Point point = new Point(x, y);
            return point;
        }
    }, new Point(), new Point());


    //左边向下动画
    private ValueAnimator anim3 = ValueAnimator.ofObject(new TypeEvaluator() {
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            //参数fraction用于表示动画的完成度，我们根据它来计算当前的动画值
            fraction = 1 - fraction;
            double angle = Math.toRadians(90 * fraction);
            int x = (int) ((mSwingRadius - mGlobeRadius) * Math.sin(angle));
            int y = (int) ((mSwingRadius - mGlobeRadius) * Math.cos(angle));
            Point point = new Point(x, y);
            return point;
        }
    }, new Point(), new Point());

    private ValueAnimator anim4 = ValueAnimator.ofObject(new TypeEvaluator() {
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            //参数fraction用于表示动画的完成度，我们根据它来计算当前的动画值
            double angle = Math.toRadians(90 * fraction);
            int x = (int) ((mSwingRadius - mGlobeRadius) * Math.sin(angle));
            int y = (int) ((mSwingRadius - mGlobeRadius) * Math.cos(angle));
            Point point = new Point(x, y);
            return point;
        }
    }, new Point(), new Point());


    public KnockLoadingView(Context context) {
        this(context, null);
    }

    public KnockLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KnockLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //使用TypedArray读取自定义的属性值
        TypedArray ta = context.getResources().obtainAttributes(attrs, R.styleable.KnockLoadingView);
        //获取共有多少属性
        int count = ta.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = ta.getIndex(i);
            switch (attr) {
                case R.styleable.KnockLoadingView_globeNum://小球个数
                    mGlobeNum = ta.getInt(attr, 4);
                    break;
                case R.styleable.KnockLoadingView_globeRadius://小球半径
                    mGlobeRadius = ta.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.KnockLoadingView_globeColor://小球颜色
                    mGlobeColor = ta.getColor(attr, getContext().getResources().getColor(R.color.colorPrimary));
                    break;
                case R.styleable.KnockLoadingView_swingRadius://小球
                    mSwingRadius = ta.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 16, getResources().getDisplayMetrics()));
                    break;
            }
        }
        ta.recycle();  //避免下次读取时出现问题
        mPaint = new Paint();
        mPaint.setColor(mGlobeColor);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //高度为小球半径+摆动半径
        int height = mGlobeRadius + mSwingRadius;
        //宽度为2*摆动半径+（小球数量-1）*小球直径
        int width = mSwingRadius + mGlobeRadius * 2 * (mGlobeNum - 1) + mSwingRadius;
        //如果测量模式为EXACTLY，则直接使用推荐值，如不为EXACTLY（一般处理wrap_content情况），使用自己计算的宽高
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? widthSize : width, (heightMode == MeasureSpec.EXACTLY) ? heightSize : height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制除左右两个小球外的其他小球
        for (int i = 0; i < mGlobeNum - 2; i++) {
            switch (i) {
                case 0:
                    mPaint.setColor(getContext().getResources().getColor(R.color.colo2));
                    break;
                case 1:
                    mPaint.setColor(getContext().getResources().getColor(R.color.colo3));
                    break;
            }
            canvas.drawCircle(mSwingRadius + (i + 1) * 2 * mGlobeRadius, mSwingRadius, mGlobeRadius, mPaint);
        }
        if (mLeftPoint == null || mRightPoint == null) {
            //初始化最左右两小球坐标
            mLeftPoint = new Point(mSwingRadius, mSwingRadius);
            mRightPoint = new Point(mSwingRadius + (mGlobeNum - 1) * mGlobeRadius * 2 + (mSwingRadius - mGlobeRadius), mGlobeRadius);
            //开启摆动动画
            startPendulumAnimation();
        }
        //绘制左右两小球
        mPaint.setColor(getContext().getResources().getColor(R.color.colo1));
        canvas.drawCircle(mLeftPoint.x, mLeftPoint.y, mGlobeRadius, mPaint);
        mPaint.setColor(getContext().getResources().getColor(R.color.colo4));
        canvas.drawCircle(mRightPoint.x, mRightPoint.y, mGlobeRadius, mPaint);
    }

    public void startAnima() {
        //开启摆动动画
        if (mAnitionStop) {
            mAnitionStop = false;
            startPendulumAnimation();
        }

    }


    /**
     * 开启动画
     */
    public void startPendulumAnimation() {
        if(mAnitionStart)return;

        mAnitionStart = true;
        //使用属性动画
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mAnitionStop) return;
                Point point = (Point) animation.getAnimatedValue();
                mRightPoint.y = mSwingRadius;
                mRightPoint.x = mSwingRadius + (mGlobeNum - 1) * mGlobeRadius * 2 + point.x;
                mRightPoint.y = mGlobeRadius + point.y;
                invalidate();
                if (mRightPoint.y == mSwingRadius && !isStart) {
                    isStart = true;
                    anim2.start();
                } else if (mRightPoint.y == mSwingRadius - 1 && !isStart) {
                    isStart = true;
                    anim2.start();
                }
            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isStart = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.setDuration(800);
        //设置补间器，控制动画的变化速率
        anim.setInterpolator(new BounceInterpolator());
        anim.start();

        ///第二个
        //使用属性动画
        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mAnitionStop) return;
                Point point = (Point) animation.getAnimatedValue();
                mLeftPoint.x = mSwingRadius - point.x;
                mLeftPoint.y = mGlobeRadius + point.y;
                invalidate();
                if (mLeftPoint.y == mGlobeRadius && !isStart2) {
                    isStart2 = true;
                    anim3.start();
                } else if (mLeftPoint.y == mGlobeRadius - 1 && !isStart2) {
                    isStart2 = true;
                    anim3.start();
                }
            }
        });
        anim2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isStart2 = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim2.setDuration(500);
        //设置补间器，控制动画的变化速率
        anim2.setInterpolator(new DecelerateInterpolator());


        ///第三个
        //使用属性动画
        anim3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mAnitionStop) return;
                Point point = (Point) animation.getAnimatedValue();
                mLeftPoint.x = mSwingRadius - point.x;
                mLeftPoint.y = mGlobeRadius + point.y;
                invalidate();
                if (mLeftPoint.y == mSwingRadius && !isStart3 && !isStart) {
                    isStart3 = true;
                    anim4.start();
                } else if (mLeftPoint.y == mSwingRadius - 1 && !isStart3 && !isStart) {
                    isStart3 = true;
                    anim4.start();
                }
            }
        });
        anim3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                isStart3 = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim3.setDuration(800);
        //设置补间器，控制动画的变化速率
        anim3.setInterpolator(new BounceInterpolator());

        ///第四个
        //使用属性动画
        anim4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mAnitionStop) return;
                Point point = (Point) animation.getAnimatedValue();
                mRightPoint.y = mSwingRadius;
                mRightPoint.x = mSwingRadius + (mGlobeNum - 1) * mGlobeRadius * 2 + point.x;
                mRightPoint.y = mGlobeRadius + point.y;

                invalidate();
                if (mRightPoint.y == mGlobeRadius && !isStart4) {
                    isStart4 = true;
                    anim.start();
                } else if (mRightPoint.y == mGlobeRadius - 1 && !isStart4) {
                    isStart4 = true;
                    anim.start();
                }
            }
        });
        anim4.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isStart4 = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim4.setDuration(500);
        //设置补间器，控制动画的变化速率
        anim4.setInterpolator(new DecelerateInterpolator());
    }

    /**
     * 停止动画
     */
    public void stopAnimation() {
        Log.i("Main","mAnitionStart = " + mAnitionStart);
        if (mAnitionStart) {
            mAnitionStop = true;
            mAnitionStart = false;
            mLeftPoint = new Point(mSwingRadius, mSwingRadius);
            mRightPoint = new Point(mSwingRadius + (mGlobeNum - 1) * mGlobeRadius * 2 + (mSwingRadius - mGlobeRadius), mGlobeRadius);
            invalidate();
            if (anim != null) {
                anim.end();
            }
            if (anim2 != null) {
                anim2.end();
            }
            if (anim3 != null) {
                anim3.end();
            }
            if (anim4 != null) {
                anim4.end();
            }
        }

    }

}
