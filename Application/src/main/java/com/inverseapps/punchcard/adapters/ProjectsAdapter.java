package com.inverseapps.punchcard.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.Constant.AppConstant;
import com.inverseapps.punchcard.model.Project;
import com.inverseapps.punchcard.model.User;
import com.inverseapps.punchcard.ui.PCActivity;
import com.inverseapps.punchcard.utils.Utilities;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProjectsAdapter extends ArrayAdapter<Project> {

    private Picasso picasso;
    protected List<Project> listOfProjects;
    public PCActivity activity;
    public SharedPreferences appPreferences;
    private User user;

    public ProjectsAdapter(Context context, List<Project> items, User user) {
        super(context, R.layout.item_project, items);

        listOfProjects = items;
        this.user = user;
        if (context instanceof PCActivity) {
            activity = (PCActivity) context;
            picasso = activity.getPCApplication().getPcFunctionService().getPicasso();

        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Project project = listOfProjects.get(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_project, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if ("inactive".equals(project.getStatus())) {
            convertView.setBackgroundColor(Color.GRAY);
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.load(project, picasso, activity, user);

        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.projectName)
        TextView projectName;

        @BindView(R.id.description)
        TextView description;

        @BindView(R.id.startEnd)
        TextView startEnd;

        @BindView(R.id.onsite)
        TextView onsite;

        @BindView(R.id.logo)
        ImageView logo;

        @BindView(R.id.contentHolder)
        ViewGroup contentHolder;

        @BindView(R.id.frame_onsite)
        FrameLayout frame_onsite;


        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void load(Project project, Picasso picasso, PCActivity activity, User user) {

            String baseUrl = "";
            // if (logo.getContext() instanceof PCActivity) {
            //  PCActivity activity = (PCActivity) logo.getContext();


            String domain = activity.getPCApplication().getAppPreferences().getString(AppConstant.PREF_KEY_DOMAIN, "");

            if (TextUtils.isEmpty(domain)) {
                domain = AppConstant.DEFAULT_DOMAIN;
            }
            String companyHandle = activity.getPCApplication().getAppPreferences().getString(AppConstant.PREF_KEY_COMPANY_HANDLE, "");

            baseUrl = String.format("http://%s.%s%s", companyHandle, domain, project.getLogo());
            // }
            //   if (!TextUtils.isEmpty(baseUrl)) {
            picasso.load(baseUrl)
                    .placeholder(R.drawable.loader_image)
                    .error(R.drawable.loader_image)
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(logo);
            //   }

            projectName.setText(project.getName());
            description.setText(project.getDescription());

            if (user.getRole().equalsIgnoreCase("user")) {
                frame_onsite.setVisibility(View.GONE);

            } else {
                frame_onsite.setVisibility(View.VISIBLE);
                onsite.setText(project.getOceanStats().getEmployeesOnSite() + "");
            }
            String shortStringStartDate = Utilities.shortStringDateFromDate(project.getStartDate());
            String shortStringEndDate = Utilities.shortStringDateFromDate(project.getEndDate());
            startEnd.setText(String.format("%s - %s", shortStringStartDate, shortStringEndDate));
        }
    }
}
