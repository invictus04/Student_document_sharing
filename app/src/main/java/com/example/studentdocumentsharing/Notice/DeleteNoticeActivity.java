package com.example.studentdocumentsharing.Notice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleCoroutineScope;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.studentdocumentsharing.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeleteNoticeActivity extends AppCompatActivity {

    private RecyclerView DeleteNoticeRecyclier;
    private ProgressBar ProgressBar;
    private ArrayList<NoticeData> list;
    private NoticeAdapter adapter;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_notice);

        DeleteNoticeRecyclier = findViewById(R.id.DeleteNoticeRecyclier);
        ProgressBar = findViewById(R.id.ProgressBar);

        reference =  FirebaseDatabase.getInstance().getReference().child("Notice");

        DeleteNoticeRecyclier.setLayoutManager(new LinearLayoutManager(this));
        DeleteNoticeRecyclier.setHasFixedSize(true);

        getNotice();
    }

    private void getNotice() {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list = new ArrayList<>();
                    for(DataSnapshot s: snapshot.getChildren()) {
                        NoticeData data = s.getValue(NoticeData.class);
                        list.add(data);
                    }
                    adapter = new NoticeAdapter(DeleteNoticeActivity.this, list);
                    adapter.notifyDataSetChanged();
                    ProgressBar.setVisibility(View.GONE);
                    DeleteNoticeRecyclier.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    ProgressBar.setVisibility(View.GONE);
                    Toast.makeText(DeleteNoticeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }
}