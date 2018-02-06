package com.inverseapps.punchcard.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.view.MenuItem;


import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.adapters.ChooseOptionAdapter;
import com.inverseapps.punchcard.model.Choose_Option;
import com.inverseapps.punchcard.model.User;
import com.inverseapps.punchcard.utils.SnappingRecyclerView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by asus on 02-Nov-17.
 */

public class ChooseOptionActivity extends PCActivity {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.activity_show_options;
    }

    @NonNull
    @BindView(R.id.recyclerView)
    SnappingRecyclerView recyclerView;

    //a list to store all the products
    private List<Choose_Option> choose_optionList;
    private User user;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = getPCApplication().getPcFunctionService().getInternalStoredUser();
        recyclerView.enableViewScaling(true);
        recyclerView.scrollBy(1);
        choose_optionList = new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Choose your plan");
        String tier = user.getClient().getPlan_test().toLowerCase();
        Logger.d(user.getClient().getPlan_test());

        ChooseOptionAdapter adapter;
        switch (tier) {
            case "trial":
                //adding some items to our list
                choose_optionList.add(new Choose_Option("PunchCard BASE", "1", "base", 0));
                choose_optionList.add(new Choose_Option("PunchCard STANDARD", "2", "standard", 0));
              //  choose_optionList.add(new Choose_Option("PunchCard PREMIUM", "3", "premium", 0));
             //   choose_optionList.add(new Choose_Option("PunchCard ENTERPRISE", "4", "enterprise", 0));


                //creating recyclerview adapter
                adapter = new ChooseOptionAdapter(this, choose_optionList,user);

                //setting adapter to recyclerview
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                break;

            case "trialexpired":
                //adding some items to our list
                choose_optionList.add(new Choose_Option("PunchCard BASE", "1", "base", 0));
                choose_optionList.add(new Choose_Option("PunchCard STANDARD", "2", "standard", 0));
              //  choose_optionList.add(new Choose_Option("PunchCard PREMIUM", "3", "premium", 0));
             //   choose_optionList.add(new Choose_Option("PunchCard ENTERPRISE", "4", "enterprise", 0));


                //creating recyclerview adapter
                adapter = new ChooseOptionAdapter(this, choose_optionList,user);

                //setting adapter to recyclerview
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                break;

            case "unsubscribe":
                //adding some items to our list
                choose_optionList.add(new Choose_Option("PunchCard BASE", "1", "base", 0));
                choose_optionList.add(new Choose_Option("PunchCard STANDARD", "2", "standard", 0));
              //  choose_optionList.add(new Choose_Option("PunchCard PREMIUM", "3", "premium", 0));
              //  choose_optionList.add(new Choose_Option("PunchCard ENTERPRISE", "4", "enterprise", 0));


                //creating recyclerview adapter
                adapter = new ChooseOptionAdapter(this, choose_optionList,user);

                //setting adapter to recyclerview
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                break;

            case "base":
                choose_optionList.add(new Choose_Option("PunchCard BASE", "1", "base", 1));
                choose_optionList.add(new Choose_Option("PunchCard STANDARD", "2", "standard", 0));
              //  choose_optionList.add(new Choose_Option("PunchCard PREMIUM", "3", "premium", 0));
              //  choose_optionList.add(new Choose_Option("PunchCard ENTERPRISE", "4", "enterprise", 0));
                //creating recyclerview adapter
                adapter = new ChooseOptionAdapter(this, choose_optionList,user);
                //setting adapter to recyclerview
                recyclerView.setAdapter(adapter);
                break;
            case "premium":
                choose_optionList.add(new Choose_Option("PunchCard BASE", "1", "base", 0));
                choose_optionList.add(new Choose_Option("PunchCard STANDARD", "2", "standard", 0));
              //  choose_optionList.add(new Choose_Option("PunchCard PREMIUM", "3", "premium", 1));
              //  choose_optionList.add(new Choose_Option("PunchCard ENTERPRISE", "4", "enterprise", 0));

                //creating recyclerview adapter
                adapter = new ChooseOptionAdapter(this, choose_optionList,user);
                //setting adapter to recyclerview
                recyclerView.setAdapter(adapter);
                break;
            case "standard":
                choose_optionList.add(new Choose_Option("PunchCard BASE", "1", "base", 0));
                choose_optionList.add(new Choose_Option("PunchCard STANDARD", "2", "standard", 1));
              //  choose_optionList.add(new Choose_Option("PunchCard PREMIUM", "3", "premium", 0));
              //  choose_optionList.add(new Choose_Option("PunchCard ENTERPRISE", "4", "enterprise", 0));
                //creating recyclerview adapter
                adapter = new ChooseOptionAdapter(this, choose_optionList,user);
                //setting adapter to recyclerview
                recyclerView.setAdapter(adapter);
                break;

            case "enterprise":
                choose_optionList.add(new Choose_Option("PunchCard BASE", "1", "base", 0));
                choose_optionList.add(new Choose_Option("PunchCard STANDARD", "2", "standard", 0));
              //  choose_optionList.add(new Choose_Option("PunchCard PREMIUM", "3", "premium", 0));
              //  choose_optionList.add(new Choose_Option("PunchCard ENTERPRISE", "4", "enterprise", 1));
                //creating recyclerview adapter
                adapter = new ChooseOptionAdapter(this, choose_optionList,user);
                //setting adapter to recyclerview
                recyclerView.setAdapter(adapter);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}