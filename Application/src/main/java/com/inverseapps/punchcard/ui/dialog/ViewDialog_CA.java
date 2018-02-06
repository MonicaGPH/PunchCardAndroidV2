package com.inverseapps.punchcard.ui.dialog;

import android.app.Activity;
import android.app.Dialog;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Handler;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.model.User;
import com.inverseapps.punchcard.ui.ChooseOptionActivity;


import com.inverseapps.punchcard.ui.HomeActivity;
import com.inverseapps.punchcard.ui.LoginActivity;

import com.inverseapps.punchcard.ui.PaymentActivity;


/**
 * Created by asus on 02-Nov-17.
 */

public class ViewDialog_CA {

    private Dialog dialog;
 public void showDialogForCardDecline(final LoginActivity activity, String msg, final User user){
     final Dialog dialog = new Dialog(activity);
     dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
     dialog.setCancelable(false);
     dialog.setContentView(R.layout.alert_layout);

     TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
     text.setText(msg);

     final Button btn_dialog = (Button) dialog.findViewById(R.id.btn_dialog);

     btn_dialog.setText("Update Payment Info");

     btn_dialog.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             dialog.dismiss();
             dialog.cancel();
             String current_plan;
             current_plan = user.getClient().getPlan_test();
             String price= null;
             if (current_plan.equalsIgnoreCase("base"))
                 price = String.valueOf(1);
             else if (current_plan.equalsIgnoreCase("standard"))
                 price = String.valueOf(2);
             else if (current_plan.equalsIgnoreCase("premium"))
                 price = String.valueOf(3);
             else if (current_plan.equalsIgnoreCase("enterprise"))
                 price = String.valueOf(4);
             else if (current_plan.equalsIgnoreCase("trial"))
                 price = String.valueOf(0);
             Intent intent = new Intent(activity, PaymentActivity.class);
             intent.putExtra(PaymentActivity.KEY_PRICE, price);
             intent.putExtra(PaymentActivity.KEY_VERSION, current_plan);
             intent.putExtra(PaymentActivity.KEY_ISCARDRENEW,true);
             intent.putExtra(PaymentActivity.KEY_CHECKCALLFROMACTIVITY, false);
             activity.startActivity(intent);


         }
     });

     dialog.show();



 }
    public void showDialogForCardDeclineWarning(final LoginActivity activity, String msg, final User user){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_layout_ca);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        Button dialogButton_ok = (Button) dialog.findViewById(R.id.btn_dialog_ok);
        final Button diaButton_upgrade = (Button) dialog.findViewById(R.id.btn_dialog_upgrade);
        diaButton_upgrade.setText("Update Payment Info");
        dialogButton_ok.setText("Remind me Later.");
        dialogButton_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.gotoHomeScreenRemindLater();
                dialog.dismiss();
                dialog.cancel();
            }
        });
        diaButton_upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                String current_plan;
                current_plan = user.getClient().getPlan_test();
                String price= null;
                if (current_plan.equalsIgnoreCase("base"))
                    price = String.valueOf(1);
                else if (current_plan.equalsIgnoreCase("standard"))
                    price = String.valueOf(2);
                else if (current_plan.equalsIgnoreCase("premium"))
                    price = String.valueOf(3);
                else if (current_plan.equalsIgnoreCase("enterprise"))
                    price = String.valueOf(4);
                else if (current_plan.equalsIgnoreCase("trial"))
                    price = String.valueOf(0);
                Intent intent = new Intent(activity, PaymentActivity.class);
                intent.putExtra(PaymentActivity.KEY_PRICE, price);
                intent.putExtra(PaymentActivity.KEY_VERSION, current_plan);
                intent.putExtra(PaymentActivity.KEY_ISCARDRENEW,true);
                intent.putExtra(PaymentActivity.KEY_CHECKCALLFROMACTIVITY, false);
                activity.startActivity(intent);


            }
        });

        dialog.show();



    }

    public void showDialogForCardDeclineWarning(final HomeActivity activity, String msg, final User user){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_layout_ca);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        Button dialogButton_ok = (Button) dialog.findViewById(R.id.btn_dialog_ok);
        final Button diaButton_upgrade = (Button) dialog.findViewById(R.id.btn_dialog_upgrade);
        diaButton_upgrade.setText("Update Payment Info");
        dialogButton_ok.setText("Remind me Later.");
        dialogButton_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
                dialog.cancel();
            }
        });
        diaButton_upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                String current_plan;
                current_plan = user.getClient().getPlan_test();
                String price= null;
                if (current_plan.equalsIgnoreCase("base"))
                    price = String.valueOf(1);
                else if (current_plan.equalsIgnoreCase("standard"))
                    price = String.valueOf(2);
                else if (current_plan.equalsIgnoreCase("premium"))
                    price = String.valueOf(3);
                else if (current_plan.equalsIgnoreCase("enterprise"))
                    price = String.valueOf(4);
                else if (current_plan.equalsIgnoreCase("trial"))
                    price = String.valueOf(0);
                Intent intent = new Intent(activity, PaymentActivity.class);
                intent.putExtra(PaymentActivity.KEY_PRICE, price);
                intent.putExtra(PaymentActivity.KEY_VERSION, current_plan);
                intent.putExtra(PaymentActivity.KEY_ISCARDRENEW,true);
                intent.putExtra(PaymentActivity.KEY_CHECKCALLFROMACTIVITY, false);
                activity.startActivity(intent);


            }
        });

        dialog.show();



    }
    public void showDialogATLogInActivity(final LoginActivity activity, String msg, final String plan_test) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_layout_ca);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        Button dialogButton_ok = (Button) dialog.findViewById(R.id.btn_dialog_ok);
        final Button diaButton_upgrade = (Button) dialog.findViewById(R.id.btn_dialog_upgrade);

        if (plan_test.equalsIgnoreCase("trial_plan") )
            diaButton_upgrade.setText("Resubscribe");
        if (plan_test.equalsIgnoreCase("unsubscribe"))
            diaButton_upgrade.setText("Resubscribe");
        dialogButton_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.getPCApplication().getPcFunctionService().logout_user(activity.logOutUserDelegate);
                dialog.dismiss();
                dialog.cancel();
            }
        });
        diaButton_upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(plan_test.equalsIgnoreCase("trial_plan") ||plan_test.equalsIgnoreCase("unsubscribe")) {
                    dialog.dismiss();
                    dialog.cancel();
                    Intent intent = new Intent(activity, ChooseOptionActivity.class);
                    activity.startActivity(intent);
                }


            }
        });

        dialog.show();

    }


    public void showDialogatTimeInterval(final Activity activity, String msg) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_layout_ca);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        Button dialogButton_ok = (Button) dialog.findViewById(R.id.btn_dialog_ok);
        Button diaButton_upgrade = (Button) dialog.findViewById(R.id.btn_dialog_upgrade);


        dialogButton_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
                dialog.cancel();
            }
        });
        diaButton_upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();

                Intent intent = new Intent(activity, ChooseOptionActivity.class);
                activity.startActivity(intent);
                activity.finish();

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
                    activity.finish();
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
