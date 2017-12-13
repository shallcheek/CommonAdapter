# CommAdapter

 RecyclerView.Adapter ,compatible with Android 4.0+.

## Gradle
``` 
 implementation 'com.chaek.android:commonAdapter:1.0.2'
```

## Example
``` 
              CommonAdapter commonAdapter = new CommonAdapter().register(ChatItemView.class,ChatTimeView.class);
               
               List<Object> chatData = new ArrayList<>();
               chatData.add(new ChatData(0, "(｡･∀･)ﾉﾞ嗨"));
               chatData.add("10分钟前");
               chatData.add(new ChatData(0, "你在做什么"));
               chatData.add(new ChatData(1, "我在看电视剧呢！"));
               chatData.add(new ChatData(0, "什么电视剧，我最近都没电视剧追了"));
               chatData.add("3分钟前");
               chatData.add(new ChatData(1, "xxxxxxxx"));
               chatData.add(new ChatData(0, "讲的是什么"));
               chatData.add("刚刚");
               chatData.add(new ChatData(1, "每天都会跟着来来往往的居民走，送他们上班，再迎接他们下班，每天积极努力地融进这个小区，它不像其他流浪狗一样害怕人，反而很喜欢人，兽医说它很健康，是一只开心的小狗，如果能有一个爱它的主人，它一定会成为一只幸福的狗狗"));
               chatData.add(new ChatData(1, "今天美丽的山城迎来了第二届国际机器人检测认证高峰论坛。这个论坛成为机器人检测认证国际交流与合作的重要平台，对于促进机器人产业发展起到了有力的推动作用。厉害了我的重庆！最后，祝本届论坛圆满成功"));
      
              commonAdapter.setListData(chatData);
              mList.setAdapter(commonAdapter);
              
              
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
    
```


## License
    Copyright 2017 shallcheek

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
