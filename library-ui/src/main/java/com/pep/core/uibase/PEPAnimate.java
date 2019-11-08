package com.pep.core.uibase;


import android.animation.Animator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


import java.util.Objects;

import androidx.fragment.app.DialogFragment;

public class PEPAnimate {

    private static int Y = 1000;

    public static void leftStartAnimate(View view) {
        view.setX(-getScreenWidth(view.getContext()));
        Objects.requireNonNull(view).animate().translationX(0).setDuration(500);
    }

    public static void rightStartAnimate(View view) {
        view.setX(getScreenWidth(view.getContext()));
        Objects.requireNonNull(view).animate().translationX(0).setDuration(500);
    }

    public static void topStartAnimate(View view) {
        if(view.getY() == 0){
            view.setY(-Y);
        }

        Objects.requireNonNull(view).animate().translationY(0).setDuration(500);
    }

    public static void bottomStartAnimate(View view) {
        if(view.getY() == 0){
            view.setY(Y);
        }

        Objects.requireNonNull(view).animate().translationY(0).setDuration(500);
    }


    public static void leftCloseAnimate(View view, Animator.AnimatorListener listener) {
        Objects.requireNonNull(view).animate().translationX(-getScreenWidth(view.getContext())).setDuration(500).setListener(listener);
    }

    public static void rightCloseAnimate(View view, Animator.AnimatorListener listener) {
        Objects.requireNonNull(view).animate().translationX(getScreenWidth(view.getContext())).setDuration(500).setListener(listener);
    }

    public static void topCloseAnimate(View view, Animator.AnimatorListener listener) {
        Objects.requireNonNull(view).animate().translationY(-getScreenHeight(view.getContext())).setDuration(500).setListener(listener);


    }

    public static void bottomCloseAnimate(View view, Animator.AnimatorListener listener) {
        Objects.requireNonNull(view).animate().translationY(getScreenHeight(view.getContext())).setDuration(500).setListener(listener);
    }

    public static void initWindow(DialogFragment dialogFragment, int gravity) {
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window win = dialogFragment.getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics dm = new DisplayMetrics();
        dialogFragment.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = gravity;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);
    }

    /**
     * Get Screen Width
     */
    public static int getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    /**
     * Get Screen Height
     */
    public static int getScreenHeight(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    private static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }
}
