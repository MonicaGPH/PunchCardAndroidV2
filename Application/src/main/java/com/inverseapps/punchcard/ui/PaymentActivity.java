package com.inverseapps.punchcard.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.app.AlertDialog;


import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;

import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.inverseapps.punchcard.Constant.AppConstant;

import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.exception.ServiceException;

import com.inverseapps.punchcard.model.User;

import com.inverseapps.punchcard.model.Billing_address;

import com.inverseapps.punchcard.model.param.PaymentParam;
import com.inverseapps.punchcard.model.param.UpdateParam;
import com.inverseapps.punchcard.utils.Card;
import com.inverseapps.punchcard.utils.CardForm;
import com.inverseapps.punchcard.utils.EmailValidator;
import com.inverseapps.punchcard.utils.OnPayBtnClickListner;
import com.orhanobut.logger.Logger;


import butterknife.BindView;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;


/**
 * Created by asus on 08-Nov-17.
 */

public class PaymentActivity extends PCActivity {
    public static final String KEY_PRICE = "price";
    public static final String KEY_VERSION = "version";
    public static final String KEY_CHECKCALLFROMACTIVITY = "checkcallfromactivity";
    public static final String KEY_ISCARDRENEW = "cardRenew";
    public SendPaymentDetailDelegate sendPaymentDetailDelegate;
    public UpdatePaymentDetailDelegate updatePaymentDetailDelegate;
    public AuthTokenDelegate authTokenDelegate;
    public String part1_version, part2_version, parts_price, parts_version, part1_name, part2_name, splitYear, companyHandle, userName, password;
    final int maxTextLengthMobile = 10;//max length of your text
    final int maxTextLengthCvv = 3;//max length of your text
    final int maxTextLengthCardNumber = 16;
    public FindUserDelegate findUserDelegate;
    public LogInDelegate logInDelegate;
    int comapany_id;
    public User user;
    public String regexPhoneNumber = "^(?:(?:\\+?1\\s*(?:[.-]\\s*)?)?(?:\\(\\s*([2-9]1[02-9]|[2-9][02-9]1|[2-9][02-9][02-9])\\s*\\)|([2-9]1[02-9]|[2-9][02-9]1|[2-9][02-9][02-9]))\\s*(?:[.-]\\s*)?)?([2-9]1[02-9]|[2-9][02-9]1|[2-9][02-9]{2})\\s*(?:[.-]\\s*)?([0-9]{4})(?:\\s*(?:#|x\\.?|ext\\.?|extension)\\s*(\\d+))?$";
    private String price, version, email,createdATParent;
    private boolean callFromActivity, isCardRenew;


    @Override
    protected int getRootLayoutRes() {
        return R.layout.activity_payment_page;
    }

    @NonNull
    @BindView(R.id.txtVersion)
    TextView txtVersion;
    @BindView(R.id.txtAddress)
    EditText txtAddress;
    @NonNull
    @BindView(R.id.txtEmail)
    EditText txtEmail;
    @NonNull
    @BindView(R.id.txtCity)
    EditText txtCity;
    @NonNull
    @BindView(R.id.txtMobile)
    EditText txtMobile;
    @NonNull
    @BindView(R.id.txtFirstName)
    EditText txtFirstName;
    @NonNull
    @BindView(R.id.txtLastName)
    EditText txtLastName;
    @NonNull
    @BindView(R.id.txtState)
    EditText txtState;
    @NonNull
    @BindView(R.id.txtZip)
    EditText txtZip;

    @NonNull
    @BindView(R.id.txtPrice)
    TextView txtPrice;

    @NonNull
    @BindView(R.id.layoutPriceANDVersion)
    LinearLayout layoutPriceANDVersion;

    @NonNull
    @BindView(R.id.txtCardNumber)
    CardForm txtCardNumber;


    @OnFocusChange({R.id.txtEmail, R.id.txtCity, R.id.txtMobile, R.id.txtState, R.id.txtZip, R.id.txtAddress, R.id.txtLastName, R.id.txtFirstName})
    public void onFocusChange(View view, boolean hasFocus) {
        if (!txtEmail.hasFocus() &&
                !txtCity.hasFocus() &&
                !txtMobile.hasFocus() &&
                !txtState.hasFocus() &&
                !txtFirstName.hasFocus() &&
                !txtLastName.hasFocus() &&
                !txtAddress.hasFocus() &&
                !txtZip.hasFocus() &&
                !txtCardNumber.hasFocus()) {
            hideKeyboard();
        }
    }

    @OnEditorAction(R.id.txtEmail)
    public boolean onEditorActionEmail(EditText v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            txtFirstName.requestFocus();
            return true;
        }
        return false;
    }

    @OnEditorAction(R.id.txtAddress)
    public boolean onEditorActionAdd(EditText v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            txtCity.requestFocus();
            return true;
        }
        return false;
    }

    @OnEditorAction(R.id.txtFirstName)
    public boolean onEditorActionFirstName(EditText v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            txtLastName.requestFocus();
            return true;
        }
        return false;
    }

    @OnEditorAction(R.id.txtLastName)
    public boolean onEditorActionLastName(EditText v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            txtMobile.requestFocus();
            txtCardNumber.setcardName(txtFirstName.getText().toString(), txtLastName.getText().toString());
            return true;
        }
        return false;
    }

    @OnEditorAction(R.id.txtCity)
    public boolean onEditorActionCity(EditText v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            txtState.requestFocus();
            return true;
        }
        return false;
    }

    @OnEditorAction(R.id.txtMobile)
    public boolean onEditorActionMobile(EditText v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            txtAddress.requestFocus();
            return true;
        }
        return false;
    }

    @OnEditorAction(R.id.txtState)
    public boolean onEditorActionMobileState(EditText v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            txtZip.requestFocus();
            return true;
        }
        return false;
    }

    @OnEditorAction(R.id.txtZip)
    public boolean onEditorActionMobileZip(EditText v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            txtCardNumber.requestFocus();
            return true;
        }
        return false;
    }
    @OnTextChanged(value = {R.id.txtLastName,
    R.id.txtFirstName},
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED) void onAfterTextChanged(Editable editable) {



        if (editable == txtLastName.getEditableText() && !TextUtils.isEmpty(txtLastName.getText().toString())) {
            txtCardNumber.setcardName(txtFirstName.getText().toString(), txtLastName.getText().toString());
        }
       else if (editable == txtFirstName.getEditableText() && !TextUtils.isEmpty(txtFirstName.getText().toString())) {
            txtCardNumber.setcardName(txtFirstName.getText().toString(), txtLastName.getText().toString());
        }

    }

    public void checkToDoneButton(Card card) {

       /* if (card.getName().contains(" ")) {
            String[] parts_name = card.getName().split(" ");
            part1_name = parts_name[0]; // First Name
            part2_name = parts_name[1]; // Second Name
        } else {
            Toast.makeText(PaymentActivity.this, "Enter correct full name", Toast.LENGTH_SHORT).show();
            return;
        }*/


        if (user.getClient().getChild_of_id() == 0) {
            comapany_id = user.getClient_id();
        } else {
            comapany_id = user.getClient().getChild_of_id();
        }
        sendCCDetails(card);
        //  authTokenDelegate.setCard(card);

        // pcFunctionService.login(companyHandle, userName, password, authTokenDelegate);


    }

    private void login() {


        companyHandle = getPCApplication().getAppPreferences().getString(AppConstant.PREF_KEY_COMPANY_HANDLE, " ");
        userName = getPCApplication().getAppPreferences().getString(AppConstant.PREF_KEY_USER_NAME, " ");
        password = getPCApplication().getAppPreferences().getString(AppConstant.PREF_KEY_PASSWORD, " ");

        pcFunctionService.login(companyHandle, userName, password, logInDelegate);
    }

    private void findUser() {
        pcFunctionService.findUser(findUserDelegate);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        price = getIntent().getStringExtra(KEY_PRICE);
        version = getIntent().getStringExtra(KEY_VERSION);
        callFromActivity = getIntent().getBooleanExtra(KEY_CHECKCALLFROMACTIVITY, false);
        if (callFromActivity) {
            getSupportActionBar().setTitle("CC Form");
            isCardRenew = getIntent().getBooleanExtra(KEY_ISCARDRENEW, false);
            if (price.equalsIgnoreCase("0") & version.equalsIgnoreCase("trial")) {

                layoutPriceANDVersion.setVisibility(View.GONE);
            } else {
                layoutPriceANDVersion.setVisibility(View.VISIBLE);
                String[] parts_version = version.split(" ");
                if (parts_version.length >= 2) {
                    part1_version = parts_version[0]; // PunchCard
                    part2_version = parts_version[1]; // BASE / STANDARD / PREMIUM

                    txtPrice.setText(price);
                    txtVersion.setText(part2_version);

                } else {
                    txtPrice.setText(price);
                    txtVersion.setText(version.toUpperCase());
                }
            }

        } else {
            getSupportActionBar().setTitle("Update CC Form");
            layoutPriceANDVersion.setVisibility(View.GONE);
            isCardRenew = getIntent().getBooleanExtra(KEY_ISCARDRENEW, false);
        }

        sendPaymentDetailDelegate = new SendPaymentDetailDelegate(this);
        listOfForegroundTaskDelegates.add(sendPaymentDetailDelegate);

        logInDelegate = new LogInDelegate(this);
        listOfForegroundTaskDelegates.add(logInDelegate);

        findUserDelegate = new FindUserDelegate(this);
        listOfForegroundTaskDelegates.add(findUserDelegate);

        authTokenDelegate = new AuthTokenDelegate(this);
        listOfForegroundTaskDelegates.add(authTokenDelegate);

        updatePaymentDetailDelegate = new UpdatePaymentDetailDelegate(this);
        listOfForegroundTaskDelegates.add(updatePaymentDetailDelegate);


        user = getPCApplication().getPcFunctionService().getInternalStoredUser();


        companyHandle = getPCApplication().getAppPreferences().getString(AppConstant.PREF_KEY_COMPANY_HANDLE, " ");
        userName = getPCApplication().getAppPreferences().getString(AppConstant.PREF_KEY_USER_NAME, " ");
        password = getPCApplication().getAppPreferences().getString(AppConstant.PREF_KEY_PASSWORD, " ");

        /*email = getPCApplication().getAppPreferences().getString(AppConstant.PREF_KEY_USER_NAME, "name");
        txtEmail.setText(email.toString());*/

        txtCardNumber.setPayBtnClickListner(new OnPayBtnClickListner() {
            @Override
            public void onClick(Card card) {
                //Your code here!! use card.getXXX() for get any card property
                //for instance card.getName();


                if (txtAddress.getText().toString().length() == 0) {
                    txtAddress.setError("Address is required!");
                    txtAddress.requestFocus();
                    Toast.makeText(PaymentActivity.this, "Address is required!", Toast.LENGTH_SHORT).show();

                } else if (!new EmailValidator().validate(txtEmail.getText().toString())) {
                    txtEmail.setError("Please enter correct Email Address!");
                    txtEmail.requestFocus();
                } else if (txtZip.getText().toString().length() == 0) {
                    txtZip.setError("Required!");
                    txtZip.requestFocus();
                    Toast.makeText(PaymentActivity.this, "Zip is required!", Toast.LENGTH_SHORT).show();
                } else if (txtFirstName.getText().toString().length() == 0) {
                    txtFirstName.setError("Required!");
                    txtFirstName.requestFocus();
                    Toast.makeText(PaymentActivity.this, "First Name is required!", Toast.LENGTH_SHORT).show();
                } else if (txtLastName.getText().toString().length() == 0) {
                    txtLastName.setError("Required!");
                    txtLastName.requestFocus();
                    Toast.makeText(PaymentActivity.this, "Last Name is required!", Toast.LENGTH_SHORT).show();
                } else if (txtCity.getText().toString().length() == 0) {
                    txtCity.setError("Required!");
                    txtCity.requestFocus();
                } else if (txtState.getText().toString().length() == 0) {
                    txtState.setError("Required!");
                    txtState.requestFocus();
                } else if (!txtMobile.getText().toString().matches(regexPhoneNumber)) {
                    txtMobile.setError("Please enter valid phone number");
                    txtMobile.requestFocus();
                } else {

                    checkToDoneButton(card);


                }

            }
        });


    }

    public static boolean isValidPhone(String phone) {
        String expression = "^([0-9\\+]|\\(\\d{1,3}\\))[0-9\\-\\. ]{3,15}$";
        CharSequence inputString = phone;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputString);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
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

    private static class SendPaymentDetailDelegate extends ForegroundTaskDelegate<Boolean> {


        public SendPaymentDetailDelegate(PaymentActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(Boolean status, Throwable throwable) {
            super.onPostExecute(status, throwable);

            PaymentActivity activity = (PaymentActivity) activityWeakReference.get();
            if (throwable == null && status &&
                    activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                String date_after_30_Days = null;
                String monthName = null;
                String today_date = activity.getCurrentDate();
                try {
                    date_after_30_Days = activity.checkDateAfter30days(today_date, activity);
                    Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date_after_30_Days);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d);
                    monthName = new SimpleDateFormat("MMMM dd, yyyy").format(cal.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                activity.showDialog(activity, activity.txtFirstName.getText().toString() + "\n" + "Thank you for your credit card information. You will not be billed for active users until your billing cycle ends on " + monthName, status);

            }
        }
    }

    private static class UpdatePaymentDetailDelegate extends ForegroundTaskDelegate<Boolean> {


        public UpdatePaymentDetailDelegate(PaymentActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(Boolean status, Throwable throwable) {
            super.onPostExecute(status, throwable);

            PaymentActivity activity = (PaymentActivity) activityWeakReference.get();
            if (throwable == null && status &&
                    activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                String date_after_30_Days = null;
                String today_date = activity.getCurrentDate();
                try {
                    date_after_30_Days = activity.checkDateAfter30days(today_date, activity);
                    Logger.d(date_after_30_Days);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                activity.showDialog(activity, "CC details has been successfully updated. ", status);

            }
        }
    }

    private void gotoHomeScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(HomeActivity.KEY_REMIND_ME_LATER_CLICKED,false);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void showDialog(Context context, String message, final boolean status) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle("Punch Card");
        alertDialog.setMessage(message);


        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // User pressed YES button. Write Logic Here

                if (status)
                    login();
                else {
                    clearform();
                    dialog.dismiss();
                }


            }
        });


        // Showing Alert Message
        alertDialog.show();
    }

    public void clearform() {
        txtAddress.setText("");
        txtCity.setText("");
        txtMobile.setText("");
        txtState.setText("");
        txtZip.setText("");

    }

    private static class FindUserDelegate extends ForegroundTaskDelegate<User> {

        public FindUserDelegate(PaymentActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(User user, Throwable throwable) {
            super.onPostExecute(user, throwable);

            PaymentActivity activity = (PaymentActivity) activityWeakReference.get();
            if (throwable == null
                    && user != null &&
                    activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                activity.gotoHomeScreen();
            }
        }
    }


    private class LogInDelegate extends ForegroundTaskDelegate<Boolean> {

        public LogInDelegate(PCActivity activity) {
            super(activity);
        }

        @Override
        protected void handleServiceError(ServiceException ex) {
            if (ex.getErrorCode() == 401) {
                showAlertDialog("Your credentials were not correct. Please try again!");
            }
        }

        @Override
        public void onPostExecute(Boolean result, Throwable throwable) {
            super.onPostExecute(result, throwable);
            PaymentActivity activity = (PaymentActivity) activityWeakReference.get();
            if (throwable == null &&
                    activity != null && !activity.isFinishing() && !activity.isDestroyed()) {

                activity.findUser();
            }
        }
    }

    public void sendCCDetails(Card card) {

        Billing_address billing_address = new Billing_address(txtAddress.getText().toString(),
                txtCity.getText().toString(),
                "US",
                txtZip.getText().toString(),
                txtState.getText().toString(),
                txtMobile.getText().toString());

        /////////////////////credential of Parent Company's user
        if (user.getClient().getChild_of_id() == 0) {
            createdATParent = user.getClient().getCreated_at();


        }
        /////////////////////credential of Child Company's user
        else {
            createdATParent = user.getCreated_dateParent();

        }
        if (callFromActivity) {


            PaymentParam param = new PaymentParam(card.getNumber(),
                    card.getType().toLowerCase(),
                    card.getExpMonth(),
                    card.getExpYear(),
                    card.getCVC(),
                    txtFirstName.getText().toString(),
                    txtLastName.getText().toString(),
                    billing_address,
                 //   user.getClient().getPlan_test(),
                 //   createdATParent,
                    txtVersion.getText().toString().toLowerCase(),
                    String.valueOf(user.getId()),
                    comapany_id,
                    isCardRenew
            );

            pcFunctionService.sendPaymentDetails(
                    param,
                    sendPaymentDetailDelegate
            );
        } else {


            UpdateParam param = new UpdateParam(card.getNumber(),
                    card.getType().toLowerCase(),
                    card.getExpMonth(),
                    card.getExpYear(),
                    card.getCVC(),
                    txtFirstName.getText().toString(),
                    txtLastName.getText().toString(),
                    billing_address,
            //        user.getClient().getPlan_test(),
             //       createdATParent,
                    String.valueOf(user.getId()),
                    comapany_id,
                    isCardRenew
            );
            pcFunctionService.updatePaymentDetails(
                    param,
                    updatePaymentDetailDelegate
            );
        }

    }

    private class AuthTokenDelegate extends ForegroundTaskDelegate<Boolean> {


        @NonNull
        private Card card;

        public void setCard(@NonNull Card card) {
            this.card = card;
        }

        public AuthTokenDelegate(PCActivity activity) {
            super(activity);
        }

        @Override
        protected void handleServiceError(ServiceException ex) {
            if (ex.getErrorCode() == 401) {
                showAlertDialog("Your credentials were not correct. Please try again!");
            }
        }

        @Override
        public void onPostExecute(Boolean result, Throwable throwable) {
            super.onPostExecute(result, throwable);
            PaymentActivity activity = (PaymentActivity) activityWeakReference.get();
            if (throwable == null &&
                    activity != null && !activity.isFinishing() && !activity.isDestroyed()) {


                activity.getPCApplication()
                        .getPcFunctionService()
                        .internalStoreBasicUserInfoIfNeeded(userName, password, true);
                activity.sendCCDetails(card);

            }
        }
    }
}
