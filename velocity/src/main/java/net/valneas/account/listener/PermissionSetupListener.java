package net.valneas.account.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.permission.PermissionsSetupEvent;
import com.velocitypowered.api.proxy.Player;
import net.valneas.account.VelocityAccountSystem;
import net.valneas.account.permission.AccountPermissionProvider;

/**
 * @author Azodox_ (Luke)
 * 18/6/2022.
 */

public class PermissionSetupListener {

    private final VelocityAccountSystem accountSystem;

    public PermissionSetupListener(VelocityAccountSystem accountSystem) {
        this.accountSystem = accountSystem;
    }

    @Subscribe
    public void onSetup(PermissionsSetupEvent event){
        if(!(event.getSubject() instanceof Player))
            return;

        event.setProvider(new AccountPermissionProvider(accountSystem, event.getProvider()));
    }
}
