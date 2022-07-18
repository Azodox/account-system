package net.valneas.account.permission;

import com.velocitypowered.api.permission.PermissionFunction;
import com.velocitypowered.api.permission.PermissionProvider;
import com.velocitypowered.api.permission.PermissionSubject;
import net.valneas.account.VelocityAccountSystem;

/**
 * @author Azodox_ (Luke)
 * 18/6/2022.
 */

public class AccountPermissionProvider implements PermissionProvider {

    private final VelocityAccountSystem accountSystem;
    private final PermissionProvider delegate;

    public AccountPermissionProvider(VelocityAccountSystem accountSystem, PermissionProvider delegate) {
        this.accountSystem = accountSystem;
        this.delegate = delegate;
    }

    @Override
    public PermissionFunction createFunction(PermissionSubject subject) {
        return new AccountPermissionFunction(accountSystem, subject, delegate.createFunction(subject));
    }
}
