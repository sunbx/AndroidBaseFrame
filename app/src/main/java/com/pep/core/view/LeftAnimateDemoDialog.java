package com.pep.core.view;

import android.view.Gravity;
import android.view.View;

import com.pep.core.R;
import com.pep.core.libbase.PEPBaseDialogFragment;


public class LeftAnimateDemoDialog extends PEPBaseDialogFragment {

    private View contentView;


    @Override
    protected int getAnimateStart() {
        return Gravity.TOP;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_left_demo;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }


}
