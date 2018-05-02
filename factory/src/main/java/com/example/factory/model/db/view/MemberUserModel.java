package com.example.factory.model.db.view;

import com.example.factory.model.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;

@QueryModel(database = AppDatabase.class)
public class MemberUserModel {
    @Column
    public String userId;
    @Column
    public String name;
    @Column
    public String alias;
    @Column
    public String portrait;
}
