package com.plaidmrdeer.silentvoice;

import com.plaidmrdeer.silentvoice.commands.SilentVoiceCommand;
import com.plaidmrdeer.silentvoice.commands.SilentVoiceReloadCommand;
import com.plaidmrdeer.silentvoice.events.MailBoxGuiListener;
import com.plaidmrdeer.silentvoice.events.MessageGuiListener;
import com.plaidmrdeer.silentvoice.gui.MailBoxGui;
import com.plaidmrdeer.silentvoice.gui.MessageGui;
import com.plaidmrdeer.silentvoice.language.LanguageSetting;
import com.plaidmrdeer.silentvoice.sql.MySql;
import com.plaidmrdeer.silentvoice.sql.Sqlite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

/**
 * @author PlaidMrdeer
 */
public final class SilentVoice extends JavaPlugin {
    private final SilentVoice instance = this;
    private LanguageSetting languageSetting;
    private MailBoxGui mailBoxGui;
    private Sqlite sqlite;
    private MySql mySql;
    private MessageGui messageGui;
    public FileConfiguration config;

    private void pluginLogo() {
        String logo1 = "&b  ____  _ _            _ __     __    _          ";
        String logo2 = "&b / ___|(_) | ___ _ __ | |\\ \\   / /__ (_) ___ ___ ";
        String logo3 = "&b \\___ \\| | |/ _ \\ '_ \\| __\\ \\ / / _ \\| |/ __/ _ \\";
        String logo4 = "&b  ___) | | |  __/ | | | |_ \\ V / (_) | | (_|  __/";
        String logo5 = "&b |____/|_|_|\\___|_| |_|\\__| \\_/ \\___/|_|\\___\\___|";
        CommandSender sender = Bukkit.getConsoleSender();

        sender.sendMessage(setStyle(logo1));
        sender.sendMessage(setStyle(logo2));
        sender.sendMessage(setStyle(logo3));
        sender.sendMessage(setStyle(logo4));
        sender.sendMessage(setStyle(logo5));
    }

    public void sendMessage(CommandSender sender, String path) {
        String message = languageSetting.language.getString(path);

        sender.sendMessage(setStyle(message));
    }

    public String setStyle(String message) {
        message = message.replace("%prefix%", config.getString("prefix"));
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private void registerCommands() {
        getCommand("silentvoicereload").setExecutor(
                new SilentVoiceReloadCommand(instance)
        );

        getCommand("silentvoice").setExecutor(new SilentVoiceCommand(instance));

    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new MailBoxGuiListener(instance), this);
        getServer().getPluginManager().registerEvents(new MessageGuiListener(instance), this);
    }

    public void reloadPluginConfig(CommandSender sender) {
        reloadConfig();
        config = getConfig();
        languageSetting.loadLang();
        connectSql(sender);
    }

    public MailBoxGui getMailBoxGui() {
        return mailBoxGui;
    }

    public LanguageSetting getLanguageSetting() {
        return languageSetting;
    }

    public MessageGui getMessageGui() {
        return messageGui;
    }

    public Sqlite getSqlite() {
        return sqlite;
    }

    public MySql getMySql() {
        return mySql;
    }

    public void connectSql(CommandSender sender) {
        try {
            if ("sqlite".equals(config.getString("sql.type"))) {
                sqlite.connectSqlite();
            } else {
                String host = config.getString("sql.mysql.host");
                String port = config.getString("sql.mysql.port");
                String database = config.getString("sql.mysql.database");
                String username = config.getString("sql.mysql.username");
                String password = config.getString("sql.mysql.password");
                mySql.connectMySql(host, port, database, username, password);
            }
            sendMessage(sender, "sql_connect_success");
        } catch (Exception e) {
            sendMessage(sender, "sql_connect_failed");
            getServer().getPluginManager().disablePlugin(instance);
        }
    }

    @Override
    public void onLoad() {
        languageSetting = new LanguageSetting(instance);
        mailBoxGui = new MailBoxGui(instance);
        sqlite = new Sqlite();
        mySql = new MySql();
        messageGui = new MessageGui(instance);
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        config = getConfig();

        if (config.getBoolean("logo")) {
            pluginLogo();
        }

        languageSetting.loadLang();

        registerCommands();
        registerEvents();

        connectSql(Bukkit.getConsoleSender());
    }

    @Override
    public void onDisable() {
        try {
            if ("sqlite".equals(config.getString("sql.type"))) {
                sqlite.closeSqlite();
                return;
            }
            mySql.closeMySql();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
