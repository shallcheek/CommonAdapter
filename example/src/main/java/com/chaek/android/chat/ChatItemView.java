package com.chaek.android.chat;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.chaek.android.R;
import com.chaek.android.adapter.AbstractItemView;
import com.chaek.android.adapter.BindItemData;
import com.chaek.android.adapter.CommonViewHolder;

/**
 * Auth: Chaek
 * Date: 2017/12/13
 */

@BindItemData(ChatData.class)
public class ChatItemView extends AbstractItemView<ChatData, ChatItemView.ChatViewHolder> {
    private static final int LEFT = 0;
    private static final int RIGHT = 1;

    @Override
    public int getItemViewType(int position, @NonNull ChatData data) {
        return data.getMessageType();
    }

    @Override
    public int getLayoutId(int viewType) {
        if (viewType == LEFT) {
            return R.layout.chat_left_view;
        } else {
            return R.layout.chat_right_view;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder vh, @NonNull ChatData data) {
        vh.mChatContent.setText(data.getMessage());
    }

    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull View view, int viewType) {
        return new ChatViewHolder(view);
    }

    public static class ChatViewHolder extends CommonViewHolder {
        private TextView mChatTime;
        private TextView mChatUserHead;
        private TextView mChatContent;


        public ChatViewHolder(View itemView) {
            super(itemView);
            mChatTime = (TextView) findViewById(R.id.chat_time);
            mChatUserHead = (TextView) findViewById(R.id.chat_user_head);
            mChatContent = (TextView) findViewById(R.id.chat_content);

        }
    }
}
