package com.pep.core.view;

import android.animation.Animator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pep.core.R;
import com.pep.core.uibase.PEPAnimate;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import static android.view.KeyEvent.KEYCODE_BACK;


public class LeftAnimateDemoDialog extends DialogFragment {

    private View contentView;

    @Override
    public void onStart() {
        super.onStart();
        PEPAnimate.initWindow(this, Gravity.LEFT);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, com.pep.core.uibase.R.style.CommonDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_left_demo, container, false);
        PEPAnimate.leftStartAnimate(contentView);

        Objects.requireNonNull(getDialog()).setOnKeyListener(dialogInterface);
        return contentView;
    }

    /**
     * The Dialog interface.
     */
    private DialogInterface.OnKeyListener dialogInterface = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
            //监听dialog 返回键
            if (KEYCODE_BACK == i && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                //执行关闭动画
                PEPAnimate.leftCloseAnimate(contentView, new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        Log.e("TAG", "onAnimationStart");
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        //动画执行完成进行关闭dialogfragment
                        dialogInterface.dismiss();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                        Log.e("TAG", "onAnimationCancel");
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                        Log.e("TAG", "onAnimationRepeat");
                    }
                });


                return true;
            }
            return false;
        }
    };


}
