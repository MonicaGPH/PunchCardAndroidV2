package com.inverseapps.punchcard.ui;

import android.Manifest;
import android.app.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.inverseapps.punchcard.Constant.AppConstant;
import com.inverseapps.punchcard.R;

import com.inverseapps.punchcard.model.Face;
import com.inverseapps.punchcard.model.Project;
import com.inverseapps.punchcard.model.User;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Locale;

import butterknife.BindView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


import android.content.ComponentName;

import android.content.pm.ResolveInfo;

import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by asus on 06-Sep-17.
 */

public class ImageCaptureActivity extends PCLocationActivity {

    private GoogleApiClient client;

    @Override
    protected int getRootLayoutRes() {
        return R.layout.activity_camera_open;
    }

    @BindView(R.id.layoutFirst)
    LinearLayout mLinearLayout;

    private User user;
    private SharedPreferences appPreferences;
    private ForegroundTaskDelegate checkOutDelegate;
    private ForegroundTaskDelegate checkInDelegate;
    public static final String KEY_PROJECT = "project";
    private Project project;
    private boolean isImage;
    private String convertedBitmapTOString;
    private FindFaceDelegate findFaceDelegate;
    // Storage Permissions
    private static final int REQUEST_PERMISSION = 1;
    private static String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final String KEY_RESPONSE_USER_UNIQUE_ID = "useruniqueid";
    private Uri fileUri; // file url to store image
    // Permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        project = getIntent().getParcelableExtra(KEY_PROJECT);
        if (savedInstanceState != null) {
            project = savedInstanceState.getParcelable(KEY_PROJECT);

        }
        user = getPCApplication().getPcFunctionService().getInternalStoredUser();
        project = getPCApplication().getPcFunctionService().getInternalStoredProject();
        findFaceDelegate = new FindFaceDelegate(this);
        listOfForegroundTaskDelegates.add(findFaceDelegate);
        askLoCameraPermission();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {


        super.onSaveInstanceState(outState);
    }

    /**
     * Create a chooser intent to select the source to get image from.<br/>
     * The source can be camera's (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br/>
     * All possible sources are added to the intent chooser.
     */
    public Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        fileUri = getOutputMediaFileUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (fileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.inverseapps.punchcard.ui.ImageCaptureActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    /**
     * Get URI to image received from capture by camera.
     */
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    /**
     * Receiving activity result method will be called after closing the camera
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        Bitmap myBitmap;
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // successfully captured the image
                // launching upload activity

                try {
                    UploadFile(fileUri);
                } catch (IOException e) {
                    e.printStackTrace();
                    Logger.d("Error in Uploading");
                }

            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
                finish();

            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }

        }

    }


    /**
     * Launching camera app to capture image
     */

    private void captureImage() {

        startActivityForResult(getPickImageChooserIntent(), CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

    }


/**
 * ------------ Helper Methods ----------------------
 * *//*


    */

    /**
     * Creating file uri to store image/video
     */

    public Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }


    /**
     * returning image / video
     */

    private static File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                AppConstant.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Logger.d("Camera", "Oops! Failed create "
                        + AppConstant.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd",
                Locale.getDefault()).format(new Date());
        File mediaFile;


        //mediaFile = new File("IMG_" + timeStamp + ".jpg");

        //"IMG_" + timeStamp + ".jpg"
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".png");


        return mediaFile;
    }

    public void askLoCameraPermission() {
        if (!hasPermission(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION);
        } else {
            captureImage();
        }
    }

    public static boolean hasPermission(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }

            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                    //your logic her
                    captureImage();
                } else {
                    //permission denied

                    Toast.makeText(this, "You Have Denied permissions.", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }


    public void UploadFile(Uri fileUri) throws IOException {


        this.fileUri = fileUri;

        File sourceFile = new File(fileUri.getPath());
        File compressedImageFile = new Compressor(this).compressToFile(sourceFile);
        Logger.d(fileUri.getPath());


        RequestBody mFile = RequestBody.create(MediaType.parse("image*//*"), compressedImageFile);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("selfie", sourceFile.getName(), mFile);
        RequestBody sub_domain = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(user.getSubdomain()));
        RequestBody uid = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(user.getId()));
        RequestBody pro_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(project.getUniq_id()));

        pcFunctionService.findFace(fileToUpload, sub_domain, uid, pro_id, findFaceDelegate);

    }


    private static class FindFaceDelegate extends ForegroundTaskDelegate<Face> {


        public FindFaceDelegate(ImageCaptureActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(Face face, Throwable throwable) {
            super.onPostExecute(face, throwable);

            ImageCaptureActivity activity = (ImageCaptureActivity) activityWeakReference.get();
            if (throwable == null && face != null &&
                    activity != null && !activity.isFinishing() && !activity.isDestroyed()) {

                if (TextUtils.isEmpty(face.getUniqId()) || face.getUniqId() == null) {

                    if (face.getMessage().equalsIgnoreCase("Image must contains face") || face.getMessage().equalsIgnoreCase("Match not found")) {
                        activity.showAlertBox(face.getMessage());

                    }
                } else {
                    String uniq_id_user = face.getUniqId();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(KEY_RESPONSE_USER_UNIQUE_ID, uniq_id_user);
                    activity.setResult(Activity.RESULT_OK, resultIntent);
                    activity.finish();
                }
            }
        }
    }

    public void showAlertBox(String message) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);


        alertDialog.setTitle("Punch Card");
        alertDialog.setMessage(message);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // User pressed YES button.
                dialog.dismiss();
                finish();

            }
        });


        // Showing Alert Message
        alertDialog.show();


    }
}
