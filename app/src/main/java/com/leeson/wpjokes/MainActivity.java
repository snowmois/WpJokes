package com.leeson.wpjokes;
/*开发一个笑话连连看应用，要求如下：
 *  a)离线时可以查看缓存的数据
 *  b)在线时自动更新并缓存数据
 *  c)列表翻页功能
 *  d)列表下拉刷新功能
 *  e)通信时数据格式为JSON
 *  */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.leeson.wpjokes.Adapter.Article;
import com.leeson.wpjokes.Adapter.ArticleAdapter;
import com.leeson.wpjokes.Utils.CheckNetwork;
import com.leeson.wpjokes.Utils.GetDataFromDb;
import com.leeson.wpjokes.Utils.HttpUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public List<Article> articles = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ArticleAdapter mArticlesAdapter;
    private int mPage = 1;//页码
    private boolean isSetAdapter = true;//判断是否设置了adapter
    private boolean isGotData = true;//判断是否获取到数据
    // 判断是不是最后一个item，默认是true
    private boolean isPullDownRefresh;//判断是否是下拉刷新
    private int mLastVisibleItemPosition;
    private GetDataFromDb mGetDataFromDb;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    mArticlesAdapter.addItem(articles, isPullDownRefresh);
                    if (articles != null)
                        mGetDataFromDb.insertDb(articles);
                    break;

            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        isSetAdapter = false;
        mArticlesAdapter = new ArticleAdapter();

        mGetDataFromDb = new GetDataFromDb(MainActivity.this);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light);

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);//设置布局管理器

        mRecyclerView = (RecyclerView) findViewById(R.id.recylerView);


        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mArticlesAdapter);

        //自动刷新监听
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                articles.clear();
                mSwipeRefreshLayout.setRefreshing(true);
                initData();
                isPullDownRefresh = false;
            }
        });
        //下拉刷新监听
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isGotData = true;//先确定状态
                isPullDownRefresh = true;
                Refreshing(mArticlesAdapter);
                initData();
//                mPage = 1;


            }
        });

        //设置滑动监听
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

//                RecyclerView.Adapter adapter = recyclerView.getAdapter();

                if (CheckNetwork.isNetworkAvailable(MainActivity.this)) {

                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                    if (layoutManager instanceof LinearLayoutManager) {

                        if (newState == RecyclerView.SCROLL_STATE_IDLE
                                && mLastVisibleItemPosition + 1 == mArticlesAdapter.getItemCount() && mArticlesAdapter.getItemCount() > 10) {
//item数量大于10刷新

                            Refreshing(mArticlesAdapter);
                            initData();
                        }
                    }
                } else {
                    mArticlesAdapter.notifyItemRemoved(mArticlesAdapter.getItemCount());
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                //获取可见界面的最后一项的position
                mLastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();

            }
        });

        mArticlesAdapter.setOnItemClickListener(new ArticleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, ContentActivity.class);

                String postTitle = articles.get(position).getPost_title().toString();
                String postDate = articles.get(position).getPost_date().toString();
                String postContent = articles.get(position).getPost_content().toString();
                intent.putExtra("postTitle", postTitle);
                intent.putExtra("postDate", postDate);
                intent.putExtra("postContent", postContent);

                startActivity(intent);
            }
        });
    }

    private void Refreshing(RecyclerView.Adapter adapter) {

        //判断是否有数据
        if (!isGotData) {
            adapter.notifyItemRemoved(adapter.getItemCount());
        } else {
            if (isPullDownRefresh) {
                mPage = 1;
            } else {

                mPage++;
            }
//            isPullDownRefresh = false;
        }


    }

    private void initData() {
        //判断网络条件，如果有网

        if (CheckNetwork.isNetworkAvailable(this)) {
            try {
                String path = "http://wp.giligiliml.top/latestpost.php";
                HttpUtils.HttpURLConnection_GET(path, handler, articles);
                mSwipeRefreshLayout.setRefreshing(false);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            GetDataFromDb getDataFromDb = new GetDataFromDb(MainActivity.this);
            articles = getDataFromDb.queryDb(mPage);
            mArticlesAdapter.addItem(articles, false);

        }

    }

}