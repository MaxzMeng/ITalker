package com.example.factory.presenter.search;

import me.maxandroid.factory.presenter.BasePresenter;

public class SearchGroupPresenter extends BasePresenter<SearchContract.GroupView>
        implements SearchContract.Presenter {
    public SearchGroupPresenter(SearchContract.GroupView mView) {
        super(mView);
    }

    @Override
    public void search(String content) {

    }
}
