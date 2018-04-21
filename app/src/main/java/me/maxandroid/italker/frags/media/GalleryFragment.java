package me.maxandroid.italker.frags.media;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import me.maxandroid.common.app.Fragment;
import me.maxandroid.common.tools.UiTool;
import me.maxandroid.common.widget.GalleryView;
import me.maxandroid.italker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends BottomSheetDialogFragment implements GalleryView.SelectedChangeListener {
    private GalleryView mGallery;
    private OnSelectedListener mListener;

    public GalleryFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TransStatusBottomSheetDialog(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        mGallery = root.findViewById(R.id.galleryView);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGallery.setup(getLoaderManager(), this);
    }

    @Override
    public void onSelectedCountChanged(int count) {
        if (count > 0) {
            dismiss();
            if (mListener != null) {
                String[] path = mGallery.getSelectedPath();
                mListener.onSelectedImage(path[0]);
                mListener = null;
            }
        }
    }

    public GalleryFragment setListener(OnSelectedListener listener) {
        mListener = listener;
        return this;
    }

    public interface OnSelectedListener {
        void onSelectedImage(String path);
    }

    public static class TransStatusBottomSheetDialog extends BottomSheetDialog {

        public TransStatusBottomSheetDialog(@NonNull Context context) {
            super(context);
        }

        public TransStatusBottomSheetDialog(@NonNull Context context, int theme) {
            super(context, theme);
        }

        protected TransStatusBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            final Window window = getWindow();
            if (window == null) {
                return;
            }
            int screenHeight = UiTool.getScreenHeight(getOwnerActivity());
            int statusHeight = UiTool.getStatusBarHeight(getOwnerActivity());

            int dialogHeight = screenHeight - statusHeight;
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    dialogHeight <= 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        }
    }
}
