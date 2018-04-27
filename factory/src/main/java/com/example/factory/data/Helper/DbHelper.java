package com.example.factory.data.Helper;

import com.example.factory.model.db.AppDatabase;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.GroupMember;
import com.example.factory.model.db.Group_Table;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.Session;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DbHelper {
    private static final DbHelper instance;

    static {
        instance = new DbHelper();
    }

    private DbHelper() {

    }

    private final Map<Class<?>, Set<ChangedListener>> changedListeners = new HashMap<>();

    private <Model extends BaseModel> Set<ChangedListener> getListeners(Class<Model> modelClass) {
        if (changedListeners.containsKey(modelClass)) {
            return changedListeners.get(modelClass);
        }
        return null;
    }

    public static <Model extends BaseModel> void addChangedListener(final Class<Model> tClass, ChangedListener<Model> listener) {
        Set<ChangedListener> changedListeners = instance.getListeners(tClass);
        if (changedListeners == null) {
            changedListeners = new HashSet<>();

            instance.changedListeners.put(tClass, changedListeners);
        }
        changedListeners.add(listener);
    }

    public static <Model extends BaseModel> void removeChangedListener(final Class<Model> tClass, ChangedListener<Model> listener) {
        Set<ChangedListener> changedListeners = instance.getListeners(tClass);
        if (changedListeners == null) {
            return;
        }

        changedListeners.remove(listener);
    }

    public static <Model extends BaseModel> void save(final Class<Model> tClass, final Model... models) {
        if (models == null || models.length == 0) {
            return;
        }
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Model> adapter = FlowManager.getModelAdapter(tClass);
                adapter.saveAll(Arrays.asList(models));
            }
        }).build()
                .execute();

        instance.notifySave(tClass, models);
    }

    public static <Model extends BaseModel> void delete(final Class<Model> tClass, final Model... models) {
        if (models == null || models.length == 0) {
            return;
        }
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Model> adapter = FlowManager.getModelAdapter(tClass);
                adapter.deleteAll(Arrays.asList(models));
            }
        }).build()
                .execute();
        instance.notifyDelete(tClass, models);
    }

    private final <Model extends BaseModel> void notifySave(final Class<Model> tClass, final Model... models) {
        final Set<ChangedListener> listeners = getListeners(tClass);
        if (listeners != null && listeners.size() > 0) {
            for (ChangedListener<Model> listener : listeners) {
                listener.onDataSave(models);
            }
        }
        if (GroupMember.class.equals(tClass)) {
            updateGroup((GroupMember[]) models);
        } else if (Message.class.equals(tClass)) {
            updateSession((Message[]) models);
        }
    }

    private final <Model extends BaseModel> void notifyDelete(final Class<Model> tClass, final Model... models) {
        final Set<ChangedListener> listeners = getListeners(tClass);
        if (listeners != null && listeners.size() > 0) {
            for (ChangedListener listener : listeners) {
                listener.onDataDelete(models);
            }
        }
    }

    private void updateGroup(GroupMember... members) {
        final Set<String> groupIds = new HashSet<>();
        for (GroupMember member : members) {
            groupIds.add(member.getGroup().getId());
        }

        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                List<Group> groups = SQLite.select()
                        .from(Group.class)
                        .where(Group_Table.id.in(groupIds))
                        .queryList();
                instance.notifySave(Group.class, groups.toArray(new Group[0]));
//                ModelAdapter<Group> adapter = FlowManager.getModelAdapter()
            }
        }).build().execute();
    }

    private void updateSession(Message... messages) {
        final Set<Session.Identify> identifies = new HashSet<>();
        for (Message message : messages) {
            Session.Identify identify = Session.createSessionIdentify(message);
            identifies.add(identify);
        }
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Session> adapter = FlowManager.getModelAdapter(Session.class);
                Session[] sessions = new Session[identifies.size()];
                int index = 0;
                for (Session.Identify identify : identifies) {
                    Session session = SessionHelper.findFromLocal(identify.id);
                    if (session == null) {
                        session = new Session(identify);
                    }
                    session.refreshToNow();
                    adapter.save(session);
                    sessions[index++] = session;
                }
                instance.notifySave(Session.class, sessions);

            }
        }).build().execute();
    }

    public interface ChangedListener<Data extends BaseModel> {
        void onDataSave(Data... list);

        void onDataDelete(Data... list);
    }
}
