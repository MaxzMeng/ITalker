package com.example.factory.utils;

import android.support.v7.util.DiffUtil;

import java.util.List;

public class DiffUtilDataCallback<T extends DiffUtilDataCallback.UiDataDiffer<T>> extends DiffUtil.Callback {
    private List<T> mOldList;
    private List<T> mNewList;

    public DiffUtilDataCallback(List<T> mOldList, List<T> mNewList) {
        this.mOldList = mOldList;
        this.mNewList = mNewList;
    }


    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        T beanOld = mOldList.get(oldItemPosition);
        T beanNew = mNewList.get(newItemPosition);
        return beanNew.isSame(beanOld);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        T beanOld = mOldList.get(oldItemPosition);
        T beanNew = mNewList.get(newItemPosition);
        return beanNew.isUiContentSame(beanOld);
    }

    public interface UiDataDiffer<T> {
        boolean isSame(T old);

        boolean isUiContentSame(T old);
    }
}
