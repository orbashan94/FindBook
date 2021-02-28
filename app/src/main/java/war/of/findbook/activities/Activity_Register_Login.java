package war.of.findbook.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import war.of.findbook.R;
import war.of.findbook.interfaces.CallBackFragment;


public class Activity_Register_Login extends AppCompatActivity  implements CallBackFragment {

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);



        navController = Navigation.findNavController(this, R.id.nav_host_register_login_fragment);
    }


    NavController navController;
    @Override
    public void showActivity(Class activity) {
        Intent intent=new Intent(this,activity);
        startActivity(intent);
        finish();
    }

    @Override
    public void showFragment(int fragmentID) {
        navController.navigate(fragmentID);
    }

    @Override
    public void showFragment(int fragmentID, Bundle bundle) {
        navController.navigate(fragmentID,bundle);
    }
}