

package com.chaek.android.adapter;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * 统一适配器
 *
 * @author Chaek
 */
public class CommonAdapter extends RecyclerView.Adapter<CommonViewHolder> implements View.OnClickListener {
    /**
     * item 点击事件的保存data的的tag id
     */
    private static final int RECYCLER_CLICK = 0x7f0f0010;
    /**
     * 最大单个最大itemType数量
     */
    private static final int MAX_ITEM_TYPE = 100;
    private static final int HEADERS_START = Integer.MIN_VALUE;
    private static final int FOOTERS_START = Integer.MIN_VALUE + 20;
    private List<View> mHeaderViews, mFooterViews;
    /**
     * 数据源
     */
    private List<Object> listData;

    /**
     * 点击事件监听
     */
    private OnRecyclerItemClickListener onRecyclerClickListener;
    /**
     * 所有class list
     */
    private List<Class> classList;
    /**
     * class对应的AbstractAdapterItemView 列表
     */
    private List<AbstractAdapterItemView> itemViewList;
    /**
     * 储存tag的对应
     */
    private ArrayMap<Integer, Integer> positionTypeMap;

    public CommonAdapter() {
        this(null);
    }

    public CommonAdapter(List listData) {
        mHeaderViews = new ArrayList<>();
        mFooterViews = new ArrayList<>();
        classList = new ArrayList<>();
        itemViewList = new ArrayList<>();
        positionTypeMap = new ArrayMap<>(100);
        if (listData == null) {
            listData = new ArrayList<>();
        }
        this.listData = listData;
    }

    /**
     * 注册class对象对应的itemView
     *
     * @param itemViewHolderClass itemViewHolderClass
     * @return 返回当前对象
     */
    public CommonAdapter register(Class... itemViewHolderClass) {
        try {
            for (Class itemClass : itemViewHolderClass) {
                this.register((AbstractAdapterItemView) itemClass.newInstance());
            }
            return this;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 注册对象对应的itemView
     *
     * @param itemViewHolder AbstractAdapterItemView
     * @return 返回当前对象
     */
    public CommonAdapter register(AbstractAdapterItemView itemViewHolder) {
        if (itemViewHolder == null) {
            throw new NullPointerException("BaseItemViewHolder not null ");
        }
        AdapterItemData annotation = itemViewHolder.getClass().getAnnotation(AdapterItemData.class);
        if (annotation == null) {
            throw new NullPointerException(itemViewHolder.getClass() + " not find @AdapterItemData(xx.class) annotation");
        }
        itemViewHolder.setCommonAdapter(this);
        Class<?>[] annotationClass = annotation.value();
        for (Class<?> c : annotationClass) {
            classList.add(c);
            itemViewList.add(itemViewHolder);
        }
        return this;
    }

    @Override
    public int getItemViewType(int position) {
        int hCount = getHeaderCount();
        int itemCount = listData.size();
        if (position < hCount) {
            return HEADERS_START + position;
        } else if (position < hCount + itemCount) {
            return getPositionItemType(position - hCount);
        } else {
            return FOOTERS_START + position - hCount - itemCount;
        }
    }

    /**
     * 获取position对应的ViewType
     *
     * @param position 位置
     * @return ViewType
     */
    private int getPositionItemType(int position) {
        Object item = listData.get(position);
        int indexOf = classList.indexOf(item.getClass());
        //获取下标
        int viewType = itemViewList.get(indexOf).getItemViewType(position, item);
        int result = indexOf * MAX_ITEM_TYPE + viewType;

        positionTypeMap.put(result, indexOf);
        return result;
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        int hCount = getHeaderCount();
        if (position >= hCount && position < hCount + listData.size()) {
            //设置参数
            holder.setHeadCount(getHeaderCount());
            holder.getRootView().setTag(RECYCLER_CLICK, holder.getItemPosition());
            holder.setHeadCount(hCount);
            Object item = listData.get(holder.getItemPosition());

            AbstractAdapterItemView vh = findItemViewHolder(holder.getItemViewType());
            vh.onBindViewHolder(holder, item);
        } else {
            holder.setIsRecyclable(false);
        }
    }

    /**
     * 获取ViewType对应的AbstractAdapterItemView
     *
     * @param viewType viewType
     * @return viewType对应的AbstractAdapterItemView
     */
    @NonNull
    private AbstractAdapterItemView findItemViewHolder(int viewType) {
        int indexOf = positionTypeMap.get(viewType);
        AbstractAdapterItemView item = itemViewList.get(indexOf);
        if (item == null) {
            throw new NullPointerException("not register map BaseItemViewHolder");
        }
        return item;
    }


    /**
     * 页面holder被回收调用
     */
    @Override
    public void onViewRecycled(CommonViewHolder holder) {
        super.onViewRecycled(holder);
        if (!isHeadFootViewType(holder.getItemViewType())) {
            findItemViewHolder(holder.getItemViewType()).onViewRecycled(holder, obtainListItemData(holder));
        }
    }

    /**
     * 当Item进入这个页面的时候调用
     */
    @Override
    public void onViewAttachedToWindow(CommonViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (!isHeadFootViewType(holder.getItemViewType())) {
            findItemViewHolder(holder.getItemViewType()).onViewAttachedToWindow(holder, obtainListItemData(holder));
        }
    }

    /**
     * 当Item离开这个页面的时候调用
     */
    @Override
    public void onViewDetachedFromWindow(CommonViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (!isHeadFootViewType(holder.getItemViewType())) {
            findItemViewHolder(holder.getItemViewType()).onViewDetachedFromWindow(holder, obtainListItemData(holder));
        }
    }

    /**
     * 获取item data
     */
    private Object obtainListItemData(CommonViewHolder holder) {
        return listData.isEmpty() || holder.getItemPosition() >= listData.size() ? null : listData.get(holder.getItemPosition());
    }


    /**
     * @return 获取列表数据
     */
    public List<Object> getListData() {
        return listData;
    }

    /**
     * 设置参数
     *
     * @param listData list数据
     */
    public void setListData(List listData) {
        this.listData.clear();
        this.listData.addAll(listData);
    }

    /**
     * 添加单条数据
     *
     * @param object 数据
     */
    public void addListData(Object object) {
        this.listData.add(object);
    }

    /**
     * 添加列表数据
     *
     * @param listData list数据
     */
    public void addListData(List<Object> listData) {
        this.listData.addAll(listData);
    }

    /**
     * 添加HeadView
     *
     * @param view view
     */
    public void addHeaderView(View view) {
        mHeaderViews.add(view);
    }

    /**
     * 添加FooterView
     *
     * @param view view
     */
    public void addFooterView(View view) {
        mFooterViews.add(view);
    }

    /**
     * 移除headView
     *
     * @param headView headView
     */
    public void removeHeadView(View headView) {
        if (mHeaderViews.contains(headView)) {
            mHeaderViews.remove(headView);
        }
    }

    /**
     * 移除FootView
     *
     * @param footView footVew
     */
    public void removeFootView(View footView) {
        if (mFooterViews.contains(footView)) {
            mFooterViews.remove(footView);
        }
    }

    private View getLayoutView(ViewGroup v, int layoutId) {
        return LayoutInflater.from(v.getContext()).inflate(layoutId, v, false);
    }


    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType < HEADERS_START + getHeaderCount()) {
            return new CommonViewHolder(mHeaderViews.get(viewType - HEADERS_START));
        } else if (viewType < FOOTERS_START + getFooterCount()) {
            return new CommonViewHolder(mFooterViews.get(viewType - FOOTERS_START));
        } else {
            int indexOf = findItemTypeIndex(viewType);
            AbstractAdapterItemView vh = findItemViewHolder(viewType);
            int realType = viewType - indexOf * MAX_ITEM_TYPE;
            View v = getLayoutView(viewGroup, vh.getLayoutId(realType));
            CommonViewHolder viewHolder = vh.onCreateViewHolder(v, realType);
            viewHolder.setOnClickListener(this);
            return viewHolder;
        }
    }

    /**
     * 获取注册的AbstractAdapterItemView 列表中的下标
     *
     * @param viewType item View type
     * @return 注册的AbstractAdapterItemView 列表中的下标
     */
    private int findItemTypeIndex(int viewType) {
        return positionTypeMap.get(viewType);
    }

    /**
     * 获取所有数据源包括head foot view数量
     */
    @Override
    public int getItemCount() {
        return getListItemCount() + mHeaderViews.size() + mFooterViews.size();
    }


    /**
     * 获取数据源数量 不包含head和foot数量
     *
     * @return 获取数据源数量
     */
    public int getListItemCount() {
        return listData != null ? listData.size() : 0;
    }

    /**
     * @return head view 数量
     */
    public int getHeaderCount() {
        return mHeaderViews.size();
    }

    /**
     * @return foot view 数量
     */
    public int getFooterCount() {
        return mFooterViews.size();
    }

    /**
     * 根据adapter的item position 获取list对应下标
     *
     * @param position adapter 中的位置
     * @return 获取list对应下标
     */
    public int getPosition(int position) {
        return position - getHeaderCount();
    }

    /**
     * 根据adapter的item  获取list数据
     *
     * @param adaptePosition adapter中的位置
     * @return 数据
     */
    public Object getItemData(int adaptePosition) {
        return listData.get(adaptePosition - getHeaderCount());
    }

    /**
     * 获取数据
     *
     * @param listIndex 列表list对应的position 设置头部之后position 不是adapter的position
     * @return position 实际数据
     */
    public Object getListItem(int listIndex) {
        return listData.get(listIndex);
    }


    /**
     * 设置点击item的点击事件
     *
     * @param onRecyclerClickListener 回调
     */
    public void setOnItemClickListener(OnRecyclerItemClickListener onRecyclerClickListener) {
        this.onRecyclerClickListener = onRecyclerClickListener;
    }

    /**
     * 判断ViewType是否属于head 或者foot
     *
     * @param viewType viewType
     * @return true是 false不是
     */
    private boolean isHeadFootViewType(int viewType) {
        return viewType <= HEADERS_START + getHeaderCount() || viewType <= FOOTERS_START + getFooterCount() || viewType >= listData.size();
    }

    /**
     * 判断position是否属于head 或者foot
     *
     * @param position adapter position
     * @return true是 false不是
     */
    public boolean isHeadFootView(int position) {
        return !(position >= getHeaderCount() && position < (getHeaderCount() + listData.size()));
    }

    @Override
    public void onClick(View v) {
        int p = (int) v.getTag(RECYCLER_CLICK);
        if (onRecyclerClickListener != null) {
            onRecyclerClickListener.onClick(listData.get(p), p);
        }
    }

    public List<View> getHeaderViews() {
        return mHeaderViews;
    }

    public List<View> getFooterViews() {
        return mFooterViews;
    }

    public void clearFootViews() {
        mFooterViews.clear();
    }

    public void clearHeadViews() {
        mHeaderViews.clear();
    }
}
