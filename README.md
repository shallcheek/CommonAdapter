# CommAdapter

 RecyclerView.Adapter ,compatible with Android 4.0+.

## gradle

 
``` 
 implementation 'com.chaek.android:commonAdapter:1.0.0'
```

## example
``` 
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
        commonAdapter.addHeaderView(LayoutInflater.from(getActivity()).inflate(R.layout.stort_recommend_item_view,mList,false));
        mList.setAdapter(commonAdapter);
        commonAdapter.addFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.stort_recommend_item_view,mList,false));
        commonAdapter.notifyDataSetChanged();
        mList.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL));
    
        @AdapterItemData(value = {String.class, Integer.class})
        public static class MainItemView extends AbstractAdapterItemView<Object, CommonViewHolder> {
    
            @Override
            public int getLayoutId(int position, @NonNull Object data) {
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
            public CommonViewHolder onCreateViewHolder(@NonNull View view, int position, @NonNull Object data) {
                return new CommonViewHolder(view);
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
