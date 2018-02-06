package com.inverseapps.punchcard.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.ui.ForegroundTaskDelegate;
import com.inverseapps.punchcard.ui.PCActivity;

import java.util.List;
import java.util.Vector;

import butterknife.ButterKnife;

/**
 * Created by Inverse, LLC on 10/17/16.
 */

public abstract class PCFragment extends Fragment {

    @LayoutRes
    protected int getRootLayoutRes() {
        return R.layout.holder_empty;
    }

    protected List<ForegroundTaskDelegate> listOfForegroundTaskDelegates;
    public PCActivity pcActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof PCActivity) {
            pcActivity = (PCActivity)context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(getRootLayoutRes(), container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listOfForegroundTaskDelegates = new Vector<>();
    }

    @Override
    public void onDestroyView() {
        for (ForegroundTaskDelegate delegate: listOfForegroundTaskDelegates) {
            if (delegate != null) {
                delegate.cancel();
            }
        }
        super.onDestroyView();
    }
}
