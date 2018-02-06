package com.inverseapps.punchcard.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.adapters.DepthPageTransformer;
import com.inverseapps.punchcard.adapters.SmartFragmentStatePagerAdapter;
import com.inverseapps.punchcard.fragments.PCProjectDetailsFragment;
import com.inverseapps.punchcard.fragments.ProjectDetailsFragment;
import com.inverseapps.punchcard.fragments.ProjectEmployeesFragment;
import com.inverseapps.punchcard.fragments.ProjectLogNotesFragment;
import com.inverseapps.punchcard.fragments.ProjectOnSitesFragment;
import com.inverseapps.punchcard.model.Project;
import com.inverseapps.punchcard.model.User;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnPageChange;

public class ProjectDetailsActivity extends PCActivity {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.activity_project_details;
    }

    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    @BindView(R.id.viewGroupProjectTab)
    RelativeLayout viewGroupProjectTab;

    @BindView(R.id.imgViewProject)
    ImageView imgViewProject;

    @BindView(R.id.lblSubProjectTab)
    TextView lblSubProjectTab;

    @BindView(R.id.viewGroupEmployeesTab)
    RelativeLayout viewGroupEmployeesTab;

    @BindView(R.id.imgViewEmployees)
    ImageView imgViewEmployees;

    @BindView(R.id.lblSubEmployeesTab)
    TextView lblSubEmployeesTab;

    @BindView(R.id.viewGroupOnSiteTab)
    RelativeLayout viewGroupOnSiteTab;

    @BindView(R.id.imgViewOnSite)
    ImageView imgViewOnSite;

    @BindView(R.id.lblSubOnSiteTab)
    TextView lblSubOnSiteTab;

    @BindView(R.id.viewGroupLogNoteTab)
    RelativeLayout viewGroupLogNoteTab;

    @BindView(R.id.imgViewLogNote)
    ImageView imgViewLogNote;

    @BindView(R.id.lblSubLogNoteTab)
    TextView lblSubLogNoteTab;

    @OnClick(R.id.viewGroupProjectTab)
    public void onClickedProjectMenu() {

        imgViewProject.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu_project_activated));
        lblSubProjectTab.setTextColor(ContextCompat.getColor(this, R.color.logo_orange));

        imgViewEmployees.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu_tasks));
        lblSubEmployeesTab.setTextColor(ContextCompat.getColor(this, R.color.background_grey));

        imgViewOnSite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu_users));
        lblSubOnSiteTab.setTextColor(ContextCompat.getColor(this, R.color.background_grey));

        imgViewLogNote.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu_log));
        lblSubLogNoteTab.setTextColor(ContextCompat.getColor(this, R.color.background_grey));

        mViewPager.setCurrentItem(0, true);

    }

    @OnClick(R.id.viewGroupEmployeesTab)
    public void onClickedEmployeesMenu() {

        imgViewProject.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu_project));
        lblSubProjectTab.setTextColor(ContextCompat.getColor(this, R.color.background_grey));

        imgViewEmployees.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu_tasks_activated));
        lblSubEmployeesTab.setTextColor(ContextCompat.getColor(this, R.color.logo_orange));

        imgViewOnSite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu_users));
        lblSubOnSiteTab.setTextColor(ContextCompat.getColor(this, R.color.background_grey));

        imgViewLogNote.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu_log));
        lblSubLogNoteTab.setTextColor(ContextCompat.getColor(this, R.color.background_grey));

        mViewPager.setCurrentItem(1, true);

    }

    @OnClick(R.id.viewGroupOnSiteTab)
    public void onClickedOnSiteMenu() {

        imgViewProject.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu_project));
        lblSubProjectTab.setTextColor(ContextCompat.getColor(this, R.color.background_grey));

        imgViewEmployees.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu_tasks));
        lblSubEmployeesTab.setTextColor(ContextCompat.getColor(this, R.color.background_grey));

        imgViewOnSite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu_users_activated));
        lblSubOnSiteTab.setTextColor(ContextCompat.getColor(this, R.color.logo_orange));

        imgViewLogNote.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu_log));
        lblSubLogNoteTab.setTextColor(ContextCompat.getColor(this, R.color.background_grey));

        mViewPager.setCurrentItem(2, true);

    }

    @OnClick(R.id.viewGroupLogNoteTab)
    public void onClickedLogMenu() {

        imgViewProject.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu_project));
        lblSubProjectTab.setTextColor(ContextCompat.getColor(this, R.color.background_grey));

        imgViewEmployees.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu_tasks));
        lblSubEmployeesTab.setTextColor(ContextCompat.getColor(this, R.color.background_grey));

        imgViewOnSite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu_users));
        lblSubOnSiteTab.setTextColor(ContextCompat.getColor(this, R.color.background_grey));

        imgViewLogNote.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu_log_activated));
        lblSubLogNoteTab.setTextColor(ContextCompat.getColor(this, R.color.logo_orange));

        mViewPager.setCurrentItem(3, true);

    }

    @OnPageChange(value = R.id.viewPager, callback = OnPageChange.Callback.PAGE_SELECTED)
    void onPageSelected(int position) {
        currentPage = position;
        PCProjectDetailsFragment fragment = mSectionsPagerAdapter.getRegisteredFragment(position);
        if (fragment == null) {
            fragment =  mSectionsPagerAdapter.instantiateItem(mViewPager, position);
        }
        fragment.reload(project, checkedInProjectUniqId);
    }

    public static final String EVENT_PUNCH_IN_OUT = "event-punch-in-out";

    private BroadcastReceiver checkInOutMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(EVENT_PUNCH_IN_OUT)) {
                checkedInProjectUniqId = intent.getStringExtra(KEY_CHECKED_IN_PROJECT_UNIQ_ID);
                checkedInProjectName = intent.getStringExtra(KEY_CHECKED_IN_PROJECT_NAME);
                supportInvalidateOptionsMenu();
            }
        }
    };

    private ProjectDetailsPagerAdapter mSectionsPagerAdapter;

    public static final String KEY_PROJECT = "project";
    public static final String KEY_CHECKED_IN_PROJECT_UNIQ_ID = "checkedInProjectUniqId";
    public static final String KEY_CHECKED_IN_PROJECT_NAME = "checkedInProjectName";
    public static final String KEY_CURRENT_PAGE = "currentPage";
    public static final String KEY_SCANNED_PROJECT_IDS = "scannedProjectIds";
    public static final String KEY_USER_IN_PROJECT = "userInProject";
    public static final String KEY_USER_PROJECT_POLYFENCE = "polyfence";
    public static final String KEY_USER_PROJECT_ADDRESS = "address";
    public static final String KEY_USER_PROJECT_CITY = "city";
    public static final String KEY_USER_PROJECT_ZIP = "zip";
    public static final String KEY_USER_PROJECT_COUNTRY = "country";
    public static final String KEY_USER_PROJECT_STATE = "state";



    private Project project;
    private String checkedInProjectUniqId,polyfenceString,addressString,zipString,stateString,countryString,cityString;
    private String checkedInProjectName;
    private int currentPage;
    private ArrayList<Integer> scannedProjectIds=new ArrayList<>();
    private boolean userInProject;

    private static int NUM_PAGES = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        project = getIntent().getParcelableExtra(KEY_PROJECT);
        checkedInProjectUniqId = getIntent().getStringExtra(KEY_CHECKED_IN_PROJECT_UNIQ_ID);
        checkedInProjectName = getIntent().getStringExtra(KEY_CHECKED_IN_PROJECT_NAME);
        polyfenceString = getIntent().getStringExtra(KEY_USER_PROJECT_POLYFENCE);
        addressString= getIntent().getStringExtra(KEY_USER_PROJECT_ADDRESS);
        zipString= getIntent().getStringExtra(KEY_USER_PROJECT_ZIP);
        stateString= getIntent().getStringExtra(KEY_USER_PROJECT_STATE);
        countryString= getIntent().getStringExtra(KEY_USER_PROJECT_COUNTRY);
        cityString=getIntent().getStringExtra(KEY_USER_PROJECT_CITY);
        currentPage = getIntent().getIntExtra(KEY_CURRENT_PAGE, 0);
        scannedProjectIds = getIntent().getIntegerArrayListExtra(KEY_SCANNED_PROJECT_IDS);
        userInProject = getIntent().getBooleanExtra(KEY_USER_IN_PROJECT, true);


        if (savedInstanceState != null) {
            project = savedInstanceState.getParcelable(KEY_PROJECT);
            checkedInProjectUniqId = savedInstanceState.getString(KEY_CHECKED_IN_PROJECT_UNIQ_ID);
            polyfenceString = getIntent().getStringExtra(KEY_USER_PROJECT_POLYFENCE);
            addressString= getIntent().getStringExtra(KEY_USER_PROJECT_ADDRESS);
            zipString= getIntent().getStringExtra(KEY_USER_PROJECT_ZIP);
            stateString= getIntent().getStringExtra(KEY_USER_PROJECT_STATE);
            countryString= getIntent().getStringExtra(KEY_USER_PROJECT_COUNTRY);
            cityString=getIntent().getStringExtra(KEY_USER_PROJECT_CITY);
            checkedInProjectName = savedInstanceState.getString(KEY_CHECKED_IN_PROJECT_NAME);
            currentPage = savedInstanceState.getInt(KEY_CURRENT_PAGE);
            scannedProjectIds = savedInstanceState.getIntegerArrayList(KEY_SCANNED_PROJECT_IDS);
            userInProject = savedInstanceState.getBoolean(KEY_USER_IN_PROJECT);
        }
        if (scannedProjectIds == null) {
            scannedProjectIds = new ArrayList<>();
        }


        User user = pcFunctionService.getInternalStoredUser();
        project= pcFunctionService.getInternalStoredProject();
        if (user != null && user.getRole().equals("user")) {
            viewGroupEmployeesTab.setVisibility(View.GONE);
            viewGroupOnSiteTab.setVisibility(View.GONE);
        }


        LocalBroadcastManager.getInstance(this).registerReceiver(checkInOutMessageReceiver,
                new IntentFilter(EVENT_PUNCH_IN_OUT));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(project.getName());

        mSectionsPagerAdapter = new ProjectDetailsPagerAdapter(getSupportFragmentManager(),
                project,
                checkedInProjectUniqId,
                polyfenceString,
                addressString,
                cityString,
                countryString,
                stateString,
                zipString,
                checkedInProjectName,
                userInProject);

        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(NUM_PAGES);
        mSectionsPagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(currentPage);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_PROJECT, project);
        outState.putString(KEY_CHECKED_IN_PROJECT_UNIQ_ID, checkedInProjectUniqId);
        outState.putString(KEY_USER_PROJECT_POLYFENCE,polyfenceString);
        outState.putString(KEY_USER_PROJECT_ADDRESS,addressString);
        outState.putString(KEY_USER_PROJECT_CITY,cityString);
        outState.putString(KEY_USER_PROJECT_COUNTRY,countryString);
        outState.putString(KEY_USER_PROJECT_STATE,stateString);
        outState.putString(KEY_USER_PROJECT_ZIP,zipString);
        outState.putString(KEY_CHECKED_IN_PROJECT_NAME, checkedInProjectName);
        outState.putInt(KEY_CURRENT_PAGE, currentPage);
        outState.putIntegerArrayList(KEY_SCANNED_PROJECT_IDS, scannedProjectIds);
        outState.putBoolean(KEY_USER_IN_PROJECT, userInProject);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(checkInOutMessageReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_project_details, menu);

        boolean checkedIn = false;
        if (project.getUniq_id().equals(checkedInProjectUniqId)) {
            checkedIn = true;
        }
        boolean hasScanPermission = false;
        if (scannedProjectIds != null) {
            for (long id : scannedProjectIds) {
                if (id == project.getId()) {
                    hasScanPermission = true;
                    break;
                }
            }
        }
        if(hasScanPermission && checkedIn) {
           menu.getItem(1).setVisible(true);
        } else {
          menu.getItem(1).setVisible(false);
          // menu.getItem(1).setVisible(true);
        }
        if (!userInProject) {
            menu.getItem(0).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_badge:
                gotoProjectBadgeScreen();
                return true;
            case R.id.action_qr:
                gotoVirtualGateScreen();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void gotoProjectBadgeScreen() {
        User user = getPCApplication().getPcFunctionService().getInternalStoredUser();
        String userUniqId = user.getUniq_id();
        String projectUniqId = project.getUniq_id();
        String projectLogo = project.getClient().getLogo();
        String projectAddress = addressString;
        String projectcity =cityString;
        String projectstate = stateString;
        String projectzip = zipString;
        String projectcountry = countryString;
        Intent intent = new Intent(this, BadgeActivity.class);
        intent.putExtra(BadgeActivity.KEY_USER_UNIQ_ID, userUniqId);
        intent.putExtra(BadgeActivity.KEY_PROJECT_UNIQ_ID, projectUniqId);
        intent.putExtra(BadgeActivity.KEY_PROJECT_LOGO, projectLogo);
        intent.putExtra(BadgeActivity.KEY_ADDRESS, projectAddress);
        intent.putExtra(BadgeActivity.KEY_CITY, projectcity);
        intent.putExtra(BadgeActivity.KEY_STATE, projectstate);
        intent.putExtra(BadgeActivity.KEY_ZIP, projectzip);
        intent.putExtra(BadgeActivity.KEY_COUNTRY, projectcountry);
        intent.putExtra(BadgeActivity.KEY_IS_MY_BADGE, true);

        startActivity(intent);
    }

    private void gotoVirtualGateScreen() {
        Intent intent = new Intent(this, VirtualGateActivity.class);
        intent.putExtra(VirtualGateActivity.KEY_PROJECT, project);
        startActivity(intent);
    }

    private static class ProjectDetailsPagerAdapter extends SmartFragmentStatePagerAdapter<PCProjectDetailsFragment> {

        private Project project;
        private String checkedInProjectUniqId;
        private String checkedInProjectName;
        private  String polyfenceString,addressString,cityString,countryString,stateString,zipString;


        private boolean userInProject;

        public ProjectDetailsPagerAdapter(FragmentManager fragmentManager,
                                          Project project,
                                          String checkedInProjectUniqId,
                                          String polyfenceString,
                                          String addressString,
                                          String cityString,
                                          String countryString,
                                          String stateString,
                                          String zipString,
                                          String checkedInProjectName,
                                          boolean userInProject) {
            super(fragmentManager);
            this.project = project;
            this.checkedInProjectUniqId = checkedInProjectUniqId;
            this.checkedInProjectName = checkedInProjectName;
            this.polyfenceString = polyfenceString;
            this.addressString = addressString;
            this.cityString = cityString;
            this.countryString = countryString;
            this.stateString = stateString;
            this.zipString = zipString;
            this.userInProject = userInProject;
        }

        @Override
        public PCProjectDetailsFragment getItem(int position) {
            switch (position) {
                case 0:
                    return ProjectDetailsFragment.newInstance(0,
                            "",
                            project,
                            checkedInProjectUniqId,
                            polyfenceString,
                            checkedInProjectName,
                            userInProject);
                case 1:
                    return ProjectEmployeesFragment.newInstance(1,
                            "",
                            project,
                            checkedInProjectUniqId,
                            addressString,
                            cityString,
                            countryString,
                            stateString,
                            zipString,
                            checkedInProjectName,
                            userInProject);
                case 2:
                    return ProjectOnSitesFragment.newInstance(2,
                            "",
                            project,
                            checkedInProjectUniqId,
                            addressString,
                            cityString,
                            countryString,
                            stateString,
                            zipString,
                            checkedInProjectName,
                            userInProject);
                case 3:
                    return ProjectLogNotesFragment.newInstance(3,
                            "",
                            project,
                            checkedInProjectUniqId,
                            checkedInProjectName,
                            userInProject);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
