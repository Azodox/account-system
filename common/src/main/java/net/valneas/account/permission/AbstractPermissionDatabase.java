package net.valneas.account.permission;

import dev.morphia.Datastore;
import dev.morphia.query.Query;
import dev.morphia.query.experimental.filters.Filters;

import java.util.List;

/**
 * @author Azodox_ (Luke)
 * 19/6/2022.
 */

public abstract class AbstractPermissionDatabase<T extends AbstractPermission> {

    protected final Datastore datastore;
    protected final Class<T> clazz;

    public AbstractPermissionDatabase(Datastore datastore, Class<T> clazz) {
        this.datastore = datastore;
        this.clazz = clazz;
    }

    public void init(T permission){
        datastore.save(permission);
    }

    public T get(String permission) {
        return getAsQuery(permission).first();
    }

    public Query<T> getAsQuery(String permission){
        return datastore.find(this.clazz).filter(Filters.eq("permission", permission)).count() == 0 ?
                datastore.find(this.clazz).filter(Filters.eq("permission", "-" + permission)) :
                datastore.find(this.clazz).filter(Filters.eq("permission", permission));
    }

    public List<T> getPermissions(){
        return datastore.find(clazz).stream().toList();
    }
}
