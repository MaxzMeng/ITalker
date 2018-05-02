package com.example.factory.net;

import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.account.AccountRspModel;
import com.example.factory.model.api.account.LoginModel;
import com.example.factory.model.api.account.RegisterModel;
import com.example.factory.model.api.group.GroupCreateModel;
import com.example.factory.model.api.group.GroupMemberAddModel;
import com.example.factory.model.api.message.MsgCreateModel;
import com.example.factory.model.api.user.UserUpdateModel;
import com.example.factory.model.card.GroupCard;
import com.example.factory.model.card.GroupMemberCard;
import com.example.factory.model.card.MessageCard;
import com.example.factory.model.card.UserCard;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by mxz on 18-3-17.
 */

public interface RemoteService {
    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);

    @POST("account/login")
    Call<RspModel<AccountRspModel>> accountLogin(@Body LoginModel model);

    @POST("account/bind/{pushId}")
    Call<RspModel<AccountRspModel>> accountBind(@Path(encoded = true, value = "pushId") String pushId);

    @PUT("user")
    Call<RspModel<UserCard>> userUpdate(@Body UserUpdateModel model);

    @GET("user/search/{name}")
    Call<RspModel<List<UserCard>>> userSearch(@Path("name") String name);

    @PUT("user/follow/{userId}")
    Call<RspModel<UserCard>> userFollow(@Path("userId") String userId);

    @GET("user/contact")
    Call<RspModel<List<UserCard>>> userContacts();

    @GET("user/{userId}")
    Call<RspModel<UserCard>> userFind(@Path("userId") String userId);

    @POST("msg")
    Call<RspModel<MessageCard>> msgPush(@Body MsgCreateModel model);

    @POST("group")
    Call<RspModel<GroupCard>> groupCreate(@Body GroupCreateModel model);

    @GET("group/{groupId}")
    Call<RspModel<GroupCard>> groupFind(@Path("groupId") String groupId);

    @GET("group/search/{name}")
    Call<RspModel<List<GroupCard>>> groupSearch(@Path(value = "name", encoded = true) String name);

    @GET("group/list/{date}")
    Call<RspModel<List<GroupCard>>> groups(@Path(value = "date", encoded = true) String date);

    @GET("group/{groupId}/member")
    Call<RspModel<List<GroupMemberCard>>> groupMembers(@Path("groupId") String groupId);

    @POST("group/{groupId}/member")
    Call<RspModel<List<GroupMemberCard>>> groupMemberAdd(@Path("groupId") String groupId,
                                                         @Body GroupMemberAddModel model);
}
