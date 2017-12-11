package com.chaek.android;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chaek.android.library.AbstractAdapterItemView;
import com.chaek.android.library.AdapterItemData;
import com.chaek.android.library.CommonAdapter;
import com.chaek.android.library.CommonViewHolder;
import com.chaek.android.store.StoreFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainSwitchListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        Fragments.with(this)
                .fragment(HomeFragment.class)
                .into(R.id.main);
    }

    @Override
    public void switchFragment(int position) {
        switch (position) {
            case 0:
                Fragments.with(this)
                        .addToBackStack()
                        .multi()
                        .fragment(StoreFragment.class)
                        .into(R.id.main);


                break;
            default:
        }

    }


}
