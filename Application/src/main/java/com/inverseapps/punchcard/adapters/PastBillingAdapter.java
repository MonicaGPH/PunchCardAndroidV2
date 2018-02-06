package com.inverseapps.punchcard.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.model.PastBilling;

import com.inverseapps.punchcard.ui.PastBillingExtendActivity;
import com.inverseapps.punchcard.utils.Utilities;
import com.orhanobut.logger.Logger;


import java.util.Hashtable;
import java.util.List;

import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by asus on 05-Dec-17.
 */

public class PastBillingAdapter extends RecyclerView.Adapter<PastBillingAdapter.PastBillingViewHolder> {

    private List<PastBilling> listOfPastBillingOuts;
    private Hashtable<String, List<PastBilling>> userPastBillingTable;
    private Context context;

    public PastBillingAdapter(List<PastBilling> dataSet, Context context) {
        listOfPastBillingOuts = dataSet;
        userPastBillingTable = new Hashtable<>();
       this.context=context;
    }

    public void buildUserCheckInOutTable() {
        userPastBillingTable.clear();

        for (PastBilling pastBilling : listOfPastBillingOuts) {
            String shortDateString = Utilities.getMonthFromDate(pastBilling.getBilling_start_date());
            Logger.d(shortDateString);

            if (!userPastBillingTable.containsKey(shortDateString)) {

                userPastBillingTable.put(shortDateString, new Vector<PastBilling>());
            }
            userPastBillingTable.get(shortDateString).add(pastBilling);
        }


    }

    @Override
    public PastBillingAdapter.PastBillingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_past_billing_parent, parent, false);
        PastBillingAdapter.PastBillingViewHolder holder = new PastBillingAdapter.PastBillingViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return listOfPastBillingOuts.size();
    }

    @Override
    public void onBindViewHolder(final PastBillingAdapter.PastBillingViewHolder holder, int position) {
         final PastBilling pastBilling = listOfPastBillingOuts.get(position);
        for (List<PastBilling> list : userPastBillingTable.values()) {
            if (list.contains(pastBilling)) {
                int index = list.indexOf(pastBilling);
                if (index == 0) {
                    holder.showHeader(true);

                } else {
                    holder.showHeader(false);

                }
                break;
            }
        }
        holder.relativedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PastBillingExtendActivity.class);
                intent.putExtra(PastBillingExtendActivity.KEY_PAST_BILLING_fromdate,pastBilling.getBilling_start_date());
                intent.putExtra(PastBillingExtendActivity.KEY_PAST_BILLING_todate,pastBilling.getBilling_end_date());
                intent.putExtra(PastBillingExtendActivity.KEY_PAST_BILLING_punches,pastBilling.getTotal_punch());
                intent.putExtra(PastBillingExtendActivity.KEY_TOTAL_AMOUNT,holder.total_amount);
                context.startActivity(intent);
            }
        });

        holder.load(pastBilling);
    }

    static class PastBillingViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        @BindView(R.id.btnHeader)
        Button btnHeader;

        String total_amount = null;
        @NonNull
        @BindView(R.id.relativedate)
        RelativeLayout relativedate;

        @NonNull
        @BindView(R.id.txt_StartDate)
        TextView txt_StartDate;

        public PastBillingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void showHeader(boolean show) {
            if (show) {
                btnHeader.setVisibility(View.VISIBLE);
            } else {
                btnHeader.setVisibility(View.GONE);
            }
        }

        public void load(PastBilling pastBilling) {


            btnHeader.setText(Utilities.getMonthFromDate(pastBilling.getBilling_start_date()));

            txt_StartDate.setText(Utilities.shortStringDateFromDate(pastBilling.getBilling_start_date()) +" - "+Utilities.shortStringDateFromDate(pastBilling.getBilling_end_date()));

            total_amount = String.valueOf(Float.valueOf(pastBilling.getPer_punch_cost()) * Float.valueOf(pastBilling.getTotal_punch()));

        }


    }





}