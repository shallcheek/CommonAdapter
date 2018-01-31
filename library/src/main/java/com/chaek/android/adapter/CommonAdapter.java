

package com.chaek.android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Chaek
 */
public class CommonAdapter extends RecyclerView.Adapter<CommonViewHolder> implements View.OnClickListener {
    private static final String TAG = "CommonAdapter";
    /**
     * item 点击事件的保存data的的tag id
     */
    private static final int RECYCLER_CLICK = 0x7f0f0010;
    /**
     * 最大单个最大itemType数量
     */
    private static final int MAX_ITEM_TYPE = 100;
    private static final int HEADERS_START = Integer.MIN_VALUE;
    private static final int FOOTERS_START = Integer.MIN_VALUE + MAX_ITEM_TYPE;
    private List<View> mHeaderViews, mFooterViews;
    private Context mContext;
    /**
     * 数据源
     */
    private List<Object> mListData;

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
    private List<AbstractItemView> itemViewList;
    /**
     * 储存tag的对应
     */
    private ArrayMap<Integer, Integer> positionTypeMap;

    public CommonAdapter() {
        this(null);
    }

    /**
     * <b>使用方法</b>
     * <p>
     * <code>new CommonAdapter(list).register(xx.class)</code>
     * <p>
     * 需要注册数据类型的{@link AbstractItemView} 否则会报no find
     * <p>
     * 注意:
     * 添加HeadView之后 position并非list数据的position 可以通过{@link #getPosition(int)} 获取list的position值
     * <br>
     * 在layoutManager中也需要处理 以及自定义ItemDecoration都需要处理HeadView 以及FootView
     *
     * @param listData 数据
     */
    public CommonAdapter(List listData) {
        mHeaderViews = new ArrayList<>();
        mFooterViews = new ArrayList<>();
        classList = new ArrayList<>();
        itemViewList = new ArrayList<>();
        positionTypeMap = new ArrayMap<>();
        if (listData != null) {
            mListData = listData;
        } else {
            mListData = new ArrayList<>();
        }
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
                this.register((AbstractItemView) itemClass.newInstance());
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
    public CommonAdapter register(AbstractItemView itemViewHolder) {
        if (itemViewHolder == null) {
            throw new NullPointerException("BaseItemViewHolder cannot null ");
        }
        BindItemData annotation = itemViewHolder.getClass().getAnnotation(BindItemData.class);
        if (annotation == null) {
            throw new NullPointerException(itemViewHolder.getClass() + "no find @AdapterItemData(xx.class) annotation");
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
        int itemCount = mListData.size();
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
        Object item = mListData.get(position);
        int classIndex = classList.indexOf(item.getClass());
        //获取下标
        int viewType = itemViewList.get(classIndex).getItemViewType(position, item);
        if (viewType < -1 || viewType > MAX_ITEM_TYPE) {
            throw new IllegalArgumentException("view type range 0~100 (viewTyp=" + viewType + ")");
        }
        int result = classIndex * MAX_ITEM_TYPE + viewType;
        positionTypeMap.put(result, classIndex);
        return result;
    }

    @Override
    public long getItemId(int position) {
        if (!isHeadFootView(position)) {
            position = getPosition(position);
            Object data = mListData.get(position);
            int classIndex = classList.indexOf(data.getClass());
            AbstractItemView itemView = itemViewList.get(classIndex);
            if (itemView != null) {
                return itemView.getItemId(position, data);
            }
        }
        return super.getItemId(position);
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup viewGroup, final int viewType) {
        this.mContext = viewGroup.getContext();
        if (viewType < HEADERS_START + getHeaderCount()) {
            return new CommonViewHolder(mHeaderViews.get(viewType - HEADERS_START));
        } else if (viewType < FOOTERS_START + getFooterCount()) {
            View f = mFooterViews.get(viewType - FOOTERS_START);
            CommonViewHolder viewHolder = new CommonViewHolder(f);
            return viewHolder;
        } else {
            int indexOf = findItemTypeIndex(viewType);
            AbstractItemView vh = findItemViewHolder(viewType);

            //获取最终的ViewType
            int realType = viewType - indexOf * MAX_ITEM_TYPE;
            View v = getLayoutView(viewGroup, vh.getLayoutId(realType));
            CommonViewHolder viewHolder = vh.onCreateViewHolder(v, realType);
            viewHolder.setOnClickListener(this);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        int hCount = getHeaderCount();
        if (position >= hCount && position < hCount + mListData.size()) {
            //设置参数
            holder.setHeadCount(getHeaderCount());
            holder.getRootView().setTag(RECYCLER_CLICK, holder);
            holder.setHeadCount(hCount);
            Object item = mListData.get(holder.getItemPosition());
            AbstractItemView vh = findItemViewHolder(holder.getItemViewType());
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
    private AbstractItemView findItemViewHolder(int viewType) {
        int indexOf = positionTypeMap.get(viewType);
        AbstractItemView item = itemViewList.get(indexOf);
        if (item == null) {
            throw new NullPointerException("not find register map BaseItemViewHolder");
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
        return mListData.isEmpty() || (holder.getItemPosition()) >= mListData.size() ? null : mListData.get(holder.getItemPosition());
    }


    /**
     * @return 获取列表数据
     */
    public List<Object> getListData() {
        return mListData;
    }

    /**
     * 设置list参数
     * <br>
     * 需要注意调用{@link #notifyDataSetChanged()} 刷新界面
     * <br>
     * 如果需要<code>DiffUtil</code> 刷新数据 则调用{@link #diffListData(List)}方法
     * 设置list参数会自动刷新界面 无需调用{@link #notifyDataSetChanged()}
     *
     * @param mListData list数据 list item 可以是任意数据格式 但是对应得格式都需要注册{@link #register(Class[])}
     */
    public void setListData(List mListData) {
        this.mListData = mListData;
    }

    /**
     * 移除所有数据重新添加数据
     *
     * @param list list
     */
    public void replaceList(List list) {
        this.mListData.clear();
        this.mListData.add(list);
        notifyDataSetChanged();
    }

    /**
     * Diff刷新界面 Adapter 根据toString()进行比对
     * <br>
     * 如自带的XDiffCallback无法满足需求可以按照此修改<br>
     * 注意 需要处理添加头部View之后position并不是list的position 可继承ListUpdateCallBack需要继承{@link CommonListUpdateCallBack }<br>
     * 里面处理了因为添加头部View之后数据源的list<br>
     * 下标位置并不是Adapter中的下标位置<br>
     * 也最好在线程中计算 主线程更新<br>
     *
     * @param newList 新的数据 注意新的list 不能跟list是同一个
     */
    @Deprecated
    public void diffListData(List newList) {
        CommonXDiffCallback xDiffCallback = new CommonXDiffCallback(getListData(), newList) {
            @Override
            protected boolean areItemsTheSame(Object oldItem, Object newItem) {
                return oldItem.toString().equals(newItem.toString());
            }

            @Override
            protected boolean areContentsTheSame(Object oldItem, Object newItem) {
                return oldItem.equals(newItem);
            }
        };

        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(xDiffCallback);
        replaceList(newList);
        diffResult.dispatchUpdatesTo(new CommonListUpdateCallBack(this));
    }

    /**
     * 添加单条数据
     *
     * @param object 数据 任意参数格式但是需要{@link #register(Class[])} 不然会报错
     */
    public void add(Object object) {
        this.mListData.add(object);
        notifyItemInserted(getHeaderCount() + mListData.size() - 1);
    }

    /**
     * 添加列表数据
     *
     * @param listData list数据
     */
    public void addListData(List<Object> listData) {
        this.mListData.addAll(listData);
        int oldSize = mListData.size() + getHeaderCount();
        mListData.addAll(listData);
        notifyItemRangeInserted(oldSize, listData.size());
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
     * 移除头部View 内部实现了刷新无需调用notifyDataSetChanged
     * 因为View是固定的所以头部或底部View设置了 {@link RecyclerView.ViewHolder#setIsRecyclable(boolean)}
     * 避免回收移除的问题
     *
     * @param headView headView
     */
    public void removeHeadView(View headView) {
        if (headView == null) {
            throw new NullPointerException("headView cannot null");
        }
        if (mHeaderViews.contains(headView)) {
            mHeaderViews.remove(headView);
            notifyDataSetChanged();
        }
    }

    /**
     * 移除FootView
     *
     * @param footView footVew
     */
    public void removeFootView(View footView) {
        if (footView == null) {
            throw new NullPointerException("footView cannot null");
        }
        if (mFooterViews.contains(footView)) {
            mFooterViews.remove(footView);
            notifyDataSetChanged();
        }
    }

    private View getLayoutView(ViewGroup v, int layoutId) {
        return LayoutInflater.from(v.getContext()).inflate(layoutId, v, false);
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
        return mListData != null ? mListData.size() : 0;
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
     * @param adapterPosition adapter中的位置
     * @return 数据
     */
    public Object getListItemData(int adapterPosition) {
        return getListData(adapterPosition - getHeaderCount());
    }

    /**
     * 获取数据
     *
     * @param listIndex 列表list对应的position 设置头部之后position 不是adapter的position
     * @return position 实际数据
     */
    public Object getListData(int listIndex) {
        return mListData.get(listIndex);
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
        return viewType < 0;
    }

    /**
     * 判断position是否属于head 或者foot
     *
     * @param position adapter position
     * @return true是 false不是
     */
    public boolean isHeadFootView(int position) {
        return !(position >= getHeaderCount() && position < (getHeaderCount() + mListData.size()));
    }

    @Override
    public void onClick(View v) {
        CommonViewHolder commonViewHolder = (CommonViewHolder) v.getTag(RECYCLER_CLICK);
        if (onRecyclerClickListener != null) {
            int position = commonViewHolder.getLayoutPosition() - getHeaderCount();
            onRecyclerClickListener.onClick(mListData.get(position), position);
        }
    }

    public List<View> getHeaderViews() {
        return mHeaderViews;
    }

    public List<View> getFooterViews() {
        return mFooterViews;
    }

    /**
     * 清除所有的Foot View
     */
    public void clearFootViews() {
        int footCount = getFooterCount();
        mFooterViews.clear();
        notifyDataSetChanged();
    }

    /**
     * 清除所有的Head View
     */
    public void clearHeadViews() {
        int headCount = getHeaderCount();
        mHeaderViews.clear();
        notifyDataSetChanged();
    }


}
