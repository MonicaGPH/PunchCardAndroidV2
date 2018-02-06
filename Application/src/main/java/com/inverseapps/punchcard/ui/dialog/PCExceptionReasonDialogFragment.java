package com.inverseapps.punchcard.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.inverseapps.punchcard.R;

/**
 * Created by Inverse, LLC on 10/20/16.
 */

public class PCExceptionReasonDialogFragment extends AppCompatDialogFragment {

    public void setListener(PCDialogFragmentListener listener) {
        this.listener = listener;
    }

    private PCDialogFragmentListener listener;

    private static final String KEY_TITLE = "title";

    public static final String KEY_FOR_CHECK_IN = "forCheckIn";

    public static final String KEY_EXCEPTION_REASON = "exceptionReason";

    public static PCExceptionReasonDialogFragment newInstance(String title, boolean forCheckIn) {
        PCExceptionReasonDialogFragment frag = new PCExceptionReasonDialogFragment();

        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        args.putBoolean(KEY_FOR_CHECK_IN, forCheckIn);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString(KEY_TITLE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (title != null) {
            builder.setTitle(title);
        }else {
            builder.setTitle(R.string.app_name);
        }

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View promptsView = inflater.inflate(R.layout.dialog_exception, null);
        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        builder.setView(promptsView);

        final PCExceptionReasonDialogFragment self = this;
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                
                String exceptionReason = userInput.getText().toString();
                self.getArguments().putString(KEY_EXCEPTION_REASON, exceptionReason);
                
                self.dismiss();
                if (listener != null) {
                    listener.onDialogPositiveClick(self);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                self.dismiss();
                if (listener != null){
                    listener.onDialogNegativeClick(self);
                }
            }
        });

        return builder.create();
    }

}
