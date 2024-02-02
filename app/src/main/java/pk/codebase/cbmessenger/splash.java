package pk.codebase.cbmessenger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class splash extends AppCompatActivity {

    ImageView logo;
    TextView name, own1, own2;
    Animation topAnim, bottomAnim;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();

        logo = findViewById(R.id.logoimg);
        name = findViewById(R.id.logonameimg);
        own1 = findViewById(R.id.ownone);
        own2 = findViewById(R.id.owntwo);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        logo.setAnimation(topAnim);
        name.setAnimation(bottomAnim);
        own1.setAnimation(bottomAnim);
        own2.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if the user is already authenticated
                if (auth.getCurrentUser() != null) {
                    // User is already authenticated, go to MainActivity
                    startActivity(new Intent(splash.this, MainActivity.class));
                } else {
                    // User is not authenticated, go to LoginActivity
                    startActivity(new Intent(splash.this, login.class));
                }
                finish();
            }
        }, 3000);
    }
}
