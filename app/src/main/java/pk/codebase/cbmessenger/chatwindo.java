package pk.codebase.cbmessenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatwindo extends AppCompatActivity {
    String receiverimg, receiverUid, receiverName, SenderUID;
    CircleImageView profile;
    TextView receiverNName;
    CardView sendBtn;
    EditText textmsg;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_win);
        getSupportActionBar().hide();

        receiverName = getIntent().getStringExtra("nameeee");
        receiverimg = getIntent().getStringExtra("receiverImg");
        receiverUid = getIntent().getStringExtra("uid");

        sendBtn = findViewById(R.id.sendbtnn);
        textmsg = findViewById(R.id.textmsg);

        profile = findViewById(R.id.profileimgg);
        receiverNName= findViewById(R.id.recivername);

        Picasso.get().load(receiverimg).into(profile);
        receiverNName.setText(""+receiverName);

        SenderUID =

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = textmsg.getText().toString();
                if (message.isEmpty()){
                    Toast.makeText(chatwindo.this, "Enter the message first", Toast.LENGTH_SHORT).show();
                }
                textmsg.setText("");
                Date data = new Date();
                msgModelclass messagess = new msgModelclass()
            }
        });
    }
}