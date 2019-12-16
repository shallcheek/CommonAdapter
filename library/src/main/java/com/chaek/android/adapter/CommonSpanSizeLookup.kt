package com.chaek.android.adapter

import androidx.recyclerview.widget.GridLayoutManager


/**
 * @author Chaek
 */
abstract class CommonSpanSizeLookup(private val commonAdapter: CommonAdapter, private val mSpanSize: Int) : GridLayoutManager.SpanSizeLookup() {
    override fun getSpanSize(position: Int): Int {
        return if (commonAdapter.isHeadFootView(position)) {
            mSpanSize
        } else getCommonSpanSize(commonAdapter.getPosition(position), commonAdapter.getListItemData(position))
    }

    /**
     * 获取list数据
     *
     * @param position CommonAdapter list的位置
     * @param listItem item数据
     * @return span
     */
    protected abstract fun getCommonSpanSize(position: Int, listItem: Any?): Int

}