package war.of.findbook.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import war.of.findbook.R;
import war.of.findbook.utilities.Shared_Preference_Helper;


public class Fragment_Profile extends Fragment {

    private FirebaseUser user;
    private TextView profile_TXT_name;
    private TextView profile_TXT_email;
    private Button btnLogOut;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        findViews(view);
        user = FirebaseAuth.getInstance().getCurrentUser();
        initViews();
        return view;
    }


    private void findViews(View view) {
        btnLogOut = view.findViewById(R.id.btnLogOut);
        profile_TXT_name = view.findViewById(R.id.profile_TXT_name);
        profile_TXT_email = view.findViewById(R.id.profile_TXT_mail);


    }

    private void initViews() {
        profile_TXT_name.setText(Shared_Preference_Helper.getInstance(getContext()).getUserName());
        profile_TXT_email.setText(user.getEmail());

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Shared_Preference_Helper.getInstance(getContext()).logOut();
                getActivity().finish();
            }
        });
    }
}

