package com.chaek.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 统一的ViewHolder
 * 可获取Context实例 和对应的AbstractAdapterItemView实例
 *
 * @author Chaek
 */
public class CommonViewHolder extends RecyclerView.ViewHolder {
    private int headCount = 0;

    private View rootView;
    public Context mContext;
    private AbstractItemView adapterItemView;

    /**
     * 设置当前CommonAdapter 的头部视图数量 用来获取ViewHolder对应数据源的位置
     *
     * @param headCount 头部View数量
     */
    public void setHeadCount(int headCount) {
        this.headCount = headCount;
    }

    public CommonViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        rootView = itemView;
    }


    /**
     * @return 获取当前rootView
     */
    public View getRootView() {
        return rootView;
    }

    /**
     * 设置viewHolder的点击时间事件
     *
     * @param onClickListener 点击事件回调函数
     */
    public void setOnClickListener(View.OnClickListener onClickListener) {
        rootView.setOnClickListener(onClickListener);
    }

    /**
     * @return 获取用来获取ViewHolder对应数据源的位置
     */
    public int getItemPosition() {
        int index = getLayoutPosition() - headCount;
        return index < 0 ? 0 : index;
    }

    /**
     * 获取id对应的View
     *
     * @param viewId viewID
     * @param <T>    View 类型
     * @return View
     */
    public <T extends View> T findViewById(int viewId) {
        return rootView.findViewById(viewId);
    }

    /**
     * 绑定AbstractAdapterItemView
     *
     * @param adapterItemView AbstractAdapterItemView
     */
    public void onBindAdapterItemView(AbstractItemView adapterItemView) {
        this.adapterItemView = adapterItemView;
    }

    /**
     * @return 获取AbstractAdapterItemView
     */
    public AbstractItemView getAdapterItemView() {
        return adapterItemView;
    }
}