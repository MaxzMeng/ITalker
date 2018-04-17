package me.maxandroid.common.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.bumptech.glide.RequestManager;

import de.hdodenhof.circleimageview.CircleImageView;
import me.maxandroid.common.R;
import me.maxandroid.factory.model.Author;

/**
 * Created by mxz on 18-3-11.
 */

public class PortraitView extends CircleImageView {


    public PortraitView(Context context) {
        super(context);
    }

    public PortraitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PortraitView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public void setup(RequestManager manager, Author author) {
        if (author == null) {
            return;
        }
        this.setup(manager, author.getPortrait());
    }
    public void setup(RequestManager manager, String url) {
        this.setup(manager, R.drawable.default_portrait, url);
    }
    public void setup(RequestManager manager, int resourceId, String url) {
        if (url == null) {
            url = "";
        }
        manager.load(url)
                .placeholder(resourceId)
                .centerCrop()
                .dontAnimate()
                .into(this);
    }
}
