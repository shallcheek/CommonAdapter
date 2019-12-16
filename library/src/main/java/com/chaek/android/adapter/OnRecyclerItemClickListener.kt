package com.chaek.android.adapter

/**
 * RecyclerView Adapter item onClick Listener
 *
 * @author Chaek
 */
interface OnRecyclerItemClickListener<T> {
    /**
     * onClick callback
     *
     * @param t     click item data
     * @param index click item position
     */
    fun onClick(t: T, index: Int)

}
