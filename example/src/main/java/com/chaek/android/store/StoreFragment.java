package com.chaek.android.store;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chaek.android.BaseFragment;
import com.chaek.android.R;
import com.chaek.android.library.AbstractAdapterItemView;
import com.chaek.android.library.AdapterItemData;
import com.chaek.android.library.CommonAdapter;
import com.chaek.android.library.CommonSpanSizeLookup;
import com.chaek.android.library.CommonViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Auth: Chaek
 * Date: 2017/12/8
 */

public class StoreFragment extends BaseFragment {
    private RecyclerView mList;

    @Override
    public int getLayoutViewId() {
        return R.layout.fragment_main;
    }

    @Override
    public void initView() {
        mList = (RecyclerView) findViewById(R.id.list);
        mList.setLayoutManager(new LinearLayoutManager(getActivity()));
        final CommonAdapter commonAdapter = new CommonAdapter()
                .register(StoreFragment.StoreAppItemView.class, StoreRecommendItemView.class)
                .register(StoreCateItemView.class);
        final List<Object> list = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            list.add(new RecommendItem(""));
        }
        for (int i = 0; i < 4; i++) {
            list.add(new CateItem(""));
        }

        for (int i = 0; i < 1000; i++) {
            list.add(new AppItem(""));
        }
        commonAdapter.setListData(list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        commonAdapter.addHeaderView(LayoutInflater.from(getActivity()).inflate(R.layout.stort_recommend_item_view, mList, false));
        commonAdapter.addFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.stort_recommend_item_view, mList, false));


        gridLayoutManager.setSpanSizeLookup(new CommonSpanSizeLookup(commonAdapter, 4) {
            @Override
            protected int getCommonSpanSize(int position) {
                Object p = commonAdapter.getListItem(position);
                if (p instanceof RecommendItem) {
                    return 2;
                }
                if (p instanceof CateItem) {
                    return 1;
                }
                if (p instanceof AppItem) {
                    return 4;
                }
                return 4;
            }
        });
        mList.setLayoutManager(gridLayoutManager);
        mList.setAdapter(commonAdapter);
    }

    @Override
    protected void initData() {

    }

    @AdapterItemData(AppItem.class)
    public static class StoreAppItemView extends AbstractAdapterItemView<AppItem, CommonViewHolder> {

        @Override
        public int getLayoutId(int position) {
            return R.layout.stort_app_item_view;
        }

        @Override
        public void onBindViewHolder(@NonNull CommonViewHolder vh, @NonNull AppItem data) {

        }

        @Override
        public CommonViewHolder onCreateViewHolder(@NonNull View view, int position) {
            return new CommonViewHolder(view);
        }
    }

    @AdapterItemData(CateItem.class)
    public static class StoreCateItemView extends AbstractAdapterItemView<CateItem, CommonViewHolder> {

        @Override
        public int getLayoutId(int position) {
            return R.layout.stort_cate_item_view;
        }

        @Override
        public void onBindViewHolder(@NonNull CommonViewHolder vh, @NonNull CateItem data) {

        }

        @Override
        public CommonViewHolder onCreateViewHolder(@NonNull View view, int position) {
            return new CommonViewHolder(view);
        }
    }

    @AdapterItemData(RecommendItem.class)
    public static class StoreRecommendItemView extends AbstractAdapterItemView<RecommendItem, CommonViewHolder> {


        @Override
        public int getLayoutId(int position) {
            return R.layout.stort_recommend_item_view;
        }

        @Override
        public void onBindViewHolder(@NonNull CommonViewHolder vh, @NonNull RecommendItem data) {

        }

        @Override
        public CommonViewHolder onCreateViewHolder(@NonNull View view, int position) {
            return new CommonViewHolder(view);
        }
    }
}
