package com.chaek.android.adapter;

import android.support.v7.util.ListUpdateCallback;


/**
 * DiffUtil 检测数据更新
 * 根据toString 进行判断
 *
 * @author Chaek
 */
public class CommonListUpdateCallBack implements ListUpdateCallback {
    private CommonAdapter commonAdapter;

    public CommonListUpdateCallBack(CommonAdapter commonAdapter) {
        this.commonAdapter = commonAdapter;
    }

    @Override
    public void onInserted(int position, int count) {
        commonAdapter.notifyItemRangeInserted(position + commonAdapter.getHeaderCount(), count);
    }

    @Override
    public void onRemoved(int position, int count) {
        commonAdapter.notifyItemRangeRemoved(position + commonAdapter.getHeaderCount(), count);
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
        commonAdapter.notifyItemMoved(fromPosition + commonAdapter.getHeaderCount(), fromPosition + commonAdapter.getHeaderCount());
    }

    @Override
    public void onChanged(int position, int count, Object payload) {
        commonAdapter.notifyItemRangeChanged(position + commonAdapter.getHeaderCount(), count, payload);
    }


}
