package war.of.findbook.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import war.of.findbook.utilities.My_Screen_Utils;

public abstract class Activity_Base extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        My_Screen_Utils.hideSystemUI2(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            My_Screen_Utils.hideSystemUI2(this);
        }
    }

}