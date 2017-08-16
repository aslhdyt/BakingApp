package me.asl.assel.bakingapp.presenter.fragment;

import android.view.View;

/**
 * Created by assel on 8/3/17.
 */

public interface FragmentInterface {
    void FragmentChange(int currentPos);
    void CreateNavButton(View viewReference, int currentPos);
}
