package com.inverseapps.punchcard.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.inverseapps.punchcard.R;

/**
 * Created by Inverse, LLC on 10/18/16.
 */

public class PCAlertDialogFragment extends AppCompatDialogFragment {

    private PCDialogFragmentListener listener;

    private static final String KEY_TITLE = "title";

    private static final String KEY_MESSAGE = "message";

    private static final String KEY_POSITIVE_TITLE = "positiveTitle";

    private static final String KEY_NEGATIVE_TITLE = "negativeTitle";

    private static final String KEY_NEUTRAL_TITLE = "neutralTitle";

    public static PCAlertDialogFragment newInstance(String title,
                                                    String message,
                                                    String positiveTitle,
                                                    String negativeTitle,
                                                    String neutralTitle) {
        PCAlertDialogFragment frag = new PCAlertDialogFragment();

        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        args.putString(KEY_MESSAGE, message);
        args.putString(KEY_POSITIVE_TITLE, positiveTitle);
        args.putString(KEY_NEGATIVE_TITLE, negativeTitle);
        args.putString(KEY_NEUTRAL_TITLE, neutralTitle);

        frag.setArguments(args);

        return frag;
    }

    public static PCAlertDialogFragment newInstance(String message,
                                                    String positiveTitle,
                                                    String negativeTitle,
                                                    String neutralTitle) {
        return PCAlertDialogFragment.newInstance(null, message, positiveTitle, negativeTitle, neutralTitle);
    }

    public static PCAlertDialogFragment newInstance(String message) {
        return PCAlertDialogFragment.newInstance(message, "OK", null, null);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString(KEY_TITLE);
        String message = getArguments().getString(KEY_MESSAGE);
        String positiveTitle = getArguments().getString(KEY_POSITIVE_TITLE);
        String negativeTitle = getArguments().getString(KEY_NEGATIVE_TITLE);
        String neutralTitle = getArguments().getString(KEY_NEUTRAL_TITLE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (title != null) {
            builder.setTitle(title);
        }else {
            builder.setTitle(R.string.app_name);
        }

        builder.setMessage(message);

        final PCAlertDialogFragment self = this;
        if (positiveTitle != null) {
            builder.setPositiveButton(positiveTitle, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    self.dismiss();
                    if (listener != null) {
                        listener.onDialogPositiveClick(self);

                    }
                }
            });
        }

        if (negativeTitle != null) {
            builder.setNegativeButton(negativeTitle, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    self.dismiss();
                    if (listener != null){
                        listener.onDialogNegativeClick(self);
                    }
                }
            });
        }

        if (neutralTitle != null) {
            builder.setNeutralButton(neutralTitle, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    self.dismiss();
                    if (listener != null) {
                        listener.onDialogNeutralClick(self);
                    }
                }
            });
        }

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PCDialogFragmentListener) {
            listener = (PCDialogFragmentListener)context;
        }
    }
}
