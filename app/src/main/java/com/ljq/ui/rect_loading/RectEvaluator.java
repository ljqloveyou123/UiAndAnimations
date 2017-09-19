package com.ljq.ui.rect_loading;

import android.animation.TypeEvaluator;
import android.graphics.Rect;

import com.ljq.ui.rect_loading.RectLoadingMode;

/**
 * Created by 刘镓旗 on 2017/9/7.
 */

public class RectEvaluator implements TypeEvaluator {

    private int mRect_Space;
    private int mRect_Size;

    public RectEvaluator(int rect_Size, int rect_Space){
        this.mRect_Size = rect_Size;
        this.mRect_Space = rect_Space;
    }


    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        RectLoadingMode endMode = new RectLoadingMode();

        if(fraction <= 0.25f){
            //初始状态,方块按逆时针顺序排列
            endMode.rect_1 = new Rect(0, 0, mRect_Size, mRect_Size);

            endMode.rect_2 = new Rect(0,
                    mRect_Size + mRect_Space,
                    mRect_Size,
                    mRect_Size * 2 + mRect_Space);

            endMode.rect_3 = new Rect(mRect_Size + mRect_Space,
                    mRect_Size + mRect_Space,
                    mRect_Size*2 + mRect_Space,
                    mRect_Size * 2 + mRect_Space);

            endMode.rect_4 = new Rect(mRect_Size + mRect_Space,
                    mRect_Size * 2 + mRect_Space*2,
                    mRect_Size * 2 + mRect_Space,
                    mRect_Size * 3 + mRect_Space * 2);
        }else if(fraction > 0.25f && fraction <= 0.5f){
            //因为整体高度=3个方块+2个间距，
            // 那么我们有两个方块和一个间隙是整体移动，
            // 所以剩下1个方块+1个间隙，然后把剩下的/2就是第一排的起始点
            //第一排起始点 = (一个方块高度 + 半个间隙高度) / 2
            int heightStart_1 = (mRect_Size + mRect_Space) /2;

            //第一排结束点是一个方块的高度+剩余部分一半
            int heightEnd_1 = heightStart_1 + mRect_Size;

            //第二排起始点 = 第一排结束点 + 一个间隙
            int heightStart2_ = heightEnd_1 + mRect_Space;
            //第二排结束点 = 第二排开始点+一个方块高度
            int heightEnd_2 = heightStart2_ + mRect_Size;

            //第二步
            endMode.rect_1 = new Rect(0,
                    heightStart_1,
                    mRect_Size,
                    heightEnd_1);
            endMode.rect_2 = new Rect(0,
                    heightStart2_,
                    mRect_Size,
                    heightEnd_2);
            endMode.rect_3 = new Rect(mRect_Size + mRect_Space,
                    heightStart2_,
                    mRect_Size * 2 + mRect_Space,
                    heightEnd_2);
            endMode.rect_4 = new Rect(mRect_Size + mRect_Space,
                    heightStart_1,
                    mRect_Size * 2 + mRect_Space,
                    heightEnd_1);
        }else if(fraction > 0.5f && fraction <= 0.75f){
            //第三步,1和2不动

            //不动
            endMode.rect_1 = new Rect(0,
                    mRect_Size + mRect_Space,
                    mRect_Size,
                    mRect_Size * 2 + mRect_Space);
            //不动
            endMode.rect_2 = new Rect(0,
                    mRect_Size * 2 + mRect_Space*2,
                    mRect_Size,
                    mRect_Size * 3 + mRect_Space * 2);
            //向上一个
            endMode.rect_3 = new Rect(mRect_Size + mRect_Space,
                    0,
                    mRect_Size*2 + mRect_Space,
                    mRect_Size);
            //4变3的值
            endMode.rect_4 = new Rect(mRect_Size + mRect_Space,
                    mRect_Size + mRect_Space,
                    mRect_Size*2 + mRect_Space,
                    mRect_Size * 2 + mRect_Space);
        }else {
            //1和4不动
            //        第四步
            endMode.rect_1 = new Rect(0,
                    mRect_Size + mRect_Space,
                    mRect_Size,
                    mRect_Size * 2 + mRect_Space);
            //向右半个
            endMode.rect_2 = new Rect((mRect_Size + mRect_Space )/2,
                    mRect_Size * 2 + mRect_Space*2,
                    (mRect_Size * 2 + mRect_Space)/2 + (mRect_Size / 2),
                    mRect_Size * 3 + mRect_Space * 2);
            //向左半个
            endMode.rect_3 = new Rect((mRect_Size + mRect_Space) / 2,
                    0,
                    (mRect_Size*2 + mRect_Space) /2 + (mRect_Size /2) ,
                    mRect_Size);
            //不动
            endMode.rect_4 = new Rect(mRect_Size + mRect_Space,
                    mRect_Size + mRect_Space,
                    mRect_Size*2 + mRect_Space,
                    mRect_Size * 2 + mRect_Space);
        }

        return endMode;
    }
}
