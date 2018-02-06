package com.inverseapps.punchcard.fragments;

import android.Manifest;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.app.ActivityCompat;

import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.TextUtils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.adapters.LogNoteAdapter;
import com.inverseapps.punchcard.model.LogNote;

import com.inverseapps.punchcard.model.LogNotesAdapterListner;
import com.inverseapps.punchcard.model.Project;
import com.inverseapps.punchcard.model.User;
import com.inverseapps.punchcard.model.param.CreateLogNoteParam;
import com.inverseapps.punchcard.ui.ForegroundTaskDelegate;

import com.inverseapps.punchcard.utils.CustomView;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.orhanobut.logger.Logger;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class ProjectLogNotesFragment extends PCProjectDetailsFragment implements SwipeRefreshLayout.OnRefreshListener,
        LogNotesAdapterListner {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.fragment_project_log_notes;
    }

    private List<LogNote> listOfLogNotes;

    @NonNull
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @NonNull
    @BindView(R.id.linearLayout2)
    LinearLayout linearLayout2;

    @NonNull
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @NonNull
    @BindView(R.id.commentHolder)
    ViewGroup commentHolder;

    @NonNull
    @BindView(R.id.txtNote)
    TextView txtNote;

    @NonNull
    @BindView(R.id.btnPost)
    Button btnPost;

    @NonNull
    @BindView(R.id.imageView_clip)
    ImageView imageView_clip;

    @NonNull
    @BindView(R.id.inhorizontalscrollview)
    LinearLayout inhorizontalscrollview;

    @OnClick(R.id.imageView_clip)
    void onClickedimageView_clip() {
        EnableRuntimePermission();
    }

    private void openCustomGallery() {
        FishBun.with(ProjectLogNotesFragment.this)
                .MultiPageMode()
                .setIsUseDetailView(false)
                .setMinCount(0)
                .setCamera(true)
                .setMaxCount(20)
                .setPickerSpanCount(2)
                .setActionBarColor(getActivity().getResources().getColor(R.color.black), getActivity().getResources().getColor(R.color.black), false)
                .setActionBarTitleColor(getActivity().getResources().getColor(R.color.white))
                .setArrayPaths(path)
                .setAlbumSpanCount(1, 2)
                .setButtonInAlbumActivity(false)
                .exceptGif(true)
                .setReachLimitAutomaticClose(false)
                .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_back_arrow))
                .setOkButtonDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_stat_name))
                .setAllViewTitle("All Photos")
                .setRequestCode(2)
                .setActionBarTitle("Select Pictures ")
                .textOnImagesSelectionLimitReached("Limit Reached!")
                .textOnNothingSelected("Nothing Selected")
                .startAlbum();
    }

    @OnClick(R.id.btnPost)
    void onClickedPostButton() {
        checkToCreateLogNote();
    }

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    int id = 1;
    private ForegroundTaskDelegate findLogNotesDelegate;
    private ForegroundTaskDelegate createNewLogNotesDelegate;
    private User user;
    public ArrayList<LogNote> logNotesArrayList = new ArrayList<>();
    private String txtpdfname, txtForPDF;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private ArrayList<Uri> path = new ArrayList<>();
    private ArrayList<String> imagePathArraylist = new ArrayList<>();
    private final int RequestPermissionCodeStorage = 11;
    private final int RequestPermissionExternalStorage = 100;

    public void EnableRuntimePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    ) {
                requestPermissions(
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        RequestPermissionCodeStorage);
            } else {
                openCustomGallery();
            }
        } else {
            //    sdk<23
            openCustomGallery();
        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        switch (requestCode) {
            case 2:

                // path = imageData.getStringArrayListExtra(Define.INTENT_PATH);
                // you can get an image path(ArrayList<String>) on <0.6.2
                if (data != null) {

                    path = data.getParcelableArrayListExtra(Define.INTENT_PATH);

                    for (int i = 0; i < path.size(); i++) {
                        Logger.d("Image URI", "" + path.get(i).getPath());

                        String pathImage = getRealPathFromUri(getActivity(), Uri.parse("content://media" + path.get(i).getPath()));
                        imagePathArraylist.add(i, pathImage);
                        addImageView(inhorizontalscrollview, pathImage);
                    }
                    path.clear();
                } else {
                    Toast.makeText(getActivity(), "No Photo is selected ", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }


    private void addImageView(LinearLayout layout, String pathImage) {
        final CustomView customView1 = new CustomView(getActivity(), pathImage);
        layout.addView(customView1);
    }


    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static ProjectLogNotesFragment newInstance(int page,
                                                      String title,
                                                      Project project,
                                                      String checkedInProjectUniqId,
                                                      String checkedInProjectName,
                                                      boolean userInProject) {
        ProjectLogNotesFragment fragment = new ProjectLogNotesFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_PAGE, page);
        args.putString(KEY_TITLE, title);
        args.putParcelable(KEY_PROJECT, project);
        args.putString(KEY_CHECKED_IN_PROJECT_UNIQ_ID, checkedInProjectUniqId);
        args.putString(KEY_CHECKED_IN_PROJECT_NAME, checkedInProjectName);
        args.putBoolean(KEY_USER_IN_PROJECT, userInProject);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {


        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findLogNotesDelegate = new FindLogNotesDelegate(this);
        listOfForegroundTaskDelegates.add(findLogNotesDelegate);

        createNewLogNotesDelegate = new CreateNewLogNoteDelegate(this);
        listOfForegroundTaskDelegates.add(createNewLogNotesDelegate);

        user = pcActivity.getPCApplication().getPcFunctionService().getInternalStoredUser();
        imageView_clip.setVisibility(View.GONE);
        if (user.getRole().equalsIgnoreCase("clientadmin")) {
            linearLayout2.setVisibility(View.VISIBLE);
        } else {
            linearLayout2.setVisibility(View.GONE);
        }


        listOfLogNotes = new Vector<>();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new LogNoteAdapter(listOfLogNotes, ProjectLogNotesFragment.this, this, project.getName());
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeResources(R.color.logo_orange);
        swipeRefreshLayout.setOnRefreshListener(this);


    }


    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        findLogNotes();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (project.getUniq_id().equals(checkedInProjectUniqId)) {
            commentHolder.setVisibility(View.VISIBLE);
            txtNote.requestFocus();
        } else {
            commentHolder.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
        findLogNotes();
    }

    @Override
    public void reload(Project project, String checkedInProjectUniqId) {
        super.reload(project, checkedInProjectUniqId);

        if (project.getUniq_id().equals(checkedInProjectUniqId)) {
            commentHolder.setVisibility(View.VISIBLE);
            txtNote.requestFocus();
        } else {
            commentHolder.setVisibility(View.GONE);
        }

        findLogNotes();

    }

    private void checkToCreateLogNote() {
        if (TextUtils.isEmpty(txtNote.getText().toString())) {
            pcActivity.showAlertDialog("Please make sure you have provided a note!");
        } else {


            MultipartBody.Part[] logNotesImagesParts = new MultipartBody.Part[imagePathArraylist.size()];
            for (int index = 0; index < imagePathArraylist.size(); index++) {

                File sourceFile = new File(imagePathArraylist.get(index));
                File compressedImageFile = null;
                try {
                    compressedImageFile = new Compressor(getActivity()).compressToFile(sourceFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), compressedImageFile);
                logNotesImagesParts[index] = MultipartBody.Part.createFormData("files", sourceFile.getName(), surveyBody);
            }


            RequestBody notes = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(txtNote.getText().toString()));
            RequestBody uniq_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(project.getUniq_id()));
            CreateLogNoteParam param = new CreateLogNoteParam(txtNote.getText().toString(),
                    project.getUniq_id());
           createLogNote(param);
          //  createLogNote(logNotesImagesParts, notes, uniq_id);
        }
    }

    private void findLogNotes() {
        pcActivity.getPCApplication()
                .getPcFunctionService()
                .findLogNotes(project.getUniq_id(),
                        findLogNotesDelegate);
    }

    private void createLogNote(MultipartBody.Part[] logNotesImagesParts, RequestBody notes, RequestBody uniq_id) {
        pcActivity.getPCApplication()
                .getPcFunctionService()
                .uploadLogesTextANDImages(logNotesImagesParts, notes, uniq_id,
                        createNewLogNotesDelegate);
    }

    private void createLogNote(CreateLogNoteParam param) {
        pcActivity.getPCApplication()
                .getPcFunctionService()
                .createNewLogNote(param,
                        createNewLogNotesDelegate);
    }

    @Override
    public void callcreateandDisplayPdf(ArrayList<LogNote> logNotesArrayList, Context context) {

        this.logNotesArrayList = logNotesArrayList;
        if (Build.VERSION.SDK_INT >= 23) {
            isStoragePermissionGranted(getActivity());
        } else {
            createandDisplayPdf(user, logNotesArrayList);
        }
    }


    private static class FindLogNotesDelegate extends ForegroundTaskDelegate<List<LogNote>> {

        private final WeakReference<ProjectLogNotesFragment> fragmentWeakReference;

        public FindLogNotesDelegate(ProjectLogNotesFragment fragment) {
            super(fragment.pcActivity);
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void onPostExecute(List<LogNote> logNotes, Throwable throwable) {
            super.onPostExecute(logNotes, throwable);

            ProjectLogNotesFragment fragment = fragmentWeakReference.get();
            if (throwable == null &&
                    fragment != null && !fragment.isDetached() && !fragment.isRemoving()) {
                fragment.listOfLogNotes.clear();
                fragment.listOfLogNotes.addAll(logNotes);

                Collections.sort(fragment.listOfLogNotes, new Comparator<LogNote>() {
                    @Override
                    public int compare(LogNote lhs, LogNote rhs) {
                        return rhs.getDateTime().compareTo(lhs.getDateTime());
                    }

                });
                fragment.adapter.notifyDataSetChanged();
                fragment.swipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    private static class CreateNewLogNoteDelegate extends ForegroundTaskDelegate<Boolean> {

        private final WeakReference<ProjectLogNotesFragment> fragmentWeakReference;

        public CreateNewLogNoteDelegate(ProjectLogNotesFragment fragment) {
            super(fragment.pcActivity);
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void onPostExecute(Boolean aBoolean, Throwable throwable) {
            super.onPostExecute(aBoolean, throwable);

            ProjectLogNotesFragment fragment = fragmentWeakReference.get();
            if (throwable == null &&
                    fragment != null && !fragment.isDetached() && !fragment.isRemoving()) {
                fragment.txtNote.setText("");
                fragment.findLogNotes();
            }
        }
    }


    public void isStoragePermissionGranted(Context context) {


        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // No explanation needed, we can request the permission.
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    RequestPermissionExternalStorage);

        } else {
            createandDisplayPdf(user, logNotesArrayList);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case RequestPermissionCodeStorage:

                if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    openCustomGallery();

                } else {

                    Toast.makeText(getActivity(), "You denied the permission", Toast.LENGTH_LONG).show();

                }
                break;

            case RequestPermissionExternalStorage:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createandDisplayPdf(user, logNotesArrayList);
                    //resume tasks needing this permission
                }
                break;
        }


    }

    // Method for creating a pdf file from text, saving it then opening it for display
    public void createandDisplayPdf(User user, ArrayList<LogNote> logNotesArrayList) {
        this.user = user;

        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = df.format(Calendar.getInstance().getTime());
        txtpdfname = user.getFirst_name() + "logNotes" + date;

        Document doc = new Document();

        try {


            @SuppressLint("SdCardPath") String path = "/sdcard/Documents/PunchCardPDF";
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            File file = new File(dir, txtpdfname + ".pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();
            PdfPTable table = new PdfPTable(1);
            for (int aw = 0; aw < logNotesArrayList.size(); aw++) {
                String txtForPDF = "\n\nTitle :  " + logNotesArrayList.get(aw).getTitle() + "\n\nLogNotes : " +
                        logNotesArrayList.get(aw).getNote() + "              Date :  " +
                        logNotesArrayList.get(aw).getTimestamp() + "\n\nProject Name :    " + logNotesArrayList.get(aw).getProjectName() + "\n\n";

                table.addCell(txtForPDF);
                // if(logNotesArrayList.get(aw).getImage()! = null && logNotesArrayList.get(aw).getImage() != " ")
                // {
                    /*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String imageUrl = "https://s.yimg.com/os/mit/ape/w/d8f6e02/dark/partly_cloudy_day.png";
                    Image imageFromWeb = Image.getInstance(new URL(imageUrl));
                    Paragraph p = new Paragraph();
                    imageFromWeb.scalePercent(30);
                    PdfPCell cell = new PdfPCell();
                    cell.addElement(imageFromWeb);
                    cell.addElement(imageFromWeb);
                    table.addCell(cell);*/

                //    }

            }

            doc.add(table);


        } catch (DocumentException de) {
            com.orhanobut.logger.Logger.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            com.orhanobut.logger.Logger.e("PDFCreator", "ioException:" + e);
        }// finally {
        doc.close();
        //  }
        logNotesArrayList.clear();

        viewPdf(txtpdfname + ".pdf", "PunchCardPDF");
    }

    // Method for opening a pdf file
    private void viewPdf(String file, String directory) {
        @SuppressLint("SdCardPath") String path1 = "/sdcard/Documents/";
        File pdfFile = new File(path1 + directory + "/" + file);
        Uri path = Uri.fromFile(pdfFile);

        mNotifyManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(getActivity());
        mBuilder.setContentTitle(txtpdfname + ".pdf")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.ic_download);

        new Downloader(path, file).execute();

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
            Toast.makeText(getActivity(), file + " is Downloading!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), file + " is located at /phone storage/Documents/PunchCardPDF", Toast.LENGTH_LONG).show();
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getActivity(), "Can't read pdf file", Toast.LENGTH_SHORT).show();
            }
            @SuppressLint("WrongConstant") PendingIntent pendingIntent = PendingIntent.getActivity(
                    getActivity(),
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
