package com.pep.core.libbase;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.pep.core.uibase.PEPDialogAnimate;

import java.util.Objects;

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
    public ViewGroup contentView;

    @Override
    public void onStart() {
        super.onStart();
        PEPDialogAnimate.initWindow(this, getAnimateStart());
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, com.pep.core.uibase.R.style.CommonDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = (ViewGroup) inflater.inflate(getLayoutId(), container, false);
        switch (getAnimateStart()) {
            case Gravity.TOP:
                PEPDialogAnimate.topStartAnimate(contentView, isAnimateKick());
                break;
            case Gravity.BOTTOM:
                PEPDialogAnimate.bottomStartAnimate(contentView, isAnimateKick());
                break;
            case Gravity.LEFT:
                PEPDialogAnimate.leftStartAnimate(contentView, isAnimateKick());
                break;
            case Gravity.RIGHT:
                PEPDialogAnimate.rightStartAnimate(contentView, isAnimateKick());
                break;
            default:
        }
        if (contentView != null && isAnimateTouch()) {
            contentView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            downX = (int) motionEvent.getRawX();
                            downY = (int) motionEvent.getRawY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            slideY = downY - (int) motionEvent.getRawY();
                            slideX = downX - (int) motionEvent.getRawX();

                            if (getAnimateStart() == Gravity.TOP) {
                                if (slideY >= 0) {
                                    if (isAnimateKick()) {
                                        ViewHelper.setY(view, -slideY / 4);
                                        Log.e("PEPBaseDialogFragment", "slideX:" + slideY);
                                    }
                                } else {
                                    ViewHelper.setY(view, -slideY);
                                    Log.e("PEPBaseDialogFragment", "slideX:" + slideY);
                                    if (isAnimateKick()) {
                                        ViewHelper.setRotation(view, -slideY / 100);
                                        Log.e("PEPBaseDialogFragment", "slideX:" + slideY);
                                    }
                                }
                            }


                            if (getAnimateStart() == Gravity.BOTTOM) {
                                if (slideY >= 0) {
                                    if (isAnimateKick()) {
                                        ViewHelper.setY(view, -slideY / 4);
                                        Log.e("PEPBaseDialogFragment", "slideX:" + slideY);
                                    }
                                } else {
                                    ViewHelper.setY(view, -slideY);
                                    Log.e("PEPBaseDialogFragment", "slideX:" + slideY);
                                    if (isAnimateKick()) {
                                        ViewHelper.setRotation(view, -slideY / 100);
                                        Log.e("PEPBaseDialogFragment", "slideX:" + slideY);
                                    }
                                }
                            }

                            if (getAnimateStart() == Gravity.LEFT) {
                                if (slideX >= 0) {
                                    ViewHelper.setX(view, -slideX);
                                    Log.e("PEPBaseDialogFragment", "slideX:" + -slideX);
                                }
                            }

                            if (getAnimateStart() == Gravity.RIGHT) {
                                if (slideX <= 0) {
                                    ViewHelper.setX(view, -slideX);
                                    Log.e("PEPBaseDialogFragment", "slideX:" + -slideX);
                                }
                            }


                            break;
                        case MotionEvent.ACTION_UP:
                            if (getAnimateStart() == Gravity.LEFT) {
                                if (slideX > 200) {
                                    ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", -slideX, -PEPDialogAnimate.getScreenWidth(view.getContext())).setDuration(300);
                                    translationX.addListener(animatorListener);
                                    translationX.start();
                                } else {
                                    if (slideX > 0) {
                                        ObjectAnimator.ofFloat(view, "translationX", -slideX, 0).setDuration(300).start();
                                    }

                                }
                            }

                            if (getAnimateStart() == Gravity.RIGHT) {
                                if (slideX < -200) {
                                    ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", -slideX, PEPDialogAnimate.getScreenWidth(view.getContext())).setDuration(300);
                                    translationX.addListener(animatorListener);
                                    translationX.start();
                                } else {
                                    if (slideX < 0) {
                                        ObjectAnimator.ofFloat(view, "translationX", -slideX, 0).setDuration(300).start();
                                    }

                                }
                            }


                            if (getAnimateStart() == Gravity.TOP) {
                                if (slideY >= 0) {
                                    if (isAnimateKick()) {
                                        ObjectAnimator.ofFloat(view, "translationY", -slideY / 4, 0).setDuration(300).start();
                                    }
                                } else {
                                    if (slideY > -200) {
                                        AnimatorSet set = new AnimatorSet();
                                        if (isAnimateKick()) {
                                            set.playTogether(
                                                    ObjectAnimator.ofFloat(view, "rotation", -slideY / 100, 0),
                                                    ObjectAnimator.ofFloat(view, "translationY", -slideY, 0)
                                            );
                                        } else {
                                            set.playTogether(
                                                    ObjectAnimator.ofFloat(view, "translationY", -slideY, 0)
                                            );
                                        }

                                        set.setDuration(300).start();
                                    } else {
                                        ViewHelper.setY(view, -slideY);
                                        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", -slideY, PEPDialogAnimate.getScreenHeight(view.getContext())).setDuration(300);
                                        translationY.addListener(animatorListener);
                                        translationY.start();
                                    }

                                }
                            }


                            if (getAnimateStart() == Gravity.BOTTOM) {
                                if (slideY >= 0) {
                                    if (isAnimateKick()) {
                                        ObjectAnimator.ofFloat(view, "translationY", -slideY / 4, 0).setDuration(300).start();
                                    }
                                } else {
                                    if (slideY > -200) {
                                        AnimatorSet set = new AnimatorSet();
                                        if (isAnimateKick()) {
                                            set.playTogether(
                                                    ObjectAnimator.ofFloat(view, "rotation", -slideY / 100, 0),
                                                    ObjectAnimator.ofFloat(view, "translationY", -slideY, 0)
                                            );
                                        } else {
                                            set.playTogether(
                                                    ObjectAnimator.ofFloat(view, "translationY", -slideY, 0)
                                            );
                                        }

                                        set.setDuration(300).start();
                                    } else {
                                        ViewHelper.setY(view, -slideY);
                                        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", -slideY, PEPDialogAnimate.getScreenHeight(view.getContext())).setDuration(300);
                                        translationY.addListener(animatorListener);
                                        translationY.start();
                                    }

                                }
                            }
                            break;
                        default:
                    }
                    return true;
                }

                private int downY = 0;//按下时的点
                private int downX = 0;//按下时的点
                private int slideY = 0;//最终移动距离
                private int slideX = 0;//最终移动距离

            });
        }


        Objects.requireNonNull(getDialog()).setOnKeyListener(dialogInterface);

        initView();
        initData();

        return contentView;
    }

    public View findViewById(int id) {
        return contentView.findViewById(id);
    }


    protected abstract int getAnimateStart();

    protected boolean isAnimateKick() {
        return false;
    }

    protected boolean isAnimateTouch() {
        return false;
    }

    public abstract int getLayoutId();

    public abstract void initView();

    public abstract void initData();

    public void initListener() {

    }


    public void close() {
        //执行关闭动画
        dismiss();
    }

    public void close(int animateStart) {
        switch (animateStart) {
            case Gravity.TOP:
                PEPDialogAnimate.topCloseAnimate(contentView, animatorListener);
                break;
            case Gravity.BOTTOM:
                PEPDialogAnimate.bottomCloseAnimate(contentView, animatorListener);
                break;
            case Gravity.LEFT:
                PEPDialogAnimate.leftCloseAnimate(contentView, animatorListener);
                break;
            case Gravity.RIGHT:
                PEPDialogAnimate.rightCloseAnimate(contentView, animatorListener);
                break;
            default:
        }
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
                close(getAnimateStart());


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
