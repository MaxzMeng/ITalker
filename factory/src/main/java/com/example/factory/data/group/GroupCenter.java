package com.example.factory.data.group;

import com.example.factory.model.card.GroupCard;
import com.example.factory.model.card.GroupMemberCard;

public interface GroupCenter {
    void dispatch(GroupCard... cards);

    void dispatch(GroupMemberCard... cards);
}
