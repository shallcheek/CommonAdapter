package com.chaek.android.library;

import android.support.v7.widget.GridLayoutManager;


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
        return getCommonSpanSize(position - commonAdapter.getHeaderCount());
    }

    protected abstract int getCommonSpanSize(int position);


}
