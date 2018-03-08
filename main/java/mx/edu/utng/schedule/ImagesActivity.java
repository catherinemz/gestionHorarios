package mx.edu.utng.schedule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity implements ImageAdapter.OnItemClickListener{

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private ProgressBar pgbCircle;

    private FirebaseStorage storage;

    private DatabaseReference databaseReference;
    private ValueEventListener dbListener;
    private List<Upload> uploadList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        recyclerView = findViewById(R.id.rcv_schedule);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        pgbCircle = findViewById(R.id.pgb_circle);

        uploadList = new ArrayList<>();

        imageAdapter = new ImageAdapter(ImagesActivity.this, uploadList);
        recyclerView.setAdapter(imageAdapter);

        imageAdapter.setOnItemClickListener(ImagesActivity.this);

        storage = FirebaseStorage.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        dbListener= databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                uploadList.clear();

                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    uploadList.add(upload);
                }

                imageAdapter.notifyDataSetChanged();
                pgbCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ImagesActivity.this, databaseError.getMessage(),
                        Toast.LENGTH_SHORT).show();
                pgbCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Click a la posición: "+position,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWhatEverClick(int position) {
        Toast.makeText(this, "Click a la posición: "+position,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {
        Upload selectedItem = uploadList.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageReference = storage.getReferenceFromUrl(selectedItem.getUrlImage());
        imageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child(selectedKey).removeValue();
                Toast.makeText(ImagesActivity.this, "Eliminado exitosamente",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        databaseReference.removeEventListener(dbListener);
    }
}
