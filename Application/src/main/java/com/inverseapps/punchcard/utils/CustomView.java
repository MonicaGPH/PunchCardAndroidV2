package com.inverseapps.punchcard.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.inverseapps.punchcard.R;
import com.squareup.picasso.Picasso;

/**
 * Created by asus on 01-Feb-18.
 */

@SuppressLint("ViewConstructor")
public class CustomView extends FrameLayout {

    private View mRoot;
    private ImageView mImgPhoto;
    private ImageButton mBtnClose;

    private Context mContext;

    public CustomView(final Context context, String pathImage) {
        this(context, pathImage, null);
    }

    public CustomView(final Context context, String pathImage, final AttributeSet attrs) {
        this(context, pathImage, attrs, 0);
    }

    public CustomView(final Context context, String pathImage, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, pathImage);
    }

    private void init(final Context context, String pathImage) {
        if (isInEditMode())
            return;

        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View customView = null;

        if (inflater != null)
            customView = inflater.inflate(R.layout.item_doubleimage, this);

        if (customView == null)
            return;

        mRoot = customView.findViewById(R.id.root);
        mImgPhoto = customView.findViewById(R.id.img_photo);
        mBtnClose =  customView.findViewById(R.id.btn_close);

        Picasso
                .with(context)
                .load("file://" + pathImage)
                .config(Bitmap.Config.RGB_565)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.user_placeholder)
                .into(mImgPhoto);

        mBtnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getRoot().setVisibility(GONE);
            }
        });
    }

    public View getRoot() {
        return mRoot;
    }

    public ImageView getImgPhoto() {
        return mImgPhoto;
    }

    public View getBtnClose() {
        return mBtnClose;
    }
}
