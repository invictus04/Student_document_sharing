package com.example.studentdocumentsharing.pdf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentdocumentsharing.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;

public class UploadPdfAcitivity extends AppCompatActivity {
    private CardView addPDF;
    private final int REQ = 1;
    private Uri pdfData;
    private EditText PDFTitle;
    private Button uploadPDFButton;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    String downloadUrl = "";
    private ProgressDialog pd;
    private TextView pdfTextView;
    private String pdfName, title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf_acitivity);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        pd = new ProgressDialog(this);
        addPDF = findViewById(R.id.addPDF);
        PDFTitle = findViewById(R.id.PDFTitle);
        uploadPDFButton = findViewById(R.id.uploadPDFButton);
        pdfTextView = findViewById(R.id.pdfTextView);


        addPDF.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {openGallery();}
        });
        uploadPDFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title=PDFTitle.getText().toString();
                if(title.isEmpty()) {
                    PDFTitle.setError("Empty");
                    PDFTitle.requestFocus();
                } else if(pdfData == null) {
                    Toast.makeText(UploadPdfAcitivity.this, "Please Upload pdf", Toast.LENGTH_SHORT).show();
                } else {
                    uploadPDF();
                }
            }
        });
    }

    private void uploadPDF() {
        pd.setTitle("Please wait......");
        pd.setMessage("Uploading");
        pd.show();
        StorageReference reference = storageReference.child("pdf/" + pdfName + "-" + System.currentTimeMillis()+ ".pdf");
        reference.putFile(pdfData)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task <Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri uri = uriTask.getResult();
                        uploadData(String.valueOf(uri));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(UploadPdfAcitivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadData(String downloadUrl) {
        String uniqueKey=databaseReference.child("pdf").push().getKey();
        HashMap data = new HashMap();
        data.put("pdfTitle", title);
        data.put("pdfUrl",downloadUrl);

        databaseReference.child("pdf").child(uniqueKey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(UploadPdfAcitivity.this, "Pdf Uploaded Succesfully", Toast.LENGTH_SHORT).show();
                PDFTitle.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadPdfAcitivity.this, "Failed to upload pdf", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF File"), REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            pdfData = data.getData();
            if (pdfData.toString().startsWith("content://")) {
                Cursor cursor1 = null;
                try {
                    cursor1 = UploadPdfAcitivity.this.getContentResolver().query(pdfData, null, null, null, null);
                    if (cursor1 != null && cursor1.moveToFirst()) {
//                        pdfName = cursor1.getString(cursor1.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        int index = cursor1.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        pdfName = cursor1.getString(index);
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }

                } else if (pdfData.toString().startsWith("file://")) {
                    pdfName = new File(pdfData.toString()).getName();
                }
                pdfTextView.setText(pdfName);
            }
        }
}
