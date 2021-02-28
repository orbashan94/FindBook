package war.of.findbook.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import war.of.findbook.interfaces.IFragment_Loader;
import war.of.findbook.R;
import war.of.findbook.fragments.Fragment_Home;
import war.of.findbook.fragments.Fragment_Chat;
import war.of.findbook.fragments.Fragment_History;
import war.of.findbook.fragments.Fragment_Post_Book;
import war.of.findbook.fragments.Fragment_Profile;
import war.of.findbook.utilities.LocationUser;

public class Activity_Main extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, IFragment_Loader {

    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton menu_BTN_addbook;

    private static final int PERMISSION_REQUEST_APP = 55;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        initViews();

        initLocation();
        loadFragment(new Fragment_Home());

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void initLocation() {

        new LocationUser(this, new LocationUser.CallbackOnLocationUpdated() {
            @Override
            public void onLocationUpdate(long timeStamp, double latitude, double longitude) {

            }

            @Override
            public void onFailPermission() {
                requestPermissions();
            }
        }).execute();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != PERMISSION_REQUEST_APP)
            return;
        boolean killed = false;
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                killed = true;
                break;
            }
        }
        if (!killed)
            initLocation();
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,

                    },
                    PERMISSION_REQUEST_APP);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navMenu_ITEM_home:
                fragment = new Fragment_Home();
                break;

            case R.id.navMenu_ITEM_favorites:
                fragment = new Fragment_History();
                break;

            case R.id.navMenu_ITEM_profile:
                fragment = new Fragment_Profile();
                break;

            case R.id.navMenu_ITEM_chat:
                fragment = new Fragment_Chat();
                break;

        }

        return loadFragment(fragment);
    }


    private void findView() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        menu_BTN_addbook = findViewById(R.id.menu_BTN_addbook);

    }

    private void initViews() {
        menu_BTN_addbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new Fragment_Post_Book());
            }
        });

    }


    @Override
    public boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}