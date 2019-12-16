package com.chaek.android.adapter

import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

import android.view.View
import android.view.ViewGroup


/**
 * `CommonKyAdapter` 对应的子视图样式 一种数据源对应一个样式 也可多种数据对应一种
 * 调用方式为:
 * <br></br>
 * [new CommonAdapter().register(AbstractAdapterItemView.class)]
 *
 * 重写 <br></br>[.getLayoutId],<br></br>[.onCreateViewHolder],<br></br>[.onBindViewHolder]
 * 即可<br></br>
 *
 * @author: Chaek
 */
abstract class AbstractItemView<T, R : CommonViewHolder> {

    /**
     * 获取CommonKyAdapter 实例操作应该在调用 setCommonAdapter之后在自己构建AbstractAdapterItemView的构造函数中调用会出现null
     *
     * @return 当前当前AbstractAdapterItemView所属的Adapter
     */
    /**
     * 赋值 commonAdapter 参考[CommonAdapter.register] }
     * 在初始化时 注册当前`AbstractAdapterItemView` 赋值可让`AbstractAdapterItemView`持有CommonAdapter对象
     *
     *
     * 获取CommonAdapter 实例操作应该在调用 setCommonAdapter之后在自己构建AbstractAdapterItemView的构造函数中调用会出现null
     *
     */
    lateinit var commonAdapter: CommonAdapter

    /**
     * 获取当前数据对应的布局文件ID
     * 创建成view 查看[CommonAdapter.onCreateViewHolder] }
     *
     * @param viewType viewType
     * @return layout id
     */
    @LayoutRes
    abstract fun getLayoutId(viewType: Int): Int


    /**
     * 获取viewType 范围0-100
     *
     * @param position list position
     * @param data     数据
     * @return View Type
     */
    @IntRange(from = 0, to = 100)
    open fun getItemViewType(position: Int, data: T): Int {
        return 0
    }

    /**
     * 将item 与 ViewHolder绑定起来 用作正常的参数设置数据绑定
     * BaseViewHolder 中可获取到context实例 来自与View [View.getContext]
     * 也可获取AbstractAdapterItemView绑定的实例
     *
     * @param vh   对应的BaseViewHolder
     * @param data item data 数据
     */
    abstract fun onBindViewHolder(vh: R, data: T)


    /**
     * 初始化创建 BaseViewHolder
     *
     * @param view     来自[.getLayoutId] 对应的布局文件创建的View
     * @param viewType 数据源中的 index 可能不是Adapter的实际 position 查看[CommonAdapter.getItemViewType]
     * @return 新建的BaseViewHolder
     */
    abstract fun onCreateViewHolder(view: View, viewType: Int): CommonViewHolder

    /**
     * 同[RecyclerView.Adapter.onViewRecycled]
     *
     * @param holder Holder of the view being attached
     */
    fun onViewRecycled(holder: R) {}

    /**
     * 同[RecyclerView.Adapter.onViewAttachedToWindow]
     *
     * @param holder Holder of the view being attached
     */
    fun onViewAttachedToWindow(holder: R) {}

    fun getItemView(viewGroup: ViewGroup, viewType: Int): View? {
        return null
    }

    /**
     * 同[RecyclerView.Adapter.onViewDetachedFromWindow]
     *
     * @param holder Holder of the view being attached
     */
    fun onViewDetachedFromWindow(holder: R) {}

    fun getItemId(position: Int, data: T): Long {
        return RecyclerView.NO_ID
    }

}
