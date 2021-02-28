package war.of.findbook.interfaces;

import android.os.Bundle;

public interface CallBackFragment {
    void showActivity( Class activity);
    void showFragment(int fragmentID);
    void showFragment(int fragmentID, Bundle bundle);
}
