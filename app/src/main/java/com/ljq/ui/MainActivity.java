package com.ljq.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ljq.ui.greedy_snake.GreedySnakeCustomView;
import com.ljq.ui.konck.KnockLoadingView;
import com.ljq.ui.rect_loading.RectLoadingView;
import com.ljq.ui.recydraw.TagDrawActivity;
import com.ljq.ui.refresh_layout.RefreshActivity;

public class MainActivity extends AppCompatActivity{

    private KnockLoadingView loadingView;
    private RectLoadingView loadingView2;
    private GreedySnakeCustomView loadingView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadingView = (KnockLoadingView) findViewById(R.id.zhonbgai);
        loadingView2 = (RectLoadingView) findViewById(R.id.loading);
        loadingView3 = (GreedySnakeCustomView) findViewById(R.id.greedy);

    }


    /**
     * 关闭动画
     * @param view
     */
    public void click_stop(View view){
        if(loadingView != null){
            loadingView.stopAnimation();
        }
        if(loadingView2 != null){
            loadingView2.stopLoadingAnimation();
        }
        if(loadingView3 != null){
            loadingView3.stopAnimation();
        }
    }

    /**
     * 开启动画
     * @param view
     */
    public void click_start(View view){
        if(loadingView != null){
            loadingView.startAnima();
        }

        if(loadingView2 != null){
            loadingView2.startLoadingAnimation();
        }
        if(loadingView3 != null){
            loadingView3.startAnimation();
        }
    }

    /**
     * 下拉刷新
     * @param view
     */
    public void click_refresh(View view){
        startActivity(new Intent(this, RefreshActivity.class));
    }

    /**
     * 标签移动排序
     * @param view
     */
    public void click_item_tag_move(View view){
        startActivity(new Intent(this, TagDrawActivity.class));
    }

}
