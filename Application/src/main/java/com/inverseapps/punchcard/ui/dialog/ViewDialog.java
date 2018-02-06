package com.inverseapps.punchcard.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;


import android.os.Handler;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.ui.LoginActivity;


/**
 * Created by asus on 02-Nov-17.
 */

public class ViewDialog  {



    public void showDialogATLogInActivity(final LoginActivity activity, String msg) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_layout);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        Button dialogButton_ok = (Button) dialog.findViewById(R.id.btn_dialog);

        dialogButton_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               activity.getPCApplication().getPcFunctionService().logout_user(activity.logOutUserDelegate);
                dialog.dismiss();
                dialog.cancel();

            }
        });


        dialog.show();

    }

    public void showDialogatTimeInterval(final Activity activity, String msg) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_layout);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        Button dialogButton_ok = (Button) dialog.findViewById(R.id.btn_dialog);

        dialogButton_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
        // Hide after some seconds
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    dialog.cancel();

                }
            }
        };

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 9000);

    }


}
