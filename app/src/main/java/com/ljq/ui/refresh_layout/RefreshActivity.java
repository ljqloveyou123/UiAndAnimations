package com.ljq.ui.refresh_layout;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ljq.ui.R;

/**
 * 下拉刷新
 * Created by 刘镓旗 on 2017/9/19.
 */

public class RefreshActivity extends Activity implements RefreshLayout.OnRefreshListener {
    private RefreshLayout refreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refresh_activity);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle);
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        //设置监听
        refreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyTestAdapter());
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            refreshLayout.setRefreshing(false);
        }
    };

    @Override
    public void onRefresh() {
        Log.e("refresh", "onRefresh");
        handler.sendEmptyMessageDelayed(0, 2000);
    }

    class MyTestAdapter extends RecyclerView.Adapter<MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new MyViewHolder(new TextView(RefreshActivity.this));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.textView.setText("position = " + position);
        }

        @Override
        public int getItemCount() {
            return 20;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }
}
