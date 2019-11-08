package com.pep.core.libbase;

import android.animation.Animator;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pep.core.uibase.PEPAnimate;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * @author sunbaixin QQ:283122529
 * @name PEPCore_Android
 * @class name：com.pep.core.libbase
 * @class describe
 * @time 2019-10-30 11:17
 * @change
 * @chang time
 * @class describe
 */
public abstract class PEPBaseDialogFragment extends DialogFragment {
    public View contentView;

    @Override
    public void onStart() {
        super.onStart();
        PEPAnimate.initWindow(this, getAnimateStart());
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, com.pep.core.uibase.R.style.CommonDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(getLayoutId(), container, false);
        PEPAnimate.leftStartAnimate(contentView);

        Objects.requireNonNull(getDialog()).setOnKeyListener(dialogInterface);

        initView();
        initData();

        return contentView;
    }

    public View findViewById(int id){
        return contentView.findViewById(id);
    }


    protected abstract int getAnimateStart();

    public abstract int getLayoutId();

    public abstract void initView();

    public abstract void initData();
    public void initListener(){

    }


    public void close() {
        //执行关闭动画
        PEPAnimate.leftCloseAnimate(contentView, animatorListener);
    }

    /**
     * The Dialog interface.
     */
    private DialogInterface.OnKeyListener dialogInterface = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(final DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
            //监听dialog 返回键
            if (KEYCODE_BACK == i && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                //执行关闭动画
                PEPAnimate.leftCloseAnimate(contentView, animatorListener);


                return true;
            }
            return false;
        }
    };

    Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            Log.e("TAG", "onAnimationStart");
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            //动画执行完成进行关闭dialogfragment
            dismiss();
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            Log.e("TAG", "onAnimationCancel");
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
            Log.e("TAG", "onAnimationRepeat");
        }
    };

}
