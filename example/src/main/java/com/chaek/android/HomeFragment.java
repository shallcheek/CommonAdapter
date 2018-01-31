package com.chaek.android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chaek.android.adapter.AbstractItemView;
import com.chaek.android.adapter.BindItemData;
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
        final CommonAdapter commonAdapter = new CommonAdapter().register(MainItemView.class);
        final List<Object> list = new ArrayList<>();
        list.add("应用商店");
        list.add("聊天对话");
        list.add("样式1");
        list.add("样式2");
        list.add(1);
        list.add(2);
        commonAdapter.setListData(list);
        commonAdapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onClick(Object t, int index) {
                mainSwitchListener.switchFragment(index);
                if (index > 1) {
                    list.remove(index);
                    commonAdapter.notifyItemRemoved(commonAdapter.getHeaderCount() + index);
                }
            }
        });
        mList.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL));

        View v;
        commonAdapter.addHeaderView(v = LayoutInflater.from(getActivity()).inflate(R.layout.stort_recommend_item_view, mList, false));
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonAdapter.removeHeadView(v);
            }
        });
        for (int i = 0; i < 4; i++) {
            final View f = LayoutInflater.from(mList.getContext()).inflate(R.layout.stort_recommend_item_view, mList, false);
            TextView text = f.findViewById(R.id.title);
            text.setText("底部" + i);
            f.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Object> objects=new ArrayList<>();
                    objects.add("测试样式1");
                    objects.add("测试样式1");
                    commonAdapter.addListData(objects);
                }
            });
            commonAdapter.addFooterView(f);
        }
        mList.setAdapter(commonAdapter);
        commonAdapter.notifyDataSetChanged();

    }

    @Override
    protected void initData() {

    }

    @BindItemData(value = {String.class, Integer.class})
    public static class MainItemView extends AbstractItemView<Object, CommonViewHolder> {

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
