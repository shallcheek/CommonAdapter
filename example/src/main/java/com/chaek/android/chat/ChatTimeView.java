package com.chaek.android.chat;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chaek.android.R;
import com.chaek.android.adapter.AbstractItemView;
import com.chaek.android.adapter.BindItemData;
import com.chaek.android.adapter.CommonViewHolder;

/**
 * Auth: Chaek
 * Date: 2017/12/13
 */

public class ChatTimeView extends AbstractItemView<String, CommonViewHolder> {
    @Override
    public int getLayoutId(int viewType) {
        return R.layout.chat_time_item_view;
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder vh, @NonNull String data) {
        TextView textView = vh.findViewById(R.id.chat_time);
        textView.setText(data);
    }

    @Override
    public CommonViewHolder onCreateViewHolder(@NonNull View view, int viewType) {
        return new CommonViewHolder(view);
    }
}
