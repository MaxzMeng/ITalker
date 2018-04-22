package me.maxandroid.factory.presenter;

import android.support.v7.util.DiffUtil;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

import me.maxandroid.common.widget.recycler.RecyclerAdapter;

public class BaseRecyclerPresenter<ViewModel, View extends BaseContract.RecyclerView> extends BasePresenter<View> {

    public BaseRecyclerPresenter(View mView) {
        super(mView);
    }

    protected void refreshData(final List<ViewModel> dataList) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                View view = getView();
                if (view == null) {
                    return;
                }
                RecyclerAdapter<ViewModel> adapter = view.getRecyclerAdapter();
                adapter.replace(dataList);
                view.onAdapterDataChanged();
            }
        });
    }

    protected void refreshData(final DiffUtil.DiffResult diffResult, final List<ViewModel> dataList) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                refreshDataOnUiThread(diffResult, dataList);
            }
        });
    }

    private void refreshDataOnUiThread(final DiffUtil.DiffResult diffResult, final List<ViewModel> dataList) {
        View view = getView();
        if (view == null) {
            return;
        }
        RecyclerAdapter<ViewModel> adapter = view.getRecyclerAdapter();
        adapter.getItems().clear();
        adapter.getItems().addAll(dataList);
        view.onAdapterDataChanged();
        diffResult.dispatchUpdatesTo(adapter);
    }
}
