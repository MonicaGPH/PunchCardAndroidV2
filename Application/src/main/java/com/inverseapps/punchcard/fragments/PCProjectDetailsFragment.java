package com.inverseapps.punchcard.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.inverseapps.punchcard.model.Project;


/**
 * Created by Inverse, LLC on 10/19/16.
 */

public abstract class PCProjectDetailsFragment extends PCFragment {

    protected static final String KEY_PAGE = "page";
    protected static final String KEY_TITLE = "title";
    protected static final String KEY_PROJECT = "project";
    protected static final String KEY_CHECKED_IN_PROJECT_UNIQ_ID = "checkedInProjectUniqId";
    protected static final String KEY_PROJECT_POLYFENCE = "polyfence";
    protected static final String KEY_CHECKED_IN_PROJECT_NAME = "checkedInProjectName";
    public static final String KEY_USER_IN_PROJECT = "userInProject";
    public static final String KEY_USER_IN_PROJECT_ADD = "userInProjectAddress";
    public static final String KEY_USER_IN_PROJECT_CITY = "userInProjectCity";
    public static final String KEY_USER_IN_PROJECT_COUNTRY = "userInProjectCountry";
    public static final String KEY_USER_IN_PROJECT_STATE = "userInProjectState";
    public static final String KEY_USER_IN_PROJECT_ZIP = "userInProjectZip";


    protected int page;
    protected String title;
    public Project project;
    public String checkedInProjectUniqId,polyfenceString;
    public String checkedInProjectName;
    protected boolean userInProject;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        page = getArguments().getInt(KEY_PAGE);
        title = getArguments().getString(KEY_TITLE);
        project = getArguments().getParcelable(KEY_PROJECT);
        checkedInProjectUniqId = getArguments().getString(KEY_CHECKED_IN_PROJECT_UNIQ_ID);
        polyfenceString = getArguments().getString(KEY_PROJECT_POLYFENCE);
        checkedInProjectName = getArguments().getString(KEY_CHECKED_IN_PROJECT_NAME);
        userInProject = getArguments().getBoolean(KEY_USER_IN_PROJECT);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            page = savedInstanceState.getInt(KEY_PAGE);
            title = savedInstanceState.getString(KEY_TITLE);
            project = savedInstanceState.getParcelable(KEY_PROJECT);
            polyfenceString= savedInstanceState.getString(KEY_PROJECT_POLYFENCE);
            checkedInProjectUniqId = savedInstanceState.getString(KEY_CHECKED_IN_PROJECT_UNIQ_ID);
            checkedInProjectName = savedInstanceState.getString(KEY_CHECKED_IN_PROJECT_NAME);
            userInProject = savedInstanceState.getBoolean(KEY_USER_IN_PROJECT);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_PAGE, page);
        outState.putString(KEY_TITLE, title);
        outState.putParcelable(KEY_PROJECT, project);
        outState.putString(KEY_CHECKED_IN_PROJECT_UNIQ_ID, checkedInProjectUniqId);
        outState.putString(KEY_PROJECT_POLYFENCE,polyfenceString);
        outState.putString(KEY_CHECKED_IN_PROJECT_NAME, checkedInProjectName);
        outState.putBoolean(KEY_USER_IN_PROJECT, userInProject);
        super.onSaveInstanceState(outState);
    }

    public void reload(Project project,  String checkedInProjectUniqId) {


        this.project = project;
        this.checkedInProjectUniqId = checkedInProjectUniqId;


    }
    public void reload(Project project,String polyfenceString,  String checkedInProjectUniqId) {


        this.project = project;
        this.polyfenceString = polyfenceString;
        this.checkedInProjectUniqId = checkedInProjectUniqId;


    }
}
