package net.valneas.account.permission;

import java.util.List;

/**
 * @author Azodox_ (Luke)
 * 25/6/2022.
 */

public class VelocityPermission extends AbstractPermission {

    public VelocityPermission(String permission, boolean isDefault, List<String> playersIds, List<Integer> ranksIds, List<String> exceptIds) {
        super(permission, isDefault, playersIds, ranksIds, exceptIds);
    }
}
