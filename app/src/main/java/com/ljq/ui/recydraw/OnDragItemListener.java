package com.ljq.ui.recydraw;

/**
 * Created by 刘镓旗 on 2017/9/13.
 */

public interface OnDragItemListener {
    /**
     * 正在移动
     * @param currentPosition
     * @param targetPosition
     */
    void onItemMove(int currentPosition,int targetPosition);
}
