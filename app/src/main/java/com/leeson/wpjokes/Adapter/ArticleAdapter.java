package com.leeson.wpjokes.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leeson.wpjokes.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MyComputer on 2016/8/27 0027.
 */
public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;
    private static final int TYPE_ITEM = 0;//item view
    private static final int TYPE_FOOTER = 1;//foot view
    private List<Article> Articlelist = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;
    //上拉加载更多状态-默认为0
    private int load_more_status = 0;
    // 判断是不是最后一个item，默认是true
    private boolean mShowFooter = true;

    //    然后在RecylerView里面定义OnItemClickListener:
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载布局文件
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.rv_item, null);

            return new ItemViewHolder(view);
        }
        // type == TYPE_FOOTER 返回footerView
        else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.footer_view, null);
            return new FooterViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (position < Articlelist.size()) {
//将数据填充到具体的view中
            if (holder instanceof ItemViewHolder) {
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                itemViewHolder.tvTitle.setText(Articlelist.get(position).getPost_title());
                itemViewHolder.tvPostDate.setText(Articlelist.get(position).getPost_date());


            } else if (holder instanceof FooterViewHolder) {
                FooterViewHolder vh = (FooterViewHolder) holder;
//上拉加载更多布局数据绑定
                switch (load_more_status) {
                    case PULLUP_LOAD_MORE:
                        vh.tvRefresh.setText("上拉加载更多...");
                        break;
                    case LOADING_MORE:
                        vh.tvRefresh.setText("正在加载更多数据...");
                        break;
                }

            }
        }
    }

    /**
     * 进行判断是普通Item视图还是FootView视图
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return Articlelist.size() + 1;
    }

    public void addItem(List<Article> articles, boolean isPullDownRefresh) {

        //如果是下拉刷新，则先把之前的数据清空
        if (isPullDownRefresh) {
            Articlelist.clear();

        }

        if (articles != null && articles.size() > 0) {

            for (Article item : articles) {
                Articlelist.add(item);
            }
            notifyDataSetChanged();
        }
    }

    public void changeMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }


    //  继承RecylerView并定义一个接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public TextView tvPostDate;

        public ItemViewHolder(final View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvPostDate = (TextView) itemView.findViewById(R.id.tvTime);
            //添加点击事件
            if (mOnItemClickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getLayoutPosition();
                        mOnItemClickListener.onItemClick(view, position);
                    }
                });
            }
        }
    }
}

