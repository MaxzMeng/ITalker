package me.maxandroid.italker.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;


/**
 * Created by mxz on 18-3-11.
 */

public class NavHelper<T> {
    private final SparseArray<Tab<T>> tabs = new SparseArray<>();
    private final Context context;
    private final int containerId;
    private final FragmentManager fragmentManager;
    private final OnTabChangedListener<T> listener;
    private Tab<T> currentTab;

    public NavHelper(Context context, int containerId, FragmentManager fragmentManager, OnTabChangedListener<T> listener) {
        this.context = context;
        this.containerId = containerId;
        this.fragmentManager = fragmentManager;
        this.listener = listener;
    }

    public NavHelper<T> add(int menuId, Tab<T> tab) {
        tabs.put(menuId, tab);
        return this;
    }

    public Tab<T> getCurrentTab() {
        return currentTab;
    }

    public boolean performClickMenu(int menuId) {
        Tab<T> tab = tabs.get(menuId);
        if (tab != null) {
            doSelect(tab);
            return true;
        }
        return false;
    }

    private void doSelect(Tab<T> tab) {
        Tab<T> oldTab = null;
        if (currentTab != null) {
            oldTab = currentTab;
            if (oldTab == tab) {
                notifyTabReselect(tab);
                return;
            }
        }
        currentTab = tab;
        doTabChanged(currentTab, oldTab);
    }

    private void doTabChanged(Tab<T> newTab, Tab<T> oldTab) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (oldTab != null) {
            if (oldTab.fragment != null) {
                ft.detach(oldTab.fragment);
            }
        }
        if (newTab != null) {
            if (newTab.fragment == null) {
                Fragment fragment = android.support.v4.app.Fragment.instantiate(context, newTab.clx.getName(), null);
                newTab.fragment = fragment;
                ft.add(containerId, fragment, newTab.clx.getName());
            } else {
                ft.attach(newTab.fragment);
            }
        }
        ft.commit();
        notifyTabSelect(newTab, oldTab);
    }

    private void notifyTabSelect(Tab<T> newTab, Tab<T> oldTab) {
        if (listener != null) {
            listener.onTabChanged(newTab, oldTab);
        }
    }

    private void notifyTabReselect(Tab<T> tab) {

    }

    public static class Tab<T> {
        public Tab(Class<? extends Fragment> clx, T extra) {
            this.clx = clx;
            this.extra = extra;
        }

        public Class<? extends Fragment> clx;
        public T extra;
        Fragment fragment;
    }

    public interface OnTabChangedListener<T> {
        void onTabChanged(Tab<T> newTab, Tab<T> oldTab);
    }
}
