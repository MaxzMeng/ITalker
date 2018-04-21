package com.example.factory.data.user;

import com.example.factory.model.card.UserCard;

public interface UserCenter {
    void dispatch(UserCard... cards);
}
