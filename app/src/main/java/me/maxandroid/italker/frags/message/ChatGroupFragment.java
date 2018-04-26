package me.maxandroid.italker.frags.message;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.AppBarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.factory.model.db.Group;
import com.example.factory.presenter.message.ChatContract;

import me.maxandroid.italker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatGroupFragment extends ChatFragment<Group>
        implements ChatContract.GroupView {



    public ChatGroupFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat_group;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        super.onOffsetChanged(appBarLayout, verticalOffset);
        if (verticalOffset == 0) {
        }
    }

    @Override
    public void onInit(Group group) {

    }

    @Override
    protected ChatContract.Presenter initPresenter() {
        return null;
    }
}
