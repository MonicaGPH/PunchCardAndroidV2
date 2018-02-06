package com.inverseapps.punchcard.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.adapters.PunchAdapter;
import com.inverseapps.punchcard.model.UserCheckInOut;
import com.inverseapps.punchcard.ui.ForegroundTaskDelegate;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;

public class MyPunchesFragment extends PCFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.fragment_my_punches;
    }

    @NonNull
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @NonNull
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<UserCheckInOut> listOfUserCheckInOuts;
    private PunchAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ForegroundTaskDelegate findUserCheckInOutDelegate;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findUserCheckInOutDelegate = new FindUserCheckInOutDelegate(this);
        listOfForegroundTaskDelegates.add(findUserCheckInOutDelegate);

        listOfUserCheckInOuts = new Vector<>();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new PunchAdapter(listOfUserCheckInOuts);
        adapter.buildUserCheckInOutTable();
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeResources(R.color.logo_orange);
        swipeRefreshLayout.setOnRefreshListener(this);

        pcActivity.getPCApplication().getPcFunctionService().findUserCheckIns(findUserCheckInOutDelegate);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        pcActivity.getPCApplication().getPcFunctionService().findUserCheckIns(findUserCheckInOutDelegate);

    }

    private static class FindUserCheckInOutDelegate extends ForegroundTaskDelegate<List<UserCheckInOut>> {

        private final WeakReference<MyPunchesFragment> fragmentWeakReference;

        public FindUserCheckInOutDelegate(MyPunchesFragment fragment) {
            super(fragment.pcActivity);
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void onPostExecute(List<UserCheckInOut> userCheckInOuts, Throwable throwable) {
            super.onPostExecute(userCheckInOuts, throwable);

            MyPunchesFragment fragment = fragmentWeakReference.get();
            if (throwable == null &&
                    fragment != null && !fragment.isDetached() && !fragment.isRemoving()) {
                fragment.listOfUserCheckInOuts.clear();
                fragment.listOfUserCheckInOuts.addAll(userCheckInOuts);
                fragment.adapter.buildUserCheckInOutTable();
                fragment.adapter.notifyDataSetChanged();

                fragment.swipeRefreshLayout.setRefreshing(false);
            }
        }
    }
}
