package com.plaidmrdeer.silentvoice.events;

import com.plaidmrdeer.silentvoice.SilentVoice;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

/**
 * @author PlaidMrdeer
 */
public class MailBoxGuiListener implements Listener, InventoryHolder {
    private final SilentVoice instance;
    public MailBoxGuiListener(final SilentVoice silentVoice) {
        instance = silentVoice;
    }
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory == null) {
            return;
        }
        if (!(inventory.getHolder() instanceof MailBoxGuiListener)) {
            return;
        }
        if (inventory.getType() != InventoryType.CHEST) {
            return;
        }
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();

        switch (slot) {
            case 2:
                player.closeInventory();
                instance.sendMessage(player, "message_mailbox_write");
                break;
            case 6:
                player.closeInventory();
                instance.sendMessage(player, "message_mailbox_view");
                break;
            default:
                break;
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return null;
    }
}
