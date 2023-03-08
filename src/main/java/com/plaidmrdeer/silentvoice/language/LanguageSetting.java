package com.plaidmrdeer.silentvoice.language;

import com.plaidmrdeer.silentvoice.SilentVoice;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author PlaidMrdeer
 */
public class LanguageSetting {
    private final SilentVoice instance;

    public FileConfiguration language;
    public LanguageSetting(SilentVoice silentVoice) {
        instance = silentVoice;
    }

    public void loadLang() {
        File langDir = new File(instance.getDataFolder(), "language");
        if (!langDir.exists()) {
            langDir.mkdir();
        }
        List<String> languageList = Arrays.asList(
                "english.yml", "chinese.yml", "german.yml"
        );

        for (String language : languageList) {
            File file = new File(langDir, language);
            if (!file.exists()) {
                instance.saveResource("language" + File.separator + language, false);
            }
        }

        File file = new File(langDir, instance.config.getString("language") + ".yml");
        if (!file.exists()) {
            Bukkit.getLogger().warning("Unknown language file!The plugin has been uninstalled.");
            Bukkit.getPluginManager().disablePlugin(instance);
            return;
        }

        language = YamlConfiguration.loadConfiguration(file);
    }
}
