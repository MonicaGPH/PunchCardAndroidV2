package com.inverseapps.punchcard.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.model.OnSite;
import com.inverseapps.punchcard.model.Project;
import com.inverseapps.punchcard.ui.ViewEmployeeActivity;
import com.inverseapps.punchcard.utils.Utilities;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Inverse, LLC on 10/20/16.
 */

public class OnSiteAdapter extends RecyclerView.Adapter<OnSiteAdapter.OnSiteViewHolder> {

    private Project project;

    private List<OnSite> listOfOnSites;
    private String addressString,zipString,stateString,countryString,cityString;

    public OnSiteAdapter(Project project,
                         String projectAddress,
                         String projectCity,
                         String projectCountry,
                         String projectState,
                         String projectZip,
                         List<OnSite> dataSet) {
        this.project = project;
        this.addressString = projectAddress;
        this.cityString = projectCity;
        this.countryString = projectCountry;
        this.stateString = projectState;
        this.zipString = projectZip;
        this.listOfOnSites = dataSet;
    }

    @Override
    public OnSiteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_onsite, parent, false);
        OnSiteViewHolder holder = new OnSiteViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(OnSiteViewHolder holder, int position) {
        final OnSite onSite = listOfOnSites.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoViewEmployeeScreen(view.getContext(), onSite);
            }
        });
        holder.load(onSite);
    }

    @Override
    public int getItemCount() {
        return listOfOnSites.size();
    }

    private void gotoViewEmployeeScreen(Context context, OnSite onSite) {
        Intent intent = new Intent(context, ViewEmployeeActivity.class);
        intent.putExtra(ViewEmployeeActivity.KEY_PROJECT, project);
        intent.putExtra(ViewEmployeeActivity.KEY_PROJECT_ADD, addressString);
        intent.putExtra(ViewEmployeeActivity.KEY_PROJECT_CITY, cityString);
        intent.putExtra(ViewEmployeeActivity.KEY_PROJECT_COUNTRY, countryString);
        intent.putExtra(ViewEmployeeActivity.KEY_PROJECT_STATE, stateString);
        intent.putExtra(ViewEmployeeActivity.KEY_PROJECT_ZIP, zipString);
        intent.putExtra(ViewEmployeeActivity.KEY_EMPLOYEE_ID, onSite.getUniq_id());
        context.startActivity(intent);
    }

    static class OnSiteViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        @BindView(R.id.lblName)
        TextView lblName;

        @NonNull
        @BindView(R.id.lblDateTime)
        TextView lblDateTime;

        public OnSiteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void load(OnSite onSite) {
            lblName.setText(onSite.getFirst_name() +" "+onSite.getLast_name());
            if (onSite.isOnSite()) {
                if (onSite.getLastCheckIn() != null) {
                    String timestamp = Utilities.shortStringTimeFromDate(onSite.getLastCheckIn());

                            String date = Utilities.shortStringDateFromDate1(onSite.getLastCheckIn());
                    lblDateTime.setText(String.format(" In %s - %s", date, timestamp));
                } else {
                    lblDateTime.setText("No checkins");
                }
            } else {
                if (onSite.getLastCheckOut() != null) {
                    String timestamp = Utilities.shortStringTimeFromDate(onSite.getLastCheckOut());
                    String date = Utilities.shortStringDateFromDate1(onSite.getLastCheckOut());
                    lblDateTime.setText(String.format(" Out %s - %s",date, timestamp));
                } else {
                    lblDateTime.setText("No checkins");
                }
            }

        }
    }
}