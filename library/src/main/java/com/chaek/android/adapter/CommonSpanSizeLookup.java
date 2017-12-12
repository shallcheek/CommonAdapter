package com.chaek.android.adapter;

import android.support.v7.widget.GridLayoutManager;


/**
 * @author Chaek
 */
public abstract class CommonSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    private CommonAdapter commonAdapter;
    private int mSpanSize;

    public CommonSpanSizeLookup(CommonAdapter commonAdapter, int span) {
        this.mSpanSize = span;
        this.commonAdapter = commonAdapter;
    }

    @Override
    public int getSpanSize(int position) {
        if (commonAdapter.isHeadFootView(position)) {
            return mSpanSize;
        }
        return getCommonSpanSize(commonAdapter.getPosition(position), commonAdapter.getItemData(position));
    }

    /**
     * 获取list数据
     *
     * @param position CommonAdapter list的位置
     * @param listItem item数据
     * @return span
     */
    protected abstract int getCommonSpanSize(int position, Object listItem);

}
