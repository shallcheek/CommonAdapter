package com.chaek.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.chaek.android.chat.ChatFragment;
import com.chaek.android.store.StoreFragment;

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
            case 1:
                Fragments.with(this)
                        .addToBackStack()
                        .fragment(ChatFragment.class)
                        .into(R.id.main);
                break;
            default:
        }

    }


}
