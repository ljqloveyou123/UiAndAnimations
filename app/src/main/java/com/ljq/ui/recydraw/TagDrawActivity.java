package com.ljq.ui.recydraw;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ljq.ui.R;

import java.util.ArrayList;

/**
 * 标签移动排序
 * Created by 刘镓旗 on 2017/9/19.
 */

public class TagDrawActivity extends Activity {

    private ArrayList<DragRecycleMode> topList;
    private ArrayList<DragRecycleMode> bottomList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tag_draw_activity);
        //初始化数据
        initTopAndBottomData();
        DragRecyclerView recyclerView = (DragRecyclerView) findViewById(R.id.draw_recycle);
        recyclerView.setData(topList, bottomList);

    }

    private void initTopAndBottomData() {
        //获取上次保存的记录
        topList = SavaTagJson.getTopDataJson(this);
        bottomList = SavaTagJson.getBottomDataJson(this);
        if (topList == null || topList.isEmpty()) {
            topList = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                DragRecycleMode mode = new DragRecycleMode();
                mode.tagName = "上标签" + i;
                topList.add(mode);
            }
        }
        if (bottomList == null || bottomList.isEmpty()) {
            bottomList = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                DragRecycleMode mode = new DragRecycleMode();
                mode.tagName = "下标签" + i;
                bottomList.add(mode);
            }
        }

    }
}
