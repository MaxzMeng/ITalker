package me.maxandroid.italker.frags.panel;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;

import butterknife.BindView;
import me.maxandroid.common.widget.recycler.RecyclerAdapter;
import me.maxandroid.face.Face;
import me.maxandroid.italker.R;

public class FaceHolder extends RecyclerAdapter.ViewHolder<Face.Bean> {
    @BindView(R.id.im_face)
    ImageView mFace;


    public FaceHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void onBind(Face.Bean bean) {
        if (bean != null && ((bean.preview instanceof Integer)
                || bean.preview instanceof String)) {
            Glide.with(mFace.getContext())
                    .load(bean.preview)
                    .asBitmap()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .into(mFace);
        }
    }
}
