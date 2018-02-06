package com.inverseapps.punchcard.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.inverseapps.punchcard.Constant.AppConstant;
import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.model.Badge;
import com.inverseapps.punchcard.model.Project;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;

import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;


public class BadgeActivity extends PCActivity {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.activity_badge;
    }

    @NonNull
    @BindView(R.id.lblProjectName)
    TextView lblProjectName;


    @NonNull
    @BindView(R.id.imgViewEmployee)
    ImageView imgViewEmployee;

    @NonNull
    @BindView(R.id.lblCompanyName)
    TextView lblCompanyName;

    @NonNull
    @BindView(R.id.lblEmployeeName)
    TextView lblEmployeeName;

    @NonNull
    @BindView(R.id.lblProjectAddress)
    TextView lblProjectAddress;

    @BindView(R.id.imgViewBadgess)
    ImageView imgViewBadge;

    @NonNull
    @BindView(R.id.lblBadgeNumber)
    TextView lblBadgeNumber;

    public static final String KEY_USER_UNIQ_ID = "userUniqId";
    public static final String KEY_PROJECT_UNIQ_ID = "projectUniqId";
    public static final String KEY_PROJECT_LOGO = "projectLogo";
    public static final String KEY_IS_MY_BADGE = "isMyBadge";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_CITY = "city";
    public static final String KEY_STATE = "state";
    public static final String KEY_ZIP = "zip";
    public static final String KEY_COUNTRY = "country";

    private String projectUniqId, projectLogo, userUniqId, projectAddress, projectCity, projectState, projectZip, projectCountry;
    private boolean isMyBadge;
    private Project project;
    private ForegroundTaskDelegate findBadgeDelegate;
    private Picasso picasso;
    private String txtpdfname, txtForPDF;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    int id = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout_log);
        final View view_data = getSupportActionBar().getCustomView();
        project = getPCApplication().getPcFunctionService().getInternalStoredProject();
        ImageButton imageButton_dwnload = view_data.findViewById(R.id.action_bar_dwnload);
        TextView txt_pageName = view_data.findViewById(R.id.txt_pageName);
        txt_pageName.setVisibility(View.VISIBLE);

        imageButton_dwnload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStoragePermissionGranted()) {
                    createandDisplayPdf(project);
                }
            }
        });
        userUniqId = getIntent().getStringExtra(KEY_USER_UNIQ_ID);
        projectUniqId = getIntent().getStringExtra(KEY_PROJECT_UNIQ_ID);
        projectAddress = getIntent().getStringExtra(KEY_ADDRESS);
        projectCity = getIntent().getStringExtra(KEY_CITY);
        projectState = getIntent().getStringExtra(KEY_STATE);
        projectZip = getIntent().getStringExtra(KEY_ZIP);
        projectCountry = getIntent().getStringExtra(KEY_COUNTRY);
        projectLogo = getIntent().getStringExtra(KEY_PROJECT_LOGO);
        isMyBadge = getIntent().getBooleanExtra(KEY_IS_MY_BADGE, false);

        if (savedInstanceState != null) {
            userUniqId = savedInstanceState.getString(KEY_USER_UNIQ_ID);
            projectUniqId = savedInstanceState.getString(KEY_PROJECT_UNIQ_ID);
            projectLogo = savedInstanceState.getString(KEY_PROJECT_LOGO);
            projectAddress = getIntent().getStringExtra(KEY_ADDRESS);
            projectCity = getIntent().getStringExtra(KEY_CITY);
            projectState = getIntent().getStringExtra(KEY_STATE);
            projectZip = getIntent().getStringExtra(KEY_ZIP);
            projectCountry = getIntent().getStringExtra(KEY_COUNTRY);
            isMyBadge = savedInstanceState.getBoolean(KEY_IS_MY_BADGE);
        }

        if (isMyBadge) {
            txt_pageName.setText(" My Badge ");

        } else {
            txt_pageName.setText(" Employee Badge ");

        }

        findBadgeDelegate = new FindBadgeDelegate(this);
        listOfForegroundTaskDelegates.add(findBadgeDelegate);

        picasso = getPCApplication().getPcFunctionService().getPicasso();

        getPCApplication().getPcFunctionService().findBadge(userUniqId, projectUniqId, findBadgeDelegate);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_USER_UNIQ_ID, userUniqId);
        outState.putString(KEY_PROJECT_UNIQ_ID, userUniqId);
        outState.putString(KEY_PROJECT_LOGO, projectLogo);
        outState.putBoolean(KEY_IS_MY_BADGE, isMyBadge);

        super.onSaveInstanceState(outState);
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

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Logger.v("PunchCard", "Permission is granted");
                return true;
            } else {


                android.support.v13.app.ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation

            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            createandDisplayPdf(project);
            //resume tasks needing this permission
        }
    }


    // Method for creating a pdf file from text, saving it then opening it for display
    public void createandDisplayPdf(Project project) {
        this.project = project;

        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = df.format(Calendar.getInstance().getTime());
        txtpdfname = project.getName() + "Badge" + date;

        Document doc = new Document(PageSize.A3, 10f, 20f, 10f, 40f);
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(2250, 1400, 1).create();


        try {


            @SuppressLint("SdCardPath") String path = "/sdcard/Documents/PunchCardPDF";
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            ScrollView root = (ScrollView) inflater.inflate(R.layout.activity_badge, null); //RelativeLayout is root view of my UI(xml) file.
            root.setDrawingCacheEnabled(true);
            Bitmap screen = getBitmapFromView(this.getWindow().findViewById(R.id.layout_root)); // here give id of our root layout (here its my RelativeLayout's id)

            File file = new File(dir, txtpdfname + ".pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            screen.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            addImage(doc, byteArray);


        } catch (DocumentException de) {
            com.orhanobut.logger.Logger.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            com.orhanobut.logger.Logger.e("PDFCreator", "ioException:" + e);
        }// finally {
        doc.close();
        //  }


        viewPdf(txtpdfname + ".pdf", "PunchCardPDF");
    }

    // Method for opening a pdf file
    private void viewPdf(String file, String directory) {
        @SuppressLint("SdCardPath") String path1 = "/sdcard/Documents/";
        File pdfFile = new File(path1 + directory + "/" + file);
        Uri path = Uri.fromFile(pdfFile);

        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle(txtpdfname + ".pdf")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.ic_download)
                .setPriority(Notification.PRIORITY_MAX);
        new Downloader(path, file).execute();

    }


    private static void addImage(Document document, byte[] byteArray) {
        Image image = null;
        try {
            image = Image.getInstance(byteArray);
        } catch (BadElementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        try {

            image.setAlignment(Image.MIDDLE);
            document.add(image);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static Bitmap getBitmapFromView(View view) {

        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

    private class Downloader extends AsyncTask<Void, Integer, Integer> {
        Uri path;
        String file;

        public Downloader(Uri path, String file) {
            this.path = path;
            this.file = file;
            com.orhanobut.logger.Logger.d(path);
            com.orhanobut.logger.Logger.d(file);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Displays the progress bar for the first time.
            mBuilder.setProgress(100, 0, false);
            Toast.makeText(BadgeActivity.this, file + " is Downloading!", Toast.LENGTH_SHORT).show();
            mNotifyManager.notify(id, mBuilder.build());
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // Update progress
            mBuilder.setProgress(100, values[0], false);
            mNotifyManager.notify(id, mBuilder.build());
            super.onProgressUpdate(values);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int i;
            for (i = 0; i <= 100; i += 20) {
                // Sets the progress indicator completion percentage
                publishProgress(Math.min(i, 100));
                try {
                    // Sleep for 5 seconds
                    Thread.sleep(1 * 1000);
                } catch (InterruptedException e) {
                    com.orhanobut.logger.Logger.d("TAG", "sleep failure");
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            // Setting the intent for pdf reader
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                Toast.makeText(BadgeActivity.this, file + " is located at /phone storage/Documents/PunchCardPDF", Toast.LENGTH_LONG).show();
            } catch (ActivityNotFoundException e) {
                Toast.makeText(BadgeActivity.this, "Can't read pdf file", Toast.LENGTH_SHORT).show();
            }
            @SuppressLint("WrongConstant") PendingIntent pendingIntent = PendingIntent.getActivity(
                    BadgeActivity.this,
                    0,
                    pdfIntent,
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            mBuilder.setContentText("Download complete");
            // Removes the progress bar
            mBuilder.setProgress(0, 0, false);
            mBuilder.setContentIntent(pendingIntent);

            mNotifyManager.notify(id, mBuilder.build());
        }
    }

    @Override
    protected void onDestroy() {

        picasso.cancelRequest(imgViewEmployee);

        picasso.cancelRequest(imgViewBadge);

        super.onDestroy();
    }

    private void load(Badge badge) {
        lblProjectName.setText(badge.getProject_name());
        lblCompanyName.setText(badge.getClient_name().trim());
        lblEmployeeName.setText(badge.getName());
        lblProjectAddress.setText(projectAddress + "\n" + projectCity + ", " + projectState + " " + projectZip);
        // lblProjectAddress.setText(project.getAddress()+"\n" + project.getCity() + ", " + project.getState() + " " + project.getZip());
        lblBadgeNumber.setText(badge.getBadge_id() + "");

        String baseUrl;
        SharedPreferences appPreferences = getPCApplication().getAppPreferences();
        String domain = appPreferences.getString(AppConstant.PREF_KEY_DOMAIN, "");
        if (TextUtils.isEmpty(domain)) {
            domain = AppConstant.DEFAULT_DOMAIN;
        }
        String companyHandle = appPreferences.getString(AppConstant.PREF_KEY_COMPANY_HANDLE, "");
        baseUrl = String.format("http://%s.%s%s", companyHandle, domain, projectLogo);


        picasso.load(badge.getAvatar_location())
                .placeholder(R.drawable.loader_image)
                .error(R.drawable.loader_image)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(imgViewEmployee);

        picasso.load(badge.getQr_location())
                .placeholder(R.drawable.loader_image)
                .error(R.drawable.loader_image)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .fit()
                .into(imgViewBadge);
    }

    private static class FindBadgeDelegate extends ForegroundTaskDelegate<Badge> {

        public FindBadgeDelegate(PCActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(Badge badge, Throwable throwable) {
            super.onPostExecute(badge, throwable);
            BadgeActivity activity = (BadgeActivity) activityWeakReference.get();
            if (activity != null && !activity.isDestroyed() && !activity.isFinishing()) {
                activity.load(badge);

            }
        }
    }
}
