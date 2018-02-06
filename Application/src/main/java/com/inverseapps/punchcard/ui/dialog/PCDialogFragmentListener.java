package com.inverseapps.punchcard.ui.dialog;

import android.support.v7.app.AppCompatDialogFragment;

/**
 * Created by Inverse, LLC on 10/18/16.
 */

public interface PCDialogFragmentListener {

    void onDialogPositiveClick(AppCompatDialogFragment dialog);
    void onDialogNegativeClick(AppCompatDialogFragment dialog);
    void onDialogNeutralClick(AppCompatDialogFragment dialog);

}
