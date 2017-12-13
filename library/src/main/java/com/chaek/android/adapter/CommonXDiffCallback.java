package com.chaek.android.adapter;

import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * Auth: Chaek
 * Date: 2017/12/12
 */

public abstract class CommonXDiffCallback extends DiffUtil.Callback {

    private final List<Object> oldList;
    private final List<Object> newList;

    public CommonXDiffCallback(List<Object> oldList, List<Object> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        final Object oldItem = oldList.get(oldItemPosition);

        final Object newItem = newList.get(newItemPosition);
        return areItemsTheSame(oldItem, newItem);
    }

    protected abstract boolean areItemsTheSame(Object oldItem, Object newItem);

    protected abstract boolean areContentsTheSame(Object oldItem, Object newItem);

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Object oldItem = oldList.get(oldItemPosition);
        final Object newItem = newList.get(newItemPosition);
        return areContentsTheSame(oldItem, newItem);
    }
}
