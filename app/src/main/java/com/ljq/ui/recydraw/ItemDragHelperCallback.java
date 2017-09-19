package com.ljq.ui.recydraw;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

/**
 * Created by 刘镓旗 on 2017/9/13.
 */

public class ItemDragHelperCallback extends ItemTouchHelper.Callback {
    /**
     * 需要支持哪个方法的滑动和拖动
     *
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int dragFlags;
        if (layoutManager instanceof GridLayoutManager || layoutManager instanceof StaggeredGridLayoutManager) {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        } else {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        }

        int swipeFlags = 0;
        //横向滑动
//        swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /**
     * 移动
     *
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        Log.e("move", "onMove");

        //不同类型不可移动
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        //回调Adapter
        if (recyclerView.getAdapter() instanceof OnDragItemListener) {
            OnDragItemListener itemListener = (OnDragItemListener) recyclerView.getAdapter();
            itemListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }
        return true;
    }

    /**
     * 滑动
     *
     * @param viewHolder
     * @param direction
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.e("move", "onSwiped");
    }

    /**
     * 清除view
     *
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        Log.e("move", "clearView");
        if(viewHolder instanceof OnItemStateListener){
            OnItemStateListener listener = (OnItemStateListener) viewHolder;
            listener.onItemMoveFinish();
        }
        super.clearView(recyclerView, viewHolder);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        Log.e("move", "isLongPressDragEnabled");
        //不支持长按拖动,手动调用
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        Log.e("move", "ViewSwipeEnabled");
        //不支持滑动,手动调用
        return false;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        Log.e("move", "onSelectedChanged");
        //不在空闲状态
        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE){
            if(viewHolder instanceof OnItemStateListener){
                OnItemStateListener listener = (OnItemStateListener) viewHolder;
                listener.onSelectedChanged(viewHolder,actionState);
            }
        }

        super.onSelectedChanged(viewHolder, actionState);
    }



}
