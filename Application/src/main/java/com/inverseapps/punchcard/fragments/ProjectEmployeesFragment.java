package com.inverseapps.punchcard.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;

import android.view.View;

import android.widget.EditText;

import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.adapters.EmployeeAdapter;
import com.inverseapps.punchcard.model.OnSite;
import com.inverseapps.punchcard.model.Project;
import com.inverseapps.punchcard.model.User;
import com.inverseapps.punchcard.ui.ForegroundTaskDelegate;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class ProjectEmployeesFragment extends PCProjectDetailsFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.fragment_project_employees;
    }

    private List<OnSite> listOfOnSites;

    private List<OnSite> displayedOnSites;

    @NonNull
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @NonNull
    @BindView(R.id.txtSearch)
    EditText txtSearch;

    /*@NonNull
    @OnClick(R.id.btnSearch)
    void onClickedSearchButton() {
        String searchText = txtSearch.getText().toString();
        if (!TextUtils.isEmpty(searchText)) {
            pcActivity.getPCApplication().getPcFunctionService().findOnSites(project.getUniq_id(),
                    searchText,
                    1,
                    128,
                    searchOnSitesDelegate);
        }
    }*/

    @NonNull
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    @OnTextChanged(R.id.txtSearch)
    void onTextChangedSearchText() {
        isOnTextChanged = true;
        String searchText = txtSearch.getText().toString().toLowerCase();


        if (TextUtils.isEmpty(searchText)) {
            pcActivity.hideKeyboard();
            displayedOnSites.clear();
            displayedOnSites.addAll(listOfOnSites);
            Collections.sort(displayedOnSites, new Comparator<OnSite>() {
                @Override
                public int compare(OnSite lhs, OnSite rhs) {
                    return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
                }

            });
            adapter = new EmployeeAdapter(project, addressString, cityString, countryString, stateString, zipString, displayedOnSites);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();  // data set changed

        } else {
            final List<OnSite> filteredList = new ArrayList<>();

            for (int i = 0; i < displayedOnSites.size(); i++) {

                final String text = displayedOnSites.get(i).getName().toLowerCase();
                if (text.contains(searchText)) {

                    filteredList.add(displayedOnSites.get(i));
                }
            }

            adapter = new EmployeeAdapter(project, addressString, cityString, countryString, stateString, zipString, filteredList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();  // data set changed
        }
    }


    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    boolean isOnTextChanged = false;
    private ForegroundTaskDelegate findOnSitesDelegate;
    private SearchOnSitesDelegate searchOnSitesDelegate;
    private String addressString, zipString, stateString, countryString, cityString;

    public static ProjectEmployeesFragment newInstance(int page,
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
        ProjectEmployeesFragment fragment = new ProjectEmployeesFragment();
        Bundle args = new Bundle();
        fragment.addressString = projectAddress;
        fragment.cityString = projectCity;
        fragment.countryString = projectCountry;
        fragment.stateString = projectState;
        fragment.zipString = projectZip;
        args.putInt(KEY_PAGE, page);
        args.putString(KEY_TITLE, title);
        args.putParcelable(KEY_PROJECT, project);
        args.putString(KEY_CHECKED_IN_PROJECT_UNIQ_ID, checkedInProjectUniqId);
        args.putString(KEY_CHECKED_IN_PROJECT_NAME, checkedInProjectName);
        args.putBoolean(KEY_USER_IN_PROJECT, userInProject);
        args.putString(KEY_USER_IN_PROJECT_ADD, projectAddress);
        args.putString(KEY_USER_IN_PROJECT_CITY, projectCity);
        args.putString(KEY_USER_IN_PROJECT_COUNTRY, projectCountry);
        args.putString(KEY_USER_IN_PROJECT_STATE, projectState);
        args.putString(KEY_USER_IN_PROJECT_ZIP, projectZip);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findOnSitesDelegate = new FindOnSitesDelegate(this);
        listOfForegroundTaskDelegates.add(findOnSitesDelegate);

        searchOnSitesDelegate = new SearchOnSitesDelegate(this);
        listOfForegroundTaskDelegates.add(searchOnSitesDelegate);

        listOfOnSites = new Vector<>();
        displayedOnSites = new Vector<>();

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        adapter = new EmployeeAdapter(project, addressString, cityString, countryString, stateString, zipString, displayedOnSites);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeResources(R.color.logo_orange);
        swipeRefreshLayout.setOnRefreshListener(this);


        User user = pcActivity.getPCApplication().getPcFunctionService().getInternalStoredUser();
        if (user != null && user.getRole().equals("user")) {

        } else {
            pcActivity.getPCApplication().getPcFunctionService().findOnSites(project.getUniq_id(), findOnSitesDelegate);
        }
    }

    @Override
    public void reload(Project project, String checkedInProjectUniqId) {
        super.reload(project, checkedInProjectUniqId);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        pcActivity.getPCApplication().getPcFunctionService().findOnSites(project.getUniq_id(), findOnSitesDelegate);
    }

    private static class FindOnSitesDelegate extends ForegroundTaskDelegate<List<OnSite>> {

        private final WeakReference<ProjectEmployeesFragment> fragmentWeakReference;

        public FindOnSitesDelegate(ProjectEmployeesFragment fragment) {
            super(fragment.pcActivity);
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void onPostExecute(List<OnSite> onSites, Throwable throwable) {
            super.onPostExecute(onSites, throwable);

            ProjectEmployeesFragment fragment = fragmentWeakReference.get();
            if (throwable == null &&
                    fragment != null && !fragment.isDetached() && !fragment.isRemoving()) {

                fragment.txtSearch.setText("");

                fragment.listOfOnSites.clear();
                fragment.listOfOnSites.addAll(onSites);

                fragment.displayedOnSites.clear();
                fragment.displayedOnSites.addAll(onSites);
                Collections.sort(fragment.displayedOnSites, new Comparator<OnSite>() {
                    @Override
                    public int compare(OnSite lhs, OnSite rhs) {
                        return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
                    }

                });
                fragment.adapter.notifyDataSetChanged();
                fragment.swipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    private static class SearchOnSitesDelegate extends ForegroundTaskDelegate<List<OnSite>> {

        private final WeakReference<ProjectEmployeesFragment> fragmentWeakReference;

        public SearchOnSitesDelegate(ProjectEmployeesFragment fragment) {
            super(fragment.pcActivity);
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void onPostExecute(List<OnSite> onSites, Throwable throwable) {
            super.onPostExecute(onSites, throwable);

            ProjectEmployeesFragment fragment = fragmentWeakReference.get();
            if (throwable == null &&
                    fragment != null && !fragment.isDetached() && !fragment.isRemoving()) {
                fragment.displayedOnSites.clear();

                fragment.displayedOnSites.addAll(onSites);
                fragment.adapter.notifyDataSetChanged();
            }
        }
    }

    public void addTextListener() {


    }
}
