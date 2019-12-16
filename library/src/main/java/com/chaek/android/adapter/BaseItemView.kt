package com.chaek.android.adapter

import android.view.View


abstract class BaseItemView<T> : AbstractItemView<T, CommonViewHolder>() {

    override fun onCreateViewHolder(view: View, viewType: Int): CommonViewHolder {
        return CommonViewHolder(view)
    }
}
