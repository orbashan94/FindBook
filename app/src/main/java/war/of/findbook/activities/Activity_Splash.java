package war.of.findbook.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



import war.of.findbook.R;

public class Activity_Splash extends Activity_Base {

    private FirebaseUser user;
    private ImageView login_IMG_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        user = FirebaseAuth.getInstance().getCurrentUser();

        //FirebaseAuth.getInstance().signOut();

        login_IMG_back = findViewById(R.id.login_IMG_back);


        Glide.with(this)
                .load(R.drawable.img_back)
                .centerCrop()
                .into(login_IMG_back);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user != null) {
                    openActivity(Activity_Main.class);
                } else { // no user signed in
                    openActivity(Activity_Register_Login.class);
                }
            }
        }, 2000);


    }


    private void openActivity(Class activity) {
        Intent myIntent = new Intent(this, activity);
        startActivity(myIntent);
        finish();

    }


}