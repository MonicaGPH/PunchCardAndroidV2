package com.inverseapps.punchcard.adapters;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.model.Choose_Option;

import com.inverseapps.punchcard.model.User;
import com.inverseapps.punchcard.ui.PaymentActivity;


import java.util.List;

/**
 * Created by asus on 02-Nov-17.
 */

public class ChooseOptionAdapter extends RecyclerView.Adapter<ChooseOptionAdapter.ChooseOptionViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Choose_Option> choose_optionList;

    private User user;

    //getting the context and product list with constructor
    public ChooseOptionAdapter(Context mCtx, List<Choose_Option> choose_optionList,User user) {
        this.mCtx = mCtx;
        this.choose_optionList = choose_optionList;
        this.user= user;
    }

    @Override
    public ChooseOptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_choose_options, null);
        return new ChooseOptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChooseOptionViewHolder holder, int position) {
        final Choose_Option choose_option = choose_optionList.get(position);
        //binding the data with the viewholder views
        holder.textViewTitle.setText(choose_option.getOption_name());
        holder.textViewPrice.setText(String.valueOf(choose_option.getOption_price()));
        holder.layout_choose_option_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPaymentScreen(mCtx, choose_option.getOption_price(), choose_option.getOption_name());
            }
        });
        holder.itemView.animate().scaleX(1.2f);
        holder.itemView.animate().scaleY(1.2f);

        String version_keyword = choose_option.getOption_keyword();

        if (choose_option.getOption_status() == 1) {

            holder.root.setBackgroundDrawable(mCtx.getResources().getDrawable(R.drawable.border_yellow));
            holder.frame_layout_present.setVisibility(View.VISIBLE);
            holder.frame_layout_recommended.setVisibility(View.GONE);
        } else if (choose_option.getOption_status() == 0) {
            holder.root.setBackgroundDrawable(mCtx.getResources().getDrawable(R.drawable.border_green));
            holder.frame_layout_recommended.setVisibility(View.VISIBLE);
            holder.frame_layout_present.setVisibility(View.GONE);
        }

        switch (version_keyword) {
            case "base":
                holder.frame_manual_punch.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_manual_punch.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_digital_badge.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_red));
                holder.img_status_DB.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_cross));

                holder.frame_badge_scan.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_red));
                holder.img_statu_BS.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_cross));

                holder.frame_auto_punch.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_red));
                holder.img_status_AP.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_cross));

                holder.frame_face_punch.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_red));
                holder.img_status_FP.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_cross));

                holder.frame_payroll.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_red));
                holder.img_status_PAYROLL.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_cross));

                holder.frame_task_management.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_red));
                holder.img_status_TM.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_cross));

                holder.frame_emp_timesheet.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_ET.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_company_summary.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_CS.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_campany_detail.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_CD.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_project_hour_summary.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_PHS.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_project_hour_detail.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_PHD.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_project_emp_onsite.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_EO.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_emp_punches.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_red));
                holder.img_status_EP.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_cross));

                holder.frame_emp_details.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_red));
                holder.img_status_ED.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_cross));

                holder.frame_emp_late.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_red));
                holder.img_status_EL.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_cross));

                holder.frame_emp_overtime.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_red));
                holder.img_status_EO.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_cross));

                holder.frame_emp_location.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_red));
                holder.img_status_ELOC.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_cross));

                holder.frame_estimated_payroll.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_red));
                holder.img_status_PAYROLL.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_cross));

                holder.frame_analytics.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_red));
                holder.img_status_ANALYTICS.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_cross));


                break;

            case "standard":
                holder.frame_manual_punch.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_manual_punch.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_digital_badge.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_DB.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_badge_scan.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_statu_BS.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_auto_punch.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_red));
                holder.img_status_AP.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_cross));

                holder.frame_face_punch.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_red));
                holder.img_status_FP.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_cross));

                holder.frame_payroll.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_red));
                holder.img_status_PAYROLL.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_cross));

                holder.frame_task_management.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_red));
                holder.img_status_TM.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_cross));

                holder.frame_emp_timesheet.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_ET.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_company_summary.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_CS.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_campany_detail.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_CD.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_project_hour_summary.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_PHS.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_project_hour_detail.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_PHD.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_project_emp_onsite.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_PEO.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_emp_punches.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_EP.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_emp_details.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_ED.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_emp_late.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_EL.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_emp_overtime.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_EO.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_emp_location.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_ELOC.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_estimated_payroll.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_red));
                holder.img_status_PAYROLL.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_cross));

                holder.frame_analytics.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_red));
                holder.img_status_ANALYTICS.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_cross));

                break;

            case "premium":
                holder.frame_manual_punch.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_manual_punch.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_digital_badge.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_DB.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_badge_scan.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_statu_BS.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_auto_punch.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_AP.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_face_punch.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_FP.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_payroll.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_red));
                holder.img_status_PAYROLL.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_cross));

                holder.frame_task_management.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_red));
                holder.img_status_TM.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_cross));

                holder.frame_emp_timesheet.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_ET.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_company_summary.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_CS.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_campany_detail.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_CD.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_project_hour_summary.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_PHS.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_project_hour_detail.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_PHD.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_project_emp_onsite.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_PEO.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_emp_punches.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_EP.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_emp_details.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_ED.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_emp_late.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_EL.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_emp_overtime.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_EO.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_emp_location.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_ELOC.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_estimated_payroll.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_red));
                holder.img_status_PAYROLL.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_cross));

                holder.frame_analytics.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_red));
                holder.img_status_ANALYTICS.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_cross));
                break;

            case "enterprise":
                holder.frame_manual_punch.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_manual_punch.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_digital_badge.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_DB.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_badge_scan.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_statu_BS.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_auto_punch.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_AP.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_face_punch.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_FP.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_payroll.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_PAYROLL.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_task_management.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_TM.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_emp_timesheet.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_ET.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_company_summary.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_CS.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_campany_detail.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_CD.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_project_hour_summary.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_PHS.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_project_hour_detail.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_PHD.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_project_emp_onsite.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_PEO.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_emp_punches.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_EP.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_emp_details.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_ED.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_emp_late.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_EL.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_emp_overtime.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_EO.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_emp_location.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_ELOC.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_estimated_payroll.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_ESTPAYROLL.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));

                holder.frame_analytics.setBackground(mCtx.getResources().getDrawable(R.drawable.circle_green));
                holder.img_status_ANALYTICS.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_tick));
                break;

        }
    }

    private void gotoPaymentScreen(Context context, String price, String version) {



            ////////////////card is decline or expire
            if(user.getClient().getCreditCardDeactivateDate()!=null && !TextUtils.isEmpty(user.getClient().getCreditCardDeactivateDate())) {
                Intent intent = new Intent(context, PaymentActivity.class);
                intent.putExtra(PaymentActivity.KEY_PRICE, price);
                intent.putExtra(PaymentActivity.KEY_VERSION, version);
                intent.putExtra(PaymentActivity.KEY_ISCARDRENEW, true);
                intent.putExtra(PaymentActivity.KEY_CHECKCALLFROMACTIVITY, true);
                context.startActivity(intent);
            }
            else {
                Intent intent = new Intent(context, PaymentActivity.class);
                intent.putExtra(PaymentActivity.KEY_PRICE, price);
                intent.putExtra(PaymentActivity.KEY_VERSION, version);
                intent.putExtra(PaymentActivity.KEY_ISCARDRENEW, false);
                intent.putExtra(PaymentActivity.KEY_CHECKCALLFROMACTIVITY, true);
                context.startActivity(intent);
            }



    }

    @Override
    public int getItemCount() {
        return choose_optionList.size();
    }


    class ChooseOptionViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewPrice;
        CardView root;
        TextView btnViewMore;
        int height;
        LinearLayout layoutViewMore;
        RelativeLayout layout_choose_option_parent,
                layoutFeatureManualPunch,
                layoutFeatureDigitalBadge,
                layoutFeatureBadgeScan,
                layoutFeatureAutoPunch,
                layoutFeatureFacePunch,
                layoutFeaturePayroll,
                layoutFeaturetaskManagement,
                layoutEmployeeTimesheet,
                layoutCompanySummary,
                layoutCompanyDetail,
                layoutProjectHoursSummary,
                layoutProjectHoursDetail,
                layoutProjectEmployeesOnsite,
                layoutEmployeePunches,
                layoutEmployeeDetail,
                layoutEmployeeLate,
                layoutEmployeeOvertime,
                layoutEmployeeLocation,
                layoutEstimatedPayroll,
                layoutAnalytics;

        FrameLayout frame_layout_recommended,
                frame_layout_present,
                frame_manual_punch,
                frame_digital_badge,
                frame_badge_scan,
                frame_auto_punch,
                frame_face_punch,
                frame_payroll,
                frame_task_management,
                frame_emp_timesheet,
                frame_company_summary,
                frame_campany_detail,
                frame_project_hour_summary,
                frame_project_hour_detail,
                frame_project_emp_onsite,
                frame_emp_punches,
                frame_emp_details,
                frame_emp_late,
                frame_emp_overtime,
                frame_emp_location,
                frame_estimated_payroll,
                frame_analytics;
        ImageView img_status_manual_punch,
                img_status_DB,
                img_statu_BS,
                img_status_AP,
                img_status_FP,
                img_status_PAYROLL,
                img_status_TM,
                img_status_ET,
                img_status_CS,
                img_status_CD,
                img_status_PHS,
                img_status_PHD,
                img_status_PEO,
                img_status_EP,
                img_status_ED,
                img_status_EL,
                img_status_EO,
                img_status_ELOC,
                img_status_ESTPAYROLL,
                img_status_ANALYTICS;

        public ChooseOptionViewHolder(View itemView) {
            super(itemView);

            textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            root = (CardView) itemView.findViewById(R.id.root);
            textViewPrice = (TextView) itemView.findViewById(R.id.textViewPrice);
            btnViewMore = (TextView) itemView.findViewById(R.id.btn_viewMore);

            layoutViewMore = (LinearLayout) itemView.findViewById(R.id.layoutViewMore);

            layout_choose_option_parent = (RelativeLayout) itemView.findViewById(R.id.layout_choose_option_parent);
            layoutFeatureManualPunch = (RelativeLayout) itemView.findViewById(R.id.layoutFeatureManualPunch);
            layoutFeatureDigitalBadge = (RelativeLayout) itemView.findViewById(R.id.layoutFeatureDigitalBadge);
            layoutFeatureBadgeScan = (RelativeLayout) itemView.findViewById(R.id.layoutFeatureBadgeScan);
            layoutFeatureAutoPunch = (RelativeLayout) itemView.findViewById(R.id.layoutFeatureAutoPunch);
            layoutFeatureFacePunch = (RelativeLayout) itemView.findViewById(R.id.layoutFeatureFacePunch);
            layoutFeaturePayroll = (RelativeLayout) itemView.findViewById(R.id.layoutFeaturePayroll);
            layoutFeaturetaskManagement = (RelativeLayout) itemView.findViewById(R.id.layoutFeaturetaskManagement);
            layoutEmployeeTimesheet = (RelativeLayout) itemView.findViewById(R.id.layoutEmployeeTimesheet);
            layoutCompanySummary = (RelativeLayout) itemView.findViewById(R.id.layoutCompanySummary);
            layoutCompanyDetail = (RelativeLayout) itemView.findViewById(R.id.layoutCompanyDetail);
            layoutProjectHoursSummary = (RelativeLayout) itemView.findViewById(R.id.layoutProjectHoursSummary);
            layoutProjectHoursDetail = (RelativeLayout) itemView.findViewById(R.id.layoutProjectHoursDetail);
            layoutProjectEmployeesOnsite = (RelativeLayout) itemView.findViewById(R.id.layoutProjectEmployeesOnsite);
            layoutEmployeePunches = (RelativeLayout) itemView.findViewById(R.id.layoutEmployeePunches);
            layoutEmployeeDetail = (RelativeLayout) itemView.findViewById(R.id.layoutEmployeeDetail);
            layoutEmployeeLate = (RelativeLayout) itemView.findViewById(R.id.layoutEmployeeLate);

            layoutEmployeeOvertime = (RelativeLayout) itemView.findViewById(R.id.layoutEmployeeOvertime);
            layoutEmployeeLocation = (RelativeLayout) itemView.findViewById(R.id.layoutEmployeeLocation);
            layoutEstimatedPayroll = (RelativeLayout) itemView.findViewById(R.id.layoutEstimatedPayroll);
            layoutAnalytics = (RelativeLayout) itemView.findViewById(R.id.layoutAnalytics);

            frame_layout_recommended = (FrameLayout) itemView.findViewById(R.id.frame_layout_recommended);
            frame_layout_present = (FrameLayout) itemView.findViewById(R.id.frame_layout_present);
            frame_manual_punch = (FrameLayout) itemView.findViewById(R.id.frame_manual_punch);
            frame_digital_badge = (FrameLayout) itemView.findViewById(R.id.frame_digital_badge);
            frame_badge_scan = (FrameLayout) itemView.findViewById(R.id.frame_badge_scan);
            frame_auto_punch = (FrameLayout) itemView.findViewById(R.id.frame_auto_punch);
            frame_face_punch = (FrameLayout) itemView.findViewById(R.id.frame_face_punch);
            frame_payroll = (FrameLayout) itemView.findViewById(R.id.frame_payroll);
            frame_task_management = (FrameLayout) itemView.findViewById(R.id.frame_task_management);
            frame_emp_timesheet = (FrameLayout) itemView.findViewById(R.id.frame_emp_timesheet);
            frame_company_summary = (FrameLayout) itemView.findViewById(R.id.frame_company_summary);
            frame_campany_detail = (FrameLayout) itemView.findViewById(R.id.frame_campany_detail);
            frame_project_hour_summary = (FrameLayout) itemView.findViewById(R.id.frame_project_hour_summary);
            frame_project_hour_detail = (FrameLayout) itemView.findViewById(R.id.frame_project_hour_detail);
            frame_project_emp_onsite = (FrameLayout) itemView.findViewById(R.id.frame_project_emp_onsite);
            frame_emp_punches = (FrameLayout) itemView.findViewById(R.id.frame_emp_punches);
            frame_emp_details = (FrameLayout) itemView.findViewById(R.id.frame_emp_details);
            frame_emp_late = (FrameLayout) itemView.findViewById(R.id.frame_emp_late);
            frame_emp_overtime = (FrameLayout) itemView.findViewById(R.id.frame_emp_overtime);
            frame_emp_location = (FrameLayout) itemView.findViewById(R.id.frame_emp_location);
            frame_estimated_payroll = (FrameLayout) itemView.findViewById(R.id.frame_estimated_payroll);
            frame_analytics = (FrameLayout) itemView.findViewById(R.id.frame_analytics);

            img_status_manual_punch = (ImageView) itemView.findViewById(R.id.img_status_manual_punch);
            img_status_DB = (ImageView) itemView.findViewById(R.id.img_status_DB);
            img_status_AP = (ImageView) itemView.findViewById(R.id.img_status_AP);
            img_statu_BS = (ImageView) itemView.findViewById(R.id.img_status_BS);
            img_status_FP = (ImageView) itemView.findViewById(R.id.img_status_FP);
            img_status_PAYROLL = (ImageView) itemView.findViewById(R.id.img_status_PAYROLL);
            img_status_TM = (ImageView) itemView.findViewById(R.id.img_status_TM);
            img_status_CS = (ImageView) itemView.findViewById(R.id.img_status_CS);
            img_status_ET = (ImageView) itemView.findViewById(R.id.img_status_ET);
            img_status_CD = (ImageView) itemView.findViewById(R.id.img_status_CD);
            img_status_PHS = (ImageView) itemView.findViewById(R.id.img_status_PHS);
            img_status_PHD = (ImageView) itemView.findViewById(R.id.img_status_PHD);
            img_status_PEO = (ImageView) itemView.findViewById(R.id.img_status_PEO);
            img_status_EP = (ImageView) itemView.findViewById(R.id.img_status_EP);
            img_status_ED = (ImageView) itemView.findViewById(R.id.img_status_ED);
            img_status_EL = (ImageView) itemView.findViewById(R.id.img_status_EL);
            img_status_EO = (ImageView) itemView.findViewById(R.id.img_status_EO);
            img_status_ELOC = (ImageView) itemView.findViewById(R.id.img_status_ELOC);
            img_status_ESTPAYROLL = (ImageView) itemView.findViewById(R.id.img_status_ESTPAYROLL);
            img_status_ANALYTICS = (ImageView) itemView.findViewById(R.id.img_status_ANALYTICS);


            btnViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (layoutViewMore.getVisibility() == View.GONE) {
                        expand(layoutViewMore, height);
                        SpannableString content = new SpannableString("View Less");
                        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                        btnViewMore.setText(content);
                    } else {
                        collapse(layoutViewMore);
                        SpannableString content = new SpannableString("View More");
                        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                        btnViewMore.setText(content);
                    }
                }
            });

            layoutViewMore.getViewTreeObserver().addOnPreDrawListener(
                    new ViewTreeObserver.OnPreDrawListener() {

                        @Override
                        public boolean onPreDraw() {
                            layoutViewMore.getViewTreeObserver().removeOnPreDrawListener(this);
                            layoutViewMore.setVisibility(View.GONE);


                            final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                            final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                            layoutViewMore.measure(widthSpec, heightSpec);
                            height = layoutViewMore.getMeasuredHeight();

                            return true;
                        }
                    });
        }


        private void expand(LinearLayout layout, int layoutHeight) {
            layout.setVisibility(View.VISIBLE);
            ValueAnimator animator = slideAnimator(layout, 0, layoutHeight);
            animator.start();
        }

        private void collapse(final LinearLayout layout) {
            int finalHeight = layout.getHeight();
            ValueAnimator mAnimator = slideAnimator(layout, finalHeight, 0);

            mAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    //Height=0, but it set visibility to GONE
                    layout.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
            mAnimator.start();
        }


        private ValueAnimator slideAnimator(final LinearLayout layout, int start, int end) {
            ValueAnimator animator = ValueAnimator.ofInt(start, end);

            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    //Update Height
                    int value = (Integer) valueAnimator.getAnimatedValue();

                    ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
                    layoutParams.height = value;
                    layout.setLayoutParams(layoutParams);
                }
            });
            return animator;
        }


    }


}

