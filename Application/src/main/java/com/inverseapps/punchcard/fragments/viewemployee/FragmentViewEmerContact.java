package com.inverseapps.punchcard.fragments.viewemployee;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.model.Employee;
import com.inverseapps.punchcard.model.Project;

import butterknife.BindView;

/**
 * Created by Inverse, LLC on 10/22/16.
 */

public class FragmentViewEmerContact extends PCFragmentViewInfo {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.fragment_view_emer_contact;
    }

    @NonNull
    @BindView(R.id.lblEmerName)
    TextView lblEmerName;

    @NonNull
    @BindView(R.id.lblEmerRelation)
    TextView lblEmerRelation;

    @NonNull
    @BindView(R.id.lblEmerEmail)
    TextView lblEmerEmail;

    @NonNull
    @BindView(R.id.lblEmerMobileNumber)
    TextView lblEmerMobileNumber;

    @NonNull
    @BindView(R.id.lblEmerHomeNumber)
    TextView lblEmerHomeNumber;

    @NonNull
    @BindView(R.id.lblEmerWorkNumber)
    TextView lblEmerWorkNumber;

    @Override
    public void reload(Project project, Employee employee) {
        super.reload(project, employee);

        if (employee.getJdoc().getEmerContact() != null) {
            lblEmerName.setText(String.format("Emer. Name: %s", employee.getJdoc().getEmerContact().getName()));
            lblEmerRelation.setText(String.format("Emer. Relation: %s", employee.getJdoc().getEmerContact().getRelation()));
            lblEmerEmail.setText(String.format("Emer. Email: %s", employee.getJdoc().getEmerContact().getEmail()));
            lblEmerMobileNumber.setText(String.format("Emer. Mobile Number: %s", employee.getJdoc().getEmerContact().getMobileNumber()));
            lblEmerHomeNumber.setText(String.format("Emer. Home Number: %s", employee.getJdoc().getEmerContact().getHomeNumber()));
            lblEmerWorkNumber.setText(String.format("Emer. Work Number: %s", employee.getJdoc().getEmerContact().getWorkNumber()));
        }
    }

}
