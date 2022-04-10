package net.valneas.account.api.commands;

import io.github.llewvallis.commandbuilder.CommandContext;
import io.github.llewvallis.commandbuilder.ExecuteCommand;
import net.valneas.account.AccountSystem;
import net.valneas.account.permission.PermissionDatabase;

public class SetDefaultCommand {

    private AccountSystem accountSystem;

    public SetDefaultCommand(AccountSystem accountSystem) {
        this.accountSystem = accountSystem;
    }

    @ExecuteCommand
    public void setDefault(CommandContext ctx, PermissionDatabase.Permission permission, boolean value) {
        if(value){
            this.accountSystem.getPermissionDispatcher().setDefault(permission.permission());
            ctx.getSender().sendMessage("'" + permission.permission() + "' définie comme par défaut ✔");
        }else{
            this.accountSystem.getPermissionDispatcher().unsetDefault(permission.permission());
            ctx.getSender().sendMessage("'" + permission.permission() + "' définie comme non par défaut ✔");
        }
    }
}
