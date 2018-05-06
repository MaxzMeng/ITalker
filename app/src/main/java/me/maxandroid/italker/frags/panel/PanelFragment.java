package me.maxandroid.italker.frags.panel;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import net.qiujuer.genius.ui.Ui;

import java.io.File;
import java.util.List;

import me.maxandroid.common.app.Application;
import me.maxandroid.common.app.Fragment;
import me.maxandroid.common.tools.AudioRecordHelper;
import me.maxandroid.common.tools.UiTool;
import me.maxandroid.common.widget.AudioRecordView;
import me.maxandroid.common.widget.GalleryView;
import me.maxandroid.common.widget.recycler.RecyclerAdapter;
import me.maxandroid.face.Face;
import me.maxandroid.italker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PanelFragment extends Fragment {
    private PanelCallback mCallBack;
    private View mFacePanel;
    private View mGalleryPanel;
    private View mRecordPanel;

    public PanelFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_panel;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        initFace(root);
        initRecord(root);
        initGallery(root);
    }

    public void setup(PanelCallback callback) {
        mCallBack = callback;
    }

    private void initFace(View root) {
        View facePanel = mFacePanel = root.findViewById(R.id.lay_panel_face);
        TabLayout tabLayout = facePanel.findViewById(R.id.tab);
        View backspace = facePanel.findViewById(R.id.im_backspace);
        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PanelCallback callback = mCallBack;
                if (callback == null) {
                    return;
                }
                KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                callback.getInputEditText().dispatchKeyEvent(event);
            }
        });
        ViewPager viewPager = facePanel.findViewById(R.id.pager);
        tabLayout.setupWithViewPager(viewPager);

        final int minFaceSize = (int) Ui.dipToPx(getResources(), 48);
        final int totalScreen = UiTool.getScreenWidth(getActivity());
        final int spanCount = totalScreen / minFaceSize;
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return Face.all(getContext()).size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.lay_face_content, container, false);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
                List<Face.Bean> faces = Face.all(getContext()).get(position).faces;
                FaceAdapter adapter = new FaceAdapter(faces, new RecyclerAdapter.AdapterListenerImpl<Face.Bean>() {
                    @Override
                    public void onItemClick(RecyclerAdapter.ViewHolder holder, Face.Bean bean) {
                        if (mCallBack == null) {
                            return;
                        }
                        EditText editText = mCallBack.getInputEditText();
                        Face.inputFace(getContext(), editText.getText(), bean, (int) (editText.getTextSize() + Ui.dipToPx(getResources(), 2)));

                    }
                });
                recyclerView.setAdapter(adapter);
                container.addView(recyclerView);
                return recyclerView;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return Face.all(getContext()).get(position).name;
            }
        });
    }

    private void initRecord(View root) {
        View recordView = mRecordPanel = root.findViewById(R.id.lay_panel_record);
        final AudioRecordView audioRecordView = recordView.findViewById(R.id.view_audio_record);
        File tmpFile = Application.getAudioTmpFile(true);
        final AudioRecordHelper helper = new AudioRecordHelper(tmpFile, new AudioRecordHelper.RecordCallback() {
            @Override
            public void onRecordStart() {

            }

            @Override
            public void onProgress(long time) {

            }

            @Override
            public void onRecordDone(File file, long time) {
                if (time < 1000) {
                    return;
                }

                File audioFile = Application.getAudioTmpFile(false);
                if (file.renameTo(audioFile)) {
                    PanelCallback panelCallback = mCallBack;
                    if (panelCallback != null) {
                        panelCallback.onRecordSend(audioFile, time);
                    }
                }
            }
        });
        audioRecordView.setup(new AudioRecordView.Callback() {
            @Override
            public void requestStartRecord() {
                helper.recordAsync();
            }

            @Override
            public void requestStopRecord(int type) {
                switch (type) {
                    case AudioRecordView.END_TYPE_DELETE:
                    case AudioRecordView.END_TYPE_CANCEL:
                        helper.stop(true);
                        break;
                    case AudioRecordView.END_TYPE_NONE:
                    case AudioRecordView.END_TYPE_PLAY:
                        helper.stop(false);
                        break;
                }
            }
        });
    }

    private void initGallery(View root) {
        View galleryPanel = mGalleryPanel = root.findViewById(R.id.lay_gallery_panel);
        final GalleryView galleryView = galleryPanel.findViewById(R.id.view_gallery);
        final TextView selectedSize = galleryPanel.findViewById(R.id.txt_gallery_select_count);
        galleryView.setup(getLoaderManager(), new GalleryView.SelectedChangeListener() {
            @Override
            public void onSelectedCountChanged(int count) {
                String resStr = getText(R.string.label_gallery_selected_size).toString();
                selectedSize.setText(String.format(resStr, count));
            }
        });
        galleryPanel.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGallerySendClick(galleryView, galleryView.getSelectedPath());
            }
        });
    }

    private void onGallerySendClick(GalleryView galleryView, String[] path) {
        galleryView.clear();

        PanelCallback callback = mCallBack;
        if (callback == null) {
            return;
        }
        callback.onSendGallery(path);

    }

    public void showFace() {
        mGalleryPanel.setVisibility(View.GONE);
        mRecordPanel.setVisibility(View.GONE);
        mFacePanel.setVisibility(View.VISIBLE);
    }

    public void showRecord() {
        mGalleryPanel.setVisibility(View.GONE);
        mRecordPanel.setVisibility(View.VISIBLE);
        mFacePanel.setVisibility(View.GONE);
    }

    public void showGallery() {
        mGalleryPanel.setVisibility(View.VISIBLE);
        mRecordPanel.setVisibility(View.GONE);
        mFacePanel.setVisibility(View.GONE);
    }


    public interface PanelCallback {
        EditText getInputEditText();

        void onSendGallery(String[] paths);

        void onRecordSend(File file, long time);
    }

}
