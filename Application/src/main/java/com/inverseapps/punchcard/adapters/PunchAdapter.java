package com.inverseapps.punchcard.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.model.UserCheckInOut;
import com.inverseapps.punchcard.utils.Utilities;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PunchAdapter extends RecyclerView.Adapter<PunchAdapter.UserCheckInOutViewHolder> {

    private List<UserCheckInOut> listOfUserCheckInOuts;
    private Hashtable<String, List<UserCheckInOut>> userCheckInOutTable;

    public PunchAdapter(List<UserCheckInOut> dataSet) {
        listOfUserCheckInOuts = dataSet;
        userCheckInOutTable = new Hashtable<>();
    }

    public void buildUserCheckInOutTable() {
        userCheckInOutTable.clear();

        for (UserCheckInOut userCheckInOutOut : listOfUserCheckInOuts) {
            String shortDateString = Utilities.stringDateFromDate(userCheckInOutOut.getCheckInDateTime(), "yyyy-MM-dd");
            if (!userCheckInOutTable.containsKey(shortDateString)) {
                userCheckInOutTable.put(shortDateString, new Vector<UserCheckInOut>());
            }
            userCheckInOutTable.get(shortDateString).add(userCheckInOutOut);
        }

        List<String> keysList = new Vector<>(userCheckInOutTable.keySet());
        Collections.sort(keysList);
        Collections.reverse(keysList);

        listOfUserCheckInOuts.clear();
        for (String key: keysList) {
            List<UserCheckInOut> list = userCheckInOutTable.get(key);
            Collections.sort(list, new Comparator<UserCheckInOut>() {
                @Override
                public int compare(UserCheckInOut obj1, UserCheckInOut obj2) {
                    return obj2.getCheckInDateTime().compareTo(obj1.getCheckInDateTime());
                }
            });
            listOfUserCheckInOuts.addAll(list);
        }
    }

    @Override
    public UserCheckInOutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_punch, parent, false);
        UserCheckInOutViewHolder holder = new UserCheckInOutViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return listOfUserCheckInOuts.size();
    }

    @Override
    public void onBindViewHolder(UserCheckInOutViewHolder holder, int position) {
        UserCheckInOut userCheckInOutOut = listOfUserCheckInOuts.get(position);
        for (List<UserCheckInOut> list: userCheckInOutTable.values()) {
            if (list.contains(userCheckInOutOut)) {
                int index = list.indexOf(userCheckInOutOut);
                if (index == 0){
                    holder.showHeader(true);
                } else {
                    holder.showHeader(false);
                }
                break;
            }
        }
        holder.load(userCheckInOutOut);
    }

    static class UserCheckInOutViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        @BindView(R.id.btnHeader)
        Button btnHeader;

        @NonNull
        @BindView(R.id.lblCheckInProjectName)
        TextView lblCheckInProjectName;

        @NonNull
        @BindView(R.id.lblCheckInTimestamp)
        TextView lblCheckInTimestamp;

        @NonNull
        @BindView(R.id.viewGroupCheckOut)
        ViewGroup viewGroupCheckOut;

        @NonNull
        @BindView(R.id.lblCheckOutProjectName)
        TextView lblCheckOutProjectName;

        @NonNull
        @BindView(R.id.lblCheckOutTimestamp)
        TextView lblCheckOutTimestamp;

        public UserCheckInOutViewHolder(View itemView) {
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

        public void load(UserCheckInOut userCheckInOut) {
            btnHeader.setText(Utilities.middleStringDateFromDate(userCheckInOut.getCheckInDateTime()));
            lblCheckInProjectName.setText(userCheckInOut.getProject().getName());
            lblCheckOutProjectName.setText(userCheckInOut.getProject().getName());

            Date checkInDateTime = userCheckInOut.getCheckInDateTime();
            lblCheckInTimestamp.setText(Utilities.shortStringDateTimeFromDate(checkInDateTime));

            Date checkOutDateTime = userCheckInOut.getCheckOutDateTime();
            if (checkOutDateTime != null) {
                lblCheckOutTimestamp.setText(Utilities.shortStringDateTimeFromDate(checkOutDateTime));
                viewGroupCheckOut.setVisibility(View.VISIBLE);
            } else {
                viewGroupCheckOut.setVisibility(View.GONE);
            }
        }
    }
}
