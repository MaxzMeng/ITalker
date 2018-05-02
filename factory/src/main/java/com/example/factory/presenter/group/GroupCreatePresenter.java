package com.example.factory.presenter.group;

import android.text.TextUtils;

import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.data.helper.GroupHelper;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.api.group.GroupCreateModel;
import com.example.factory.model.card.GroupCard;
import com.example.factory.model.db.view.UserSampleModel;
import com.example.factory.net.UploadHelper;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.maxandroid.factory.data.DataSource;
import me.maxandroid.factory.presenter.BaseRecyclerPresenter;

public class GroupCreatePresenter extends BaseRecyclerPresenter<GroupCreateContract.ViewModel, GroupCreateContract.View>
        implements GroupCreateContract.Presenter,DataSource.Callback<GroupCard>{
    private Set<String> users = new HashSet<>();

    public GroupCreatePresenter(GroupCreateContract.View mView) {
        super(mView);
    }

    @Override
    public void start() {
        super.start();
        Factory.runOnAsync(loader);
    }

    @Override
    public void create(final String name, final String desc, final String picture) {
        GroupCreateContract.View view = getView();
        view.showLoading();

        if (TextUtils.isEmpty(name) ||
                TextUtils.isEmpty(desc) ||
                TextUtils.isEmpty(picture) ||
                users.size() == 0) {
            view.showError(R.string.label_group_create_invalid);
            return;
        }

        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                String url = uploadPicture(picture);
                if (TextUtils.isEmpty(url)) {
                    return;
                }
                GroupCreateModel model = new GroupCreateModel(name, desc, url, users);
                GroupHelper.create(model, GroupCreatePresenter.this);
            }
        });


    }

    @Override
    public void changeSelect(GroupCreateContract.ViewModel model, boolean isSelected) {
        if (isSelected) {
            users.add(model.author.getId());
        } else {
            users.remove(model.author.getId());
        }
    }

    private String uploadPicture(String path) {
        String url = UploadHelper.uploadPortrait(path);
        if (TextUtils.isEmpty(url)) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    GroupCreateContract.View view = getView();
                    if (view != null) {
                        view.showError(R.string.data_upload_error);
                    }
                }
            });
        }
        return url;
    }
    private Runnable loader = new Runnable() {
        @Override
        public void run() {
//            List<User> users = UserHelper.getContact();
            List<UserSampleModel> sampleModels = UserHelper.getSampleContact();
            List<GroupCreateContract.ViewModel> models = new ArrayList<>();
            for (UserSampleModel sampleModel : sampleModels) {
                GroupCreateContract.ViewModel viewModel = new GroupCreateContract.ViewModel();
                viewModel.author = sampleModel;
                models.add(viewModel);
            }
            refreshData(models);
        }
    };

    @Override
    public void onDataLoaded(GroupCard groupCard) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                GroupCreateContract.View view = getView();
                if (view != null) {
                    view.onCreateSucceed();
                }
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                GroupCreateContract.View view = getView();
                if (view != null) {
                    view.showError(strRes);
                }
            }
        });
    }
}
