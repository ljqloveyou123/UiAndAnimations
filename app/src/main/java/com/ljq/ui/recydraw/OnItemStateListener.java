package com.ljq.ui.recydraw;

import android.support.v7.widget.RecyclerView;

/**
 * Created by 刘镓旗 on 2017/9/13.
 */

public interface OnItemStateListener {

    /**
     * item选择状态发生改变
     * @param viewHolder
     * @param actionState
     */
    void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState);

    /**
     * 移动结束了
     */
    void onItemMoveFinish();
}
