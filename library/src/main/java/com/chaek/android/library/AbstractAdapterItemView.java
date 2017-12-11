package com.chaek.android.library;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


/**
 * <code>CommonKyAdapter</code> 对应的子视图样式 一种数据源对应一个样式 也可多种数据对应一种
 * 调用方式为:
 * <br>
 * <code>new CommonKyAdapter().register(AbstractAdapterItemView.class)</code>
 * <br>
 * 重写 <br>{@link #getLayoutId(int, Object)},<br>{@link #onCreateViewHolder(View, int, Object)},<br>{@link #onBindViewHolder(CommonViewHolder, Object)}
 * 即可<br>
 * 注意事项:继承AbstractAdapterItemView使用一定要对应的注解相应的数据源格式 {@link AdapterItemData}
 * <p>
 *
 * @author: Chaek
 * @data: 2017/12/6
 */
public abstract class AbstractAdapterItemView<T, R extends CommonViewHolder> {

    protected CommonAdapter commonAdapter;

    /**
     * 赋值 commonAdapter 参考{@link CommonAdapter#register(Class[])} }
     * 在初始化时 注册当前<code>AbstractAdapterItemView</code> 赋值可让<code>AbstractAdapterItemView</code>持有CommonKyAdapter对象
     * <p>
     * 获取CommonKyAdapter 实例操作应该在调用 setCommonAdapter之后在自己构建AbstractAdapterItemView的构造函数中调用会出现null
     *
     * @param commonAdapter 当前AbstractAdapterItemView所属的Adapter
     */
    public void setCommonAdapter(CommonAdapter commonAdapter) {
        this.commonAdapter = commonAdapter;
    }

    /**
     * 获取CommonKyAdapter 实例操作应该在调用 setCommonAdapter之后在自己构建AbstractAdapterItemView的构造函数中调用会出现null
     *
     * @return 当前当前AbstractAdapterItemView所属的Adapter
     */
    public CommonAdapter getCommonAdapter() {
        return commonAdapter;
    }

    /**
     * 获取当前数据对应的布局文件ID
     * 创建成view 查看{@link CommonAdapter#onCreateViewHolder(ViewGroup, int)} }
     *
     * @param position 数据源中的 index 可能不是Adapter的实际 position
     *                 查看{@link CommonAdapter#getItemViewType(int)}
     * @param data     data 当前item数据
     * @return 布局文件
     */
    @LayoutRes
    public abstract int getLayoutId(int position, @NonNull T data);


    /**
     * 将item 与 ViewHolder绑定起来 用作正常的参数设置数据绑定
     * BaseViewHolder 中可获取到context实例 来自与View {@link View#getContext()}
     * 也可获取AbstractAdapterItemView绑定的实例
     *
     * @param vh   对应的BaseViewHolder
     * @param data item data 数据
     */
    public abstract void onBindViewHolder(@NonNull R vh, @NonNull T data);

    /**
     * 初始化创建 BaseViewHolder
     *
     * @param view     来自{@link #getLayoutId(int, Object)} 对应的布局文件创建的View
     * @param position 数据源中的 index 可能不是Adapter的实际 position 查看{@link CommonAdapter#getItemViewType(int)}
     * @param data     数据
     * @return 新建的BaseViewHolder
     */
    public abstract R onCreateViewHolder(@NonNull View view, int position, @NonNull T data);

    /**
     * 同{@link RecyclerView.Adapter#onViewRecycled(RecyclerView.ViewHolder)}
     *
     * @param holder Holder of the view being attached
     * @param data   数据源 可能为空
     */
    public void onViewRecycled(@NonNull R holder, T data) {
    }

    /**
     * 同{@link RecyclerView.Adapter#onViewAttachedToWindow(RecyclerView.ViewHolder)}
     *
     * @param holder Holder of the view being attached
     * @param data   数据源 可能为空
     */
    public void onViewAttachedToWindow(@NonNull R holder, T data) {
    }

    /**
     * 同{@link RecyclerView.Adapter#onViewDetachedFromWindow(RecyclerView.ViewHolder)}
     *
     * @param holder Holder of the view being attached
     * @param data   数据源 可能为空
     */
    public void onViewDetachedFromWindow(@NonNull R holder, T data) {
    }
}
