package com.plaidmrdeer.silentvoice.commands;

import com.plaidmrdeer.silentvoice.SilentVoice;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author PlaidMrdeer
 */
public class SilentVoiceCommand implements CommandExecutor {
    private final SilentVoice instance;
    public SilentVoiceCommand(final SilentVoice silentVoice) {
        instance = silentVoice;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {

        String silentVoicePermissions = "SilentVoice.command.silentvoice";
        String silentVoiceViewPermissions = "SilentVoice.command.silentvoice.view";
        String silentVoiceWritePermissions = "SilentVoice.command.silentvoice.write";
        String noPermissionPath = "no_permissions";
        int subLength = 2;

        if (!sender.hasPermission(silentVoicePermissions)) {
            instance.sendMessage (sender, noPermissionPath);
            return true;
        }

        if (!(sender instanceof Player)) {
            instance.sendMessage (sender, noPermissionPath);
            return true;
        }

        try {
            if (args.length >= subLength) {
                if (!sender.hasPermission(silentVoiceWritePermissions)) {
                    instance.sendMessage (sender, noPermissionPath);
                    return true;
                }
                LocalDateTime dateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = dateTime.format(formatter);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 1; i <= args.length - 1; i++) {
                                sb.append(args[i]).append(" ");
                            }
                            String message = sb.toString().trim();
                            if ("sqlite".equals (instance.config.getString("sql.type"))) {
                                instance.getSqlite().createTable(args[0]);
                                instance.getSqlite().insert(args[0], message, formattedDateTime);
                            } else {
                                instance.getMySql().createTable(args[0]);
                                instance.getMySql().insert(args[0], message, formattedDateTime);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        instance.sendMessage(sender, "message_complete");
                        cancel();
                    }
                }.runTaskAsynchronously(instance);
                return true;
            }

            if (args.length == subLength - 1) {
                if (!sender.hasPermission(silentVoiceViewPermissions)) {
                    instance.sendMessage (sender, noPermissionPath);
                    return true;
                }
                instance.getMessageGui().messageGui((Player) sender, args[0]);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        instance.getMailBoxGui().mailBoxGui((Player) sender);
        return true;
    }
}
