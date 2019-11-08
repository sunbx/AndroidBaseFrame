package com.pep.core.libbase;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;


public abstract class PEPBaseLayoutActivity<A extends PEPBaseActivity, P extends PEPBasePresenter> extends RelativeLayout implements View.OnClickListener {
    public A activity;
    public P presenter;

    public PEPBaseLayoutActivity(Context context) {
        super(context);
    }

    public PEPBaseLayoutActivity(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PEPBaseLayoutActivity(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void init() {
        View.inflate(getContext(), getLayoutId(), this);
        initView();
        initData();
        initListener();
    }


    public void setPresenter(A activity, P presenter) {
        this.activity = activity;
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
