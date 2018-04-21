package me.maxandroid.italker.frags.user;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.factory.Factory;
import com.example.factory.net.UploadHelper;
import com.example.factory.presenter.user.UpdateInfoContract;
import com.example.factory.presenter.user.UpdateInfoPresenter;
import com.yalantis.ucrop.UCrop;

import net.qiujuer.genius.ui.widget.Loading;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import me.maxandroid.common.app.Application;
import me.maxandroid.common.app.Fragment;
import me.maxandroid.common.app.PresenterFragment;
import me.maxandroid.common.widget.PortraitView;
import me.maxandroid.italker.R;
import me.maxandroid.italker.activities.MainActivity;
import me.maxandroid.italker.frags.media.GalleryFragment;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateInfoFragment extends PresenterFragment<UpdateInfoContract.Presenter> implements UpdateInfoContract.View {
    @BindView(R.id.im_portrait)
    PortraitView mPortraitView;
    @BindView(R.id.im_sex)
    ImageView mSex;
    @BindView(R.id.edit_desc)
    EditText mDesc;
    @BindView(R.id.loading)
    Loading mLoading;
    @BindView(R.id.btn_submit)
    Button mSubmit;
    private String mPortraitPath;
    private boolean isMan;

    public UpdateInfoFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_update_info;
    }

    @OnClick(R.id.im_portrait)
    void onPortraitClick() {
        new GalleryFragment().setListener(new GalleryFragment.OnSelectedListener() {
            @Override
            public void onSelectedImage(String path) {
                UCrop.Options options = new UCrop.Options();
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                options.setCompressionQuality(96);

                File dPath = Application.getPortraitTmpFile();
                UCrop.of(Uri.fromFile(new File(path)), Uri.fromFile(dPath))
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(520, 520)
                        .withOptions(options)
                        .start(getActivity());
            }
        }).show(getChildFragmentManager(), GalleryFragment.class.getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                loadPortrait(resultUri);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    private void loadPortrait(Uri uri) {
        mPortraitPath = uri.getPath();
        Glide.with(this)
                .load(uri)
                .asBitmap()
                .centerCrop()
                .into(mPortraitView);
        final String localPath = uri.getPath();
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                UploadHelper.uploadPortrait(localPath);
            }
        });

    }

    @OnClick(R.id.im_sex)
    void onSexClick() {
        isMan = !isMan;
        Drawable drawable = getResources().getDrawable(isMan ? R.drawable.ic_sex_man : R.drawable.ic_sex_woman);
        mSex.setImageDrawable(drawable);
        mSex.getBackground().setLevel(isMan ? 0 : 1);
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        String desc = mDesc.getText().toString();
        mPresenter.update(mPortraitPath, desc, isMan);
    }

    @Override
    protected UpdateInfoContract.Presenter initPresenter() {
        return new UpdateInfoPresenter(this);
//        return null;
    }

    @Override
    public void updateSucceed() {
        MainActivity.show(getContext());
        getActivity().finish();
    }

    @Override
    public void showError(int str) {
        super.showError(str);
        mLoading.stop();
        mPortraitView.setEnabled(true);
        mDesc.setEnabled(true);
        mSex.setEnabled(true);
        mSubmit.setEnabled(true);
    }

    @Override
    public void showLoading() {
        super.showLoading();
        mLoading.start();
        mPortraitView.setEnabled(false);
        mDesc.setEnabled(false);
        mSex.setEnabled(false);
        mSubmit.setEnabled(false);
    }
}
