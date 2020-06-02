package com.example.arshit.studentattendanceapp.FragmentManagement;

import android.support.v4.app.Fragment;
import android.view.View;



import java.util.List;

public abstract class ThemeFragment extends Fragment {
    public boolean isFormBeingEdited() {
        return false;
    }

    /**
     * Method is called when onBackPress is called in an {@link android.app.Activity}
     *
     * @return "true" if Fragment needs to be popped from back stack;
     */
    public boolean onBackPressed() {
        return true;
    }

    public View getCustomActionBar() {
        return null;
    }

    public abstract String getTitle();

    public boolean getScrollBehavior() {
        return false;
    }

    public boolean showBackAsUp() {
        return false;
    }

    public void manageDeepLink(List<String> deepLink) {
    }

    public void onShow() {
    }

}
