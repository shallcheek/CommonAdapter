package com.chaek.android.adapter


import androidx.collection.ArrayMap
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.lang.reflect.ParameterizedType
import java.util.ArrayList
import kotlin.reflect.KClass


/**
 * <mSideList>使用方法</mSideList>
 *
 *
 * `new CommonAdapter(list).register(xx.class)`
 *
 *
 * 需要注册数据类型的[AbstractItemView] 否则会报no find
 *
 *
 * 注意:
 * 添加HeadView之后 position并非list数据的position 可以通过[.getPosition] 获取list的position值
 * <br></br>
 * 在layoutManager中也需要处理 以及自定义ItemDecoration都需要处理HeadView 以及FootView
 *
 * @param listData 数据
 */
class CommonAdapter
@JvmOverloads constructor(listData: List<Any>? = null) : RecyclerView.Adapter<CommonViewHolder>(), View.OnClickListener {
    /**
     * 数据源
     */
    private var mListData = mutableListOf<Any>()
    val headerViews = mutableListOf<View>()
    var footerViews = mutableListOf<View>()
    /**
     * 所有class list
     */
    private val classList: MutableList<KClass<*>>
    /**
     * class对应的AbstractAdapterItemView 列表
     */
    private val itemViewList: MutableList<AbstractItemView<*, *>>

    /**
     * 点击事件监听
     */
    private var onRecyclerClickListener: OnRecyclerItemClickListener<Any>? = null
    /**
     * 储存tag的对应
     */
    private val positionTypeMap: ArrayMap<Int, Int>
    /**
     * @return 获取列表数据
     */
    val listData: MutableList<Any>
        get() = mListData

    /**
     * 获取数据源数量 不包含head和foot数量
     *
     * @return 获取数据源数量
     */
    val listItemCount: Int
        get() = mListData.size

    /**
     * @return head view 数量
     */
    val headerCount: Int
        get() = headerViews.size
    /**
     * @return foot view 数量
     */
    val footerCount: Int
        get() = this.footerViews.size


    init {
        this.footerViews = ArrayList()
        classList = ArrayList()
        itemViewList = ArrayList()
        positionTypeMap = ArrayMap()
        mListData = if (listData != null) {
            listData as MutableList<Any>
        } else {
            ArrayList()
        }
    }

    /**
     * 注册class对象对应的itemView
     *
     * @param itemViewHolderClass itemViewHolderClass
     *
     */
    @SafeVarargs
    fun register(itemViewHolderClass: KClass<out AbstractItemView<*, *>>): CommonAdapter {
        try {
            this.register(itemViewHolderClass = itemViewHolderClass.java)
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return this
    }

    fun register(itemViewHolderClass: Class<out AbstractItemView<*, *>>): CommonAdapter {
        try {
            this.register(itemView = itemViewHolderClass.newInstance())
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return this
    }


    /**
     * 注册对象对应的itemView
     *
     * @param itemView AbstractAdapterItemView
     * @return 返回当前对象
     */
    fun register(itemView: AbstractItemView<*, *>): CommonAdapter {
        itemView.commonAdapter = this
        val annotation = itemView::class.java.getAnnotation(BindItemData::class.java)
        if (annotation != null) {
            val annotationClass = annotation.value
            for (c in annotationClass) {
                registerType(c, itemView)
            }
        } else {
            //直接获取泛型的class
            val tClass = (itemView.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>
            registerType(tClass.kotlin, itemView)
        }
        return this
    }

    private fun registerType(c: KClass<*>, itemView: AbstractItemView<*, *>) {
        classList.add(c)
        itemViewList.add(itemView)
    }


    override fun getItemViewType(position: Int): Int {
        val hCount = headerCount
        val itemCount = mListData.size
        return when {
            position < hCount -> HEADERS_START + position
            position < hCount + itemCount -> getPositionItemType(position - hCount)
            else -> FOOTERS_START + position - hCount - itemCount
        }
    }

    private fun findClassIndex(kClass: KClass<*>): Int {
        var classIndex = classList.indexOf(kClass)
        if (classIndex < 0) {
            classIndex = classList.indexOf(Any::class)
        }
        return classIndex
    }

    /**
     * 获取position对应的ViewType
     *
     * @param position 位置
     * @return ViewType
     */
    private fun getPositionItemType(position: Int): Int {
        val item = mListData[position]
        val classIndex = findClassIndex(item::class)
        requireNotNull(classIndex < 0, { "not find $item register" })
        val itemView = itemViewList[classIndex] as AbstractItemView<Any, *>
        val viewType = itemView.getItemViewType(position, item)
        require(!(viewType < -1 || viewType > MAX_ITEM_TYPE)) { "view type range 0~100 (viewTyp=$viewType)" }
        val result = classIndex * MAX_ITEM_TYPE + viewType
        positionTypeMap[result] = classIndex
        return result
    }

    override fun getItemId(position: Int): Long {
        if (!isHeadFootView(position)) {
            val index = getPosition(position)
            val data = mListData[index]
            val classIndex = findClassIndex(data::class)
            val itemView = itemViewList[classIndex] as AbstractItemView<Any, *>
            return itemView.getItemId(index, data)
        }
        return super.getItemId(position)
    }

    private fun getLayoutView(v: ViewGroup, layoutId: Int): View {
        return LayoutInflater.from(v.context).inflate(layoutId, v, false)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CommonViewHolder {
        when {
            viewType < HEADERS_START + headerCount -> {
                return CommonViewHolder(headerViews[viewType - HEADERS_START])
            }
            viewType < FOOTERS_START + footerCount -> {
                return CommonViewHolder(footerViews[viewType - FOOTERS_START])
            }
            else -> {
                val indexOf = findItemTypeIndex(viewType)
                val vh = getViewTypeItemView(viewType)
                val realType = viewType - indexOf * MAX_ITEM_TYPE
                var view = vh.getItemView(viewGroup, realType)
                if (view == null) {
                    view = getLayoutView(viewGroup, vh.getLayoutId(realType))
                }
                val viewHolder = vh.onCreateViewHolder(view, realType)
                viewHolder.setOnClickListener(this)
                return viewHolder
            }
        }
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        val hCount = headerCount
        if (position >= hCount && position < hCount + mListData.size) {
            holder.setHeadCount(headerCount)
            holder.rootView.setTag(RECYCLER_CLICK, holder)
            holder.setHeadCount(hCount)
            val item = mListData[holder.itemPosition]
            val vh = getViewTypeItemView(holder.itemViewType)
            vh.onBindViewHolder(holder, item)
        } else {
            holder.setIsRecyclable(false)
        }
    }

    /**
     * 获取viewType对应的AbstractAdapterItemView
     *
     * @param viewType viewType
     * @return viewType对应的AbstractAdapterItemView
     */
    private fun getViewTypeItemView(viewType: Int): AbstractItemView<Any, CommonViewHolder> {
        return itemViewList[positionTypeMap[viewType]!!] as AbstractItemView<Any, CommonViewHolder>
    }


    /**
     * 页面holder被回收调用
     */
    override fun onViewRecycled(holder: CommonViewHolder) {
        super.onViewRecycled(holder)
        if (!isHeadFootViewType(holder.itemViewType)) {
            val item = getViewTypeItemView(holder.itemViewType)
            item.onViewRecycled(holder)
        }
    }

    /**
     * 当Item进入这个页面的时候调用
     */
    override fun onViewAttachedToWindow(holder: CommonViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (!isHeadFootViewType(holder.itemViewType)) {
            getViewTypeItemView(holder.itemViewType).onViewAttachedToWindow(holder)
        }
    }

    /**
     * 当Item离开这个页面的时候调用
     */
    override fun onViewDetachedFromWindow(holder: CommonViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (!isHeadFootViewType(holder.itemViewType)) {
            val itemView = getViewTypeItemView(holder.itemViewType)
            itemView.onViewDetachedFromWindow(holder)
        }
    }

    /**
     * 设置list参数
     * @param mListData list数据 list item 可以是任意数据格式 但是对应得格式都需要注册[.register]
     */
    fun setListData(mListData: MutableList<*>) {
        this.mListData = mListData as MutableList<Any>
    }

    /**
     * 移除所有数据重新添加数据
     *
     * @param list list
     */
    fun replaceList(list: List<*>) {
        this.mListData.clear()
        this.mListData.addAll(list as List<Any>)
    }


    /**
     * 添加单条数据
     *
     * @param any 数据 任意参数格式但是需要[.register] 不然会报错
     */
    fun add(any: Any) {
        this.mListData.add(any)
    }

    /**
     * 添加列表数据
     *
     * @param listData list数据
     */
    fun addListData(listData: List<*>) {
        this.mListData.addAll(listData as List<Any>)
    }

    /**
     * 添加HeadView
     *
     * @param view view
     */
    fun addHeaderView(view: View?) {
        view?.let {
            headerViews.add(view)
        }
    }

    /**
     * 添加FooterView
     *
     * @param view view
     */
    fun addFooterView(view: View) {
        this.footerViews.add(view)
    }

    /**
     * 移除头部View 内部实现了刷新无需调用notifyDataSetChanged
     * 因为View是固定的所以头部或底部View设置了 [RecyclerView.ViewHolder.setIsRecyclable]
     * 避免回收移除的问题
     *
     * @param headView headView
     */
    fun removeHeadView(headView: View?) {
        requireNotNull(headView, { "headView cannot null" })
        if (headerViews.contains(headView)) {
            headerViews.remove(headView)
        }
    }

    /**
     * 移除FootView
     *
     * @param footView footVew
     */
    fun removeFootView(footView: View?) {
        requireNotNull(footView, { "footView cannot null" })
        if (this.footerViews.contains(footView)) {
            this.footerViews.remove(footView)
        }
    }


    /**
     * 获取注册的AbstractAdapterItemView 列表中的下标
     *
     * @param viewType item View type
     * @return 注册的AbstractAdapterItemView 列表中的下标
     */
    private fun findItemTypeIndex(viewType: Int): Int {
        return positionTypeMap[viewType]!!
    }

    /**
     * 获取所有数据源包括head foot view数量
     */
    override fun getItemCount(): Int {
        return listItemCount + headerViews.size + this.footerViews.size
    }

    /**
     * 根据adapter的item position 获取list对应下标
     *
     * @param position adapter 中的位置
     * @return 获取list对应下标
     */
    fun getPosition(position: Int): Int {
        return position - headerCount
    }

    /**
     * 根据adapter的item  获取list数据
     *
     * @param adapterPosition adapter中的位置
     * @return 数据
     */
    fun getListItemData(adapterPosition: Int): Any? {
        return getListData(adapterPosition - headerCount)
    }

    /**
     * 获取数据
     *
     * @param listIndex 列表list对应的position 设置头部之后position 不是adapter的position
     * @return position 实际数据
     */
    fun getListData(listIndex: Int): Any? {
        return if (listIndex >= mListData.size) {
            null
        } else mListData[listIndex]
    }


    /**
     * 设置点击item的点击事件
     *
     * @param onRecyclerClickListener 回调
     */
    fun setOnItemClickListener(onRecyclerClickListener: OnRecyclerItemClickListener<*>) {
        this.onRecyclerClickListener = onRecyclerClickListener as OnRecyclerItemClickListener<Any>
    }

    /**
     * 判断ViewType是否属于head 或者foot
     *
     * @param viewType viewType
     * @return true是 false不是
     */
    private fun isHeadFootViewType(viewType: Int): Boolean {
        return viewType < 0
    }

    /**
     * 判断position是否属于head 或者foot
     *
     * @param position adapter position
     * @return true是 false不是
     */
    fun isHeadFootView(position: Int): Boolean {
        return !(position >= headerCount && position < headerCount + mListData.size)
    }

    override fun onClick(v: View) {
        val commonViewHolder = v.getTag(RECYCLER_CLICK) as CommonViewHolder
        if (onRecyclerClickListener != null) {
            val position = commonViewHolder.layoutPosition - headerCount
            onRecyclerClickListener?.onClick(mListData[position], position)
        }
    }

    /**
     * 清除所有的Foot View
     */
    fun clearFootViews() {
        this.footerViews.clear()
    }

    /**
     * 清除所有的Head View
     */
    fun clearHeadViews() {
        headerViews.clear()
    }




    companion object {
        private val TAG = "CommonAdapter"
        /**
         * item 点击事件的保存data的的tag id
         */
        private const val RECYCLER_CLICK = 0x7f0f0010
        /**
         * 最大单个最大itemType数量
         */
        private const val MAX_ITEM_TYPE = 100
        private const val HEADERS_START = Integer.MIN_VALUE
        private const val FOOTERS_START = Integer.MIN_VALUE + MAX_ITEM_TYPE
    }


}
