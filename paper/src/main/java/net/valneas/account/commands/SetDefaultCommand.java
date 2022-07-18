package net.valneas.account.commands;

import io.github.llewvallis.commandbuilder.CommandContext;
import io.github.llewvallis.commandbuilder.ExecuteCommand;
import net.valneas.account.PaperAccountSystem;
import net.valneas.account.permission.PaperPermission;

public class SetDefaultCommand {

    private final PaperAccountSystem accountSystem;

    public SetDefaultCommand(PaperAccountSystem accountSystem) {
        this.accountSystem = accountSystem;
    }

    @ExecuteCommand
    public void setDefault(CommandContext ctx, PaperPermission permission, boolean value) {
        if(value){
            this.accountSystem.getPermissionDispatcher().setDefault(permission.getPermission());
            ctx.getSender().sendMessage("'" + permission.getPermission() + "' définie comme par défaut ✔");
        }else{
            this.accountSystem.getPermissionDispatcher().unsetDefault(permission.getPermission());
            ctx.getSender().sendMessage("'" + permission.getPermission() + "' définie comme non par défaut ✔");
        }
    }
}
