package me.maxandroid.italker.frags.main;


import butterknife.BindView;
import me.maxandroid.common.app.Fragment;
import me.maxandroid.common.widget.GalleryView;
import me.maxandroid.italker.R;


public class ActiveFragment extends Fragment {
    @BindView(R.id.galleryView)
    GalleryView mGallery;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initData() {
        super.initData();
        mGallery.setup(getLoaderManager(), new GalleryView.SelectedChangeListener() {
            @Override
            public void onSelectedCountChanged(int count) {

            }
        });
    }
}
