package net.valneas.account.events;

import net.valneas.account.AbstractAccountManager;
import org.bukkit.event.Event;

public abstract class AccountEvent extends Event {

    protected AbstractAccountManager account;

    public AccountEvent(AbstractAccountManager account) {
        this.account = account;
    }

    public AccountEvent(boolean isAsync, AbstractAccountManager account) {
        super(isAsync);
        this.account = account;
    }

    public final AbstractAccountManager getAccount() {
        return account;
    }
}
