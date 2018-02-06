package com.inverseapps.punchcard.ui;

import android.Manifest;
import android.annotation.SuppressLint;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.Nullable;

import android.support.v13.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v4.app.NotificationCompat.Builder;

import com.inverseapps.punchcard.R;

import com.inverseapps.punchcard.utils.Utilities;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.orhanobut.logger.Logger;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by asus on 11-Dec-17.
 */

public class PastBillingExtendActivity extends PCActivity {
    public static final String KEY_PAST_BILLING_fromdate = "pastbilling_from_date";
    public static final String KEY_PAST_BILLING_todate = "pastbilling_to_date";
    public static final String KEY_PAST_BILLING_punches = "pastbilling_punches";
    public static final String KEY_TOTAL_AMOUNT = "totalamount";
    private String total_amount, total_punches, frm_date, to_date, txtForPDF, txtpdfname;
    private NotificationManager mNotifyManager;
    private Builder mBuilder;
    int id = 1;

    @Override
    protected int getRootLayoutRes() {
        return R.layout.item_past_billing_data;
    }

    @Nullable
    @BindView(R.id.txt_total_punches)
    TextView txt_total_punches;

    @Nullable
    @BindView(R.id.txt_total_amount)
    TextView txt_total_amount;

    @Nullable
    @BindView(R.id.txt_fromdate)
    TextView txt_fromdate;
    @Nullable
    @BindView(R.id.txt_todate)
    TextView txt_todate;

    @Nullable
    @BindView(R.id.btnDownloadStatement)
    Button btnDownloadStatement;

    @OnClick(R.id.btnDownloadStatement)
    public void onClickedButtonDownloadStatement() {
        downloadStatement();
    }

    private void downloadStatement() {
        if (isStoragePermissionGranted()) {
            createandDisplayPdf(txtForPDF);
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {

            total_punches = intent.getStringExtra(KEY_PAST_BILLING_punches);
            total_amount = intent.getStringExtra(KEY_TOTAL_AMOUNT);
            Date frmdate = (Date) intent.getSerializableExtra(KEY_PAST_BILLING_fromdate);
            Date todate = (Date) intent.getSerializableExtra(KEY_PAST_BILLING_todate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            txtpdfname = sdf.format(frmdate);
            frm_date = Utilities.monthStringDateFromDate(frmdate);
            to_date = Utilities.monthStringDateFromDate(todate);


            txt_total_punches.setText(total_punches);
            txt_total_amount.setText(total_amount);
            txt_fromdate.setText(frm_date);
            txt_todate.setText(to_date);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(frm_date);

            txtForPDF = "\n\n\nTotal Number of Unique Punches\n\n" +
                    total_punches + "\n\n" +
                    "Total amount\n\n" +
                    "$ " + total_amount + "\n\n" +
                    "Billing Dates\n\n" +
                    "From:" + frm_date + "\n\n" +
                    "To:" + to_date;

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

    // Method for creating a pdf file from text, saving it then opening it for display
    public void createandDisplayPdf(String text) {

        Document doc = new Document();

        try {
            // String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "Documents//PunchCardPDF";

            @SuppressLint("SdCardPath") String path = "/sdcard/Documents/PunchCardPDF";
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            File file = new File(dir, "STATEMENT" + txtpdfname + ".pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();

            Paragraph p1 = new Paragraph(text);
            Font paraFont = new Font(Font.FontFamily.COURIER);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.setFont(paraFont);

            //add paragraph to document
            doc.add(p1);

        } catch (DocumentException de) {
            Logger.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Logger.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }


        viewPdf("STATEMENT" + txtpdfname + ".pdf", "PunchCardPDF");
    }

    // Method for opening a pdf file
    private void viewPdf(String file, String directory) {
        @SuppressLint("SdCardPath") String path1 = "/sdcard/Documents/";
        File pdfFile = new File(path1+ directory + "/" + file);
        Uri path = Uri.fromFile(pdfFile);

        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(PastBillingExtendActivity.this);
        mBuilder.setContentTitle("STATEMENT" + txtpdfname + ".pdf")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.ic_download);

        new Downloader(path, file).execute();

    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Logger.v("PunchCard", "Permission is granted");
                return true;
            } else {


                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
            createandDisplayPdf(txtForPDF);
            //resume tasks needing this permission
        }
    }

    private class Downloader extends AsyncTask<Void, Integer, Integer> {
        Uri path;
        String file;

        public Downloader(Uri path, String file) {
            this.path = path;
            this.file= file;
            Logger.d(path);
            Logger.d(file);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Displays the progress bar for the first time.
            mBuilder.setProgress(100, 0, false);
            Toast.makeText(PastBillingExtendActivity.this, file + " is Downloading!", Toast.LENGTH_SHORT).show();
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
                    Logger.d("TAG", "sleep failure");
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
                Toast.makeText(PastBillingExtendActivity.this, file + " is located at /phone storage/Documents/PunchCardPDF", Toast.LENGTH_LONG).show();
            } catch (ActivityNotFoundException e) {
                Toast.makeText(PastBillingExtendActivity.this, "Can't read pdf file", Toast.LENGTH_SHORT).show();
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    PastBillingExtendActivity.this,
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
}
