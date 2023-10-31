package com.example.studentdocumentsharing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.studentdocumentsharing.Image.UploadImage;
import com.example.studentdocumentsharing.Notice.DeleteNoticeActivity;
import com.example.studentdocumentsharing.Notice.UploadNotice;
import com.example.studentdocumentsharing.faculty.UpdateFaculty;
import com.example.studentdocumentsharing.pdf.UploadPdfAcitivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    CardView uploadNotice, addGalleryImage, addEbook, faculty, deleteNotice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uploadNotice = findViewById(R.id.addNotice);
        addGalleryImage = findViewById(R.id.addGalleryImage);
        addEbook = findViewById(R.id.addEbook);
        faculty = findViewById(R.id.faculty);
        deleteNotice = findViewById(R.id.deleteNotice);

        uploadNotice.setOnClickListener(this);
        addGalleryImage.setOnClickListener(this);
        addEbook.setOnClickListener(this);
        faculty.setOnClickListener(this);
        deleteNotice.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.addNotice) {
            Intent intent = new Intent(MainActivity.this, UploadNotice.class);
            startActivity(intent);
        } else if(view.getId() == R.id.addGalleryImage) {
            Intent intent = new Intent(MainActivity.this, UploadImage.class);
            startActivity(intent);
        } else if(view.getId() == R.id.addEbook) {
            Intent intent = new Intent(MainActivity.this, UploadPdfAcitivity.class);
            startActivity(intent);
        } else if(view.getId() == R.id.faculty) {
            Intent intent = new Intent(MainActivity.this, UpdateFaculty.class);
            startActivity(intent);
        } else if(view.getId() == R.id.deleteNotice) {
            Intent intent = new Intent(MainActivity.this, DeleteNoticeActivity.class);
            startActivity(intent);
        }
    }
}