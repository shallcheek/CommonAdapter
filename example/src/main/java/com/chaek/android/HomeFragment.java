package com.chaek.android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chaek.android.adapter.AbstractAdapterItemView;
import com.chaek.android.adapter.AdapterItemData;
import com.chaek.android.adapter.CommonAdapter;
import com.chaek.android.adapter.CommonViewHolder;
import com.chaek.android.adapter.OnRecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Auth: Chaek
 * Date: 2017/12/8
 */

public class HomeFragment extends BaseFragment {
    private RecyclerView mList;

    private MainSwitchListener mainSwitchListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainSwitchListener) {
            mainSwitchListener = (MainSwitchListener) context;
        }
    }

    @Override
    public int getLayoutViewId() {
        return R.layout.fragment_main;
    }

    @Override
    public void initView() {
        mList = (RecyclerView) findViewById(R.id.list);
        mList.setLayoutManager(new LinearLayoutManager(getActivity()));
        CommonAdapter commonAdapter = new CommonAdapter().register(MainItemView.class);
        List<String> list = new ArrayList<>();
        list.add("应用商店");
        list.add("关于我们");
        list.add("样式1");
        list.add("样式2");
        commonAdapter.setListData(list);
        commonAdapter.addListData(1);
        commonAdapter.addListData(2);
        commonAdapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onClick(Object t, int index) {
                mainSwitchListener.switchFragment(index);
            }
        });
        commonAdapter.addHeaderView(LayoutInflater.from(getActivity()).inflate(R.layout.stort_recommend_item_view, mList, false));
        mList.setAdapter(commonAdapter);
        commonAdapter.addFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.stort_recommend_item_view, mList, false));
        commonAdapter.notifyDataSetChanged();
        mList.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void initData() {

    }

    @AdapterItemData(value = {String.class, Integer.class})
    public static class MainItemView extends AbstractAdapterItemView<Object, CommonViewHolder> {


        @Override
        public int getLayoutId(int viewType) {
            return R.layout.main_item_view;
        }

        @Override
        public void onBindViewHolder(@NonNull CommonViewHolder vh, @NonNull Object data) {
            TextView t = vh.findViewById(R.id.btn1);
            if (data instanceof String) {
                t.setText(data.toString());
            } else if (data instanceof Integer) {
                t.setText(data + "");
            }
        }

        @Override
        public CommonViewHolder onCreateViewHolder(@NonNull View view, int viewType) {
            return new CommonViewHolder(view);
        }
    }
}
