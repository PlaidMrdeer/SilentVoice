package com.plaidmrdeer.silentvoice.commands;

import com.plaidmrdeer.silentvoice.SilentVoice;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * @author PlaidMrdeer
 */
public class SilentVoiceReloadCommand implements CommandExecutor {
    private final SilentVoice instance;
    public SilentVoiceReloadCommand(final SilentVoice silentVoice) {
        instance = silentVoice;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {

        String silentVoiceReloadPermissions = "SilentVoice.command.silentvoicereload";
        String noPermissionPath = "no_permissions";
        String commandErrorPath = "command_error";
        String commandReloadPath = "command_reload";
        int subLength = 0;

        if (!sender.hasPermission(silentVoiceReloadPermissions)) {
            instance.sendMessage (sender, noPermissionPath);
            return true;
        }

        if (subLength != args.length) {
            instance.sendMessage (sender, commandErrorPath);
            return true;
        }

        instance.reloadPluginConfig(sender);
        instance.sendMessage (sender, commandReloadPath);
        return true;
    }
}
