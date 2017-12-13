package com.chaek.android.chat;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chaek.android.BaseFragment;
import com.chaek.android.R;
import com.chaek.android.adapter.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Auth: Chaek
 * Date: 2017/12/13
 */

public class ChatFragment extends BaseFragment {
    private RecyclerView mList;

    private CommonAdapter commonAdapter;

    @Override
    public int getLayoutViewId() {
        return R.layout.fragment_main;
    }

    @Override
    public void initView() {
        mList = (RecyclerView) findViewById(R.id.list);
        mList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    protected void initData() {
        commonAdapter = new CommonAdapter().register(ChatItemView.class,ChatTimeView.class);
        List<Object> chatData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chatData.add(new ChatData(0, "(｡･∀･)ﾉﾞ嗨"));
            chatData.add("10分钟前");
            chatData.add(new ChatData(0, "你在做什么"));
            chatData.add(new ChatData(1, "我在看电视剧呢！"));
            chatData.add(new ChatData(0, "什么电视剧，我最近都没电视剧追了"));
            chatData.add("3分钟前");
            chatData.add(new ChatData(1, "九州海上牧云记呢"));
            chatData.add(new ChatData(0, "讲的是什么"));
            chatData.add("刚刚");
            chatData.add(new ChatData(1, "每天都会跟着来来往往的居民走，送他们上班，再迎接他们下班，每天积极努力地融进这个小区，它不像其他流浪狗一样害怕人，反而很喜欢人，兽医说它很健康，是一只开心的小狗，如果能有一个爱它的主人，它一定会成为一只幸福的狗狗"));
            chatData.add(new ChatData(1, "今天美丽的山城迎来了第二届国际机器人检测认证高峰论坛。这个论坛成为机器人检测认证国际交流与合作的重要平台，对于促进机器人产业发展起到了有力的推动作用。厉害了我的重庆！最后，祝本届论坛圆满成功"));
        }

        commonAdapter.setListData(chatData);
        mList.setAdapter(commonAdapter);
    }
}
