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

import com.google.firebase.auth.FirebaseAuth;

import war.of.findbook.R;
import war.of.findbook.activities.Activity_Main;
import war.of.findbook.repositories.Book_Repository;
import war.of.findbook.utilities.Shared_Preference_Helper;


public class Fragment_Register extends Fragment_Base {

    private String emailAddress;
    private String userName;
    private Button btnRegister;
    private EditText edtName;
    private EditText edtEmail;
    private EditText edtPass;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadViews(view);
        loadAllListeners();


    }

    public void createNewUser(String email, String pass) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                Shared_Preference_Helper.getInstance(getContext()).storeUserName(userName);
                Shared_Preference_Helper.getInstance(getContext()).storeUserID(email);

                Book_Repository.getInstance(getContext()).insertUser(email, userName);

                Toast.makeText(getContext(), "Register!", Toast.LENGTH_SHORT).show();
                mListener.showActivity(Activity_Main.class);

            } else
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

        });

    }

    private void loadAllListeners() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String pass = edtPass.getText().toString();
                String name = edtName.getText().toString();


                if (email.isEmpty() || pass.isEmpty() || name.isEmpty()) {
                    Toast.makeText(getContext(), "Input no valid", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pass.length() < 6) {
                    Toast.makeText(getContext(), "Pass to short", Toast.LENGTH_SHORT).show();
                    return;
                }

                userName = name;
                emailAddress = email;
                createNewUser(email, pass);
            }
        });


    }

    private void loadViews(View view) {
        btnRegister = view.findViewById(R.id.btnRegister);
        edtName = view.findViewById(R.id.edtName);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtPass = view.findViewById(R.id.edtPass);
    }
}