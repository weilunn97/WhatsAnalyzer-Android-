package com.jed.whatsapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UploadedCloudFileAdapter extends RecyclerView.Adapter<UploadedCloudFileAdapter.ViewHolder> {

    public static final String TAG = "UploadedCloudFileAdapter";

    List<UploadedCloudFile> UploadedCloudFileList;
    Context context;

    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference(currentUser.getDisplayName());

    public UploadedCloudFileAdapter(List<UploadedCloudFile> UploadedCloudFileList) {
        this.UploadedCloudFileList = UploadedCloudFileList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        UploadedCloudFile UploadedCloudFile = UploadedCloudFileList.get(position);

        // WE STRIP OFF THE DATETIME STAMP FROM THE FILENAME FOR BETTER PRESENTATION
        String truncatedFileName = UploadedCloudFile.getFileName().substring(0,
                UploadedCloudFile.getFileName().length() - 28);
        Calendar c = Calendar.getInstance();
        c.setTime(UploadedCloudFile.getLastModified());
        c.add(Calendar.HOUR_OF_DAY, 8);
        Date localDate = c.getTime();
        String truncatedDate = localDate.toString().substring(0, localDate.toString().length() - 9);
        holder.fileName.setText(truncatedFileName);
        holder.fileDate.setText(truncatedDate);
    }

    @Override
    public int getItemCount() {
        return UploadedCloudFileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView fileDate;
        TextView fileName;
        Button selectButton;
        CardView cv;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            fileDate = itemView.findViewById(R.id.fileDate);
            fileName = itemView.findViewById(R.id.fileName);
            cv = itemView.findViewById(R.id.cv);
            selectButton = itemView.findViewById(R.id.selectButton);
            selectButton.setOnClickListener(this);
        }

        /*
        TODO
        1. Based on filename, download file object
        2. Place activeFile in FileProcessing

         */
        @Override
        public void onClick(View v) {
            int position = this.getPosition();
            Log.d(TAG, "Position : " + position);
            Log.d(TAG, "FileName : " + UploadedCloudFileList.get(position).getFileName());


            System.out.println(fileName.getText().toString() + " CHOSEN");
            // TODO : FIND OUT HOW TO GET URI FROM FIREBASE AND  PROCESS THE FILE
            // TODO : READING FILE DIRECTLY FROM FB CLOUD
            StorageReference chosenFileRef =
                    mStorageRef.child(UploadedCloudFileList.get(position).getFileName());
            chosenFileRef.getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        FileProcessing.setUploadedFileURI(uri);
                        FileProcessing.setInitialized(false);
                    })
                    .addOnFailureListener(e -> Log.d(TAG,
                            mStorageRef.child("DOWNLOAD FAILURE + " + UploadedCloudFileList.get(position).getFileName()).toString()));
        }
    }
}