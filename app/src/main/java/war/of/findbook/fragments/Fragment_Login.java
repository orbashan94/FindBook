package war.of.findbook.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import war.of.findbook.R;
import war.of.findbook.activities.Activity_Main;
import war.of.findbook.repositories.Book_Repository;
import war.of.findbook.utilities.Shared_Preference_Helper;


public class Fragment_Login extends Fragment_Base {


    private EditText edtPass, edtEmail;
    private Button btnLogin;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


     }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadViews(view);
        loadListenerClick();
    }

    private void loadListenerClick() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String pass = edtPass.getText().toString();


                FirebaseAuth mAuth = FirebaseAuth.getInstance();

                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                             Shared_Preference_Helper.getInstance(getContext()).storeUserID(email);
                             Book_Repository.getInstance(getContext()).loadNameFromLogin(email);
                            mListener.showActivity(Activity_Main.class);
                        } else
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }


    private void loadViews(View view) {
        edtPass = view.findViewById(R.id.edtPass);
        edtEmail = view.findViewById(R.id.edtEmail);
        btnLogin = view.findViewById(R.id.btnLogin);
    }


}