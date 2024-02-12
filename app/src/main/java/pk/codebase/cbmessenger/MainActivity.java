package pk.codebase.cbmessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView mainUserRecyclerView;
    UserAdapter adapter;
    FirebaseDatabase database;
    ArrayList<Users> usersArrayList;
    ImageView imglogout;
    ImageView camButton, setButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        camButton = findViewById(R.id.camBut);
        setButton = findViewById(R.id.settingBut);

        DatabaseReference reference = database.getReference().child("user");

        usersArrayList = new ArrayList<>();
        // if the user is not logged in then the user will directed to login page
        auth = FirebaseAuth.getInstance();
        mainUserRecyclerView = findViewById(R.id.mainUserRecyclerView);
        adapter = new UserAdapter(MainActivity.this,usersArrayList);
        mainUserRecyclerView.setAdapter(adapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                   Users users = dataSnapshot.getValue(Users.class);
                   usersArrayList.add(users);
               }
               adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        imglogout = findViewById(R.id.logoutimg);

        imglogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this,R.style.dialoge);
                dialog.setContentView(R.layout.dialog_layout);
                Button yes, no;
                yes = dialog.findViewById(R.id.yesBtn);
                no = dialog.findViewById(R.id.noBtn);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivity.this, login.class);
                        startActivity(intent);
                        finish();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, setting.class);
                startActivity(intent);
            }
        });

        camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,10);
            }
        });




        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(auth.getCurrentUser() == null){
            Intent intent = new Intent(MainActivity.this,login.class);
            startActivity(intent);
        }
    }
}