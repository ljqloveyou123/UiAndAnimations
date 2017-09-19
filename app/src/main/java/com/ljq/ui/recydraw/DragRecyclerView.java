package com.ljq.ui.recydraw;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;

import java.util.ArrayList;


/**
 * 标签移动排序
 * Created by 刘镓旗 on 2017/9/13.
 */

public class DragRecyclerView extends RecyclerView {

    //上下的数据
    //layoutManger
    private ItemTouchHelper mTouchHelper;
    //默认列个数
    private int mDetault_span = 4;
    private GridLayoutManager mLayoutManager;
    private ArrayList<DragRecycleMode> mTopData;
    private ArrayList<DragRecycleMode> mBottomData;
    private DragRecycleAdapter mAdapter;

    public DragRecyclerView(Context context) {
        super(context);
        init(context);
    }


    public DragRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DragRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mLayoutManager = new GridLayoutManager(context, mDetault_span);
        mTouchHelper = new ItemTouchHelper(new ItemDragHelperCallback());
        mTouchHelper.attachToRecyclerView(this);
        setLayoutManager(mLayoutManager);
    }

    /**
     * 设置列个数
     *
     * @param count
     */
    public void setSpanCount(int count) {
        this.mDetault_span = count;
        mLayoutManager.setSpanCount(mDetault_span);
    }

    public void setData(ArrayList<DragRecycleMode> topdata, ArrayList<DragRecycleMode> bottomdata) {
        this.mTopData = topdata;
        this.mBottomData = bottomdata;
        if (mAdapter == null) {
            mAdapter = new DragRecycleAdapter(mTopData, mBottomData, mTouchHelper,getContext());
        }
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = mAdapter.getItemViewType(position);
                return viewType == DragRecycleAdapter.TOP_HEAD_TPYT || viewType == DragRecycleAdapter.BOTTOM_HEAD_TYPE ? 4 : 1;
            }
        });
        setAdapter(mAdapter);
    }

}
