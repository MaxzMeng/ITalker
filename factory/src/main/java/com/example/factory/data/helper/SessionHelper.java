package com.example.factory.data.helper;

import com.example.factory.model.db.Session;
import com.example.factory.model.db.Session_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

public class SessionHelper {
    public static Session findFromLocal(String id) {
        return SQLite.select()
                .from(Session.class)
                .where(Session_Table.id.eq(id))
                .querySingle();
    }
}
