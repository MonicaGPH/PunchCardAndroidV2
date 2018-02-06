package com.inverseapps.punchcard.adapters;


import android.annotation.SuppressLint;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.fragments.ProjectLogNotesFragment;
import com.inverseapps.punchcard.model.LogNote;
import com.inverseapps.punchcard.model.LogNotesAdapterListner;

import com.inverseapps.punchcard.utils.Utilities;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;



public class LogNoteAdapter extends RecyclerView.Adapter<LogNoteAdapter.LogNoteViewHolder> {

    public List<LogNote> listOfLogNotes;

    public ArrayList<LogNote> logNotesArrayList = new ArrayList<>();
    private LogNotesAdapterListner logNotesAdapterListner;
    private ProjectLogNotesFragment context;
    public int count = 0;
    private boolean isActionBar = false;
    private String projectName;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();


    public LogNoteAdapter(List<LogNote> dataSet, ProjectLogNotesFragment context, LogNotesAdapterListner listener, String projectName) {
        this.listOfLogNotes = dataSet;
        this.context = context;
        this.projectName = projectName;
        this.logNotesAdapterListner = listener;
    }

    @Override
    public LogNoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_log_note, parent, false);
        LogNoteViewHolder holder = new LogNoteViewHolder(view);



        return holder;
    }

    @Override
    public void onBindViewHolder(LogNoteViewHolder holder, int position) {
        final LogNote logNote = listOfLogNotes.get(position);
        if(position ==0){
            // sequence example

        }
        holder.load(logNote);
    }

    @Override
    public int getItemCount() {
        return listOfLogNotes.size();
    }

    class LogNoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @NonNull
        @BindView(R.id.lblTitle)
        TextView lblTitle;

        @NonNull
        @BindView(R.id.lblTimestamp)
        TextView lblTimestamp;

        @BindView(R.id.contentHolder)
        LinearLayout contentHolder;

        @NonNull
        @BindView(R.id.lblNote)
        TextView lblNote;

        @NonNull
        @BindView(R.id.inhorizontalscrollview)
        LinearLayout inhorizontalscrollview;


        public LogNoteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void load(LogNote logNote) {

            if (logNote.getType() != null) {
                if (logNote.getType().equals("punch-in exception")) {
                    lblTitle.setText("Check in Exception");
                } else if (logNote.getType().equals("punch out exception")) {
                    lblTitle.setText("Check out Exception");
                } else {
                    lblTitle.setText("General");
                }
            }
            if (logNote.getDateTime() != null) {
                lblTimestamp.setText(Utilities.shortStringDateTimeFromDate(logNote.getDateTime()));
            } else {
                lblTimestamp.setText("");
            }
            if (!TextUtils.isEmpty(logNote.getImage()) && logNote.getImage() != null) {
                inhorizontalscrollview.setVisibility(View.VISIBLE);
                addImageView(inhorizontalscrollview, logNote.getImage().toString());

            } else {
                inhorizontalscrollview.setVisibility(View.GONE);
            }
            lblNote.setText(logNote.getNote());
        }

        private void addImageView(LinearLayout layout, String pathImage) {
            ImageView imageView = new ImageView(context.getActivity());
            Picasso
                    .with(context.getActivity())
                    .load("file://" + pathImage)
                    .config(Bitmap.Config.RGB_565)
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.user_placeholder)
                    .into(imageView);

            int width = 150;
            int height = 150;

            LinearLayout.LayoutParams imParams = new LinearLayout.LayoutParams(width, height);
            imParams.setMargins(10, 10, 10, 0);
            layout.addView(imageView, imParams);
        }

        @SuppressLint("ResourceType")
        @Override
        public void onClick(final View view) {

            ((AppCompatActivity) context.getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            ((AppCompatActivity) context.getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(true);
            ((AppCompatActivity) context.getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) context.getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
            ((AppCompatActivity) context.getActivity()).getSupportActionBar().setCustomView(R.layout.action_bar_layout_log);
            final View view_data = ((AppCompatActivity) context.getActivity()).getSupportActionBar().getCustomView();

            ImageButton imageButton_dwnload = view_data.findViewById(R.id.action_bar_dwnload);
            TextView textView_textselected = view_data.findViewById(R.id.txt_numberSelected);


            imageButton_dwnload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (logNotesAdapterListner != null) {
                        logNotesAdapterListner.callcreateandDisplayPdf(logNotesArrayList, context.getActivity());
                        view_data.setVisibility(View.GONE);
                        view.setSelected(false);

                        contentHolder.setBackgroundColor(context.getResources().getColor(R.color.background_white));
                    }


                }
            });
            LogNote logNote = listOfLogNotes.get(getAdapterPosition());

            if (selectedItems.get(getAdapterPosition(), false)) {
                selectedItems.delete(getAdapterPosition());
                view.setSelected(false);
                --count;
                contentHolder.setBackgroundColor(context.getResources().getColor(R.color.background_white));
                if (count == 0) {
                    textView_textselected.setVisibility(View.GONE);
                    view_data.setVisibility(View.GONE);
                    ((AppCompatActivity) context.getActivity()).getSupportActionBar().setTitle(projectName);
                }
                textView_textselected.setText(count + "\r  Selected");


                List<LogNote> found = new ArrayList<>();
                for (LogNote logNote1 : logNotesArrayList) {
                    if (logNote1.getNote().equals(logNote.getNote())) {
                        found.add(logNote1);
                    }
                }
                logNotesArrayList.removeAll(found);

            } else {

                count++;

                textView_textselected.setText(count + "\r  Selected");
                contentHolder.setBackgroundColor(context.getResources().getColor(R.color.background_grey));
                selectedItems.put(getAdapterPosition(), true);
                textView_textselected.setVisibility(View.VISIBLE);
                logNotesArrayList.add(new LogNote(lblTitle.getText().toString(), logNote.getNote(), lblTimestamp.getText().toString(), logNote.getProject().getName()));
                view.setSelected(true);


            }
        }
    }


}

