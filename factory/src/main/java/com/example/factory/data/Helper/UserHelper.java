package com.example.factory.data.Helper;

import android.util.Log;

import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.user.UserUpdateModel;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.User;
import com.example.factory.model.db.User_Table;
import com.example.factory.net.Network;
import com.example.factory.net.RemoteService;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.IOException;
import java.util.List;

import me.maxandroid.factory.data.DataSource;
import me.maxandroid.utils.CollectionUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mxz on 18-3-18.
 */

public class UserHelper {
    public static void update(UserUpdateModel model, final DataSource.Callback<UserCard> callback) {
        RemoteService service = Network.remote();
        Call<RspModel<UserCard>> call = service.userUpdate(model);

        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel.success()) {
                    UserCard userCard = rspModel.getResult();
//                    User user = userCard.build();
                    Factory.getUserCenter().dispatch(userCard);
//                    user.save();
//                    DbHelper.save(User.class, user);
                    callback.onDataLoaded(userCard);
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    public static Call search(String name, final DataSource.Callback<List<UserCard>> callback) {
        RemoteService service = Network.remote();
        Call<RspModel<List<UserCard>>> call = service.userSearch(name);
        call.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> rspModel = response.body();
                if (rspModel.success()) {
                    callback.onDataLoaded(rspModel.getResult());
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });

        return call;
    }


    public static void follow(String id, final DataSource.Callback<UserCard> callback) {
        RemoteService service = Network.remote();

        Call<RspModel<UserCard>> call = service.userFollow(id);
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel.success()) {
                    UserCard userCard = rspModel.getResult();
                    Factory.getUserCenter().dispatch(userCard);
//                    User user = userCard.build();
//                    DbHelper.save(User.class, user);
//                    user.save();
                    callback.onDataLoaded(userCard);
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });

    }

    public static void refreshContacts() {
        RemoteService service = Network.remote();

        service.userContacts().enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> rspModel = response.body();
                if (rspModel.success()) {
                    List<UserCard> cards = rspModel.getResult();
                    if (cards == null || cards.size() == 0) {
                        return;
                    }
//                    callback.onDataLoaded(rspModel.getResult());
                    Factory.getUserCenter().dispatch(CollectionUtil.toArray(cards, UserCard.class));
                } else {
                    Factory.decodeRspCode(rspModel, null);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
//                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    public static User findFromLocal(String id) {
        return SQLite.select()
                .from(User.class)
                .where(User_Table.id.eq(id))
                .querySingle();

    }

    public static User findFromNet(String id) {
        RemoteService remoteService = Network.remote();
        try {
            Response<RspModel<UserCard>> response = remoteService.userFind(id).execute();
            UserCard card = response.body().getResult();
            if (card != null) {
                User user = card.build();
//                user.save();
//                DbHelper.save(User.class, user);
                Factory.getUserCenter().dispatch(card);
                return user;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static User search(String id) {
        User user = findFromLocal(id);
        if (user == null) {
            return findFromNet(id);
        }
        return user;
    }

    public static User searchFirstOfNet(String id) {
        User user = findFromNet(id);
        if (user == null) {
            return findFromLocal(id);
        }
        Log.d("UserHelper", "searchFirstOfNet: " + user.toString());
        return user;
    }
}
