package com.example.factory.presenter.search;

import com.example.factory.data.helper.GroupHelper;
import com.example.factory.model.card.GroupCard;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

import me.maxandroid.factory.data.DataSource;
import me.maxandroid.factory.presenter.BasePresenter;
import retrofit2.Call;

public class SearchGroupPresenter extends BasePresenter<SearchContract.GroupView>
        implements SearchContract.Presenter,DataSource.Callback<List<GroupCard>> {
    private Call searchCall;

    public SearchGroupPresenter(SearchContract.GroupView mView) {
        super(mView);
    }

    @Override
    public void search(String content) {
        start();
        if (searchCall != null && !searchCall.isExecuted()) {
            searchCall.cancel();
        }
        searchCall = GroupHelper.search(content, this);
    }

    @Override
    public void onDataLoaded(final List<GroupCard> groupCards) {
        final SearchContract.GroupView view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onSearchDone(groupCards);
                }
            });
        }
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final SearchContract.GroupView view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(strRes);
                }
            });
        }
    }
}
