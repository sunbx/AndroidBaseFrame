package com.pep.core.uibase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pep.core.libimage.PEPImageManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * The type Photo page view fragment.
 *
 * @author sunbaixin
 */
public class PhotoPageViewFragment extends Fragment {
    /**
     * Sets on click listener. 图片点击回调监听
     *
     * @param onClickListener the on click listener
     */
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private OnClickListener onClickListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inPhotoPageViewFragmentflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inPhotoPageViewFragmentflater.inflate(R.layout.view_image, container, false);
        PhotoView image = root.findViewById(R.id.image);
        String url = (String) getArguments().get("url");
        PEPImageManager.getInstance().load(this, image, url);
        image.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (onClickListener != null) {
                    onClickListener.onClick(view);
                }
            }
        });
        return root;
    }

    /**
     * The interface On click listener.图片点击回调
     */
    interface OnClickListener {
        /**
         * On click.回调事件
         *
         * @param view the view
         */
        void onClick(View view);
    }
}