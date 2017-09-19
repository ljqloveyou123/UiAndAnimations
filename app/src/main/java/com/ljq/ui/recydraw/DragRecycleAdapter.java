package com.ljq.ui.recydraw;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.ljq.ui.R;

import java.util.ArrayList;

/**
 * Created by 刘镓旗 on 2017/9/13.
 */

public class DragRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnDragItemListener {

    //上边标题
    public static final int TOP_HEAD_TPYT = 0;

    //上边的数据
    public static final int TOP_DATA_TYPE = 1;

    //下边标题
    public static final int BOTTOM_HEAD_TYPE = 2;
    //下边的数据
    public static final int BOTTOM_DATA_TYPE = 3;

    //上下标题个数
    public static final int HEAD_COUNT = 2;

    private final ArrayList<DragRecycleMode> mTopData;//上边数据
    private final ArrayList<DragRecycleMode> mBottomData;//下边数据
    private final ItemTouchHelper mTouchHelper;//item触摸器
    private final Context mContext;


    public DragRecycleAdapter(ArrayList<DragRecycleMode> topdata, ArrayList<DragRecycleMode> bottomData, ItemTouchHelper touchHelper, Context context) {
        mTopData = topdata;
        mBottomData = bottomData;
        mTouchHelper = touchHelper;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView;
        switch (viewType) {
            //上边标签的标题
            case TOP_HEAD_TPYT:
                itemView = inflater.inflate(R.layout.move_top_head, parent, false);
                return new TopHeadHolder(itemView);

            //上方的标签
            case TOP_DATA_TYPE:
                itemView = inflater.inflate(R.layout.move_text, parent, false);
                TopDataHolder topDataViewHolder = new TopDataHolder(itemView);
                initTopItemHolder(topDataViewHolder, parent);
                return topDataViewHolder;

            //下方的标题
            case BOTTOM_HEAD_TYPE:
                itemView = inflater.inflate(R.layout.move_top_head, parent, false);
                return new BottomHeadHolder(itemView);
            //下方的标签
            case BOTTOM_DATA_TYPE:
                itemView = inflater.inflate(R.layout.move_text, parent, false);
                BottomDataHolder bottomDataViewHolder = new BottomDataHolder(itemView);
                initBottomItemHolder(bottomDataViewHolder, parent);
                return bottomDataViewHolder;
        }

        return null;
    }

    /**
     * 初始化下方标签
     *
     * @param bottomDataViewHolder
     * @param parent
     */
    private void initBottomItemHolder(final BottomDataHolder bottomDataViewHolder, final ViewGroup parent) {
        bottomDataViewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置点击事件
                int currentPosition = bottomDataViewHolder.getAdapterPosition();
                //获取要移动的view和要移动到的view
                RecyclerView recyclerView = (RecyclerView) parent;
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                View currentView = layoutManager.findViewByPosition(currentPosition);
                //得到上边的最后一个view
                View targetView = layoutManager.findViewByPosition(mTopData.size());
                int span = layoutManager.getSpanCount();

                //两种情况：一种是上边的标签正好是满排，也就是说要添加的为位置应该是下一排的第一个才对
                //1.一种是上边的标签正好是满排，也就是说要添加的为位置应该是下一排的第一个才对
                //2.还有一种情况是上边已经没有标签
                //这两种情况下上边的集合 % 列 都等于0,这时候我们要的targetview就是
                //mTopData.size()+1的view，也就是说取到的是第二个标题的位置
                int targetX;
                int targetY;
                Log.e("bottom", "bottom 1 = " + mTopData.size());
                Log.e("bottom", "bottom 2 = " + span);
                Log.e("bottom", "bottom 3 = " + (mTopData.size() % span == 0));
                if (mTopData.size() % span == 0) {
                    //特殊情况是第二个标题
                    targetView = layoutManager.findViewByPosition(mTopData.size() + 1);
                    targetX = targetView.getLeft();
                    targetY = targetView.getTop();
                } else {
                    //正常时获取的是前一个view
                    targetX = targetView.getRight();//前一个右边
                    targetY = targetView.getTop();
                }
                //移动删除
                moveToTop(currentPosition);
                startMoveAnimation(recyclerView, currentView, targetX, targetY);

            }
        });
    }


    /**
     * 初始化上边标签
     *
     * @param viewHolder
     * @param parent
     */
    private void initTopItemHolder(final TopDataHolder viewHolder, final ViewGroup parent) {
        final RecyclerView recyclerView = (RecyclerView) parent;
        final GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        //点击事件
        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //当点击的时候需要移动到下面的第一个
                //1.获取当前要移动的位置
                int currentPosition = viewHolder.getAdapterPosition();
                //2.获取需要移动的两个view
                //需要移动view
                View currentView = layoutManager.findViewByPosition(currentPosition);
                //要移动到的view
                View targetView = layoutManager.findViewByPosition(mTopData.size() + 2);//有两个head

                //3.移动删除
                int spanCount = layoutManager.getSpanCount();
                int targetX;
                int targetY;
                //下方没有的时候或者上边最后一排只有一个
                if (targetView == null || ((mTopData.size() - 1) % spanCount == 0)) {
                    //这其实是标题
                    targetView = layoutManager.findViewByPosition(mTopData.size() + 1);
                    targetX = targetView.getLeft();
                    targetY = targetView.getBottom();
                } else {
                    targetX = targetView.getLeft();
                    targetY = targetView.getTop();
                }
                //3.移动
                moveToBottom(currentPosition);
                startMoveAnimation(recyclerView, currentView, targetX, targetY);
            }
        });
        //长按事件
        viewHolder.textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                mTouchHelper.startDrag(viewHolder);
                return true;
            }
        });

    }

    /**
     * 开启移动动画
     *
     * @param recyclerView
     * @param currentView
     * @param targetX
     * @param targetY
     */
    private void startMoveAnimation(RecyclerView recyclerView, View currentView, int targetX, int targetY) {
        final ViewGroup viewGroup = (ViewGroup) recyclerView.getParent();
        //添加一个镜像的view
        final ImageView imageView = addMoveAnimationView(viewGroup, recyclerView, currentView);

        //创建动画
        TranslateAnimation animation = new TranslateAnimation(currentView.getLeft(), targetX, currentView.getTop(), targetY);
        animation.setDuration(300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.e("animation", "onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束
                Log.e("animation", "onAnimationEnd");
                viewGroup.removeView(imageView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.e("animation", "onAnimationRepeat");
            }
        });
        imageView.startAnimation(animation);

    }

    /**
     * 添加一个镜像的view
     *
     * @param viewGroup
     * @param recyclerView
     * @param currentView
     * @return
     */
    private ImageView addMoveAnimationView(ViewGroup viewGroup, RecyclerView recyclerView, View currentView) {
        //是否缓存资源
        currentView.destroyDrawingCache();
        //设置可以生成缓存资源
        currentView.setDrawingCacheEnabled(true);
        //创建bitmap
        Bitmap bitmap = Bitmap.createBitmap(currentView.getDrawingCache());
        //设置不可生成缓存资源
        ImageView imageView = new ImageView(recyclerView.getContext());
        imageView.setImageBitmap(bitmap);
        currentView.setDrawingCacheEnabled(false);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.MarginLayoutParams(currentView.getWidth(), currentView.getHeight());
        viewGroup.addView(imageView, layoutParams);
        return imageView;
    }

    private void moveToTop(int currentPosition) {
        Log.e("moveToTop", "moveToTop = " + currentPosition);
        DragRecycleMode bottomItemData = getBottomItemData(currentPosition);
        mBottomData.remove(bottomItemData);
        mTopData.add(bottomItemData);
//        notifyItemMoved(currentPosition, mTopData.size());
        updataMoveIndex(currentPosition, mTopData.size());
    }

    /**
     * 这里需要更新数据库
     *
     * @param currentPosition
     * @param targetPosition
     */
    private void updataMoveIndex(int currentPosition, int targetPosition) {
        notifyItemMoved(currentPosition, targetPosition);
        //将当前关注的存起来
        SavaTagJson.saveTopData(mContext,mTopData);
        SavaTagJson.savaBottomData(mContext,mBottomData);
    }


    /**
     * 移动到下方
     *
     * @param currentPosition
     */
    private void moveToBottom(int currentPosition) {
        //当前位置
        DragRecycleMode t = getTopItemData(currentPosition);
        mTopData.remove(t);
        mBottomData.add(0, t);
//        notifyItemMoved(currentPosition, mTopData.size() + 2);//2个head,
        updataMoveIndex(currentPosition, mTopData.size() + 2);
    }

    private DragRecycleMode getTopItemData(int position) {
        return mTopData.get(position - 1);
    }

    private DragRecycleMode getBottomItemData(int position) {
        return mBottomData.get(position - mTopData.size() - 2);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TopHeadHolder) {
            TopHeadHolder topHeadHolder = (TopHeadHolder) holder;
            topHeadHolder.textView.setText("点击删除关注");
            topHeadHolder.textView2.setText("点击删除关注");
        }
        if (holder instanceof TopDataHolder) {
            TopDataHolder topDataHolder = (TopDataHolder) holder;
            topDataHolder.textView.setText(getTopItemData(position).tagName);
            topDataHolder.imageView.setVisibility(View.VISIBLE);
        }

        if (holder instanceof BottomHeadHolder) {
            BottomHeadHolder bottomHeadHolder = (BottomHeadHolder) holder;
            bottomHeadHolder.textView.setText("点击添加关注");
            bottomHeadHolder.textView2.setText("点击添加关注");
        }

        if (holder instanceof BottomDataHolder) {
            BottomDataHolder bottomDataHolder = (BottomDataHolder) holder;
            bottomDataHolder.textView.setText(getBottomItemData(position).tagName);
            bottomDataHolder.imageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {

        return mTopData.size() + mBottomData.size() + HEAD_COUNT;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return TOP_HEAD_TPYT;
        } else if (position > 0 && position < mTopData.size() + 1) {//有一个head
            return TOP_DATA_TYPE;
        } else if (position == mTopData.size() + 1) {
            //有一个head
            return BOTTOM_HEAD_TYPE;
        } else {
            return BOTTOM_DATA_TYPE;
        }
    }

    /**
     * 拖动开始，数据交换位置
     *
     * @param currentPosition
     * @param targetPosition
     */
    @Override
    public void onItemMove(int currentPosition, int targetPosition) {
        //这里的position是在Adapter中的position，并不是我们集合中的position，我们有一个head
        //1.得到当前移动位置在集合中的位置
        Log.e("currentPosition", "currentPosition = " + currentPosition);
        Log.e("targetPosition", "targetPosition = " + targetPosition);
        int oldIndex = currentPosition - 1;//有一个head
        int newIndex = targetPosition - 1;
        mTopData.set(oldIndex, mTopData.set(newIndex, mTopData.get(oldIndex)));
//        notifyItemMoved(currentPosition, targetPosition);
        updataMoveIndex(currentPosition, targetPosition);
    }


    /**
     * 上边标题
     */
    static class TopHeadHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TextView textView2;

        public TopHeadHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.move_top_head);
            textView2 = (TextView) itemView.findViewById(R.id.move_top_head_two);
        }
    }

    /**
     * 上边数据
     */
    private static class TopDataHolder extends RecyclerView.ViewHolder implements OnItemStateListener {
        public TextView textView;
        public ImageView imageView;

        public TopDataHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_move_text);
            imageView = (ImageView) itemView.findViewById(R.id.tv_move_edit);
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            textView.setBackgroundResource(R.drawable.text_bg_yes);
            Log.e("onSelectedChanged", "onSelectedChanged");

        }

        @Override
        public void onItemMoveFinish() {
            textView.setBackgroundResource(R.drawable.text_bg_no);
            Log.e("onItemMoveFinish", "onItemMoveFinish");
        }
    }


    /**
     * 头部标题
     */
    private static class BottomHeadHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TextView textView2;

        public BottomHeadHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.move_top_head);
            textView2 = (TextView) itemView.findViewById(R.id.move_top_head_two);
        }
    }

    /**
     * 下边数据
     */
    private static class BottomDataHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;

        public BottomDataHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_move_text);
            imageView = (ImageView) itemView.findViewById(R.id.tv_move_edit);
        }
    }
}
