package war.of.findbook.fragments;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import war.of.findbook.interfaces.CallBackFragment;

public class Fragment_Base extends Fragment {
    protected CallBackFragment mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof CallBackFragment){
            mListener= (CallBackFragment) context;
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener=null;
    }
}
