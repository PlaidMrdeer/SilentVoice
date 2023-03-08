package com.plaidmrdeer.silentvoice.gui;

import com.plaidmrdeer.silentvoice.SilentVoice;
import com.plaidmrdeer.silentvoice.events.MailBoxGuiListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author PlaidMrdeer
 */
public class MailBoxGui {
    private final SilentVoice instance;
    public MailBoxGui(SilentVoice silentVoice) {
        instance = silentVoice;
    }

    public void mailBoxGui(Player player) {
        Inventory inventory = Bukkit.createInventory(
                new MailBoxGuiListener(instance),
                9,
                instance.setStyle(instance.getLanguageSetting().language.getString("message_mailbox_title")));
        ItemStack itemStack;
        ItemMeta itemMeta;

        itemStack = new ItemStack(Material.ENCHANTED_BOOK);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(instance.setStyle(instance.getLanguageSetting().language.getString("view_message")));
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(2, itemStack);

        itemStack = new ItemStack(Material.NAME_TAG);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(instance.setStyle(instance.getLanguageSetting().language.getString("message_mailbox")));
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(4, itemStack);

        itemStack = new ItemStack(Material.WRITABLE_BOOK);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(instance.setStyle(instance.getLanguageSetting().language.getString("write_message")));
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(6, itemStack);

        player.openInventory(inventory);
    }
}
