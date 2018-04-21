package com.example.factory.data.message;

import com.example.factory.model.card.MessageCard;

public interface MessageCenter {
    void dispatch(MessageCard... cards);
}
