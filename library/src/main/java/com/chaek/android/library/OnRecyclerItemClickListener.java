package com.chaek.android.library;

/**
 * RecyclerView Adapter item onClick Listener
 *
 * @author Chaek
 */
public interface OnRecyclerItemClickListener {
    /**
     * onClick callback
     *
     * @param t     click item data
     * @param index click item position
     */
    void onClick(Object t, int index);
}
