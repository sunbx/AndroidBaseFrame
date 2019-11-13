package com.pep.core.view;

import android.view.Gravity;
import android.view.View;

import com.pep.core.R;
import com.pep.core.libbase.PEPBaseDialogFragment;


public class AnimateDemoDialog extends PEPBaseDialogFragment {

    private View contentView;


    @Override
    protected int getAnimateStart() {
        return Gravity.BOTTOM;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_left_demo;
    }

    @Override
    protected boolean isAnimateKick() {
        return true;
    }

    @Override
    protected boolean isAnimateTouch() {
        return true;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }


}
