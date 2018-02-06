package com.inverseapps.punchcard.fragments.editprofile;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.fragments.PCFragment;
import com.inverseapps.punchcard.model.User;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Inverse, LLC on 10/22/16.
 */

public abstract class PCFragmentEditInfo extends PCFragment {

    @NonNull
    @BindView(R.id.btnHeader)
    Button btnHeader;

    @NonNull
    @BindView(R.id.content)
    ViewGroup content;

    @OnClick(R.id.btnHeader)
    void onClickedHeaderButton() {

        if (content.getVisibility() == View.VISIBLE) {
            btnHeader.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down_arrow, 0);
            content.setVisibility(View.GONE);
        } else if (content.getVisibility() == View.GONE) {
            content.setVisibility(View.VISIBLE);
            btnHeader.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_up_arrow, 0);
            if (txtFirst != null) {
                txtFirst.requestFocus();
            }
        }
    }

    protected OnChangedUserListener listener;

    protected  EditText txtFirst;

    public static final String KEY_USER = "user";

    protected User user;

    protected boolean didReload = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChangedUserListener) {
            listener = (OnChangedUserListener)context;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            user = savedInstanceState.getParcelable(KEY_USER);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_USER, user);

        super.onSaveInstanceState(outState);
    }

    public void reload(User user) {
        this.user = user;
    }

    public interface OnChangedUserListener {
        void onChangedUserInfo(PCFragmentEditInfo fragment, User user);
        void onChangedUserAvatar(PCFragmentEditInfo fragment, Bitmap avatar);
    }
}
