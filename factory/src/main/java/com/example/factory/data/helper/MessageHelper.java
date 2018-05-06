package com.example.factory.data.helper;

import android.os.SystemClock;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.factory.Factory;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.message.MsgCreateModel;
import com.example.factory.model.card.MessageCard;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.Message_Table;
import com.example.factory.net.Network;
import com.example.factory.net.RemoteService;
import com.example.factory.net.UploadHelper;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.File;

import me.maxandroid.common.Common;
import me.maxandroid.common.app.Application;
import me.maxandroid.utils.PicturesCompressor;
import me.maxandroid.utils.StreamUtil;
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

                if (card.getType() != Message.TYPE_STR) {
                    if (!card.getContent().startsWith(UploadHelper.ENDPOINT)) {
                        String content;
                        switch (card.getType()) {
                            case Message.TYPE_PIC:
                                content = uploadPicture(card.getContent());
                                break;
                            default:
                                content = "";
                                break;
                        }
                        if (TextUtils.isEmpty(content)) {
                            card.setStatus(Message.STATUS_FAILED);
                            Factory.getMessageCenter().dispatch(card);
                        }
                        card.setContent(content);
                        Factory.getMessageCenter().dispatch(card);
                        model.refreshByCard();

                    }
                }
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

    private static String uploadPicture(String path) {
        File file = null;
        try {
            file = Glide.with(Factory.app())
                    .load(path)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file != null) {
            String cacheDir = Application.getCacheDirFile().getAbsolutePath();
            String tempFile = String.format("%s/image/Cache_%s.png", cacheDir, SystemClock.uptimeMillis());
            try {
                if (PicturesCompressor.compressImage(file.getAbsolutePath(), tempFile, Common.Constance.MAX_UPLOAD_IMAGE_LENGTH)) {
                    String ossPath = UploadHelper.uploadImage(tempFile);
                    StreamUtil.delete(tempFile);
                    return ossPath;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String uploadAudio(String content) {
        return null;
    }

    public static Message findLastWithGroup(String groupId) {
        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.group_id.eq(groupId))
                .orderBy(Message_Table.createAt, false)
                .querySingle();
    }

    public static Message findLastWithUser(String userId) {
        return SQLite.select()
                .from(Message.class)
                .where(OperatorGroup.clause()
                        .and(Message_Table.sender_id.eq(userId))
                        .and(Message_Table.group_id.isNull()))
                .or(Message_Table.receiver_id.eq(userId))
                .orderBy(Message_Table.createAt, false) // 倒序查询
                .querySingle();
    }
}
