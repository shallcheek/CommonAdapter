package com.chaek.android.store;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chaek.android.BaseFragment;
import com.chaek.android.R;
import com.chaek.android.adapter.AbstractItemView;
import com.chaek.android.adapter.BaseItemView;
import com.chaek.android.adapter.BindItemData;
import com.chaek.android.adapter.CommonAdapter;
import com.chaek.android.adapter.CommonSpanSizeLookup;
import com.chaek.android.adapter.CommonViewHolder;

import org.jetbrains.annotations.NotNull;

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
                .register(StoreFragment.StoreAppItemView.class)
                .register(StoreRecommendItemView.class)
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
            protected int getCommonSpanSize(int position, Object listItem) {
                if (listItem instanceof RecommendItem) {
                    return 2;
                }
                if (listItem instanceof CateItem) {
                    return 1;
                }
                if (listItem instanceof AppItem) {
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


    public static class StoreAppItemView extends BaseItemView<AppItem> {

        @Override
        public int getLayoutId(int viewType) {
            return R.layout.stort_app_item_view;
        }

        @Override
        public void onBindViewHolder(@NonNull CommonViewHolder vh, @NonNull AppItem data) {

        }

    }

    public static class StoreCateItemView extends AbstractItemView<CateItem, CommonViewHolder> {

        @Override
        public int getLayoutId(int viewType) {
            return R.layout.stort_cate_item_view;
        }

        @Override
        public void onBindViewHolder(@NonNull CommonViewHolder vh, @NonNull CateItem data) {

        }

        @NotNull
        @Override
        public CommonViewHolder onCreateViewHolder(@NotNull View view, int viewType) {
            return new CommonViewHolder(view);
        }
    }

    public static class StoreRecommendItemView extends AbstractItemView<RecommendItem, CommonViewHolder> {


        @Override
        public int getLayoutId(int position) {
            return R.layout.stort_recommend_item_view;
        }

        @Override
        public void onBindViewHolder(@NonNull CommonViewHolder vh, @NonNull RecommendItem data) {

        }

        @Override
        public CommonViewHolder onCreateViewHolder(@NonNull View view, int viewType) {
            return new CommonViewHolder(view);
        }
    }
}
