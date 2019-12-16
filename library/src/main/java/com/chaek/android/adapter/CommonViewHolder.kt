package com.chaek.android.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View

/**
 * 统一的ViewHolder
 * 可获取Context实例 和对应的AbstractAdapterItemView实例
 *
 * @author Chaek
 */
open class CommonViewHolder(val rootView: View) : RecyclerView.ViewHolder(rootView) {
    private var headCount=0
    var context: Context=rootView.context
    /**
     * @return 获取AbstractAdapterItemView
     */
    var adapterItemView: AbstractItemView<*, *>?=null
        private set

    /**
     * @return 获取用来获取ViewHolder对应数据源的位置
     */
    val itemPosition: Int
        get() {
            val index=layoutPosition - headCount
            return if (index < 0) 0 else index
        }

    /**
     * 设置当前CommonAdapter 的头部视图数量 用来获取ViewHolder对应数据源的位置
     *
     * @param headCount 头部View数量
     */
    fun setHeadCount(headCount: Int) {
        this.headCount=headCount
    }

    /**
     * 设置viewHolder的点击时间事件
     *
     * @param onClickListener 点击事件回调函数
     */
    fun setOnClickListener(onClickListener: View.OnClickListener) {
        rootView.setOnClickListener(onClickListener)
    }

    /**
     * 获取id对应的View
     *
     * @param viewId viewID
     * @param <T>    View 类型
     * @return View
    </T> */
    fun <T : View> findViewById(viewId: Int): T? {
        return rootView.findViewById(viewId)
    }

    /**
     * 绑定AbstractAdapterItemView
     *
     * @param adapterItemView AbstractAdapterItemView
     */
    fun onBindAdapterItemView(adapterItemView: AbstractItemView<*, *>) {
        this.adapterItemView=adapterItemView
    }
}