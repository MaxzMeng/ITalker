package com.example.factory.presenter.group;

import com.example.factory.model.db.Group;
import com.example.factory.model.db.User;

import me.maxandroid.factory.model.Author;
import me.maxandroid.factory.presenter.BaseContract;

public interface GroupCreateContract {
    interface Presenter extends BaseContract.Presenter {
        void create(String name, String desc, String picture);

        void changeSelect(ViewModel model, boolean isSelected);
    }

    interface View extends BaseContract.RecyclerView<Presenter,ViewModel> {
        void onCreateSucceed();
    }


    class ViewModel{
        public Author author;
        public boolean isSelected;
    }
}
