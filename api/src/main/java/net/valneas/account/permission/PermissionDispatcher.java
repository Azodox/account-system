package net.valneas.account.permission;

import com.mongodb.client.result.UpdateResult;
import dev.morphia.query.Query;

/**
 * @author Azodox_ (Luke)
 * 15/7/2022.
 */

public interface PermissionDispatcher {

    void onEnable();
    void reloadPermissions();
    void reloadPermissions(Object target);
    void setDefault(String permission);
    void unsetDefault(String permission);
    <T> void set(Object target, T permission, String field, Object value);
    void set(Object target, String permission, String field, Object value);
    <T> void set(T player);
    UpdateResult addException(Object exception, String permission);
    UpdateResult removeException(Object exception, String permission);
    UpdateResult addPermissionToObject(Object target, String permission);
    UpdateResult removePermissionFromObject(Object target, String permission);
    <T> UpdateResult addAccordingToObject(Object target, Query<T> query);
    <T> UpdateResult removeAccordingToObject(Object target, Query<T> query);
}
