package com.jacky.compatible.launcher.features.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.leanback.widget.BaseGridView;
import androidx.leanback.widget.OnChildSelectedListener;
import androidx.leanback.widget.OnChildViewHolderSelectedListener;
import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.RecyclerView;

import com.jacky.compatible.launcher.adapter.AppAdapter;
import com.jacky.compatible.launcher.adapter.RecyclerItemClickListener;
import com.jacky.compatible.launcher.main.MainActivity;
import com.jacky.compatible.launcher.model.AppBean;
import com.jacky.launcher.R;
import com.jacky.uikit.fragment.BaseFragment;

import java.util.List;

public class AppFragment extends BaseFragment {

    private MainActivity mParent;
    private List<AppBean> mAppList;
    private Receiver receiver;
    //    private GridView mGridView;
    private AppAdapter mAdapter;
    public Context context;

    private VerticalGridView mVGridView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParent = (MainActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_app, null);
        init(view);
        return view;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
    }

    public void init(View view) {
        context = this.getContext();

        mVGridView = (VerticalGridView) view.findViewById(R.id.app_grid);

        mVGridView.setNumColumns(4);

        AppDataManage getAppInstance = new AppDataManage(mParent);
        mAppList = getAppInstance.getLaunchAppList();

        mAdapter = new AppAdapter(this, getContext(), mAppList);
//        mGridView.setAdapter(mAdapter);

        mVGridView.setAdapter(mAdapter);
        mVGridView.requestFocus();

        mVGridView.setOnChildSelectedListener(new OnChildSelectedListener() {
            @Override
            public void onChildSelected(ViewGroup parent, View view, final int position, long id) {
                Log.i("sajad", "select!");
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("sajad", "click!");
                        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(
                                mAppList.get(position).getPackageName());
                        if (launchIntent != null) {
                            context.startActivity(launchIntent);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void requestInitFocus() {
        if (mAdapter.getItemCount() > 0) {
            mVGridView.getChildAt(0).requestFocus();
            mVGridView.getChildAt(0).requestFocus();

        }
    }

    private boolean isFocusOnTopLine() {
        int count = mAdapter.getItemCount();
        if (mAdapter.getItemCount() > 5) {
            count = 5;
        }
        for (int i = 0; i < count; i++) {
            View view = mVGridView.getChildAt(i);
            if (view.hasFocus()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                if (isFocusOnTopLine()) {
                    mParent.requestTabFocus();
                    return true;
                }
                return false;
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        receiver = new Receiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_ADDED");
        filter.addAction("android.intent.action.PACKAGE_REMOVED");
        filter.addDataScheme("package");
        mParent.registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            mParent.unregisterReceiver(receiver);
        }
    }

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {

            }
            if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {

            }
        }
    }
}


