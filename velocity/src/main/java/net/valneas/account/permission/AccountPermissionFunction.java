package net.valneas.account.permission;

import com.velocitypowered.api.permission.PermissionFunction;
import com.velocitypowered.api.permission.PermissionSubject;
import com.velocitypowered.api.permission.Tristate;
import net.valneas.account.VelocityAccountSystem;

/**
 * @author Azodox_ (Luke)
 * 18/6/2022.
 */

public class AccountPermissionFunction implements PermissionFunction {

    private final VelocityAccountSystem accountSystem;
    private final PermissionSubject subject;
    private final PermissionFunction delegate;

    public AccountPermissionFunction(VelocityAccountSystem accountSystem, PermissionSubject subject, PermissionFunction delegate) {
        this.accountSystem = accountSystem;
        this.subject = subject;
        this.delegate = delegate;
    }

    @Override
    public Tristate getPermissionValue(String permission) {
        return this.accountSystem.getPermissionDatabase().getPermissionValue(subject, permission, this.delegate);
    }
}
