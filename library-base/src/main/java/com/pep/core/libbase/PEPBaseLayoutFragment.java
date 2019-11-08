package com.pep.core.libbase;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;


public abstract class PEPBaseLayoutFragment<F extends PEPBaseFragment, P extends PEPBasePresenter> extends RelativeLayout implements View.OnClickListener {
    public F fragment;
    public P presenter;

    public PEPBaseLayoutFragment(Context context) {
        super(context);
    }

    public PEPBaseLayoutFragment(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PEPBaseLayoutFragment(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void init() {
        View.inflate(getContext(), getLayoutId(), this);
        initView();
        initData();
        initListener();
    }


    public void setPresenter(F fragment, P presenter) {
        this.fragment = fragment;
        this.presenter = presenter;
        init();
    }

    public abstract void initView();

    public abstract void initData();

    public abstract void initListener();


    public abstract int getLayoutId();


    @Override
    public void onClick(View view) {

    }
}
