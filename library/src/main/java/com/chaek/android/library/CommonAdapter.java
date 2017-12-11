

package com.chaek.android.library;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CommonAdapter extends RecyclerView.Adapter<CommonViewHolder> implements View.OnClickListener {
    private static final int RECYCLER_CLICK = 0x7f0f0010;
    private static final int HEADERS_START = Integer.MIN_VALUE;
    private static final int FOOTERS_START = Integer.MIN_VALUE + 10;
    private List<View> mHeaderViews, mFooterViews;
    private List<Object> listData;
    private OnRecyclerItemClickListener onRecyclerClickListener;
    private List<Class> classList;
    private List<AbstractAdapterItemView> itemViewList;
    private HashMap<Integer, Integer> positionTypeMap;

    public CommonAdapter() {
        this(null);
    }

    public CommonAdapter(List listData) {
        mHeaderViews = new ArrayList<>();
        mFooterViews = new ArrayList<>();
        classList = new ArrayList<>();
        itemViewList = new ArrayList<>();
        positionTypeMap = new HashMap<Integer, Integer>(50);
        if (listData == null) {
            listData = new ArrayList<>();
        }
        this.listData = listData;
    }

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
    public void onViewRecycled(CommonViewHolder holder) {
        super.onViewRecycled(holder);
        if (!isHeadFootViewType(holder.getItemViewType())) {
            findItemViewHolder(holder.getItemViewType()).onViewRecycled(holder, obtainListItemData(holder));
        }
    }

    @Override
    public void onViewAttachedToWindow(CommonViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (!isHeadFootViewType(holder.getItemViewType())) {
            findItemViewHolder(holder.getItemViewType()).onViewAttachedToWindow(holder, obtainListItemData(holder));
        }
    }

    @Override
    public void onViewDetachedFromWindow(CommonViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        positionTypeMap.remove(holder.getItemViewType());
        if (!isHeadFootViewType(holder.getItemViewType())) {
            findItemViewHolder(holder.getItemViewType()).onViewDetachedFromWindow(holder, obtainListItemData(holder));
        }
    }

    private Object obtainListItemData(CommonViewHolder holder) {
        return listData.isEmpty() || holder.getItemPosition() >= listData.size() ? null : listData.get(holder.getItemPosition());
    }


    public void setListData(List listData) {
        this.listData.clear();
        this.listData.addAll(listData);
    }

    public List<Object> getListData() {
        return listData;
    }

    public void addListData(Object object) {
        this.listData.add(object);
    }

    public void addListData(List<Object> listData) {
        this.listData.addAll(listData);
    }

    public void addHeaderView(View view) {
        mHeaderViews.add(view);
    }

    public void addFooterView(View view) {
        mFooterViews.add(view);
    }

    private View getLayoutView(ViewGroup v, int layoutId) {
        return LayoutInflater.from(v.getContext()).inflate(layoutId, v, false);
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        int hCount = getHeaderCount();
        if (position >= hCount && position < hCount + listData.size()) {
            holder.setHeadCount(getHeaderCount());
            holder.getRootView().setTag(RECYCLER_CLICK, holder.getItemPosition());
            holder.setHeadCount(hCount);
            Object item = listData.get(holder.getItemPosition());
            AbstractAdapterItemView vh = findItemViewHolder(holder.getItemViewType());
            vh.onBindViewHolder(holder, item);
        }
    }

    @NonNull
    private AbstractAdapterItemView findItemViewHolder(int viewType) {
        int realType = viewType / 100 - positionTypeMap.get(viewType);
        AbstractAdapterItemView item = itemViewList.get(realType);
        if (item == null) {
            throw new NullPointerException("not register map BaseItemViewHolder");
        }
        return item;
    }


    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType < HEADERS_START + getHeaderCount()) {
            return new CommonViewHolder(mHeaderViews.get(viewType - HEADERS_START));
        } else if (viewType < FOOTERS_START + getFooterCount()) {
            return new CommonViewHolder(mFooterViews.get(viewType - FOOTERS_START));
        } else {
            int typePosition = findItemTypeIndex(viewType);
            AbstractAdapterItemView vh = findItemViewHolder(viewType);
            int realType = viewType / 100 - typePosition;
            View v = getLayoutView(viewGroup, vh.getLayoutId(realType));
            CommonViewHolder viewHolder = vh.onCreateViewHolder(v, realType);
            viewHolder.setOnClickListener(this);
            return viewHolder;
        }
    }


    @Override
    public int getItemCount() {
        return getListItemCount() + mHeaderViews.size() + mFooterViews.size();
    }


    public int getListItemCount() {
        return listData != null ? listData.size() : 0;
    }

    public int getHeaderCount() {
        return mHeaderViews.size();
    }

    public int getPosition(int position) {
        return position - getHeaderCount();
    }

    public Object getItemData(int position) {
        return listData.get(position - getHeaderCount());
    }

    public Object getListItem(int position) {
        return listData.get(position);
    }


    public int getFooterCount() {
        return mFooterViews.size();
    }

    @Override
    public int getItemViewType(int position) {
        int hCount = getHeaderCount();
        int itemCount = listData.size();
        if (position < hCount) {
            return HEADERS_START + position;
        } else if (position < hCount + itemCount) {
            return findItemType(position - hCount);
        } else {
            return FOOTERS_START + position - hCount - itemCount;
        }
    }

    private int findItemType(int position) {
        Object item = listData.get(position);
        int indexOf = classList.indexOf(item.getClass());
        int viewType = itemViewList.get(indexOf).getItemViewType(position, item);
        int result = indexOf * 100 + viewType + indexOf;
        positionTypeMap.put(result, indexOf);
        return result;
    }

    private int findItemTypeIndex(int viewType) {
        return positionTypeMap.get(viewType);
    }

    public void setOnItemClickListener(OnRecyclerItemClickListener onRecyclerClickListener) {
        this.onRecyclerClickListener = onRecyclerClickListener;
    }

    private boolean isHeadFootViewType(int viewType) {
        return viewType <= HEADERS_START + getHeaderCount() || viewType <= FOOTERS_START + getFooterCount() || viewType >= listData.size();
    }

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

    public void clearFootViews() {
        mFooterViews.clear();
    }

    public void clearHeadViews() {
        mHeaderViews.clear();
    }
}
