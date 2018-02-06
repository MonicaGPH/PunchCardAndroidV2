package com.inverseapps.punchcard.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.adapters.OnSiteAdapter;
import com.inverseapps.punchcard.model.OnSite;
import com.inverseapps.punchcard.model.Project;
import com.inverseapps.punchcard.model.User;
import com.inverseapps.punchcard.ui.ForegroundTaskDelegate;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;

public class ProjectOnSitesFragment extends PCProjectDetailsFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.fragment_project_onsites;
    }

    private List<OnSite> listOfOnSitesPunchedIn;
    private List<OnSite> listOfOnSitesNotPunchedIn;

    @NonNull
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @NonNull
    @BindView(R.id.recyclerViewPunchedIn)
    RecyclerView recyclerViewPunchedIn;

    @NonNull
    @BindView(R.id.recyclerViewNotPunchedIn)
    RecyclerView recyclerViewNotPunchedIn;

    private RecyclerView.Adapter adapterPunchedIn;
    private RecyclerView.Adapter adapterNotPunchedIn;
    private RecyclerView.LayoutManager layoutManagerPunchedIn;
    private RecyclerView.LayoutManager layoutManagerNotPunchedIn;
    private String addressString, zipString, stateString, countryString, cityString;
    private ForegroundTaskDelegate findOnSitesDelegate;

    public static ProjectOnSitesFragment newInstance(int page,
                                                     String title,
                                                     Project project,
                                                     String checkedInProjectUniqId,
                                                     String projectAddress,
                                                     String projectCity,
                                                     String projectCountry,
                                                     String projectState,
                                                     String projectZip,
                                                     String checkedInProjectName,
                                                     boolean userInProject) {
        ProjectOnSitesFragment fragment = new ProjectOnSitesFragment();
        fragment.addressString = projectAddress;
        fragment.cityString = projectCity;
        fragment.countryString = projectCountry;
        fragment.stateString = projectState;
        fragment.zipString = projectZip;
        Bundle args = new Bundle();
        args.putInt(KEY_PAGE, page);
        args.putString(KEY_TITLE, title);
        args.putParcelable(KEY_PROJECT, project);
        args.putString(KEY_USER_IN_PROJECT_ADD, projectAddress);
        args.putString(KEY_USER_IN_PROJECT_CITY, projectCity);
        args.putString(KEY_USER_IN_PROJECT_COUNTRY, projectCountry);
        args.putString(KEY_USER_IN_PROJECT_STATE, projectState);
        args.putString(KEY_USER_IN_PROJECT_ZIP, projectZip);
        args.putString(KEY_CHECKED_IN_PROJECT_UNIQ_ID, checkedInProjectUniqId);
        args.putString(KEY_CHECKED_IN_PROJECT_NAME, checkedInProjectName);
        args.putBoolean(KEY_USER_IN_PROJECT, userInProject);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findOnSitesDelegate = new FindOnSitesDelegate(this);
        listOfForegroundTaskDelegates.add(findOnSitesDelegate);

        listOfOnSitesPunchedIn = new Vector<>();
        listOfOnSitesNotPunchedIn = new Vector<>();
        layoutManagerPunchedIn = new LinearLayoutManager(getActivity());
        layoutManagerNotPunchedIn = new LinearLayoutManager(getActivity());

        recyclerViewPunchedIn.setLayoutManager(layoutManagerPunchedIn);
        recyclerViewPunchedIn.setHasFixedSize(true);
        recyclerViewPunchedIn.setItemAnimator(new DefaultItemAnimator());
        adapterPunchedIn = new OnSiteAdapter(project, addressString, cityString, countryString, stateString, zipString,listOfOnSitesPunchedIn);
        recyclerViewPunchedIn.setAdapter(adapterPunchedIn);

        recyclerViewNotPunchedIn.setLayoutManager(layoutManagerNotPunchedIn);
        recyclerViewNotPunchedIn.setHasFixedSize(true);
        recyclerViewNotPunchedIn.setItemAnimator(new DefaultItemAnimator());
        adapterNotPunchedIn = new OnSiteAdapter(project,addressString, cityString, countryString, stateString, zipString,listOfOnSitesNotPunchedIn);
        recyclerViewNotPunchedIn.setAdapter(adapterNotPunchedIn);

        swipeRefreshLayout.setColorSchemeResources(R.color.logo_orange);
        swipeRefreshLayout.setOnRefreshListener(this);
    }


    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        pcActivity.getPCApplication().getPcFunctionService().findOnSites(project.getUniq_id(), findOnSitesDelegate);
    }

    @Override
    public void onResume() {
        super.onResume();

        User user = pcActivity.getPCApplication().getPcFunctionService().getInternalStoredUser();
        if (user != null && user.getRole().equals("user")) {

        } else {
            pcActivity.getPCApplication().getPcFunctionService().findOnSites(project.getUniq_id(), findOnSitesDelegate);
        }
    }

    @Override
    public void reload(Project project, String checkedInProjectUniqId) {
        super.reload(project, checkedInProjectUniqId);

        pcActivity.getPCApplication().getPcFunctionService().findOnSites(project.getUniq_id(), findOnSitesDelegate);
    }

    private static class FindOnSitesDelegate extends ForegroundTaskDelegate<List<OnSite>> {

        private final WeakReference<ProjectOnSitesFragment> fragmentWeakReference;

        public FindOnSitesDelegate(ProjectOnSitesFragment fragment) {
            super(fragment.pcActivity);
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void onPostExecute(List<OnSite> onSites, Throwable throwable) {
            super.onPostExecute(onSites, throwable);
            ProjectOnSitesFragment fragment = fragmentWeakReference.get();
            if (throwable == null &&
                    fragment != null && !fragment.isDetached() && !fragment.isRemoving()) {

                List<OnSite> listOfPunchedIn = new Vector<>();
                List<OnSite> listOfNotPunchedIn = new Vector<>();
                for (OnSite onSite : onSites) {
                    if (onSite.isOnSite()) {
                        listOfPunchedIn.add(onSite);
                    } else {
                        listOfNotPunchedIn.add(onSite);
                    }
                }

                fragment.listOfOnSitesPunchedIn.clear();
                fragment.listOfOnSitesPunchedIn.addAll(listOfPunchedIn);

                Collections.sort(fragment.listOfOnSitesPunchedIn, new Comparator<OnSite>() {
                    @Override
                    public int compare(OnSite lhs, OnSite rhs) {
                        return rhs.getLastCheckIn().compareTo(lhs.getLastCheckIn());
                    }

                });
                fragment.adapterPunchedIn.notifyDataSetChanged();

                fragment.listOfOnSitesNotPunchedIn.clear();
                fragment.listOfOnSitesNotPunchedIn.addAll(listOfNotPunchedIn);

                Collections.sort(fragment.listOfOnSitesNotPunchedIn, new Comparator<OnSite>() {
                    @Override
                    public int compare(OnSite lhs, OnSite rhs) {
                        if(lhs.getLastCheckIn()!= null && rhs.getLastCheckIn() !=null) {
                            return rhs.getLastCheckOut().compareTo(lhs.getLastCheckOut());
                        }
                        return 0;
                    }

                });
                fragment.adapterNotPunchedIn.notifyDataSetChanged();

                fragment.swipeRefreshLayout.setRefreshing(false);
            }
        }
    }
}
