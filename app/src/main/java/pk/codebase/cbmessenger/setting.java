package pk.codebase.cbmessenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class setting extends AppCompatActivity {

    ImageView setProfile;
    EditText setName, setStatus;
    Button doneButton;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String email, password;
    Uri setImageUri;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(setting.this);  // Initialize progressDialog
        setProfile = findViewById(R.id.settingprofile);
        setName = findViewById(R.id.settingname);
        setStatus = findViewById(R.id.settingstatus);
        doneButton = findViewById(R.id.donebutt);

        // First we have to fetch data, so that when the user goes to the setting page his/her data would be already there.
        DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
        StorageReference storageReference = storage.getReference().child("Upload").child(auth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email = snapshot.child("mail").getValue().toString();
                password = snapshot.child("password").getValue().toString();
                String name = snapshot.child("userName").getValue().toString();
                String profile = snapshot.child("profilepic").getValue().toString();
                String status = snapshot.child("status").getValue().toString();
                setName.setText(name);
                setStatus.setText(status);
                Picasso.get().load(profile).into(setProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("images/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                String name = setName.getText().toString();
                String status = setStatus.getText().toString();
                if (setImageUri != null) {
                    storageReference.putFile(setImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String finalimageUri = uri.toString();
                                    Users users = new Users(auth.getUid(), name, email, password, finalimageUri, status);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(setting.this, "Data is saved", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();  // Dismiss the progress dialog
                                                Intent intent = new Intent(setting.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                progressDialog.dismiss();  // Dismiss the progress dialog
                                                Toast.makeText(setting.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                } else {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String finalImageUri = uri.toString();
                            Users users = new Users(auth.getUid(), name, email, password, finalImageUri, status);
                            reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(setting.this, "Data Is save", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();  // Dismiss the progress dialog
                                        Intent intent = new Intent(setting.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        progressDialog.dismiss();  // Dismiss the progress dialog
                                        Toast.makeText(setting.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (data != null) {
                setImageUri = data.getData();
                setProfile.setImageURI(setImageUri);
            }
        }
    }
}
