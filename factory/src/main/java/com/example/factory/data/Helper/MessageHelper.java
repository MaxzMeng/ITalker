package com.example.factory.data.Helper;

import com.example.factory.Factory;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.message.MsgCreateModel;
import com.example.factory.model.card.MessageCard;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.Message_Table;
import com.example.factory.net.Network;
import com.example.factory.net.RemoteService;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageHelper {
    public static Message findFromLocal(String id) {
        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.id.eq(id))
                .querySingle();
    }

    public static void push(final MsgCreateModel model) {
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                Message message = findFromLocal(model.getId());
                if (message != null && message.getStatus() != Message.STATUS_FAILED) {
                    return;
                }
                final MessageCard card = model.buildCard();
                Factory.getMessageCenter().dispatch(card);
                RemoteService service = Network.remote();
                service.msgPush(model).enqueue(new Callback<RspModel<MessageCard>>() {
                    @Override
                    public void onResponse(Call<RspModel<MessageCard>> call, Response<RspModel<MessageCard>> response) {
                        RspModel<MessageCard> rspModel = response.body();
                        if (rspModel != null && rspModel.success()) {
                            MessageCard rspCard = rspModel.getResult();
                            if (rspCard != null) {
                                Factory.getMessageCenter().dispatch(rspCard);
                            }
                        } else {
                            Factory.decodeRspCode(rspModel, null);
                            onFailure(call, null);
                        }
                    }

                    @Override
                    public void onFailure(Call<RspModel<MessageCard>> call, Throwable t) {
                        card.setStatus(Message.STATUS_FAILED);
                        Factory.getMessageCenter().dispatch(card);
                    }
                });
            }
        });
    }
}
