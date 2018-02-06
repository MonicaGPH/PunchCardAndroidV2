package com.inverseapps.punchcard.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.view.View;

import android.widget.TextView;

import com.inverseapps.punchcard.BuildConfig;
import com.inverseapps.punchcard.R;


import butterknife.BindView;


public class AboutAppFragment extends PCFragment {
    @Override
    protected int getRootLayoutRes() {
        return R.layout.fragment_about_app;
    }

    @NonNull
    @BindView(R.id.lblAppVersion)
    TextView lblAppVersion;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lblAppVersion.setText("Version: " + BuildConfig.VERSION_NAME);
    }
}
